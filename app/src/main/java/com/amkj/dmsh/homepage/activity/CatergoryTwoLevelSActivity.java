package com.amkj.dmsh.homepage.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.CatergoryTwoLevelAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :分类-二级分类
 */
public class CatergoryTwoLevelSActivity extends BaseActivity {
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
    private String mCatergoryOneLevelName;
    private String mPid;
    private String mId;
    private CatergoryOneLevelEntity mCatergoryEntity;
    private CatergoryTwoLevelAdapter mTwoLevelAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_catergory_two_level;
    }

    @Override
    protected void initViews() {
        if (getIntent().getExtras() != null) {
            mPid = getIntent().getStringExtra(ConstantVariable.CATEGORY_PID);//一级分类id
            mId = getIntent().getStringExtra(ConstantVariable.CATEGORY_ID);//二级分类id,类型为"全部"时传0
        } else {
            showToast("数据有误，请重试");
            finish();
        }


        mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setTitleName(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setTitleName(int position) {
        mTvHeaderTitle.setText(position == 0 ? mCatergoryOneLevelName + "(全部)" : mChildCategoryListBeanList.get(position).getName());
    }

    @Override
    protected void loadData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.QUALITY_SHOP_TYPE, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mChildCategoryListBeanList.clear();
                mCatergoryEntity = GsonUtils.fromJson(result, CatergoryOneLevelEntity.class);
                if (mCatergoryEntity != null) {
                    String code = mCatergoryEntity.getCode();
                    List<CatergoryOneLevelBean> catergoryList = mCatergoryEntity.getResult();
                    if (catergoryList != null && catergoryList.size() > 0) {
                        for (int i = 0; i < catergoryList.size(); i++) {
                            if (mPid.equals(catergoryList.get(i).getId())) {
                                CatergoryOneLevelBean catergoryOneLevelBean = catergoryList.get(i);
                                if (catergoryOneLevelBean != null) {
                                    mCatergoryOneLevelName = catergoryOneLevelBean.getName();
                                    List<ChildCategoryListBean> childCategoryList = catergoryOneLevelBean.getChildCategoryList();
                                    if (childCategoryList != null) {
                                        //加上(全部)分类
                                        ChildCategoryListBean allCatergoryBean = new ChildCategoryListBean();
                                        allCatergoryBean.setName("全部");
                                        allCatergoryBean.setId("0");
                                        allCatergoryBean.setPid(mPid);
                                        mChildCategoryListBeanList.add(allCatergoryBean);
                                        mChildCategoryListBeanList.addAll(childCategoryList);
                                        mTwoLevelAdapter = new CatergoryTwoLevelAdapter(getSupportFragmentManager(), mChildCategoryListBeanList);
                                        mVpContainer.setAdapter(mTwoLevelAdapter);
                                        mStbGoodsCatergory.setViewPager(mVpContainer);
                                        mVpContainer.setOffscreenPageLimit(mChildCategoryListBeanList.size() - 1);
                                        for (int j = 0; j < mChildCategoryListBeanList.size(); j++) {
                                            if (mId.equals(mChildCategoryListBeanList.get(j).getId())) {
                                                mStbGoodsCatergory.setCurrentTab(j);
                                                setTitleName(j);
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    } else if (ERROR_CODE.equals(code)) {
                        showToast(mCatergoryEntity.getMsg());
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mChildCategoryListBeanList, mCatergoryEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mChildCategoryListBeanList, mCatergoryEntity);

            }
        });
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
