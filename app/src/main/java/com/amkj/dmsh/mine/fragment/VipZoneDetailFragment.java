package com.amkj.dmsh.mine.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.bean.WeekProductEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

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
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView mCommunalRecyclerWrap;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mDownloadBtnCommunal;
    @BindView(R.id.nested_scrollview)
    NestedScrollView mNestedScrollview;
    private List<LikedProductBean> mGoodsList = new ArrayList<>();
    private WeekProductEntity mVipProductEntity;
    private GoodProductAdapter mGoodProductAdapter;
    private CommunalDetailAdapter communalDetailAdapter;
    private List<CommunalDetailObjectBean> descriptionList = new ArrayList<>();
    private String mZoneId;
    private WeekProductEntity.WeekProductBean mWeekProductBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_week_product_list;
    }

    @Override
    protected void initViews() {
        mTlNormalBar.setVisibility(GONE);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
        //记录埋点参数sourceId
        ConstantMethod.saveSourceId(getSimpleName(), mZoneId);
        //一键回到顶部
        int screenHeight = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenHeight();
        mNestedScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, newY, i2, oldY) -> {
            if (newY > screenHeight * 1.5) {
                if (mDownloadBtnCommunal.getVisibility() == GONE) {
                    mDownloadBtnCommunal.setVisibility(VISIBLE);
                    mDownloadBtnCommunal.show(false);
                }
                if (!mDownloadBtnCommunal.isVisible()) {
                    mDownloadBtnCommunal.show(false);
                }
            } else {
                if (mDownloadBtnCommunal.isVisible()) {
                    mDownloadBtnCommunal.hide(false);
                }
            }
        });
        mDownloadBtnCommunal.setOnClickListener(v -> {
            mNestedScrollview.fling(0);
            mNestedScrollview.scrollTo(0, 0);
            mDownloadBtnCommunal.hide(false);
        });

        //初始化富文本
        mCommunalRecyclerWrap.setNestedScrollingEnabled(false);
        mCommunalRecyclerWrap.setLayoutManager(new LinearLayoutManager((getActivity())));
        communalDetailAdapter = new CommunalDetailAdapter(getActivity(), descriptionList);
        // TODO: 2020/8/26 补充h5链接
        communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShareDataBean shareDataBean = null;
                if (view.getId() == R.id.tv_communal_share && mWeekProductBean != null) {
                    shareDataBean = new ShareDataBean(mWeekProductBean.getCoverImg()
                            , !TextUtils.isEmpty(mWeekProductBean.getTitle()) ? mWeekProductBean.getTitle() : "会员日"
                            , "我在多么生活发现这几样好物，性价比不错，还包邮"
                            , Url.BASE_SHARE_PAGE_TWO + "goods/CustomZone.html?id=" + mZoneId);

                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(getActivity(), shareDataBean, view, loadHud);
            }
        });
        mCommunalRecyclerWrap.setAdapter(communalDetailAdapter);

        //初始化商品列表
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRvGoods.setLayoutManager(mGridLayoutManager);
        mGoodProductAdapter = new GoodProductAdapter(getActivity(), mGoodsList);
        mRvGoods.setAdapter(mGoodProductAdapter);
        mRvGoods.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
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
                        if (!TextUtils.isEmpty(mWeekProductBean.getCoverImg())) {
                            mIvCover.setVisibility(VISIBLE);
                            GlideImageLoaderUtil.loadImage(getActivity(), mIvCover, mWeekProductBean.getCoverImg());
                        }
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
                mGoodProductAdapter.notifyDataSetChanged();
                communalDetailAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList.size() > 0 || descriptionList.size() > 0, mVipProductEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList.size() > 0 || descriptionList.size() > 0, mVipProductEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            mZoneId = (String) bundle.get("zoneId");
        }
    }
}
