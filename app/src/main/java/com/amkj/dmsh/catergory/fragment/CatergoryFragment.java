package com.amkj.dmsh.catergory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.RecyclerViewScrollHelper;
import com.amkj.dmsh.catergory.adapter.CatergoryOneLevelAdapter;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.RelateArticleBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.activity.ArticleTypeActivity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.flycoTablayout.SlidingIconTabLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatAd;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;

/**
 * Created by xiaoxin on 2019/4/19 0019
 * Version:v4.0.0
 * ClassDescription :一级分类(代替良品)
 */
public class CatergoryFragment extends BaseFragment {
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.tb_catergory)
    LinearLayout mTbCatergory;
    @BindView(R.id.tablayout_catergory)
    TabLayout mTablayoutCatergory;
    @BindView(R.id.fl_fragment_quality)
    FrameLayout mFlFragmentQuality;
    @BindView(R.id.rv_catergory)
    RecyclerView mRvCatergory;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    @BindView(R.id.iv_float_ad_icon)
    ImageView iv_float_ad_icon;
    @BindView(R.id.stb_catergory)
    SlidingIconTabLayout mStbCatergory;
    Unbinder unbinder;
    @BindView(R.id.vp_catergory)
    ViewPager mVpCatergory;
    Unbinder unbinder1;
    private List<CatergoryOneLevelBean> mCatergoryBeanList = new ArrayList<>();
    private CatergoryOneLevelEntity mCatergoryEntity;
    private CatergoryOneLevelAdapter mOneLevelAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_catergory;
    }

    @Override
    protected void initViews() {
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            getOneLevelData();
        });
        mOneLevelAdapter = new CatergoryOneLevelAdapter(getActivity(), mCatergoryBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRvCatergory.setLayoutManager(linearLayoutManager);
        mRvCatergory.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)
                .create());
        mRvCatergory.setAdapter(mOneLevelAdapter);
        mOneLevelAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            RelateArticleBean relateArticleBean = (RelateArticleBean) view.getTag();
            switch (view.getId()) {
                //进入文章专题
                case R.id.rl_more_artical:
                    if (relateArticleBean != null) {
                        Intent intent = new Intent(getActivity(), ArticleTypeActivity.class);
                        intent.putExtra("categoryTitle", relateArticleBean.getCategoryName());
                        intent.putExtra("categoryId", relateArticleBean.getArticles().get(0).getArticleCategoryId());
                        if (getActivity() != null) startActivity(intent);
                    }
                    break;
                //点击进入文章详情
                case R.id.fl_artical_left:
                    if (relateArticleBean != null) {
                        Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                        intent.putExtra("ArtId", String.valueOf(relateArticleBean.getArticles().get(0).getDocumentId()));
                        startActivity(intent);
                    }
                    break;
                //点击进入文章详情
                case R.id.fl_artical_right:
                    if (relateArticleBean != null) {
                        Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                        intent.putExtra("ArtId", String.valueOf(relateArticleBean.getArticles().get(1).getDocumentId()));
                        startActivity(intent);
                    }
                    break;
            }
        });
        mTablayoutCatergory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RecyclerViewScrollHelper.scrollToPosition(mRvCatergory, tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mRvCatergory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                mTablayoutCatergory.setScrollPosition(position, 0, true);
            }
        });

        mTablayoutCatergory.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    @Override
    protected void loadData() {
        //获取一级分类数据
        getOneLevelData();
        //浮窗广告
        getFloatAd(getActivity(), iv_float_ad_icon);
    }


    private void getOneLevelData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.QUALITY_SHOP_TYPE, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mCatergoryEntity = new Gson().fromJson(result, CatergoryOneLevelEntity.class);
                if (mCatergoryEntity != null) {
                    String code = mCatergoryEntity.getCode();
                    List<CatergoryOneLevelBean> catergoryList = mCatergoryEntity.getResult();
                    if (catergoryList != null && catergoryList.size() > 0) {
                        mCatergoryBeanList.clear();
                        for (int i = 0; i < catergoryList.size(); i++) {
                            if (catergoryList.get(i).getType() != 4) {
                                mCatergoryBeanList.add((catergoryList.get(i)));
                            }
                        }
                        mOneLevelAdapter.notifyDataSetChanged();
                        mTablayoutCatergory.removeAllTabs();
                        List<String> titleList = new ArrayList<>();
                        for (int i = 0; i < mCatergoryBeanList.size(); i++) {
                            TabLayout.Tab tab = mTablayoutCatergory.newTab();
                            tab.setText(mCatergoryBeanList.get(i).getName());
                            mTablayoutCatergory.addTab(tab);
                            titleList.add(mCatergoryBeanList.get(i).getName());
                        }
//                        MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
//                        mVpCatergory.setAdapter(myPagerAdapter,titleList );

                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mCatergoryEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCatergoryBeanList, mCatergoryEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCatergoryBeanList, mCatergoryEntity);
            }
        });
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
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).titleBar(mTbCatergory).keyboardEnable(true).navigationBarEnable(false)
                .statusBarDarkFont(true).init();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }
}
