package com.amkj.dmsh.find.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.CommunalComment;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.dominant.bean.PostCommentEntity;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;
import com.amkj.dmsh.find.adapter.PostContentAdapter;
import com.amkj.dmsh.find.adapter.PostRelatedAdapter;
import com.amkj.dmsh.find.bean.PostDetailEntity;
import com.amkj.dmsh.find.bean.PostDetailEntity.PostDetailBean;
import com.amkj.dmsh.find.bean.PostDetailEntity.PostDetailBean.PictureBean;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.find.bean.RelatedGoodsBean;
import com.amkj.dmsh.find.view.PostDetailHeadView;
import com.amkj.dmsh.find.view.PostGoodsView;
import com.amkj.dmsh.find.view.PostReplyPw;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.PostCommentAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.WindowUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.StaggeredDividerItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiEditText;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.skipUserCenter;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_URL;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_FOLLOW_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_POST_CONTENT;
import static com.amkj.dmsh.constant.Url.BASE_SHARE_PAGE_TWO;
import static com.amkj.dmsh.constant.Url.F_INVITATION_DETAIL;
import static com.amkj.dmsh.constant.Url.MINE_INVITATION_DEL;
import static com.amkj.dmsh.constant.Url.Q_DML_SEARCH_COMMENT;


/**
 * Created by xiaoxin on 2019/7/11
 * Version:v4.1.0
 * ClassDescription :发现-帖子详情
 */
public class PostDetailActivity extends BaseActivity {
    @BindView(R.id.post_detail_headview)
    PostDetailHeadView mPostDetailHeadview;
    @BindView(R.id.rv_topic_content)
    RecyclerView mRvRecommend;
    @BindView(R.id.emoji_edit_comment)
    EmojiEditText mEmojiEditComment;
    @BindView(R.id.tv_send_comment)
    TextView mTvSendComment;
    @BindView(R.id.ll_input_comment)
    LinearLayout mLlInputComment;
    @BindView(R.id.tv_publish_comment)
    TextView mTvPublishComment;
    @BindView(R.id.tv_article_bottom_like)
    TextView mTvArticleBottomLike;
    @BindView(R.id.tv_article_bottom_collect)
    TextView mTvArticleBottomCollect;
    @BindView(R.id.ll_article_comment)
    LinearLayout mLlArticleComment;
    @BindView(R.id.rel_article_img_bottom)
    RelativeLayout mRelArticleImgBottom;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mFloatingActionButton;

    private List<PostCommentBean> mCommentList = new ArrayList<>();
    private PostCommentAdapter postCommentAdapter;
    private PostRelatedAdapter postRelatedAdapter;
    private PopupWindow mAlphaPw;
    private float locationY;
    List<PostBean> mPostList = new ArrayList<>();
    PostContentAdapter postAdapter;
    private PostEntity mPostEntity;
    private PostCommentEntity mPostCommentEntity;
    private int scrollY;
    private String mArtId;
    private int mArticletype;
    //    详情描述
    private List<CommunalDetailObjectBean> descriptionDetailList = new ArrayList<>();
    private PostDetailView postDetailView;
    private CommunalDetailAdapter communalDetailAdapter;
    private PostDetailEntity mPostDetailEntity;
    private PostDetailBean mPostDetailBean;
    private AlertDialogHelper delArticleDialogHelper;
    private String richText = "<p>postText</p>";
    private String richImg = "<p style=\"padding:0;margin:0;\"><img src=\"postImg\" style=\"display:block;width: 100%;\" /></p>";
    private String richLink = "<a href=\"postLink\">网页链接</a>";


    @Override
    protected int getContentView() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void initViews() {
        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra("ArtId")) || "0".equals(getIntent().getStringExtra("ArtId"))) {
            showToast("数据有误，请重试");
            finish();
        }

        mArtId = getIntent().getStringExtra("ArtId");
        mArticletype = getIntent().getIntExtra("articletype", 0);
        //记录埋点参数sourceId
        ConstantMethod.saveSourceId(getClass().getSimpleName(), mArtId);
        setFloatingButton(mFloatingActionButton, mRvRecommend);
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            loadData();
            EventBus.getDefault().post(new EventMessage(UPDATE_POST_CONTENT, getSimpleName()));
        });

        //动态设置头部
        mPostDetailHeadview.init(this);
        LinearLayout llHeadUser = mPostDetailHeadview.findViewById(R.id.ll_head_user);
        mRvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY = (dy == 0) ? 0 : scrollY + dy;
                //设置标题栏
                float ratio = scrollY * 1.0f / AutoSizeUtils.mm2px(mAppContext, 130) * 1.0f - 1;
                if (ratio >= 1) {
                    llHeadUser.setAlpha(1);
                } else if (ratio > 0) {
                    llHeadUser.setAlpha(ratio);
                } else {
                    llHeadUser.setAlpha(0);
                }
            }
        });

        //初始化帖子详情
        View view = LayoutInflater.from(this).inflate(R.layout.view_post_detail, (ViewGroup) mRvRecommend.getParent(), false);
        postDetailView = new PostDetailView();
        ButterKnife.bind(postDetailView, view);
        postDetailView.init();

        //初始化推荐帖子列表
        postAdapter = new PostContentAdapter(getActivity(), mPostList, true);
        postAdapter.addHeaderView(view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRvRecommend.setItemAnimator(null);
        mRvRecommend.setLayoutManager(layoutManager);
        mRvRecommend.addItemDecoration(new StaggeredDividerItemDecoration(AutoSizeUtils.mm2px(mAppContext, 10), true));
        mRvRecommend.setAdapter(postAdapter);
        mRvRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[layoutManager.getSpanCount()];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getPostDetail();
    }

    //获取帖子详情
    private void getPostDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", mArtId);
        params.put("isV2", "true");
        if (mArticletype == 1) {
            params.put("version", 1);
        }
        if (userId > 0) {
            params.put("fuid", String.valueOf(userId));
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_INVITATION_DETAIL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mPostDetailEntity = GsonUtils.fromJson(result, PostDetailEntity.class);
                if (mPostDetailEntity != null && mPostDetailEntity.getResult() != null) {
                    mPostDetailBean = mPostDetailEntity.getResult();
                    postDetailView.updateDetail();
                    getComment();
                    getRecommend();
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostDetailBean, mPostDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostDetailBean, mPostDetailEntity);
            }
        });
    }

    //获取帖子评论
    private void getComment() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", mArtId);
        params.put("currentPage", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        params.put("showCount", 3);
        params.put("replyCurrentPage", 1);
        params.put("replyShowCount", DEFAULT_COMMENT_TOTAL_COUNT);
        params.put("comtype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_DML_SEARCH_COMMENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mCommentList.clear();

                mPostCommentEntity = GsonUtils.fromJson(result, PostCommentEntity.class);
                postDetailView.updateComment();
            }

            @Override
            public void onNotNetOrException() {
                postDetailView.mLlComment.setVisibility(mCommentList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    //获取推荐帖子
    private void getRecommend() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", 1);
        map.put("showCount", TOTAL_COUNT_FORTY);
        map.put("type", 6);
        if (mPostDetailBean != null) {
            map.put("topicId", mPostDetailBean.getTopic_id());
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_POST_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mPostEntity = GsonUtils.fromJson(result, PostEntity.class);
                mPostList.clear();
                if (mPostEntity != null) {
                    String code = mPostEntity.getCode();
                    List<PostBean> postList = mPostEntity.getPostList();
                    if (SUCCESS_CODE.equals(code) && postList != null && postList.size() > 0) {
                        mPostList.addAll(postList);
                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mPostEntity.getMsg());
                    }
                }

                postAdapter.notifyItemRangeChanged(0, mPostList.size());
                postDetailView.mTvRecommendPost.setVisibility(mPostList.size() > 0 ? VISIBLE : GONE);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                postDetailView.mTvRecommendPost.setVisibility(mPostList.size() > 0 ? VISIBLE : GONE);
            }
        });
    }

    private void setPublishComment(PostCommentBean postCommentBean) {
        if (mPostDetailBean != null) {
            if (userId > 0) {
                if (View.VISIBLE == mLlInputComment.getVisibility()) {
                    commentViewVisible(GONE, postCommentBean);
                } else {
                    commentViewVisible(View.VISIBLE, postCommentBean);
                }
            } else {
                getLoginStatus(this);
            }
        }
    }

    private void commentViewVisible(int visibility, final PostCommentBean postCommentBean) {
        mLlInputComment.setVisibility(visibility);
        mLlArticleComment.setVisibility(visibility == View.VISIBLE ? GONE : View.VISIBLE);
        if (View.VISIBLE == visibility) {
            mEmojiEditComment.requestFocus();
            //弹出键盘
            if (postCommentBean != null) {
                mEmojiEditComment.setHint("回复" + postCommentBean.getNickname() + ":");
            } else {
                mEmojiEditComment.setHint("");
            }
            CommonUtils.showSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
            mTvSendComment.setOnClickListener(v -> {
                //判断有内容调用接口
                String comment = mEmojiEditComment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    comment = mEmojiEditComment.getText().toString();
                    CommunalComment communalComment = new CommunalComment();
                    communalComment.setCommType(COMMENT_TYPE);
                    communalComment.setContent(comment);
                    if (postCommentBean != null) {
                        communalComment.setIsReply(1);
                        communalComment.setReplyUserId(postCommentBean.getUid());
                        communalComment.setPid(postCommentBean.getId());
                        communalComment.setMainCommentId(postCommentBean.getMain_comment_id() > 0
                                ? postCommentBean.getMain_comment_id() : postCommentBean.getId());
                    }
                    communalComment.setObjId(mPostDetailBean.getId());
                    communalComment.setUserId(userId);
                    communalComment.setToUid(mPostDetailBean.getUid());
                    sendComment(communalComment);
                } else {
                    showToast("请正确输入内容");
                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(mEmojiEditComment.getContext(), mEmojiEditComment);
        }
    }

    //发送评论
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        mTvSendComment.setText("发送中…");
        mTvSendComment.setEnabled(false);
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnSendCommentFinish(new ConstantMethod.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                mTvSendComment.setText("发送");
                mTvSendComment.setEnabled(true);
                showToast(R.string.comment_send_success);
                commentViewVisible(GONE, null);
                getComment();
                mEmojiEditComment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                mTvSendComment.setText("发送");
                mTvSendComment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(this, communalComment);
    }

    @OnClick({R.id.iv_img_share, R.id.tv_article_bottom_like, R.id.tv_article_bottom_collect, R.id.tv_publish_comment})
    public void onViewClicked(View view) {
        if (mPostDetailBean == null) return;
        switch (view.getId()) {
            case R.id.iv_head:
                skipUserCenter(this, mPostDetailBean.getUid());
                break;
            //点击评论帖子
            case R.id.tv_publish_comment:
                setPublishComment(null);
                break;
            //帖子点赞
            case R.id.tv_article_bottom_like:
                SoftApiDao.favorPostDetail(this, mPostDetailBean.getId(), mTvArticleBottomLike);
                break;
            //帖子收藏
            case R.id.tv_article_bottom_collect:
                SoftApiDao.CollectPost(this, mPostDetailBean.getId(), mTvArticleBottomCollect);
                break;
            //分享帖子
            case R.id.iv_img_share:
                if (mPostDetailBean != null) {
                    UMShareAction umShareAction = new UMShareAction(getActivity()
                            , mPostDetailBean.getPath()
                            , mPostDetailBean.getTopic_title()
                            , "@" + mPostDetailBean.getNickname() + ":" + (TextUtils.isEmpty(mPostDetailBean.getDigest()) ? "我刚刚完成了一个分享，去看看吧" : mPostDetailBean.getDigest())
                            , BASE_SHARE_PAGE_TWO + "m/template/find_template/find_detail.html" + "?id=" + mPostDetailBean.getId()
                            , "pages/findDetail/findDetail?id=" + mPostDetailBean.getId(), mPostDetailBean.getId());
                }
                break;
        }
    }

    class PostDetailView {
        @BindView(R.id.iv_head)
        ImageView mIvHead;
        @BindView(R.id.tv_user_name)
        EmojiTextView mTvUserName;
        @BindView(R.id.tv_publish_time)
        TextView mTvPublishTime;
        @BindView(R.id.tv_follow)
        TextView mTvFollow;
        @BindView(R.id.ll_user_info)
        LinearLayout mLlUserInfo;
        @BindView(R.id.tv_view_num)
        TextView mTvViewNum;
        @BindView(R.id.tv_collect_num)
        TextView mTvCollectNum;
        @BindView(R.id.tv_favor_num)
        TextView mTvFavorNum;
        @BindView(R.id.rv_related)
        RecyclerView mRvRelated;
        @BindView(R.id.posterView)
        PostGoodsView mPosterView;
        @BindView(R.id.tv_commnet_num)
        TextView mTvCommnetNum;
        @BindView(R.id.rv_comment)
        RecyclerView mRvComment;
        @BindView(R.id.tv_more_comment)
        TextView mTvMoreComment;
        @BindView(R.id.ll_comment)
        LinearLayout mLlComment;
        @BindView(R.id.rv_detail)
        RecyclerView mRvDetail;
        @BindView(R.id.ll_post_detail)
        LinearLayout mLLPostDetail;
        @BindView(R.id.tv_recommend_post)
        TextView mTvRecommendPost;
        @BindView(R.id.iv_high_quality)
        ImageView mIvHighQuality;


        void init() {
            //初始化富文本列表
            communalDetailAdapter = new CommunalDetailAdapter(getActivity(), descriptionDetailList);
            mRvDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvDetail.setAdapter(communalDetailAdapter);
            //初始化评论列表
            postCommentAdapter = new PostCommentAdapter(getActivity(), mCommentList, COMMENT_TYPE);
            mRvComment.setNestedScrollingEnabled(false);
            mRvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRvComment.setAdapter(postCommentAdapter);
            postCommentAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                EmojiTextView tvContent = (EmojiTextView) adapter.getViewByPosition(mRvComment, position, R.id.tv_comment_content);
                PostCommentBean postCommentBean = (PostCommentBean) view.getTag();
                if (postCommentBean != null) {
                    //点击弹窗（回复和举报）
                    mAlphaPw = new PostReplyPw(getActivity(), postCommentBean.getId(), 2) {
                        @Override
                        public void onCommentClick() {
                            setPublishComment(postCommentBean);
                        }
                    };

                    mAlphaPw.showAsDropDown(tvContent, 0, 0);
                }
                return true;
            });

            postCommentAdapter.setOnItemClickListener((adapter, view, position) -> {
                PostCommentBean postCommentBean = (PostCommentBean) view.getTag();
                if (postCommentBean != null) {
                    setPublishComment(postCommentBean);
                }
            });

            //初始化关联商品列表
            postRelatedAdapter = new PostRelatedAdapter(getActivity(), null);
            mRvRelated.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mRvRelated.setAdapter(postRelatedAdapter);
        }

        void updateDetail() {
            mIvHighQuality.setVisibility(mPostDetailBean.getIsRewarded() == 1 ? VISIBLE : GONE);
            //设置用户信息
            updateUserInfo();
            //普通内容
            descriptionDetailList.clear();
            List<CommunalDetailBean> descriptionList = new ArrayList<>();
            if (mPostDetailBean.getArticletype() == 2) {
                String description = (String) mPostDetailBean.getDescription();
                if (!TextUtils.isEmpty(description)) {
                    CommunalDetailBean communalDetailBean = new CommunalDetailBean();
                    communalDetailBean.setType("text");
                    //判断是否包含网址
                    Matcher matcher = Pattern.compile(REGEX_URL).matcher(description);
                    if (matcher.find()) {
                        //手动转化成超链接
                        String link = matcher.group();
                        communalDetailBean.setContent(richText.replace("postText", description.replace(link, richLink.replace("postLink", link))));
                    } else {
                        communalDetailBean.setContent(richText.replace("postText", description));
                    }
                    descriptionList.add(communalDetailBean);
                }

                List<PictureBean> pictures = mPostDetailBean.getPicture();
                if (pictures != null && pictures.size() > 0) {
                    for (int i = 0; i < pictures.size(); i++) {
                        CommunalDetailBean communalDetailBean = new CommunalDetailBean();
                        communalDetailBean.setType("text");
                        String postimg = richImg.replace("postImg", pictures.get(i).getPath());
                        communalDetailBean.setContent(postimg);
                        descriptionList.add(communalDetailBean);
                    }
                }
            } else if (mPostDetailBean.getArticletype() == 1) {
                try {

                    String Json = GsonUtils.toJson(mPostDetailBean.getDescription());
                    descriptionList = GsonUtils.fromJson(Json, new TypeToken<List<CommunalDetailBean>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (descriptionList != null && descriptionList.size() > 0) {
                List<CommunalDetailObjectBean> objectBeans = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descriptionList);
                for (CommunalDetailObjectBean communalDetailObjectBean : objectBeans) {
                    communalDetailObjectBean.setPost(true);
                }
                descriptionDetailList.addAll(objectBeans);
            }
            communalDetailAdapter.notifyDataSetChanged();

            //设置帖子信息
            mTvViewNum.setText(("浏览·" + mPostDetailBean.getView()));
            mTvCollectNum.setText(("收藏·" + mPostDetailBean.getCollectNum()));
            mTvFavorNum.setText(("赞·" + mPostDetailBean.getFavorNum()));
            mTvArticleBottomLike.setText(String.valueOf(mPostDetailBean.getFavorNum() > 0 ? mPostDetailBean.getFavorNum() : "赞"));
            mTvArticleBottomLike.setSelected(mPostDetailBean.isFavor());
            mTvArticleBottomCollect.setSelected(mPostDetailBean.isCollect());

            //设置帖子关联商品
            List<RelatedGoodsBean> goods = mPostDetailBean.getGoods();
            if (goods != null && goods.size() > 0) {
                //单个商品
                if (goods.size() == 1) {
                    RelatedGoodsBean relatedGoodsBean = goods.get(0);
                    mPosterView.updateData(getActivity(), relatedGoodsBean);
                } else {
                    //多个商品
                    postRelatedAdapter.setNewData(goods);
                }
                mRvRelated.setVisibility(goods.size() == 1 ? View.GONE : View.VISIBLE);
                mPosterView.setVisibility(goods.size() == 1 ? View.VISIBLE : View.GONE);
            }
        }

        void updateUserInfo() {
            //更新头部用户信息
            mPostDetailHeadview.updateData(getActivity(), mPostDetailBean);
            //更新详情用户信息
            GlideImageLoaderUtil.loadRoundImg(getActivity(), mIvHead, mPostDetailBean.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 70), R.drawable.default_ava_img);
            mTvUserName.setText(getStrings(mPostDetailBean.getNickname()));
            mTvPublishTime.setText(getStrings(mPostDetailBean.getCtime()));
            mTvFollow.setText(mPostDetailBean.isFlag() ? "已关注" : "关注");
            mTvFollow.setSelected(mPostDetailBean.isFlag());
            mLlUserInfo.setVisibility(VISIBLE);
        }

        private void updateComment() {
            if (mPostCommentEntity != null) {
                if (mPostCommentEntity.getCode().equals(SUCCESS_CODE)) {
                    int commentSize = mPostCommentEntity.getCommentSize();
                    mTvCommnetNum.setText(getIntegralFormat(getActivity(), R.string.comment_num, commentSize));
                    mTvMoreComment.setText(getIntegralFormat(getActivity(), R.string.more_comment, commentSize));
                    mTvMoreComment.setVisibility(commentSize > 3 ? VISIBLE : GONE);//总评论不足3条时不显示更多评价
                    List<PostCommentBean> commentList = mPostCommentEntity.getCommentList();
                    if (commentList != null && commentList.size() > 0) {
                        mCommentList.addAll(commentList.subList(0, commentList.size() >= 3 ? 3 : commentList.size()));
                    }
                }
            }

            postCommentAdapter.notifyDataSetChanged();
            mLlComment.setVisibility(mCommentList.size() > 0 ? View.VISIBLE : View.GONE);
        }

        //用户中心
        @OnClick(R.id.iv_head)
        void skipUserCenter() {
            ConstantMethod.skipUserCenter(getActivity(), mPostDetailBean.getUid());
        }

        //关注
        @OnClick(R.id.tv_follow)
        void follow() {
            SoftApiDao.followUser(getActivity(), mPostDetailBean.getUid(), mTvFollow, null);
        }

        //查看更多评论
        @OnClick(R.id.tv_more_comment)
        void moreComment() {
            if (mPostDetailBean != null) {
                Intent intent = new Intent(getActivity(), AllPostCommentActivity.class);
                intent.putExtra("objId", mPostDetailBean.getId());
                intent.putExtra("toUid", mPostDetailBean.getUid());
                startActivity(intent);
            }
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartLayout;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        //子评论回复
        if ("replyChildComment".equals(message.type)) {
            PostCommentBean.ReplyCommListBean replyCommListBean = (PostCommentBean.ReplyCommListBean) message.result;
            PostCommentBean postCommentBean = new PostCommentBean();
            postCommentBean.setNickname(replyCommListBean.getNickname());
            postCommentBean.setUid(replyCommListBean.getUid());
            postCommentBean.setId(replyCommListBean.getId());
            postCommentBean.setMain_comment_id(replyCommListBean.getMain_comment_id());
            postCommentBean.setObj_id(replyCommListBean.getObj_id());
            setPublishComment(postCommentBean);
        } else if (UPDATE_FOLLOW_STATUS.equals(message.type)) {
            mPostDetailBean.setFlag((boolean) message.result);
            postDetailView.updateUserInfo();
        } else if (UPDATE_POST_COMMENT.equals(message.type) && getSimpleName().equals(message.result)) {
            getComment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        WindowUtils.closePw(mAlphaPw);
        WindowUtils.closePw(postCommentAdapter.getChildPostCommentPw());
    }

    /**
     * 点击编辑器外区域隐藏键盘 避免点击搜索完没有隐藏键盘
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


    @OnTouch(R.id.rv_topic_content)
    boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - locationY;//y轴距离
                if (Math.abs(moveY) > 250 && mLlInputComment.getVisibility() == VISIBLE) {
                    commentViewVisible(GONE, null);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }

    @Override
    public View getTopView() {
        return mRvRecommend;
    }

    //判断当前是否是自己的帖子（自己的帖子才显示删除按钮）
    public boolean isMyPost() {
        return mPostDetailBean != null && userId == mPostDetailBean.getUid();
    }

    public void showDelDialog() {
        if (delArticleDialogHelper == null) {
            delArticleDialogHelper = new AlertDialogHelper(this);
            delArticleDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                    .setMsg("确定删除该篇帖子？").setCancelText("取消").setConfirmText("提交")
                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s))
                    .setCancelable(false);
            delArticleDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    delArticle();
                }

                @Override
                public void cancel() {
                }
            });
        }
        delArticleDialogHelper.show();
    }


    private void delArticle() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", mArtId);
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, MINE_INVITATION_DEL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        EventBus.getDefault().post(new EventMessage(ConstantVariable.DELETE_POST, mArtId));
                        showToast("删除帖子完成");
                        finish();
                    } else {
                        showToastRequestMsg( requestStatus);
                    }
                }
            }
        });
    }
}
