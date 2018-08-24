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
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.message.adapter.MessageListAdapter;
import com.amkj.dmsh.message.bean.MessageTotalEntity;
import com.amkj.dmsh.message.bean.MessageTotalEntity.MessageTotalBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnChatmsgListener;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getUnReadServiceMessage;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipInitDataXNService;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_SERVICE_PAGE_URL;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;

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
    private int uid;
    private MessageListAdapter messageListAdapter;

    private List<QualityTypeBean> messageList = new ArrayList();

    private final String[] typeName = {"订单消息", "通知消息", "活动消息", "评论", "赞"};

    private final String[] typePic = {"mes_logis_icon", "mes_notify_icon", "mes_hot_icon", "mes_comment_icon", "mes_like_icon"};
    private boolean isOnPause;
    private Badge badge;
    private String avatar;

    @Override
    protected int getContentView() {
        return R.layout.activity_home_message;
    }

    @Override
    protected void initViews() {
        getLoginStatus();
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
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
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
        badge = getBadge(MessageActivity.this, fl_service, (int)(AutoUtils.getPercentWidth1px()*15),(int)(AutoUtils.getPercentWidth1px()*15));
    }

    @OnClick(R.id.tv_indent_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void loadData() {
        if (uid != 0) {
            getMessageTotal();
        }
    }

    @Override
    protected void onResume() {
        if (isOnPause) {
            isOnPause = false;
            loadData();
        }
        badge.setBadgeNumber(getUnReadServiceMessage());
        super.onResume();
    }

    @Override
    protected void onPause() {
        isOnPause = true;
        super.onPause();
    }

    // 消息统计数目
    private void getMessageTotal() {
        String url = Url.BASE_URL + Url.H_MES_STATISTICS + uid;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                MessageTotalEntity messageTotalEntity = gson.fromJson(result, MessageTotalEntity.class);
                if (messageTotalEntity != null) {
                    if (messageTotalEntity.getCode().equals("01")) {
                        setMessageTotalData(messageTotalEntity.getMessageTotalBean());
                    } else {
                        if (messageTotalEntity.getCode().equals("02")) {
                            return;
                        } else {
                            showToast(MessageActivity.this, messageTotalEntity.getMsg());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(MessageActivity.this, "网络错误，请刷新试试");
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

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
            avatar = personalInfo.getAvatar();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
        }
    }

    private void getUnReadMessage() {
        if (uid != 0) {
            //传递用户信息
            getServiceCount();
            Ntalker.getExtendInstance().message().setOnChatmsgListener(new OnChatmsgListener() {
                @Override
                public void onChatMsg(boolean isSelfMsg, String settingId, String username, String msgContent, long msgTime, boolean isUnread, int unReadCount, String uIcon) {
                    if (isUnread) {
                        getServiceCount();
                    }
                }
            });
        }
    }

    private void getServiceCount() {
        List<Map<String, Object>> list = Ntalker.getExtendInstance().conversation().getList();
        int mesCount = 0;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                boolean isUnread = (boolean) list.get(i).get("isunread");
                if (isUnread) {
                    int messageCount = (int) list.get(i).get("messagecount");
                    mesCount += messageCount;
                }
            }
        }
        badge.setBadgeNumber(mesCount);
    }

    @OnClick(R.id.iv_indent_service)
    void skipService() {
        ChatParamsBody chatParamsBody = new ChatParamsBody();
        chatParamsBody.startPageTitle = getStrings("通知消息");
        chatParamsBody.startPageUrl = DEFAULT_SERVICE_PAGE_URL;
        chatParamsBody.headurl = avatar;
        skipInitDataXNService(MessageActivity.this, chatParamsBody);
    }
}
