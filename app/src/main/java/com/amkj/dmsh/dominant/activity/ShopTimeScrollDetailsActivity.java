package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.alipay.AliPayResult;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.ArticleCommentEntity;
import com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.CommunalComment;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.adapter.AdapterReceiverComment;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.LuckyMoneyBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.CustomPopWindow;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.stat.StatService;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import emojicon.EmojiconEditText;

import static android.view.View.GONE;
import static com.amkj.dmsh.R.id.tv_send_comment;
import static com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean.FOOT_EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_EMPTY_OBJECT;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getWaterMarkImgUrl;

;

public class ShopTimeScrollDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    //    商品详情
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    //评论框
    @BindView(R.id.emoji_edit_comment)
    EmojiconEditText emoji_edit_comment;
    //    评论发送
    @BindView(tv_send_comment)
    TextView tv_sendComment;
    @BindView(R.id.start_layout)
    RelativeLayout start_layout;
    //    评论布局
    @BindView(R.id.ll_input_comment)
    LinearLayout ll_input_comment;
    //    设置提醒 || 立即购买 ？ 已抢光
    @BindView(R.id.tv_time_set_warm_start)
    TextView tv_time_set_warm_start;
    // 提前预览
    @BindView(R.id.tv_time_ahead_watch_start)
    TextView tv_time_ahead_watch_start;
    //    底栏按钮
    @BindView(R.id.fl_bottom_layout)
    FrameLayout fl_bottom_layout;
    boolean isStartBuy;
    //    轮播图片视频
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    private List<CommunalDetailObjectBean> itemBodyList = new ArrayList<>();
    private CommunalDetailAdapter contentOfficialAdapter;
    //产品Id
    private String productId;
    private int page = 1;
    private AdapterReceiverComment adapterReceiverComment;
    private List<ArticleCommentEntity.ArticleCommentBean> articleCommentList = new ArrayList<>();
    private ShopDetailsEntity shopDetailsEntity;
    private String thirdUrl;
    private String thirdId;
    private ShopTimeHeaderView shopTimeHeaderView;
    private ShopTimeFootView shopTimeFootView;
    private int scrollY = 0;
    private float screenHeight;
    //    按下点击位置
    private float locationY;
    private CustomPopWindow mCustomPopWindow;
    private String sharePageUrl = Url.BASE_SHARE_PAGE_TWO + "m/template/common/taoBaoGoods.html?id=";
    private ShopPropertyBean shopProperty;
    private ConstantMethod constantMethod;
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.activity_time_goods_details;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        communal_recycler.setLayoutManager(new LinearLayoutManager(ShopTimeScrollDetailsActivity.this));
        contentOfficialAdapter = new CommunalDetailAdapter(ShopTimeScrollDetailsActivity.this, itemBodyList);
        View headerView = LayoutInflater.from(ShopTimeScrollDetailsActivity.this).inflate(R.layout.layout_time_shop_details_header, (ViewGroup) communal_recycler.getParent(), false);
        View footView = LayoutInflater.from(ShopTimeScrollDetailsActivity.this).inflate(R.layout.layout_time_shop_details_foot, (ViewGroup) communal_recycler.getParent(), false);
        shopTimeHeaderView = new ShopTimeHeaderView();
        ButterKnife.bind(shopTimeHeaderView, headerView);
        shopTimeFootView = new ShopTimeFootView();
        ButterKnife.bind(shopTimeFootView, footView);
        shopTimeFootView.initView();
        contentOfficialAdapter.addHeaderView(headerView);
        contentOfficialAdapter.addFooterView(footView);
        communal_recycler.setAdapter(contentOfficialAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        shopTimeFootView.recycler_shop_time_content_list.setLayoutManager(new LinearLayoutManager(ShopTimeScrollDetailsActivity.this));
        adapterReceiverComment = new AdapterReceiverComment(ShopTimeScrollDetailsActivity.this, articleCommentList);
        shopTimeFootView.recycler_shop_time_content_list.setAdapter(adapterReceiverComment);
        shopTimeFootView.recycler_shop_time_content_list.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        contentOfficialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (loadHud != null) {
                    loadHud.show();
                }
                switch (view.getId()) {
                    case R.id.tv_click_get_lucky_money:
                    case R.id.ll_get_lucky_money:
                        CommunalDetailObjectBean couponBean = (CommunalDetailObjectBean) view.getTag();
                        if (couponBean != null) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            if (userId != 0) {
                                skipAliBCWebView(couponBean.getCouponUrl());
                            } else {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                getLoginStatus(ShopTimeScrollDetailsActivity.this);
                            }
                        }
                        break;
                }
            }
        });
        adapterReceiverComment.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position >= 0) {
                    if (userId > 0) {
                        if (View.VISIBLE == ll_input_comment.getVisibility()) {
                            updateEditTextBodyVisible(GONE, articleCommentList.get(position));
                        } else {
                            updateEditTextBodyVisible(View.VISIBLE, articleCommentList.get(position));
                        }
                    } else {
                        getLoginStatus(ShopTimeScrollDetailsActivity.this);
                    }
                }
            }
        });
        adapterReceiverComment.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_receiver_content:
                        ArticleCommentEntity.ArticleCommentBean articleCommentBean = (ArticleCommentEntity.ArticleCommentBean) view.getTag();
                        if (articleCommentBean != null) {
                            if (userId > 0) {
                                if (View.VISIBLE == ll_input_comment.getVisibility()) {
                                    updateEditTextBodyVisible(GONE, articleCommentBean);
                                } else {
                                    updateEditTextBodyVisible(View.VISIBLE, articleCommentBean);
                                }
                            } else {
                                getLoginStatus(ShopTimeScrollDetailsActivity.this);
                            }
                        }
                        break;
                }
            }
        });
        adapterReceiverComment.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= adapterReceiverComment.getItemCount()) {
                    page++;
                    getProductComment();
                } else {
                    if (!(page == 1 && adapterReceiverComment.getItemCount() == 1
                            && articleCommentList.get(0).getItemType() == FOOT_EMPTY_CODE)) {
                        adapterReceiverComment.loadMoreEnd(false);
                    }
                }

            }
        }, shopTimeFootView.recycler_shop_time_content_list);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getApplication();
                    screenHeight = app.getScreenHeight();
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
    }

    public void updateEditTextBodyVisible(int visibility, final ArticleCommentBean commentBean) {
        ll_input_comment.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            start_layout.setVisibility(GONE);
            ll_input_comment.setVisibility(View.VISIBLE);
        } else {
            start_layout.setVisibility(View.VISIBLE);
            ll_input_comment.setVisibility(View.GONE);
        }
        if (View.VISIBLE == visibility) {
            emoji_edit_comment.requestFocus();
            if (commentBean != null) {
                emoji_edit_comment.setHint("回复" + commentBean.getNickname() + ":");
            } else {
                emoji_edit_comment.setHint("");
            }
            CommonUtils.showSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
            tv_sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断有内容调用接口
                    String comment = emoji_edit_comment.getText().toString();
                    if (comment.length() > 0) {
                        comment = emoji_edit_comment.getText().toString();
                    }
                    if (comment.length() > 0) {
                        CommunalComment communalComment = new CommunalComment();
                        communalComment.setCommType("goods");
                        communalComment.setContent(comment);
                        if (commentBean != null) {
                            ShopPropertyBean shopPropertyBean = shopDetailsEntity.getShopPropertyBean();
                            communalComment.setIsReply(1);
                            communalComment.setReplyUserId(commentBean.getUid());
                            try {
                                communalComment.setPid(commentBean.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            communalComment.setObjId(shopPropertyBean.getId());
                            communalComment.setToUid(commentBean.getUid());
                        } else if (shopDetailsEntity != null && shopDetailsEntity.getShopPropertyBean() != null) {
                            ShopPropertyBean shopPropertyBean = shopDetailsEntity.getShopPropertyBean();
                            communalComment.setObjId(shopPropertyBean.getId());
                        }
                        communalComment.setUserId(userId);
                        sendComment(communalComment);
                    } else {
                        showToast(ShopTimeScrollDetailsActivity.this, "请正确输入内容");
                    }
                }
            });
            emoji_edit_comment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().length() > 0) {
                        tv_sendComment.setEnabled(true);
                    } else {
                        tv_sendComment.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(emoji_edit_comment.getContext(), emoji_edit_comment);
        }
    }

    //发送评论
    // invitationDetails 文章内容参数
    private void sendComment(CommunalComment communalComment) {
        loadHud.setCancellable(true);
        loadHud.show();
        tv_sendComment.setText("发送中…");
        tv_sendComment.setEnabled(false);
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnSendCommentFinish(new ConstantMethod.OnSendCommentFinish() {
            @Override
            public void onSuccess() {
                loadHud.dismiss();
                tv_sendComment.setText("发送");
                tv_sendComment.setEnabled(true);
                updateEditTextBodyVisible(GONE, null);
                page = 1;
                getProductComment();
                emoji_edit_comment.setText("");
            }

            @Override
            public void onError() {
                loadHud.dismiss();
                tv_sendComment.setText("发送");
                tv_sendComment.setEnabled(true);
            }
        });
        constantMethod.setSendComment(ShopTimeScrollDetailsActivity.this, communalComment);
    }

    @Override
    protected void postEventResult(EventMessage message) {
        String type = message.type;
        switch (type) {
            case "showEditView":
                if (userId > 0) {
                    if (View.VISIBLE == ll_input_comment.getVisibility()) {
                        updateEditTextBodyVisible(GONE, null);
                    } else {
                        updateEditTextBodyVisible(View.VISIBLE, null);
                    }
                } else {
                    getLoginStatus(ShopTimeScrollDetailsActivity.this);
                }
                break;
            case "replyComment":
                if (userId > 0) {
                    ArticleCommentBean commentBean = (ArticleCommentBean) message.result;
                    if (View.VISIBLE == ll_input_comment.getVisibility()) {
                        updateEditTextBodyVisible(GONE, commentBean);
                    } else {
                        updateEditTextBodyVisible(View.VISIBLE, commentBean);
                    }
                } else {
                    getLoginStatus(ShopTimeScrollDetailsActivity.this);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            CallbackContext.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 销毁电商SDK相关资源引用，防止内存泄露
         */
        AlibcTradeSDK.destory();
    }

    @Override
    protected void loadData() {
        page = 1;
        getHeaderData();
        getProductComment();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //  评论内容
    private void getProductComment() {
        String url = Url.BASE_URL + Url.FIND_INVI_COMMENT_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        params.put("currentPage", page);
        params.put("comtype", "goods");
        if (userId != 0) {
            params.put("uid", userId);
        }
        if (NetWorkUtils.checkNet(ShopTimeScrollDetailsActivity.this)) {
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    adapterReceiverComment.loadMoreComplete();
                    if (page == 1) {
                        articleCommentList.clear();
                    }
                    Gson gson = new Gson();
                    ArticleCommentEntity articleCommentEntity = gson.fromJson(result, ArticleCommentEntity.class);
                    if (articleCommentEntity != null) {
                        if (articleCommentEntity.getCode().equals("01")) {
                            articleCommentList.addAll(articleCommentEntity.getArticleCommentList());
                            shopTimeFootView.tv_show_text_all_comment.setText("全部评论");
                        } else if (articleCommentEntity.getCode().equals("02")) {
                            ArticleCommentEntity.ArticleCommentBean articleCommentBean;
                            if (page == 1 && adapterReceiverComment.getItemCount() - adapterReceiverComment.getHeaderLayoutCount() < 1) {
                                shopTimeFootView.tv_show_text_all_comment.setText("暂无评论");
                                articleCommentBean = new ArticleCommentEntity.ArticleCommentBean();
                                articleCommentBean.setItemType(FOOT_EMPTY_CODE);
                                articleCommentList.add(articleCommentBean);
                            }
                        } else {
                            showToast(ShopTimeScrollDetailsActivity.this, articleCommentEntity.getMsg());
                        }
                            adapterReceiverComment.notifyDataSetChanged();
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    adapterReceiverComment.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    //商品详情内容
    private void getHeaderData() {
        String url = Url.BASE_URL + Url.H_TIME_GOODS_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        params.put("userId", userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(ShopTimeScrollDetailsActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                fl_bottom_layout.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                shopDetailsEntity = gson.fromJson(result, ShopDetailsEntity.class);
                if (shopDetailsEntity != null) {
                    if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        setTimeProductData(shopDetailsEntity);
                    } else if (shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(ShopTimeScrollDetailsActivity.this, R.string.shopOverdue);
                    } else {
                        showToast(ShopTimeScrollDetailsActivity.this, shopDetailsEntity.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,shopProperty,shopDetailsEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                showToast(ShopTimeScrollDetailsActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,shopProperty,shopDetailsEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                showToast(ShopTimeScrollDetailsActivity.this, R.string.connectedFaile);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,shopProperty,shopDetailsEntity);
            }
        });
    }

    // 点击评论
    @OnClick({R.id.tv_time_details_comment_start})
    void clickComment(View view) {
        if (userId > 0) {
            if (View.VISIBLE == ll_input_comment.getVisibility()) {
                updateEditTextBodyVisible(GONE, null);
            } else {
                updateEditTextBodyVisible(View.VISIBLE, null);
            }
        } else {
            getLoginStatus(ShopTimeScrollDetailsActivity.this);
        }
    }

    private void cancelWarm(int productId) {
        String url = Url.BASE_URL + Url.CANCEL_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        getHeaderData();
                    } else {
                        showToast(ShopTimeScrollDetailsActivity.this, status.getMsg());
                    }
                    tv_time_set_warm_start.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                tv_time_set_warm_start.setEnabled(true);
                loadHud.dismiss();
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setWarm(int productId) {
        String url = Url.BASE_URL + Url.ADD_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        getHeaderData();
                    } else {
                        showToast(ShopTimeScrollDetailsActivity.this, status.getMsg());
                    }
                    tv_time_set_warm_start.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                tv_time_set_warm_start.setEnabled(true);
                super.onError(ex, isOnCallback);
            }
        });
    }

    public void setTimeProductData(final ShopDetailsEntity detailsEntity) {
        shopProperty = detailsEntity.getShopPropertyBean();
        Properties prop = new Properties();
        prop.setProperty("proName", getStrings(shopProperty.getName()));
        prop.setProperty("proId", String.valueOf(shopProperty.getId()));
        StatService.trackCustomKVEvent(this, "timeProLook", prop);
        imagesVideoList.clear();
        String[] images = shopProperty.getImages().split(",");
        CommunalADActivityBean communalADActivityBean;
        if (images.length != 0) {
            List<String> imageList = Arrays.asList(images);
            for (int i = 0; i < imageList.size(); i++) {
                communalADActivityBean = new CommunalADActivityBean();
                if (i == 0) {
                    communalADActivityBean.setPicUrl(getWaterMarkImgUrl(imageList.get(i), shopProperty.getWaterRemark()));
                } else {
                    communalADActivityBean.setPicUrl(imageList.get(i));
                }
                imagesVideoList.add(communalADActivityBean);
            }
        } else {
            communalADActivityBean = new CommunalADActivityBean();
            communalADActivityBean.setPicUrl(getStrings(shopProperty.getPicUrl()));
            imagesVideoList.add(communalADActivityBean);
        }
//         轮播图
        if (cbViewHolderCreator == null) {
            cbViewHolderCreator = new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new CommunalAdHolderView(itemView, ShopTimeScrollDetailsActivity.this, false);
                }

                @Override
                public int getLayoutId() {
                    return R.layout.layout_ad_image_video;
                }
            };
        }
        shopTimeHeaderView.banner_shop_time_details.setPages(ShopTimeScrollDetailsActivity.this, cbViewHolderCreator, imagesVideoList).setCanLoop(true)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius});
//        商品名字
        itemBodyList.clear();
        shopTimeHeaderView.tv_shop_time_product_name.setText(shopProperty.getName());
//        商品价格区间
        if (!TextUtils.isEmpty(shopProperty.getPrice()) && !TextUtils.isEmpty(shopProperty.getMaxPrice())) {
            if (getFloatNumber(shopProperty.getPrice()) < getFloatNumber(shopProperty.getMaxPrice())) {
                shopTimeHeaderView.tv_shop_time_product_price.setText("￥" + shopProperty.getPrice() + "-" + shopProperty.getMaxPrice());
            } else {
                shopTimeHeaderView.tv_shop_time_product_price.setText("￥" + shopProperty.getPrice());
            }
        } else {
            if (!TextUtils.isEmpty(shopProperty.getPrice())) {
                shopTimeHeaderView.tv_shop_time_product_price.setText("￥" + shopProperty.getPrice());
            } else {
                shopTimeHeaderView.tv_shop_time_product_price.setVisibility(GONE);
            }
        }
//           市场价
        shopTimeHeaderView.tv_shop_time_product_mk_price.setText("市场价：￥" + shopProperty.getMarketPrice());
        //中间加横线
        shopTimeFootView.tv_article_details_count.setText(String.valueOf(shopProperty.getCommentNum()));
        if (shopProperty.getSkimEnable() == 1) {
            tv_time_ahead_watch_start.setEnabled(true);
            tv_time_ahead_watch_start.setText("提前预览");
        } else {
            tv_time_ahead_watch_start.setEnabled(false);
            tv_time_ahead_watch_start.setText("不可预览");
        }
        if (shopProperty.isRemind()) {
            tv_time_set_warm_start.setText("取消提醒");
        } else {
            tv_time_set_warm_start.setText("开团提醒");
        }
        setCountTime();
        if (!ConstantMethod.isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopProperty.getEndTime())) {
            getConstant();
            constantMethod.createSchedule();
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    shopProperty.setAddSecond(shopProperty.getAddSecond() + 1);
                    setCountTime();
                }
            });
        } else {
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
        }
//            详情内容
        if (shopProperty.getLuckyMoney() != null || shopProperty.getItemBody() != null || shopProperty.getFlashsaleInfo() != null) {
            itemBodyList.clear();
            if (shopProperty.getLuckyMoney() != null && shopProperty.getLuckyMoney().size() > 0) {
                List<LuckyMoneyBean> luckyMoney = shopProperty.getLuckyMoney();
                CommunalDetailObjectBean communalDetailObjectBean;
                for (int i = 0; i < luckyMoney.size(); i++) {
                    LuckyMoneyBean luckyMoneyBean = luckyMoney.get(i);
                    communalDetailObjectBean = new CommunalDetailObjectBean();
                    communalDetailObjectBean.setName(luckyMoneyBean.getName());
                    communalDetailObjectBean.setCouponUrl(luckyMoneyBean.getUrl());
                    communalDetailObjectBean.setItemType(1);
                    itemBodyList.add(communalDetailObjectBean);
                }
            }
            if (shopProperty.getItemBody() != null && shopProperty.getItemBody().size() > 0) {
                List<CommunalDetailBean> itemBody = shopProperty.getItemBody();
                if (itemBody != null) {
                    itemBodyList.addAll(ConstantMethod.getDetailsDataList(itemBody));
                }
            }
            contentOfficialAdapter.setNewData(itemBodyList);
        }
        if (itemBodyList.size() < 1) {
            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
            communalDetailObjectBean.setItemType(TYPE_EMPTY_OBJECT);
            itemBodyList.add(communalDetailObjectBean);
            contentOfficialAdapter.setNewData(itemBodyList);
        }
        thirdUrl = shopProperty.getThirdUrl();
        thirdId = shopProperty.getThirdId();
    }

    // 提前预览
    @OnClick({R.id.tv_time_ahead_watch_start})
    void aHeadWatch(View view) {
        if (userId != 0) {
            skipNewTaoBao();
            if (shopProperty != null) {
                Properties prop = new Properties();
                prop.setProperty("proName", getStrings(shopProperty.getName()));
                prop.setProperty("proId", String.valueOf(shopProperty.getId()));
                StatService.trackCustomKVEvent(this, "timeProAhead", prop);
            }
        } else {
            getLoginStatus(ShopTimeScrollDetailsActivity.this);
        }
    }

    //    立即购买 设置提醒 取消提醒
    @OnClick(R.id.tv_time_set_warm_start)
    void buySetWarm(View view) {
//        立即购买
        if (isStartBuy) {
            if (userId != 0) {
                skipNewTaoBao();
                if (shopProperty != null) {
                    Properties prop = new Properties();
                    prop.setProperty("proName", getStrings(shopProperty.getName()));
                    prop.setProperty("proId", String.valueOf(shopProperty.getId()));
                    StatService.trackCustomKVEvent(this, "timeProBuy", prop);
                }
            } else {
                getLoginStatus(ShopTimeScrollDetailsActivity.this);
            }
        } else {
            if (shopDetailsEntity.getShopPropertyBean() != null) {
                if (userId != 0) {
                    isFirstRemind(shopDetailsEntity);
                } else {
                    getLoginStatus(ShopTimeScrollDetailsActivity.this);
                }
            }
        }
    }

    @OnClick(R.id.tv_time_details_service)
    void foundService(View view) {
        skipServiceDataInfo(shopDetailsEntity);
    }

    private void isFirstRemind(final ShopDetailsEntity shopDetailsEntity) {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus foreShowBean = gson.fromJson(result, RequestStatus.class);
                if (foreShowBean != null) {
                    if (foreShowBean.getCode().equals("01")) {
                        if (foreShowBean.getResult().isHadRemind()) { //已设置过提醒
                            if (shopDetailsEntity != null) {
                                loadHud.show();
                                if (shopDetailsEntity.getShopPropertyBean().isRemind()) {
//            取消提醒
                                    cancelWarm(shopDetailsEntity.getShopPropertyBean().getId());
                                } else {
//            设置提醒
                                    setWarm(shopDetailsEntity.getShopPropertyBean().getId());
                                }
                            }
                        } else {
                            setDefaultWarm();
                        }
                    } else if (!foreShowBean.getCode().equals("02")) {
                        showToast(ShopTimeScrollDetailsActivity.this, foreShowBean.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ShopTimeScrollDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void setDefaultWarm() {
//        设置提醒
        View indentPopWindow = LayoutInflater.from(ShopTimeScrollDetailsActivity.this).inflate(R.layout.layout_first_time_product_warm, communal_recycler, false);
        AutoUtils.autoSize(indentPopWindow);
        PopupWindowView popupWindowView = new PopupWindowView();
        ButterKnife.bind(popupWindowView, indentPopWindow);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(ShopTimeScrollDetailsActivity.this)
                .setView(indentPopWindow)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
        popupWindowView.rp_time_pro_warm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String numberWarm = radioButton.getText().toString().trim();
                Message message = new Message();
                message.arg1 = 1;
                message.obj = numberWarm;
                handler.sendMessageDelayed(message, 618);
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                String number = (String) msg.obj;
                mCustomPopWindow.dissmiss();
                setWarmTime(getNumber(number));
            }
            return false;
        }
    });

    private void setWarmTime(String number) {
        String url = Url.BASE_URL + Url.TIME_WARM_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("m_uid", userId);
        params.put("longtime", number);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null && requestStatus.getCode().equals("01")) {
                    showToast(ShopTimeScrollDetailsActivity.this, "已设置产品提醒时间，提前" + requestStatus.getLongtime() + "分钟");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }
    //    七鱼客服
    private void skipServiceDataInfo(ShopDetailsEntity shopDetailsEntity) {
        QyProductIndentInfo qyProductIndentInfo = null;
        String sourceTitle = "";
        String sourceUrl = "";
        if (shopDetailsEntity != null) {
            ShopPropertyBean shopPropertyBean = shopDetailsEntity.getShopPropertyBean();
            qyProductIndentInfo = new QyProductIndentInfo();
            sourceUrl = sharePageUrl + shopPropertyBean.getId();
            sourceTitle = "限时特惠详情：" + shopPropertyBean.getName();
            qyProductIndentInfo.setUrl(sourceUrl);
            qyProductIndentInfo.setTitle(getStrings(shopPropertyBean.getName()));
            qyProductIndentInfo.setPicUrl(getStrings(shopPropertyBean.getPicUrl()));
            qyProductIndentInfo.setNote("￥" + getStrings(shopPropertyBean.getPrice()));
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, sourceTitle, sourceUrl, qyProductIndentInfo);
    }
    public void setCountTime() {
        //格式化结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date startTime;
        Date endTime;
        try {
            //格式化结束时间
            Date dateCurrent;
            if (!TextUtils.isEmpty(shopDetailsEntity.getCurrentTime())) {
                dateCurrent = formatter.parse(shopDetailsEntity.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            startTime = formatter.parse(shopProperty.getStartTime());
            if (dateCurrent.getTime() < startTime.getTime()) {
                DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
                dynamic.setTimeTextColor(0xff333333);
                dynamic.setSuffixTextColor(0xff333333);
                shopTimeHeaderView.ct_pro_show_time_detail.dynamicShow(dynamic.build());
                long timeMillis = startTime.getTime() - dateCurrent.getTime() - shopProperty.getAddSecond() * 1000;
                shopTimeHeaderView.ct_pro_show_time_detail.updateShow(timeMillis);
                shopTimeHeaderView.tv_foreShow_time_detail_status.setText("距开团");
                start_layout.setVisibility(View.VISIBLE);
                isStartBuy = false;
            } else {
                isStartBuy = true;
                //格式化开始时间
                endTime = formatter.parse(shopProperty.getEndTime());
                long timeMillis = endTime.getTime() - dateCurrent.getTime() - shopProperty.getAddSecond() * 1000;
                shopTimeHeaderView.ct_pro_show_time_detail.updateShow(timeMillis);
                shopTimeHeaderView.tv_foreShow_time_detail_status.setText("距结束");
                tv_time_ahead_watch_start.setVisibility(View.INVISIBLE);
                if (timeMillis < 1) {
                    tv_time_set_warm_start.setEnabled(false);
                    tv_time_set_warm_start.setText("已结束");
                } else if (shopProperty.getQuantity() == 0) {
                    tv_time_set_warm_start.setEnabled(false);
                    tv_time_set_warm_start.setText("已抢光");
                } else {
                    tv_time_set_warm_start.setEnabled(true);
                    tv_time_set_warm_start.setText("立即购买");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!ConstantMethod.isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopProperty.getEndTime())) {
            shopTimeHeaderView.ct_pro_show_time_detail.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.setOnCountdownEndListener(null);
                    loadData();
                }
            });
        } else {
            shopTimeHeaderView.ct_pro_show_time_detail.setOnCountdownEndListener(null);
        }
    }

    private void skipNewTaoBao() {
        if (thirdId != null || thirdUrl != null) {
            setClickProductTotal();
            if (thirdId != null) {
                AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(new AlibcLoginCallback() {
                    @Override
                    public void onSuccess(int i) {
                        skipNewShopDetails();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        showToast(ShopTimeScrollDetailsActivity.this, "登录失败 ");
                    }
                });
            } else {
                skipNewShopDetails();
            }
        } else {
            showToast(this, "地址缺失");
        }
    }

    /**
     * 设置限时特惠购买统计
     */
    private void setClickProductTotal() {
        if (NetWorkUtils.checkNet(ShopTimeScrollDetailsActivity.this)) {
            String url = Url.BASE_URL + Url.TIME_PRODUCT_CLICK_TOTAL;
            Map<String, Object> params = new HashMap<>();
            params.put("id", productId);
            XUtil.Post(url, params, new MyCallBack<String>() {
            });
        }
    }

    private void skipNewShopDetails() {
//                    跳转淘宝商品详情
        if (!TextUtils.isEmpty(thirdId)) {
            /**
             * 打开电商组件, 使用默认的webview打开
             *
             * @param activity             必填
             * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
             * @param showParams           show参数
             * @param taokeParams          淘客参数
             * @param trackParam           yhhpass参数
             * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
             * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
             */
            //提供给三方传递配置参数
            Map<String, String> exParams = new HashMap<>();
            exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
            //设置页面打开方式
            AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
            //实例化商品详情 itemID打开page
            AlibcBasePage ordersPage = null;
            if (thirdId != null) {
                ordersPage = new AlibcDetailPage(thirdId);
            } else {
                ordersPage = new AlibcPage(thirdUrl);
            }
//            AlibcTaokeParams taokeParams = new AlibcTaokeParams();
//            taokeParams.pid = "mm_113346569_43964046_400008826";

            AlibcTrade.show(ShopTimeScrollDetailsActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
                @Override
                public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                    //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                    showToast(ShopTimeScrollDetailsActivity.this, "获取详情成功");
                }

                @Override
                public void onFailure(int code, String msg) {
                    //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                    showToast(ShopTimeScrollDetailsActivity.this, msg);
                }
            });
        } else if (!TextUtils.isEmpty(thirdUrl)) {
//                     网页地址
            Intent intent = new Intent();
            intent.setClass(ShopTimeScrollDetailsActivity.this, DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", thirdUrl);
            startActivity(intent);
        }
    }

    public void skipAliBCWebView(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (userId != 0) {
                skipNewTaoBao(url);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus(ShopTimeScrollDetailsActivity.this);
            }
        } else {
            showToast(ShopTimeScrollDetailsActivity.this, "地址缺失");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    }

    private void skipNewTaoBao(final String url) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                skipNewShopDetails(url);
            }

            @Override
            public void onFailure(int code, String msg) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(ShopTimeScrollDetailsActivity.this, "登录失败 ");
            }
        });
    }

    private void skipNewShopDetails(String url) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //实例化商品详情 itemID打开page
        AlibcBasePage ordersPage = new AlibcPage(url);
        AlibcTrade.show(ShopTimeScrollDetailsActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                showToast(context, "获取详情成功");
                Log.d("商品详情", "onTradeSuccess: ");
                AliPayResult payResult = alibcTradeResult.payResult;
                List<String> paySuccessOrders = payResult.paySuccessOrders;
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d("商品详情", "onFailure: " + code + msg);
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                showToast(ShopTimeScrollDetailsActivity.this, msg);
            }
        });
    }

    class ShopTimeHeaderView {
        //    轮播图
        @BindView(R.id.banner_shop_time_details)
        ConvenientBanner banner_shop_time_details;
        //商品名称
        @BindView(R.id.tv_shop_time_product_name)
        TextView tv_shop_time_product_name;
        //        商品价格
        @BindView(R.id.tv_shop_time_product_price)
        TextView tv_shop_time_product_price;
        //        商场参考价
        @BindView(R.id.tv_shop_time_product_mk_price)
        TextView tv_shop_time_product_mk_price;
        //        倒计时
        @BindView(R.id.tv_pro_time_detail_status)
        TextView tv_foreShow_time_detail_status;
        @BindView(R.id.ct_pro_show_time_detail)
        CountdownView ct_pro_show_time_detail;
    }


    class ShopTimeFootView {
        //产品评论列表
        @BindView(R.id.recycler_shop_time_content_list)
        RecyclerView recycler_shop_time_content_list;
        // 产品收藏
        @BindView(R.id.tv_article_collect_count)
        TextView tv_article_details_collect;
        // 产品评论
        @BindView(R.id.tv_article_comment_count)
        TextView tv_article_details_count;
        @BindView(R.id.tv_show_text_all_comment)
        TextView tv_show_text_all_comment;

        public void initView() {
            tv_article_details_collect.setVisibility(GONE);
            recycler_shop_time_content_list.setNestedScrollingEnabled(false);

        }
    }

    class PopupWindowView {
        @BindView(R.id.rp_time_pro_warm)
        RadioGroup rp_time_pro_warm;
    }

    private String getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                return m.group();
        }
        return "3";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantMethod != null) {
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare(View view) {
        if (shopDetailsEntity != null) {
            new UMShareAction(ShopTimeScrollDetailsActivity.this
                    , shopDetailsEntity.getShopPropertyBean().getPicUrl()
                    , "我在多么生活看中了" + shopDetailsEntity.getShopPropertyBean().getName()
                    , getStrings(shopDetailsEntity.getShopPropertyBean().getSubtitle())
                    , sharePageUrl + shopDetailsEntity.getShopPropertyBean().getId());
        }
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
                    updateEditTextBodyVisible(View.GONE, null);
                }
                break;
            case MotionEvent.ACTION_UP:
                locationY = 0;
                break;
        }
        return false;
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }
}

