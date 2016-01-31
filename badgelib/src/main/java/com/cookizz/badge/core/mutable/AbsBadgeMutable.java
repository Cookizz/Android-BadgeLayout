package com.cookizz.badge.core.mutable;

import com.cookizz.badge.core.container.BadgeContainer;
import com.cookizz.badge.core.BadgeObserver;
import com.cookizz.badge.core.style.BadgeStyle;

/**
 * Base class for badge mutable
 * Created by Cookizz on 2015/9/19.
 */
public abstract class AbsBadgeMutable implements BadgeMutable {

    private boolean isAttached;
    private boolean isShown;
    private boolean isDetached;
    private BadgeStyle mStyle;
    private BadgeObserver mObserver;
    private BadgeContainer mContainer;

    public AbsBadgeMutable(BadgeStyle style) {
        mStyle = style;
    }

    @Override
    public boolean isShown() {
        return isShown;
    }

    @Override
    public BadgeMutable show() {
        if(!isShown) {
            isShown = true;
            notifyObserver();
        }
        return this;
    }

    @Override
    public BadgeMutable hide() {
        if(isShown) {
            isShown = false;
            notifyObserver();
        }
        return this;
    }

    @Override
    public BadgeStyle getStyle() {
        return mStyle;
    }

    @Override
    public void detach() {
        detach(true);
    }

    @Override
    public void detach(boolean notifyObserver) {
        if(isAttached) {
            if(notifyObserver) {
                notifyObserver();
            }
            mStyle = null;
            mObserver = null;
            mContainer = null;
            isAttached = false;
            isDetached = true;
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

    protected final void notifyObserver() {
        if(mObserver != null) {
            mObserver.onBadgeStateChange();
        }
    }

    public final boolean authenticateContainer(BadgeContainer container) {
        if(mContainer == null && !isDetached) {
            mContainer = container;
            isAttached = true;
            return true;
        }
        return mContainer == container;
    }
}
