package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.SpringSaleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/3
 * class description:请输入类描述
 */

public class SpringSaleHorizonAdapter extends BaseQuickAdapter<SpringSaleBean, BaseViewHolderHelper> {
    private final Context context;

    public SpringSaleHorizonAdapter(Context context, List<SpringSaleBean> springSaleBeanList) {
        super(R.layout.adapter_layout_spring_hori, springSaleBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, SpringSaleBean springSaleBean) {
        if (springSaleBean.getQuantity() < 1) {
            helper.setGone(R.id.iv_spring_hori_sale_tag_out, true);
        } else {
            helper.setGone(R.id.iv_spring_hori_sale_tag_out, false);
        }
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_springSale_hori_product), springSaleBean.getPicUrl());
        if (!TextUtils.isEmpty(springSaleBean.getMaxPrice())
                && getFloatNumber(springSaleBean.getPrice()) < getFloatNumber(springSaleBean.getMaxPrice())) {
            helper.setText(R.id.tv_spring_horizon_pro_price, "￥" + springSaleBean.getPrice() + "+");
        } else {
            helper.setText(R.id.tv_spring_horizon_pro_price, "￥" + springSaleBean.getPrice());
        }
        helper.setText(R.id.tv_spring_horizon_pro_name, getStrings(springSaleBean.getName()));
        helper.itemView.setTag(springSaleBean);
    }
}
