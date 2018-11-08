package com.bj.hmxxteacher.email.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.api.MLConfig;
import com.bj.hmxxteacher.email.model.Letter;
import com.bj.hmxxteacher.email.model.Reply;
import com.bj.hmxxteacher.email.view.IViewEmail;
import com.bj.hmxxteacher.email.view.IViewReply;
import com.bj.hmxxteacher.mvp.Presenter;
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

public class ReplyPresenter extends Presenter {

    private Context mContext;
    private IViewReply iView;

    public ReplyPresenter(Context context, IViewReply iView) {
        this.mContext = context;
        this.iView = iView;
    }

    public void getReply(final String id,final int page){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"xinxiang/xinxianginfo")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("limit","10")
                        .params("offset",String.valueOf((page) *10))
                        .params("xinjianid",id)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("信件结果", str);

                                e.onNext(str);
                                e.onComplete();
                            }
                        });

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String reply) throws Exception {

                        iView.getEmailReply(reply);
                    }
                });

    }

    public void sendReply(final String phone,final String content,final String xinjianid){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                OkGo.<String>post(BASE_API_URL+"xinxiang/huifuset")
                        .params("appkey", MLConfig.HTTP_APP_KEY)
                        .params("userphone",phone)
                        .params("content",content)
                        .params("xinjianid",xinjianid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                String str = response.body().toString();
                                Log.e("发送回复结果",str);
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
                        iView.sendReply(s);
                    }
                });
    }


    @Override
    public void onDestory() {
        mContext = null;
        iView = null;
    }
}
