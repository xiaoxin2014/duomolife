package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.message.bean.OfficialNotifyEntity;
import com.amkj.dmsh.message.bean.OfficialNotifyEntity.OfficialNotifyParseBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/11
 * class description:官方通知详情
 */
public class OfficialNotifyDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    //主：评论列表
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_official_details;
    //    文章详情
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private int scrollY;
    private float screenHeight;
    private String notifyId;
    private CommunalDetailAdapter contentOfficialAdapter;
    private List<CommunalDetailObjectBean> itemBodyList = new ArrayList<>();
    private int uid;
    private CoverTitleView coverTitleView;
    @Override
    protected int getContentView() {
        return R.layout.activity_message_official_notify;
    }
    @Override
    protected void initViews() {
        isLoginStatus();
        tv_header_title.setText("");
        tv_header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        notifyId = intent.getStringExtra("notifyId");
        //        官方文章内容
        contentOfficialAdapter = new CommunalDetailAdapter(this, itemBodyList);
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.setLayoutManager(new LinearLayoutManager(OfficialNotifyDetailsActivity.this));
        View coverView = LayoutInflater.from(OfficialNotifyDetailsActivity.this)
                .inflate(R.layout.layout_communal_article_cover_title, (ViewGroup) communal_recycler.getParent(), false);
        coverTitleView = new CoverTitleView();
        ButterKnife.bind(coverTitleView, coverView);
        contentOfficialAdapter.addHeaderView(coverView);
        communal_recycler.setAdapter(contentOfficialAdapter);
        contentOfficialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
                if (communalDetailBean != null) {
                    ConstantMethod.skipProductUrl(OfficialNotifyDetailsActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
                    ConstantMethod.totalOfficialProNum(communalDetailBean.getId(), notifyId);
                }
            }
        });
        contentOfficialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (loadHud != null) {
                    loadHud.show();
                }
                switch (view.getId()) {
                    case R.id.img_product_coupon_pic:
                        int couponId = (int) view.getTag(R.id.iv_avatar_tag);
                        int type = (int) view.getTag(R.id.iv_type_tag);
                        if (couponId > 0) {
                            if (uid != 0) {
                                if (type == TYPE_COUPON) {
                                    getDirectCoupon(couponId);
                                } else if (type == TYPE_COUPON_PACKAGE) {
                                    getDirectCouponPackage(couponId);
                                }
                            } else {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                getLoginStatus();
                            }
                        }
                        break;
                    case R.id.iv_communal_cover_wrap:
                        CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if (detailObjectBean != null) {
                            if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG) {
                                Intent intent = new Intent(OfficialNotifyDetailsActivity.this, ShopScrollDetailsActivity.class);
                                intent.putExtra("productId", String.valueOf(detailObjectBean.getId()));
                                startActivity(intent);
                                ConstantMethod.totalOfficialProNum(detailObjectBean.getId(), notifyId);
                            }
                        }
                        loadHud.dismiss();
                        break;
                    case R.id.tv_click_get_lucky_money:
                    case R.id.ll_get_lucky_money:
                        CommunalDetailObjectBean couponBean = (CommunalDetailObjectBean) view.getTag();
                        if (couponBean != null) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            if (uid != 0) {
                                skipAliBCWebView(couponBean.getCouponUrl());
                            } else {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                getLoginStatus();
                            }
                        }
                        break;
                    case R.id.iv_ql_bl_add_car:
                        CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                        if (qualityWelPro != null) {
                            if (userId > 0) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(OfficialNotifyDetailsActivity.this, baseAddCarProInfoBean, loadHud);
                            } else {
                                loadHud.dismiss();
                                getLoginStatus();
                            }
                        }
                        break;
                    case R.id.tv_communal_tb_link:
                        CommunalDetailObjectBean tbLink = (CommunalDetailObjectBean) view.getTag();
                        skipAliBCWebView(tbLink.getUrl());
                        break;
                    default:
                        loadHud.dismiss();
                        break;
                }
            }
        });
        smart_official_details.setOnRefreshListener((refreshLayout) -> {
           loadData();
        });
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
                    download_btn_communal.setVisibility(View.GONE);
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
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    public void setStatusColor() {
        super.setStatusColor();
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.H_MES_OFFICIAL_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", notifyId);
        if (uid > 0) {
            params.put("uid", uid);
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                smart_official_details.finishRefresh();
                communal_load.setVisibility(View.GONE);
                communal_empty.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                itemBodyList.clear();
                Gson gson = new Gson();
                OfficialNotifyEntity officialNotifyEntity = gson.fromJson(result, OfficialNotifyEntity.class);
                if (officialNotifyEntity != null) {
                    if (officialNotifyEntity.getCode().equals("01")) {
                        OfficialNotifyParseBean officialNotifyParseBean = officialNotifyEntity.getOfficialNotifyParseBean();
                        tv_header_title.setText(getStrings(officialNotifyParseBean.getTitle()));
                        setMessageOfficialHeader(officialNotifyEntity.getOfficialNotifyParseBean());
                        List<CommunalDetailBean> contentBeanList = officialNotifyEntity.getOfficialNotifyParseBean().getContentBeanList();
                        if (contentBeanList != null) {
                            itemBodyList.clear();
                            itemBodyList.addAll(ConstantMethod.getDetailsDataList(contentBeanList));
                        }
                    } else if (officialNotifyEntity.getCode().equals("02")) {
                        showToast(OfficialNotifyDetailsActivity.this, R.string.invalidData);
                    } else {
                        showToast(OfficialNotifyDetailsActivity.this, officialNotifyEntity.getMsg());
                    }
                    contentOfficialAdapter.setNewData(itemBodyList);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                communal_load.setVisibility(View.GONE);
                communal_empty.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
                smart_official_details.finishRefresh();
                showToast(OfficialNotifyDetailsActivity.this, R.string.unConnectedNetwork);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setMessageOfficialHeader(OfficialNotifyParseBean officialNotifyParseBean) {
        //加载数据
        GlideImageLoaderUtil.loadCenterCrop(OfficialNotifyDetailsActivity.this, coverTitleView.img_article_details_bg, getStrings(officialNotifyParseBean.getCover_url()));
        coverTitleView.tv_article_details_title.setText(getStrings(officialNotifyParseBean.getTitle()));
        coverTitleView.tv_article_details_time.setText(getStrings(officialNotifyParseBean.getCreate_time()));
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    private void getDirectCoupon(int id) {
        String url = Url.BASE_URL + Url.FIND_ARTICLE_COUPON;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", uid);
        params.put("couponId", id);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(OfficialNotifyDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(OfficialNotifyDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void getDirectCouponPackage(int couponId) {
        String url = Url.BASE_URL + Url.COUPON_PACKAGE;
        Map<String, Object> params = new HashMap<>();
        params.put("uId", uid);
        params.put("cpId", couponId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(OfficialNotifyDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(OfficialNotifyDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(OfficialNotifyDetailsActivity.this, R.string.Get_Coupon_Fail);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    public void skipAliBCWebView(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (uid != 0) {
                skipNewTaoBao(url);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus();
            }
        } else {
            showToast(OfficialNotifyDetailsActivity.this, "地址缺失");
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
                showToast(OfficialNotifyDetailsActivity.this, "登录失败 ");
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
        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams();

        AlibcTrade.show(OfficialNotifyDetailsActivity.this, ordersPage, showParams, alibcTaokeParams, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                showToast(context, "获取详情成功");
                Log.d("商品详情", "onTradeSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d("商品详情", "onFailure: " + code + msg);
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                showToast(ShopTimeScrollDetailsActivity.this, msg);
            }
        });
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IS_LOGIN_CODE) {
                getLoginStatus();
            }
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        loadData();
    }

    class CoverTitleView {
        //        标题
        @BindView(R.id.tv_article_details_title)
        TextView tv_article_details_title;
        //        封面图
        @BindView(R.id.img_article_details_bg)
        ImageView img_article_details_bg;
        //        发布时间
        @BindView(R.id.tv_article_details_time)
        TextView tv_article_details_time;
    }

}
