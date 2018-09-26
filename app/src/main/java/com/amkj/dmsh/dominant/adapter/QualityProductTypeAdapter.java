package com.amkj.dmsh.dominant.adapter;

import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author Liuguipeng
 * @email liuguipeng163@163.com
 * create on 4/10/2018
 * class description 良品侧边栏分类
 */
public class QualityProductTypeAdapter extends BaseQuickAdapter<QualityTypeBean, BaseViewHolder> {

    public QualityProductTypeAdapter(List<QualityTypeBean> qualityTypeBeanList) {
        super(R.layout.adapter_quality_left_type, qualityTypeBeanList);
    }

    @Override
    protected void convert(BaseViewHolder helper, QualityTypeBean qualityTypeBean) {
        TextView tv_quality_type = helper.getView(R.id.tv_quality_type);
        tv_quality_type.setSelected(qualityTypeBean.isSelect());
        helper.setText(R.id.tv_quality_type, getStrings(qualityTypeBean.getName()))
                .itemView.setTag(qualityTypeBean);
    }
}
