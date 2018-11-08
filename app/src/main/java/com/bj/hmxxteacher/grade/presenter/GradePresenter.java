package com.bj.hmxxteacher.grade.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.entity.BaseDataInfo;
import com.bj.hmxxteacher.grade.view.IViewGrade;
import com.bj.hmxxteacher.mvp.Presenter;
import com.bj.hmxxteacher.reason.view.IViewReason;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxteacher.api.HttpUtilService.BASE_API_URL;

public class GradePresenter extends Presenter {

    private Context mContext;
    private IViewGrade iView;

    public GradePresenter(Context context, IViewGrade iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getPositions(final String teacherphone) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"js_classguanli/classguanli_juese")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("xueqicode","201809")
                        .params("teacherphone",teacherphone)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("角色筛选结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }

                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) throws Exception {
                            iView.getPositions(reason);


                    }
                });
    }

    public void getGrades(final String juese_code) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"js_classguanli/classguanli_grade")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("xueqicode","201809")
                        .params("juese_code",juese_code)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("年级筛选结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }

                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) throws Exception {
                        iView.getGrades(reason);
                    }
                });
    }

    public void getClassList(final String teacherphone,final String juese_code,final String nianji_code,final String type,final int page) {
        Log.e("参数","phone="+teacherphone+"juese="+juese_code+"nanji="+nianji_code+"type="+type+"page="+page);

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"js_classguanli/index")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("teacherphone",teacherphone)
                        .params("xueqicode","201809")
                        .params("juese_code",juese_code)
                        .params("grade",nianji_code)
                        .params("type",type)
                        .params("limit","10")
                        .params("offset",String.valueOf((page) *10))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("班级信息筛选结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }

                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) throws Exception {
                        iView.getClassList(reason);
                    }
                });
    }




    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}
