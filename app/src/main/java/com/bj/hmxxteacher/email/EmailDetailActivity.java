package com.bj.hmxxteacher.email;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.api.MLProperties;
import com.bj.hmxxteacher.email.adapter.ReplyAdapter;
import com.bj.hmxxteacher.email.model.Reply;
import com.bj.hmxxteacher.email.presenter.ReplyPresenter;
import com.bj.hmxxteacher.email.view.IViewReply;
import com.bj.hmxxteacher.entity.BaseDataInfo;
import com.bj.hmxxteacher.utils.Base64Util;
import com.bj.hmxxteacher.utils.KeyBoardUtils;
import com.bj.hmxxteacher.utils.PreferencesUtils;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.widget.SpacesItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EmailDetailActivity extends BaseActivity implements IViewReply {

    @BindView(R.id.header_img_back)
    ImageView headerImgBack;
    @BindView(R.id.header_tv_left2)
    TextView headerTvLeft;
    @BindView(R.id.layout_sanjiao)
    LinearLayout layoutSanjiao;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.edt_content)
    EditText edtContent;

    private Unbinder unbinder;
    private ReplyPresenter presenter;
    private ReplyAdapter adapter;

    private List<Reply.DataBean.HuifuListBean> dataList = new ArrayList<>();
    private String xinjianid;
    private String userPhoneNumber;

    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail);
        unbinder = ButterKnife.bind(this);
        userPhoneNumber = PreferencesUtils.getString(this, MLProperties.PREFER_KEY_USER_ID, "");

        xinjianid = getIntent().getStringExtra("xinjianid");
        Log.e("xinjianid=",xinjianid);

        presenter = new ReplyPresenter(this, this);
        presenter.getReply(xinjianid, currentPage);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReplyAdapter(R.layout.recycler_item_letter_reply,dataList);
        mRecyclerView.setAdapter(adapter);

        initTitleBar();
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                presenter.getReply(xinjianid, currentPage);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                presenter.getReply(xinjianid, currentPage);
            }
        });
    }

    private void initTitleBar() {
        headerImgBack.setVisibility(View.VISIBLE);
        headerTvLeft.setVisibility(View.VISIBLE);
        headerTvLeft.setText("校长信箱");
        //layoutSanjiao.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.header_img_back, R.id.bt_up, R.id.bt_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_img_back:
                finish();
                break;
            case R.id.bt_up:
                T.showShort(EmailDetailActivity.this, "上一个");
                break;
            case R.id.bt_down:
                T.showShort(EmailDetailActivity.this, "下一个");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDestory();
    }

    @Override
    public void getEmailReply(String result) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadmore();

        Reply reply = JSON.parseObject(result, new TypeReference<Reply>() {
        });

        if (reply.getRet().equals("1")) {
            tvTitle.setText(Base64Util.decode(reply.getData().getTitle()));
            tvDate.setText(reply.getData().getDate());
            tvContent.setText(Base64Util.decode(reply.getData().getContent()));
            if (currentPage == 0) {
                dataList.clear();
            }
            if (reply.getData().getHuifu_list() != null) {
                dataList.clear();
                dataList.addAll(reply.getData().getHuifu_list());
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void sendReply(String result) {
        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });

        if (dataInfo.getRet().equals("1")) {
            currentPage = 0;
            presenter.getReply(xinjianid, currentPage);
            T.showLong(EmailDetailActivity.this, "回复成功");
        }
    }

    @OnClick(R.id.tv_send)
    public void onClick() {

        if(StringUtils.isEmpty(edtContent.getText().toString())){
            T.showLong(EmailDetailActivity.this,"回复内容不能为空");
        }else {
            String content = Base64Util.encode(edtContent.getText().toString());
            presenter.sendReply(userPhoneNumber,content,xinjianid);

            KeyBoardUtils.closeKeybord(this.getCurrentFocus().getWindowToken(), this);
            edtContent.setText("");
        }
    }
}
