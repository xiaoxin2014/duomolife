package com.amkj.dmsh.mine.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.VipSlideProAdapter;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.bean.WeekProductEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2020/8/13
 * Version:v4.7.0
 * ClassDescription :会员日/每周会员特价专区
 */
public class VipZoneDetailFragment extends BaseFragment {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.fl_header_service)
    FrameLayout mFlHeaderService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecyclerWrap;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mDownloadBtnCommunal;
    @BindView(R.id.tv_communal_pro_tag)
    TextView mTvCommunalProTag;
    @BindView(R.id.tv_communal_pro_title)
    TextView mTvCommunalProTitle;
    @BindView(R.id.rv_communal_pro)
    RecyclerView mRvCommunalPro;
    @BindView(R.id.ll_communal_pro_list)
    LinearLayout mLlCommunalProList;
    @BindView(R.id.dr_communal_pro)
    DrawerLayout mDrCommunalPro;
    private List<LikedProductBean> mGoodsList = new ArrayList<>();
    private WeekProductEntity mVipProductEntity;
    private CommunalDetailAdapter communalDetailAdapter;
    private List<CommunalDetailObjectBean> descriptionList = new ArrayList<>();
    private String mZoneId;
    private WeekProductEntity.WeekProductBean mWeekProductBean;
    private VipSlideProAdapter vipSlideProAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_draw_refresh;
    }

    @Override
    protected void initViews() {
        mIvImgShare.setVisibility(GONE);
        mFlHeaderService.setVisibility(GONE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = AutoSizeUtils.mm2px(mAppContext, 50);
        drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        try {
            drawable.setColor(0xffffffff);
            drawable.setStroke(1, 0xffcccccc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTvCommunalProTag.setBackground(drawable);

        //记录埋点参数sourceId
        ConstantMethod.saveSourceId(getClass().getSimpleName(), String.valueOf(mZoneId));
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
        setFloatingButton(mDownloadBtnCommunal, mCommunalRecyclerWrap);

        //初始化富文本
        mCommunalRecyclerWrap.setNestedScrollingEnabled(false);
        mCommunalRecyclerWrap.setLayoutManager(new LinearLayoutManager((getActivity())));
        communalDetailAdapter = new CommunalDetailAdapter(getActivity(), descriptionList);
        communalDetailAdapter.setOnItemChildClickListener((adapter, view, position) -> CommunalWebDetailUtils.getCommunalWebInstance()
                .setWebDataClick(getActivity(), null, view, loadHud));
        mCommunalRecyclerWrap.setAdapter(communalDetailAdapter);

        //初始化商品列表
        mRvCommunalPro.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCommunalPro.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        vipSlideProAdapter = new VipSlideProAdapter(getActivity(), mGoodsList);
        vipSlideProAdapter.setEnableLoadMore(false);
        mRvCommunalPro.setAdapter(vipSlideProAdapter);
        vipSlideProAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                mDrCommunalPro.closeDrawers();
                skipProductUrl(getActivity(), likedProductBean.getType_id(), likedProductBean.getId());
            }
        });
    }

    @Override
    protected void loadData() {
        getWeekGoods();
    }

    //获取每周会员特价商品
    private void getWeekGoods() {
        Map<String, Object> map = new HashMap<>();
        map.put("zoneId", mZoneId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_VIP_ZONE_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mGoodsList.clear();
                mVipProductEntity = GsonUtils.fromJson(result, WeekProductEntity.class);
                if (mVipProductEntity != null) {
                    mWeekProductBean = mVipProductEntity.getResult();
                    if (mWeekProductBean != null) {
                        mTvHeaderTitle.setText(getStrings(mWeekProductBean.getTitle()));
                        List<LikedProductBean> goodsList = mWeekProductBean.getGoodsList();
                        if (goodsList != null && goodsList.size() > 0) {
                            mGoodsList.addAll(goodsList);
                        }

                        List<CommunalDetailBean> description = mWeekProductBean.getDescription();
                        if (description != null && description.size() > 0) {
                            mCommunalRecyclerWrap.setVisibility(VISIBLE);
                            List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(description);
                            if (detailsDataList != null) {
                                descriptionList.addAll(detailsDataList);
                                communalDetailAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                vipSlideProAdapter.notifyDataSetChanged();
                communalDetailAdapter.notifyDataSetChanged();
                mTvCommunalProTag.setText(mGoodsList.size() + "商品");
                mTvCommunalProTag.setVisibility(mGoodsList.size() > 0 ? VISIBLE : GONE);
                mTvCommunalProTitle.setText(getStrings(mWeekProductBean.getTitle()));
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList.size() > 0 || descriptionList.size() > 0, mVipProductEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                mTvCommunalProTag.setVisibility(mGoodsList.size() > 0 ? VISIBLE : GONE);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList.size() > 0 || descriptionList.size() > 0, mVipProductEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    @OnClick({ R.id.tv_communal_pro_tag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_communal_pro_tag:
                if (mDrCommunalPro.isDrawerOpen(mLlCommunalProList)) {
                    mDrCommunalPro.closeDrawers();
                } else {
                    mDrCommunalPro.openDrawer(mLlCommunalProList);
                }
                break;
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            mZoneId = (String) bundle.get("zoneId");
        }
    }
}
