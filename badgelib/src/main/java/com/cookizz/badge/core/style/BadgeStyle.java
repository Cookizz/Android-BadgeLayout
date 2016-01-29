package com.cookizz.badge.core.style;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.cookizz.badge.core.mutable.BadgeMutable;

/**
 * 角标样式接口
 * Created by Cookizz on 2015/9/14.
 */
public interface BadgeStyle {

    /**
     * 应用样式
     * @param canvas 画布
     * @param rect 绘制的目标矩形区域
     * @param mutable 角标可变状态
     */
    void apply(Canvas canvas, Rect rect, BadgeMutable mutable);
}
