package com.bj.hmxxteacher.reason.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.reason.model.Reason;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import static com.bj.hmxxteacher.api.HttpUtilService.BASE_RESOURCE_URL;

/**
 * Created by Administrator on 2018/7/13 0013.
 */

public class ReasonAdapter extends BaseQuickAdapter<Reason.DataBean,BaseViewHolder> {

    public ReasonAdapter(int layoutResId, @Nullable List<Reason.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Reason.DataBean item) {
        helper.setText(R.id.tv_reasonName, item.getLiyou());

        SimpleDraweeView sv =helper.getView(R.id.sv_img);
        if(item.getCode().equals("-1")){
            Glide.with(mContext).load(R.mipmap.ic_reason_edit).into(sv);
        }else {
            Glide.with(mContext).load(BASE_RESOURCE_URL+item.getImg()).into(sv);
        }

        helper.addOnClickListener(R.id.bt_add);

    }

}
