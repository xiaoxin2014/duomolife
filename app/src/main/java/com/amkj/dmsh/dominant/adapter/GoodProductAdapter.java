package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dao.AddClickDao;
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
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.skipGroupDetail;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.AD_COVER;
import static com.amkj.dmsh.constant.ConstantVariable.PICTURE;
import static com.amkj.dmsh.constant.ConstantVariable.PRODUCT;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;


/**
 * Created by xiaoxin on 2019/7/12
 * Version:v4.0.0
 * ClassDescription :一行2个商品通用适配器
 */

public class GoodProductAdapter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolder> {
    private Activity context;
    private int type;//0默认类型 1富文本 2多么会员价

    public GoodProductAdapter(Activity context, List<LikedProductBean> goodsProList) {
        this(context, goodsProList, 0);
    }

    public GoodProductAdapter(Activity context, List<LikedProductBean> goodsProList, int type) {
        super(goodsProList);
        addItemType(PRODUCT, R.layout.item_commual_goods_2x);//普通商品
        addItemType(PICTURE, R.layout.item_commual_pic_2x);//普通图片
        addItemType(AD_COVER, R.layout.item_commual_cover_2x);//封面图片
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        switch (likedProductBean.getItemType()) {
            //封面图片
            case PICTURE:
            case AD_COVER:
                ImageView iv_quality_good_product_ad = helper.getView(R.id.iv_quality_good_product_ad);
                GlideImageLoaderUtil.loadCenterCrop(context, iv_quality_good_product_ad, getStrings(likedProductBean.getPicUrl()));
                helper.itemView.setTag(R.id.iv_tag, likedProductBean);
                break;
            //普通商品（product类型）
            case PRODUCT:
                GlideImageLoaderUtil.loadSquareImg(context, helper.getView(R.id.iv_qt_pro_img)
                        , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), AutoSizeUtils.mm2px(mAppContext, 350));
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_qt_pro_descrip, getStrings(likedProductBean.getSubtitle()))
                        .setText(R.id.tv_qt_pro_name, !TextUtils.isEmpty(likedProductBean.getName()) ?
                                getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                        .setText(R.id.tv_qt_pro_price, ConstantMethod.getRmbFormat(context, likedProductBean.getPrice()))
                        .addOnClickListener(R.id.iv_pro_add_car).setTag(R.id.iv_pro_add_car, likedProductBean)
                        .setGone(R.id.iv_pro_add_car, type != 2)
                        .setText(R.id.tv_save, getStringsFormat(context, R.string.vip_save_money, likedProductBean.getVipReduce()))
                        .setGone(R.id.ll_save, type == 2 && !TextUtils.isEmpty(likedProductBean.getVipReduce()));

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
                if (likedProductBean.getMarketLabelList() != null && likedProductBean.getMarketLabelList().size() > 0) {
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
        //点击商品进入详情
        if (type != 1) {
            helper.itemView.setOnClickListener(v -> {
                //3.1.9 加入好物广告统计
                if (likedProductBean.getItemType() == AD_COVER) {
                    AddClickDao.adClickTotal(context, likedProductBean.getAndroidLink(), likedProductBean.getId(), true);
                } else {
                    if (!TextUtils.isEmpty(likedProductBean.getGpInfoId())) {
                        skipGroupDetail(context, likedProductBean.getGpInfoId());
                    } else {
                        skipProductUrl(context, likedProductBean.getType_id(), likedProductBean.getId(), likedProductBean.getAndroidLink());
                    }
                }
            });
        }
    }
}
