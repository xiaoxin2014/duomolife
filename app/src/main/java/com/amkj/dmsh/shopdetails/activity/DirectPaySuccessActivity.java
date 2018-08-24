package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.bean.HomeQualityFloatAdEntity;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.integration.IntegExchangeDetailActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_INTEGRAL_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_PAY_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_TYPE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/31
 * class description:支付完成页面
 */
public class DirectPaySuccessActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
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
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int scrollY;
    private float screenHeight;
    private int uid;
    private List<LikedProductBean> typeDetails = new ArrayList();
    private String indentNo;
    private String indentProductType;
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private AlertDialogImage alertDialogAdImage;
    private AlertDialog alertImageAdDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        tv_header_titleAll.setText("支付完成");
        tv_header_shared.setVisibility(GONE);
        tl_normal_bar.setSelected(true);
        Intent intent = getIntent();
        indentNo = intent.getStringExtra("indentNo");
        indentProductType = intent.getStringExtra("INDENT_PRODUCT_TYPE");

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            getRecommendProductData();
        });
        communal_recycler.setLayoutManager(new GridLayoutManager(DirectPaySuccessActivity.this, 2));
        View headerView = LayoutInflater.from(DirectPaySuccessActivity.this)
                .inflate(R.layout.layout_pay_success_header, (ViewGroup) communal_recycler.getParent(), false);
        WelfareHeaderView welfareHeaderView = new WelfareHeaderView();
        ButterKnife.bind(welfareHeaderView, headerView);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        qualityTypeProductAdapter = new QualityTypeProductAdapter(DirectPaySuccessActivity.this, typeDetails);
        qualityTypeProductAdapter.addHeaderView(headerView);
        communal_recycler.setVerticalScrollBarEnabled(false);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setEnableLoadMore(false);
        qualityTypeProductAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            loadHud.show();
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                if (userId > 0) {
                    switch (view.getId()) {
                        case R.id.iv_pro_add_car:
                            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                            baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                            baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                            baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                            baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                            ConstantMethod constantMethod = new ConstantMethod();
                            constantMethod.addShopCarGetSku(DirectPaySuccessActivity.this, baseAddCarProInfoBean, loadHud);
                            break;
                    }
                } else {
                    loadHud.dismiss();
                    getLoginStatus();
                }
            }
        });
        qualityTypeProductAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                Intent bundle = new Intent(DirectPaySuccessActivity.this, ShopScrollDetailsActivity.class);
                bundle.putExtra("productId", String.valueOf(likedProductBean.getId()));
                bundle.putExtra(RECOMMEND_TYPE, RECOMMEND_PAY_SUCCESS);
                startActivity(bundle);
            }
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
                    download_btn_communal.setVisibility(GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                    - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (firstVisibleItemPosition > mVisibleCount) {
                communal_recycler.scrollToPosition(mVisibleCount);
            }
            communal_recycler.smoothScrollToPosition(0);
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        getRecommendProductData();
        getPaySucAd();
    }

    private void getPaySucAd() {
        if (NetWorkUtils.checkNet(DirectPaySuccessActivity.this)) {
            String url = Url.BASE_URL + Url.Q_PAY_SUCCESS_AD_DIALOG;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    HomeQualityFloatAdEntity floatAdEntity = gson.fromJson(result, HomeQualityFloatAdEntity.class);
                    if (floatAdEntity != null) {
                        if (floatAdEntity.getCode().equals("01")) {
                            CommunalADActivityBean communalADActivityBean = floatAdEntity.getCommunalADActivityBean();
                            if (communalADActivityBean != null) {
                                GlideImageLoaderUtil.loadFinishImgDrawable(DirectPaySuccessActivity.this, communalADActivityBean.getPicUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                                    @Override
                                    public void onSuccess(Bitmap bitmap) {
                                        if (alertImageAdDialog == null) {
                                            alertDialogAdImage = new AlertDialogImage();
                                            alertImageAdDialog = alertDialogAdImage.createAlertDialog(DirectPaySuccessActivity.this);
                                        }
                                        alertImageAdDialog.show();
                                        alertDialogAdImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                                            @Override
                                            public void imageClick() {
                                                adClickTotal(communalADActivityBean.getObjectId());
                                                setSkipPath(DirectPaySuccessActivity.this, communalADActivityBean.getAndroidLink(), false);
                                                alertImageAdDialog.dismiss();
                                            }
                                        });
                                        alertDialogAdImage.setImage(bitmap);
                                    }

                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onError(Drawable errorDrawable) {
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

    //    推荐商品列表
    private void getRecommendProductData() {
        String url = Url.BASE_URL + Url.Q_PAY_SUCCESS_PRODUCT;
        if (NetWorkUtils.checkNet(DirectPaySuccessActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("order_no", indentNo);
            params.put("uid", uid);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    communal_empty.setVisibility(View.GONE);
                    typeDetails.clear();
                    Gson gson = new Gson();
                    UserLikedProductEntity likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProductEntity != null) {
                        if (likedProductEntity.getCode().equals("01")) {
                            typeDetails.addAll(likedProductEntity.getLikedProductBeanList());
                        } else {
                            showToast(DirectPaySuccessActivity.this, likedProductEntity.getMsg());
                        }
                    }
                    qualityTypeProductAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.VISIBLE);
                    showToast(DirectPaySuccessActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            qualityTypeProductAdapter.loadMoreComplete();
            communal_load.setVisibility(View.GONE);
            communal_empty.setVisibility(View.GONE);
            communal_error.setVisibility(View.VISIBLE);
            showToast(DirectPaySuccessActivity.this, R.string.unConnectedNetwork);
        }
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
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
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(GONE);
        communal_error.setVisibility(GONE);
        loadData();
    }

    class WelfareHeaderView {
        @BindView(R.id.tv_pro_title)
        TextView tv_pro_title;

        //        查看订单
        @OnClick(R.id.tv_pay_suc_indent)
        void skipIndent(View view) {
            if (!TextUtils.isEmpty(indentNo)) {
                Intent intent = new Intent();
                if (INDENT_INTEGRAL_PRODUCT.equals(indentProductType)) {
                    intent.setClass(DirectPaySuccessActivity.this, IntegExchangeDetailActivity.class);
                } else {
                    intent.setClass(DirectPaySuccessActivity.this, DirectExchangeDetailsActivity.class);
                }
                intent.putExtra("orderNo", indentNo);
                startActivity(intent);
                finish();
            }
        }

        //        跳转良品首页
        @OnClick(R.id.tv_pay_suc_quality)
        void skipQuality(View view) {
            Intent intent = new Intent(DirectPaySuccessActivity.this, MainActivity.class);
            intent.putExtra("isSkipPage", "1");
            intent.putExtra("indexShowLight", "1");
            intent.putExtra("type", "quality");
            startActivity(intent);
            finish();
        }

        public void initViews() {
            tv_pro_title.setText("- 商品推荐 -");
        }
    }
}
