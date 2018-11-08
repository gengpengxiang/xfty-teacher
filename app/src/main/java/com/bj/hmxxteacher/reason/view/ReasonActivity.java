package com.bj.hmxxteacher.reason.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.reason.fragment.ReasonFragment1;
import com.bj.hmxxteacher.reason.fragment.ReasonFragment2;
import com.bj.hmxxteacher.widget.ViewPagerTriangleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReasonActivity extends AppCompatActivity {


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.vpti_main_tab)
    ViewPagerTriangleIndicator mViewPagerTriangleIndicator;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    private Unbinder unbinder;

    private String studentid;

    private List<String> mTitles = Arrays.asList("积极向上", "有待改进");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reason);
        unbinder = ButterKnife.bind(this);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) relativeLayout.getLayoutParams();
        int width = getScreenWidth(this);
        int height = getScreenHeight(this);
        params.width = (int) (width * 0.7);
        params.height = (int) (height * 0.7);
        relativeLayout.setLayoutParams(params);

        setupViewPager(viewPager);

        mViewPagerTriangleIndicator.setPageTitle(mTitles);
        //绑定ViewPager
        mViewPagerTriangleIndicator.setViewPagerWithIndicator(viewPager);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static int getScreenWidth(Activity context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private void setupViewPager(ViewPager viewPager) {
        InfoAdapter adapter = new InfoAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReasonFragment1(), "积极向上");

        adapter.addFragment(new ReasonFragment2(), "有待改进");
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.bt_cancel)
    public void onClick() {
        finish();
    }

    class InfoAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public InfoAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
