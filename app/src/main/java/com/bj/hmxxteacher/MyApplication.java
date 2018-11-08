package com.bj.hmxxteacher;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.bj.hmxxteacher.exception.CrashHandler;
import com.bj.hmxxteacher.manager.IMHelper;
import com.bj.hmxxteacher.manager.UMPushManager;
import com.bj.hmxxteacher.utils.FrescoImagePipelineConfigFactory;
import com.bj.hmxxteacher.utils.FrescoUtils;
import com.bj.hmxxteacher.utils.LL;
import com.bj.hmxxteacher.zzautolayout.config.AutoLayoutConifg;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.PushAgent;

import java.util.List;

/**
 * Created by zz379 on 2017/9/6.
 */

public class MyApplication extends Application {

    private static MyApplication app;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("way", "MyApplication -->> onCreate() -->> 初始化");
        app = this;
        context = getApplicationContext();

        initFresco();
        // 默认使用的高度是设备的可用高度，也就是不包括状态栏和底部的操作栏的，如果你希望拿设备的物理高度进行百分比化：
        AutoLayoutConifg.getInstance().useDeviceSize();
        // 初始化友盟统计
        initUM();
        // 初始化EaseUI
        IMHelper.getInstance().init(this);

        initOkGo();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    private void initOkGo() {
        OkGo.getInstance().init(this);
    }

    private void initUM() {
        // 参数3:Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "ead11796c81542260963b770670ebbf8");
        // 初始化友盟推送相关信息
        LL.i("制造商：" + Build.MANUFACTURER);
        if (!Build.MANUFACTURER.equals("Xiaomi")) {
            LL.i("初始化友盟推送");
        } else {
            // MIUI系统
            LL.i("初始化小米推送");
        }
        // 初始化友盟推送
        UMPushManager manager = UMPushManager.getInstance();
        PushAgent pushAgent = PushAgent.getInstance(this);
        manager.initUmeng(pushAgent);
    }

    /**
     * 初始化FaceBook的Fresco框架
     */
    private void initFresco() {
        Fresco.initialize(this, FrescoImagePipelineConfigFactory.getOkHttpImagePipelineConfig(this));
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return app;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        FrescoUtils.TrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        FrescoUtils.clearAllMemoryCaches();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
