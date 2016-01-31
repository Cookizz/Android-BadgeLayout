package com.cookizz.badge.core.style;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * BadgeStyle base class
 * Created by Cookizz on 2015/9/17.
 */
public abstract class AbsBadgeStyle implements BadgeStyle {

    protected final Point referencedScreenResolution; // used for auto screen adaption
    protected final Point gravity; // gravity of your badge within the target view's rectangle area
    protected final Point offset; // extra px offsets with direction X and Y

    protected final float screenAdaptionScale;

    public AbsBadgeStyle(Context context) {

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

        // calculate screen-adapted values
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
        screenAdaptionScale = intrinsicDiagonal / refDiagonal;
    }

    public abstract Point getReferencedScreenResolution();

    /**
     * "Gravity" indicates which X and Y direction (mapping Point.x and Point.y)
     * will your badge go when attached on the target view.
     */
    public abstract Point getGravity();

    /**
     * "Offset" indicates the extra X and Y px offsets your badge will have
     * based on its gravity which is defined by getGravity().
     */
    public abstract Point getOffset();
}
