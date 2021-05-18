package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :会员最爱买商品
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
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_pic), item.getPicUrl());
        helper.setText(R.id.tv_name, getStrings(item.getName()))
                .setGone(R.id.iv_com_pro_tag_out, item.getQuantity() < 1)
                .setText(R.id.tv_vip_price, "¥" + item.getVipPrice())
                .setText(R.id.tv_price, "¥" + item.getPrice())
                .setGone(R.id.ll_price, !item.getPrice().equals(item.getVipPrice()));
        TextView tvPrice = helper.getView(R.id.tv_price);
        tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //删除线
        helper.itemView.setOnClickListener(v -> skipProductUrl(context, 1, item.getId()));
        helper.itemView.setTag(item);
    }
}
