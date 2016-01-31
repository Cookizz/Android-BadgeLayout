package com.cookizz.badge.core;

import android.view.View;

import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.core.style.DotStyle;
import com.cookizz.badge.core.style.FigureStyle;
import com.cookizz.badge.mutable.DotBadge;
import com.cookizz.badge.mutable.FigureBadge;

/**
 * BadgeManager interface
 * Created by Cookizz on 2015/9/19.
 */
public interface BadgeManager {

    /**
     * used for generating a figure badge
     * @param viewId id of target view to bind
     * @param badgeStyle desired style of your badge
     * @return badge mutable object
     */
    FigureBadge createFigureBadge(int viewId, Class<? extends FigureStyle> badgeStyle);

    FigureBadge createFigureBadge(View view, Class<? extends FigureStyle> badgeStyle);

    /**
     * used for generating a dot badge
     * @param viewId id of target view to bind
     * @param badgeStyle desired style of your badge
     * @return badge mutable object
     */
    DotBadge createDotBadge(int viewId, Class<? extends DotStyle> badgeStyle);

    DotBadge createDotBadge(View view, Class<? extends DotStyle> badgeStyle);

    BadgeMutable findBadge(int viewId);

    BadgeMutable findBadge(View view);

    void clearAllBadges();
}