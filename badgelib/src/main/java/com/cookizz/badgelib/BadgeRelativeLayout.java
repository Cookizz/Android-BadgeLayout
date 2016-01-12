package com.cookizz.badgelib;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.cookizz.badgelib.core.BadgeManager;
import com.cookizz.badgelib.core.BadgeObserver;
import com.cookizz.badgelib.core.mutable.BadgeMutable;
import com.cookizz.badgelib.core.style.AbsBadgeStyle;
import com.cookizz.badgelib.core.container.BadgeOverlay;
import com.cookizz.badgelib.core.style.DotStyle;
import com.cookizz.badgelib.core.style.FigureStyle;
import com.cookizz.badgelib.mutable.DotBadge;
import com.cookizz.badgelib.mutable.FigureBadge;
import com.cookizz.badgelib.core.style.BadgeStyleFactory;

/**
 * 角标相对布局
 * Created by dugd on 2015/9/13.
 */
public class BadgeRelativeLayout extends RelativeLayout implements BadgeManager, BadgeObserver {

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
    private int mockId = -1; // 假想的viewId，用来与无法提供id的view进行绑定

    // 临时计算用矩形
    private final Rect mTempRect = new Rect();

    public BadgeRelativeLayout(Context context) {
        this(context, null);
    }

    public BadgeRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mFactory = BadgeStyleFactory.getInstance(context);
        mObserver = getViewTreeObserver();

        mOverlay = new BadgeOverlay(context);
        mOverlayLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mTargetViews = new SparseArray<>();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BadgeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

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
        if (childView != null) {
            AbsBadgeStyle style = mFactory.createStyle(badgeStyle);
            mutable = new FigureBadge(style);
            mutable.setObserver(this);
            mOverlay.putBadgeMutable(viewId, mutable);
            mTargetViews.put(viewId, childView);
            mOverlay.invalidate();
        }
        return mutable;
    }

    @Override
    public FigureBadge createFigureBadge(View view, Class<? extends FigureStyle> badgeStyle) {

        AbsBadgeStyle style = mFactory.createStyle(badgeStyle);
        FigureBadge mutable = new FigureBadge(style);
        if (view == null || !isDescendant(view, this)) {
            return mutable;
        }
        mutable.setObserver(this);

        int mockId;
        int indexOfValue = mTargetViews.indexOfValue(view);
        if (indexOfValue == -1) {
            mockId = generateMockId();
        } else {
            mockId = mTargetViews.keyAt(indexOfValue);
        }
        mOverlay.putBadgeMutable(mockId, mutable);
        mTargetViews.put(mockId, view);
        mOverlay.invalidate();
        return mutable;
    }

    @Override
    public final DotBadge createDotBadge(int viewId, Class badgeStyle) {
        View childView = findViewById(viewId);
        DotBadge mutable = null;
        if (childView != null) {
            AbsBadgeStyle style = mFactory.createStyle(badgeStyle);
            mutable = new DotBadge(style);
            mutable.setObserver(this);
            mOverlay.putBadgeMutable(viewId, mutable);
            mTargetViews.put(viewId, childView);
            mOverlay.invalidate();
        }
        return mutable;
    }

    @Override
    public DotBadge createDotBadge(View view, Class<? extends DotStyle> badgeStyle) {

        AbsBadgeStyle style = mFactory.createStyle(badgeStyle);
        DotBadge mutable = new DotBadge(style);
        if (view == null || !isDescendant(view, this)) {
            return mutable;
        }
        mutable.setObserver(this);

        int mockId;
        int indexOfValue = mTargetViews.indexOfValue(view);
        if (indexOfValue == -1) {
            mockId = generateMockId();
        } else {
            mockId = mTargetViews.keyAt(indexOfValue);
        }
        mOverlay.putBadgeMutable(mockId, mutable);
        mTargetViews.put(mockId, view);
        mOverlay.invalidate();
        return mutable;
    }

    @Override
    public BadgeMutable findBadge(int viewId) {
        return mOverlay.getBadgeMutable(viewId);
    }

    @Override
    public BadgeMutable findBadge(View view) {
        int index = mTargetViews.indexOfValue(view);
        if (index == -1) {
            return null;
        }
        int mockId = mTargetViews.keyAt(index);
        return mOverlay.getBadgeMutable(mockId);
    }

    private boolean isDescendant(View child, View container) {
        if (child == null || container == null) {
            return false;
        }
        while (child != null) {
            View parent = (View) child.getParent();
            if (parent == container) {
                return true;
            }
            child = parent;
        }
        return false;
    }

    private int generateMockId() {
        return --mockId;
    }

    @Override
    public final void clearAllBadges() {

        mOverlay.clearBadgeMutables();
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
        SparseArray<BadgeMutable> mutables = mOverlay.getBadgeMutableCopies();

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
            mOverlay.removeBadgeMutable(id);
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
        mOverlay.removeBadgeMutable(id);
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
    public final void bringChildToFront(@NonNull View child) {
        super.bringChildToFront(child);
    }
}
