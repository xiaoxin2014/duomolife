package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.user.bean.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_TOPIC;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_3;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/12/29
 * version 3.6
 * class description: 商品热门 专题推荐
 */
public class ShopRecommendHotTopicAdapter extends BaseMultiItemQuickAdapter<ShopRecommendHotTopicBean, BaseViewHolder> {
    private final Context context;

    public ShopRecommendHotTopicAdapter(Context context, List<ShopRecommendHotTopicBean> data) {
        super(data);
        this.context = context;
//        商品
        addItemType(TYPE_0, R.layout.adapter_layout_qt_pro);
//        热门专题
        addItemType(TYPE_1, R.layout.adapter_dml_optimized_sel);
//        头部标题栏
        addItemType(TYPE_2, R.layout.adapter_ql_goods_pro_header);
//        底栏更多推荐
        addItemType(TYPE_3, R.layout.adapter_layout_communal_more);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopRecommendHotTopicBean shopRecommendHotTopicBean) {
        switch (helper.getItemViewType()) {
            case TYPE_1:
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_cover_detail_bg), shopRecommendHotTopicBean.getPicUrl());
                helper.setText(R.id.tv_opt_sel_title, getStrings(shopRecommendHotTopicBean.getTitle()))
                        .setGone(R.id.tv_opt_sel_subtitle, false);
                helper.itemView.setTag(shopRecommendHotTopicBean);
                break;
            case TYPE_2:
                if (PRO_TOPIC.equals(shopRecommendHotTopicBean.getType())) {
                    helper.setText(R.id.tv_pro_title, "- 热门专题 -");
                } else if (PRO_COMMENT.equals(shopRecommendHotTopicBean.getType())) {
                    helper.setText(R.id.tv_pro_title, "- 同类热销商品 -");
                }
                helper.itemView.setTag(shopRecommendHotTopicBean);
                break;
            case TYPE_3:
                if (PRO_TOPIC.equals(shopRecommendHotTopicBean.getType())) {
                    helper.setText(R.id.tv_communal_more, "- 更多相关专题推荐 -");
                } else if (PRO_COMMENT.equals(shopRecommendHotTopicBean.getType())) {
                    helper.setText(R.id.tv_communal_more, "- 更多同类商品推荐 -");
                }
                helper.itemView.setTag(shopRecommendHotTopicBean);
                break;
            default:
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_qt_pro_img)
                        , shopRecommendHotTopicBean.getPicUrl());
                helper.setGone(R.id.iv_com_pro_tag_out, shopRecommendHotTopicBean.getQuantity() < 1)
                        .setText(R.id.tv_qt_pro_descrip, getStrings(shopRecommendHotTopicBean.getTitle()))
                        .setText(R.id.tv_qt_pro_price, "¥" + shopRecommendHotTopicBean.getPrice())
                        .setText(R.id.tv_qt_pro_name, getStrings(shopRecommendHotTopicBean.getSubtitle()))
                        .setGone(R.id.iv_pro_add_car, false);
                FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
                if(shopRecommendHotTopicBean.getMarketLabelList()!=null
                        &&shopRecommendHotTopicBean.getMarketLabelList().size()>0){
                    fbl_market_label.setVisibility(View.VISIBLE);
                    fbl_market_label.removeAllViews();
                        for (MarketLabelBean marketLabelBean:shopRecommendHotTopicBean.getMarketLabelList()) {
                            if(!TextUtils.isEmpty(marketLabelBean.getTitle())){
                                fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context,marketLabelBean.getTitle(),0));
                            }
                        }
                }else{
                    fbl_market_label.setVisibility(View.GONE);
                }
                helper.itemView.setTag(shopRecommendHotTopicBean);
                break;
        }
    }
}
