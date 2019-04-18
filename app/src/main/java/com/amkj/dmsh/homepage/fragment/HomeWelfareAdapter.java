package com.amkj.dmsh.homepage.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

/**
 * Created by xiaoxin on 2019/4/14 0014
 * Version:v4.0.0
 * ClassDescription :首页福利社（精选专题）适配器
 */
public class HomeWelfareAdapter extends BaseQuickAdapter<HomeWelfareBean, BaseViewHolder> {

    private final Context mContext;
    private final int mWidth;

    public HomeWelfareAdapter(Context context, @Nullable List<HomeWelfareBean> data) {
        super(R.layout.item_home_walfare, data);
        mContext = context;
        int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
        mWidth = (screenWidth / 5 * 4);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeWelfareBean item) {
        if (item==null) return;
        GlideImageLoaderUtil.loadRoundImg(mContext, helper.getView(R.id.iv_welfare_cover), item.getPicUrl(), 10);
        helper.setText(R.id.tv_topic_name, item.getTitle())
                .addOnClickListener(R.id.iv_welfare_cover).setTag(R.id.iv_welfare_cover, R.id.iv_tag, item);
//        helper.setText(R.id.tv_topic_desc, item.get());
        RecyclerView rvTopicGoods = helper.getView(R.id.rv_topic_goods);
        View itemView = helper.itemView;
        //动态设置福利社主题Item宽度
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
                layoutParams.width = mWidth;
                itemView.setLayoutParams(layoutParams);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rvTopicGoods.setLayoutManager(linearLayoutManager);
        List<GoodsBean> goods = item.getGoods();
        List<GoodsBean> newGoods = new ArrayList<>();
        //最多显示三个
        for (int i = 0; i < (goods.size() > 3 ? 3 : goods.size()); i++) {
            newGoods.add(goods.get(i));
        }

        //初始化福利社商品适配器
        BaseQuickAdapter topicGoodsAdapter = new BaseQuickAdapter<GoodsBean, BaseViewHolder>(R.layout.item_welfear_goods, newGoods) {
            @Override
            protected void convert(BaseViewHolder helper, GoodsBean goodsBean) {
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
                        for (LikedProductBean.MarketLabelBean marketLabelBean : goodsBean.getMarketLabelList()) {
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
                        layoutParams.width = (mWidth - AutoSizeUtils.mm2px(mContext, 10) * 4) / newGoods.size();
                        childItemView.setLayoutParams(layoutParams);
                    }
                });
                childItemView.setTag(goodsBean);
            }
        };
        topicGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsBean goodsBean = (GoodsBean) view.getTag();
            Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(goodsBean.getId()));
            mContext.startActivity(intent);
        });
        rvTopicGoods.setAdapter(topicGoodsAdapter);

        itemView.setTag(item);
    }
}
