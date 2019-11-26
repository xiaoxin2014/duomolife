package com.amkj.dmsh.homepage.fragment;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.HomeCommonBean;
import com.amkj.dmsh.homepage.bean.MarqueeTextEntity;
import com.amkj.dmsh.homepage.bean.MarqueeTextEntity.MarqueeTextBean;
import com.amkj.dmsh.message.activity.MessageActivity;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.views.MarqueeTextView;
import com.amkj.dmsh.views.flycoTablayout.SlidingIconTabLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getCarCount;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatAd;
import static com.amkj.dmsh.constant.ConstantMethod.getMessageCount;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_ALL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GTE_HOME_NAVBAR;
import static com.amkj.dmsh.constant.Url.H_Q_MARQUEE_AD;
import static com.amkj.dmsh.dao.AddClickDao.adClickTotal;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新版首页
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
    //    跑马灯布局
    @BindView(R.id.ll_home_marquee)
    LinearLayout ll_home_marquee;
    @BindView(R.id.tv_marquee_text)
    MarqueeTextView mTvMarqueeText;
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
        //获取tab栏
        getHomeNavbar();
        //浮窗广告
        getFloatAd(getActivity(), iv_float_ad_icon);
        //跑马灯
        if (!isAutoClose) {
            getMarqueeData();
        }
        //购物车数量
        getCarCount(getActivity());
        //获取消息数量
        getMessageCount(getActivity(), badgeMsg);
    }


    //获取首页Tab栏数据
    private void getHomeNavbar() {
        Map<String, Object> map = new HashMap<>();
        map.put("source", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), GTE_HOME_NAVBAR, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                mHomeNavbarEntity = gson.fromJson(result, HomeCommonEntity.class);
                if (mHomeNavbarEntity != null) {
                    List<HomeCommonBean> goodsNavbarList = mHomeNavbarEntity.getResult();
                    String code = mHomeNavbarEntity.getCode();
                    if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mHomeNavbarEntity.getMsg());
                    } else {
                        String bgColor = mHomeNavbarEntity.getBgColor();
                        String fontColor = mHomeNavbarEntity.getFontColor();
                        //设置导航栏颜色
                        if (!TextUtils.isEmpty(bgColor)) {
                            mTablayoutHome.setBackgroundColor(Color.parseColor(bgColor));
                            mTbHomeNew.setBackgroundColor(Color.parseColor(bgColor));
                            ImmersionBar.with(HomePageFragment.this).titleBar(mTbHomeNew).keyboardEnable(true).navigationBarEnable(false)
                                    .statusBarDarkFont(true).statusBarColor(bgColor).init();
                            mIvMessage.setSelected(true);
                            mIvHomeShopCar.setSelected(true);
                        }

                        //设置tab指示器颜色
                        if (!TextUtils.isEmpty(fontColor)) {
                            mTablayoutHome.setIndicatorColor(Color.parseColor(fontColor));
                            mTablayoutHome.setTextUnselectColor(Color.parseColor(fontColor));
                            mTablayoutHome.setTextSelectColor(Color.parseColor(fontColor));
                        }

                        //设置导航栏适配器
                        if (goodsNavbarList != null && goodsNavbarList.size() > 0) {
                            mGoodsNavbarList.clear();
                            mGoodsNavbarList.addAll(goodsNavbarList);
                            HomePageNewAdapter homePageNewAdapter = new HomePageNewAdapter(HomePageFragment.this.getChildFragmentManager(), mGoodsNavbarList);
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
                                mTvMarqueeText.setText(rollMsg);
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


    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            getMessageCount(getActivity(), badgeMsg);
        }
        isFirst = false;
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

    @OnClick(R.id.iv_float_ad_icon)
    void floatAdSkip(View view) {
        CommunalADActivityEntity.CommunalADActivityBean communalADActivityBean = (CommunalADActivityEntity.CommunalADActivityBean) view.getTag(R.id.iv_tag);
        if (communalADActivityBean != null) {
            adClickTotal(getActivity(), communalADActivityBean.getId());
            setSkipPath(getActivity(), getStrings(communalADActivityBean.getAndroidLink()), false);
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

    public String getFragmentName() {
        if (mTablayoutHome != null && mTablayoutHome.getViewPager() != null) {
            int currentTab = mTablayoutHome.getCurrentTab();
            PagerAdapter adapter = mTablayoutHome.getViewPager().getAdapter();
            if (adapter instanceof HomePageNewAdapter) {
                Fragment item = ((HomePageNewAdapter) adapter).getItem(currentTab);
                if (item != null) {
                    return item.getClass().getSimpleName();
                }
            } else {
                return "";
            }
        }
        return "";
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badgeCart != null) {
                badgeCart.setBadgeNumber((int) message.result);
            }
        }
    }

    //拼装滚动通知内容，并计算滚动时间
    private String getRollMsg(LifecycleOwner activity, List<MarqueeTextBean> marqueeTextList, ViewGroup viewGroup) {
        StringBuffer msg = new StringBuffer();
        try {
            int totalBlankLength = 0;
            for (MarqueeTextBean marqueeTextBean : marqueeTextList) {
                int displayType = marqueeTextBean.getDisplayType();
                int id = marqueeTextBean.getId();
                //如果是1直接显示，如果是0判断之前有没有显示过
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
            //根据通知的长度计算滚动时间，滚动结束隐藏通知
            long totalTime = 300 * (int) ((msg.length() - totalBlankLength) + totalBlankLength * 1.0f / 3);
            new LifecycleHandler(activity).postDelayed(() -> viewGroup.setVisibility(View.GONE), totalTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return msg.toString();
    }
}
