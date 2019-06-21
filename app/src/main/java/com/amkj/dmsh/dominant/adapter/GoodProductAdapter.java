package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.AD_COVER;
import static com.amkj.dmsh.constant.ConstantVariable.PRODUCT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:好物
 */

public class GoodProductAdapter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Activity context;

    public GoodProductAdapter(Activity context, List<LikedProductBean> goodsProList) {
        super(goodsProList);
        addItemType(PRODUCT, R.layout.item_commual_goods_2x);//普通商品
        addItemType(AD_COVER, R.layout.item_commual_cover_pic_2x);//封面图片
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        switch (likedProductBean.getItemType()) {
            //封面图片
            case AD_COVER:
                ImageView iv_quality_good_product_ad = helper.getView(R.id.iv_quality_good_product_ad);
                GlideImageLoaderUtil.loadCenterCrop(context, iv_quality_good_product_ad, getStrings(likedProductBean.getPicUrl()));
                //点击广告封面图片
                helper.itemView.setOnClickListener(v -> {
                    /**
                     * 3.1.9 加入好物广告统计
                     */
                    adClickTotal(context, likedProductBean.getId());
                    if (!TextUtils.isEmpty(likedProductBean.getAndroidLink())) {
                        setSkipPath(context, getStrings(likedProductBean.getAndroidLink()), false);
                    } else {
                        Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                        context.startActivity(intent);
                    }
                });
                break;
            //普通商品（product类型）
            case PRODUCT:
                GlideImageLoaderUtil.loadThumbCenterCrop(context, helper.getView(R.id.iv_qt_pro_img)
                        , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), true);
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_qt_pro_descrip, getStrings(likedProductBean.getSubtitle()))
                        .setText(R.id.tv_qt_pro_name, !TextUtils.isEmpty(likedProductBean.getName()) ?
                                getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                        .setText(R.id.tv_qt_pro_price, ConstantMethod.getRmbFormat(context, likedProductBean.getPrice()))
                        .addOnClickListener(R.id.iv_pro_add_car).setTag(R.id.iv_pro_add_car, likedProductBean);
                //点击商品进入详情
                helper.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    context.startActivity(intent);
                });

                //加入购物车
                helper.getView(R.id.iv_pro_add_car).setOnClickListener(v -> {
                    ((BaseActivity) context).loadHud.show();
                    if (userId > 0) {
                        BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                        baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                        baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                        baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                        baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                        ConstantMethod constantMethod = new ConstantMethod();
                        constantMethod.addShopCarGetSku(context, baseAddCarProInfoBean, ((BaseActivity) context).loadHud);
                    } else {
                        ((BaseActivity) context).loadHud.dismiss();
                        getLoginStatus(context);
                    }
                });
                FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
                if (!TextUtils.isEmpty(likedProductBean.getActivityTag()) || (likedProductBean.getMarketLabelList() != null
                        && likedProductBean.getMarketLabelList().size() > 0)) {
                    fbl_market_label.setVisibility(View.VISIBLE);
                    fbl_market_label.removeAllViews();
                    //活动标签（仅有一个）
                    if (!TextUtils.isEmpty(likedProductBean.getActivityTag())) {
                        fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, likedProductBean.getActivityTag(), 1));
                    }
                    //营销标签(可以有多个)
                    if (likedProductBean.getMarketLabelList() != null
                            && likedProductBean.getMarketLabelList().size() > 0) {
                        for (MarketLabelBean marketLabelBean : likedProductBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 0));
                            }
                        }
                    }

                    //限制标签最多显示一行，超出屏幕外的自动移除
                    if (fbl_market_label.getChildCount() > 0) {
                        ViewTreeObserver observer = fbl_market_label.getViewTreeObserver();
                        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int width = fbl_market_label.getMeasuredWidth();
                                int max = helper.itemView.getMeasuredWidth();
                                if (width >= max) {
                                    fbl_market_label.removeViewAt(fbl_market_label.getChildCount() - 1);
                                } else {
                                    fbl_market_label.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            }
                        });
                    }
                } else {
                    fbl_market_label.setVisibility(View.GONE);
                }
                helper.itemView.setTag(likedProductBean);
                break;

        }
    }
}
