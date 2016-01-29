package com.cookizz.badge.core.style;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * 角标样式(template method)
 * Created by Cookizz on 2015/9/17.
 */
public abstract class AbsBadgeStyle implements BadgeStyle {

    // 参考屏幕分辨率
    protected final Point referencedScreenResolution;
    // 重力场(1, -1表示紧贴右上角)
    protected final Point gravity;
    // 偏移场(各个分量代表向所表示的方向偏移一个向量值，可正负)
    protected final Point offset;

    // 屏幕适配缩放比
    protected final float mAdaptScale;

    public AbsBadgeStyle(Context context) {

        // 初始化参数
        Point resolution = getReferencedScreenResolution();
        if(resolution == null) {
            referencedScreenResolution = new Point();
        }
        else {
            referencedScreenResolution = resolution;
        }

        Point gravity = getGravity();
        if(gravity == null) {
            this.gravity = new Point();
        }
        else {
            this.gravity = gravity;
        }

        Point offset = getOffset();
        if(offset == null) {
            this.offset = new Point();
        }
        else {
            this.offset = offset;
        }

        // 配置适配缩放比
        int refWidth = referencedScreenResolution.x;
        int refHeight = referencedScreenResolution.y;
        final float refDiagonal = (float) Math.sqrt(refHeight * refHeight + refWidth * refWidth);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outPoint = new Point();

        int screenWidth, screenHeight;
        if(Build.VERSION.SDK_INT >= 13) {
            display.getSize(outPoint);
            screenHeight = outPoint.y;
            screenWidth = outPoint.x;
        }
        else {
            screenHeight = display.getHeight();
            screenWidth = display.getWidth();
        }
        final float intrinsicDiagonal = (float) Math.sqrt(screenHeight * screenHeight + screenWidth * screenWidth);
        mAdaptScale = intrinsicDiagonal / refDiagonal;
    }

    /**
     * 获得参照屏幕大小（分辨率）
     */
    public abstract Point getReferencedScreenResolution();

    /**
     * 获得重力场（Point.x > 0表示右边有重力，Point.y > 0表示下方有重力，合成以后等同于右下角）
     */
    public abstract Point getGravity();

    /**
     * 获得偏移场(Point.x表示横向偏移量，Point.y表示纵向偏移量)
     */
    public abstract Point getOffset();
}
