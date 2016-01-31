package com.cookizz.badge.core.container;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.cookizz.badge.core.mutable.AbsBadgeMutable;
import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.core.style.BadgeStyle;

/**
 * Overlay view in badge layout where your badges are to be drawn on
 * Created by Cookizz on 2015/9/13.
 */
public final class BadgeOverlay extends View implements BadgeContainer {

    private final SparseArray<BadgeMutable> mutables;
    private SparseArray<Rect> rects;

    public BadgeOverlay(Context context) {
        super(context);

        setEnabled(false);

        mutables = new SparseArray<>();
        rects = new SparseArray<>();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed) {
            try {
                ViewGroup.LayoutParams params = getLayoutParams();
                ViewGroup parent = (ViewGroup) getParent();
                final int expectedWidth = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
                final int expectedHeight = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
                if(params.width != expectedWidth || params.height != expectedHeight) {
                    params.width = expectedWidth;
                    params.height = expectedHeight;
                    setLayoutParams(params);
                }
            }
            catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    public void putBadgeMutable(int viewId, BadgeMutable mutable) {
        if(mutable != null && mutable instanceof AbsBadgeMutable) {
            AbsBadgeMutable absBadgeMutable = (AbsBadgeMutable) mutable;
            if(absBadgeMutable.authenticateContainer(this)) {
                BadgeMutable remaining = mutables.get(viewId);
                if(remaining != null) {
                    remaining.detach(false);
                }
                mutables.put(viewId, mutable);
            }
        }
    }

    public BadgeMutable getBadgeMutable(int targetViewId) {
        return mutables.get(targetViewId);
    }

    public void removeBadgeMutable(int targetViewId) {
        BadgeMutable mutable = mutables.get(targetViewId);
        if(mutable != null) {
            mutable.detach(false);
            mutables.remove(targetViewId);
        }
    }

    public void clearBadgeMutables() {
        while(mutables.size() > 0) {
            int key = mutables.keyAt(0);
            BadgeMutable mutable = mutables.valueAt(0);
            mutable.detach(false);
            mutables.remove(key);
        }
    }

    public void feedRects(SparseArray<Rect> rects) {
        this.rects = rects;
    }

    public SparseArray<BadgeMutable> getBadgeMutableCopies() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return mutables.clone();
        } else {
            SparseArray<BadgeMutable> copy = new SparseArray<>();
            for(int i = 0; i < mutables.size(); i++) {
                copy.put(mutables.keyAt(i), mutables.valueAt(i));
            }
            return copy;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int size = rects.size();
        for(int i = 0; i < size; i++) {
            int id = rects.keyAt(i);
            Rect rect = rects.get(id);
            BadgeMutable mutable = mutables.get(id);
            if(mutable != null) {
                BadgeStyle style = mutable.getStyle();
                if(style != null) {
                    style.apply(canvas, rect, mutable);
                }
            }
        }
    }
}
