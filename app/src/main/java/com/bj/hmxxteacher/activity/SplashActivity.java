package com.bj.hmxxteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.utils.AppUtils;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.ScreenUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * APP启动页
 */
public class SplashActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    private boolean isShowGuideByAdmin = false;     // 管理员直接决定是否要显示导航页
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

            if (ScreenUtils.isNavigationBarShow(this)) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.white));
            }
        }

        setContentView(R.layout.activity_splash);
        // 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);

        //add
//        intentToMainActivity();

        initView();

        initData();
    }




    public void initData() {
        // 首先判断是否要进入导航页
        if (isShowGuide()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, 1500);
        } else {
            // 判断登录时间，看是否需要重新登录
            long lastLoginTime = PreferencesUtils.getLong(this, MLProperties.PREFER_KEY_LOGIN_Time, 0);
            String userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID, "");
            int loginStatus = PreferencesUtils.getInt(this, MLProperties.PREFER_KEY_LOGIN_STATUS);
            if (lastLoginTime == 0 || StringUtils.isEmpty(userPhoneNumber) || loginStatus != 1 ||
                    isLoginAgain(lastLoginTime)) {
                cleanAllPreferencesData();  // 清空所有Preference数据
                intentToLoginActivity();    // 跳转到登录页面
            } else {
                // 登录环信
                //login2Ease(userPhoneNumber);
                intentToMainActivity();
            }
        }
    }

    private void cleanAllPreferencesData() {
        // 清除所有app内的数据
        PreferencesUtils.cleanAllData(this);
        // 清除环信数据
        getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE).edit().clear().apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void initView() {
        ImageView imgSplash = (ImageView) this.findViewById(R.id.img_splash);
        TextView tvTitle = (TextView) this.findViewById(R.id.tv_title);
        Animation animation = new AlphaAnimation(0, 1.0f);
        animation.setStartTime(0);
        animation.setDuration(1500);
        animation.setFillAfter(true);
        imgSplash.startAnimation(animation);

        Animation animation2 = new AlphaAnimation(0, 1.0f);
        animation2.setStartTime(500);
        animation2.setDuration(1000);
        animation2.setFillAfter(true);
        tvTitle.startAnimation(animation2);
    }

    /**
     * 判断是否显示启动页
     *
     * @return
     */
    private boolean isShowGuide() {
        // 如果管理员决定当前版本不再显示导航页，则直接跳过后面的检查
        if (!isShowGuideByAdmin) {
            return isShowGuideByAdmin;
        }
        String oldVersion = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_VERSION_CODE, "");
        String currVersion = AppUtils.getVersionName(this);
        if (StringUtils.isEmpty(oldVersion) || !oldVersion.equals(currVersion)) {
            // 保存当前版本号 
            PreferencesUtils.putString(this, MLProperties.PREFER_KEY_VERSION_CODE, currVersion);
            return true;
        } else {
            return false;
        }
    }

    private void login2Ease(String userPhoneNumber) {
        String userEaseID = MLConfig.EASE_TEACHER_ID_PREFIX + userPhoneNumber;
        EMClient.getInstance().login(userEaseID, "123456", new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                // 加载环信相关数据
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("way", "登录聊天服务器成功！");
                // 跳转到首页
                intentToMainActivity();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("way", "登录聊天服务器失败！");
                // 跳转到登录页面
                intentToLoginActivity();
            }
        });
    }

    /**
     * 1500毫秒后跳转到登录页面
     */
    private void intentToLoginActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 1500);
    }

    private void intentToMainActivity() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 1500);
    }

    private boolean isLoginAgain(long lastLoginTime) {
        long currTime = System.currentTimeMillis();
        int span = (int) (currTime - lastLoginTime) / 1000 / 60 / 60 / 24;

        if (span >= MLConfig.KEEP_LOGIN_TIME_LENGTH) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("qidongye");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("qidongye");
        MobclickAgent.onPause(this);
    }
}
