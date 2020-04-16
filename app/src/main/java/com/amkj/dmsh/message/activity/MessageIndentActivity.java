package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.message.adapter.MessageIndentAdapter;
import com.amkj.dmsh.message.bean.MessageIndentEntity;
import com.amkj.dmsh.message.bean.MessageIndentEntity.MessageIndentBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_MES_INDENT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:订单消息
 */
public class MessageIndentActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private MessageIndentAdapter messageIndentAdapter;
    //官方通知数据
    private List<MessageIndentBean> messageArticleList = new ArrayList<>();
    private MessageIndentEntity messageOfficialEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_shared.setVisibility(GONE);
        tv_header_titleAll.setText("交易物流消息");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageIndentActivity.this);
        communal_recycler.setLayoutManager(linearLayoutManager);
        messageIndentAdapter = new MessageIndentAdapter(MessageIndentActivity.this, messageArticleList);
        communal_recycler.setAdapter(messageIndentAdapter);
        messageIndentAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageIndentBean messageIndentBean = (MessageIndentBean) view.getTag();
            if (messageIndentBean != null && !TextUtils.isEmpty(messageIndentBean.getObj())) {
                setSkipPath(getActivity(), messageIndentBean.getAndroidLink(), false);
            }
        });
        messageIndentAdapter.setOnLoadMoreListener(() -> {
            page++;
            getData();
        }, communal_recycler);

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        setFloatingButton(download_btn_communal, communal_recycler);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_MES_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                messageIndentAdapter.loadMoreComplete();
                if (page == 1) {
                    messageArticleList.clear();
                }
                Gson gson = new Gson();
                messageOfficialEntity = gson.fromJson(result, MessageIndentEntity.class);
                if (messageOfficialEntity != null) {
                    if (messageOfficialEntity.getCode().equals(SUCCESS_CODE)) {
                        List<MessageIndentBean> messageIndentList = messageOfficialEntity.getMessageIndentList();
                        if (messageIndentList != null) {
                            messageArticleList.addAll(messageIndentList);
                        }
                    } else if (messageOfficialEntity.getCode().equals(EMPTY_CODE)) {
                        messageIndentAdapter.loadMoreEnd();
                    } else {
                        showToast(MessageIndentActivity.this, messageOfficialEntity.getMsg());
                    }
                }
                messageIndentAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, messageArticleList, messageOfficialEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                messageIndentAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, messageArticleList, messageOfficialEntity);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
