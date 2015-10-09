package com.cookizz.badgelib;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.cookizz.badgelib.core.BadgeManager;
import com.cookizz.badgelib.core.BadgeObserver;
import com.cookizz.badgelib.core.BadgeOverlay;
import com.cookizz.badgelib.core.mutable.BadgeMutable;
import com.cookizz.badgelib.core.style.AbsBadgeStyle;
import com.cookizz.badgelib.core.style.BadgeStyleFactory;

/**
 * 角标帧布局
 * Created by dugd on 2015/10/9.
 */
public class BadgeFrameLayout extends FrameLayout implements BadgeManager, BadgeObserver {

    private BadgeStyleFactory mFactory;
    private ViewTreeObserver mObserver;

    // 限制重绘频率
    private int preDrawFilter = 0;
    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            // 随时恢复对新observer的引用，使其保持alive状态
            if(!mObserver.isAlive()) {
                mObserver = getViewTreeObserver();
            }
            // 避免overlay不断重绘
            if(preDrawFilter++ % 2 == 0) {
                SparseArray<Rect> rects = assembleRects();
                mOverlay.feedRects(rects);
                restoreOverlay();
            }
            return true;
        }
    };

    // 角标绘图表面和其布局参数
    private final BadgeOverlay mOverlay;
    private final LayoutParams mOverlayLayoutParams;

    // 角标targetView缓存
    private final SparseArray<View> mTargetViews;

    // 临时计算用矩形
    private final Rect mTempRect = new Rect();

    public BadgeFrameLayout(Context context) {
        this(context, null);
    }

    public BadgeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mFactory = BadgeStyleFactory.getInstance(context);
        mObserver = getViewTreeObserver();

        mOverlay = new BadgeOverlay(context);
        mOverlayLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mTargetViews = new SparseArray<>();
    }

    @Override
    public final FigureBadge createFigureBadge(int viewId, Class badgeStyle) {

        View childView = findViewById(viewId);
        FigureBadge mutable = null;
        if(childView != null) {
            AbsBadgeStyle style = mFactory.createStyle(badgeStyle);
            mutable = new FigureBadge(style);
            mutable.setObserver(this);
            mOverlay.putMutable(viewId, mutable);
            mTargetViews.put(viewId, childView);
            mOverlay.invalidate();
        }
        return mutable;
    }

    @Override
    public final DotBadge createDotBadge(int viewId, Class badgeStyle) {
        View childView = findViewById(viewId);
        DotBadge mutable = null;
        if(childView != null) {
            AbsBadgeStyle style = mFactory.createStyle(badgeStyle);
            mutable = new DotBadge(style);
            mutable.setObserver(this);
            mOverlay.putMutable(viewId, mutable);
            mTargetViews.put(viewId, childView);
            mOverlay.invalidate();
        }
        return mutable;
    }

    @Override
    public final void clearAllBadges() {

        mOverlay.clearMutables();
        mTargetViews.clear();
        mOverlay.invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // 唤醒listeners
        mObserver = getViewTreeObserver();
        if(mObserver.isAlive()) {
            mObserver.addOnPreDrawListener(mOnPreDrawListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // 移除listeners
        mObserver = getViewTreeObserver();
        if(mObserver.isAlive()) {
            mObserver.removeOnPreDrawListener(mOnPreDrawListener);
        }
    }

    /**
     * 收集当前需要绘制角标的目标矩形区域
     */
    private SparseArray<Rect> assembleRects() {

        SparseArray<Rect> rects = new SparseArray<>();
        SparseArray<BadgeMutable> mutables = mOverlay.copyMutables();

        final int size = mutables.size();
        for(int i = 0; i < size; i++) {
            int id = mutables.keyAt(i);
            BadgeMutable mutable = mutables.get(id);
            View childView = mTargetViews.get(id);
            if(childView != null) {
                if(childView.getVisibility() == View.VISIBLE) {
                    Rect outRect = new Rect();
                    mTempRect.setEmpty();
                    calcuIntrinsicHitRect(childView, outRect, mutable, id);
                    if(!outRect.isEmpty()) {
                        rects.put(id, outRect);
                    }
                }
            }
        }
        return rects;
    }

    /**
     * 定位子View
     * @param childView 子View
     * @param outRect 方法的输出对象
     */
    private void calcuIntrinsicHitRect(View childView, Rect outRect, BadgeMutable mutable, int id) {

        // 如果mutable已经是detach状态，则从overlay中删除之，直接返回
        if(mutable != null && !mutable.isAttached()) {
            mOverlay.removeMutable(id);
            if(outRect != null) {
                outRect.setEmpty();
            }
            return;
        }

        if(outRect == null) {
            return;
        }

        if(childView == null) {
            outRect.setEmpty();
            return;
        }

        childView.getHitRect(outRect);

        int offsetX = 0, offsetY = 0;
        View currParent = (View) childView.getParent();

        // 向根布局回溯，直到找到角标布局
        while(currParent != null) {
            if(this == currParent) {
                offsetX -= currParent.getPaddingLeft();
                offsetY -= currParent.getPaddingTop();
                outRect.offset(offsetX, offsetY);
                return;
            }
            currParent.getHitRect(mTempRect);
            offsetX += mTempRect.left;
            offsetY += mTempRect.top;
            currParent = (View) currParent.getParent();
        }

        // 如果子View不在本布局中，清除该View的缓存
        mOverlay.removeMutable(id);
        mTargetViews.remove(id);
        outRect.setEmpty();
    }

    /**
     * 恢复角标绘图表面
     */
    private void restoreOverlay() {

        mOverlay.setLayoutParams(mOverlayLayoutParams);
        ViewGroup parent = (ViewGroup) mOverlay.getParent();
        if(parent == null) {
            addView(mOverlay);
        }
        else if(parent != this) {
            parent.removeView(mOverlay);
            addView(mOverlay);
        }
        else {
            boolean atTop = parent.getChildAt(parent.getChildCount() - 1) == mOverlay;
            if(!atTop) {
                mOverlay.bringToFront();
            }
            else {
                mOverlay.invalidate();
            }
        }
    }

    @Override
    public final void onBadgeStateChange() {
        mOverlay.invalidate();
    }

    /**
     * 下列方法禁止子类覆盖
     * @return
     */
    @Override
    public final int getChildCount() {
        return super.getChildCount();
    }

    @Override
    public final View getChildAt(int index) {
        return super.getChildAt(index);
    }

    @Override
    public final int indexOfChild(View child) {
        return super.indexOfChild(child);
    }

    @Override
    public final void bringChildToFront(View child) {
        super.bringChildToFront(child);
    }
}
