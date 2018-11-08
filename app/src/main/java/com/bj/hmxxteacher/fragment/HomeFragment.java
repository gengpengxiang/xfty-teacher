package com.bj.hmxxteacher.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.activity.BadgeDetailActivity;
import com.bj.hmxxteacher.activity.CommendDetailActivity;
import com.bj.hmxxteacher.activity.MainActivity;
import com.bj.hmxxteacher.activity.StudentAllActivity;
import com.bj.hmxxteacher.activity.StudentGradeActivity;
import com.bj.hmxxteacher.activity.ThanksDetailActivity;
import com.bj.hmxxteacher.activity.ThanksNotifiActivity;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.entity.MessageEvent;
import com.bj.hmxxteacher.homeclass.adapter.ClassAdapter;
import com.bj.hmxxteacher.homeclass.adapter.DynamicAdapter;
import com.bj.hmxxteacher.homeclass.adapter.StudentRankAdapter;
import com.bj.hmxxteacher.homeclass.model.ClassGuanli;
import com.bj.hmxxteacher.homeclass.model.ClassInfo;
import com.bj.hmxxteacher.homeclass.model.Dynamic;
import com.bj.hmxxteacher.homeclass.model.StudentRank;
import com.bj.hmxxteacher.homeclass.presenter.ClassPresenter;
import com.bj.hmxxteacher.homeclass.view.IViewClass;
import com.bj.hmxxteacher.reason.view.ReasonActivity;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.widget.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bj.hmxxteacher.api.HttpUtilService.BASE_RESOURCE_URL;

public class HomeFragment extends Fragment implements IViewClass, View.OnClickListener {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.bt_change)
    TextView btChange;
    @BindView(R.id.header_notification)
    FrameLayout headerNotification;


    //headview相关控件
    private SimpleDraweeView svClassPic;
    private TextView tvClassName, tvBadgeNum, tvClassNum, tvStudentNum, tvThanksNum, tvClassDianzanNum, tvClassGaijinNum,
            tvStudentDianzanNum, tvStudentGaijinNum;
    private FrameLayout layoutChakan;
    private RecyclerView recyclerViewDengji;
    private StudentRankAdapter studentRankAdapter;
    private List<StudentRank> studentRankList = new ArrayList<>();

    private List<ClassGuanli.DataBean> classList = new ArrayList<>();
    private PopupWindow mPopupWindow;

    private ClassPresenter presenter;
    private String userPhoneNumber;
    private ClassAdapter classAdapter;//切换班级

    private DynamicAdapter dynamicAdapter;//动态
    private List<Dynamic.DataBean> dynamicList = new ArrayList<>();
    private View headView;


    private String className;
    private String classcode;
    private String schoolImgUrl;
    private int currentPage = 0;
    private String accountType;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        userPhoneNumber = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID, "");
        accountType = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_ACCOUNT_TYPE, "");
        presenter = new ClassPresenter(getActivity(), this);

        if (accountType.equals("0")) {
            presenter.getClassGuanli(userPhoneNumber);
        }

        if (PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_DEFAULT_CLASSCODE) == null) {
            presenter.getTeacherInfo(userPhoneNumber);
        } else {
            classcode = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_DEFAULT_CLASSCODE);
            presenter.getClassInfo(userPhoneNumber, classcode);
        }


        initPopWindow();
        initRecyclerHeadView();
        initViews();

        schoolImgUrl = PreferencesUtils.getString(getActivity(), MLProperties.BUNDLE_KEY_SCHOOL_IMG, "");

        return view;

    }

    private void initRecyclerHeadView() {
        headView = getActivity().getLayoutInflater().inflate(R.layout.recycler_homeclass_headview, null);

        svClassPic = (SimpleDraweeView) headView.findViewById(R.id.sv_classPic);
        //tvSubject = (TextView) headView.findViewById(R.id.tv_subject);
        tvClassName = (TextView) headView.findViewById(R.id.tv_className);
        tvBadgeNum = (TextView) headView.findViewById(R.id.tv_badgeNum);
        tvClassNum = (TextView) headView.findViewById(R.id.tv_classNum);
        tvStudentNum = (TextView) headView.findViewById(R.id.tv_studentNum);
        tvThanksNum = (TextView) headView.findViewById(R.id.tv_thanksNum);
        tvClassDianzanNum = (TextView) headView.findViewById(R.id.tv_class_dianzanNum);
        tvClassGaijinNum = (TextView) headView.findViewById(R.id.tv_class_gaijinNum);
        tvStudentDianzanNum = (TextView) headView.findViewById(R.id.tv_student_dianzanNum);
        tvStudentGaijinNum = (TextView) headView.findViewById(R.id.tv_student_gaijinNum);

        recyclerViewDengji = (RecyclerView) headView.findViewById(R.id.mRecyclerView_dengji);
        recyclerViewDengji.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewDengji.setLayoutManager(linearLayoutManager);
        studentRankAdapter = new StudentRankAdapter(R.layout.recycler_item_dengji, studentRankList);
        recyclerViewDengji.setAdapter(studentRankAdapter);
        studentRankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), StudentGradeActivity.class);
                Bundle args = new Bundle();
                args.putString(MLConfig.KEY_CLASS_ID, classcode);
                args.putString(MLConfig.KEY_GRADE_ID, String.valueOf(position + 1));
                args.putString(MLConfig.KEY_GRADE_NAME, studentRankList.get(position).getName());
                intent.putExtras(args);
                startActivity(intent);
            }
        });


        headView.findViewById(R.id.tab1).setOnClickListener(this);
        headView.findViewById(R.id.tab2).setOnClickListener(this);
        headView.findViewById(R.id.tab3).setOnClickListener(this);
        headView.findViewById(R.id.tab4).setOnClickListener(this);

        headView.findViewById(R.id.layout_commend_class).setOnClickListener(this);
        headView.findViewById(R.id.layout_commend_student).setOnClickListener(this);
        headView.findViewById(R.id.layout_chakan).setOnClickListener(this);


    }


    private void initViews() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dynamicAdapter = new DynamicAdapter(R.layout.recycler_item_homeclass_dynamic, dynamicList);
        dynamicAdapter.addHeaderView(headView);
        dynamicAdapter.setEmptyView(R.layout.recycler_item_news_empty_home, mRecyclerView);
        dynamicAdapter.setHeaderAndEmpty(true);

        dynamicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.right_menu_confirm:
                        T.showShort(getActivity(), "删除");

                        // 获取当前父级位置
//                        int cp = getParentPosition(person);
// 通过父级位置找到当前list，删除指定下级
//                        ((Level1Item) getData().get(position)).removeSubItem(person);
// 列表层删除相关位置的数据
                        dynamicAdapter.getData().remove(dynamicList.get(position));
// 更新视图
                        dynamicAdapter.notifyDataSetChanged();


                        break;
                    case R.id.content:
                        T.showShort(getActivity(), "取消");
                        break;
                }
            }
        });

        //滑动删除
//        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(dynamicAdapter);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
//        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // 开启滑动删除
//        dynamicAdapter.enableSwipeItem();
//        dynamicAdapter.setOnItemSwipeListener(onItemSwipeListener);

        mRecyclerView.setAdapter(dynamicAdapter);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getClassInfo(userPhoneNumber, classcode);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getClassDynamic(classcode, currentPage);
            }
        });

    }


    private void initPopWindow() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popwindow, null);
        RecyclerView popRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        popRecyclerView.addItemDecoration(new SpacesItemDecoration(1));
        classAdapter = new ClassAdapter(R.layout.recycler_item_popwindow, classList);
        classAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPopupWindow.dismiss();
                mSmartRefreshLayout.autoRefresh();
                currentPage = 0;
                className = classList.get(position).getClass_name();
                classcode = classList.get(position).getClasscode();
                presenter.getClassInfo(userPhoneNumber, classcode);
            }
        });

        popRecyclerView.setAdapter(classAdapter);
        popRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getClassList(String result) {
        ClassGuanli classGuanli = JSON.parseObject(result, new TypeReference<ClassGuanli>() {
        });

        if (classGuanli.getRet().equals("1")) {
//            className = classGuanli.getData().get(0).getClass_name();
//            classcode = classGuanli.getData().get(0).getClasscode();
//            presenter.getClassInfo(userPhoneNumber, classcode);

            classList.clear();
            classList.addAll(classGuanli.getData());
            classAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getClassInfo(String result) {

        if (mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }

        ClassInfo classInfo = JSON.parseObject(result, new TypeReference<ClassInfo>() {
        });
        if (classInfo.getRet().equals("1")) {
            ClassInfo.DataBean bean = classInfo.getData();

            setHeadviewDatas(bean);
            presenter.getClassDynamic(classcode, currentPage);
        }

    }

    @Override
    public void getClassDynamic(String result) {

        if (mSmartRefreshLayout.isLoading()) {
            mSmartRefreshLayout.finishLoadmore();
        }

        Dynamic dynamic = JSON.parseObject(result, new TypeReference<Dynamic>() {
        });
        if (dynamic.getRet().equals("1")) {
            if (currentPage == 0) {
                dynamicList.clear();
            }
            dynamicList.addAll(dynamic.getData());
            dynamicAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void getTeacherInfo(String result) {
        classcode = result;
        presenter.getClassInfo(userPhoneNumber, classcode);
    }

    private void setHeadviewDatas(ClassInfo.DataBean bean) {


        svClassPic.setImageURI(schoolImgUrl);
        Log.e("学校图片地址", schoolImgUrl);

        //tvSubject.setText(bean.getTeacher_xueke());
        tvClassName.setText(bean.getClass_name());

        tvBadgeNum.setText(bean.getClass_badge());
        tvClassNum.setText(bean.getClass_pingjia());
        tvStudentNum.setText(bean.getClass_score());
        tvThanksNum.setText(bean.getClass_ganxie());
        tvClassDianzanNum.setText(bean.getClass_pingjia_dianzan());
        tvClassGaijinNum.setText(bean.getClass_pingjia_gaijin());
        tvStudentDianzanNum.setText(bean.getClass_dianzan());
        tvStudentGaijinNum.setText(bean.getClass_gaijin());

        studentRankList.clear();
        studentRankList.add(new StudentRank(bean.getGrade_1_name(), bean.getClass_grade_1()));
        studentRankList.add(new StudentRank(bean.getGrade_2_name(), bean.getClass_grade_2()));
        studentRankList.add(new StudentRank(bean.getGrade_3_name(), bean.getClass_grade_3()));
        studentRankList.add(new StudentRank(bean.getGrade_4_name(), bean.getClass_grade_4()));
        studentRankList.add(new StudentRank(bean.getGrade_5_name(), bean.getClass_grade_5()));

        studentRankAdapter.notifyDataSetChanged();
    }


    @OnClick({R.id.bt_change, R.id.header_notification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_change:
                //账号权限type  2所有权限   1只有管理  0只有班级首页
                //修改后
                if (accountType.equals("0")) {
                    mPopupWindow.showAsDropDown(btChange, 0, 2);

                } else {
                    EventBus.getDefault().post(new MessageEvent("tabManageFragment"));
                }

                break;
            case R.id.header_notification:
                Intent intent1 = new Intent(getActivity(), ThanksNotifiActivity.class);
                startActivity(intent1);
                break;
            case R.id.tab1:
                Intent intent2 = new Intent(getActivity(), BadgeDetailActivity.class);
                intent2.putExtra(MLConfig.KEY_CLASS_ID, classcode);
                startActivity(intent2);
                break;
            case R.id.tab2:
                T.showShort(getActivity(), "蜂蜜：" + tvClassNum.getText().toString());
                break;
            case R.id.tab3:
                //T.showShort(getActivity(),"敬请期待");
                Intent intent4 = new Intent(getActivity(), CommendDetailActivity.class);
                Bundle args = new Bundle();
                args.putString(MLConfig.KEY_CLASS_ID, classcode);
                intent4.putExtras(args);
                startActivity(intent4);
                break;
            case R.id.tab4:
                Intent intent5 = new Intent(getActivity(), ThanksDetailActivity.class);
                intent5.putExtra(MLConfig.KEY_CLASS_ID, classcode);
                startActivity(intent5);
                break;
            case R.id.layout_commend_class:
                Intent intent6 = new Intent(getActivity(), ReasonActivity.class);
                intent6.putExtra("id", classcode);
                intent6.putExtra("reasontype", "class");//班级评价原因的type
                startActivity(intent6);

                break;
            case R.id.layout_commend_student:
                Intent intent7 = new Intent(getActivity(), StudentAllActivity.class);
                intent7.putExtra(MLConfig.KEY_CLASS_ID, classcode);
                startActivity(intent7);
                break;
            case R.id.layout_chakan:
                Intent intent8 = new Intent(getActivity(), StudentAllActivity.class);
                intent8.putExtra(MLConfig.KEY_CLASS_ID, classcode);
                intent8.putExtra(MLProperties.BUNDLE_KEY_VIEWPAGER_INDEX, 1);
                startActivity(intent8);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("commentsuccess")) {
            currentPage = 0;
            presenter.getClassInfo(userPhoneNumber, classcode);
        }
        if (event.getMessage().equals("tabHomeFragment")) {
            mSmartRefreshLayout.autoRefresh();
            className = event.getParam2();
            classcode = event.getParam1();
            presenter.getClassInfo(userPhoneNumber, classcode);

        }
    }
}
