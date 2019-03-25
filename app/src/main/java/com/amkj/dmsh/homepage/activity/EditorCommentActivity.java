package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.EditorCommentEntity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.EditorCommentAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.CommonUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.bean.EditorCommentEntity.EditorCommentBean;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.EDITOR_SELECT_FAVOR;

/**
 * Created by xiaoxin on 2019/3/16 0016
 * class description:精选留言
 */
public class EditorCommentActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.fl_header_service)
    FrameLayout mFlHeaderService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    @BindView(R.id.rv_comment)
    RecyclerView mRvComment;
    @BindView(R.id.emoji_edit_comment)
    EmojiEditText mEmojiEditComment;
    @BindView(R.id.tv_send_comment)
    TextView mTvSendComment;
    @BindView(R.id.ll_input_comment)
    LinearLayout mLlInputComment;
    @BindView(R.id.tv_publish_comment)
    TextView mTvPublishComment;
    @BindView(R.id.tv_article_bottom_like)
    TextView mTvBottomLike;
    @BindView(R.id.tv_article_bottom_collect)
    TextView mTvArticleBottomCollect;
    @BindView(R.id.ll_article_comment)
    LinearLayout mLlArticleComment;
    @BindView(R.id.rel_article_bottom)
    RelativeLayout mRelArticleBottom;
    List<EditorCommentBean> commentList = new ArrayList<>();
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    private EditorCommentAdapter mCommentAdapter;
    private EditorCommentEntity mEditorCommentEntity;
    private float locationY;
    private int page = 1;
    private String mRedactorpickedId;


    @Override
    protected int getContentView() {
        return R.layout.activity_editor_comment;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText(R.string.editor_select_comment);
        mIvImgService.setVisibility(View.GONE);
        mIvImgShare.setVisibility(View.GONE);
        mTvArticleBottomCollect.setVisibility(View.GONE);
        if (getIntent() != null) {
            mRedactorpickedId = getIntent().getStringExtra("redactorpickedId");
        }
        initRv();
    }

    private void initRv() {
        mCommentAdapter = new EditorCommentAdapter(this, R.layout.item_editor_comment, commentList);
        //留言点赞
        mCommentAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (userId > 0) {
                EditorCommentBean itemBean = (EditorCommentBean) view.getTag();
                if (itemBean != null) {
                    itemBean.setIsFavor(!itemBean.getIsFavor());
                    itemBean.setLikeNum(itemBean.getIsFavor() ? itemBean.getLikeNum() + 1 :
                            itemBean.getLikeNum() - 1);
                    TextView textView = (TextView) view;
                    textView.setSelected(!textView.isSelected());
                    textView.setText(getStrings(String.valueOf(itemBean.getLikeNum())));
                    setCommentLike(itemBean);
                }
            } else {
                getLoginStatus(this);
            }
        });

        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });
        mCommentAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, mRvComment);
        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvComment.setLayoutManager(llManager);
        mRvComment.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(this, 1), getResources().getColor(R.color.text_color_e_s)));
        mRvComment.setAdapter(mCommentAdapter);
    }

    @Override
    protected void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("showCount", TOTAL_COUNT_TEN);
        map.put("redactorpickedId", mRedactorpickedId);
        Map<String, Object> map1 = new HashMap<>();
        map.putAll(map1);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.EDITOR_COMMENT_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                Gson gson = new Gson();
                mEditorCommentEntity = gson.fromJson(result, EditorCommentEntity.class);
                if (mEditorCommentEntity == null) return;
                List<EditorCommentBean> resultList = mEditorCommentEntity.getResult();
                String code = mEditorCommentEntity.getCode();
                if (resultList == null || resultList.size() < 1 || EMPTY_CODE.equals(code)) {
                    mCommentAdapter.loadMoreEnd();
                } else if (SUCCESS_CODE.equals(code)) {
                    if (page == 1) {
                        commentList.clear();
                    }
                    commentList.addAll(resultList);
                    mCommentAdapter.notifyDataSetChanged();
                    mCommentAdapter.loadMoreComplete();
                } else {
                    showToast(EditorCommentActivity.this, mEditorCommentEntity.getMsg());
                    mCommentAdapter.loadMoreFail();
                }

                //更新点赞数量和状态
                mTvHeaderTitle.setText(commentList.size() == 0 ? getString(R.string.editor_select_comment) :
                        getIntegralFormat(EditorCommentActivity.this, R.string.editor_select_comment2, commentList.size()));
                mTvBottomLike.setSelected(mEditorCommentEntity.getIsFavor());
                mTvBottomLike.setText(getStrings(String.valueOf(mEditorCommentEntity.getFavorCount())));
                NetLoadUtils.getNetInstance().showLoadSir(loadService, commentList, mEditorCommentEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                mCommentAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, commentList, mEditorCommentEntity);
            }
        });
    }

    @OnClick({R.id.tv_life_back, R.id.tv_publish_comment, R.id.tv_article_bottom_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //点击写评论
            case R.id.tv_publish_comment:
                if (!TextUtils.isEmpty(mRedactorpickedId)) {
                    if (userId > 0) {
                        commentViewVisible(VISIBLE == mLlInputComment.getVisibility() ? GONE : VISIBLE);
                    } else {
                        getLoginStatus(this);
                    }
                }
                break;
            //文章点赞
            case R.id.tv_article_bottom_like:
                boolean selected = !mTvBottomLike.isSelected();
                if (!TextUtils.isEmpty(mRedactorpickedId)) {
                    if (userId > 0) {
                        setGoodsLiked(selected);
                        try {
                            int num = Integer.parseInt(mTvBottomLike.getText().toString());
                            mTvBottomLike.setText(getStrings(String.valueOf(selected ? num + 1 : num - 1)));
                            mTvBottomLike.setSelected(selected);
                        } catch (Exception ignored) {
                        }
                    } else {
                        getLoginStatus(this);
                    }
                }
                break;
        }
    }

    private void commentViewVisible(int visibility) {
        mLlInputComment.setVisibility(visibility);
        mLlArticleComment.setVisibility(visibility == VISIBLE ? GONE : VISIBLE);
        if (VISIBLE == visibility) {
            mEmojiEditComment.requestFocus();
            //弹出键盘
            mEmojiEditComment.setHint(getString(R.string.comment_article_hint));
            CommonUtils.showSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
            mTvSendComment.setOnClickListener((v) -> {
                //判断有内容调用接口
                String comment = mEmojiEditComment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    comment = mEmojiEditComment.getText().toString();
                    if (!TextUtils.isEmpty(mRedactorpickedId)) {
                        sendComment(comment);
                    }
                } else {
                    showToast(this, "请正确输入内容");
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
        }
    }

    //给文章点赞
    private void setGoodsLiked(boolean selected) {
        Map<String, Object> params = new HashMap<>();
        //文章id
        params.put("redactorpickedId", mRedactorpickedId);
        params.put("operation", selected ? 1 : 0);
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, EDITOR_SELECT_FAVOR, params, null);
    }

    //给留言点赞
    private void setCommentLike(EditorCommentBean editorCommentBean) {
        Map<String, Object> params = new HashMap<>();
        //留言id
        params.put("commentId", editorCommentBean.getId());
        params.put("toUserId", editorCommentBean.getUid());
        params.put("operation", editorCommentBean.getFavorStatus());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.EDITOR_COMMENT_FAVOR, params, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //给小编留言
    private void sendComment(String comment) {
        if (userId > 0) {
            loadHud.show();
            Map<String, Object> map = new HashMap<>();
            map.put("content", comment);
            map.put("redactorpickedId", mRedactorpickedId);
            map.put("uid", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.EDITOR_SELECT_COMMENT, map, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    commentViewVisible(GONE);
                    mEmojiEditComment.setText("");
                    ConstantMethod.showToast(EditorCommentActivity.this, R.string.comment_article_send_success);
                }

                @Override
                public void onNotNetOrException() {
                    loadHud.dismiss();
                }

                @Override
                public void netClose() {
                    showToast(EditorCommentActivity.this, R.string.unConnectedNetwork);
                }

                @Override
                public void onError(Throwable throwable) {
                    showToast(EditorCommentActivity.this, R.string.comment_send_failed);
                }
            });
        } else {
            getLoginStatus(this);
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected View getLoadView() {
        return mSmartLayout;
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

    // Return whether touch the view.
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

    @OnTouch(R.id.rv_comment)
    boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - locationY;//y轴距离
                if (Math.abs(moveY) > 250) {
                    commentViewVisible(GONE);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }
}
