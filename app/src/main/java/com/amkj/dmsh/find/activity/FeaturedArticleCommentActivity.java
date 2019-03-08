package com.amkj.dmsh.find.activity;

import android.content.Context;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.adapter.FeaturedArticleCommentAdapter;
import com.amkj.dmsh.find.bean.FeaturedArticleCommentEntity.FeaturedArticleCommentBean;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.OffsetLinearLayoutManager;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/7
 * version 3.3.0
 * class description:精选文章评论
 */
public class FeaturedArticleCommentActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView tvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar tlNormalBar;
    @BindView(R.id.communal_recycler)
    RecyclerView communalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smartCommunalRefresh;
    @BindView(R.id.emoji_edit_comment)
    EmojiEditText emojiEditComment;
    @BindView(R.id.tv_send_comment)
    TextView tvSendComment;
    @BindView(R.id.ll_input_comment)
    LinearLayout llInputComment;
    @BindView(R.id.tv_publish_comment)
    TextView tvPublishComment;
    @BindView(R.id.tv_article_bottom_like)
    TextView tvArticleBottomLike;
    @BindView(R.id.tv_article_bottom_collect)
    TextView tvArticleBottomCollect;
    @BindView(R.id.ll_article_comment)
    LinearLayout llArticleComment;
    @BindView(R.id.fl_feature_article_comment)
    FrameLayout flFeatureArticleComment;
    private float locationY;
    private int screenHeight;
    private List<FeaturedArticleCommentBean> featuredArticleCommentList = new ArrayList<>();
    private FeaturedArticleCommentAdapter featuredArticleCommentAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_featured_article_comment;
    }

    @Override
    protected void initViews() {
        tvHeaderShared.setVisibility(View.GONE);
        tlNormalBar.setSelected(true);
        tvArticleBottomCollect.setVisibility(View.GONE);
        flFeatureArticleComment.setVisibility(View.GONE);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        featuredArticleCommentAdapter = new FeaturedArticleCommentAdapter(FeaturedArticleCommentActivity.this, featuredArticleCommentList);
        communalRecycler.setLayoutManager(new OffsetLinearLayoutManager(this));
        communalRecycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        communalRecycler.setAdapter(featuredArticleCommentAdapter);
        featuredArticleCommentAdapter.setEmptyView(R.layout.layout_featured_comment_empty,communalRecycler);
        smartCommunalRefresh.setOnRefreshListener(refreshLayout ->loadData());
        featuredArticleCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FeaturedArticleCommentBean featuredArticleCommentBean = (FeaturedArticleCommentBean) view.getTag();
                if(featuredArticleCommentBean==null){
                    return;
                }
                switch (view.getId()){
                    case R.id.tv_featured_comment_liked:
                        if (userId > 0) {
                            int likeNum = featuredArticleCommentBean.getLikeCount();
                            TextView textView = (TextView) view;
                            textView.setSelected(!textView.isSelected());
                            featuredArticleCommentBean.setLikeCount(textView.isSelected()
                                    ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : 0);
                            textView.setText(String.valueOf(featuredArticleCommentBean.getLikeCount()));
                        } else {
                            getLoginStatus(FeaturedArticleCommentActivity.this);
                        }
                        break;
                }
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
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

//    @Override
//    protected View getLoadView() {
//        return smartCommunalRefresh;
//    }
//
//    @Override
//    protected boolean isAddLoad() {
//        return true;
//    }
    @Override
    protected void loadData() {
        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        if(flFeatureArticleComment.getVisibility() == GONE){
            flFeatureArticleComment.setVisibility(View.VISIBLE);
        }
        smartCommunalRefresh.finishRefresh();
    }

    //发送评论
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        tvSendComment.setText("发送中…");
        tvSendComment.setEnabled(false);
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnSendCommentFinish(new ConstantMethod.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                tvSendComment.setText("发送");
                tvSendComment.setEnabled(true);
                showToast(FeaturedArticleCommentActivity.this, R.string.comment_send_success);
                commentViewVisible(GONE, null);
//                page = 1;
//                getArticleImgComment();
                emojiEditComment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tvSendComment.setText("发送");
                tvSendComment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(FeaturedArticleCommentActivity.this, communalComment);
    }

    private void setPublishComment() {
        if (View.VISIBLE == llInputComment.getVisibility()) {
            commentViewVisible(GONE, null);
        } else {
//            clickScrollToComment();
            commentViewVisible(View.VISIBLE, null);
        }
    }

    private void commentViewVisible(int visibility,Object object) {
        llInputComment.setVisibility(visibility);
        llArticleComment.setVisibility(visibility == View.VISIBLE ? GONE : View.VISIBLE);
        if (View.VISIBLE == visibility) {
            emojiEditComment.requestFocus();
//            //弹出键盘
//            if (dmlSearchCommentBean != null) {
//                emojiEditComment.setHint("回复" + dmlSearchCommentBean.getNickname() + ":");
//            } else {
//                emojiEditComment.setHint("");
//            }
            CommonUtils.showSoftInput(emojiEditComment.getContext(), emojiEditComment);
            tvSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断有内容调用接口
                    String comment = emojiEditComment.getText().toString();
                    if (!TextUtils.isEmpty(comment)) {
                        comment = emojiEditComment.getText().toString();
                        FeaturedArticleCommentBean featuredArticleCommentBean = new FeaturedArticleCommentBean();
                        featuredArticleCommentBean.setContent(comment);
                        featuredArticleCommentBean.setAvatar("http://image.domolife.cn/2016-04-26_571f105476475.jpg");
                        featuredArticleCommentBean.setLikeCount(featuredArticleCommentList.size());
                        featuredArticleCommentBean.setTime("2019-03-08");
                        featuredArticleCommentBean.setNickName("kimi");
                        featuredArticleCommentList.add(featuredArticleCommentBean);
                        featuredArticleCommentAdapter.notifyDataSetChanged();
//                        CommunalComment communalComment = new CommunalComment();
//                        communalComment.setCommType(COMMENT_TYPE);
//                        communalComment.setContent(comment);
//                        if (dmlSearchCommentBean != null) {
//                            communalComment.setIsReply(1);
//                            communalComment.setReplyUserId(dmlSearchCommentBean.getUid());
//                            communalComment.setPid(dmlSearchCommentBean.getId());
//                            communalComment.setMainCommentId(dmlSearchCommentBean.getMainContentId() > 0
//                                    ? dmlSearchCommentBean.getMainContentId() : dmlSearchCommentBean.getId());
//                        }
//                        communalComment.setObjId(dmlSearchDetailBean.getId());
//                        communalComment.setUserId(userId);
//                        communalComment.setToUid(dmlSearchDetailBean.getUid());
//                        sendComment(communalComment);

                    } else {
                        showToast(FeaturedArticleCommentActivity.this, "请正确输入内容");
                    }
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(emojiEditComment.getContext(), emojiEditComment);
        }
    }
    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnTouch(R.id.communal_recycler)
    boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - locationY;//y轴距离
                if (Math.abs(moveY) > 250) {
                    commentViewVisible(GONE, null);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }

    @OnClick(R.id.tv_publish_comment)
    void publishComment(View view) {
        if (userId > 0) {
            setPublishComment();
        } else {
            getLoginStatus(FeaturedArticleCommentActivity.this);
        }
    }

    //文章点赞
    @OnClick(R.id.tv_article_bottom_like)
    void likeArticle(View view) {
//        if (dmlSearchDetailBean != null) {
//            if (userId > 0) {
//                setArticleLike();
//            } else {
//                getLoginStatus(FeaturedArticleCommentActivity.this);
//            }
//        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
