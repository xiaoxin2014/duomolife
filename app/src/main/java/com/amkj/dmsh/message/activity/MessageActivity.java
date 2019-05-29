package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.message.adapter.MessageListAdapter;
import com.amkj.dmsh.message.bean.MessageTotalEntity;
import com.amkj.dmsh.message.bean.MessageTotalEntity.MessageTotalBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_MES_STATISTICS;

;

/**
 * 消息记录
 */
public class MessageActivity extends BaseActivity {
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.fl_service)
    FrameLayout fl_service;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private MessageListAdapter messageListAdapter;

    private List<QualityTypeBean> messageList = new ArrayList();

    private final String[] typeName = {"订单消息", "通知消息", "活动消息", "评论", "赞"};

    private final String[] typePic = {"mes_logis_icon", "mes_notify_icon", "mes_hot_icon", "mes_comment_icon", "mes_like_icon"};
    private boolean isOnPause;
    private MessageTotalEntity messageTotalEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_home_message;
    }

    @Override
    protected void initViews() {
        getLoginStatus(MessageActivity.this);
        iv_indent_search.setVisibility(View.GONE);
        tv_indent_title.setText("消息");
        QualityTypeBean qualityTypeBean;
        for (int i = 0; i < typeName.length; i++) {
            qualityTypeBean = new QualityTypeBean();
            qualityTypeBean.setName(typeName[i]);
            qualityTypeBean.setPicUrl(typePic[i]);
            qualityTypeBean.setId(i);
            messageList.add(qualityTypeBean);
        }
        //获取头部消息列表
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        messageListAdapter = new MessageListAdapter(MessageActivity.this, messageList);
        communal_recycler.setAdapter(messageListAdapter);
        messageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
                if (qualityTypeBean != null) {
                    Intent intent = new Intent();
                    switch (qualityTypeBean.getId()) {
                        case 0:
                            intent.setClass(MessageActivity.this, MessageIndentActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent.setClass(MessageActivity.this, MessageSysMesActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            intent.setClass(MessageActivity.this, MessageHotActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            intent.setClass(MessageActivity.this, MessageCommentActivity.class);
                            startActivity(intent);
                            break;
                        case 4:
                            intent.setClass(MessageActivity.this, MessageLikedActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_indent_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void loadData() {
        getMessageTotal();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return communal_recycler;
    }

    @Override
    protected void onResume() {
        if (isOnPause) {
            isOnPause = false;
            loadData();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        isOnPause = true;
        super.onPause();
    }

    // 消息统计数目
    private void getMessageTotal() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_MES_STATISTICS
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();
                        messageTotalEntity = gson.fromJson(result, MessageTotalEntity.class);
                        if (messageTotalEntity != null) {
                            if (messageTotalEntity.getCode().equals(SUCCESS_CODE)) {
                                setMessageTotalData(messageTotalEntity.getMessageTotalBean());
                            } else if (!messageTotalEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(MessageActivity.this, messageTotalEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void netClose() {
                        showToast(MessageActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(MessageActivity.this, R.string.invalidData);
                    }
                });
    }

    private void setMessageTotalData(MessageTotalBean messageTotalBean) {
        for (int i = 0; i < messageList.size(); i++) {
            QualityTypeBean qualityTypeBean = messageList.get(i);
            switch (i) {
                case 0:
                    qualityTypeBean.setType(messageTotalBean.getOrderTotal());
                    messageList.set(i, qualityTypeBean);
                    break;
                case 1:
                    qualityTypeBean.setType(messageTotalBean.getSmTotal());
                    messageList.set(i, qualityTypeBean);
                    break;
                case 2:
                    qualityTypeBean.setType(messageTotalBean.getCommOffifialTotal());
                    messageList.set(i, qualityTypeBean);
                    break;
                case 3:
                    qualityTypeBean.setType(messageTotalBean.getCommentTotal());
                    messageList.set(i, qualityTypeBean);
                    break;
                case 4:
                    qualityTypeBean.setType(messageTotalBean.getLikeTotal());
                    messageList.set(i, qualityTypeBean);
                    break;
            }
        }
        messageListAdapter.setNewData(messageList);
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

    @OnClick(R.id.iv_indent_service)
    void skipService() {
        QyServiceUtils.getQyInstance()
                .openQyServiceChat(MessageActivity.this
                        , "通知消息", "");
    }
}
