package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import static com.amkj.dmsh.constant.ConstantMethod.isVip;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;

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
        ImageView ivCover = helper.getView(R.id.iv_pic);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivCover.getLayoutParams();
        if (isVip()) {
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            layoutParams.setMargins(AutoSizeUtils.mm2px(context, 16), 0, AutoSizeUtils.mm2px(context, 16), AutoSizeUtils.mm2px(context, 21));
        }
        ivCover.setLayoutParams(layoutParams);
        GlideImageLoaderUtil.loadImage(context, ivCover, item.getPicUrl());

        helper.setText(R.id.tv_power_name, getStrings(item.getTitle()))
                .setGone(R.id.tv_power_name, !TextUtils.isEmpty(item.getTitle()))
                .setText(R.id.tv_power_desc, getStrings(item.getSubtitle()) + ">>")
                .setGone(R.id.tv_power_desc, !TextUtils.isEmpty(item.getSubtitle()))
                .setBackgroundRes(R.id.ll_power_bottom, isVip() ? R.drawable.shape_exclusive_coupon_bg : R.drawable.border_rect_10dp_bg_white);
        //点击封面跳转权益描述
        helper.getView(R.id.iv_pic).setOnClickListener(v -> {
            skipPowerDetail(item.getPosition());
        });
        //点击副标题
        helper.getView(R.id.tv_power_desc).setOnClickListener(v -> {
            //会员跳转指定链接
            if (isVip() || item.getTitle().contains("分享")) {
                setSkipPath(context, item.getAndroidLink(), false);
            } else {
                //非会员跳转权益描述
                skipPowerDetail(item.getPosition());
            }
        });
    }

    private void skipPowerDetail(String position) {
        Intent intent = new Intent(context, VipPowerDetailActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
