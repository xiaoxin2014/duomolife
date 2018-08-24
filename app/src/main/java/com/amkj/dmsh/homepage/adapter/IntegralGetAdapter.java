package com.amkj.dmsh.homepage.adapter;

import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.IntegralGetEntity.IntegralGetBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/23
 * version 3.1.5
 * class description:积分获取
 */
public class IntegralGetAdapter extends BaseQuickAdapter<IntegralGetBean, BaseViewHolderHelper> {
    public IntegralGetAdapter(List<IntegralGetBean> integralGetBeanList) {
        super(R.layout.adapter_integral_get, integralGetBeanList);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, IntegralGetBean integralGetBean) {
        TextView tv_integral_get_button_text = helper.getView(R.id.tv_integral_get_button_text);
        tv_integral_get_button_text.setSelected(integralGetBean.getButtonFlag()==1);
        helper.setText(R.id.tv_integral_get_title, getStrings(integralGetBean.getTitle()))
                .setText(R.id.tv_integral_get_content, getStrings(integralGetBean.getContent()))
                .setText(R.id.tv_integral_get_button_text, getStrings(integralGetBean.getButton()))
                .setText(R.id.tv_integral_get_desc, getStrings(integralGetBean.getDescription()));
        helper.itemView.setTag(integralGetBean);
    }
}
