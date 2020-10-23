package com.amkj.dmsh.time.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.time.bean.BrandEntity.BrandBean.BrandProductBean;
import com.amkj.dmsh.user.bean.MarketLabelBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 * ClassDescription :团购单品适配器
 */
public class SingleProductAdapter extends BaseQuickAdapter<BrandProductBean, BaseViewHolder> {
    private final Context context;

    public SingleProductAdapter(Context context, @Nullable List<BrandProductBean> data) {
        super(R.layout.item_single_product, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandProductBean item) {
        if (item == null) return;
        ImageView ivCover = helper.getView(R.id.iv_cover);
        TextView tvMarketPrice = helper.getView(R.id.tv_market_price);
        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvMarketPrice.getPaint().setAntiAlias(true);
        String price = "团购价¥" + item.getPrice();
        GlideImageLoaderUtil.loadSquareImg(context, ivCover, item.getPicUrl(), item.getWaterRemark(), AutoSizeUtils.mm2px(mAppContext, 360));
        helper.setText(R.id.tv_desc, getStrings(item.getSubtitle()))
                .setGone(R.id.tv_desc, !TextUtils.isEmpty(item.getSubtitle()))
                .setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_price, getSpannableString(price, price.indexOf("¥"), price.length(), 1.33f, ""))
                .setText(R.id.tv_market_price, getStringsChNPrice(context, item.getMarketPrice()));
        helper.itemView.setOnClickListener(v -> skipProductUrl(context, 0, item.getId()));
        FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
        fbl_market_label.removeAllViews();

        List<MarketLabelBean> marketLabelList = item.getMarketLabelList();
        if (marketLabelList != null && marketLabelList.size() > 0) {
            for (MarketLabelBean marketLabelBean : marketLabelList) {
                if (marketLabelBean.getLabelCode() == 0) {//优惠券标签
                    fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 3));
                } else {//营销标签
                    fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 0));
                }
            }
        }

        fbl_market_label.setVisibility(fbl_market_label.getChildCount() > 0 ? View.VISIBLE : View.GONE);
        helper.itemView.setTag(item);
    }
}
