package com.bj.hmxxteacher.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.adapter.LatestNewsAdapter2;
import com.bj.hmxxteacher.api.LmsDataService;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.bean.StudentInfos;
import com.bj.hmxxteacher.dialog.CommendReasonAlertDialog;
import com.bj.hmxxteacher.dialog.CommendReasonInfo;
import com.bj.hmxxteacher.entity.ClassNewsInfo;
import com.bj.hmxxteacher.entity.KidDataInfo;
import com.bj.hmxxteacher.entity.MessageEvent;
import com.bj.hmxxteacher.reason.view.ReasonActivity;
import com.bj.hmxxteacher.utils.LL;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.utils.T;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
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

import static com.bj.hmxxteacher.api.HttpUtilService.BASE_API_URL;
import static com.bj.hmxxteacher.api.HttpUtilService.BASE_URL;
import static com.scwang.smartrefresh.layout.constant.RefreshState.Loading;
import static com.scwang.smartrefresh.layout.constant.RefreshState.Refreshing;

/**
 * Created by zz379 on 2017/2/3.
 * 学生详情页面，点击学生列表的学生头像进入
 */

public class StudentDetailActivity extends BaseActivity implements View.OnClickListener {


    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;
    private LatestNewsAdapter2 mAdapter;
    private int currentPage = 1;
    public static long lastRefreshTime;
    private List<ClassNewsInfo> mDataList = new ArrayList<>();

    private String classId;
    private String className;
    private TextView tvTitle;
    private String userPhoneNumber;

    private LinearLayout llCommendStudent;
    private View headerView;

    private SimpleDraweeView imgStudPhoto;
    private TextView tvStudPingyu;
    private TextView tvStudScore;
    private TextView tvStudBadge;
    private TextView tvStudGrade;
    private TextView tvStudGradePro;
    private String studId;
    private String studName;
    private String studPhoto;
    private String studScore;
    private String studBadge;
    private String studBadgePro;
    private String studGrade;
    private String studPingyu;

    List<CommendReasonInfo> reasonList = new ArrayList<>();
    private TextView tvAddScore;
    private SimpleDraweeView ivSchoolPic;
    private String schoolImg;

    private long currMillis = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        EventBus.getDefault().register(this);
        // 初始化页面
        initToolBar();
        initView();
        initData();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        TextView tvTitle = (TextView) this.findViewById(R.id.header_tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("学生主页");

        LinearLayout llHeaderLeft = (LinearLayout) this.findViewById(R.id.header_ll_left);
        llHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentDetailActivity.this.finish();
            }
        });

        ImageView imgBack = (ImageView) this.findViewById(R.id.header_img_back);
        imgBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initView() {
        llCommendStudent = (LinearLayout) this.findViewById(R.id.ll_qrCode);
        llCommendStudent.setOnClickListener(this);
        mSmartRefreshLayout = (SmartRefreshLayout) this.findViewById(R.id.mSmartRefreshLayout);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        // look as listview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // set Adatper
        mAdapter = new LatestNewsAdapter2(mDataList);

        headerView = mAdapter.setHeaderView(R.layout.recycler_header_student_detail_2, mRecyclerView);
        initHeaderView();

        mRecyclerView.setAdapter(mAdapter);


        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 1;
                getKidData();
                getCommentList(String.valueOf(currentPage));
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                getCommentList(String.valueOf(currentPage));
            }
        });

    }

    private void initHeaderView() {
        imgStudPhoto = (SimpleDraweeView) headerView.findViewById(R.id.img_kidPhoto);
        ivSchoolPic = (SimpleDraweeView) headerView.findViewById(R.id.img_classBg);
        tvStudPingyu = (TextView) headerView.findViewById(R.id.tv_kid_pingyu);
        tvStudScore = (TextView) headerView.findViewById(R.id.tv_jifen);
        tvStudBadge = (TextView) headerView.findViewById(R.id.tv_huizhang);
        tvStudGrade = (TextView) headerView.findViewById(R.id.tv_dengji);
        tvAddScore = (TextView) headerView.findViewById(R.id.tv_addJifen);
        tvStudGradePro = (TextView) headerView.findViewById(R.id.tv_zhuanxiang);//班级
    }

    private void updateHeaderView() {
        if (!StringUtils.isEmpty(schoolImg)) {
            ivSchoolPic.setImageURI(Uri.parse(schoolImg));
        }
        if (!StringUtils.isEmpty(studPhoto)) {
            imgStudPhoto.setImageURI(Uri.parse(studPhoto));
        }
        // tvStudPingyu.setText(studPingyu);
        // tvStudScore.setText("评价 " + studScore);
        // tvStudBadge.setText("徽章 " + studBadge);
        tvStudGrade.setText(studGrade + "等级");
    }

    @Override
    protected void initData() {
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID, "");
        schoolImg = PreferencesUtils.getString(this, MLProperties.BUNDLE_KEY_SCHOOL_IMG, "");

        Bundle args = getIntent().getExtras();
        studId = args.getString(MLConfig.KEY_STUDENT_ID);
        studName = args.getString(MLConfig.KEY_STUDENT_NAME);
        studPhoto = args.getString(MLConfig.KEY_STUDENT_PHOTO);
        studScore = args.getString(MLConfig.KEY_STUDENT_SCORE);
        studBadge = args.getString(MLConfig.KEY_STUDENT_BADGE);
        studGrade = args.getString(MLConfig.KEY_STUDENT_GRADE);
        studPingyu = args.getString(MLConfig.KEY_STUDENT_PINGYU);

        updateHeaderView();

        currentPage = 1;

        getCommentList(String.valueOf(currentPage));
        getKidData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("student");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("student");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("commentsuccess")) {
            currentPage = 1;
            getCommentList(String.valueOf(currentPage));
            getKidData();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_qrCode) {
            MobclickAgent.onEvent(StudentDetailActivity.this, "student_thumb");
            if (System.currentTimeMillis() - currMillis > 1000) {


                Intent intent = new Intent(StudentDetailActivity.this, ReasonActivity.class);
                intent.putExtra("id", studId);
                intent.putExtra("reasontype","student");//学生评价的type
                startActivity(intent);

                currMillis = System.currentTimeMillis();
            }
        }
    }

    private void getCommentList(final String pageIndex) {
        Observable.create(new ObservableOnSubscribe<List<ClassNewsInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ClassNewsInfo>> e) throws Exception {
                LmsDataService mService = new LmsDataService();
                List<ClassNewsInfo> infoList = mService.getStudentAllNewsFromAPI(studId, pageIndex, studPhoto);
                e.onNext(infoList);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ClassNewsInfo>>() {
                    @Override
                    public void accept(List<ClassNewsInfo> result) throws Exception {
                        if (result == null) {

                            T.showShort(StudentDetailActivity.this, "服务器开小差了，请重试");
                        } else {
                            if (mSmartRefreshLayout.getState() == Refreshing) {
                                mSmartRefreshLayout.finishRefresh();
                            }
                            if (mSmartRefreshLayout.getState() == Loading) {
                                mSmartRefreshLayout.finishLoadmore();
                            }
                            if (currentPage == 1) {
                                mDataList.clear();
                                mDataList.addAll(result);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mDataList.addAll(result);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }


    private class MyCommentTask extends AsyncTask<String, Integer, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String studentId = params[0];
            String reasonId = params[1];
            String teacherId = params[2];

            LmsDataService mService = new LmsDataService();
            String[] result;
            try {
                result = mService.commendStudentFromAPI(teacherId, studentId, reasonId, "0");
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
            T.showShort(StudentDetailActivity.this, result[1]);
            if (!StringUtils.isEmpty(result[0]) && result[0].equals("1")) {
                actionAddScore("1");
            }
        }
    }

    private Handler mHandler = new Handler();

    private void actionAddScore(final String value) {
        tvAddScore.setText("+" + value);
        animationTextView(tvAddScore, value);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                studScore = String.valueOf(Integer.valueOf(StringUtils.isEmpty(studScore) ? "0" : studScore) + Integer.valueOf(value));
                tvStudScore.setText("评价 " + (StringUtils.isEmpty(studScore) ? "0" : studScore));
            }
        }, 900);

    }

    private void animationTextView(final TextView tv, String value) {
        tv.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -0.3f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        translateAnimation.setDuration(1000);
        animationSet.addAnimation(translateAnimation);

        tv.startAnimation(animationSet);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setVisibility(View.GONE);
            }
        }, 800);
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

    private void getKidData() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "jz/getdata")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("studentid", studId)
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
                        Log.e("获取学生数据", s);
                        StudentInfos studentInfo = JSON.parseObject(s, new TypeReference<StudentInfos>() {
                        });
                        if(studentInfo.getRet().equals("1")){
                            StudentInfos.DataBean dataBean = studentInfo.getData();
                            tvStudScore.setText("点赞 "+dataBean.getDianzan()+"");
                            tvStudBadge.setText("徽章 "+dataBean.getHuizhang());
                            tvStudGradePro.setText("待改进 "+dataBean.getGaijin());

                            tvStudGrade.setText(dataBean.getDengji() + "等级");
                            tvStudPingyu.setText(dataBean.getPingyu());
                        }
                    }
                });

       /* Observable.create(new ObservableOnSubscribe<KidDataInfo>() {
            @Override
            public void subscribe(ObservableEmitter<KidDataInfo> e) throws Exception {
                LmsDataService mService = new LmsDataService();
                KidDataInfo kidDataInfo = mService.getStudentDataFromAPI(studId);
                e.onNext(kidDataInfo);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<KidDataInfo>() {
                    @Override
                    public void accept(KidDataInfo result) throws Exception {
                        String errorCode = result.getErrorCode();
                        String message = result.getMessage();
                        if (errorCode.equals("0") || errorCode.equals("2")) {
                            T.showShort(StudentDetailActivity.this, message);
                        } else {
                            String kidScore = result.getScore();
                            tvStudScore.setText("评价" + (StringUtils.isEmpty(kidScore) || kidScore.equals("0") ? "" : " " + kidScore));
                            studScore = kidScore;

                            String kidBadge = result.getBadge();
                            tvStudBadge.setText("徽章" + (StringUtils.isEmpty(kidBadge) || kidBadge.equals("0") ? "" : " " + kidBadge));
                            studBadge = kidBadge;

                            String kidBadgePro = result.getBadgePro();
                            tvStudGradePro.setText("班级" + (StringUtils.isEmpty(kidBadgePro) || kidBadgePro.equals("0") ? "" : " " + kidBadgePro));
                            studBadgePro = kidBadgePro;

                            String kidGrade = result.getGrade();
                            tvStudGrade.setText((StringUtils.isEmpty(kidGrade) ? "" : kidGrade) + "等级");
                            studGrade = kidGrade;

                            String kidPingYu = (StringUtils.isEmpty(result.getPingyu()) ? "你准备好了吗？" : result.getPingyu());
                            tvStudPingyu.setText(kidPingYu);
                            studPingyu = kidPingYu;
                        }
                    }
                });*/
    }


}
