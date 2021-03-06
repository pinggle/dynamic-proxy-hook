package com.weishu.upf.dynamic_proxy_hook.app2.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

import com.weishu.upf.dynamic_proxy_hook.app2.MainActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/***********************************************************************
 Android23下 ContextImpl::startActivity 函数定义为:
 -----------------------------------------------------------------------
 public void ContextImpl::startActivity(Intent intent, Bundle options) {
    warnIfCallingFromSystemProcess();
    if ((intent.getFlags()&Intent.FLAG_ACTIVITY_NEW_TASK) == 0) {
        throw new AndroidRuntimeException(
        "Calling startActivity() from outside of an Activity "
        + " context requires the FLAG_ACTIVITY_NEW_TASK flag."
        + " Is this really what you want?");
    }
    mMainThread.getInstrumentation().execStartActivity(
    getOuterContext(), mMainThread.getApplicationThread(), null,
        (Activity) null, intent, -1, options);
 }
 -----------------------------------------------------------------------
 ***********************************************************************/

/**
 * @author weishu
 * @date 16/1/28
 */
public class HookHelper {

    private static final String TAG = "HookHelper";

    public static void attachContext() throws Exception {
        // 先获取到当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);

        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

        // 创建代理对象
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);

        // 偷梁换柱
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);

        Log.v(TAG, "DTPrint HOOK (ContextImpl::startActivity) Over");
    }

    public static void attachActivity(MainActivity activity) throws Exception {

        // 传递当前的 Activity 对象进来;

        // 拿到原始的 mInstrumentation字段
        Class<?> activityClass = Activity.class;
        Field mInstrumentation = activityClass.getDeclaredField("mInstrumentation");
        mInstrumentation.setAccessible(true);
        Instrumentation base = (Instrumentation) mInstrumentation.get(activity);

        // 创建代理对象
        AppInstrumentation appInstrumentation = new AppInstrumentation(base);

        // 偷梁换柱
        mInstrumentation.set(activity, appInstrumentation);

        Log.v(TAG, "DTPrint HOOK (Activity::startActivity) Over");
    }
}
