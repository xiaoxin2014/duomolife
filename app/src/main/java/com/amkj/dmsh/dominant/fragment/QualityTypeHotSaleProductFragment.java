package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
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
    private GoodProductAdapter qualityTypeProductAdapter;
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
        qualityTypeProductAdapter = new GoodProductAdapter(getActivity(), likedProductBeans);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setEnableLoadMore(false);
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
        String url =  Url.QUALITY_HOT_SALE_LIST_NEW;
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

                        typeProductBean = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (typeProductBean != null) {
                            if (typeProductBean.getCode().equals(SUCCESS_CODE)) {
                                likedProductBeans.addAll(removeExistUtils.removeExistList(typeProductBean.getGoodsList()));
                            } else if (typeProductBean.getCode().equals(EMPTY_CODE)) {
                                qualityTypeProductAdapter.loadMoreEnd();
                            } else {
                                qualityTypeProductAdapter.loadMoreEnd();
                                showToast( typeProductBean.getMsg());
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
                    public void onError(Throwable throwable) {
                        showToast(R.string.invalidData);
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
