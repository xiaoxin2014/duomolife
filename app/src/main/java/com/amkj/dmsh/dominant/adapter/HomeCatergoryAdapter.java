package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

/**
 * Created by xiaoxin on 2019/4/16 0016
 * Version:v4.0.0
 * ClassDescription :新版首页分类商品适配器
 */

public class HomeCatergoryAdapter extends BaseQuickAdapter<UserLikedProductEntity, BaseViewHolder> {
    private final Activity context;
    private CBViewHolderCreator cbViewHolderCreator;

    public HomeCatergoryAdapter(Activity context, List<UserLikedProductEntity> productList) {
        super(R.layout.item_home_catergory, productList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserLikedProductEntity userLikedProductEntity) {
        if (userLikedProductEntity == null) return;
        helper.setText(R.id.tv_catergory_name, userLikedProductEntity.getCatergoryName())
                .addOnClickListener(R.id.rl_more_product).setTag(R.id.rl_more_product, userLikedProductEntity);

        //初始化广告位
        ConvenientBanner convenientBanner = helper.getView(R.id.cb_banner);
        List<UserLikedProductEntity.AdBean> adList = userLikedProductEntity.getAdList();

        if (adList != null && adList.size() > 0) {
            convenientBanner.setVisibility(View.VISIBLE);
            if (cbViewHolderCreator == null) {
                cbViewHolderCreator = new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {
                        return new CommunalAdHolderView(itemView, context, true);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.layout_ad_image_video;
                    }
                };
            }

            List<CommunalADActivityBean> adBeanList = new ArrayList<>();
            for (int i = 0; i < adList.size(); i++) {
                UserLikedProductEntity.AdBean adBean = adList.get(i);
                CommunalADActivityBean communalADActivityBean = new CommunalADActivityBean();
                communalADActivityBean.setId(adBean.getIdX());
                communalADActivityBean.setAndroidLink(adBean.getAndroid());
                communalADActivityBean.setPicUrl(adBean.getPicUrl());
                adBeanList.add(communalADActivityBean);
            }

            convenientBanner.setPages(context, cbViewHolderCreator, adBeanList).setCanLoop(true)
                    .setPointViewVisible(true).setCanScroll(true)
                    .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                    .startTurning(getShowNumber("5") * 1000);
        } else {
            convenientBanner.setVisibility(View.GONE);
        }


        RecyclerView rvGoods = helper.getView(R.id.rv_catergory_goods);
        //初始化新人专享适配器
        GridLayoutManager newUserManager = new GridLayoutManager(context
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
                GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_goods_pic), goodsBean.getPicUrl());
                helper.setText(R.id.tv_price, "¥" + goodsBean.getPrice())
                        .setText(R.id.tv_name, getStrings(goodsBean.getName()));
                FlexboxLayout fbl_label = helper.getView(R.id.fbl_label);
                if (!TextUtils.isEmpty(goodsBean.getActivityTag()) || (goodsBean.getMarketLabelList() != null
                        && goodsBean.getMarketLabelList().size() > 0)) {
                    fbl_label.setVisibility(View.VISIBLE);
                    fbl_label.removeAllViews();
                    if (!TextUtils.isEmpty(goodsBean.getActivityTag())) {
                        fbl_label.addView(getLabelInstance().createLabelText(context, goodsBean.getActivityTag(), 1));
                    }
                    if (goodsBean.getMarketLabelList() != null
                            && goodsBean.getMarketLabelList().size() > 0) {
                        for (LikedProductBean.MarketLabelBean marketLabelBean : goodsBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_label.addView(getLabelInstance().createLabelText(context, marketLabelBean.getTitle(), 0));
                            }
                        }
                    }
                } else {
                    fbl_label.setVisibility(View.GONE);
                }

                helper.itemView.setTag(goodsBean);
            }
        };
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean goodsBean = (LikedProductBean) view.getTag();
            Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(goodsBean.getId()));
            context.startActivity(intent);
        });

        rvGoods.setAdapter(baseQuickAdapter);
        helper.itemView.setTag(userLikedProductEntity);
    }
}
