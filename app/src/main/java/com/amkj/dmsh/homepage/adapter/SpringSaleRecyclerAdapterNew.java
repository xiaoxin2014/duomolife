package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.SpringSaleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import java.util.List;

import cn.iwgang.countdownview.CountdownView;

import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/2
 * class description:限时特惠商品
 */
public class SpringSaleRecyclerAdapterNew extends BaseMultiItemQuickAdapter<SpringSaleBean, BaseViewHolderHelper> {
    private final Context context;

    public SpringSaleRecyclerAdapterNew(Context context, List<SpringSaleBean> saleTimeTotalList) {
        super(saleTimeTotalList);
        this.context = context;
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_promotion_pro_item);
        addItemType(ConstantVariable.TYPE_1, R.layout.adapter_promotion_foreshow_date_header);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, SpringSaleBean springSaleBean) {
        switch (helper.getItemViewType()) {
//            单品
            case ConstantVariable.TYPE_0:
                if (springSaleBean.getQuantity() < 1) {
                    helper.setGone(R.id.img_spring_sale_tag_out, true);
                } else {
                    helper.setGone(R.id.img_spring_sale_tag_out, false);
                }
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_springSale_product), springSaleBean.getPicUrl());
                if (!TextUtils.isEmpty(springSaleBean.getMaxPrice())
                        && getFloatNumber(springSaleBean.getPrice()) < getFloatNumber(springSaleBean.getMaxPrice())) {
                    helper.setText(R.id.tv_product_duomolife_price, getStringsChNPrice(context,springSaleBean.getPrice()) + "+");
                } else {
                    helper.setText(R.id.tv_product_duomolife_price, getStringsChNPrice(context,springSaleBean.getPrice()));
                }
                helper.setText(R.id.tv_springSale_introduce, getStrings(springSaleBean.getName()));

                ImageView iv_pro_time_warm = helper.getView(R.id.iv_pro_time_warm);
//                if (!isTimeStart(springSaleBean)) {
//                    helper.setGone(R.id.iv_pro_time_warm, true);
//                    iv_pro_time_warm.setSelected(springSaleBean.getIsRemind() != 0);
//                } else {
//                    helper.setGone(R.id.iv_pro_time_warm, false);
//                }
                helper.addOnClickListener(R.id.iv_pro_time_warm).setTag(R.id.iv_pro_time_warm, springSaleBean);
                springSaleBean.setTimeObject(null);
                helper.itemView.setTag(springSaleBean);
                break;
//            品牌团
            case ConstantVariable.TYPE_1:
                helper.setIsRecyclable(false);
                CountdownView ct_time_communal_show_bg = helper.getView(R.id.ct_time_communal_show_bg);
                springSaleBean.setTimeObject(ct_time_communal_show_bg);
                break;
        }
    }
}
