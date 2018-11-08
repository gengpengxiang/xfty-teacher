package com.bj.hmxxteacher.email.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.email.model.Letter;
import com.bj.hmxxteacher.email.model.Reply;
import com.bj.hmxxteacher.email.view.IViewEmail;
import com.bj.hmxxteacher.entity.BaseDataInfo;
import com.bj.hmxxteacher.mvp.Presenter;
import com.bj.hmxxteacher.reason.model.Reason;
import com.bj.hmxxteacher.reason.view.IViewReason;
import com.bj.hmxxteacher.widget.LoadingDialog;
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

public class EmailPresenter extends Presenter {

    private Context mContext;
    private IViewEmail iView;

    public EmailPresenter(Context context, IViewEmail iView) {
        this.mContext = context;
        this.iView = iView;
    }

    /**
     *
     *{"type":"","info":"weidu,yidu,huifu,xingbiao"}
     *
     *   .cacheKey("reason"+type)
     .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
     */
    public void getEmailList(final int page,final String type) {

        final LoadingDialog loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"xinxiang/xinxianglist")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("limit","10")
                        .params("offset",String.valueOf((page) *10))
                        .params("xueqi_data","201809")
                        .params("type",type)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("信件结果", str);

                                e.onNext(str);
                                e.onComplete();
                            }

                            @Override
                            public void onFinish() {
                                e.onComplete();
                            }
                        });



            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {

                        loadingDialog.dismiss();
                        iView.getEmailList(result);

                    }
                });
    }

    public void xingbiao(final String id,final String type){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"xinxiang/xinjianstar")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("xinjianid",id)
                        .params("type",type)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("星标结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }

                            @Override
                            public void onFinish() {
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        iView.xingbiao(s);
                    }
                });

    }

    public void delete(final String id){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"xinxiang/xinjiandel")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("xinjianid",id)
                        .params("type","xiaozhang")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("删除结果", str);
                                e.onNext(str);
                                e.onComplete();
                            }

                            @Override
                            public void onFinish() {
                                e.onComplete();
                            }
                        });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        iView.delete(s);
                    }
                });

    }


    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}
