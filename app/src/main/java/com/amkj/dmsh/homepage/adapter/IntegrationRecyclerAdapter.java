package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.IntegrationProEntity.IntegrationBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;


/**
 * Created by atd48 on 2016/6/29.
 */
public class IntegrationRecyclerAdapter extends BaseMultiItemQuickAdapter<IntegrationBean, BaseViewHolder> {
    private final Context context;

    public IntegrationRecyclerAdapter(Context context, List<IntegrationBean> integrationBeanList) {
        super(integrationBeanList);
        addItemType(TYPE_0, R.layout.adapter_layout_attendance_integral_list);
        addItemType(TYPE_1, R.layout.layout_integral_pro_header);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegrationBean integrationBean) {
        if (helper.getItemViewType() == TYPE_0) {
            ImageView iv_integral_pro_tag_out = helper.getView(R.id.iv_integral_pro_tag_out);
            iv_integral_pro_tag_out.setImageResource(R.drawable.goods_sold_out);
            if (integrationBean.getQuantity() < 1) {
                iv_integral_pro_tag_out.setVisibility(View.VISIBLE);
            } else {
                iv_integral_pro_tag_out.setVisibility(View.GONE);
            }
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_attendance_integral_pro_img)
                    , integrationBean.getPicUrl());
            helper.setText(R.id.tv_attendance_integral_pro_name, getStrings(integrationBean.getName()));
            String priceName;
            if (integrationBean.getIntegralType() == 0) {
                priceName = String.format(context.getResources().getString(R.string.integral_indent_product_price), integrationBean.getIntegralPrice());
            } else {
                priceName = String.format(context.getResources().getString(R.string.integral_product_add)
                        , integrationBean.getIntegralPrice());
            }
            helper.setText(R.id.tv_attendance_integral_pro_price, getStrings(priceName))
                    .itemView.setTag(integrationBean);
        } else if (helper.getItemViewType() == TYPE_1) {
            helper.addOnClickListener(R.id.tv_integral_pro_type);
        }
    }
}
