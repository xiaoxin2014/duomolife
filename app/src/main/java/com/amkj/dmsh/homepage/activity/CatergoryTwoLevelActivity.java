package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.adapter.CatergoryTwoLevelAdapter;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TWO_LEVEL_LIST;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :分类-二级分类
 */
public class CatergoryTwoLevelActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    @BindView(R.id.stb_goods_catergory)
    SlidingTabLayout mStbGoodsCatergory;
    @BindView(R.id.viewpager_container)
    ViewPager mVpContainer;
    @BindView(R.id.ll_catergory)
    LinearLayout mLlCatergory;
    private List<ChildCategoryListBean> mChildCategoryListBeanList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_catergory_two_level;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            List<ChildCategoryListBean> extraList = getIntent().getParcelableArrayListExtra(CATEGORY_TWO_LEVEL_LIST);
            int position = getIntent().getIntExtra("position", 0);

            if (extraList != null && extraList.size() > 0) {
                mChildCategoryListBeanList.addAll(extraList);
            }

            String catergoryOneLevelName = getIntent().getStringExtra(ConstantVariable.CATEGORY_NAME);
            int pid = getIntent().getIntExtra(ConstantVariable.CATEGORY_PID, 0);

            //加上(全部)分类
            ChildCategoryListBean allCatergoryBean = new ChildCategoryListBean();
            allCatergoryBean.setType(4);
            allCatergoryBean.setName("全部");
            allCatergoryBean.setId(0);
            allCatergoryBean.setPid(pid);
            mChildCategoryListBeanList.add(0, allCatergoryBean);

            CatergoryTwoLevelAdapter twoLevelAdapter = new CatergoryTwoLevelAdapter(getSupportFragmentManager(), mChildCategoryListBeanList);
            mVpContainer.setAdapter(twoLevelAdapter);
            mStbGoodsCatergory.setViewPager(mVpContainer);
            mVpContainer.setOffscreenPageLimit(mChildCategoryListBeanList.size() - 1);
            setTitleName(position, catergoryOneLevelName);
            mStbGoodsCatergory.setCurrentTab(position);
            mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    setTitleName(i, catergoryOneLevelName);

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
        NetLoadUtils.getNetInstance().showLoadSir(loadService, mChildCategoryListBeanList);
    }

    private void setTitleName(int position, String catergoryOneLevelName) {
        mTvHeaderTitle.setText(position == 0 ? catergoryOneLevelName + "(全部)" : mChildCategoryListBeanList.get(position).getName());
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlCatergory;
    }
}
