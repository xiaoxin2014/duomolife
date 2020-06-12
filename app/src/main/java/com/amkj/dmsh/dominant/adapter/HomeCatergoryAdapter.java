package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;

/**
 * Created by xiaoxin on 2019/4/16 0016
 * Version:v4.0.0
 * ClassDescription :新版首页分类商品适配器
 */

public class HomeCatergoryAdapter extends BaseQuickAdapter<UserLikedProductEntity, BaseViewHolder> {
    private final Activity mContext;
    private CBViewHolderCreator cbViewHolderCreator;

    public HomeCatergoryAdapter(Activity context, List<UserLikedProductEntity> productList) {
        super(R.layout.item_home_catergory, productList);
        this.mContext = context;
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
                        return new CommunalAdHolderView(itemView, mContext, true);
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

            convenientBanner.setPages((LifecycleOwner) mContext, cbViewHolderCreator, adBeanList)
                    .startTurning(getShowNumber("5") * 1000);
        } else {
            convenientBanner.setVisibility(View.GONE);
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
        CatergoryGoodsAdapter catergoryGoodsAdapter = new CatergoryGoodsAdapter(mContext, userLikedProductEntity.getGoodsList());
        catergoryGoodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean goodsBean = (LikedProductBean) view.getTag();
            if (goodsBean != null) {
                Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(goodsBean.getId()));
                mContext.startActivity(intent);
            }
        });

        rvGoods.setAdapter(catergoryGoodsAdapter);
        helper.itemView.setTag(userLikedProductEntity);
    }
}
