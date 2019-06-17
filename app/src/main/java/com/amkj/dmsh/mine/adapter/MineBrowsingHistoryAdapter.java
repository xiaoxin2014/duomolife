package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.MineBrowsHistoryEntity.MineBrowsHistoryBean;
import com.amkj.dmsh.mine.bean.MineBrowsHistoryEntity.MineBrowsHistoryBean.GoodsInfoListBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/10
 * version 3.2.0
 * class description:我的浏览记录
 */
public class MineBrowsingHistoryAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private final Context context;
    //    是否是当前日期第一次加载 控制top line是否展示
    private boolean isFirstLoad = false;

    public MineBrowsingHistoryAdapter(Context context, List<MultiItemEntity> browsHistoryBeanList) {
        super(browsHistoryBeanList);
        addItemType(TYPE_0, R.layout.adapter_browsing_history_product);
        addItemType(TYPE_1, R.layout.adapter_browsing_history_header);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity multiItemEntity) {
        switch (helper.getItemViewType()) {
            case TYPE_1:
                isFirstLoad = true;
                helper.itemView.setSelected(helper.getAdapterPosition() != 0);
                MineBrowsHistoryBean mineBrowsHistoryBean = (MineBrowsHistoryBean) multiItemEntity;
                CheckBox cb_browse_history_header = helper.getView(R.id.cb_browse_history_header);
                cb_browse_history_header.setVisibility(mineBrowsHistoryBean.isEditStatus() ? View.VISIBLE : View.GONE);
                cb_browse_history_header.setChecked(mineBrowsHistoryBean.isSelectStatus());
                helper.setText(R.id.tv_browse_history_header, getStrings(mineBrowsHistoryBean.getTime()))
                        .setTag(R.id.cb_browse_history_header, mineBrowsHistoryBean)
                        .addOnClickListener(R.id.cb_browse_history_header)
                        .itemView.setTag(mineBrowsHistoryBean);
                break;
            default:
                helper.getView(R.id.ll_browsing_history_product).setSelected(isFirstLoad);
                isFirstLoad = false;
                GoodsInfoListBean goodsInfoListBean = (GoodsInfoListBean) multiItemEntity;
                CheckBox cb_browse_history_product = helper.getView(R.id.cb_browse_history_product);
                cb_browse_history_product.setVisibility(goodsInfoListBean.isEditStatus() ? View.VISIBLE : View.GONE);
                cb_browse_history_product.setChecked(goodsInfoListBean.isSelectStatus());
                GlideImageLoaderUtil.loadFitCenter(context, helper.getView(R.id.iv_browse_history_product_cover), goodsInfoListBean.getProductImg());
                helper.setText(R.id.tv_browse_history_product_name, getStrings(goodsInfoListBean.getTitle()))
                        .setText(R.id.tv_browse_history_product_price, getStringsChNPrice(context, goodsInfoListBean.getPrice()))
                        .setText(R.id.tv_browse_history_product_market_price, getStringsChNPrice(context, goodsInfoListBean.getMarketPrice()))
                        .setTag(R.id.cb_browse_history_product, goodsInfoListBean)
                        .setGone(R.id.iv_product_out_tag,goodsInfoListBean.getQuantity()<1)
                        .addOnClickListener(R.id.cb_browse_history_product);
                TextView textView = helper.getView(R.id.tv_browse_history_product_market_price);
                textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
                if ((goodsInfoListBean.getGoodsTags() != null
                        && goodsInfoListBean.getGoodsTags().size() > 0)
                        || !TextUtils.isEmpty(goodsInfoListBean.getActivityTag())) {
                    fbl_market_label.setVisibility(View.VISIBLE);
                    fbl_market_label.removeAllViews();
                    if (!TextUtils.isEmpty(goodsInfoListBean.getActivityTag())) {
                        MarketLabelBean marketLabelBean = new MarketLabelBean();
                        marketLabelBean.setTitle(goodsInfoListBean.getActivityTag());
                        fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 1));
                    }
                    for (MarketLabelBean marketLabelBean : goodsInfoListBean.getGoodsTags()) {
                        if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                            fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 0));
                        }
                    }
                } else {
                    fbl_market_label.setVisibility(View.GONE);
                }
                helper.itemView.setTag(goodsInfoListBean);
                break;
        }
    }
}
