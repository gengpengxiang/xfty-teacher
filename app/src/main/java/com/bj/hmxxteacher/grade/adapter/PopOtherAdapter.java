package com.bj.hmxxteacher.grade.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.grade.model.Grade;
import com.bj.hmxxteacher.grade.model.Other;
import com.bj.hmxxteacher.zzautolayout.utils.AutoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PopOtherAdapter extends BaseQuickAdapter<Other,BaseViewHolder> {


    public PopOtherAdapter(int layoutResId, @Nullable List<Other> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Other item) {

        helper.setText(R.id.tv_item_popwindow,item.getNamezh());

    }

}
