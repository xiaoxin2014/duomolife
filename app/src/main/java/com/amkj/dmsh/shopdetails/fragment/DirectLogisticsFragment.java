package com.amkj.dmsh.shopdetails.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.LogisticTextBean;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.bean.TabNameBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.QualityCustomAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.ExpressAdapter;
import com.amkj.dmsh.shopdetails.bean.LogisticsNewEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/23
 * class description:订单物流查询
 */
public class DirectLogisticsFragment extends BaseFragment {
    @BindView(R.id.cb_banner)
    ConvenientBanner mCbBanner;
    @BindView(R.id.tv_express_name)
    TextView mTvExpressName;
    @BindView(R.id.tv_express_num)
    TextView mTvExpressNum;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.rv_express)
    RecyclerView mRvExpress;
    @BindView(R.id.rl_express)
    RelativeLayout mRlExpress;
    @BindView(R.id.tv_qy_service)
    TextView mTvQyService;
    @BindView(R.id.home_title_collapsing_view)
    CollapsingToolbarLayout mHomeTitleCollapsingView;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mSlidingTablayout;
    @BindView(R.id.ab_find_layout)
    AppBarLayout mAbFindLayout;
    @BindView(R.id.vp_custom)
    ViewPager mVpCustom;
    @BindView(R.id.smart_scroll_communal_refresh)
    SmartRefreshLayout mSmartScrollCommunalRefresh;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tv_change)
    TextView mTvChange;
    @BindView(R.id.ll_express_info)
    LinearLayout mLlExpressInfo;
    private LogisticsNewEntity.LogisticsDetailBean mLogisticsDetailBean;
    private CBViewHolderCreator cbViewHolderCreator;
    private String[] titles = {"专区1", "专区2", "专区3", "专区4"};
    private String[] CUSTOM_IDS = new String[]{"405", "406", "407", "408"};
    private List<LogisticTextBean> mLogisticPacketList = new ArrayList<>();
    private QualityCustomAdapter qualityCustomAdapter;
    private ExpressAdapter mExpressAdapter;
    private boolean expand;
    private LogisticsNewEntity mLogisticsNewEntity;
    private String orderNo;
    private String expressNo;
    private String refundNo;

    @Override
    protected int getContentView() {
        return R.layout.fragment_express_package;
    }

    @Override
    protected void initViews() {
        mSmartScrollCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            getExressInfo();
        });
    }

    @Override
    protected void loadData() {
        mRvExpress.setLayoutManager(new LinearLayoutManager(getActivity()));
        mExpressAdapter = new ExpressAdapter(getActivity(), mLogisticPacketList);
        mRvExpress.setAdapter(mExpressAdapter);
        getExressInfo();
    }

    //动态修改物流线的高度
    private void setLineHeight() {
        mRvExpress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRvExpress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (mLogisticPacketList.size() > 1) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewLine.getLayoutParams();
                    try {
                        int measuredHeight = mRvExpress.getMeasuredHeight();
                        int firstHeight = mRvExpress.getChildAt(0).getMeasuredHeight() / 2 + 10;
                        int lastHeight = mRvExpress.getChildAt(mRvExpress.getChildCount() - 1).getMeasuredHeight() / 2;
                        layoutParams.setMargins(AutoSizeUtils.mm2px(getActivity(), 28), firstHeight, 0, lastHeight);
                        layoutParams.height = measuredHeight - firstHeight - lastHeight;
                    } catch (Exception e) {
                        layoutParams.height = 0;
                    }
                    mViewLine.setLayoutParams(layoutParams);
                }
            }
        });
    }

    //获取物流信息
    private void getExressInfo() {
        Map<String, Object> params = new HashMap<>();
        //判断是退款物流还是普通物流
        if (!TextUtils.isEmpty(refundNo)) {
            params.put("refundNo", refundNo);
        } else {
            params.put("orderNo", orderNo);
            params.put("expressNo", expressNo);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), !TextUtils.isEmpty(refundNo) ? Url.Q_REFUND_LOGISTICS_DETAIL : Url.Q_LOGISTICS_DETAIL,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        mSmartScrollCommunalRefresh.finishRefresh();

                        mLogisticsNewEntity = GsonUtils.fromJson(result, LogisticsNewEntity.class);
                        if (mLogisticsNewEntity != null) {
                            if (mLogisticsNewEntity.getCode().equals(SUCCESS_CODE)) {
                                LogisticsNewEntity.LogisticsDetailBean logisticsDetail = mLogisticsNewEntity.getLogisticsDetail();
                                if (logisticsDetail != null) {
                                    mLogisticsDetailBean = logisticsDetail;
                                    setExpressData();
                                }
                            } else {
                                showToast(mLogisticsNewEntity.getMsg());
                            }
                        }

                        showLoadService();
                    }

                    @Override
                    public void onNotNetOrException() {
                        mSmartScrollCommunalRefresh.finishRefresh();
                        showLoadService();
                    }
                });
    }

    private void showLoadService() {
        if (mLogisticsDetailBean != null) {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        } else {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
        }
    }

    private void setExpressData() {
        try {
            List<OrderProductNewBean> directGoods = new ArrayList<>();
            //初始化商品轮播图
            if (mLogisticsNewEntity.getProductInfo() != null) {
                directGoods.add(mLogisticsNewEntity.getProductInfo());
            } else {
                directGoods.addAll(mLogisticsNewEntity.getProductInfoList());
            }
            List<CommunalADActivityBean> adList = new ArrayList<>();
            for (OrderProductNewBean orderProductNewBean : directGoods) {
                CommunalADActivityBean adActivityBean = new CommunalADActivityBean();
                adActivityBean.setAndroidLink(orderProductNewBean.getProductId() > 0 ? "app://ShopScrollDetailsActivity?productId=" + orderProductNewBean.getProductId() : "");
                adActivityBean.setPicUrl(orderProductNewBean.getPicUrl());
                adList.add(adActivityBean);
            }
            if (cbViewHolderCreator == null) {
                cbViewHolderCreator = new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {
                        return new CommunalAdHolderView(itemView, getActivity(), mCbBanner, true);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.layout_ad_image_video;
                    }
                };
            }
            mCbBanner.setPages(getActivity(), cbViewHolderCreator, adList)
                    .startTurning(3000);

            //初始化物流
            expand = false;
            mTvChange.setSelected(false);
            mTvChange.setText("展开");
            List<LogisticTextBean> logistis = mLogisticsDetailBean.getList();
            mLlExpressInfo.setVisibility(!TextUtils.isEmpty(mLogisticsDetailBean.getNumber()) ? View.VISIBLE : View.GONE);
            mTvExpressName.setText(getStrings(mLogisticsDetailBean.getCompanyName()));
            mTvExpressNum.setText(("物流单号：" + mLogisticsDetailBean.getNumber()));
            mLogisticPacketList.clear();
            mLogisticPacketList.add(new LogisticTextBean("", "收货地址:" + mLogisticsNewEntity.getAddress()));
            if (logistis != null && logistis.size() > 0) {
                mLogisticPacketList.addAll(logistis);
                mTvChange.setVisibility(logistis.size() > 2 ? View.VISIBLE : View.GONE);
                mTvChange.setOnClickListener(v -> {
                    expand = !expand;
                    mTvChange.setSelected(expand);
                    mTvChange.setText(expand ? "收起" : "展开");
                    mExpressAdapter.setNewData(expand ? mLogisticPacketList : mLogisticPacketList.subList(0, 2));
                    setLineHeight();
                });
            }
            mExpressAdapter.setNewData(mLogisticPacketList.subList(0, Math.min(mLogisticPacketList.size(), 2)));
            setLineHeight();

            mViewLine.setVisibility(mLogisticPacketList.size() > 1 ? View.VISIBLE : View.GONE);
            mRlExpress.setVisibility(mLogisticPacketList.size() > 1 ? View.VISIBLE : View.GONE);
            //初始化自定义专区
            qualityCustomAdapter = new QualityCustomAdapter(getChildFragmentManager(), Arrays.asList(CUSTOM_IDS), getSimpleName());
            mVpCustom.setAdapter(qualityCustomAdapter);
            mVpCustom.setOffscreenPageLimit(titles.length - 1);
            mSlidingTablayout.setViewPager(mVpCustom, titles);
            mSmartScrollCommunalRefresh.finishRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @OnClick({R.id.tv_copy, R.id.ll_qy_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_copy:
                if (mLogisticsDetailBean != null) {
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", mLogisticsDetailBean.getNumber());
                    cm.setPrimaryClip(mClipData);
                    showToast("物流单号复制成功");
                }
                break;
            case R.id.ll_qy_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(), "物流详情");
                break;
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        orderNo = bundle.getString("orderNo");
        expressNo = bundle.getString("expressNo");
        refundNo = bundle.getString("refundNo");
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CUSTOM_NAME)) {
            try {
                if (mSlidingTablayout != null) {
                    TabNameBean tabNameBean = (TabNameBean) message.result;
                    if (getSimpleName().equals(tabNameBean.getSimpleName())) {
                        TextView titleView = mSlidingTablayout.getTitleView(tabNameBean.getPosition());
                        titleView.setText(tabNameBean.getTabName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
