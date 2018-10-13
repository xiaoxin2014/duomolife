package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityHotSaleAdapter;
import com.amkj.dmsh.dominant.bean.QualityHotSaleShaftEntity;
import com.amkj.dmsh.dominant.bean.QualityHotSaleShaftEntity.HotSaleShaftBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.screenWidth;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:热销商品
 */
public class QualityTypeHotSaleProActivity extends BaseActivity {
    @BindView(R.id.smart_refresh_hot_sale)
    SmartRefreshLayout smart_refresh_hot_sale;
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.rel_communal_banner)
    RelativeLayout relCommunalBanner;
    @BindView(R.id.ad_communal_banner)
    ConvenientBanner adCommunalBanner;
    @BindView(R.id.std_dominant_hot_sale)
    SlidingTabLayout stdDominantHotSale;
    @BindView(R.id.vp_dominant_hot_sale)
    ViewPager vpDominantHotSale;
    private Badge badge;
    private QualityHotSaleShaftEntity qualityHotSaleShaftEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_dominant_hot_sale;
    }

    @Override
    protected void initViews() {
        iv_img_share.setVisibility(View.GONE);
        tv_header_titleAll.setText("热销单品");
        tl_quality_bar.setSelected(true);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        stdDominantHotSale.setTextsize(AutoSizeUtils.mm2px(mAppContext,28));
        smart_refresh_hot_sale.setOnRefreshListener((refreshLayout) -> {
            loadData();
            EventBus.getDefault().post(new EventMessage("refresh","hotSaleData"));
        });
        badge = getBadge(QualityTypeHotSaleProActivity.this, fl_header_service);
        totalPersonalTrajectory = insertNewTotalData(QualityTypeHotSaleProActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

//    @Override
//    protected View getLoadView() {
//        return smart_refresh_hot_sale;
//    }
//
//    @Override
//    protected boolean isAddLoad() {
//        return true;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void loadData() {
        getQualityProductShaft();
        getCarCount();
    }

    private void getQualityProductShaft() {
        String url = Url.BASE_URL + Url.QUALITY_HOT_SALE_SHAFT;
        NetLoadUtils.getQyInstance().loadNetDataPost(QualityTypeHotSaleProActivity.this, url
                , null, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_refresh_hot_sale.finishRefresh();
                        Gson gson = new Gson();
                        qualityHotSaleShaftEntity = gson.fromJson(result, QualityHotSaleShaftEntity.class);
                        if (qualityHotSaleShaftEntity != null) {
                            if (qualityHotSaleShaftEntity.getCode().equals(SUCCESS_CODE)) {
                                setHotSaleShaftData(qualityHotSaleShaftEntity.getHotSaleShaft());
                            } else if (!qualityHotSaleShaftEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(QualityTypeHotSaleProActivity.this, qualityHotSaleShaftEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_refresh_hot_sale.finishRefresh();
                        showToast(QualityTypeHotSaleProActivity.this, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_refresh_hot_sale.finishRefresh();
                        showToast(QualityTypeHotSaleProActivity.this, R.string.invalidData);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, qualityHotSaleShaftEntity);
                    }
                });
    }

    /**
     * 设置热销单品 时间轴
     * @param hotSaleShaft
     */
    private void setHotSaleShaftData(List<HotSaleShaftBean> hotSaleShaft) {
        int currentTab = stdDominantHotSale.getCurrentTab();
        if(hotSaleShaft!=null&&hotSaleShaft.size()>0){
            QualityHotSaleAdapter qualityHotSaleAdapter = new QualityHotSaleAdapter(getSupportFragmentManager(), hotSaleShaft);
            stdDominantHotSale.setVisibility(View.VISIBLE);
            if(hotSaleShaft.size()>3){
                stdDominantHotSale.setTabWidth((float) (screenWidth/3.5));
            }else{
                if(hotSaleShaft.size()==1){
                    stdDominantHotSale.setVisibility(View.GONE);
                }else{
                    stdDominantHotSale.setTabWidth((float) (screenWidth/hotSaleShaft.size()));
                }
            }
            vpDominantHotSale.setAdapter(qualityHotSaleAdapter);
            stdDominantHotSale.setViewPager(vpDominantHotSale);
            stdDominantHotSale.setCurrentTab(currentTab);
        }
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals("02")) {
                            showToast(QualityTypeHotSaleProActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar() {
        Intent intent = new Intent(QualityTypeHotSaleProActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
