package com.amkj.dmsh.dominant.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.bean.QualityGoodProductEntity.Attribute;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.dominant.fragment.QualityOldFragment.updateCarNum;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;



/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:好物
 */

public class QualityGoodNewProAdapter extends BaseQuickAdapter<Attribute, BaseViewHolder> {
    private final BaseActivity context;

    public QualityGoodNewProAdapter(BaseActivity context, List<Attribute> goodsProList) {
        super(R.layout.adapter_layout_qt_pro, goodsProList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Attribute attribute) {
        LinearLayout ll_quality_product = helper.getView(R.id.ll_quality_product);
        ImageView iv_quality_good_product_ad = helper.getView(R.id.iv_quality_good_product_ad);
        ll_quality_product.setVisibility(View.GONE);
        iv_quality_good_product_ad.setVisibility(View.GONE);
        if (attribute == null) {
            return;
        }
        switch (getStrings(attribute.getObjectType())) {
            case "product":
                ll_quality_product.setVisibility(View.VISIBLE);
                LikedProductBean likedProductBean = (LikedProductBean) attribute;
                GlideImageLoaderUtil.loadThumbCenterCrop(context, (ImageView) helper.getView(R.id.iv_qt_pro_img)
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
                    context.loadHud.show();
                    if (userId > 0) {
                        LikedProductBean likedProductBean1 = (LikedProductBean) attribute;
                        BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                        baseAddCarProInfoBean.setProductId(likedProductBean1.getId());
                        baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean1.getActivityCode()));
                        baseAddCarProInfoBean.setProName(getStrings(likedProductBean1.getName()));
                        baseAddCarProInfoBean.setProPic(getStrings(likedProductBean1.getPicUrl()));
                        ConstantMethod constantMethod = new ConstantMethod();
                        constantMethod.addShopCarGetSku(context, baseAddCarProInfoBean, context.loadHud);
                        constantMethod.setAddOnCarListener(() -> EventBus.getDefault().post(new EventMessage(updateCarNum, updateCarNum)));
                    } else {
                        context.loadHud.dismiss();
                        getLoginStatus(context);
                    }
                });
                FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
                if (!TextUtils.isEmpty(likedProductBean.getActivityTag()) || (likedProductBean.getMarketLabelList() != null
                        && likedProductBean.getMarketLabelList().size() > 0)) {
                    fbl_market_label.setVisibility(View.VISIBLE);
                    fbl_market_label.removeAllViews();
                    if (!TextUtils.isEmpty(likedProductBean.getActivityTag())) {
                        fbl_market_label.addView(getLabelInstance().createLabelText(context, likedProductBean.getActivityTag(), 1));
                    }
                    if (likedProductBean.getMarketLabelList() != null
                            && likedProductBean.getMarketLabelList().size() > 0) {
                        for (MarketLabelBean marketLabelBean : likedProductBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_market_label.addView(getLabelInstance().createLabelText(context, marketLabelBean.getTitle(), 0));
                            }
                        }
                    }
                } else {
                    fbl_market_label.setVisibility(View.GONE);
                }
                break;
            case "ad":
                CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) attribute;
                iv_quality_good_product_ad.setVisibility(View.VISIBLE);
                GlideImageLoaderUtil.loadCenterCrop(context, iv_quality_good_product_ad, getStrings(communalADActivityBean.getPicUrl()));
                //点击广告封面图片
                helper.itemView.setOnClickListener(v -> {
                    /**
                     * 3.1.9 加入好物广告统计
                     */
                    adClickTotal(context, communalADActivityBean.getId());
                    setSkipPath(context, getStrings(communalADActivityBean.getAndroidLink()), false);
                });
                break;
        }
        helper.itemView.setTag(attribute);
    }
}
