package com.lxxself.jietu.util;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/8/20.
 */
public class StatusBarAction {
    public static final void collapseStatusBar(Context ctx) {
        Object sbservice = ctx.getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method collapse;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                collapse = statusBarManager.getMethod("collapsePanels");
            } else {
                collapse = statusBarManager.getMethod("collapse");
            }
            collapse.invoke(sbservice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
