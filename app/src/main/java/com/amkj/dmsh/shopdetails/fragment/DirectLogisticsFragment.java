package com.amkj.dmsh.shopdetails.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.TabNameBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.adapter.QualityCustomAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.ExpressAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticPacketBean;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean.LogisticsDetailsBean.LogisticsBean.LogisticTextBean;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantVariable.REQUEST_NOTIFICATION_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_EXPRESS_DATA;


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
    @BindView(R.id.ll_notify)
    LinearLayout mLlNotify;
    @BindView(R.id.tv_open_notify)
    TextView mTvOpenNotify;
    private DirectLogisticPacketBean directLogisticPacketBean;
    private CBViewHolderCreator cbViewHolderCreator;
    private String[] titles = {"专区1", "专区2", "专区3", "专区4"};
    private List<LogisticTextBean> mLogisticPacketList = new ArrayList<>();
    private QualityCustomAdapter qualityCustomAdapter;
    private ExpressAdapter mExpressAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_express_package;
    }

    @Override
    protected void initViews() {
        mSmartScrollCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            EventBus.getDefault().post(new EventMessage(UPDATE_EXPRESS_DATA, 0));
        });

        //判断通知是否打开
        mLlNotify.setVisibility(!getDeviceAppNotificationStatus() ? View.VISIBLE : View.GONE);
        String string = getString(R.string.receive_express_msg);
        mTvOpenNotify.setText(ConstantMethod.getSpannableString(string,16,string.length(),-1,"#0a88fa"));
    }

    @Override
    protected void loadData() {
        mRvExpress.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    mRvExpress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mViewLine.setVisibility(View.VISIBLE);
                    int measuredHeight = mRvExpress.getMeasuredHeight();
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mViewLine.getLayoutParams();
                    int lastMargin = mExpressAdapter.getViewByPosition(mRvExpress, mExpressAdapter.getData().size() - 1, R.id.ll_express).getMeasuredHeight() / 2;
                    int firstMargin = mExpressAdapter.getViewByPosition(mRvExpress, 0, R.id.ll_express).getMeasuredHeight() / 2;
                    layoutParams.setMargins(AutoSizeUtils.mm2px(getActivity(), 28), firstMargin, 0, lastMargin);
                    layoutParams.height = measuredHeight - lastMargin - firstMargin;
                    mViewLine.setLayoutParams(layoutParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    mViewLine.setVisibility(View.GONE);
                }
            }
        });

        setData();
    }


    private void setData() {
        try {
            //初始化商品轮播图
            List<LogisticsProductPacketBean> directGoods = directLogisticPacketBean.getDirectGoods();
            List<CommunalADActivityBean> adList = new ArrayList<>();
            for (LogisticsProductPacketBean packetBean : directGoods) {
                CommunalADActivityBean adActivityBean = new CommunalADActivityBean();
                adActivityBean.setAndroidLink(!packetBean.isPresent() ? "app://ShopScrollDetailsActivity?productId=" + packetBean.getId() : "");
                adActivityBean.setPicUrl(packetBean.getPicUrl());
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
            mCbBanner.setPages(getActivity(), cbViewHolderCreator, adList).setCanLoop(true)
                    .setPointViewVisible(false)
                    .startTurning(1000);

            //初始化物流
            mLogisticPacketList.clear();
            mLogisticPacketList.add(new LogisticTextBean("", "收货地址:" + directLogisticPacketBean.getAddress()));
            mLogisticPacketList.addAll(directLogisticPacketBean.getLogisticPacketList());
            mTvExpressName.setText(directLogisticPacketBean.getExpressCompany());
            mTvExpressNum.setText(("物流单号：" + directLogisticPacketBean.getExpressNo()));
            mRvExpress.setLayoutManager(new LinearLayoutManager(getActivity()));
            mExpressAdapter = new ExpressAdapter(getActivity(), mLogisticPacketList);
            mRvExpress.setAdapter(mExpressAdapter);
            mViewLine.setVisibility(mLogisticPacketList.size() > 1 ? View.VISIBLE : View.GONE);

            //初始化自定义专区
            List<String> mProductTypeList = new ArrayList<>();
            mProductTypeList.add("140");
            mProductTypeList.add("137");
            mProductTypeList.add("138");
            mProductTypeList.add("139");
            qualityCustomAdapter = new QualityCustomAdapter(getChildFragmentManager(), mProductTypeList, getSimpleName());
            mVpCustom.setAdapter(qualityCustomAdapter);
            mVpCustom.setOffscreenPageLimit(titles.length - 1);
            mVpCustom.setCurrentItem(0);
            mSlidingTablayout.setViewPager(mVpCustom, titles);
            mSlidingTablayout.setCurrentTab(0);
            mSmartScrollCommunalRefresh.finishRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (directLogisticPacketBean != null) {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        } else {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        directLogisticPacketBean = bundle.getParcelable("DirectLogisticPacketBean");
    }

    @OnClick({R.id.tv_copy, R.id.tv_qy_service, R.id.tv_open_notify, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_copy:
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", directLogisticPacketBean.getExpressNo());
                cm.setPrimaryClip(mClipData);
                ConstantMethod.showToast(getActivity(), "已成功复制到剪切板");
                break;
            case R.id.tv_qy_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(), "物流详情");
                break;
            case R.id.tv_open_notify:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_NOTIFICATION_STATUS);
                break;
            case R.id.iv_close:
                mLlNotify.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLlNotify.setVisibility(!getDeviceAppNotificationStatus() ? View.VISIBLE : View.GONE);
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
