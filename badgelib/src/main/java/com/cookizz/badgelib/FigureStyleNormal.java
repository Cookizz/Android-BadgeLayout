package com.cookizz.badgelib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;

import com.cookizz.badgelib.template.FigureStyleTemplate;

/**
 * 数字角标：贴附中等大小控件
 * Created by dugd on 2015/9/17.
 */
public class FigureStyleNormal extends FigureStyleTemplate {

    private final String TAG = getClass().getSimpleName();

    FigureStyleNormal(Context context) {
        super(context);
    }

    @Override
    public int getTextSize() {
        return 24;
    }

    @Override
    public int getTextColor(Context context) {
        return Color.rgb(255, 255, 255);
    }

    @Override
    public Typeface getTypeface(Context context) {
        return null;
    }

    @Override
    public int getBackgroundColor(Context context) {
        return Color.rgb(238, 37, 45);
    }

    @Override
    public int getTerminalRadius() {
        return 18;
    }

    @Override
    public int getWidth(int figure) {
        if(figure < 0) {
            return 0;
        }
        final int divideBy10 = figure / 10;
        if(divideBy10 == 0) {
            return 36;
        }
        else if(divideBy10 < 10) {
            return 48;
        }
        else {
            return 62;
        }
    }

    @Override
    public String getText(int figure) {
        String text;
        if(figure > 99) {
            text = "99+";
        }
        else {
            text = String.valueOf(figure);
        }
        return text;
    }

    @Override
    public boolean isVisible(int figure) {
        return figure > 0;
    }

    @Override
    public Point getReferencedScreenResolution() {
        return new Point(750, 1334);
    }

    @Override
    public Point getGravity() {
        return new Point(1, -1);
    }

    @Override
    public Point getOffset() {
        return new Point(10, -10);
    }
}
