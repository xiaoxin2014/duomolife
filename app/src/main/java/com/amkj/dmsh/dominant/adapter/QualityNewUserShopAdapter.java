package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/5
 * class description:新人专享
 */

public class QualityNewUserShopAdapter extends BaseMultiItemQuickAdapter<UserLikedProductEntity.LikedProductBean, BaseViewHolder> {
    private final Context context;

    public QualityNewUserShopAdapter(Context context, List<UserLikedProductEntity.LikedProductBean> qualityNewUserShopList) {
        super(qualityNewUserShopList);
        addItemType(TYPE_0, R.layout.item_commual_goods_3x);
        addItemType(TYPE_1, R.layout.adapter_new_user_header);//头部标题栏
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserLikedProductEntity.LikedProductBean likedProductBean) {
        if (helper.getItemViewType() == TYPE_0) {
            String waterRemark = likedProductBean.getWaterRemark();
            String goodsUrl = likedProductBean.getPicUrl();
            if (!TextUtils.isEmpty(waterRemark)) {
                goodsUrl = GlideImageLoaderUtil.getWaterMarkImgUrl(likedProductBean.getPicUrl(), likedProductBean.getWaterRemark());
            }
            GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_goods_pic), goodsUrl);
            helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                    .setText(R.id.tv_price, ConstantMethod.getRmbFormat(context, likedProductBean.getPrice()))
                    .setText(R.id.tv_name, getStrings(likedProductBean.getName()));
            FlexboxLayout fbl_label = helper.getView(R.id.fbl_label);
            if (!TextUtils.isEmpty(likedProductBean.getActivityTag()) || (likedProductBean.getMarketLabelList() != null
                    && likedProductBean.getMarketLabelList().size() > 0)) {
                fbl_label.setVisibility(View.VISIBLE);
                fbl_label.removeAllViews();
                if (!TextUtils.isEmpty(likedProductBean.getActivityTag())) {
                    fbl_label.addView(ProductLabelCreateUtils.createLabelText(context, likedProductBean.getActivityTag(), 1));
                }
                if (likedProductBean.getMarketLabelList() != null
                        && likedProductBean.getMarketLabelList().size() > 0) {
                    for (UserLikedProductEntity.LikedProductBean.MarketLabelBean marketLabelBean : likedProductBean.getMarketLabelList()) {
                        if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                            fbl_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 0));
                            if (fbl_label.getChildCount() >= 2) break;
                        }
                    }
                }
            } else {
                fbl_label.setVisibility(View.GONE);
            }

            helper.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                context.startActivity(intent);
            });
        } else if (helper.getItemViewType() == TYPE_1) {
            ImageView imageView = helper.getView(R.id.iv_type);
            imageView.setImageResource(likedProductBean.getType_id());
        }
    }
}
