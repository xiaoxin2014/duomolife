package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.CommonPagerAdapter;
import com.amkj.dmsh.base.ViewHolder;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo.GoodsListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import java.util.List;

/**
 * Created by xiaoxin on 2019/9/30
 * Version:v4.3.1
 * ClassDescription :加价购商品封面适配器
 */
public class PurchaseCoverAdapter extends CommonPagerAdapter<GoodsListBean> {

    private final Context mContext;

    public PurchaseCoverAdapter(Context context, List<GoodsListBean> datas) {
        super(context, datas, R.layout.item_pucharse_cover);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder helper, int position, GoodsListBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover), ConstantMethod.getStrings(item.getPicUrl()));
        helper.itemView.setOnClickListener(v -> ConstantMethod.skipProductUrl(mContext, 1, item.getProductId()));
    }
}
