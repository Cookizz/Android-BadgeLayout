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
 * Badge layout internal visitor
 * Created by Cookizz on 2016/1/29.
 */
class BadgeLayoutVisitor implements BadgeManager, BadgeObserver {

    private ViewGroup element;

    private BadgeStyleFactory badgeStyleFactory;
    private ViewTreeObserver viewTreeObserver;

    private int preDrawFilter = 0; // put limitations on frequency of redrawing
    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if(!viewTreeObserver.isAlive()) { // keep observer to be alive
                viewTreeObserver = element.getViewTreeObserver();
            }
            if(preDrawFilter++ % 2 == 0) {
                SparseArray<Rect> rects = assembleRects();
                badgeOverlay.feedRects(rects);
                restoreOverlay();
            }
            return true;
        }
    };

    private BadgeOverlay badgeOverlay;
    private ViewGroup.LayoutParams badgeOverlayLayoutParams;

    private SparseArray<View> targetViews; // target view cache
    private int mockId = -1; // used for offer ids to those target views who are lack of id

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
        // revoke observer
        viewTreeObserver = element.getViewTreeObserver();
        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(mOnPreDrawListener);
        }
    }

    public void onDetachFromWindow() {
        // remove observer
        viewTreeObserver = element.getViewTreeObserver();
        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnPreDrawListener(mOnPreDrawListener);
        }
    }

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
                    calculateIntrinsicHitRect(childView, outRect, mutable, id);
                    if(!outRect.isEmpty()) {
                        rects.put(id, outRect);
                    }
                }
            }
        }
        return rects;
    }

    /**
     * used for positioning the drawing area of the given targetView
     * @param outRect rectangle drawing area to output
     */
    private void calculateIntrinsicHitRect(View childView, Rect outRect, BadgeMutable mutable, int id) {

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

        // retrieve towards root, until meeting BadgeLayout
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

        // clear cache
        badgeOverlay.removeBadgeMutable(id);
        targetViews.remove(id);
        outRect.setEmpty();
    }

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
