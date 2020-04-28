package com.amkj.dmsh.homepage.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CategoryTypeEntity;
import com.amkj.dmsh.bean.CategoryTypeEntity.CategoryTypeBean;
import com.amkj.dmsh.homepage.adapter.HomeArticleTypeAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.Url.H_CATEGORY_LIST;

/**
 * Created by xiaoxin on 2019/4/24 0024
 * Version:v4.0.0
 * ClassDescription :文章分类
 */
public class ArticleTypeActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    @BindView(R.id.stb_artical)
    SlidingTabLayout mStbArtical;
    @BindView(R.id.viewpager_container)
    ViewPager mViewpagerContainer;
    @BindView(R.id.ll_catergory)
    LinearLayout mLlCatergory;
    private List<CategoryTypeBean> mCategoryTypeList = new ArrayList<>();
    private CategoryTypeEntity mCategoryTypeEntity;
    private int mCategoryId;

    @Override
    protected int getContentView() {
        return R.layout.activity_artical_catergory;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("种草特辑");
        if (getIntent() != null) {
            mCategoryId = getStringChangeIntegers(getIntent().getStringExtra("categoryId"));
        }
    }

    @Override
    protected void loadData() {
        getArticleTypeList();
    }

    private void getArticleTypeList() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_CATEGORY_LIST, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                mCategoryTypeEntity = gson.fromJson(result, CategoryTypeEntity.class);
                if (mCategoryTypeEntity != null) {
                    List<CategoryTypeBean> categoryTypeList = mCategoryTypeEntity.getCategoryTypeList();
                    if (categoryTypeList != null && categoryTypeList.size() > 0) {
                        mCategoryTypeList.addAll(categoryTypeList);
                        mCategoryTypeList.add(new CategoryTypeBean("全部"));
                        HomeArticleTypeAdapter qualityPageAdapter = new HomeArticleTypeAdapter(getSupportFragmentManager(), categoryTypeList);
                        mViewpagerContainer.setAdapter(qualityPageAdapter);
                        mStbArtical.setViewPager(mViewpagerContainer);
                        mViewpagerContainer.setOffscreenPageLimit(mCategoryTypeList.size() - 1);
                        setCurrentTab();
                    } else {
                        showToast(mCategoryTypeEntity.getMsg());
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCategoryTypeList, mCategoryTypeEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCategoryTypeList, mCategoryTypeEntity);
            }
        });
    }

    private void setCurrentTab() {
        if (mCategoryId != 0) {
            for (int i = 0; i < mCategoryTypeList.size(); i++) {
                int id = mCategoryTypeList.get(i).getId();
                if (mCategoryId == id) {
                    mStbArtical.setCurrentTab(i);
                    break;
                }
            }
        } else {
            mStbArtical.setCurrentTab(0);
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlCatergory;
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
