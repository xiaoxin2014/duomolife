package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_THIRTY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:热销商品
 */
public class QualityTypeHotSaleProductFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List<LikedProductBean> likedProductBeans = new ArrayList();
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private UserLikedProductEntity typeProductBean;
    private String hotSaleDay;
    private RemoveExistUtils removeExistUtils;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler;
    }

    @Override
    protected void initViews() {
        if (TextUtils.isEmpty(hotSaleDay)) {
            return;
        }
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        qualityTypeProductAdapter = new QualityTypeProductAdapter(getActivity(), likedProductBeans);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setEnableLoadMore(false);
        qualityTypeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        qualityTypeProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
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
            }
        });
        totalPersonalTrajectory = insertNewTotalData(getActivity());
        removeExistUtils = new RemoveExistUtils();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        getQualityTypePro();
    }

    private void getQualityTypePro() {
        String url = Url.BASE_URL + Url.QUALITY_HOT_SALE_LIST_NEW;
        Map<String, Object> params = new HashMap<>();
        params.put("day", hotSaleDay);
        if("1".equals(hotSaleDay) ){
            params.put("showCount", TOTAL_COUNT_TWENTY);
        }else{
            params.put("showCount", TOTAL_COUNT_THIRTY);
        }
        params.put("currentPage", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        qualityTypeProductAdapter.loadMoreComplete();
                        likedProductBeans.clear();
                        removeExistUtils.clearData();
                        Gson gson = new Gson();
                        typeProductBean = gson.fromJson(result, UserLikedProductEntity.class);
                        if (typeProductBean != null) {
                            if (typeProductBean.getCode().equals(SUCCESS_CODE)) {
                                likedProductBeans.addAll(removeExistUtils.removeExistList(typeProductBean.getLikedProductBeanList()));
                            } else if (typeProductBean.getCode().equals(EMPTY_CODE)) {
                                qualityTypeProductAdapter.loadMoreEnd();
                            } else {
                                qualityTypeProductAdapter.loadMoreEnd();
                                showToast(getActivity(), typeProductBean.getMsg());
                            }
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, likedProductBeans, typeProductBean);
                    }

                    @Override
                    public void onNotNetOrException() {
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, likedProductBeans, typeProductBean);
                    }

                    @Override
                    public void netClose() {
                        showToast(getActivity(), R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(getActivity(), R.string.invalidData);
                    }
                });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if ("refresh".equals(message.type)
                && "hotSaleData".equals(message.result)) {
            loadData();
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        hotSaleDay = (String) bundle.get("hotSaleDay");
    }
}
