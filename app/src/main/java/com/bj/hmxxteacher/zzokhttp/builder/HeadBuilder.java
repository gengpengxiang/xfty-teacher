package com.bj.hmxxteacher.zzokhttp.builder;


import com.bj.hmxxteacher.zzokhttp.OkHttpUtils;
import com.bj.hmxxteacher.zzokhttp.request.OtherRequest;
import com.bj.hmxxteacher.zzokhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
