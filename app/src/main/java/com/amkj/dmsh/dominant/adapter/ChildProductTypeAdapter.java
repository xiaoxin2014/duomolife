package com.amkj.dmsh.dominant.adapter;

import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/15
 * version 3.0.9
 * class description:商品子分类
 */

public class ChildProductTypeAdapter extends BaseQuickAdapter<QualityTypeBean, BaseViewHolderHelper> {
    public ChildProductTypeAdapter(List<QualityTypeBean> qualityTypeBeans) {
        super(R.layout.adapter_product_child_type, qualityTypeBeans);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, QualityTypeBean qualityTypeBean) {
        TextView tv_product_child_type = helper.getView(R.id.tv_product_child_type);
        tv_product_child_type.setSelected(qualityTypeBean.isSelect());
        helper.setText(R.id.tv_product_child_type, getStrings(qualityTypeBean.getChildName()))
                .itemView.setTag(qualityTypeBean);
    }
}
