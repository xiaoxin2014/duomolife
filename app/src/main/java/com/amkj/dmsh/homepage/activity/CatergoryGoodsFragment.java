package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
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

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.Url.Q_PRODUCT_TYPE_LIST;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :分类-二级分类-商品
 */
public class CatergoryGoodsFragment extends BaseFragment {
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.rv_catergory_goods)
    RecyclerView mRvCatergoryGoods;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    private List<LikedProductBean> productList = new ArrayList<>();
    private String orderType = "1";
    private int page = 1;
    private UserLikedProductEntity likedProductEntity;
    private RemoveExistUtils<LikedProductBean> removeExistUtils = new RemoveExistUtils<>();
    QualityTypeProductAdapter mCatergoryGoodsAdapter;
    private String mId;
    private String mPid;

    @Override
    protected int getContentView() {
        return R.layout.fragment_catergory_goods;
    }

    @Override
    protected void initViews() {
        //初始化监听事件
        initListener();
        //初始化适配器
        initAdapter();
    }

    private void initListener() {
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            orderType = "1";
            ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(false);
            ((RadioButton) mRadioGroup.getChildAt(1)).setChecked(false);
            loadData();
        });

        mRadioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            if (!loadHud.isShowing()) loadHud.show();
            if (id == R.id.rb_new) {
                orderType = "4";
            } else if (id == R.id.rb_num) {
                orderType = "2";
            }
            loadData();
        });

    }

    private void initAdapter() {
        //分类商品适配器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity()
                , 2);
        mRvCatergoryGoods.setLayoutManager(gridLayoutManager);
        ItemDecoration itemDecoration = new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create();
        mRvCatergoryGoods.addItemDecoration(itemDecoration);
        mCatergoryGoodsAdapter = new QualityTypeProductAdapter(getActivity(), productList);
        mCatergoryGoodsAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, mRvCatergoryGoods);
        mRvCatergoryGoods.setAdapter(mCatergoryGoodsAdapter);
        mCatergoryGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                startActivity(intent);
            }
        });
        mCatergoryGoodsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            loadHud.show();
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                if (userId > 0) {
                    switch (view.getId()) {
                        case R.id.iv_pro_add_car:
                            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                            baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                            baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                            baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                            baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                            ConstantMethod constantMethod = new ConstantMethod();
                            constantMethod.addShopCarGetSku(getActivity(), baseAddCarProInfoBean, loadHud);
                            break;
                    }
                } else {
                    loadHud.dismiss();
                    getLoginStatus(getActivity());
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getCatergoryGoods();
    }


    private void getCatergoryGoods() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", 18);
        params.put("id", mId);
        params.put("pid", mPid);
        params.put("orderTypeId", orderType);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_PRODUCT_TYPE_LIST
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
                            if (likedProductBeanList != null && likedProductBeanList.size() > 0) {
                                if (page == 1) {
                                    productList.clear();
                                    removeExistUtils.clearData();
                                }
                                productList.addAll(removeExistUtils.removeExistList(likedProductEntity.getLikedProductBeanList()));
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
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
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
    protected void getReqParams(Bundle bundle) {
        mId = (String) bundle.get("id");
        mPid = (String) bundle.get("pid");
    }
}
