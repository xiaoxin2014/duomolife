package com.amkj.dmsh.mine.fragment;

import android.os.Bundle;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.NewGridItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

/**
 * Created by xiaoxin on 2020/9/24
 * Version:v4.7.0
 */
public class VipExclusiveGoodsFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private int page = 1;
    private BaseQuickAdapter qualityCustomTopicAdapter;
    private List<LikedProductBean> customProList = new ArrayList<>();
    private UserLikedProductEntity userLikedProductEntity;
    private String categoryId;
    private boolean isHomePage;


    @Override
    protected int getContentView() {
        return R.layout.fragment_group_custom_topic;
    }

    @Override
    protected void initViews() {
        //初始化会员专享自定义专区
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        qualityCustomTopicAdapter = new GoodProductAdapter(getActivity(), customProList);
        communal_recycler.setLayoutManager(gridLayoutManager);
        communal_recycler.addItemDecoration(new NewGridItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        communal_recycler.setAdapter(qualityCustomTopicAdapter);
        qualityCustomTopicAdapter.setOnLoadMoreListener(() -> {
            page++;
            getQualityCustomPro();
        }, communal_recycler);
        qualityCustomTopicAdapter.setEnableLoadMore(!isHomePage);
    }

    @Override
    protected void loadData() {
        page = 1;
        getQualityCustomPro();
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getQualityCustomPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_VIP_EXCLUSIVE_GOODS_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        qualityCustomTopicAdapter.loadMoreComplete();
                        if (page == 1) {
                            customProList.clear();
                        }

                        userLikedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (userLikedProductEntity != null) {
                            if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                customProList.addAll(userLikedProductEntity.getGoodsList());
                            } else if (userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityCustomTopicAdapter.loadMoreEnd();
                            } else {
                                showToast(userLikedProductEntity.getMsg());
                            }
                        }
                        qualityCustomTopicAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        qualityCustomTopicAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
                    }
                });
    }


    @Override
    protected void getReqParams(Bundle bundle) {
        categoryId = bundle.getString("categoryId");
        isHomePage = "1".equals(bundle.getString("isHomePage"));
    }
}
