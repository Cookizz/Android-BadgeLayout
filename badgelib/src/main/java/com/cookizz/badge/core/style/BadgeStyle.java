package com.cookizz.badge.core.style;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.cookizz.badge.core.mutable.BadgeMutable;

/**
 * Badge style interface
 * Created by Cookizz on 2015/9/14.
 */
public interface BadgeStyle {

    /**
     * Perform badge draw
     * @param rect The rectangle area your badge is being drawn
     */
    void apply(Canvas canvas, Rect rect, BadgeMutable mutable);
}
