package com.amkj.dmsh.mine.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.amkj.dmsh.mine.bean.VipSettleInfoEntity.VipSettleInfoBean.CardListBean.GiftListBean;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :开卡礼商品适配器
 */
public class VipGiftAdapter extends BaseQuickAdapter<GiftListBean, BaseViewHolder> {
    private final Context context;

    public VipGiftAdapter(Context context, @Nullable List<GiftListBean> data) {
        super(R.layout.item_vip_present, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftListBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_pic), item.getProductImg());
        helper.setText(R.id.tv_name, item.getProductName());
        helper.getView(R.id.checkbox).setSelected(item.isSelected());
        helper.itemView.setTag(item);
    }
}
