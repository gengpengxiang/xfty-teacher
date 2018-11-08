package com.bj.hmxxteacher.reason.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.activity.ReasonEditActivity;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.entity.MessageEvent;
import com.bj.hmxxteacher.reason.adapter.ReasonAdapter;
import com.bj.hmxxteacher.reason.model.Reason;
import com.bj.hmxxteacher.reason.presenter.ReasonPresenter;
import com.bj.hmxxteacher.reason.view.IViewReason;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.widget.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReasonFragment2 extends Fragment implements IViewReason {


    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private ReasonAdapter adapter;
    private List<Reason.DataBean> list = new ArrayList<>();
    private ReasonPresenter presenter;
    private String teacherphone;
    private String id;
    private String reasonType;
    private String reasonTypeNum = "0";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reason, container, false);
        unbinder = ButterKnife.bind(this, view);

        teacherphone = PreferencesUtils.getString(getActivity(), MLProperties.PREFER_KEY_USER_ID, "");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(1));
        adapter = new ReasonAdapter(R.layout.recycler_item_reasons2,list);

        mRecyclerView.setAdapter(adapter);

        id = getActivity().getIntent().getStringExtra("id");
        reasonType = getActivity().getIntent().getStringExtra("reasontype");
        presenter = new ReasonPresenter(getActivity(),this);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(list.get(position).getCode().equals("-1")){
                    Intent intent = new Intent(getActivity(), ReasonEditActivity.class);
                    intent.putExtra("type", "2");
                    intent.putExtra("banji_type",reasonTypeNum);
                    startActivity(intent);
                }else {
                    if (reasonType.equals("class")) {
                        presenter.commentClass(teacherphone, list.get(position).getCode(), id, "2");
                    }
                    if (reasonType.equals("student")) {
                        presenter.commentStudent(teacherphone, list.get(position).getCode(), id, "2");
                    }
                }
            }
        });

       /* adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(list.get(position).getCode().equals("-1")){
                    Intent intent = new Intent(getActivity(), ReasonEditActivity.class);
                    intent.putExtra("type", "2");
                    intent.putExtra("banji_type",reasonTypeNum);
                    startActivity(intent);
                }else {
                    if (reasonType.equals("class")) {
                        presenter.commentClass(teacherphone, list.get(position).getCode(), id, "2");
                    }
                    if (reasonType.equals("student")) {
                        presenter.commentStudent(teacherphone, list.get(position).getCode(), id, "2");
                    }
                }

            }
        });
*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (reasonType.equals("class")) {
            reasonTypeNum = "0";
            presenter.getReasonList(teacherphone, "2", "0");

        }
        if (reasonType.equals("student")) {
            reasonTypeNum = "1";
            presenter.getReasonList(teacherphone, "2", "1");

        }

//        presenter.getReasonList(teacherphone,"2",reasonType);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void getReasonInfo(String result) {
        Log.e("Fragment2", "222");
        Reason reason = JSON.parseObject(result, new TypeReference<Reason>() {
        });
        if(reason.getRet().equals("1")){

            list.clear();
            list.addAll(reason.getData());

//            Reason.DataBean newReason = new Reason.DataBean("-1","编辑理由","");
//            list.add(newReason);
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    public void comment(String result) {
        if(result.equals("1")){
            EventBus.getDefault().post(new MessageEvent("commentsuccess"));
            T.showShort(getActivity(),"评价成功");
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestory();
    }
}
