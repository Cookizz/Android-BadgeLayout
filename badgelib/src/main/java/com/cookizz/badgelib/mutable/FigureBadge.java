package com.cookizz.badgelib.mutable;

import com.cookizz.badgelib.core.mutable.AbsBadgeMutable;
import com.cookizz.badgelib.core.mutable.BadgeMutable;
import com.cookizz.badgelib.core.style.BadgeStyle;

/**
 * 角标可变状态
 * Created by dugd on 2015/9/19.
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
