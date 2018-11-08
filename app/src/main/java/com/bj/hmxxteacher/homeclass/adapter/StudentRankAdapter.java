package com.bj.hmxxteacher.homeclass.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.homeclass.model.StudentRank;
import com.bj.hmxxteacher.zzimgselector.utils.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

public class StudentRankAdapter extends BaseQuickAdapter<StudentRank,BaseViewHolder> {


    public StudentRankAdapter(int layoutResId, @Nullable List<StudentRank> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentRank item) {

        ViewGroup.LayoutParams layoutParams = helper.getView(R.id.layout_item_dengji).getLayoutParams();
        layoutParams.width = (ScreenUtils.getScreenWidth(mContext)) / 5;//
        helper.getView(R.id.layout_item_dengji).setLayoutParams(layoutParams);


        helper.setText(R.id.tv_dengjiName, item.getName());
        helper.setText(R.id.tv_dengjiNum, item.getNumber());
    }

}
