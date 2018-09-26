package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityGoods.Goods;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by LGuipeng on 2016/7/4.
 */
public class QualityTimeAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    private final Context context;

    public QualityTimeAdapter(Context context, List<Goods> goods) {
        super(R.layout.adapter_quality_lv_time, goods);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods goods) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_quality_time_item), goods.getPicUrl());
        helper.setText(R.id.tv_quality_time_item_name, getStrings(goods.getName()))
                .setText(R.id.tv_quality_time_item_price, "￥" + goods.getPrice());
        helper.itemView.setTag(goods);
    }
}
