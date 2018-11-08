package com.bj.hmxxteacher.activity;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.api.LmsDataService;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.dialog.UpdateAPPAlertDialog;
import com.bj.hmxxteacher.dialog.UpdateAPPAlertDialog2;
import com.bj.hmxxteacher.entity.AppVersionInfo;
import com.bj.hmxxteacher.entity.MessageEvent;
import com.bj.hmxxteacher.entity.TeacherInfo;
import com.bj.hmxxteacher.fragment.ChangeBottomTabListener;
import com.bj.hmxxteacher.fragment.ConversationListFragment;
import com.bj.hmxxteacher.fragment.HomeFragment;
import com.bj.hmxxteacher.fragment.ManageFragment;
import com.bj.hmxxteacher.fragment.UserFragment;
import com.bj.hmxxteacher.manager.IntentManager;
import com.bj.hmxxteacher.manager.UMPushManager;
import com.bj.hmxxteacher.service.DownloadAppService;
import com.bj.hmxxteacher.utils.AppUtils;
import com.bj.hmxxteacher.utils.IMMLeaks;
import com.bj.hmxxteacher.utils.KeyBoardUtils;
import com.bj.hmxxteacher.utils.LL;
import com.bj.hmxxteacher.utils.LeakedUtils;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.ScreenUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.widget.CustomViewPager;
import com.bj.hmxxteacher.zzokhttp.OkHttpUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements ChangeBottomTabListener {

    private static final int TAB_NUMBER = 3;

    @BindView(R.id.mViewPager)
    CustomViewPager mViewPager;
    @BindView(R.id.ll_bottomBar)
    LinearLayout llBottomBar;
    @BindViews({R.id.iv_tab0, R.id.iv_tab1, R.id.iv_tab2})
    ImageView[] ivTabs;
    @BindViews({R.id.tv_tab0, R.id.tv_tab1, R.id.tv_tab2,})
    TextView[] tvTabs;
    int[] resTabImageSelect = {R.mipmap.ic_tab_class_teacher, R.mipmap.ic_guanli_green, R.mipmap.ic_tab_person_selected};
    int[] resTabImageUnSelect = {R.mipmap.ic_tab_class_unselected, R.mipmap.ic_guanli_gray, R.mipmap.ic_tab_person};
    @BindView(R.id.ll_tab0)
    LinearLayout llTab0;
    @BindView(R.id.ll_tab1)
    LinearLayout llTab1;
    @BindView(R.id.ll_tab2)
    LinearLayout llTab2;
    private Fragment[] mTabFragments;
    private long exitTime = 0;

    private static MainActivity instance = null;

    private int currentPageIndex = 0;
    private String teacherPhoneNumber;
    private String accountType;

    private long currMillis = 0;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        EventBus.getDefault().register(this);


        instance = this;
        // 恢复数据
        if (savedInstanceState != null && 0 != savedInstanceState.getInt("CurrPageIndex")) {
            currentPageIndex = savedInstanceState.getInt("CurrPageIndex");
        }
        teacherPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID, "");
        //add
        //getTeacherInfo(teacherPhoneNumber);
//账号权限type  2所有权限   1只有管理  0只有班级首页
        accountType = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_ACCOUNT_TYPE, "");
        if (accountType.equals("2")) {
            llTab0.setVisibility(View.VISIBLE);
            llTab1.setVisibility(View.VISIBLE);
            llTab2.setVisibility(View.VISIBLE);

            currentPageIndex = 0;

        } else if (accountType.equals("1")) {
            llTab0.setVisibility(View.VISIBLE);
            llTab1.setVisibility(View.VISIBLE);
            llTab2.setVisibility(View.VISIBLE);

//            currentPageIndex = 1;
            currentPageIndex = 0;

        } else if (accountType.equals("0")) {
            llTab0.setVisibility(View.VISIBLE);
            llTab1.setVisibility(View.GONE);
            llTab2.setVisibility(View.VISIBLE);

            currentPageIndex = 0;
        }

        initToolBar();
        initView();

        if (System.currentTimeMillis() - currMillis > 1000) {
            MyCheckNewVersionTask versionTask = new MyCheckNewVersionTask();
            versionTask.execute();
            currMillis = System.currentTimeMillis();
        }
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
    }

    @Override
    protected void initView() {
        //currentPageIndex = getIntent().getIntExtra(MLProperties.BUNDLE_KEY_MAIN_PAGEINDEX, currentPageIndex);

        mTabFragments = new Fragment[TAB_NUMBER];

        HomeFragment homeFragment = new HomeFragment();
        UserFragment userFragment = new UserFragment();
        userFragment.setBottomTabListener(this);

        ManageFragment manageFragment = new ManageFragment();

        mTabFragments[0] = homeFragment;
        mTabFragments[1] = manageFragment;
        mTabFragments[2] = userFragment;

        MyFragmentPagerAdapter mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(currentPageIndex);
        mViewPager.setOffscreenPageLimit(3);
        actionTabItemSelect(currentPageIndex);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        initData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!MainActivity.this.isFinishing()) {
                    RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE)
                            .subscribe();
                }
            }
        }, 1000);

    }

    @Override
    protected void initData() {
        teacherPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID, "");
        updateUnreadLabel();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        // 添加标签
        String schoolID = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_SCHOOL_CODE, "");
        if (!StringUtils.isEmpty(schoolID)) {
            UMPushManager.getInstance().addTag(schoolID);
        }

        if (!StringUtils.isEmpty(teacherPhoneNumber)) {
            // 初始化友盟
            UMPushManager manager = UMPushManager.getInstance();
            manager.setPushAlias(teacherPhoneNumber);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @OnClick({R.id.ll_tab0, R.id.ll_tab1, R.id.ll_tab2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tab0:
                actionTabItemSelect(0);
                break;
            case R.id.ll_tab1:
                actionTabItemSelect(1);
                break;
            case R.id.ll_tab2:
                if (StringUtils.isEmpty(teacherPhoneNumber)) {
                    // 如果手机号为空，则跳转到登录页面
                    IntentManager.toLoginActivity(this, IntentManager.LOGIN_SUCC_ACTION_MAINACTIVITY);
                    return;
                }
                actionTabItemSelect(2);
                break;
        }
    }


    public void actionTabItemSelect(int position) {
        llBottomBar.requestFocus();
        KeyBoardUtils.closeKeybord(llBottomBar.getWindowToken(), this);
        for (int i = 0; i < TAB_NUMBER; i++) {
            if (i == position) {
                ivTabs[i].setImageResource(resTabImageSelect[i]);
                tvTabs[i].setTextColor(ContextCompat.getColor(this, R.color.text_tab_selected));
            } else {
                ivTabs[i].setImageResource(resTabImageUnSelect[i]);
                tvTabs[i].setTextColor(ContextCompat.getColor(this, R.color.text_tab_unselected));
            }
        }
        if (currentPageIndex != position) {
            currentPageIndex = position;
            mViewPager.setCurrentItem(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) <= 2000) {
                // 退出APP
                finishSelf();
            } else {
                Toast.makeText(this, getString(R.string.toast_home_exit_system), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public static void finishSelf() {
        if (instance != null && !instance.isFinishing()) {
            instance.finish();
            instance = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDateUI(MessageEvent event) {
        if (event.getMessage().equals("tabManageFragment")) {
            actionTabItemSelect(1);
        }
        if (event.getMessage().equals("tabHomeFragment")) {
            actionTabItemSelect(0);
        }
    }

    @Override
    protected void onDestroy() {
        LeakedUtils.fixTextLineCacheLeak();
        IMMLeaks.fixFocusedViewLeak(getApplication());
        if (instance != null) instance = null;
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        super.onStop();
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                // refresh conversation list
                if (mTabFragments[1] != null) {
                    ((ConversationListFragment) mTabFragments[1]).refresh();
                }
            }
        });
    }

    /**
     * update unread message count
     */
    private void updateUnreadLabel() {
//        int count = getUnreadMsgCountTotal();
//        if (currentPageIndex != 1 && count > 0) {
//            ivNotification.setVisibility(View.VISIBLE);
//        } else {
//            ivNotification.setVisibility(View.GONE);
//        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrPageIndex", currentPageIndex);
    }

    @Override
    public void onTabChange(int position) {
        actionTabItemSelect(position);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LL.i("MainActivity -- onNewIntent()");
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mTabFragments[position];
        }

        @Override
        public int getCount() {
            return TAB_NUMBER;
        }

        @Override
        public long getItemId(int position) {
            // 获取当前数据的hashCode
            return mTabFragments[position].hashCode();
        }
    }


    private class MyCheckNewVersionTask extends AsyncTask<String, Integer, AppVersionInfo> {

        @Override
        protected AppVersionInfo doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            AppVersionInfo info;
            String versionName = AppUtils.getVersionName(MainActivity.this);
            String qudao = AppUtils.getMetaDataFromApplication(MainActivity.this, MLConfig.KEY_CHANNEL_NAME);
            try {
                Log.e("版本号=", versionName + "渠道=" + qudao);
                info = mService.checkNewVersion(versionName, qudao);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                info = new AppVersionInfo();
                info.setErrorCode("0");
            }
            return info;
        }

        @Override
        protected void onPostExecute(AppVersionInfo info) {
            if (StringUtils.isEmpty(info.getErrorCode()) || info.getErrorCode().equals("0")) {
                return;
            }
            if (info.getErrorCode().equals("1")) {

                String qiangzhigengxin = info.getQiangzhigengxin();
                Log.e("强制更新", qiangzhigengxin);

                showNewVersionDialog(qiangzhigengxin, info.getTitle(), info.getContent(), info.getDownloadUrl());
            } else if (info.getErrorCode().equals("2")) {
                //T.showShort(MainActivity.this, info.getMessage());

            }
        }
    }

    private void showNewVersionDialog(final String gengxin, String title, String content, final String downloadUrl) {

        UpdateAPPAlertDialog dialog = new UpdateAPPAlertDialog(this);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setConfirmClickListener(new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final UpdateAPPAlertDialog sweetAlertDialog) {
                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean success) throws Exception {
                                if (success) {
                                    startDownloadAppService(downloadUrl);
                                    sweetAlertDialog.startDownload();
                                } else {
                                    sweetAlertDialog.dismiss();
                                }
                            }
                        });
            }
        });
        dialog.setCancelClickListener(new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(UpdateAPPAlertDialog sweetAlertDialog) {

                if (gengxin.equals("1")) {
                    T.showShort(MainActivity.this, "请更新版本后重试");
                } else {
                    sweetAlertDialog.dismiss();
                }

            }
        });

        dialog.setCancelDownloadListener(new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(UpdateAPPAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                stopDownloadAppService(downloadUrl);
            }
        });

        dialog.show();
        if (gengxin.equals("1")) {
            dialog.setCancelable(false);
            dialog.hideTvCancelDownLoad();
        } else {
            dialog.setCancelable(true);
        }

    }


    private void startDownloadAppService(String downloadUrl) {
        Intent intent = new Intent(MainActivity.this, DownloadAppService.class);
        Bundle args = new Bundle();
        args.putString(MLConfig.KEY_BUNDLE_DOWNLOAD_URL, downloadUrl);
        intent.putExtras(args);
        MainActivity.this.startService(intent);
    }

    private void stopDownloadAppService(String downloadUrl) {
        OkHttpUtils.getInstance().cancelTag(DownloadAppService.FILENAME);
    }
}
