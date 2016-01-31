package com.cookizz.badge.mutable;

import com.cookizz.badge.core.mutable.AbsBadgeMutable;
import com.cookizz.badge.core.mutable.BadgeMutable;
import com.cookizz.badge.core.style.BadgeStyle;

/**
 * DotBadge mutable class
 * Created by Cookizz on 2015/9/19.
 */
public final class DotBadge extends AbsBadgeMutable {

    public DotBadge(BadgeStyle style) {
        super(style);
    }

    @Override
    public BadgeMutable setFigure(int figure) {
        return this;
    }

    @Override
    public int getFigure() {
        return -1;
    }
}
