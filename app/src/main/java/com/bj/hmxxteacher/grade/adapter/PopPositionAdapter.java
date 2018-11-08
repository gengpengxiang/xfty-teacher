package com.bj.hmxxteacher.grade.adapter;

import android.support.annotation.Nullable;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.grade.model.Position;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PopPositionAdapter extends BaseQuickAdapter<Position.DataBean,BaseViewHolder> {



    public PopPositionAdapter(int layoutResId, @Nullable List<Position.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Position.DataBean item) {
        helper.setText(R.id.tv_item_popwindow, item.getJuese_name());



    }

}
