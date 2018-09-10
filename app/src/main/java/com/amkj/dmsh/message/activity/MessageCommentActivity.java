package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.find.activity.ArticleDetailsImgActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.message.adapter.MessageCommunalAdapterNew;
import com.amkj.dmsh.message.bean.MessageCommentEntity;
import com.amkj.dmsh.message.bean.MessageCommentEntity.MessageCommentBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_ADVISE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * Created by atd48 on 2016/7/11.
 */
public class MessageCommentActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    //回复布局
    @BindView(R.id.ll_input_comment)
    LinearLayout ll_input_comment;
    //编辑框
    @BindView(R.id.emoji_edit_comment)
    EditText emoji_edit_comment;
    //发送评论
    @BindView(R.id.tv_send_comment)
    TextView tv_sendComment;

    private List<MessageCommentBean> commentList = new ArrayList();
    public static final String TYPE = "message_comment";
    private int page = 1;
    private MessageCommunalAdapterNew messageCommunalAdapter;
    private int scrollY = 0;
    private float screenHeight;
    private MessageCommentEntity articleCommentEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_communal;
    }

    @Override
    protected void initViews() {
        getLoginStatus(MessageCommentActivity.this);
        tv_header_titleAll.setText("评论");
        header_shared.setVisibility(View.INVISIBLE);
        messageCommunalAdapter = new MessageCommunalAdapterNew(MessageCommentActivity.this, commentList, TYPE);
        //        获取头部消息列表
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(messageCommunalAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        messageCommunalAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= messageCommunalAdapter.getItemCount()) {
                    page++;
                    getCommentData();
                } else {
                    messageCommunalAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getApplication();
                    screenHeight = app.getScreenHeight() * 0.5f;
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
        messageCommunalAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MessageCommentBean messageCommentBean = (MessageCommentBean) view.getTag(R.id.iv_avatar_tag);
                if (messageCommentBean == null) {
                    messageCommentBean = (MessageCommentBean) view.getTag();
                }
                if (messageCommentBean != null) {
                    Intent intent = new Intent();
                    switch (view.getId()) {
                        case R.id.tv_mes_com_receiver:
                            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                                showCommentVisible(GONE, null);
                            } else {
                                showCommentVisible(View.VISIBLE, messageCommentBean);
                            }
                            break;
                        case R.id.iv_inv_user_avatar:
                            intent.setClass(MessageCommentActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(messageCommentBean.getUid()));
                            startActivity(intent);
                            break;
                        case R.id.tv_user_product_description:
                            skipActivity(messageCommentBean);
                    }
                }
            }
        });
    }

    private void skipActivity(MessageCommentBean messageCommentBean) {
        Intent intent = new Intent();
        if (messageCommentBean.getStatus() == 1) {
            switch (messageCommentBean.getObj()) {
                case "doc":
                    intent.setClass(MessageCommentActivity.this, ArticleOfficialActivity.class);
                    intent.putExtra("ArtId", String.valueOf(messageCommentBean.getObj_id()));
                    startActivity(intent);
                    break;
                case "post":
                    intent.setClass(MessageCommentActivity.this, ArticleDetailsImgActivity.class);
                    intent.putExtra("ArtId", String.valueOf(messageCommentBean.getObj_id()));
                    startActivity(intent);
                    break;
                case "goods":
                    intent.setClass(MessageCommentActivity.this, ShopTimeScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(messageCommentBean.getObj_id()));
                    startActivity(intent);
                    break;
            }
        } else {
            showToast(MessageCommentActivity.this, "已删除");
        }
    }

    @OnClick(R.id.tv_life_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("replyComment")) {
            MessageCommentBean articleCommentBean = (MessageCommentBean) message.result;
            if (articleCommentBean != null) {
                if (View.VISIBLE == ll_input_comment.getVisibility()) {
                    showCommentVisible(GONE, null);
                } else {
                    showCommentVisible(View.VISIBLE, articleCommentBean);
                }
            }
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getCommentData();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCommentData() {
        if (userId != 0) {
            String url = Url.BASE_URL + Url.H_MES_COMMENT;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            params.put("currentPage", page);
            params.put("version", 2);
            NetLoadUtils.getQyInstance().loadNetDataPost(MessageCommentActivity.this, url
                    , params, new NetLoadUtils.NetLoadListener() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    messageCommunalAdapter.loadMoreComplete();
                    if (page == 1) {
                        commentList.clear();
                    }
                    Gson gson = new Gson();
                    articleCommentEntity = gson.fromJson(result, MessageCommentEntity.class);
                    if (articleCommentEntity != null) {
                        if (articleCommentEntity.getCode().equals(SUCCESS_CODE)) {
                            commentList.addAll(articleCommentEntity.getMessageCommentList());
                        } else if (!articleCommentEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(MessageCommentActivity.this, articleCommentEntity.getMsg());
                        }
                    }
                    messageCommunalAdapter.notifyDataSetChanged();
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,commentList,articleCommentEntity);
                }

                @Override
                public void netClose() {
                    smart_communal_refresh.finishRefresh();
                    messageCommunalAdapter.loadMoreComplete();
                    showToast(MessageCommentActivity.this, R.string.unConnectedNetwork);
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,commentList,articleCommentEntity);
                }

                @Override
                public void onError(Throwable throwable) {
                    smart_communal_refresh.finishRefresh();
                    messageCommunalAdapter.loadMoreComplete();
                    showToast(MessageCommentActivity.this, R.string.invalidData);
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,commentList,articleCommentEntity);
                }
            });
        } else {
            NetLoadUtils.getQyInstance().showLoadSir(loadService, commentList, articleCommentEntity);
        }
    }

    public void showCommentVisible(int visibility, final MessageCommentBean messageCommentBean) {
        ll_input_comment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            //弹出键盘
            if (messageCommentBean != null) {
                emoji_edit_comment.setHint("回复" + messageCommentBean.getNickname1() + ":");
            } else {
                emoji_edit_comment.setHint("");
            }
            CommonUtils.showSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
            tv_sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断有内容调用接口
                    String comment = emoji_edit_comment.getText().toString();
                    if (!TextUtils.isEmpty(comment) && messageCommentBean != null) {
                        comment = emoji_edit_comment.getText().toString();
                        CommunalComment communalComment = new CommunalComment();
                        switch (messageCommentBean.getObj()) {
                            case "doc":
                            case "post":
                                communalComment.setCommType("doc");
                                setCommentData(communalComment, messageCommentBean);
                                break;
                            case "goods":
                                communalComment.setCommType("goods");
                                setCommentData(communalComment, messageCommentBean);
                                break;
                            case MES_ADVISE:
                                communalComment.setCommType(MES_ADVISE);
                                break;
                            case MES_FEEDBACK:
                                communalComment.setCommType(MES_FEEDBACK);
                                break;
                        }
                        communalComment.setUserId(userId);
                        communalComment.setContent(comment);
                        sendComment(communalComment, messageCommentBean);
                    } else {
                        showToast(MessageCommentActivity.this, "请正确输入内容");
                    }
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
        }
    }

    private void setCommentData(CommunalComment communalComment, MessageCommentBean messageCommentBean) {
        communalComment.setIsReply(1);
        communalComment.setReplyUserId(messageCommentBean.getUid());
        communalComment.setPid(messageCommentBean.getId());
        communalComment.setMainCommentId(messageCommentBean.getMainCommentId());
        communalComment.setObjId(messageCommentBean.getObj_id());
        communalComment.setToUid(messageCommentBean.getTo_uid());
    }

    private void sendComment(CommunalComment communalComment, final MessageCommentBean messageCommentBean) {
        loadHud.setCancellable(true);
        loadHud.show();
        tv_sendComment.setText("发送中");
        tv_sendComment.setEnabled(false);
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnSendCommentFinish(new ConstantMethod.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                //请求成功
                showToast(MessageCommentActivity.this, R.string.comment_send_success);
                showCommentVisible(GONE, null);
                skipActivity(messageCommentBean);
                tv_sendComment.setText("发送");
                tv_sendComment.setEnabled(true);
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_sendComment.setText("发送");
                tv_sendComment.setEnabled(true);
                emoji_edit_comment.setText("");
            }
        });
        constantMethod.setSendComment(MessageCommentActivity.this, communalComment);
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
}
