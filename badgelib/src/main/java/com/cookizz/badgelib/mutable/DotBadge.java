package com.cookizz.badgelib.mutable;

import com.cookizz.badgelib.core.mutable.AbsBadgeMutable;
import com.cookizz.badgelib.core.mutable.BadgeMutable;
import com.cookizz.badgelib.core.style.BadgeStyle;

/**
 * 小圆点角标
 * Created by dugd on 2015/9/19.
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
