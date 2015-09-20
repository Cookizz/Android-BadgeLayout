package com.cookizz.badgelib.template;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.cookizz.badgelib.core.mutable.BadgeMutable;
import com.cookizz.badgelib.core.style.AbsBadgeStyle;

/**
 * 小圆点角标(template method)
 * Created by dugd on 2015/9/17.
 */
public abstract class DotStyleTemplate extends AbsBadgeStyle {

    private final String TAG = getClass().getSimpleName();

    // 小圆点半径
    private final int mRadius;
    // 画笔
    private final Paint mPaint;

    public DotStyleTemplate(Context context) {
        super(context);

        // 应用适配缩放比
        mOffset.x *= mAdaptScale;
        mOffset.y *= mAdaptScale;
        mRadius = (int) (getRadius() * mAdaptScale);

        // 初始化背景画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getBackgroundColor(context));
    }

    @Override
    public final void apply(Canvas canvas, Rect rect, BadgeMutable mutable) {

        if(mutable.isAttached() && mutable.isShown()) {
            final int centerX, centerY;
            if(mGravity.x == 0) {
                centerX = mOffset.x + (rect.right + rect.left) / 2;
            }
            else {
                centerX = mOffset.x + (mGravity.x > 0 ? (rect.right - mRadius) : (rect.left + mRadius));
            }
            if(mGravity.y == 0) {
                centerY = mOffset.y + (rect.bottom + rect.top) / 2;
            }
            else {
                centerY = mOffset.y + (mGravity.y > 0 ? (rect.bottom - mRadius) : (rect.top + mRadius));
            }
            canvas.drawCircle(centerX, centerY, mRadius, mPaint);
        }
    }

    /**
     * 获得背景颜色
     * @param context
     * @return
     */
    public abstract int getBackgroundColor(Context context);

    /**
     * 获得小圆点半径
     * @return
     */
    public abstract int getRadius();
}
