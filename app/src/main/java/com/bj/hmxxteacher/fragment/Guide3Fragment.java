package com.bj.hmxxteacher.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.activity.LoginActivity;
import com.bj.hmxxteacher.activity.MainActivity;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zz379 on 2017/8/11.
 * 导航页3
 */

public class Guide3Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_3, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.tv_enter)
    void onClickEnter() {
        endOfGuide();
    }

    /**
     * 1. 上次登录时间为0，或者距离上次登录时间超多15天
     * 2. 手机号为空
     * 3. 登录状态不为1 的情况下，需要跳转到登录页面
     */
    private void endOfGuide() {
        // 判断登录时间，看是否需要重新登录
        long lastLoginTime = PreferencesUtils.getLong(getActivity(), MLProperties.PREFER_KEY_LOGIN_Time, 0);
        String userPhoneNumber = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID, "");
        int loginStatus = PreferencesUtils.getInt(getActivity(), MLProperties.PREFER_KEY_LOGIN_STATUS);
        if (lastLoginTime == 0 || StringUtils.isEmpty(userPhoneNumber) || loginStatus != 1 ||
                isLoginAgain(lastLoginTime)) {
            cleanAllPreferencesData();  // 清空所有Preference数据
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            // 登录环信
            login2Ease(userPhoneNumber);
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
                // 登录成功，跳转到首页
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("way", "登录聊天服务器失败！");
                // 登录环信失败，跳转到登录页面重新进行登录
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private boolean isLoginAgain(long lastLoginTime) {
        long currTime = System.currentTimeMillis();
        int span = (int) (currTime - lastLoginTime) / 1000 / 60 / 60 / 24;

        if (span >= MLConfig.KEEP_LOGIN_TIME_LENGTH) {
            return true;
        }

        return false;
    }

    private void cleanAllPreferencesData() {
        // 清除所有app内的数据
        PreferencesUtils.cleanAllData(getActivity());
        // 清除环信数据
        getActivity().getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE).edit().clear().apply();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("guide3");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("guide3");
    }
}
