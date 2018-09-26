package com.amkj.dmsh.dominant.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/13
 * version 3.0.9
 * class description:二级目录 商品 排序
 */

public class ProductTypeSortAdapter extends BaseQuickAdapter<QualityTypeBean,BaseViewHolder>{
    public ProductTypeSortAdapter(List<QualityTypeBean> productSortList) {
        super(R.layout.adapter_product_sort_type, productSortList);
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityTypeBean qualityTypeBean) {
        TextView tv_product_sort_type = helper.getView(R.id.tv_product_sort_type);
        tv_product_sort_type.setText(TextUtils.isEmpty(qualityTypeBean.getName())
                ?getStrings(qualityTypeBean.getSortName()):getStrings(qualityTypeBean.getName()));
        tv_product_sort_type.setSelected(qualityTypeBean.isSelect());
        helper.itemView.setTag(qualityTypeBean);
    }
}
