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
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.adapter.SpringSaleRecyclerAdapterNew;
import com.amkj.dmsh.homepage.bean.BaseTimeProductTopicBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeForeShowBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/9/19
 * version 3.1.6
 * class description:top推荐 即将截团
 */
public class TopRecommendAtTimeEndGroupFragment extends BaseFragment {
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    private String promotionProductType;
    //    商品总数
    private List<BaseTimeProductTopicBean> saleTimeTotalList = new ArrayList();
    private SpringSaleRecyclerAdapterNew springSaleRecyclerAdapter;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler_wrap;
    }

    @Override
    protected void initViews() {
        if(TextUtils.isEmpty(promotionProductType)){
            return;
        }
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        springSaleRecyclerAdapter = new SpringSaleRecyclerAdapterNew(getActivity(), saleTimeTotalList);
        communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_white)






                .create());
        communal_recycler_wrap.setAdapter(springSaleRecyclerAdapter);
        springSaleRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseTimeProductTopicBean baseTimeProductTopicBean = (BaseTimeProductTopicBean) view.getTag();
                if (baseTimeProductTopicBean != null) {
                    Intent intent = new Intent();
                    TimeForeShowBean timeForeShowBean = (TimeForeShowBean) baseTimeProductTopicBean;
                    intent.setClass(getActivity(), ShopTimeScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(timeForeShowBean.getId()));
                    startActivity(intent);
                }
            }
        });
        springSaleRecyclerAdapter.setEnableLoadMore(false);
    }

    @Override
    protected void loadData() {
        getTopOverRecommendProduct();
    }

    private void getTopOverRecommendProduct() {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_TOP_PRODUCT;
        Map<String, Object> params = new HashMap<>();
        if("topRecommend".equals(promotionProductType)){
            params.put("recommendType", "top");
        }else{
            params.put("recommendType", "over");
        }
        if (userId != 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        springSaleRecyclerAdapter.loadMoreComplete();
                        Gson gson = new Gson();
                        TimeForeShowEntity timeForeShowEntity = gson.fromJson(result, TimeForeShowEntity.class);
                        if (timeForeShowEntity != null) {
                            if (timeForeShowEntity.getCode().equals(SUCCESS_CODE)) {
                                springSaleRecyclerAdapter.setEnableLoadMore(true);
                                if (timeForeShowEntity.getTimeForeShowList() != null
                                        && timeForeShowEntity.getTimeForeShowList().size() > 0) {
                                    saleTimeTotalList.addAll(timeForeShowEntity.getTimeForeShowList());
                                }
                                springSaleRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void netClose() {
                        springSaleRecyclerAdapter.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        springSaleRecyclerAdapter.loadMoreComplete();
                    }
                });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        promotionProductType = bundle.getString("promotionProductType");
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refreshData")
                &&"timeProduct".equals(message.result)) {
            loadData();
        }
    }
}
