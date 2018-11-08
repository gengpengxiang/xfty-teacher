package com.bj.hmxxteacher.grade.adapter;

import android.support.annotation.Nullable;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.grade.model.ClassInfo;
import com.bj.hmxxteacher.grade.model.Grade;
import com.bj.hmxxteacher.utils.Base64Util;
import com.bj.hmxxteacher.zzautolayout.utils.AutoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

public class ClassAdapter extends BaseQuickAdapter<ClassInfo.DataBean,BaseViewHolder> {


    public ClassAdapter(int layoutResId, @Nullable List<ClassInfo.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassInfo.DataBean item) {
        AutoUtils.auto(helper.itemView);

        helper.setText(R.id.tv_grade, item.getName());

        if(item.getName().length()<5){
            helper.setText(R.id.tv_grade, item.getName()+"  ");
        }else {
            helper.setText(R.id.tv_grade, item.getName());
        }
        helper.setText(R.id.tv_score, item.getZongfen()+"分");


        helper.setText(R.id.tv_dianzanNum, item.getDianzan());
        helper.setText(R.id.tv_gaijinNum, item.getGaijin());

        helper.addOnClickListener(R.id.tv_commend);

    }

}
