package com.bj.hmxxteacher.adapter;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.entity.ClassItemInfo;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.zzautolayout.utils.AutoUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by zz379 on 2017/1/6.
 * 全部学生页面adapter
 */

public class AllStudentAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> {

    private List<ClassItemInfo> mDataList;

    public AllStudentAdapter(List<ClassItemInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_student_info, parent, false);
        vh = new ViewHolderStudent(v, true);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder fholder, int position, boolean isItem) {
        ClassItemInfo itemInfo = mDataList.get(position);

        ViewHolderStudent holder = (ViewHolderStudent) fholder;
        holder.tvStudRankNum.setText(String.valueOf(position + 1));

        if (!StringUtils.isEmpty(itemInfo.getStudImg())) {
            holder.imgStudPhoto.setImageURI(Uri.parse(itemInfo.getStudImg()));
        }

        holder.tvStudName.setText(itemInfo.getStudName());
        holder.tvStudScore.setText(itemInfo.getStudScore());
        holder.tvStudBadge.setText(itemInfo.getStudBadge());
        holder.tvStudGrade.setText(itemInfo.getStudGrade());
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new ViewHolderStudent(view, false);
    }

    public class ViewHolderStudent extends RecyclerView.ViewHolder {

        private RelativeLayout itemContainer;
        private TextView tvStudRankNum;
        private SimpleDraweeView imgStudPhoto;
        private TextView tvStudName;
        private TextView tvStudScore, tvStudBadge, tvStudGrade;
        private TextView tvCommendStudent;

        public ViewHolderStudent(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                AutoUtils.auto(itemView);
                itemContainer = (RelativeLayout) itemView.findViewById(R.id.itemContainer);
                tvStudRankNum = (TextView) itemView.findViewById(R.id.tv_studentRankNum);
                imgStudPhoto = (SimpleDraweeView) itemView.findViewById(R.id.img_kidPhoto);
                tvStudName = (TextView) itemView.findViewById(R.id.tv_studentName);
                tvStudScore = (TextView) itemView.findViewById(R.id.tv_studentScore);
                tvStudBadge = (TextView) itemView.findViewById(R.id.tv_studentBadge);
                tvStudGrade = (TextView) itemView.findViewById(R.id.tv_studentGrade);

                tvCommendStudent = (TextView) itemView.findViewById(R.id.tv_commend_student);
                tvCommendStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myItemClickListener != null) {
                            myItemClickListener.onCommendClick(v, getAdapterPosition());
                        }
                    }
                });

                itemContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myItemClickListener != null) {
                            myItemClickListener.onClick(v, getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

    public void setData(List<ClassItemInfo> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }


    public void insert(ClassItemInfo person, int position) {
        insert(mDataList, person, position);
    }

    public void remove(int position) {
        remove(mDataList, position);
    }

    public void clear() {
        clear(mDataList);
    }

    public ClassItemInfo getItem(int position) {
        if (position < mDataList.size())
            return mDataList.get(position);
        else
            return null;
    }

    private OnMyItemClickListener myItemClickListener;

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public interface OnMyItemClickListener {
        void onClick(View view, int position);

        void onCommendClick(View view, int position);
    }
}
