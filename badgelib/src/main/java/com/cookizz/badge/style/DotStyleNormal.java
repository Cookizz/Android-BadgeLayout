package com.cookizz.badge.style;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;

import com.cookizz.badge.core.style.DotStyle;

/**
 * A sample implementation of 'dot' badge style
 * Created by Cookizz on 2015/9/17.
 */
public class DotStyleNormal extends DotStyle {


    public DotStyleNormal(Context context) {
        super(context);
    }

    @Override
    public int getBackgroundColor(Context context) {
        return Color.rgb(238, 37, 45);
    }

    @Override
    public int getRadius() {
        return 10;
    }

    @Override
    public Point getReferencedScreenResolution() {
        // iPhone 6 resolution
        return new Point(750, 1334);
    }

    @Override
    public Point getGravity() {
        return new Point(1, -1);
    }

    @Override
    public Point getOffset() {
        return new Point(6, -6);
    }
}
