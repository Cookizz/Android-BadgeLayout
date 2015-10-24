package com.cookizz.badgelib.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.cookizz.badgelib.FigureBadge;
import com.cookizz.badgelib.core.BadgeMutable;
import com.cookizz.badgelib.core.style.AbsBadgeStyle;

/**
 * 数字角标(template method)
 * Created by dugd on 2015/9/17.
 */
public abstract class FigureStyleTemplate extends AbsBadgeStyle {

    // 末端圆角半径
    private final int mTerminalRadius;
    // 文字大小
    private final int mTextSize;
    // 字体居中偏移
    private final int mTextBaselineCenterOffset;

    // 背景画笔
    private final Paint mBgPaint;
    // 文字画笔
    private final Paint mTextPaint;

    public FigureStyleTemplate(Context context) {
        super(context);

        // 应用适配缩放比
        mOffset.x *= mAdaptScale;
        mOffset.y *= mAdaptScale;
        mTerminalRadius = (int) (getTerminalRadius() * mAdaptScale);
        mTextSize = (int) (getTextSize() * mAdaptScale);

        // 初始化背景画笔
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(getBackgroundColor(context));

        // 初始化文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(getTextColor(context));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        Typeface typeface = getTypeface(context);
        if(typeface != null) {
            mTextPaint.setTypeface(typeface);
        }
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        mTextBaselineCenterOffset = -(fmi.bottom - fmi.top) / 2 - fmi.top;
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
                    if(mGravity.x == 0) {
                        centerX = mOffset.x + (rect.right + rect.left) / 2;
                    }
                    else {
                        centerX = mOffset.x + (mGravity.x > 0 ? (rect.right - width / 2) : (rect.left + width / 2));
                    }
                    if(mGravity.y == 0) {
                        centerY = mOffset.y + (rect.bottom + rect.top) / 2;
                    }
                    else {
                        centerY = mOffset.y + (mGravity.y > 0 ? (rect.bottom - mTerminalRadius) : (rect.top + mTerminalRadius));
                    }
                    // 绘制背景
                    if(width == 2 * mTerminalRadius) {
                        canvas.drawCircle(centerX, centerY, mTerminalRadius, mBgPaint);
                    }
                    else {
                        final int dist = width / 2 - mTerminalRadius;
                        canvas.drawCircle(centerX - dist, centerY, mTerminalRadius, mBgPaint);
                        canvas.drawCircle(centerX + dist, centerY, mTerminalRadius, mBgPaint);
                        canvas.drawRect(centerX - dist, centerY - mTerminalRadius, centerX + dist, centerY + mTerminalRadius, mBgPaint);
                    }
                    // 绘制文字
                    canvas.drawText(text, centerX, centerY + mTextBaselineCenterOffset, mTextPaint);
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
     * @param context
     * @return
     */
    public abstract int getTextColor(Context context);

    /**
     * 获得字体
     * @param context
     * @return
     */
    public abstract Typeface getTypeface(Context context);

    /**
     * 获得背景颜色
     * @param context
     * @return
     */
    public abstract int getBackgroundColor(Context context);

    /**
     * 获得末端圆角半径
     */
    public abstract int getTerminalRadius();

    /**
     * 随数字变化，获得宽度
     * @param figure
     * @return
     */
    public abstract int getWidth(int figure);

    /**
     * 随数字变化，获得相应的文字
     * @return
     */
    public abstract String getText(int figure);

    /**
     * 随数字变化，获得可见性
     * @param figure
     * @return
     */
    public abstract boolean isVisible(int figure);
}
