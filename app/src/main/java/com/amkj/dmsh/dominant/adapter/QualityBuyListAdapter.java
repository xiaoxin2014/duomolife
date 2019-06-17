package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.QualityBuyListEntity.QualityBuyListBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/26
 * class description:必买清单商品
 */

public class QualityBuyListAdapter extends BaseQuickAdapter<QualityBuyListBean, BaseViewHolder> {
    private final Context context;

    public QualityBuyListAdapter(Context context, List<QualityBuyListBean> qualityBuyListBeanList) {
        super(R.layout.adapter_ql_shop_buy_recommend, qualityBuyListBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityBuyListBean qualityBuyListBean) {
        GlideImageLoaderUtil.loadFitCenter(context, (ImageView) helper.getView(R.id.iv_ql_bl_product), qualityBuyListBean.getPicUrl());
        helper.setText(R.id.tv_ql_bl_pro_name, getStrings(qualityBuyListBean.getName()))
                .setText(R.id.tv_ql_bl_product_recommend, getStrings(qualityBuyListBean.getRecommendReason()))
                .setText(R.id.tv_ql_bl_pro_price, getStringsChNPrice(context,qualityBuyListBean.getPrice()))
                .setGone(R.id.iv_com_pro_tag_out, qualityBuyListBean.getQuantity() < 1 )
                .addOnClickListener(R.id.iv_ql_bl_add_car).setTag(R.id.iv_ql_bl_add_car, qualityBuyListBean);
        ImageView iv_ql_bl_add_car = helper.getView(R.id.iv_ql_bl_add_car);
        iv_ql_bl_add_car.setSelected(qualityBuyListBean.isInCart());
        FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
        if(qualityBuyListBean.getMarketLabelList()!=null
                &&qualityBuyListBean.getMarketLabelList().size()>0){
            fbl_market_label.setVisibility(View.VISIBLE);
            fbl_market_label.removeAllViews();
            if(qualityBuyListBean.getMarketLabelList()!=null
                    &&qualityBuyListBean.getMarketLabelList().size()>0){
                for (MarketLabelBean marketLabelBean:qualityBuyListBean.getMarketLabelList()) {
                    if(!TextUtils.isEmpty(marketLabelBean.getTitle())){
                        fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context,marketLabelBean.getTitle(),0));
                    }
                }
            }
        }else{
            fbl_market_label.setVisibility(View.GONE);
        }
        helper.itemView.setTag(qualityBuyListBean);
    }
}
