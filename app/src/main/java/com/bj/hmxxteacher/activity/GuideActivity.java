package com.bj.hmxxteacher.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.adapter.GuideFragmentPagerAdapter;
import com.bj.hmxxteacher.fragment.Guide1Fragment;
import com.bj.hmxxteacher.fragment.Guide2Fragment;
import com.bj.hmxxteacher.fragment.Guide3Fragment;
import com.bj.hmxxteacher.utils.ScreenUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zz379 on 2017/8/11.
 * 导航页，首次安装或版本更新后进入
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initStatus();
        initView();
    }

    @Override
    protected void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            // 如果存在虚拟按键，则设置虚拟按键的背景色
            if (ScreenUtils.isNavigationBarShow(this)) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, android.R.color.black));
            }
        }
    }

    @Override
    protected void initView() {
        List<Fragment> dataList = new ArrayList<>();
        dataList.add(new Guide1Fragment());
        dataList.add(new Guide2Fragment());
        dataList.add(new Guide3Fragment());

        GuideFragmentPagerAdapter mAdapter = new GuideFragmentPagerAdapter(getSupportFragmentManager(), dataList);

        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
