package com.amkj.dmsh.message.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.CommunalComment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.CommentDao;
import com.amkj.dmsh.message.adapter.MessageCommunalAdapterNew;
import com.amkj.dmsh.message.bean.MessageCommentEntity;
import com.amkj.dmsh.message.bean.MessageCommentEntity.MessageCommentBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;


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
    //    ???????????????
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    //????????????
    @BindView(R.id.ll_input_comment)
    LinearLayout ll_input_comment;
    //?????????
    @BindView(R.id.emoji_edit_comment)
    EditText emoji_edit_comment;
    //????????????
    @BindView(R.id.tv_send_comment)
    TextView tv_sendComment;

    private List<MessageCommentBean> commentList = new ArrayList<>();
    public static final String TYPE = "message_comment";
    private int page = 1;
    private MessageCommunalAdapterNew messageCommunalAdapter;
    private MessageCommentEntity articleCommentEntity;
    private float locationY;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_communal;
    }

    @Override
    protected void initViews() {
        getLoginStatus(MessageCommentActivity.this);
        tv_header_titleAll.setText("??????");
        header_shared.setVisibility(View.INVISIBLE);
        messageCommunalAdapter = new MessageCommunalAdapterNew(MessageCommentActivity.this, commentList, TYPE);
        //        ????????????????????????
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.setBackgroundColor(getResources().getColor(R.color.light_gray_f));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_ten_dp).create());
        communal_recycler.setAdapter(messageCommunalAdapter);

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        messageCommunalAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getCommentData();
            }
        }, communal_recycler);
        setFloatingButton(download_btn_communal, communal_recycler);
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
                        case R.id.rel_adapter_message_communal:
                            skipActivity(messageCommentBean);
                    }
                }
            }
        });
    }

    private void skipActivity(MessageCommentBean messageCommentBean) {
        if (messageCommentBean.getStatus() == -1) {
            showToast("?????????");
        } else {
            ConstantMethod.setSkipPath(getActivity(), messageCommentBean.getAndroidLink(), false);
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
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected String getEmptyText() {
        return "??????20????????????????????????";
    }

    private void getCommentData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("showCount", 10);
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(MessageCommentActivity.this, Url.GET_MY_COMMENT_MESSAGE_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                messageCommunalAdapter.loadMoreComplete();
                if (page == 1) {
                    commentList.clear();
                }

                articleCommentEntity = GsonUtils.fromJson(result, MessageCommentEntity.class);
                if (articleCommentEntity != null) {
                    if (articleCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        commentList.addAll(articleCommentEntity.getMessageCommentList());
                    } else if (articleCommentEntity.getCode().equals(EMPTY_CODE)) {
                        messageCommunalAdapter.loadMoreEnd();
                    } else {
                        showToast(articleCommentEntity.getMsg());
                    }
                }
                messageCommunalAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, commentList, articleCommentEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                messageCommunalAdapter.loadMoreEnd();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, commentList, articleCommentEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
    }

    public void showCommentVisible(int visibility, final MessageCommentBean messageCommentBean) {
        ll_input_comment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            //????????????
            if (messageCommentBean != null) {
                emoji_edit_comment.setHint("??????" + messageCommentBean.getNickname1() + ":");
            } else {
                emoji_edit_comment.setHint("");
            }
            CommonUtils.showSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
            tv_sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //???????????????????????????
                    String comment = emoji_edit_comment.getText().toString();
                    if (!TextUtils.isEmpty(comment) && messageCommentBean != null) {
                        comment = emoji_edit_comment.getText().toString();
                        CommunalComment communalComment = new CommunalComment();
                        communalComment.setUserId(userId);
                        communalComment.setContent(comment);
                        communalComment.setCommType(messageCommentBean.getObj());
                        switch (messageCommentBean.getObj()) {
                            case "post":
                                communalComment.setCommType("doc");
                                setCommentData(communalComment, messageCommentBean);
                                break;
                            case "doc":
                            case "groupbuydoc":
                            case "goods":
                                setCommentData(communalComment, messageCommentBean);
                                break;
                        }

                        sendComment(communalComment, messageCommentBean);
                    } else {
                        showToast("?????????????????????");
                    }
                }
            });
        } else if (GONE == visibility) {
            //????????????
            CommonUtils.hideSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
        }
    }

    private void setCommentData(CommunalComment communalComment, MessageCommentBean messageCommentBean) {
        communalComment.setIsReply(1);
        communalComment.setReplyUserId(messageCommentBean.getUid());
        communalComment.setPid(messageCommentBean.getId());
        communalComment.setMainCommentId(messageCommentBean.getMainCommentId() > 0 ? messageCommentBean.getMainCommentId() : messageCommentBean.getId());
        communalComment.setObjId(messageCommentBean.getObj_id());
        communalComment.setToUid(messageCommentBean.getTo_uid());
    }

    private void sendComment(CommunalComment communalComment, final MessageCommentBean messageCommentBean) {
        loadHud.setCancellable(true);
        loadHud.show();
        tv_sendComment.setText("?????????");
        tv_sendComment.setEnabled(false);
        CommentDao.setSendComment(this, communalComment, new CommentDao.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                //????????????
                showToast(R.string.comment_send_success);
                showCommentVisible(GONE, null);
                skipActivity(messageCommentBean);
                tv_sendComment.setText("??????");
                tv_sendComment.setEnabled(true);
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_sendComment.setText("??????");
                tv_sendComment.setEnabled(true);
                emoji_edit_comment.setText("");
            }
        });
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

    /**
     * ???????????????????????????????????? ???????????????????????????????????????
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @OnTouch(R.id.communal_recycler)
    boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - locationY;//y?????????
                if (Math.abs(moveY) > 250 && ll_input_comment.getVisibility() == VISIBLE) {
                    ll_input_comment.setVisibility(GONE);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }
}
