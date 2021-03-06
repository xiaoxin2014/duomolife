package com.amkj.dmsh.homepage.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.RollMsgIdDataSave;
import com.amkj.dmsh.homepage.activity.AllSearchDetailsNewActivity;
import com.amkj.dmsh.homepage.adapter.HomePageNewAdapter;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.homepage.bean.MarqueeTextEntity;
import com.amkj.dmsh.homepage.bean.MarqueeTextEntity.MarqueeTextBean;
import com.amkj.dmsh.message.activity.MessageActivity;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingIconTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import li.yz.simplemarqueeviewlib.SimpleMarqueeView;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatAd;
import static com.amkj.dmsh.constant.ConstantMethod.getMessageCount;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REFRESH_MESSAGE_TOTAL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_ALL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GTE_HOME_NAVBAR;
import static com.amkj.dmsh.constant.Url.H_Q_MARQUEE_AD;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :????????????
 */
public class HomePageFragment extends BaseFragment {
    @BindView(R.id.iv_message)
    ImageView mIvMessage;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.iv_home_shop_car)
    ImageView mIvHomeShopCar;
    @BindView(R.id.tablayout_home)
    SlidingIconTabLayout mTablayoutHome;
    @BindView(R.id.vp_home)
    ViewPager mVpHome;
    @BindView(R.id.fl_fragment_quality)
    FrameLayout mFlFragmentQuality;
    @BindView(R.id.tb_home_new)
    LinearLayout mTbHomeNew;
    @BindView(R.id.fra_home_message)
    FrameLayout mFraHomeMessage;
    @BindView(R.id.fl_shop_car)
    FrameLayout mFlShopCar;
    @BindView(R.id.iv_float_ad_icon)
    ImageView iv_float_ad_icon;
    //    ???????????????
    @BindView(R.id.ll_home_marquee)
    LinearLayout ll_home_marquee;
    @BindView(R.id.tv_marquee_text)
    SimpleMarqueeView mTvMarqueeText;
    private Badge badgeCart;
    private Badge badgeMsg;
    private List<HomeCommonBean> mGoodsNavbarList = new ArrayList<>();
    private HomeCommonEntity mHomeNavbarEntity;
    private boolean isAutoClose;
    private boolean isFirst = true;


    @Override
    protected int getContentView() {
        return R.layout.fragment_home_page_new;
    }

    @Override
    protected void initViews() {
        badgeMsg = getTopBadge(getActivity(), mFraHomeMessage);
        badgeCart = getTopBadge(getActivity(), mFlShopCar);
        mTablayoutHome.setTextsize(AutoSizeUtils.mm2px(mAppContext, 30));
        mTablayoutHome.setTabPadding(AutoSizeUtils.mm2px(mAppContext, 1));
    }

    @Override
    protected void loadData() {
        //??????tab???
        getHomeNavbar();
        //????????????
        getFloatAd(getActivity(), iv_float_ad_icon);
        //?????????
        if (!isAutoClose) {
            getMarqueeData();
        }
        //???????????????
        getCarCount(getActivity());
        //??????????????????
        getMessageCount(getActivity(), badgeMsg);
    }


    //????????????Tab?????????
    private void getHomeNavbar() {
        Map<String, Object> map = new HashMap<>();
        map.put("source", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), GTE_HOME_NAVBAR, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                mHomeNavbarEntity = GsonUtils.fromJson(result, HomeCommonEntity.class);
                if (mHomeNavbarEntity != null) {
                    List<HomeCommonBean> goodsNavbarList = mHomeNavbarEntity.getResult();
                    String code = mHomeNavbarEntity.getCode();
                    if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mHomeNavbarEntity.getMsg());
                    } else {
                        String bgColor = mHomeNavbarEntity.getBgColor();
                        String fontColor = mHomeNavbarEntity.getFontColor();
                        //?????????????????????
                        if (!TextUtils.isEmpty(bgColor)) {
                            mTablayoutHome.setBackgroundColor(Color.parseColor(bgColor));
                            mTbHomeNew.setBackgroundColor(Color.parseColor(bgColor));
                            ImmersionBar.with(HomePageFragment.this).titleBar(mTbHomeNew).keyboardEnable(true).navigationBarEnable(false)
                                    .statusBarDarkFont(true).statusBarColor(bgColor).init();
                            mIvMessage.setSelected(true);
                            mIvHomeShopCar.setSelected(true);
                        }

                        //??????tab???????????????
                        if (!TextUtils.isEmpty(fontColor)) {
                            mTablayoutHome.setIndicatorColor(Color.parseColor(fontColor));
                            mTablayoutHome.setTextUnselectColor(Color.parseColor(fontColor));
                            mTablayoutHome.setTextSelectColor(Color.parseColor(fontColor));
                        }

                        //????????????????????????
                        if (goodsNavbarList != null && goodsNavbarList.size() > 0) {
                            mGoodsNavbarList.clear();
//                            HomeCommonBean homeCommonBean = new HomeCommonBean();
//                            homeCommonBean.setName("?????????");
//                            homeCommonBean.setLink("https://www.domolife.cn/m/template/2019Spring/home.html");
//                            homeCommonBean.setShowType("1");
//                            mGoodsNavbarList.add(homeCommonBean);
                            mGoodsNavbarList.addAll(goodsNavbarList);
                            HomePageNewAdapter homePageNewAdapter = new HomePageNewAdapter(getChildFragmentManager(), mGoodsNavbarList);
                            mVpHome.setAdapter(homePageNewAdapter);
                            mVpHome.setOffscreenPageLimit(mGoodsNavbarList.size() - 1);
                            mTablayoutHome.setViewPager(mVpHome, mGoodsNavbarList, fontColor);
                            mVpHome.setCurrentItem(0);
                        }

                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsNavbarList, mHomeNavbarEntity);

            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsNavbarList, mHomeNavbarEntity);
            }
        });
    }

    private void getMarqueeData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_Q_MARQUEE_AD, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                MarqueeTextEntity marqueeTextEntity = MarqueeTextEntity.objectFromData(result);
                if (marqueeTextEntity != null) {
                    if (marqueeTextEntity.getCode().equals(SUCCESS_CODE)) {
                        List<MarqueeTextBean> marqueeTextList = marqueeTextEntity.getMarqueeTextList();
                        if (marqueeTextList != null && marqueeTextList.size() > 0) {
                            String rollMsg = getRollMsg(getActivity(), marqueeTextList, ll_home_marquee);
                            if (!TextUtils.isEmpty(rollMsg)) {
                                ll_home_marquee.setVisibility(View.VISIBLE);
                                mTvMarqueeText.setText(rollMsg, true);
                                RollMsgIdDataSave.getSingleton().saveMsgId(marqueeTextList);
                            }
                        } else {
                            ll_home_marquee.setVisibility(View.GONE);
                        }
                    } else {
                        ll_home_marquee.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                ll_home_marquee.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.iv_message, R.id.tv_search, R.id.iv_home_shop_car, R.id.iv_home_marquee_close})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_message:
                intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_search:
                intent = new Intent(getActivity(), AllSearchDetailsNewActivity.class);
                intent.putExtra(SEARCH_TYPE, SEARCH_ALL);
                startActivity(intent);
                break;
            case R.id.iv_home_shop_car:
                intent = new Intent(getActivity(), ShopCarActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_home_marquee_close:
                isAutoClose = true;
                ll_home_marquee.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).titleBar(mTbHomeNew).keyboardEnable(true).navigationBarEnable(false)
                .statusBarDarkFont(true).init();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected boolean isLazy() {
        return false;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badgeCart != null) {
                badgeCart.setBadgeNumber((int) message.result);
            }
        } else if (message.type.equals(REFRESH_MESSAGE_TOTAL)) {
            getMessageCount(getActivity(), badgeMsg);
        }
    }

    //????????????????????????????????????????????????
    private String getRollMsg(Context context, List<MarqueeTextBean> marqueeTextList, ViewGroup viewGroup) {
        StringBuffer msg = new StringBuffer();
        try {
            int totalBlankLength = 0;
            for (MarqueeTextBean marqueeTextBean : marqueeTextList) {
                int displayType = marqueeTextBean.getDisplayType();
                int id = marqueeTextBean.getId();
                //?????????1????????????????????????0??????????????????????????????
                if (displayType == 0 || !RollMsgIdDataSave.getSingleton().containId(id)) {
                    String content = marqueeTextBean.getContent();
                    int show_count = marqueeTextBean.getShow_count();
                    for (int j = 0; j < show_count; j++) {
                        msg.append(content);
                        int blankLength = 30 - content.length();
                        for (int k = 0; k < (blankLength < 10 ? 10 : blankLength); k++) {
                            msg.append("   ");
                            totalBlankLength += 3;
                        }
                    }
                }
            }
            //??????????????????????????????????????????????????????????????????
            long totalTime = 300 * (int) ((msg.length() - totalBlankLength) + totalBlankLength * 1.0f / 3);
            new LifecycleHandler(context).postDelayed(() -> viewGroup.setVisibility(View.GONE), totalTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg.toString();
    }

    @Override
    public void onVisible() {
        super.onVisible();
        if (!isFirst) {
            getMessageCount(getActivity(), badgeMsg);
        }
        isFirst = false;
    }

    public String getFragmentName() {
        try {
            int currentTab = mTablayoutHome.getCurrentTab();
            PagerAdapter adapter = mTablayoutHome.getViewPager().getAdapter();
            if (adapter instanceof HomePageNewAdapter) {
                Fragment item = ((HomePageNewAdapter) adapter).getItem(currentTab);
                return item.getClass().getSimpleName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
