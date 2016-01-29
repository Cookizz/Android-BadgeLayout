package com.cookizz.badge;

import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.cookizz.badge.core.BadgeManager;
import com.cookizz.badge.core.BadgeObserver;
import com.cookizz.badge.core.container.BadgeOverlay;
import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.core.style.AbsBadgeStyle;
import com.cookizz.badge.core.style.BadgeStyleFactory;
import com.cookizz.badge.core.style.DotStyle;
import com.cookizz.badge.core.style.FigureStyle;
import com.cookizz.badge.mutable.DotBadge;
import com.cookizz.badge.mutable.FigureBadge;

/**
 * 角标布局访问者
 * Created by Cookizz on 2016/1/29.
 */
class BadgeLayoutVisitor implements BadgeManager, BadgeObserver {

    private ViewGroup element;

    private BadgeStyleFactory badgeStyleFactory;
    private ViewTreeObserver viewTreeObserver;

    // 限制重绘频率
    private int preDrawFilter = 0;
    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            // 随时恢复对新observer的引用，使其保持alive状态
            if(!viewTreeObserver.isAlive()) {
                viewTreeObserver = element.getViewTreeObserver();
            }
            // 避免overlay不断重绘
            if(preDrawFilter++ % 2 == 0) {
                SparseArray<Rect> rects = assembleRects();
                badgeOverlay.feedRects(rects);
                restoreOverlay();
            }
            return true;
        }
    };

    // 角标绘图表面和其布局参数
    private BadgeOverlay badgeOverlay;
    private ViewGroup.LayoutParams badgeOverlayLayoutParams;

    // 角标targetView缓存
    private SparseArray<View> targetViews;
    private int mockId = -1; // 假想的viewId，用来与无法提供id的view进行绑定

    // 临时计算用矩形
    private final Rect mTempRect = new Rect();

    public void visit(RelativeLayout relativeLayout) {
        element = relativeLayout;

        badgeStyleFactory = BadgeStyleFactory.getInstance(element.getContext());
        viewTreeObserver = element.getViewTreeObserver();

        badgeOverlay = new BadgeOverlay(element.getContext());
        badgeOverlayLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        targetViews = new SparseArray<>();
    }

    public void visit(FrameLayout frameLayout) {
        element = frameLayout;

        badgeStyleFactory = BadgeStyleFactory.getInstance(element.getContext());
        viewTreeObserver = element.getViewTreeObserver();

        badgeOverlay = new BadgeOverlay(element.getContext());
        badgeOverlayLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        targetViews = new SparseArray<>();
    }

    @Override
    public FigureBadge createFigureBadge(int viewId, Class<? extends FigureStyle> badgeStyle) {
        View childView = element.findViewById(viewId);
        FigureBadge mutable = null;
        if (childView != null) {
            AbsBadgeStyle style = badgeStyleFactory.createStyle(badgeStyle);
            mutable = new FigureBadge(style);
            mutable.setObserver(this);
            badgeOverlay.putBadgeMutable(viewId, mutable);
            targetViews.put(viewId, childView);
            badgeOverlay.invalidate();
        }
        return mutable;
    }

    @Override
    public FigureBadge createFigureBadge(View view, Class<? extends FigureStyle> badgeStyle) {
        AbsBadgeStyle style = badgeStyleFactory.createStyle(badgeStyle);
        FigureBadge mutable = new FigureBadge(style);
        if (view == null || !isDescendant(view, element)) {
            return mutable;
        }
        mutable.setObserver(this);

        int mockId;
        int indexOfValue = targetViews.indexOfValue(view);
        if (indexOfValue == -1) {
            mockId = generateMockId();
        } else {
            mockId = targetViews.keyAt(indexOfValue);
        }
        badgeOverlay.putBadgeMutable(mockId, mutable);
        targetViews.put(mockId, view);
        badgeOverlay.invalidate();
        return mutable;
    }

    @Override
    public DotBadge createDotBadge(int viewId, Class<? extends DotStyle> badgeStyle) {
        View childView = element.findViewById(viewId);
        DotBadge mutable = null;
        if (childView != null) {
            AbsBadgeStyle style = badgeStyleFactory.createStyle(badgeStyle);
            mutable = new DotBadge(style);
            mutable.setObserver(this);
            badgeOverlay.putBadgeMutable(viewId, mutable);
            targetViews.put(viewId, childView);
            badgeOverlay.invalidate();
        }
        return mutable;
    }

    @Override
    public DotBadge createDotBadge(View view, Class<? extends DotStyle> badgeStyle) {
        AbsBadgeStyle style = badgeStyleFactory.createStyle(badgeStyle);
        DotBadge mutable = new DotBadge(style);
        if (view == null || !isDescendant(view, element)) {
            return mutable;
        }
        mutable.setObserver(this);

        int mockId;
        int indexOfValue = targetViews.indexOfValue(view);
        if (indexOfValue == -1) {
            mockId = generateMockId();
        } else {
            mockId = targetViews.keyAt(indexOfValue);
        }
        badgeOverlay.putBadgeMutable(mockId, mutable);
        targetViews.put(mockId, view);
        badgeOverlay.invalidate();
        return mutable;
    }

    @Override
    public BadgeMutable findBadge(int viewId) {
        return badgeOverlay.getBadgeMutable(viewId);
    }

    @Override
    public BadgeMutable findBadge(View view) {
        int index = targetViews.indexOfValue(view);
        if (index == -1) {
            return null;
        }
        int mockId = targetViews.keyAt(index);
        return badgeOverlay.getBadgeMutable(mockId);
    }

    @Override
    public void clearAllBadges() {
        badgeOverlay.clearBadgeMutables();
        targetViews.clear();
        badgeOverlay.invalidate();
    }

    @Override
    public void onBadgeStateChange() {
        badgeOverlay.invalidate();
    }

    public void onAttachToWindow() {
        // 唤醒listeners
        viewTreeObserver = element.getViewTreeObserver();
        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(mOnPreDrawListener);
        }
    }

    public void onDetachFromWindow() {
        // 移除listeners
        viewTreeObserver = element.getViewTreeObserver();
        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnPreDrawListener(mOnPreDrawListener);
        }
    }

    /**
     * 收集当前需要绘制角标的目标矩形区域
     */
    private SparseArray<Rect> assembleRects() {

        SparseArray<Rect> rects = new SparseArray<>();
        SparseArray<BadgeMutable> mutables = badgeOverlay.getBadgeMutableCopies();

        final int size = mutables.size();
        for(int i = 0; i < size; i++) {
            int id = mutables.keyAt(i);
            BadgeMutable mutable = mutables.get(id);
            View childView = targetViews.get(id);
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
            badgeOverlay.removeBadgeMutable(id);
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
            if(element == currParent) {
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
        badgeOverlay.removeBadgeMutable(id);
        targetViews.remove(id);
        outRect.setEmpty();
    }

    /**
     * 恢复角标绘图表面
     */
    private void restoreOverlay() {

        badgeOverlay.setLayoutParams(badgeOverlayLayoutParams);
        ViewGroup parent = (ViewGroup) badgeOverlay.getParent();
        if(parent == null) {
            element.addView(badgeOverlay);
        }
        else if(parent != element) {
            parent.removeView(badgeOverlay);
            element.addView(badgeOverlay);
        }
        else {
            boolean atTop = parent.getChildAt(parent.getChildCount() - 1) == badgeOverlay;
            if(!atTop) {
                badgeOverlay.bringToFront();
            }
            else {
                badgeOverlay.invalidate();
            }
        }
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
}
