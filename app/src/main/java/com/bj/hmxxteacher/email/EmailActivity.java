package com.bj.hmxxteacher.email;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bj.hmxxteacher.BaseActivity;
import com.bj.hmxxteacher.R;
import com.bj.hmxxteacher.email.adapter.LetterAdapter;
import com.bj.hmxxteacher.email.model.Letter;
import com.bj.hmxxteacher.email.presenter.EmailPresenter;
import com.bj.hmxxteacher.email.view.IViewEmail;
import com.bj.hmxxteacher.entity.BaseDataInfo;
import com.bj.hmxxteacher.utils.StringUtils;
import com.bj.hmxxteacher.utils.T;
import com.bj.hmxxteacher.widget.LoadingDialog;
import com.bj.hmxxteacher.widget.SpacesItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

public class EmailActivity extends BaseActivity implements IViewEmail {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.header_img_back)
    ImageView headerImgBack;
    @BindView(R.id.header_ll_left)
    LinearLayout headerLlLeft;
    @BindView(R.id.header_tv_title)
    TextView headerTvTitle;
    @BindView(R.id.header_tv_left)
    TextView headerTvLeft;
    @BindView(R.id.header_tv_right)
    TextView headerTvRight;
    @BindView(R.id.layout_bottom)
    RelativeLayout layoutBottom;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.mSmartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    private Unbinder unbinder;

    private LetterAdapter adapter;

    private List<Letter.DataBean.ListDataBean> dataList = new ArrayList<>();


    private boolean isLongClick = false;
    private boolean isSelectedAll = false;
    private EmailPresenter presenter;
    private TabLayout.Tab tab1, tab2;

    private List<String> selectedId = new ArrayList<>();
    private int currentTab = 1;
    private int currentPage = 0;

    //private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        unbinder = ButterKnife.bind(this);

        //loadingDialog = new LoadingDialog(EmailActivity.this);


        presenter = new EmailPresenter(this, this);

        presenter.getEmailList(currentPage, "weidu");


        tab1 = tabLayout.getTabAt(0);
        tab2 = tabLayout.getTabAt(1);

        initToolBar();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPage = 0;
                if (isLongClick) {
                   complete();
                }
                if (tab.getPosition() == 1) {
                    currentTab = 4;
                    presenter.getEmailList(currentPage, "xingbiao");
                    radioGroup.setVisibility(View.GONE);
                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radioButton1:
                            currentTab = 1;
                            presenter.getEmailList(currentPage, "weidu");
                            break;
                        case R.id.radioButton2:
                            currentTab = 2;
                            presenter.getEmailList(currentPage, "yidu");
                            break;
                        case R.id.radioButton3:
                            currentTab = 3;
                            presenter.getEmailList(currentPage, "huifu");
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                currentPage = 0;
                switch (checkedId) {
                    case R.id.radioButton1:
                        currentTab = 1;
                        presenter.getEmailList(currentPage, "weidu");
                        break;
                    case R.id.radioButton2:
                        currentTab = 2;
                        presenter.getEmailList(currentPage, "yidu");
                        break;
                    case R.id.radioButton3:
                        currentTab = 3;
                        presenter.getEmailList(currentPage, "huifu");
                        break;
                    default:
                        break;
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(2));
        adapter = new LetterAdapter(R.layout.recycler_item_letter, dataList);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter2, View view, int position) {
                if (isLongClick) {
                    if (dataList.get(position).isSelected()) {
                        dataList.get(position).setSelected(false);
                    } else {
                        dataList.get(position).setSelected(true);
                    }
                    adapter.notifyDataSetChanged();
                    return;
                } else {

                }
                Intent intent = new Intent(EmailActivity.this, EmailDetailActivity.class);
                intent.putExtra("xinjianid", dataList.get(position).getXinjianid());
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter2, View view, int position) {
                layoutBottom.setVisibility(View.VISIBLE);
                headerTvLeft.setVisibility(View.VISIBLE);
                headerTvRight.setVisibility(View.VISIBLE);
                headerImgBack.setVisibility(View.GONE);
                headerTvTitle.setVisibility(View.GONE);
                isLongClick = true;
                adapter.longclick = true;
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 0;
                refresh(currentPage);

            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                currentPage++;
                refresh(currentPage);
            }
        });

    }

    @Override
    protected void initToolBar() {
        headerImgBack.setVisibility(View.VISIBLE);
        headerTvTitle.setVisibility(View.VISIBLE);
        headerTvTitle.setText("校长信箱");
        headerTvLeft.setText("全选");
        headerTvRight.setText("完成");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.onDestory();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isLongClick) {
                complete();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.header_img_back, R.id.header_tv_left, R.id.header_tv_right, R.id.bt_biaoji, R.id.bt_quxiao, R.id.bt_shanchu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_img_back:
                finish();
                break;
            case R.id.header_tv_left:
                if (isSelectedAll) {
                    headerTvLeft.setText("全选");
                    isSelectedAll = false;
                    for (int i = 0; i < dataList.size(); i++) {
                        dataList.get(i).setSelected(false);
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    headerTvLeft.setText("取消全选");
                    isSelectedAll = true;
                    for (int i = 0; i < dataList.size(); i++) {
                        dataList.get(i).setSelected(true);
                    }
                    adapter.notifyDataSetChanged();
                }

                break;
            case R.id.header_tv_right:
                complete();

                break;
            case R.id.bt_biaoji:
                starOrDelete(1);
                break;
            case R.id.bt_quxiao:
                starOrDelete(2);
                break;
            case R.id.bt_shanchu:
                starOrDelete(3);
                break;
        }
    }

    private void complete(){
        isLongClick = false;
        layoutBottom.setVisibility(View.GONE);
        headerTvLeft.setVisibility(View.GONE);
        headerTvRight.setVisibility(View.GONE);
        headerImgBack.setVisibility(View.VISIBLE);
        headerTvTitle.setVisibility(View.VISIBLE);
        isSelectedAll = true;
        adapter.longclick = false;
        adapter.notifyDataSetChanged();
    }

    private void refresh(int page) {
        switch (currentTab) {
            case 1:
                presenter.getEmailList(page, "weidu");
                break;
            case 2:
                presenter.getEmailList(page, "yidu");
                break;
            case 3:
                presenter.getEmailList(page, "huifu");
                break;
            case 4:
                presenter.getEmailList(page, "xingbiao");
                break;
        }
    }

    private void starOrDelete(int type) {
        selectedId.clear();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).isSelected()) {
                selectedId.add(dataList.get(i).getXinjianid());
            }
        }
        Log.e("选中标记", JSON.toJSONString(selectedId));

        if(selectedId.size()!=0) {

            String multiContent = StringUtils.trimFirstAndLastChar(JSON.toJSONString(selectedId));
            String newIds = multiContent.replace("\"", "");
            Log.e("选中新标记", newIds);
            if (type == 1) {
                presenter.xingbiao(newIds, "star");
            }
            if (type == 2) {
                presenter.xingbiao(newIds, "quxiao");
            }
            if (type == 3) {
                presenter.delete(newIds);
            }
        }

    }

    @Override
    public void getEmailList(String result) {

        //loadingDialog.dismiss();

        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadmore();
        Letter letter = JSON.parseObject(result, new TypeReference<Letter>() {
        });

        if (letter.getRet().equals("3")) {//未读查询成功
            tab1.setText("收件箱(" + letter.getData().getXinjian_num() + ")");
            tab2.setText("星标(" + letter.getData().getStar_num() + ")");

            if (currentPage == 0) {
                dataList.clear();
            }
            dataList.addAll(letter.getData().getList_data());
            adapter.notifyDataSetChanged();
        }

        if (letter.getRet().equals("5")) {//已读查询成功
            tab1.setText("收件箱(" + letter.getData().getXinjian_num() + ")");
            tab2.setText("星标(" + letter.getData().getStar_num() + ")");

            if (currentPage == 0) {
                dataList.clear();
            }
            dataList.addAll(letter.getData().getList_data());
            adapter.notifyDataSetChanged();
        }
        if (letter.getRet().equals("7")) {//回复查询成功
            tab1.setText("收件箱(" + letter.getData().getXinjian_num() + ")");
            tab2.setText("星标(" + letter.getData().getStar_num() + ")");

            if (currentPage == 0) {
                dataList.clear();
            }
            dataList.addAll(letter.getData().getList_data());
            adapter.notifyDataSetChanged();
        }
        if (letter.getRet().equals("9")) {//星标查询成功
            tab1.setText("收件箱(" + letter.getData().getXinjian_num() + ")");
            tab2.setText("星标(" + letter.getData().getStar_num() + ")");

            if (currentPage == 0) {
                dataList.clear();
            }
            dataList.addAll(letter.getData().getList_data());
            adapter.notifyDataSetChanged();
        }
        if (letter.getData() == null) {
            if (currentPage == 0) {
                dataList.clear();
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void xingbiao(String result) {
        currentPage = 0;
        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });



        if(dataInfo.getRet().equals("1")){
            complete();
            refresh(currentPage);
        }
        if(dataInfo.getRet().equals("2")){
            complete();
            refresh(currentPage);
        }
    }

    @Override
    public void delete(String result) {
        currentPage = 0;
        BaseDataInfo dataInfo = JSON.parseObject(result, new TypeReference<BaseDataInfo>() {
        });
        if(dataInfo.getRet().equals("1")){
            complete();
            refresh(currentPage);
        }
    }


}
