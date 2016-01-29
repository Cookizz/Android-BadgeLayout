package com.cookizz.badge.core;

/**
 * 角标观察者
 * Created by Cookizz on 2015/9/19.
 */
public interface BadgeObserver {

    /**
     * 监听角标状态改变
     */
    void onBadgeStateChange();
}
