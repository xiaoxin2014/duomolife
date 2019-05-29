package com.amkj.dmsh.homepage.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean;
import com.amkj.dmsh.catergory.view.CatergoryHeadView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
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

import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_PID;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.Url.Q_PRODUCT_TYPE_LIST;


/**
 * Created by xiaoxin on 2019/4/21 0021
 * Version:v4.0.0
 * ClassDescription :一级分类相关商品
 */
public class HomeCatergoryActivity extends BaseActivity {
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.rv_catergory_goods)
    RecyclerView mRvCatergoryGoods;
    private List<LikedProductBean> productList = new ArrayList<>();
    private int page = 1;
    private UserLikedProductEntity likedProductEntity;
    private RemoveExistUtils<LikedProductBean> removeExistUtils = new RemoveExistUtils<>();
    CatergoryGoodsAdapter mCatergoryGoodsAdapter;
    private CatergoryOneLevelEntity mCatergoryEntity;
    private String mCategoryPid;
    private String mCategoryName;
    private String mCategoryId;
    private CatergoryHeadView mCatergoryHeadView;

    @Override
    protected int getContentView() {
        return R.layout.activity_home_catergory;
    }

    @Override
    protected void initViews() {
        mIvImgService.setVisibility(View.GONE);
        mIvImgShare.setVisibility(View.GONE);
        if (getIntent() != null) {
            mCategoryPid = getIntent().getStringExtra(CATEGORY_PID);
            mCategoryName = getIntent().getStringExtra(CATEGORY_NAME);
            mCategoryId = getIntent().getStringExtra(CATEGORY_ID);
            mTvHeaderTitle.setText(mCategoryName);
        }

        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });

        //分类商品适配器
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
            getCatergoryGoods();
        }, mRvCatergoryGoods);
        mCatergoryHeadView = new CatergoryHeadView(getActivity(), null);
        mCatergoryGoodsAdapter.addHeaderView(mCatergoryHeadView);
        mRvCatergoryGoods.setAdapter(mCatergoryGoodsAdapter);
    }

    @Override
    protected void loadData() {
        getOneLevelData();
        getCatergoryGoods();
    }

    private void getOneLevelData() {
        Map<String, String> map = new HashMap<>();
        map.put("categoryPid", mCategoryPid);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.QUALITY_SHOP_TYPE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mCatergoryEntity = new Gson().fromJson(result, CatergoryOneLevelEntity.class);
                if (mCatergoryEntity != null) {
                    String code = mCatergoryEntity.getCode();
                    List<CatergoryOneLevelBean> catergoryList = mCatergoryEntity.getResult();
                    if (catergoryList != null && catergoryList.size() > 0) {
                        mCatergoryHeadView.updateView(catergoryList.get(0));
                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mCatergoryEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
            }
        });
    }

    private void getCatergoryGoods() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", 18);
        params.put("id", mCategoryPid);
        params.put("pid", mCategoryId);
        params.put("orderTypeId", 1);

        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_PRODUCT_TYPE_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        mSmartLayout.finishRefresh();
                        Gson gson = new Gson();
                        likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            List<LikedProductBean> likedProductBeanList = likedProductEntity.getLikedProductBeanList();
                            if (likedProductBeanList != null && likedProductBeanList.size() > 0) {
                                if (page == 1) {
                                    productList.clear();
                                    removeExistUtils.clearData();
                                }
                                productList.addAll(removeExistUtils.removeExistList(likedProductBeanList));
                                mCatergoryGoodsAdapter.notifyDataSetChanged();
                                mCatergoryGoodsAdapter.loadMoreComplete();
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
                        mCatergoryGoodsAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productList, likedProductEntity);
                    }
                });
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartLayout;
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
