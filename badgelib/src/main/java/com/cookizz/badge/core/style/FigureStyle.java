package com.cookizz.badge.core.style;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.mutable.FigureBadge;

/**
 * 数字角标(template method)
 * Created by Cookizz on 2015/9/17.
 */
public abstract class FigureStyle extends AbsBadgeStyle {

    private final int terminalRadius;
    // 字体垂直偏移（默认在居中位置）
    private final int textOffsetVertical;

    private final Paint backgroundPaint;
    private final Paint textPaint;

    public FigureStyle(Context context) {
        super(context);

        // 应用适配缩放比
        offset.x *= mAdaptScale;
        offset.y *= mAdaptScale;
        terminalRadius = (int) (getTerminalRadius() * mAdaptScale);
        int textSize = (int) (getTextSize() * mAdaptScale);

        // 初始化背景画笔
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(getBackgroundColor(context));

        // 初始化文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(getTextColor(context));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        Typeface typeface = getTypeface(context);
        if(typeface != null) {
            textPaint.setTypeface(typeface);
        }
        Paint.FontMetricsInt fmi = textPaint.getFontMetricsInt();
        textOffsetVertical = -(fmi.bottom - fmi.top) / 2 - fmi.top;
    }

    @Override
    public final void apply(Canvas canvas, Rect rect, BadgeMutable mutable) {

        try {
            FigureBadge figureMutable = (FigureBadge) mutable;
            if(figureMutable.isAttached() && figureMutable.isShown()) {
                final int figure = figureMutable.getFigure();
                if(isVisible(figure)) {
                    final int width = (int) (getWidth(figure) * mAdaptScale);
                    final String text = getText(figure);
                    final int centerX, centerY;
                    if(gravity.x == 0) {
                        centerX = offset.x + (rect.right + rect.left) / 2;
                    }
                    else {
                        centerX = offset.x + (gravity.x > 0 ? (rect.right - width / 2) : (rect.left + width / 2));
                    }
                    if(gravity.y == 0) {
                        centerY = offset.y + (rect.bottom + rect.top) / 2;
                    }
                    else {
                        centerY = offset.y + (gravity.y > 0 ? (rect.bottom - terminalRadius) : (rect.top + terminalRadius));
                    }
                    // 绘制背景
                    if(width == 2 * terminalRadius) {
                        canvas.drawCircle(centerX, centerY, terminalRadius, backgroundPaint);
                    }
                    else {
                        final int dist = width / 2 - terminalRadius;
                        canvas.drawCircle(centerX - dist, centerY, terminalRadius, backgroundPaint);
                        canvas.drawCircle(centerX + dist, centerY, terminalRadius, backgroundPaint);
                        canvas.drawRect(centerX - dist, centerY - terminalRadius, centerX + dist, centerY + terminalRadius, backgroundPaint);
                    }
                    // 绘制文字
                    canvas.drawText(text, centerX, centerY + textOffsetVertical, textPaint);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得文字
     */
    public abstract int getTextSize();

    /**
     * 获得文字颜色
     */
    public abstract int getTextColor(Context context);

    /**
     * 获得字体
     */
    public abstract Typeface getTypeface(Context context);

    /**
     * 获得背景颜色
     */
    public abstract int getBackgroundColor(Context context);

    /**
     * 获得末端圆角半径
     */
    public abstract int getTerminalRadius();

    /**
     * 随数字变化，获得宽度
     */
    public abstract int getWidth(int figure);

    /**
     * 随数字变化，获得相应的文字
     */
    public abstract String getText(int figure);

    /**
     * 随数字变化，获得可见性
     */
    public abstract boolean isVisible(int figure);
}
