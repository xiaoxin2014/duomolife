package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.LogisticsPagerAdapter;
import com.amkj.dmsh.shopdetails.bean.LogisticsNewEntity;
import com.amkj.dmsh.shopdetails.bean.LogisticsNewEntity.PackageInfoBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/3/27
 * Version:v4.5.0
 * ClassDescription :新版物流详情
 */
public class DirectLogisticsDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.stl_direct_logistics_details)
    SlidingTabLayout stl_direct_logistics_details;
    @BindView(R.id.vp_direct_logistics_details)
    ViewPager vp_direct_logistics_details;
    @BindView(R.id.ll_detail)
    LinearLayout ll_detail;
    private String orderNo;
    private String refundNo;
    private String zeroOrderNo;
    private List<String> pageTitle = new ArrayList<>();
    private LogisticsNewEntity mLogisticsNewEntity;
    List<PackageInfoBean> mPackageInfoList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_logistics_details;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("物流详情");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        refundNo = intent.getStringExtra("refundNo");
        zeroOrderNo = intent.getStringExtra("zeroOrderNo");
        stl_direct_logistics_details.setTextsize(AutoSizeUtils.mm2px(mAppContext, 28));
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return ll_detail;
    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(refundNo)) {//退款物流
            PackageInfoBean packageInfoBean = new PackageInfoBean();
            packageInfoBean.setRefundNo(refundNo);
            mPackageInfoList.add(packageInfoBean);
            setData();
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        } else if (!TextUtils.isEmpty(orderNo)) {//普通物流
            getOrderExpressNo();
        } else if (!TextUtils.isEmpty(zeroOrderNo)) {//0元试用订单物流
            PackageInfoBean packageInfoBean = new PackageInfoBean();
            packageInfoBean.setZeroOrderNo(zeroOrderNo);
            mPackageInfoList.add(packageInfoBean);
            setData();
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        }
    }

    //获取该订单下所有的物流单号
    private void getOrderExpressNo() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(DirectLogisticsDetailsActivity.this, Url.Q_ORDER_LOGISTICS_PACKAGE,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        mPackageInfoList.clear();
                        pageTitle.clear();
                        mLogisticsNewEntity = GsonUtils.fromJson(result, LogisticsNewEntity.class);
                        if (mLogisticsNewEntity != null) {
                            if (mLogisticsNewEntity.getCode().equals(SUCCESS_CODE)) {
                                List<LogisticsNewEntity.PackageInfoBean> packageInfoList = mLogisticsNewEntity.getPackageInfoList();
                                if (packageInfoList != null && packageInfoList.size() > 0) {
                                    mPackageInfoList.addAll(packageInfoList);
                                    setData();
                                }
                            } else if (!mLogisticsNewEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(mLogisticsNewEntity.getMsg());
                            }
                        }

                        stl_direct_logistics_details.setVisibility(mPackageInfoList.size() > 1 ? View.VISIBLE : View.GONE);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mPackageInfoList, mLogisticsNewEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, mPackageInfoList, mLogisticsNewEntity);
                    }
                });
    }

    private void setData() {
        for (int i = 0; i < mPackageInfoList.size(); i++) {
            pageTitle.add("包裹" + (i + 1));
        }

        LogisticsPagerAdapter logisticsPagerAdapter = new LogisticsPagerAdapter(getSupportFragmentManager(), pageTitle, mPackageInfoList);
        vp_direct_logistics_details.setAdapter(logisticsPagerAdapter);
        vp_direct_logistics_details.setOffscreenPageLimit(pageTitle.size() - 1);
        stl_direct_logistics_details.setViewPager(vp_direct_logistics_details);

        if (mPackageInfoList.size() <= 3)
            stl_direct_logistics_details.setTabWidth(AutoSizeUtils.mm2px(mAppContext, 250));
        else {
            stl_direct_logistics_details.setTabWidth(AutoSizeUtils.mm2px(mAppContext, 196));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) stl_direct_logistics_details.getLayoutParams();
            layoutParams.leftMargin = AutoSizeUtils.mm2px(mAppContext, 25);
            stl_direct_logistics_details.setLayoutParams(layoutParams);
        }
    }


    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
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
            NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
            loadData();
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (ConstantVariable.UPDATE_EXPRESS_DATA.equals(message.type)) {
            loadData();
        }
    }
}
