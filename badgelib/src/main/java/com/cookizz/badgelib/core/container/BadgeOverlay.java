package com.cookizz.badgelib.core.container;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.cookizz.badgelib.core.mutable.AbsBadgeMutable;
import com.cookizz.badgelib.core.mutable.BadgeMutable;
import com.cookizz.badgelib.core.style.BadgeStyle;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 角标绘图表面
 * Created by dugd on 2015/9/13.
 */
public final class BadgeOverlay extends View implements BadgeContainer {

    // 角标状态集合
    private final SparseArray<BadgeMutable> mutables;
    // 待绘制的角标矩形区域集合
    private SparseArray<Rect> rects;

    public BadgeOverlay(Context context) {
        super(context);

        setEnabled(false);

        // 初始化集合
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

    /**
     * 放置角标
     */
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

    /**
     * 获得指定角标状态
     */
    public BadgeMutable getBadgeMutable(int targetViewId) {
        return mutables.get(targetViewId);
    }

    /**
     * 删除角标状态
     */
    public void removeBadgeMutable(int targetViewId) {
        BadgeMutable mutable = mutables.get(targetViewId);
        if(mutable != null) {
            mutable.detach(false);
            mutables.remove(targetViewId);
        }
    }

    /**
     * 清除所有角标状态
     */
    public void clearBadgeMutables() {
        while(mutables.size() > 0) {
            int key = mutables.keyAt(0);
            BadgeMutable mutable = mutables.valueAt(0);
            mutable.detach(false);
            mutables.remove(key);
        }
    }

    /**
     * 提供将要绘制的矩形区域集合
     */
    public void feedRects(SparseArray<Rect> rects) {
        this.rects = rects;
    }

    /**
     * 获得角标状态（副本）
     */
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
                // 委托drawable绘制角标当前状态
                BadgeStyle style = mutable.getStyle();
                if(style != null) {
                    style.apply(canvas, rect, mutable);
                }
            }
        }
    }
}
