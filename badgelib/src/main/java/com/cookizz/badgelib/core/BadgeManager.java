package com.cookizz.badgelib.core;

import com.cookizz.badgelib.DotBadge;
import com.cookizz.badgelib.FigureBadge;

/**
 * 角标管理器接口
 * Created by dugd on 2015/9/19.
 */
public interface BadgeManager {

    /**
     * 创建数字角标（自定义样式）
     * @param viewId
     * @param badgeStyle
     * @return
     */
    FigureBadge createFigureBadge(int viewId, Class badgeStyle);

    /**
     * 创建圆点角标（自定义样式）
     * @param viewId
     * @param badgeStyle
     * @return
     */
    DotBadge createDotBadge(int viewId, Class badgeStyle);

    /**
     * 清除所有角标
     */
    void clearAllBadges();
}
