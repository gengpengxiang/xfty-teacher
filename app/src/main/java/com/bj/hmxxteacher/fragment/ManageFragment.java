package com.bj.hmxxteacher.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.entity.MessageEvent;
import com.bj.hmxxteacher.grade.adapter.ClassAdapter;
import com.bj.hmxxteacher.grade.adapter.PopGradeAdapter;
import com.bj.hmxxteacher.grade.adapter.PopOtherAdapter;
import com.bj.hmxxteacher.grade.adapter.PopPositionAdapter;
import com.bj.hmxxteacher.grade.model.ClassInfo;
import com.bj.hmxxteacher.grade.model.Grade;
import com.bj.hmxxteacher.grade.model.Other;
import com.bj.hmxxteacher.grade.model.Position;
import com.bj.hmxxteacher.grade.presenter.GradePresenter;
import com.bj.hmxxteacher.grade.view.IViewGrade;
import com.bj.hmxxteacher.reason.view.ReasonActivity;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.widget.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zz379 on 2017/8/11.
 * 导航页
 */

public class ManageFragment extends Fragment implements IViewGrade {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.header_tv_title)
    TextView headerTvTitle;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_dianzan)
    TextView tvDianzan;
    @BindView(R.id.spinner1)
    LinearLayout spinner1;
    @BindView(R.id.spinner2)
    LinearLayout spinner2;
    @BindView(R.id.spinner3)
    LinearLayout spinner3;

    private ClassAdapter classAdapter;
    private List<ClassInfo.DataBean> classList = new ArrayList<>();

    private List<Position.DataBean> positionList = new ArrayList<>();
    private List<Grade.DataBean> gradeList = new ArrayList<>();
    private PopPositionAdapter popPositionAdapter;
    private PopGradeAdapter popGradeAdapter;
    private PopOtherAdapter popOtherAdapter;
    private PopupWindow mPopupWindow;
    private String userPhoneNumber;
    private GradePresenter presenter;
    private RecyclerView popRecyclerView;
    private ArrayList<Other> otherList;

    private String juese_code, nianji_code;
    private String other_tiaojian = "dianzan";
    private int currentPage = 0;
    private String accountType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_grade_all, container, false);
        unbinder = ButterKnife.bind(this, view);

        userPhoneNumber = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID, "");
        accountType = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_ACCOUNT_TYPE, "");
        headerTvTitle.setVisibility(View.VISIBLE);
        headerTvTitle.setText("班级管理");

        presenter = new GradePresenter(getActivity(), this);
        presenter.getPositions(userPhoneNumber);

        initPopWindow();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new SpacesItemDecoration(2));
        classAdapter = new ClassAdapter(R.layout.recycler_item_grade_info, classList);
        mRecyclerView.setAdapter(classAdapter);

        classAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //账号权限type  2所有权限   1只有管理  0只有班级首页
                if(accountType.equals("0")){
                    T.showLong(getActivity(),"敬请期待");

                }else {
                    EventBus.getDefault().post(new MessageEvent("tabHomeFragment",classList.get(position).getClasscode(),classList.get(position).getName()));
                }
            }
        });

        classAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ReasonActivity.class);
                intent.putExtra("id", classList.get(position).getClasscode());
                intent.putExtra("reasontype", "class");//班级评价原因的type
                startActivity(intent);
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getClassList(userPhoneNumber, juese_code, nianji_code, other_tiaojian, currentPage);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getClassList(userPhoneNumber, juese_code, nianji_code, other_tiaojian, currentPage);
            }
        });

        screen();//获取筛选结果

        return view;
    }

    private void screen() {

    }

    private void initPopWindow() {

        otherList = new ArrayList<>();
        otherList.clear();

        //otherList.add(new Other("点赞", "排序"));
        otherList.add(new Other("点赞", "点赞"));
        otherList.add(new Other("待改进", "待改进"));
        otherList.add(new Other("总分", "总分"));

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popwindow, null);

        popRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        popRecyclerView.addItemDecoration(new SpacesItemDecoration(1));

        popPositionAdapter = new PopPositionAdapter(R.layout.recycler_item_popwindow, positionList);
        popGradeAdapter = new PopGradeAdapter(R.layout.recycler_item_popwindow, gradeList);
        popOtherAdapter = new PopOtherAdapter(R.layout.recycler_item_popwindow, otherList);
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        popPositionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                mSmartRefreshLayout.autoRefresh();

                currentPage = 0;
                juese_code = positionList.get(position).getJuese_code();
                mPopupWindow.dismiss();
                tvPosition.setText(positionList.get(position).getJuese_name());
                presenter.getGrades(positionList.get(position).getJuese_code());
            }
        });
        popGradeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                mSmartRefreshLayout.autoRefresh();

                currentPage = 0;
                nianji_code = gradeList.get(position).getNianji_code();
                mPopupWindow.dismiss();
                tvGrade.setText(gradeList.get(position).getNianji_name());
            }
        });

        popOtherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                mSmartRefreshLayout.autoRefresh();

                currentPage = 0;
                other_tiaojian = otherList.get(position).getName();
                mPopupWindow.dismiss();
                tvDianzan.setText(otherList.get(position).getNamezh());
            }
        });

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));

        View bottomView = view.findViewById(R.id.bottomView);
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestory();
    }

    @OnClick(R.id.tv_position)
    public void onClick() {

    }

    @Override
    public void getPositions(String result) {

        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }


        Position position = JSON.parseObject(result, new TypeReference<Position>() {
        });
        if (position.getRet().equals("1")) {
            positionList.clear();
            positionList.addAll(position.getData());
            popRecyclerView.setAdapter(popPositionAdapter);

            popPositionAdapter.notifyDataSetChanged();

            if (position.getData() != null && position.getData().size() != 0) {
                String juse_name = position.getData().get(0).getJuese_name();
                tvPosition.setText(juse_name);
                juese_code = position.getData().get(0).getJuese_code();
                presenter.getGrades(juese_code);
            }
        }

    }

    @Override
    public void getGrades(String result) {

        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }

        Grade grade = JSON.parseObject(result, new TypeReference<Grade>() {
        });
        if (grade.getRet().equals("1")) {
            gradeList.clear();
            gradeList.addAll(grade.getData());
            popRecyclerView.setAdapter(popGradeAdapter);
            popPositionAdapter.notifyDataSetChanged();
            if (grade.getData().size() != 0) {
                String nianji_name = grade.getData().get(0).getNianji_name();
                tvGrade.setText(nianji_name);
                nianji_code = grade.getData().get(0).getNianji_code();

                presenter.getClassList(userPhoneNumber, juese_code, nianji_code, other_tiaojian, currentPage);
            }
        }
    }

    @Override
    public void getClassList(String result) {

        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }

        if(mSmartRefreshLayout.isLoading()){
            mSmartRefreshLayout.finishLoadmore();
        }

        ClassInfo classInfo = JSON.parseObject(result, new TypeReference<ClassInfo>() {
        });
        if (classInfo.getRet().equals("1")) {
            //T.showShort(getActivity(), classInfo.getMsg());
            if (currentPage == 0) {
                classList.clear();
                classAdapter.notifyDataSetChanged();
            }
        }
        if (classInfo.getRet().equals("2")) {
            if (currentPage == 0) {
                classList.clear();

            }
            classList.addAll(classInfo.getData());
            classAdapter.notifyDataSetChanged();
        }
    }


    @OnClick({R.id.spinner1, R.id.spinner2, R.id.spinner3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.spinner1:
                popRecyclerView.setAdapter(popPositionAdapter);
                mPopupWindow.showAsDropDown(spinner1, 0, 2);
                break;
            case R.id.spinner2:
                popRecyclerView.setAdapter(popGradeAdapter);
                mPopupWindow.showAsDropDown(spinner2, 0, 2);
                break;
            case R.id.spinner3:
                popRecyclerView.setAdapter(popOtherAdapter);
                mPopupWindow.showAsDropDown(spinner3, 0, 2);
                break;
        }
    }
}
