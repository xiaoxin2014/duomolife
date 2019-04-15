package com.amkj.dmsh.homepage.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.HomeWelfareEntity.HomeWelfareBean;
import com.amkj.dmsh.bean.HomeWelfareEntity.HomeWelfareBean.GoodsBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

/**
 * Created by xiaoxin on 2019/4/14 0014
 * Version:v4.0.0
 * ClassDescription :首页福利社（精选专题）适配器
 */
public class HomeWelfareAdapter extends BaseQuickAdapter<HomeWelfareBean, BaseViewHolder> {

    private final Context mContext;

    public HomeWelfareAdapter(Context context, @Nullable List<HomeWelfareBean> data) {
        super(R.layout.item_home_walfare, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeWelfareBean item) {
        GlideImageLoaderUtil.loadRoundImg(mContext, helper.getView(R.id.iv_welfare_cover), item.getPicUrl(), 10);
        helper.setText(R.id.tv_topic_name, item.getTitle())
                .addOnClickListener(R.id.iv_welfare_cover);
//        helper.setText(R.id.tv_topic_desc, item.get());
        RecyclerView rvTopicGoods = helper.getView(R.id.rv_topic_goods);
        View itemView = helper.itemView;
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
                int width = (screenWidth / 5 * 4);
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.width = width;
                itemView.setLayoutParams(layoutParams);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        rvTopicGoods.setLayoutManager(gridLayoutManager);
        ItemDecoration itemDecorationRight = new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp_red)
                .create();
        rvTopicGoods.addItemDecoration(itemDecorationRight);
        List<GoodsBean> goods = item.getGoods();
        List<GoodsBean> newGoods = new ArrayList<>();
        //最多显示三个
        for (int i = 0; i < (goods.size() > 3 ? 3 : goods.size()); i++) {
            newGoods.add(goods.get(i));
        }

        BaseQuickAdapter topicGoodsAdapter = new BaseQuickAdapter<GoodsBean, BaseViewHolder>(R.layout.item_welfear_goods, newGoods) {
            @Override
            protected void convert(BaseViewHolder helper, GoodsBean goodsBean) {
                GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_pic), goodsBean.getPicUrl());
                helper.setText(R.id.tv_price, goodsBean.getPrice());
                FlexboxLayout fbl_label = helper.getView(R.id.fbl_label);
                if (!TextUtils.isEmpty(goodsBean.getActivityTag()) || (goodsBean.getMarketLabelList() != null
                        && goodsBean.getMarketLabelList().size() > 0)) {
                    fbl_label.setVisibility(View.VISIBLE);
                    fbl_label.removeAllViews();
                    if (!TextUtils.isEmpty(goodsBean.getActivityTag())) {
                        fbl_label.addView(getLabelInstance().createLabelText(mContext, goodsBean.getActivityTag(), 1));
                    }
                    if (goodsBean.getMarketLabelList() != null
                            && goodsBean.getMarketLabelList().size() > 0) {
                        for (LikedProductBean.MarketLabelBean marketLabelBean : goodsBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_label.addView(getLabelInstance().createLabelText(mContext, marketLabelBean.getTitle(), 0));
                            }
                        }
                    }
                } else {
                    fbl_label.setVisibility(View.GONE);
                }
                helper.itemView.setTag(goodsBean);
            }
        };
        topicGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsBean goodsBean = (GoodsBean) view.getTag();
            Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", goodsBean.getId());
            mContext.startActivity(intent);
        });
        rvTopicGoods.setAdapter(topicGoodsAdapter);

        helper.itemView.setTag(item);
    }
}
