package com.cookizz.badge.core.style;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 角标工厂类（flyweight, singleton）
 * Created by Cookizz on 2015/9/17.
 */
public final class BadgeStyleFactory {

    private static BadgeStyleFactory mInstance;
    public static BadgeStyleFactory getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new BadgeStyleFactory(context);
        }
        return mInstance;
    }

    // 角标样式对象池
    private Map<Class, AbsBadgeStyle> mStylePool = new HashMap<>();

    private Context mContext;

    private BadgeStyleFactory(Context context) {
        mContext = context;
    }

    /**
     * 创建（提取）角标样式
     */
    public AbsBadgeStyle createStyle(Class clazz) {

        AbsBadgeStyle badge = mStylePool.get(clazz);
        if(badge == null) {
            if(clazz != null) {
                try {
                    Constructor constructor = clazz.getDeclaredConstructor(Context.class);
                    constructor.setAccessible(true);
                    badge = (AbsBadgeStyle) constructor.newInstance(mContext);
                    mStylePool.put(clazz, badge);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return badge;
    }
}
