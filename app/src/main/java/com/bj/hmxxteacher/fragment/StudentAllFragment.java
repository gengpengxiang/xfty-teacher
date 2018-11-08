package com.bj.hmxxteacher.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bj.hmxxteacher.BaseFragment;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.activity.ReasonEditActivity;
import com.bj.hmxxteacher.activity.StudentDetailActivity;
import com.bj.hmxxteacher.adapter.AllStudentAdapter;
import com.bj.hmxxteacher.api.LmsDataService;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.dialog.CommendReasonAlertDialog;
import com.bj.hmxxteacher.dialog.CommendReasonInfo;
import com.bj.hmxxteacher.entity.ClassItemInfo;
import com.bj.hmxxteacher.entity.MessageEvent;
import com.bj.hmxxteacher.reason.view.ReasonActivity;
import com.bj.hmxxteacher.utils.LL;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.utils.T;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.scwang.smartrefresh.layout.constant.RefreshState.Loading;
import static com.scwang.smartrefresh.layout.constant.RefreshState.Refreshing;

/**
 * Created by zz379 on 2017/2/3.
 * 学生列表页面
 */

public class StudentAllFragment extends BaseFragment {

    SmartRefreshLayout mSmartRefreshLayout;
    RecyclerView mRecyclerView;

    private AllStudentAdapter mAdapter;
    private int currentPage = 1;
    private List<ClassItemInfo> mDataList = new ArrayList<>();

    private String classId;
    private String userPhoneNumber;
    private String orderby;

    List<CommendReasonInfo> reasonList = new ArrayList<>();
    // 全班学生
    private String gradeTypeID = "0";
    private long currMillis = 0;


    public StudentAllFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_student_all, container, false);
    }

    @Override
    protected void bindViews(View view) {
        mSmartRefreshLayout = (SmartRefreshLayout)view.findViewById(R.id.mSmartRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        // look as listview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // set Adatper
        mAdapter = new AllStudentAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);


//        mXRefreshView.setEmptyView(R.layout.recycler_item_news_empty_3);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 1;
                getAllStudents(gradeTypeID,String.valueOf(currentPage));
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                getAllStudents(gradeTypeID,String.valueOf(currentPage));
            }
        });

    }

    @Override
    protected void setListener() {
        mAdapter.setOnMyItemClickListener(new AllStudentAdapter.OnMyItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                intent2StudentDetailActivity(mDataList.get(position));
            }

            @Override
            public void onCommendClick(View view, int position) {
                MobclickAgent.onEvent(getActivity(), "class_thumb");

                Intent intent = new Intent(getActivity(), ReasonActivity.class);
                intent.putExtra("id", mDataList.get(position).getStudId());
                intent.putExtra("reasontype","student");//学生评价的type
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processLogic() {
        userPhoneNumber = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID, "");
        classId = getArguments().getString(MLConfig.KEY_CLASS_ID, "");
        orderby = getArguments().getString(MLConfig.KEY_CLASS_STUDENTS_ORDERBY, "");

        currentPage = 1;

        getAllStudents(gradeTypeID,String.valueOf(currentPage));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        MobclickAgent.onPageStart("allStudent");
    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        MobclickAgent.onPageEnd("allStudent");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("commentsuccess")) {

            currentPage = 1;
            getAllStudents(gradeTypeID,String.valueOf(currentPage));
        }
    }

    private void getAllStudents(final String type, final String pageIndex) {
        Observable.create(new ObservableOnSubscribe<List<ClassItemInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ClassItemInfo>> e) throws Exception {
                LmsDataService mService = new LmsDataService();
                List<ClassItemInfo> dataList = mService.getClassAllStudentFromAPI(classId, type, pageIndex, orderby);

                e.onNext(dataList);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ClassItemInfo>>() {
                    @Override
                    public void accept(List<ClassItemInfo> classItemInfos) throws Exception {

                        if(mSmartRefreshLayout.getState()==Refreshing){
                            mSmartRefreshLayout.finishRefresh();
                        }
                        if(mSmartRefreshLayout.getState()==Loading){
                            mSmartRefreshLayout.finishLoadmore();
                        }


                        if (currentPage == 1) {
                            mDataList.clear();
                            mDataList.addAll(classItemInfos);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mDataList.addAll(classItemInfos);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }


    private void intent2StudentDetailActivity(ClassItemInfo student) {
        Intent intent = new Intent(getActivity(), StudentDetailActivity.class);
        Bundle args = new Bundle();
        args.putString(MLConfig.KEY_STUDENT_ID, student.getStudId());
        args.putString(MLConfig.KEY_STUDENT_NAME, student.getStudName());
        args.putString(MLConfig.KEY_STUDENT_PHOTO, student.getStudImg());
        args.putString(MLConfig.KEY_STUDENT_SCORE, student.getStudScore());
        args.putString(MLConfig.KEY_STUDENT_BADGE, student.getStudBadge());
        args.putString(MLConfig.KEY_STUDENT_GRADE, student.getStudGrade());
        args.putString(MLConfig.KEY_STUDENT_PINGYU, student.getStudPingyu());
        intent.putExtras(args);
        startActivity(intent);
    }


    private void parseCommendReasons(String result) {
        reasonList.clear();
        try {
            JSONArray resultArray = new JSONArray(result);
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject reasonObj = resultArray.optJSONObject(i);
                String reasonID = reasonObj.optString("id");
                String reasonTitle = reasonObj.optString("title");
                reasonList.add(new CommendReasonInfo(reasonID, reasonTitle));
            }
            // 添加编辑理由的功能
            reasonList.add(new CommendReasonInfo("-1", "编辑理由"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCommendReason() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                LmsDataService mService = new LmsDataService();
                String result;
                try {
                    result = mService.getCommendReasonFromAPI(userPhoneNumber);
                    Log.e("2结果", result);
                    PreferencesUtils.putString(getActivity(), "CommendReason", result);
                } catch (Exception e) {
                    e.printStackTrace();
                    LL.e(e);
                    result = "";
                    emitter.onError(e);
                }
                emitter.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                        parseCommendReasons(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
