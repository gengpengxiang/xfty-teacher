package com.bj.hmxxteacher.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.api.LmsDataService;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.bean.TeacherInfos;
import com.bj.hmxxteacher.dialog.HelpAlertDialog;
import com.bj.hmxxteacher.entity.MessageEvent;
import com.bj.hmxxteacher.entity.TeacherInfo;
import com.bj.hmxxteacher.utils.KeyBoardUtils;
import com.bj.hmxxteacher.utils.LL;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.ScreenUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.utils.T;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxteacher.api.HttpUtilService.BASE_RESOURCE_URL;
import static com.bj.hmxxteacher.api.HttpUtilService.BASE_URL;

/**
 * Created by he on 2016/12/19.
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtPhoneNumber, edtCode;
    private SimpleDraweeView btnLogin;
    private TextView tvGetCode, tvWithProblem;
    private int timeRemaining; //剩余时间
    private ScrollView mScrollView;
    private ProgressDialog mProgressDialog;
    private LinearLayout llDouhaoProtocol;
    private LmsDataService mService;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果存在虚拟按键，则设置虚拟按键的背景色
        if (ScreenUtils.checkDeviceHasNavigationBar(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }
        setContentView(R.layout.activity_login);
        mService = new LmsDataService();
        // 初始化页面
        initToolBar();
        initView();
        initData();

        // 恢复数据
        if (savedInstanceState != null && !StringUtils.isEmpty(savedInstanceState.getString("PhoneNumber"))) {
            edtPhoneNumber.post(new Runnable() {
                @Override
                public void run() {
                    edtPhoneNumber.setText(savedInstanceState.getString("PhoneNumber"));
                }
            });
        }
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        ImageView ivBack = (ImageView) this.findViewById(R.id.header_img_back);
        ivBack.setVisibility(View.VISIBLE);
        TextView tvTitle = (TextView) this.findViewById(R.id.header_tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.app_name);
        LinearLayout llLeft = (LinearLayout) this.findViewById(R.id.header_ll_left);
        llLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initView() {
        mScrollView = (ScrollView) this.findViewById(R.id.scrollview);
        LinearLayout llContent = (LinearLayout) this.findViewById(R.id.ll_content);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                        , LoginActivity.this);
            }
        });
        edtPhoneNumber = (EditText) this.findViewById(R.id.edt_phoneNumber);
        edtCode = (EditText) this.findViewById(R.id.edt_verificationCode);
        btnLogin = (SimpleDraweeView) this.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        tvGetCode = (TextView) this.findViewById(R.id.tv_getCode);
        tvGetCode.setOnClickListener(this);
        tvWithProblem = (TextView) this.findViewById(R.id.tv_withProblem);
        tvWithProblem.setOnClickListener(this);
        llDouhaoProtocol = (LinearLayout) this.findViewById(R.id.ll_douhaoProtocol);

        edtPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
        edtCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeScrollView();
                return false;
            }
        });
        // addLayoutListener(llContent, tvWithProblem);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(android.R.style.Theme_Material_Dialog_Alert);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("数据加载中...");
        mProgressDialog.setCancelable(false);

        llDouhaoProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - currTimeMillin >= 1000) {
                    currTimeMillin = System.currentTimeMillis();
                    Intent intent = new Intent(LoginActivity.this, ProtocolActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                actionLogin();
                break;
            case R.id.tv_getCode:
                MobclickAgent.onEvent(LoginActivity.this, "login_code");
                actionGetCode();
                break;
            case R.id.tv_withProblem:
                KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                        , LoginActivity.this);
                actionForHelp();
                break;
        }
    }

    private void actionForHelp() {
        HelpAlertDialog dialog = new HelpAlertDialog(LoginActivity.this);
        dialog.setContentText("使用幸福田园教师版过程中遇到任何问题或您有什么建议，请联系小助手。\n微信号：pkugame\n电话：15201635868\n邮箱：douhaojiaoyu@163.com");
        dialog.show();
    }

    private long currTimeMillin;

    /**
     * 点击login按钮
     */
    private void actionLogin() {
        if (System.currentTimeMillis() - currTimeMillin < 1000) {
            currTimeMillin = System.currentTimeMillis();
        } else {
            currTimeMillin = System.currentTimeMillis();

            KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                    , LoginActivity.this);
            String phoneNum = edtPhoneNumber.getText().toString().trim();
            String codeNum = edtCode.getText().toString().trim();
            if (StringUtils.isEmpty(phoneNum)) {
                T.showShort(this, "请输入手机号");
                return;
            }
            if (!StringUtils.checkPhoneNumber(phoneNum)) {
                T.showShort(this, "手机号输入有误");
                return;
            }
            if (StringUtils.isEmpty(codeNum)) {
                T.showShort(this, "请输入验证码");
                return;
            }

            btnLogin.setClickable(false);
            mProgressDialog.show();
            // 判断手机号和验证码是否匹配
            standardLogin(phoneNum, codeNum);
        }
    }

    /**
     * 标准登录，验证手机号和验证码是否匹配
     *
     * @param phone
     * @param code
     */
    private void standardLogin(final String phone, final String code) {
        Observable.create(new ObservableOnSubscribe<TeacherInfo>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<TeacherInfo> e) throws Exception {
                TeacherInfo result = mService.loginFromAPI2(phone, code);
                e.onNext(result);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TeacherInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull TeacherInfo result) {
                        if (!StringUtils.isEmpty(result.getErrorCode()) && result.getErrorCode().equals("1")) {
                            // 登录成功后，根据直播状态判断, 获取教师的个人信息
                            getTeacherInfo(phone);
                        } else {
                            T.showShort(LoginActivity.this, result.getMessage());
                            btnLogin.setClickable(true);
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        T.showShort(LoginActivity.this, "服务器开小差了，请稍后重试！");
                        btnLogin.setClickable(true);
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getTeacherInfo(final String phoneNumber) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL+"js/getteacherinfo")
                        .params("appkey",MLConfig.HTTP_APP_KEY)
                        .params("teacherphone",phoneNumber)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                e.onNext(str);
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        TeacherInfos teacherInfo = JSON.parseObject(s,new TypeReference<TeacherInfos>(){});

                        if(teacherInfo.getRet().equals("1")){
                            TeacherInfos.DataBean dataBean = teacherInfo.getData();
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_TEACHER_IMG, BASE_RESOURCE_URL+dataBean.getTeacherimg());
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_CLASS_NAME, dataBean.getName());
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.PREFER_KEY_USER_ID, dataBean.getTeacherphone());
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_NAME, dataBean.getSchoolname());
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_CODE, dataBean.getSchoolcode());
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.BUNDLE_KEY_SCHOOL_IMG, BASE_RESOURCE_URL+dataBean.getSchoolimg());

                            //账号权限type  2所有权限   1只有管理  0只有班级首页
                            // 登录成功，然后登录环信
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.PREFER_KEY_ACCOUNT_TYPE, dataBean.getType());
                            PreferencesUtils.putString(LoginActivity.this, MLProperties.PREFER_KEY_DEFAULT_CLASSCODE, dataBean.getMoren_classcode());
                            //EventBus.getDefault().post(new MessageEvent("accounttype",dataBean.getType()));

                            loginSuccess();
                        }
                    }
                });

    }

    private void actionGetCode() {
        if (System.currentTimeMillis() - currTimeMillin < 1000) {
            currTimeMillin = System.currentTimeMillis();
        } else {
            currTimeMillin = System.currentTimeMillis();
            KeyBoardUtils.closeKeybord(LoginActivity.this.getCurrentFocus().getWindowToken()
                    , LoginActivity.this);
            String phoneNum = edtPhoneNumber.getText().toString().trim();
            if (StringUtils.isEmpty(phoneNum)) {
                T.showShort(this, "请输入手机号");
                return;
            }
            if (!StringUtils.checkPhoneNumber(phoneNum)) {
                T.showShort(this, "手机号输入有误");
                return;
            }
            // 获取验证码，并开始倒计时

            MyGetCodeTask myGetCodeTask = new MyGetCodeTask();
            myGetCodeTask.execute(phoneNum);
        }
    }

    private void startTimerSchedule() {
        timeRemaining = 61;
        tvGetCode.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        tvGetCode.setClickable(false);

        mHandler.post(timerRunnable);
    }

    Handler mHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timeRemaining--;
            tvGetCode.setText(String.format("%d秒后可重发", timeRemaining));
            if (timeRemaining < 0) {
                tvGetCode.setText("获取验证码");
                tvGetCode.setTextColor(ContextCompat.getColor(LoginActivity.this,
                        R.color.colorPrimary));
                tvGetCode.setClickable(true);
            } else {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * 登录到环信
     *
     * @param userPhoneNumber
     */
    private void login2Ease(final String userPhoneNumber) {
        final String userEaseID = MLConfig.EASE_TEACHER_ID_PREFIX + userPhoneNumber;
        EMClient.getInstance().login(userEaseID, "123456", new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                // 加载环信相关数据
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("way", userEaseID + "登录聊天服务器成功！");
                // 登录成功, 跳转到首页
                loginSuccess();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnLogin.setClickable(true);
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        Log.d("way", userEaseID + "登录失败，请重新发送验证码" + " " + code);
                        if (code == 200) {
                            // 加载环信相关数据
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.d("way", "登录聊天服务器成功！");
                            // 登录成功, 跳转到首页
                            loginSuccess();
                        } else {
                            T.showShort(LoginActivity.this, "登录失败，请重新发送验证码" + " " + code);
                        }
                    }
                });
            }
        });
    }

    /**
     * 登录成功后跳转到首页
     */
    private void loginSuccess() {
        PreferencesUtils.putLong(this, MLProperties.PREFER_KEY_LOGIN_Time, System.currentTimeMillis());
        PreferencesUtils.putInt(this, MLProperties.PREFER_KEY_LOGIN_STATUS, 1);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private class MyGetCodeTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            tvGetCode.setClickable(false);
            startTimerSchedule();   // 开始计时
        }

        @Override
        protected String[] doInBackground(String... params) {
            String[] result;
            try {
                result = mService.getCodeFromAPI2(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[2];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (!StringUtils.isEmpty(result[0]) && result[0].equals("1")) {
                // startTimerSchedule();   // 开始计时
                T.showShort(LoginActivity.this, "获取验证码成功");
            } else {
                tvGetCode.setClickable(true);
                T.showShort(LoginActivity.this, StringUtils.isEmpty(result[1]) ? "发送验证码失败" : result[1]);
            }
        }
    }

    @Override
    public void onBackPressed() {
        cleanAllPreferencesData();
        this.finish();
        overridePendingTransition(R.anim.act_alpha_in, R.anim.act_top_bottom_out);
    }

    private void cleanAllPreferencesData() {
        // 清除所有app内的数据
        PreferencesUtils.cleanAllData(this);
        // 清除环信数据
        getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE).edit().clear().apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("login");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("login");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(timerRunnable);
        super.onDestroy();
    }

    /**
     * 使ScrollView指向底部
     */
    private void changeScrollView() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, tvWithProblem.getBottom() - mScrollView.getHeight());
            }
        }, 300);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PhoneNumber", edtPhoneNumber.getText().toString());
    }
}
