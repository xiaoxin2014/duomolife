package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.MainActivity;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.HomeQualityFloatAdEntity;
import com.amkj.dmsh.dao.AddClickDao;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.integration.IntegExchangeDetailActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.views.alertdialog.AlertDialogImage;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_INTEGRAL_PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.MAIN_QUALITY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_PAY_SUCCESS_AD_DIALOG;
import static com.amkj.dmsh.constant.Url.Q_PAY_SUCCESS_PRODUCT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/31
 * class description:??????????????????
 */
public class DirectPaySuccessActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ???????????????
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int scrollY;
    private float screenHeight;
    private List<LikedProductBean> typeDetails = new ArrayList();
    private String indentNo;
    private String indentProductType;
    private GoodProductAdapter qualityTypeProductAdapter;
    private AlertDialogImage alertDialogAdImage;
    private UserLikedProductEntity likedProductEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        getLoginStatus(DirectPaySuccessActivity.this);
        tv_header_titleAll.setText("????????????");
        tv_header_shared.setVisibility(GONE);
        tl_normal_bar.setSelected(true);
        Intent intent = getIntent();
        indentNo = intent.getStringExtra("indentNo");
        indentProductType = intent.getStringExtra(INDENT_PRODUCT_TYPE);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> getRecommendProductData());
        communal_recycler.setLayoutManager(new GridLayoutManager(DirectPaySuccessActivity.this, 2));
        View headerView = LayoutInflater.from(DirectPaySuccessActivity.this)
                .inflate(R.layout.layout_pay_success_header, (ViewGroup) communal_recycler.getParent(), false);
        WelfareHeaderView welfareHeaderView = new WelfareHeaderView();
        ButterKnife.bind(welfareHeaderView, headerView);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        qualityTypeProductAdapter = new GoodProductAdapter(DirectPaySuccessActivity.this, typeDetails);
        qualityTypeProductAdapter.addHeaderView(headerView);
        communal_recycler.setVerticalScrollBarEnabled(false);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setEnableLoadMore(false);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show();
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
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
    }

    @Override
    protected void loadData() {
        getRecommendProductData();
        getPaySucAd();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getPaySucAd() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_PAY_SUCCESS_AD_DIALOG, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                HomeQualityFloatAdEntity floatAdEntity = GsonUtils.fromJson(result, HomeQualityFloatAdEntity.class);
                if (floatAdEntity != null) {
                    if (floatAdEntity.getCode().equals(SUCCESS_CODE)) {
                        CommunalADActivityBean communalADActivityBean = floatAdEntity.getCommunalADActivityBean();
                        if (communalADActivityBean != null) {
                            setPaySuccessDialog(communalADActivityBean);
                        }
                    }
                }
            }
        });
    }

    private void setPaySuccessDialog(CommunalADActivityBean communalADActivityBean) {
        GlideImageLoaderUtil.setLoadImgFinishListener(DirectPaySuccessActivity.this, communalADActivityBean.getPicUrl(), new GlideImageLoaderUtil.ImageLoaderFinishListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                if (alertDialogAdImage == null) {
                    alertDialogAdImage = new AlertDialogImage(DirectPaySuccessActivity.this);
                }
                alertDialogAdImage.show();
                alertDialogAdImage.setAlertClickListener(new AlertDialogImage.AlertImageClickListener() {
                    @Override
                    public void imageClick() {
                        AddClickDao.adClickTotal(getActivity(),communalADActivityBean.getAndroidLink(), communalADActivityBean.getId(),false);
                    }
                });
                alertDialogAdImage.setImage(bitmap);
            }

            @Override
            public void onError() {

            }
        });
    }

    //    ??????????????????
    private void getRecommendProductData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("order_no", indentNo);
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_PAY_SUCCESS_PRODUCT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                typeDetails.clear();

                likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                if (likedProductEntity != null) {
                    if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        typeDetails.addAll(likedProductEntity.getGoodsList());
                    } else {
                        showToast( likedProductEntity.getMsg());
                    }
                }
                qualityTypeProductAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, typeDetails, likedProductEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, typeDetails, likedProductEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast( R.string.invalidData);
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

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    class WelfareHeaderView {
        @BindView(R.id.tv_pro_title)
        TextView tv_pro_title;

        //        ????????????
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

        //        ??????????????????
        @OnClick(R.id.tv_pay_suc_quality)
        void skipQuality(View view) {
            Intent intent = new Intent(DirectPaySuccessActivity.this, MainActivity.class);
            intent.putExtra("isSkipPage", "1");
            intent.putExtra("indexShowLight", "1");
            intent.putExtra("type", MAIN_QUALITY);
            startActivity(intent);
            finish();
        }

        public void initViews() {
            tv_pro_title.setText("- ???????????? -");
        }
    }
}
