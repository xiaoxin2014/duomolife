package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :每周会员特价商品
 */
public class VipFavoriteAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Context context;

    public VipFavoriteAdapter(Context context, @Nullable List<LikedProductBean> data) {
        super(R.layout.item_vip_favorite, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean item) {
        if (item == null) return;
        TextView view = helper.getView(R.id.tv_market_price);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        view.getPaint().setAntiAlias(true);
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_pic), item.getPicUrl());
        helper.setText(R.id.tv_name, getStrings(item.getName()))
                .setGone(R.id.iv_com_pro_tag_out, item.getQuantity() < 1)
                .setText(R.id.tv_price, "¥" + item.getPrice())
                .setText(R.id.tv_market_price, "¥" + item.getMarketPrice());
        helper.itemView.setOnClickListener(v -> skipProductUrl(context, 1, item.getId()));
        helper.itemView.setTag(item);
    }
}
