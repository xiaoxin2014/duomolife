package com.amkj.dmsh.homepage.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.CommonPagerAdapter;
import com.amkj.dmsh.base.ViewHolder;
import com.amkj.dmsh.bean.HomeWelfareEntity.HomeWelfareBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

/**
 * Created by xiaoxin on 2019/4/14 0014
 * Version:v4.0.0
 * ClassDescription :首页福利社（精选专题）适配器
 */
public class HomeWelfareAdapter extends CommonPagerAdapter<HomeWelfareBean> {

    public HomeWelfareAdapter(Context context, List<HomeWelfareBean> datas) {
        super(context, datas, R.layout.item_home_walfare);
    }

    @Override
    public void convert(ViewHolder helper, int position, HomeWelfareBean item) {
        if (item == null) return;
        ImageView ivCover = helper.getView(R.id.iv_welfare_cover);
        GlideImageLoaderUtil.loadRoundImg(mContext, helper.getView(R.id.iv_welfare_cover), item.getPicUrl(), AutoSizeUtils.mm2px(mAppContext, 5));
        helper.setText(R.id.tv_topic_name, item.getTitle());
        ivCover.setOnClickListener(view -> {

        });
//        helper.setText(R.id.tv_topic_desc, item.get());
        RecyclerView rvTopicGoods = helper.getView(R.id.rv_topic_goods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvTopicGoods.setLayoutManager(linearLayoutManager);
        List<HomeWelfareBean.GoodsBean> goods = item.getGoods();
        List<HomeWelfareBean.GoodsBean> newGoods = new ArrayList<>();
        //最多显示三个
        for (int i = 0; i < (goods.size() > 3 ? 3 : goods.size()); i++) {
            newGoods.add(goods.get(i));
        }

        //初始化福利社商品适配器
        BaseQuickAdapter topicGoodsAdapter = new BaseQuickAdapter<HomeWelfareBean.GoodsBean, BaseViewHolder>(R.layout.item_welfear_goods, newGoods) {
            @Override
            protected void convert(BaseViewHolder helper, HomeWelfareBean.GoodsBean goodsBean) {
                GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_pic), goodsBean.getPicUrl());
                helper.setText(R.id.tv_price, "¥" + goodsBean.getPrice());
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
                        for (UserLikedProductEntity.LikedProductBean.MarketLabelBean marketLabelBean : goodsBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_label.addView(getLabelInstance().createLabelText(mContext, marketLabelBean.getTitle(), 0));
                            }
                        }
                    }
                } else {
                    fbl_label.setVisibility(View.GONE);
                }

                View childItemView = helper.itemView;
                //动态设置商品宽度
                childItemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        childItemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = childItemView.getLayoutParams();

                        layoutParams.width = AutoSizeUtils.mm2px(mContext, 548) / 3;
                        childItemView.setLayoutParams(layoutParams);
                    }
                });
            }
        };
        topicGoodsAdapter.setOnItemClickListener((adapter, view, position1) -> {
            HomeWelfareBean.GoodsBean goodsBean = (HomeWelfareBean.GoodsBean) view.getTag();
            Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(goodsBean.getId()));
            mContext.startActivity(intent);
        });
        rvTopicGoods.setAdapter(topicGoodsAdapter);
    }
}
