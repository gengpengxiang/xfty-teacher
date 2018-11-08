package com.bj.hmxxteacher.reason.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.entity.BaseDataInfo;
import com.bj.hmxxteacher.mvp.Presenter;
import com.bj.hmxxteacher.reason.model.Reason;
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

public class ReasonPresenter extends Presenter {

    private Context mContext;
    private IViewReason iView;

    public ReasonPresenter(Context context, IViewReason iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getReasonList(final String teacherphone, final String type,final String reasontype) {

        Log.e("请求参数=","type="+type+"banji_type="+reasontype);

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"dianzan/dzliyou")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("teacherphone",teacherphone)
                        .params("type",type)
                        .params("banji_type",reasontype)
                        .cacheKey("reason"+type+reasontype)
                        .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("原因结果", str);

                                e.onNext(str);
//                                e.onComplete();
                            }

                            @Override
                            public void onCacheSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("原因缓存", str);

                                e.onNext(str);
                               // e.onComplete();
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                e.onComplete();
                            }
                        });



            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reason) throws Exception {

                        Log.e("成功了","111");
                            iView.getReasonInfo(reason);


                    }
                });
    }

    public void commentStudent(final String teacherphone, final String liyou,final String studentid,final String type) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"dianzan/dianzan")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("teacherphone",teacherphone)
                        .params("type",type)
                        .params("liyou",liyou)
                        .params("studentid",studentid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("评价结果", str);
                                BaseDataInfo baseDataInfo = JSON.parseObject(str, new TypeReference<BaseDataInfo>() {
                                });
                                e.onNext(baseDataInfo.getRet());
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {

                        iView.comment(result);
                    }
                });
    }

    public void commentClass(final String teacherphone, final String liyou,final String id,final String type) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"dianzan/dianzan_class")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("teacherphone",teacherphone)
                        .params("class_code",id)
                        .params("type",type)
                        .params("liyou",liyou)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("评价结果", str);
                                BaseDataInfo baseDataInfo = JSON.parseObject(str, new TypeReference<BaseDataInfo>() {
                                });
                                e.onNext(baseDataInfo.getRet());
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {

                        iView.comment(result);
                    }
                });
    }


    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}
