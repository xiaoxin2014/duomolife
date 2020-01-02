package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/12/26
 * Version:v4.4.1
 * ClassDescription :更多拼团商品
 */
public class GroupRecommendAdapter extends BaseQuickAdapter<QualityGroupBean, BaseViewHolder> {

    private final Activity mContext;

    public GroupRecommendAdapter(Activity context, @Nullable List<QualityGroupBean> data) {
        super(R.layout.item_scroll_detail_goods_recommend, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityGroupBean item) {
        if (item != null) {
            GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover_right), item.getCoverImage());
            helper.setText(R.id.tv_price_right, getRmbFormat(mContext, item.getGpPrice()))
                    .setGone(R.id.tv_market_price_right, !TextUtils.isEmpty(item.getPrice()))
                    .setText(R.id.tv_market_price_right, "¥" + getStrings(item.getPrice()));
            ((TextView) helper.getView(R.id.tv_market_price_right)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            ((TextView) helper.getView(R.id.tv_market_price_right)).getPaint().setAntiAlias(true);
            helper.itemView.setTag(item);
        }
    }
}
