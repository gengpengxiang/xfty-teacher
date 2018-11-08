package com.bj.hmxxteacher.email.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.email.model.Letter;
import com.bj.hmxxteacher.utils.Base64Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class LetterAdapter extends BaseQuickAdapter<Letter.DataBean.ListDataBean,BaseViewHolder> {

    public Boolean longclick = false;

    public LetterAdapter(int layoutResId, @Nullable List<Letter.DataBean.ListDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Letter.DataBean.ListDataBean item) {
        helper.setText(R.id.tv_name, item.getJiazhang());
        helper.setText(R.id.tv_time, item.getDate());
        helper.setText(R.id.tv_grade, item.getClass_name());
        helper.setText(R.id.tv_title, Base64Util.decode(item.getTitle()));

        RelativeLayout layout = helper.getView(R.id.layout_select);
        ImageView iv = helper.getView(R.id.iv_select);

        ImageView ivStar = helper.getView(R.id.iv_star);
        if(item.getStar_status().equals("0")){
            ivStar.setVisibility(View.GONE);
        }else {
            ivStar.setVisibility(View.VISIBLE);
        }


        if(longclick) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);//否则让其消失
        }

        if(item.isSelected()){
            iv.setImageResource(R.mipmap.ic_select_green);
        }else {
            iv.setImageResource(R.mipmap.ic_select_gray);
        }

    }

}
