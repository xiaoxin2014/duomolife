package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.CatergoryGoodsAdapter;
import com.amkj.dmsh.dominant.bean.NewUserCouponEntity;
import com.amkj.dmsh.dominant.initviews.UserFirstView;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.time.bean.UserFirstEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_GET_COUPON;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_LIST;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_LIST;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :??????????????????Fragment
 */
public class QualityNewUserFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ???????????????
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    private List<LikedProductBean> qualityNewUserShopList = new ArrayList<>();
    private CatergoryGoodsAdapter qualityNewUserShopAdapter;
    private QNewUserCoverHelper qNewUserCoverHelper;
    private View qNewUserCoverView;
    private UserLikedProductEntity qualityNewUserShopEntity;
    private int page = 1;
    private UserFirstView mUserFirstView;

    @Override
    protected int getContentView() {
        return R.layout.activity_quality_new_user;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("????????????");
        mTlQualityBar.setVisibility(GONE);
        iv_img_service.setVisibility(GONE);
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });
        setFloatingButton(download_btn_communal, communal_recycler);
        //?????????????????????
        qualityNewUserShopAdapter = new CatergoryGoodsAdapter(getActivity(), qualityNewUserShopList);
        qualityNewUserShopAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return qualityNewUserShopList.get(position).getItemType() == ConstantVariable.TITLE ? 3 : 1;
            }
        });
        qualityNewUserShopAdapter.setOnLoadMoreListener(() -> {
            page++;
            getNewUserCouponProduct();
        }, communal_recycler);
        //????????????????????????
        mUserFirstView = new UserFirstView(getActivity());
        qualityNewUserShopAdapter.addHeaderView(mUserFirstView);
        //?????????????????????
        qNewUserCoverView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_new_user_cover, communal_recycler, false);
        qNewUserCoverHelper = new QNewUserCoverHelper();
        ButterKnife.bind(qNewUserCoverHelper, qNewUserCoverView);
        qualityNewUserShopAdapter.addHeaderView(qNewUserCoverView);
        communal_recycler.setAdapter(qualityNewUserShopAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        getNewUserFirst();
        getQualityTypePro();
    }

    //????????????0????????????
    private void getNewUserFirst() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.QUALITY_NEW_USER_FIRST, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                UserFirstEntity userFirstEntity = GsonUtils.fromJson(result, UserFirstEntity.class);
                if (userFirstEntity != null) {
                    String code = userFirstEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        mUserFirstView.updateView(userFirstEntity);
                    } else {
                        qualityNewUserShopAdapter.removeHeaderView(mUserFirstView);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
            }
        });
    }

    //????????????
    private void getQualityTypePro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", TOTAL_COUNT_FORTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_NEW_USER_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (page == 1) {
                    qualityNewUserShopList.clear();
                }

                qualityNewUserShopEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                if (qualityNewUserShopEntity != null) {
                    List<LikedProductBean> goodsList = qualityNewUserShopEntity.getGoodsList();
                    if (goodsList != null && goodsList.size() > 0 && SUCCESS_CODE.equals(qualityNewUserShopEntity.getCode())) {
                        //????????????????????????
                        LikedProductBean qualityNewUserShopBean = new LikedProductBean();
                        qualityNewUserShopBean.setItemType(ConstantVariable.TITLE);
                        qualityNewUserShopBean.setTitleHead(R.drawable.newuser_exclusive);
                        qualityNewUserShopList.add(qualityNewUserShopBean);
                        //??????????????????
                        for (int i = 0; i < goodsList.size(); i++) {
                            LikedProductBean likedProductBean = goodsList.get(i);
                            likedProductBean.setActivityTag("????????????");
                        }
                        qualityNewUserShopList.addAll(goodsList);
                        qualityNewUserShopAdapter.notifyDataSetChanged();
                    } else if (EMPTY_CODE.equals(qualityNewUserShopEntity.getCode())) {
                        showToast(qualityNewUserShopEntity.getMsg());
                    }
                }
                getNewUserCouponProduct();
            }

            @Override
            public void onNotNetOrException() {
                if (page == 1) {
                    qualityNewUserShopList.clear();
                }
                getNewUserCouponProduct();
            }
        });
    }

    //????????????
    private void getNewUserCouponProduct() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("productType", 2);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_CUSTOM_PRO_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();

                UserLikedProductEntity qualityNewUserShopEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                if (qualityNewUserShopEntity != null) {
                    List<LikedProductBean> goodsList = qualityNewUserShopEntity.getGoodsList();
                    String code = qualityNewUserShopEntity.getCode();
                    if (goodsList != null && goodsList.size() > 0 && SUCCESS_CODE.equals(code)) {
                        //????????????????????????
                        if (page == 1) {
                            LikedProductBean qualityNewUserShopBean = new LikedProductBean();
                            qualityNewUserShopBean.setItemType(ConstantVariable.TITLE);
                            qualityNewUserShopBean.setTitleHead(R.drawable.coupon_zone);
                            qualityNewUserShopList.add(qualityNewUserShopBean);
                        }
                        qualityNewUserShopList.addAll(goodsList);
                        qualityNewUserShopAdapter.notifyDataSetChanged();
                        qualityNewUserShopAdapter.loadMoreComplete();
                    } else if (ERROR_CODE.equals(code)) {
                        showToast(qualityNewUserShopEntity.getMsg());
                        qualityNewUserShopAdapter.loadMoreFail();
                    } else {
                        qualityNewUserShopAdapter.loadMoreEnd();
                    }
                } else {
                    qualityNewUserShopAdapter.loadMoreEnd();
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityNewUserShopList, qualityNewUserShopEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                qualityNewUserShopAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityNewUserShopList, qualityNewUserShopEntity);
            }
        });
    }

    /**
     * ?????? ?????????????????????
     */
    private void getNewUserCouponGift() {
        loadHud.show();
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_NEW_USER_GET_COUPON, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                NewUserCouponEntity newUserCouponEntity = GsonUtils.fromJson(result, NewUserCouponEntity.class);
                if (newUserCouponEntity != null) {
                    if (newUserCouponEntity.getCode().equals(SUCCESS_CODE)) {
                        qNewUserCoverHelper.tv_new_user_get_coupon.setText("?????????");
                    } else if (!newUserCouponEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(newUserCouponEntity.getMsg());
                        qNewUserCoverHelper.tv_new_user_get_coupon.setText("?????????");
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }
        });
    }


    public class QNewUserCoverHelper {
        @BindView(R.id.iv_new_user_cover)
        ImageView iv_new_user_cover;
        @BindView(R.id.tv_new_user_get_coupon)
        public
        TextView tv_new_user_get_coupon;

        @OnClick(R.id.tv_new_user_get_coupon)
        void getNewUserCoupon() {
            if (userId > 0) {
                getNewUserCouponGift();
            } else {
                getLoginStatus(getActivity());
            }
        }
    }
}
