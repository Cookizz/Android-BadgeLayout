package com.cookizz.badgelib.core.mutable;

import com.cookizz.badgelib.core.BadgeObserver;
import com.cookizz.badgelib.core.style.BadgeStyle;

/**
 * 角标可变状态抽象类
 * Created by dugd on 2015/9/19.
 */
public abstract class AbsBadgeMutable implements BadgeMutable {

    private boolean isAttached;
    private boolean isShown;
    private BadgeObserver mObserver;
    private BadgeStyle style;

    public AbsBadgeMutable(BadgeStyle style) {
        isShown = false;
        isAttached = true;
        this.style = style;
    }

    @Override
    public boolean isShown() {
        return isShown;
    }

    @Override
    public void show() {
        if(!isShown) {
            isShown = true;
            notifyObserver();
        }
    }

    @Override
    public void hide() {
        if(isShown) {
            isShown = false;
            notifyObserver();
        }
    }

    @Override
    public BadgeStyle getStyle() {
        return style;
    }

    @Override
    public void detach() {
        if(isAttached) {
            isAttached = false;
            notifyObserver();
        }
    }

    @Override
    public void detach(boolean notifyObserver) {
        if(isAttached) {
            isAttached = false;
            if(notifyObserver) {
                notifyObserver();
            }
        }
    }

    @Override
    public boolean isAttached() {
        return isAttached;
    }

    @Override
    public void setObserver(BadgeObserver observer) {
        mObserver = observer;
    }

    @Override
    public void removeObserver() {
        mObserver = null;
    }

    /**
     * 通知observer
     */
    protected final void notifyObserver() {
        if(mObserver != null) {
            mObserver.onBadgeStateChange();
        }
    }
}
