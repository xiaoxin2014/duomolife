package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.activity.VipPowerDetailActivity;
import com.amkj.dmsh.mine.bean.PowerEntity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :权益列表适配器
 */
public class PowerTopAdapter extends BaseQuickAdapter<PowerEntity.PowerBean, BaseViewHolder> {
    private final Context context;

    public PowerTopAdapter(Context context, @Nullable List<PowerEntity.PowerBean> data) {
        super(R.layout.item_power_top, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PowerEntity.PowerBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.iv_pic), item.getIcon(), AutoSizeUtils.mm2px(context, 88));
        helper.setText(R.id.tv_power_name, getStrings(item.getTitle()));
        helper.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VipPowerDetailActivity.class);
            intent.putExtra("position", String.valueOf(helper.getAdapterPosition()));
            context.startActivity(intent);
        });
    }
}
