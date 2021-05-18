package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.user.bean.MarketLabelBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.isVip;
import static com.amkj.dmsh.constant.ConstantMethod.skipGroupDetail;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;

public class ProNoShopCarAdapter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Activity context;

    public ProNoShopCarAdapter(Activity context, List<LikedProductBean> productSearList) {
        super(productSearList);
        addItemType(TYPE_0, R.layout.item_commual_goods_2x);
//        头部标题栏
        addItemType(TYPE_1, R.layout.adapter_ql_goods_pro_header);
//        搜索无结果
        addItemType(TYPE_2, R.layout.adapter_search_pro_empty);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        switch (helper.getItemViewType()) {
            case TYPE_1:
                helper.setText(R.id.tv_pro_title, "- 同类热销商品 -");
                break;
            case TYPE_2:
                helper.setText(R.id.tv_search_empty_title, String.format(context.getString(R.string.search_pro_empty), getStrings(likedProductBean.getTitle())))
                        .addOnClickListener(R.id.tv_search_empty_more);
                break;
            case TYPE_0:
                GlideImageLoaderUtil.loadSquareImg(context, (ImageView) helper.getView(R.id.iv_qt_pro_img)
                        , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), AutoSizeUtils.mm2px(mAppContext, 350));
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_qt_pro_descrip, getStrings(likedProductBean.getSubtitle()))
                        .setText(R.id.tv_qt_pro_name, !TextUtils.isEmpty(likedProductBean.getName()) ?
                                getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                        .addOnClickListener(R.id.iv_pro_add_car).setTag(R.id.iv_pro_add_car, likedProductBean);

                //商品价格
                TextView tvPrice = helper.getView(R.id.tv_qt_pro_price);
                ImageView ivLogoFront = helper.getView(R.id.iv_vip_logo_front);
                TextView tvOldPrice = helper.getView(R.id.tv_old_price);
                tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //删除线
                TextView tvVipPrice = helper.getView(R.id.tv_vip_price);
                ImageView ivLogo = helper.getView(R.id.iv_vip_logo);
                LinearLayout llVipPrice = helper.getView(R.id.ll_vip_price);
                String activityCode = likedProductBean.getActivityCode();
                if (!TextUtils.isEmpty(activityCode) && activityCode.contains("XSG")) {
                    tvOldPrice.setVisibility(!TextUtils.isEmpty(likedProductBean.getOldPrice()) ? View.VISIBLE : View.GONE);
                    llVipPrice.setVisibility(View.GONE);
                    ivLogoFront.setVisibility(View.GONE);
                    tvPrice.setText(getStringsChNPrice(context, likedProductBean.getPrice()));
                    tvOldPrice.setText(getStringsChNPrice(context, likedProductBean.getOldPrice()));
                } else {
                    tvOldPrice.setVisibility(View.GONE);
                    llVipPrice.setVisibility(!TextUtils.isEmpty(likedProductBean.getVipPrice()) ? View.VISIBLE : View.GONE);
                    if (isVip() && !TextUtils.isEmpty(likedProductBean.getVipPrice())) {
                        tvPrice.setText(getStringsChNPrice(context, likedProductBean.getVipPrice()));
                        ivLogoFront.setVisibility(View.VISIBLE);
                        tvVipPrice.setText(getStringsChNPrice(context, likedProductBean.getPrice()));
                        ivLogo.setVisibility(View.GONE);
                    } else {
                        tvPrice.setText(getStringsChNPrice(context, likedProductBean.getPrice()));
                        ivLogoFront.setVisibility(View.GONE);
                        tvVipPrice.setText(getStringsChNPrice(context, likedProductBean.getVipPrice()));
                        ivLogo.setVisibility(View.VISIBLE);
                    }
                }

                //加入购物车
                if (likedProductBean.getType_id() == 1) {//只有自营商品才能加入购物车
                    helper.getView(R.id.iv_pro_add_car).setOnClickListener(v -> {
                        BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                        baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                        baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                        baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                        baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                        addShopCarGetSku(context, baseAddCarProInfoBean);
                    });
                }

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(likedProductBean.getGpInfoId())) {
                            skipGroupDetail(context, likedProductBean.getGpInfoId());
                        } else {
                            skipProductUrl(context, likedProductBean.getType_id(), likedProductBean.getId());
                        }
                    }
                });
                FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
                fbl_market_label.removeAllViews();

                //活动标签（仅有一个）
                if (!TextUtils.isEmpty(likedProductBean.getActivityTag())) {
                    fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, likedProductBean.getActivityTag(), 1));
                }

                //会员标签
                if (!TextUtils.isEmpty(likedProductBean.getVipTag())) {
                    fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, likedProductBean.getVipTag(), 0));
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
                fbl_market_label.setVisibility(fbl_market_label.getChildCount() > 0 ? View.VISIBLE : View.GONE);
                helper.itemView.setTag(likedProductBean);
                break;
        }
    }
}
