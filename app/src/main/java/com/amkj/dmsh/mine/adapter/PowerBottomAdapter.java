package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.mine.bean.PowerEntity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :权益列表适配器
 */
public class PowerBottomAdapter extends BaseQuickAdapter<PowerEntity.PowerBean, BaseViewHolder> {
    private final Context context;

    public PowerBottomAdapter(Context context, @Nullable List<PowerEntity.PowerBean> data) {
        super(R.layout.item_power_bottom, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PowerEntity.PowerBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_pic), item.getPicUrl());
        helper.setText(R.id.tv_power_name, getStrings(item.getTitle()))
                .setGone(R.id.tv_power_name, !TextUtils.isEmpty(item.getTitle()))
                .setText(R.id.tv_power_desc, getStrings(item.getSubtitle()) + ">>")
                .setGone(R.id.tv_power_desc, !TextUtils.isEmpty(item.getSubtitle()));
        helper.itemView.setOnClickListener(v -> ConstantMethod.setSkipPath(context, item.getAndroidLink(), false));
    }
}
