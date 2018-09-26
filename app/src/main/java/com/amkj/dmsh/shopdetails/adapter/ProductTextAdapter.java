package com.amkj.dmsh.shopdetails.adapter;

import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.PresentProductInfoBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/12/18
 * version 1.0
 * class description:赠品适配器 单文本 --商品详情赠品 优惠券
 */

public class ProductTextAdapter extends BaseMultiItemQuickAdapter<PresentProductInfoBean, BaseViewHolder> {

    public ProductTextAdapter(List<PresentProductInfoBean> productInfoBeans) {
        super(productInfoBeans);
        addItemType(TYPE_0, R.layout.adapter_layout_communal_text);
        addItemType(TYPE_1, R.layout.adapter_layout_communal_check_red_text);
        addItemType(TYPE_2, R.layout.adapter_layout_product_coupon);
    }

    @Override
    protected void convert(BaseViewHolder helper, PresentProductInfoBean productInfoBean) {
        switch (productInfoBean.getItemType()) {
            case TYPE_1:
                CheckBox cb_red_communal_text = helper.getView(R.id.cb_red_communal_text);
                cb_red_communal_text.setChecked(productInfoBean.isChecked());
                cb_red_communal_text.setText(getStrings(productInfoBean.getPresentName()));
                break;
            case TYPE_2:
                LinearLayout ll_product_coupon = helper.getView(R.id.ll_product_coupon);
                ll_product_coupon.setSelected(productInfoBean.isSelect());
                helper.setText(R.id.tv_communal_coupon_name, getStrings(productInfoBean.getName()))
                        .setText(R.id.tv_communal_tag,"【优惠券】")
                        .setVisible(R.id.tv_communal_tag,productInfoBean.isFirstTag())
                        .itemView.setTag(productInfoBean);
                break;
            default:
                helper.setText(R.id.tv_communal_text,getStrings(productInfoBean.getPresentName()))
                        .setText(R.id.tv_communal_present_tag,"【赠品】")
                        .setVisible(R.id.tv_communal_present_tag,productInfoBean.isFirstTag());
                break;
        }
    }
}
