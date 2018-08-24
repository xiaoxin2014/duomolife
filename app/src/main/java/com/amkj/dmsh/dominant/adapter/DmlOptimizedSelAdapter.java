package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelEntity.DmlOptimizedSelBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:请输入类描述
 */

public class DmlOptimizedSelAdapter extends BaseQuickAdapter<DmlOptimizedSelBean, BaseViewHolderHelper> {
    private final Context context;

    public DmlOptimizedSelAdapter(Context context, List<DmlOptimizedSelBean> dmlOptimizedSelList) {
        super(R.layout.adapter_dml_optimized_sel, dmlOptimizedSelList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, DmlOptimizedSelBean dmlOptimizedSelBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_cover_detail_bg), dmlOptimizedSelBean.getPicUrl());
        helper.setText(R.id.tv_opt_sel_title, getStrings(dmlOptimizedSelBean.getTitle()))
                .setText(R.id.tv_opt_sel_subtitle, getStrings(dmlOptimizedSelBean.getSubtitle()));
        helper.itemView.setTag(dmlOptimizedSelBean);
    }
}
