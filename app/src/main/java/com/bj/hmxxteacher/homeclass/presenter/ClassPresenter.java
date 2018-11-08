package com.bj.hmxxteacher.homeclass.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.activity.LoginActivity;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.bean.TeacherInfos;
import com.bj.hmxxteacher.grade.view.IViewGrade;
import com.bj.hmxxteacher.homeclass.view.IViewClass;
import com.bj.hmxxteacher.mvp.Presenter;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bj.hmxxteacher.api.HttpUtilService.BASE_API_URL;
import static com.bj.hmxxteacher.api.HttpUtilService.BASE_RESOURCE_URL;
import static com.bj.hmxxteacher.api.HttpUtilService.BASE_URL;

public class ClassPresenter extends Presenter {

    private Context mContext;
    private IViewClass iView;

    public ClassPresenter(Context context, IViewClass iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getClassGuanli(final String teacherphone) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "js/getlink")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("teacherphone", teacherphone)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("关联班级结果", str);
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

    public void getClassInfo(final String teacherphone,final String classcode) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "js/classdata")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("classcode", classcode)
                        .params("teacherphone", teacherphone)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("关联班级详细", str);
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
                        iView.getClassInfo(reason);


                    }
                });
    }

    public void getClassDynamic(final String classcode,final int page) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL + "js/classzxdt")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("classcode", classcode)
                        .params("limit", "10")
                        .params("offset",String.valueOf(page*10))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("最新动态结果", str);
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
                        iView.getClassDynamic(reason);


                    }
                });
    }

    public void getTeacherInfo(final String phoneNumber) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_URL+"js/getteacherinfo")
                        .params("appkey",MLConfig.HTTP_APP_KEY)
                        .params("teacherphone",phoneNumber)
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
                        TeacherInfos teacherInfo = JSON.parseObject(s,new TypeReference<TeacherInfos>(){});

                        if(teacherInfo.getRet().equals("1")){
                            TeacherInfos.DataBean dataBean = teacherInfo.getData();

                            //账号权限type  2所有权限   1只有管理  0只有班级首页
                            PreferencesUtils.putString(mContext, MLProperties.PREFER_KEY_DEFAULT_CLASSCODE, dataBean.getMoren_classcode());

                            iView.getTeacherInfo(dataBean.getMoren_classcode());

                        }
                    }
                });

    }



    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}
