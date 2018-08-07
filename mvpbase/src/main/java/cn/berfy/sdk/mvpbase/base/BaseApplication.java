package cn.berfy.sdk.mvpbase.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import cn.berfy.sdk.mvpbase.config.CacheConstant;
import cn.berfy.sdk.mvpbase.config.Constant;
import cn.berfy.sdk.mvpbase.util.AppUtils;
import cn.berfy.sdk.mvpbase.util.GsonUtil;
import cn.berfy.sdk.mvpbase.util.LogF;
import cn.berfy.sdk.mvpbase.util.ScreenUtil;
import cn.berfy.sdk.mvpbase.util.SharedPreferenceUtils;


public abstract class BaseApplication extends Application {

    private final String TAG = "AppApplication";
    private static Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;
    private static BaseApplication mInstance;

    //网络状态
    public static int NET_STATE = 1;//0没有网  1wifi 2 移动网络

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ScreenUtil.init(mInstance);
        mMainThreadHandler = new Handler();
        mMainThreadLooper = getMainLooper();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Application getContext() {
        return mInstance;
    }

    /**
     * @return 获取主线程的Handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * @return 获取主线程的Looper
     */
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    /**
     * @return 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * @return 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }
}
