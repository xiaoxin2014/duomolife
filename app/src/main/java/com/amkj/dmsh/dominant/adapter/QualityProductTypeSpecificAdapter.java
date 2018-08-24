package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

/**
 * @author Liuguipeng
 * @email liuguipeng163@163.com
 * create on 4/10/2018
 * class description 良品侧边栏具体分类
 */
public class QualityProductTypeSpecificAdapter extends BaseMultiItemQuickAdapter<QualityTypeBean, BaseViewHolderHelper> {
    private final Context context;

    public QualityProductTypeSpecificAdapter(Context context, List<QualityTypeBean> qualityTypeBeanList) {
//        super(R.layout.adapter_quality_type, qualityTypeBeanList);
        super(qualityTypeBeanList);
        addItemType(TYPE_0, R.layout.adapter_quality_type);
        addItemType(TYPE_1, R.layout.adapter_quality_specific_type);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, QualityTypeBean qualityTypeBeanBean) {
        if (helper.getItemViewType() == TYPE_1) {
            helper.setText(R.id.tv_quality_type_specific, "—— " + getStrings(qualityTypeBeanBean.getName())+" ——");
        }else{
            GlideImageLoaderUtil.loadFitCenter(context, (ImageView) helper.getView(R.id.iv_product_type), getStrings(qualityTypeBeanBean.getPicUrl()));
            helper.setText(R.id.tv_type_title, getStrings(qualityTypeBeanBean.getName()));
            helper.itemView.setTag(qualityTypeBeanBean);
        }
    }
}
