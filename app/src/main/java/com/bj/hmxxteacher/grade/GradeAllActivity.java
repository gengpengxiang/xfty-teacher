package com.bj.hmxxteacher.grade;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.adapter.PopwindowAdapter;
import com.bj.hmxxteacher.grade.adapter.ClassAdapter;
import com.bj.hmxxteacher.utils.ScreenUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.widget.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GradeAllActivity extends BaseActivity {

    @BindView(R.id.header_img_back)
    ImageView headerImgBack;
    @BindView(R.id.header_tv_title)
    TextView headerTvTitle;

    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    private Unbinder unbinder;

    private ClassAdapter gradeAdapter;
    private List<String> list = new ArrayList<>();

    private CustomPopWindow mPopWindow;
    private PopwindowAdapter popwindowAdapter;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_all);
        unbinder = ButterKnife.bind(this);

        initTitleBar();

        String[] cates = new String[]{
                "点赞", "待改进"
        };
        list.add("一年级1班");
        list.add("二年级");
        list.add("三年级");
        list.add("四年级");




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.recycler_item_popwindow, list);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cates);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);





//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        //mRecyclerView.addItemDecoration(new SpacesItemDecoration(2));
//        gradeAdapter = new ClassAdapter(R.layout.recycler_item_grade_info, dataList);
//        mRecyclerView.setAdapter(gradeAdapter);
//        gradeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(GradeAllActivity.this, ReasonActivity.class);
//                intent.putExtra("studentid", "1101");
//                intent.putExtra("reasontype", "1");//班级评价原因的type
//                startActivity(intent);
//            }
//        });

    }

    private void initTitleBar() {
        headerImgBack.setVisibility(View.VISIBLE);
        headerTvTitle.setVisibility(View.VISIBLE);
        headerTvTitle.setText("班级管理");
        toolbar.setBackgroundColor(Color.parseColor("#4aa003"));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R.id.tv_position)
    public void onClick() {
        T.showLong(GradeAllActivity.this,"click");
        showPosition();
    }

    private void showPosition() {

        list.add("一年级1班");
        list.add("二年级");
        list.add("三年级");
        list.add("四年级");

//        View view = LayoutInflater.from(GradeAllActivity.this).inflate(R.layout.layout_popwindow, null);
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
//        popwindowAdapter = new PopwindowAdapter(R.layout.recycler_item_popwindow, list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(GradeAllActivity.this));
//        recyclerView.setAdapter(popwindowAdapter);
//        popwindowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                T.showLong(GradeAllActivity.this,list.get(position));
//            }
//        });
//        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
//        mPopupWindow.showAsDropDown(tvPosition, 0, 2);
    }
}
