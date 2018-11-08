package com.bj.hmxxteacher.grade.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.grade.model.Grade;
import com.bj.hmxxteacher.grade.model.Position;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PopGradeAdapter extends BaseQuickAdapter<Grade.DataBean,BaseViewHolder> {



    public PopGradeAdapter(int layoutResId, @Nullable List<Grade.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Grade.DataBean item) {
        helper.setText(R.id.tv_item_popwindow, item.getNianji_name());
    }

}
