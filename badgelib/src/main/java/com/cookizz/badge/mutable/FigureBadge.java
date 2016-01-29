package com.cookizz.badge.mutable;

import com.cookizz.badge.core.mutable.AbsBadgeMutable;
import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.core.style.BadgeStyle;

/**
 * 角标可变状态
 * Created by Cookizz on 2015/9/19.
 */
public final class FigureBadge extends AbsBadgeMutable {

    private int figure;

    public FigureBadge(BadgeStyle style) {
        super(style);
        figure = 0;
    }

    @Override
    public BadgeMutable setFigure(int figure) {
        this.figure = figure;
        notifyObserver();
        return this;
    }

    @Override
    public int getFigure() {
        return figure;
    }
}
