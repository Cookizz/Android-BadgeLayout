package com.cookizz.badge;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.cookizz.badge.core.BadgeManager;
import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.core.style.DotStyle;
import com.cookizz.badge.core.style.FigureStyle;
import com.cookizz.badge.mutable.DotBadge;
import com.cookizz.badge.mutable.FigureBadge;

/**
 * Subclass of FrameLayout supporting badge operation
 * Created by Cookizz on 2015/10/9.
 */
public class BadgeFrameLayout extends FrameLayout implements BadgeManager {

    private BadgeLayoutVisitor visitor;

    public BadgeFrameLayout(Context context) {
        this(context, null);
    }

    public BadgeFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        visitor = new BadgeLayoutVisitor();
        visitor.visit(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BadgeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        visitor = new BadgeLayoutVisitor();
        visitor.visit(this);
    }

    @Override
    public final FigureBadge createFigureBadge(int viewId, Class<? extends FigureStyle> badgeStyle) {
        return visitor.createFigureBadge(viewId, badgeStyle);
    }

    @Override
    public FigureBadge createFigureBadge(View view, Class<? extends FigureStyle> badgeStyle) {
        return visitor.createFigureBadge(view, badgeStyle);
    }

    @Override
    public final DotBadge createDotBadge(int viewId, Class<? extends DotStyle> badgeStyle) {
        return visitor.createDotBadge(viewId, badgeStyle);
    }

    @Override
    public DotBadge createDotBadge(View view, Class<? extends DotStyle> badgeStyle) {
        return visitor.createDotBadge(view, badgeStyle);
    }

    @Override
    public BadgeMutable findBadge(int viewId) {
        return visitor.findBadge(viewId);
    }

    @Override
    public BadgeMutable findBadge(View view) {
        return visitor.findBadge(view);
    }

    @Override
    public final void clearAllBadges() {
        visitor.clearAllBadges();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        visitor.onAttachToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        visitor.onDetachFromWindow();
    }

    @Override
    public final int getChildCount() {
        return super.getChildCount();
    }

    @Override
    public final View getChildAt(int index) {
        return super.getChildAt(index);
    }

    @Override
    public final int indexOfChild(View child) {
        return super.indexOfChild(child);
    }

    @Override
    public final void bringChildToFront(@NonNull View child) {
        super.bringChildToFront(child);
    }
}
