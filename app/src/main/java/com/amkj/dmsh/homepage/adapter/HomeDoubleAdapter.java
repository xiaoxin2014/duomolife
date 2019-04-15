package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.bean.HomeCommonEntity.ProductInfoListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/15 0015
 * Version:v4.0.0
 * ClassDescription :
 */
public class HomeDoubleAdapter extends BaseQuickAdapter<ProductInfoListBean, BaseViewHolder> {

    private final Context mContext;

    public HomeDoubleAdapter(Context context, @Nullable List<ProductInfoListBean> data) {
        super(R.layout.item_home_double, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfoListBean item) {
        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_cover), item.getImg());
        helper.setText(R.id.tv_price, "¥" + ConstantMethod.getStrings(item.getPrice()));
        ((TextView) helper.getView(R.id.tv_market_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        helper.setText(R.id.tv_market_price, "¥" + ConstantMethod.getStrings(item.getMarketPrice()));
//        helper.itemView.setOnClickListener(view -> {
//            setSkipPath(mContext,item.get)
//        });
       helper.itemView.setTag(item);
    }
}
