package com.bj.hmxxteacher;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bj.hmxxteacher.dialog.UpdateAPPAlertDialog;
import com.bj.hmxxteacher.dialog.UpdateAPPAlertDialog2;
import com.bj.hmxxteacher.utils.ScreenUtils;
import com.bj.hmxxteacher.widget.LoadingDialog;
import com.bj.hmxxteacher.zzautolayout.AutoLayoutActivity;
import com.jaeger.library.StatusBarUtil;
import com.umeng.message.PushAgent;

/**
 * Created by he on 2016/11/9.
 */

public class BaseActivity extends AutoLayoutActivity {

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        PushAgent.getInstance(this).onAppStart();
    }

    protected void initToolBar() {
        initStatus();
    }

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initStatus() {
        // 设置顶部状态栏颜色
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            // 如果存在虚拟按键，则设置虚拟按键的背景色
            if (ScreenUtils.isNavigationBarShow(this)) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }
    }

    /**
     * 1、获取main在窗体的可视区域
     * 2、获取main在窗体的不可视区域高度
     * 3、判断不可视区域高度
     * 1、大于100：键盘显示  获取Scroll的窗体坐标
     * 算出main需要滚动的高度，使scroll显示。
     * 2、小于100：键盘隐藏
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 显示加载对话框
     */
    public void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void createUpdateAppDialog(String title, String content, UpdateAPPAlertDialog.OnSweetClickListener listener, UpdateAPPAlertDialog.OnSweetClickListener confirmListener,
                                      UpdateAPPAlertDialog.OnSweetClickListener cancelListener) {
        UpdateAPPAlertDialog dialog = new UpdateAPPAlertDialog(this);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setConfirmClickListener(confirmListener);
        dialog.setCancelClickListener(cancelListener);

        dialog.setCancelable(false);

        dialog.show();
    }

    public void createUpdateAppDialog2(String title, String content, UpdateAPPAlertDialog2.OnSweetClickListener listener, UpdateAPPAlertDialog2.OnSweetClickListener confirmListener,
                                      UpdateAPPAlertDialog2.OnSweetClickListener cancelListener) {
        UpdateAPPAlertDialog2 dialog = new UpdateAPPAlertDialog2(this);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setConfirmClickListener(confirmListener);
        dialog.setCancelClickListener(cancelListener);

        dialog.setCancelable(false);

        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
