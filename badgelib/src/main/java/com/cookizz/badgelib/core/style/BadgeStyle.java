package com.cookizz.badgelib.core.style;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.cookizz.badgelib.core.mutable.BadgeMutable;

/**
 * 角标样式接口
 * Created by dugd on 2015/9/14.
 */
public interface BadgeStyle {

    /**
     * 应用样式
     * @param canvas
     * @param rect
     * @param mutable
     */
    void apply(Canvas canvas, Rect rect, BadgeMutable mutable);
}
