package com.bj.hmxxteacher.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.bj.hmxxteacher.BaseFragment;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.activity.LoginActivity;
import com.bj.hmxxteacher.activity.MainActivity;
import com.bj.hmxxteacher.activity.SettingActivity;
import com.bj.hmxxteacher.api.LmsDataService;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.dialog.CancelConfirmAlertDialog;
import com.bj.hmxxteacher.dialog.CancelConfirmAlertDialog4;
import com.bj.hmxxteacher.dialog.InviteOthersAlertDialog;
import com.bj.hmxxteacher.dialog.UpdateAPPAlertDialog;
import com.bj.hmxxteacher.email.EmailActivity;
import com.bj.hmxxteacher.entity.AppVersionInfo;
import com.bj.hmxxteacher.grade.GradeAllActivity;
import com.bj.hmxxteacher.manager.UMPushManager;
import com.bj.hmxxteacher.service.DownloadAppService;
import com.bj.hmxxteacher.utils.AppUtils;
import com.bj.hmxxteacher.utils.LL;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.zzimgselector.view.ImageSelectorActivity;
import com.bj.hmxxteacher.zzokhttp.OkHttpUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMClient;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.bj.hmxxteacher.utils.BitmapUtils.changeFileSize;

/**
 * Created by zz379 on 2017/4/7.
 * "我的" 页面
 */

public class UserFragment extends BaseFragment {

    @BindView(R.id.header_tv_title)
    TextView tvTitle;
    @BindView(R.id.img_kidPhoto)
    SimpleDraweeView imgUserPhoto;
    @BindView(R.id.img_schoolBg)
    SimpleDraweeView imgSchoolBg;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone_number)
    TextView tvUserPhoneNumber;
    @BindView(R.id.tv_schoolName)
    TextView tvSchoolName;
    @BindView(R.id.tv_versionName)
    TextView tvVersionName;
    @BindView(R.id.rl_clearclass)
    RelativeLayout rlClearClass;
    @BindView(R.id.sv_content)
    ScrollView mScrollView;
    @BindView(R.id.rl_email)
    RelativeLayout rlEmail;

    private String userPhotoPath;
    private String userName, userPhoneNumber, userSchoolName, userClassName;
    private String classCode;
    private String schoolID;
    private String schoolImg;

    private long currMillis = 0;

    private final CompositeDisposable disposables = new CompositeDisposable();
    // 是否已经绑定班级：1-已绑定、0-未绑定
    private String classLinked;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_info, container, false);
        ButterKnife.bind(this, view);

        initToolbar();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void bindViews(View view) {

    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

    }

    private void initToolbar() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.bottom_tab_2));
        // 头图
        imgSchoolBg.setImageURI(Uri.parse("res:///" + R.mipmap.bg_user_banner));
        // 版本信息
        tvVersionName.setText("V" + AppUtils.getVersionName(getActivity()));
    }

    private void initView() {
        userPhotoPath = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_TEACHER_IMG,"");
        userName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_NAME,"");
        userSchoolName = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_NAME, "");
        classCode = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_KID_ID, "");
        schoolID = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_CODE, "");
        schoolImg = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_IMG, "");
        // 是否绑定班级
        classLinked = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_CLASS_LINKED, "0");
        userPhoneNumber = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID, "");

        // 用户头像
        if (!StringUtils.isEmpty(userPhotoPath)) {
            imgUserPhoto.setImageURI(Uri.parse(userPhotoPath));
        }
        //  用户名称
        if (StringUtils.isEmpty(userName)) {
            tvUserName.setText("暂无");
        } else {
            tvUserName.setText(userName);
        }
        // 用户手机号
        tvUserPhoneNumber.setText(userPhoneNumber);

        // 学校名称
        if (StringUtils.isEmpty(userSchoolName)) {
            tvSchoolName.setText("未绑定学校");
        } else {
            tvSchoolName.setText(userSchoolName);
        }

        if ("1".equals(classLinked)) {
            rlClearClass.setVisibility(View.VISIBLE);
        } else {
            rlClearClass.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
            userPhotoPath = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_TEACHER_IMG);
            if (!StringUtils.isEmpty(userPhotoPath)) {
                imgUserPhoto.setImageURI(Uri.parse(userPhotoPath));
            }
        } else {
            onInVisible();
        }
    }

    @OnClick(R.id.img_kidPhoto)
    void clickUserPhoto() {
        MobclickAgent.onEvent(getActivity(), "mine_changeAvatar");
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean success) {
                        if (success) {
                            actionSelectKidPhoto();
                        } else {
                            T.showShort(getActivity(), "未获取到相机权限");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.rl_question)
    void clickQuestion() {
        if (System.currentTimeMillis() - currMillis > 1000) {
            CancelConfirmAlertDialog dialog = new CancelConfirmAlertDialog(getActivity())
                    .setTitleText("联系客服")
                    .setContentText("拨打客服电话 " + getString(R.string.custome_service_phonenumber))
                    .setConfirmText("拨打")
                    .setCancelClickListener(new CancelConfirmAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(CancelConfirmAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new CancelConfirmAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(CancelConfirmAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            actionCallPhone(getString(R.string.custome_service_phonenumber));
                        }
                    });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            currMillis = System.currentTimeMillis();
        }
    }

    /**
     * 拨打电话 -->> 需要动态申请权限
     *
     * @param phoneNumber
     */
    private void actionCallPhone(final String phoneNumber) {
        MobclickAgent.onEvent(getActivity(), "mine_customerService");
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CALL_PHONE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Boolean success) {
                        if (success) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            startActivity(intent);
                        } else {
                            // 未授权拨打电话
                            LL.i("未授权拨打电话");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.rl_inviteParents)
    void clickInviteParents() {
        MobclickAgent.onEvent(getActivity(), "mine_inviteParent");
        if (System.currentTimeMillis() - currMillis > 1000) {
            showInviteDialog(R.mipmap.ic_invite_parent, R.string.invite_parents_content);
            currMillis = System.currentTimeMillis();
        }
    }

    @OnClick(R.id.rl_inviteTeachers)
    void clickInviteTeachers() {
        MobclickAgent.onEvent(getActivity(), "mine_inviteTeacher");
        if (System.currentTimeMillis() - currMillis > 1000) {
            showInviteDialog(R.mipmap.ic_invite_teacher, R.string.invite_teachers_content);
            currMillis = System.currentTimeMillis();
        }
    }

    private void showInviteDialog(int resID, final int contentID) {
        InviteOthersAlertDialog dialog = new InviteOthersAlertDialog(getActivity())
                .setContentImage(ContextCompat.getDrawable(getActivity(), resID))
                .setCancelText("去QQ粘贴")
                .setConfirmText("去微信粘贴")
                .setCancelClickListener(new InviteOthersAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(InviteOthersAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        actionCallQQ(getString(contentID));
                    }
                })
                .setConfirmClickListener(new InviteOthersAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(InviteOthersAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        actionCallWeixin(getString(contentID));
                    }
                });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void actionCallQQ(String content) {
        // 复制内容到剪切板
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(data);

        // 检查是否安装了QQ
        if (isQQClientAvailable(getActivity())) {
            // Intent 打开QQ
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
            startActivity(intent);
        } else {
            T.showShort(getActivity(), "已复制成功，请到QQ粘贴并发送邀请");
        }
    }

    private void actionCallWeixin(String content) {
        // 复制内容到剪切板
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(data);

        // 检查是否安装了微信
        if (isWeixinAvilible(getActivity())) {
            // Intent 打开微信
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            startActivity(intent);
        } else {
            T.showShort(getActivity(), "已复制成功，请到微信粘贴并发送邀请");
        }
    }

    /***
     * 检查是否安装了微信
     * @param context
     * @return
     */
    private boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                LogUtils.e("pn = " + pn);
                if (pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnClick(R.id.rl_clearclass)
    void clickClearClassData() {
        if (System.currentTimeMillis() - currMillis > 1000) {
            CancelConfirmAlertDialog4 dialog = new CancelConfirmAlertDialog4(getActivity())
                    .setTitleText("重置数据说明")
                    .setContentText("选择确定，即表示您要重新开始为学生记录数据，系统将把您的班级页面中所有班级和学生的数据置为0，数据重新开始累计。\n此功能适用于学期初或每月初，要重新开始记录学生评价数据的情况。\n注意重置数据是清空所有您带的班级的数据哟~如果需要找回数据请微信联系：pkugame")
                    .setCancelClickListener(new CancelConfirmAlertDialog4.OnSweetClickListener() {
                        @Override
                        public void onClick(CancelConfirmAlertDialog4 sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new CancelConfirmAlertDialog4.OnSweetClickListener() {
                        @Override
                        public void onClick(CancelConfirmAlertDialog4 sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            clearAllClassData();
                        }
                    });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            currMillis = System.currentTimeMillis();
        }
    }

    private void clearAllClassData() {
        Observable.create(new ObservableOnSubscribe<String[]>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String[]> e) throws Exception {
                LmsDataService service = new LmsDataService();
                String[] result = service.clearAllClassData(userPhoneNumber);
                e.onNext(result);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoadingDialog();
                    }

                    @Override
                    public void onNext(@NonNull String[] strings) {
                        if (!StringUtils.isEmpty(strings[1])) {
                            T.showShort(getActivity(), strings[1]);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        hideLoadingDialog();
                        T.showShort(getActivity(), "服务器开小差了，请稍后重试");
                    }

                    @Override
                    public void onComplete() {
                        hideLoadingDialog();
                    }
                });
    }

    @OnClick(R.id.rl_about)
    void clickAbout() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rl_check_version)
    void clickCheckNewVersion() {
        MobclickAgent.onEvent(getActivity(), "mine_checkVersion");
        if (System.currentTimeMillis() - currMillis > 1000) {
            MyCheckNewVersionTask versionTask = new MyCheckNewVersionTask();
            versionTask.execute();
            currMillis = System.currentTimeMillis();
        }
    }

    @OnClick(R.id.btn_logout)
    void clickLogout() {
        MobclickAgent.onEvent(getActivity(), "mine_loginOut");
        // 删除手机号和设备的关联关系
        UMPushManager.getInstance().removePushAlias(userPhoneNumber);
        UMPushManager.getInstance().removeAllTag(schoolID);
        // 清空缓存
        cleanAllPreferencesData();
        // 退出环信
        EMClient.getInstance().logout(true);
        // 跳转到登录页面
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        // 自动退出MainActivity
        MainActivity.finishSelf();
    }

    private void cleanAllPreferencesData() {
        // 清除所有app内的数据
        PreferencesUtils.cleanAllData(getActivity());
        // 清除环信数据
        getActivity().getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE).edit().clear().apply();
    }

    private void actionSelectKidPhoto() {
        ImageSelectorActivity.start(this, 1, ImageSelectorActivity.MODE_SINGLE
                , true, true, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Activity.RESULT_FIRST_USER) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // do something
            String filePath = images.get(0);
            Log.i("way", "FilePath:" + filePath);
            if (filePath != null && !filePath.equals("")) {
                File file = new File(filePath);
                Log.i("way", "FileSize:" + file.length() / 1024 + "KB");
                String kidPicturePath = changeFileSize(filePath);
                File newFile = new File(kidPicturePath);
                Log.i("way", "newFileSize:" + newFile.length() / 1024 + "KB");
                if (!StringUtils.isEmpty(kidPicturePath)) {
                    UpdateUserPhotoTask task = new UpdateUserPhotoTask();
                    task.execute(kidPicturePath);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @OnClick({R.id.rl_email, R.id.rl_grade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_email:
                Intent intent = new Intent(getActivity(), EmailActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_grade:
                Intent intent2 = new Intent(getActivity(), GradeAllActivity.class);
                startActivity(intent2);
                break;
        }
    }


    private class MyCheckNewVersionTask extends AsyncTask<String, Integer, AppVersionInfo> {

        @Override
        protected AppVersionInfo doInBackground(String... params) {
            LmsDataService mService = new LmsDataService();
            AppVersionInfo info;
            String versionName = AppUtils.getVersionName(getActivity());
            String qudao = AppUtils.getMetaDataFromApplication(getActivity(), MLConfig.KEY_CHANNEL_NAME);
            try {
                Log.e("版本号=",versionName+"渠道="+qudao);
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

                showNewVersionDialog(qiangzhigengxin,info.getTitle(), info.getContent(), info.getDownloadUrl());
            } else if (info.getErrorCode().equals("2")) {
                T.showShort(getActivity(), info.getMessage());
            }
        }
    }

    private void showNewVersionDialog(final String gengxin, String title, String content, final String downloadUrl) {

        Log.e("user也更新",gengxin);
        createUpdateAppDialog(title, content, new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final UpdateAPPAlertDialog sweetAlertDialog) {
                RxPermissions rxPermissions = new RxPermissions(getActivity());
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
        }, new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(UpdateAPPAlertDialog sweetAlertDialog) {
                //sweetAlertDialog.dismiss();
                sweetAlertDialog.dismiss();



            }
        }, new UpdateAPPAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(UpdateAPPAlertDialog sweetAlertDialog) {
                // cancel Download
                sweetAlertDialog.dismiss();
                stopDownloadAppService(downloadUrl);
            }
        });
    }

    private void startDownloadAppService(String downloadUrl) {
        Intent intent = new Intent(getActivity(), DownloadAppService.class);
        Bundle args = new Bundle();
        args.putString(MLConfig.KEY_BUNDLE_DOWNLOAD_URL, downloadUrl);
        intent.putExtras(args);
        getActivity().startService(intent);
    }

    private void stopDownloadAppService(String downloadUrl) {
        OkHttpUtils.getInstance().cancelTag(DownloadAppService.FILENAME);
    }

    private class UpdateUserPhotoTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String kidPicturePath = params[0];
            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.uploadKidPhoto(userPhoneNumber, kidPicturePath);
            } catch (Exception e) {
                e.printStackTrace();
                LL.e(e);
                result = new String[3];
                result[0] = "0";
                result[1] = "服务器开小差了，请待会重试";
                result[2] = kidPicturePath;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            hideLoadingDialog();
            if (StringUtils.isEmpty(result[0]) || result[0].equals("0")) {
                T.showShort(getActivity(), StringUtils.isEmpty(result[1]) ? "服务器开小差了，请待会重试" : result[1]);
            } else {
                T.showShort(getActivity(), "头像更新成功");
                userPhotoPath = result[1];
                PreferencesUtils.putString(getActivity(), MLProperties.BUNDLE_KEY_TEACHER_IMG, userPhotoPath);
                if (!StringUtils.isEmpty(userPhotoPath)) {
                    imgUserPhoto.setImageURI(Uri.parse(userPhotoPath));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        //initView();
        MobclickAgent.onPageStart("mine");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("mine");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /********* 在fragment中切换tab *********/

    private ChangeBottomTabListener bottomTabListener;

    public void setBottomTabListener(ChangeBottomTabListener bottomTabListener) {
        this.bottomTabListener = bottomTabListener;
    }
}
