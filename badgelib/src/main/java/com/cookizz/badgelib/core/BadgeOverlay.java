package com.cookizz.badgelib.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.cookizz.badgelib.core.style.BadgeStyle;

/**
 * 角标绘图表面
 * Created by dugd on 2015/9/13.
 */
public final class BadgeOverlay extends View implements BadgeContainer {

    // 角标状态集合
    private final SparseArray<BadgeMutable> mMutables;
    // 待绘制的目标View区域
    private SparseArray<Rect> mRectList;

    public BadgeOverlay(Context context) {
        super(context);

        setEnabled(false);

        // 初始化集合
        mMutables = new SparseArray<>();
        mRectList = new SparseArray<>();
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
    public void putMutable(int targetViewId, BadgeMutable mutable) {
        if(mutable != null && mutable instanceof AbsBadgeMutable) {
            AbsBadgeMutable absBadgeMutable = (AbsBadgeMutable) mutable;
            if(absBadgeMutable.authenticateContainer(this)) {
                BadgeMutable remaining = mMutables.get(targetViewId);
                if(remaining != null) {
                    remaining.detach(false);
                }
                mMutables.put(targetViewId, mutable);
            }
        }
    }

    /**
     * 获得指定角标状态
     */
    public BadgeMutable getMutable(int targetViewId) {
        return mMutables.get(targetViewId);
    }

    /**
     * 删除角标状态
     */
    public void removeMutable(int targetViewId) {
        BadgeMutable mutable = mMutables.get(targetViewId);
        if(mutable != null) {
            mutable.detach(false);
            mMutables.remove(targetViewId);
        }
    }

    /**
     * 清除所有角标状态
     */
    public void clearMutables() {
        while(mMutables.size() > 0) {
            int key = mMutables.keyAt(0);
            BadgeMutable mutable = mMutables.valueAt(0);
            mutable.detach(false);
            mMutables.remove(key);
        }
    }

    /**
     * 提供将要绘制的矩形区域集合
     */
    public void feedRects(SparseArray<Rect> rects) {
        mRectList = rects;
    }

    /**
     * 获得角标状态（副本）
     */
    public SparseArray<BadgeMutable> copyMutables() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return mMutables.clone();
        } else {
            SparseArray<BadgeMutable> copy = new SparseArray<>();
            for(int i = 0; i < mMutables.size(); i++) {
                copy.put(mMutables.keyAt(i), mMutables.valueAt(i));
            }
            return copy;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int size = mRectList.size();
        for(int i = 0; i < size; i++) {
            int id = mRectList.keyAt(i);
            Rect rect = mRectList.get(id);
            BadgeMutable mutable = mMutables.get(id);
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
