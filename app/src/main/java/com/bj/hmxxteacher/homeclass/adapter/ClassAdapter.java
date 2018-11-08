package com.bj.hmxxteacher.homeclass.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.grade.model.ClassInfo;
import com.bj.hmxxteacher.homeclass.model.ClassGuanli;
import com.bj.hmxxteacher.zzautolayout.utils.AutoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ClassAdapter extends BaseQuickAdapter<ClassGuanli.DataBean,BaseViewHolder> {


    public ClassAdapter(int layoutResId, @Nullable List<ClassGuanli.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassGuanli.DataBean item) {
        helper.setText(R.id.tv_item_popwindow, item.getClass_name());

    }

}
