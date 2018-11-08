package com.bj.hmxxteacher.email.adapter;

import android.support.annotation.Nullable;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.email.model.Reply;
import com.bj.hmxxteacher.utils.Base64Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

public class ReplyAdapter extends BaseQuickAdapter<Reply.DataBean.HuifuListBean,BaseViewHolder> {

    public Boolean longclick = false;

    public ReplyAdapter(int layoutResId, @Nullable List<Reply.DataBean.HuifuListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Reply.DataBean.HuifuListBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_date, item.getTime());
        helper.setText(R.id.tv_content, Base64Util.decode(item.getHuifu_content()));


    }

}
