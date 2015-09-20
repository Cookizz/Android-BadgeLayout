package com.cookizz.badgelib.core.mutable;

import com.cookizz.badgelib.core.BadgeObserver;
import com.cookizz.badgelib.core.style.BadgeStyle;

/**
 * 角标可变状态接口
 * Created by dugd on 2015/9/19.
 */
public interface BadgeMutable {

    /**
     * 显示角标
     */
    void show();

    /**
     * 隐藏角标
     */
    void hide();

    /**
     * 判断角标显隐性
     */
    boolean isShown();

    /**
     * 分离角标
     */
    void detach();

    /**
     * 分离角标（可选择是否通知观察者）
     * @param notifyObserver
     */
    void detach(boolean notifyObserver);

    /**
     * 判断附着状态
     */
    boolean isAttached();

    /**
     * 获得角标样式
     * @return
     */
    BadgeStyle getStyle();

    /**
     * 设置观察者
     * @param observer
     */
    void setObserver(BadgeObserver observer);

    /**
     * 移除观察者
     */
    void removeObserver();
}
