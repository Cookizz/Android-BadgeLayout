package com.cookizz.badge.core.mutable;

import com.cookizz.badge.core.BadgeObserver;
import com.cookizz.badge.core.style.BadgeStyle;

/**
 * Badge mutable interface
 * Created by Cookizz on 2015/9/19.
 */
public interface BadgeMutable {

    BadgeMutable show();

    BadgeMutable hide();

    boolean isShown();

    void detach();

    void detach(boolean notifyObserver);

    boolean isAttached();

    BadgeStyle getStyle();

    void setObserver(BadgeObserver observer);

    void removeObserver();

    BadgeMutable setFigure(int figure);

    int getFigure();
}
