package com.amkj.dmsh.dominant.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/9/24
 * Version:v4.7.0
 * ClassDescription :会员专区侧滑商品列表
 */

public class VipSlideProAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Context context;

    public VipSlideProAdapter(Context context, List<LikedProductBean> welfareProductList) {
        super(R.layout.adapter_wel_slide_pro, welfareProductList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        GlideImageLoaderUtil.loadHeaderImg(context, helper.getView(R.id.iv_wel_slide_pro), likedProductBean.getPicUrl());
        helper.setText(R.id.tv_wel_slide_pro_name, getStrings(likedProductBean.getName()))
                .setText(R.id.tv_wel_slide_pro_price, "¥ " + likedProductBean.getPrice());
        helper.itemView.setTag(likedProductBean);
    }
}
