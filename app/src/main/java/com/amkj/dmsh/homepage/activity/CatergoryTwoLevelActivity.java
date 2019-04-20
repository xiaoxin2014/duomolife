package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.CatergoryGoodsAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TWO_LEVEL_LIST;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_PRODUCT_TYPE_LIST;

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
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.fl_header_service)
    FrameLayout mFlHeaderService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    @BindView(R.id.tablayout_catergory)
    TabLayout mTablayoutCatergory;
    @BindView(R.id.rb_new)
    RadioButton mRbNew;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.rv_catergory_goods)
    RecyclerView mRvCatergoryGoods;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    private List<ChildCategoryListBean> mChildCategoryListBeanList;
    private List<LikedProductBean> productList = new ArrayList<>();
    private int mPosition;
    private int page = 1;
    private UserLikedProductEntity likedProductEntity;
    private RemoveExistUtils<LikedProductBean> removeExistUtils = new RemoveExistUtils<>();
    CatergoryGoodsAdapter mCatergoryGoodsAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_catergory_product;
    }

    @Override
    protected void initViews() {
        mIvImgShare.setVisibility(View.GONE);
        mIvImgService.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && getIntent().getParcelableArrayListExtra(CATEGORY_TWO_LEVEL_LIST) != null && getIntent().getParcelableArrayListExtra(CATEGORY_TWO_LEVEL_LIST).size() > 0) {
            mChildCategoryListBeanList = getIntent().getParcelableArrayListExtra(CATEGORY_TWO_LEVEL_LIST);
            mPosition = getIntent().getIntExtra("position", 0);
            ChildCategoryListBean childCategoryListBean = mChildCategoryListBeanList.get(mPosition);
            //加上(全部)分类
            ChildCategoryListBean allCatergoryBean = new ChildCategoryListBean();
            allCatergoryBean.setType(4);
            allCatergoryBean.setName("全部");
            mChildCategoryListBeanList.add(allCatergoryBean);
            mTvHeaderTitle.setText(childCategoryListBean.getName());
        } else {
            showToast(this, R.string.miss_parameters_hint);
            return;
        }

        mTablayoutCatergory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (page != 1) {
                    //获取分类商品
                    if (!loadHud.isShowing()) loadHud.show();
                    page = 1;
                    mPosition = tab.getPosition();
                    getCatergoryGoods();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTablayoutCatergory.setTabMode(TabLayout.MODE_SCROLLABLE);

        mRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton childAt = (RadioButton) radioGroup.getChildAt(i);
            if (childAt.getId() == R.id.rb_new) {

            } else if (childAt.getId() == R.id.rb_num) {

            }
        });

        //初始化适配器
        initAdapter();
    }

    private void initAdapter() {
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });
        //初始化新人专享适配器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this
                , 3);
        mRvCatergoryGoods.setLayoutManager(gridLayoutManager);
        ItemDecoration itemDecoration = new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create();
        mRvCatergoryGoods.addItemDecoration(itemDecoration);
        mCatergoryGoodsAdapter = new CatergoryGoodsAdapter(this, productList);
        mCatergoryGoodsAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        });
        mRvCatergoryGoods.setAdapter(mCatergoryGoodsAdapter);
    }

    @Override
    protected void loadData() {
        //获取分类商品
        getCatergoryGoods();
    }


    private void getCatergoryGoods() {
        ChildCategoryListBean childCategoryListBean = mChildCategoryListBeanList.get(mPosition);
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("id", childCategoryListBean.getId());
        params.put("pid", childCategoryListBean.getPid());
        params.put("orderTypeId", childCategoryListBean.getType());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_PRODUCT_TYPE_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        mSmartLayout.finishRefresh();
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }

                        Gson gson = new Gson();
                        likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            List<LikedProductBean> likedProductBeanList = likedProductEntity.getLikedProductBeanList();
                            if (likedProductBeanList != null && likedProductBeanList.size() > 0 && SUCCESS_CODE.equals(likedProductEntity.getCode())) {
                                if (page == 1) {
                                    productList.clear();
                                    removeExistUtils.clearData();
                                }
                                productList.addAll(removeExistUtils.removeExistList(likedProductEntity.getLikedProductBeanList()));
                                mCatergoryGoodsAdapter.notifyDataSetChanged();
                                mCatergoryGoodsAdapter.loadMoreComplete();

                                if (page == 1) {
                                    mTablayoutCatergory.removeAllTabs();
                                    for (int i = 0; i < mChildCategoryListBeanList.size(); i++) {
                                        if (i != mPosition) {
                                            TabLayout.Tab tab = mTablayoutCatergory.newTab().setText(mChildCategoryListBeanList.get(i).getName());
                                            mTablayoutCatergory.addTab(tab);
                                        }
                                    }
                                }
                            } else if (ERROR_CODE.equals(likedProductEntity.getCode())) {
                                ConstantMethod.showToast(likedProductEntity.getMsg());
                                mCatergoryGoodsAdapter.loadMoreFail();
                            } else {
                                mCatergoryGoodsAdapter.loadMoreEnd();
                            }
                        } else {
                            mCatergoryGoodsAdapter.loadMoreEnd();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productList, likedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        mSmartLayout.finishRefresh();
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        mCatergoryGoodsAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productList, likedProductEntity);
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
    protected View getLoadView() {
        return mSmartLayout;
    }
}
