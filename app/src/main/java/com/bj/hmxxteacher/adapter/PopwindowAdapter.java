package com.bj.hmxxteacher.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.email.model.Reply;
import com.bj.hmxxteacher.grade.model.GradeGuanlian;
import com.bj.hmxxteacher.utils.Base64Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PopwindowAdapter extends BaseQuickAdapter<GradeGuanlian.DataBean,BaseViewHolder> {



    public PopwindowAdapter(int layoutResId, @Nullable List<GradeGuanlian.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GradeGuanlian.DataBean item) {
        helper.setText(R.id.tv_item_popwindow, item.getClass_name());



    }

}
