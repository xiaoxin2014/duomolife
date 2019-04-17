package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

/**
 * Created by xiaoxin on 2019/4/16 0016
 * Version:v4.0.0
 * ClassDescription :新版首页分类商品适配器
 */

public class HomeProductAdapter extends BaseQuickAdapter<UserLikedProductEntity, BaseViewHolder> {
    private final Context context;

    public HomeProductAdapter(Context context, List<UserLikedProductEntity> productList) {
        super(R.layout.item_home_catergory, productList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserLikedProductEntity userLikedProductEntity) {
        if (userLikedProductEntity == null) return;
        helper.setText(R.id.tv_catergory_name, userLikedProductEntity.getCatergoryName())
                .addOnClickListener(R.id.rl_more_product).setTag(R.id.rl_more_product, userLikedProductEntity)
                .addOnClickListener(R.id.iv_ad).setTag(R.id.iv_ad, R.id.iv_tag, userLikedProductEntity);

        helper.getView(R.id.iv_ad).setVisibility(TextUtils.isEmpty(userLikedProductEntity.getAdCover()) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(userLikedProductEntity.getAdCover())) {
            GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_ad), userLikedProductEntity.getAdCover());
        }
        RecyclerView rvGoods = helper.getView(R.id.rv_catergory_goods);
        //初始化新人专享适配器
        GridLayoutManager newUserManager = new GridLayoutManager(mContext
                , 3);
        rvGoods.setLayoutManager(newUserManager);
        if (rvGoods.getTag() == null) {
            ItemDecoration itemDecoration = new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_five_gray_f)
                    .create();
            rvGoods.addItemDecoration(itemDecoration);
            rvGoods.setTag(userLikedProductEntity);
        }
        rvGoods.setNestedScrollingEnabled(false);
        BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter<LikedProductBean, BaseViewHolder>(R.layout.item_home_catergory_goods, userLikedProductEntity.getLikedProductBeanList()) {
            @Override
            protected void convert(BaseViewHolder helper, LikedProductBean goodsBean) {
                GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_pic), goodsBean.getPicUrl());
                helper.setText(R.id.tv_price, "¥" + goodsBean.getPrice())
                        .setText(R.id.tv_name, getStrings(goodsBean.getName()));
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

//                View childItemView = helper.itemView;
//                //动态设置商品宽度
//                childItemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        childItemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        ViewGroup.LayoutParams layoutParams = childItemView.getLayoutParams();
//                        layoutParams.width = (mWidth - AutoSizeUtils.mm2px(mContext, 10) * 4) / newGoods.size();
//                        childItemView.setLayoutParams(layoutParams);
//                    }
//                });
                helper.itemView.setTag(goodsBean);
            }
        };
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean goodsBean = (LikedProductBean) view.getTag();
            Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(goodsBean.getId()));
            mContext.startActivity(intent);
        });

        rvGoods.setAdapter(baseQuickAdapter);
        helper.itemView.setTag(userLikedProductEntity);
    }
}
