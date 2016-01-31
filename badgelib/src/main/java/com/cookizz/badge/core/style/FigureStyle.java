package com.cookizz.badge.core.style;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.mutable.FigureBadge;

/**
 * Base class for figure badge style (template method)
 * Created by Cookizz on 2015/9/17.
 */
public abstract class FigureStyle extends AbsBadgeStyle {

    private final int terminalRadius;
    private final int textOffsetVertical;

    private final Paint backgroundPaint;
    private final Paint textPaint;

    public FigureStyle(Context context) {
        super(context);

        // calculate screen-adapted values
        offset.x *= screenAdaptionScale;
        offset.y *= screenAdaptionScale;
        terminalRadius = (int) (getTerminalRadius() * screenAdaptionScale);
        int textSize = (int) (getTextSize() * screenAdaptionScale);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(getBackgroundColor(context));

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
                    final int width = (int) (getWidth(figure) * screenAdaptionScale);
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
                    if(width == 2 * terminalRadius) {
                        canvas.drawCircle(centerX, centerY, terminalRadius, backgroundPaint);
                    }
                    else {
                        final int dist = width / 2 - terminalRadius;
                        canvas.drawCircle(centerX - dist, centerY, terminalRadius, backgroundPaint);
                        canvas.drawCircle(centerX + dist, centerY, terminalRadius, backgroundPaint);
                        canvas.drawRect(centerX - dist, centerY - terminalRadius, centerX + dist, centerY + terminalRadius, backgroundPaint);
                    }
                    canvas.drawText(text, centerX, centerY + textOffsetVertical, textPaint);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public abstract int getTextSize();

    public abstract int getTextColor(Context context);

    public abstract Typeface getTypeface(Context context);

    public abstract int getBackgroundColor(Context context);

    public abstract int getTerminalRadius();

    public abstract int getWidth(int figure);

    public abstract String getText(int figure);

    public abstract boolean isVisible(int figure);
}