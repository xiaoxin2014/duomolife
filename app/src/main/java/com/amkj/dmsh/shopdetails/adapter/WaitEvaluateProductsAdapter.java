package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity.ScoreGoodsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.skipJoinTopic;

/**
 * Created by xiaoxin on 2020/4/18
 * Version:v4.5.0
 * ClassDescription :待评价商品订单列表适配器
 */
public class WaitEvaluateProductsAdapter extends BaseQuickAdapter<OrderProductNewBean, BaseViewHolder> {

    private final Activity context;

    public WaitEvaluateProductsAdapter(Activity activity, @Nullable List<OrderProductNewBean> data) {
        super(R.layout.item_wait_evaluate, data);
        context = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderProductNewBean item) {
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), item.getPicUrl());
        helper.setText(R.id.tv_direct_indent_pro_name, getStrings(item.getProductName()))
                .setText(R.id.tv_direct_indent_pro_sku, getStrings(item.getSaleSkuValue()))
                .setText(R.id.tv_product_status, getStrings(item.getStatusText()))
                .setGone(R.id.tv_product_status, !TextUtils.isEmpty((item.getStatusText())))
                .setText(R.id.tv_predict_time, getStrings(item.getRemindText()))
                .setGone(R.id.tv_predict_time, !TextUtils.isEmpty(item.getRemindText()))
                .setText(R.id.tv_direct_pro_count, "x" + item.getCount())
                .setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context, item.getPrice()));

        ScoreGoodsBean scoreGoodsBean = new ScoreGoodsBean();
        scoreGoodsBean.setCover(item.getPicUrl());
        scoreGoodsBean.setProductName(item.getProductName());
        scoreGoodsBean.setOrderNo(item.getOrderNo());
        scoreGoodsBean.setProductId(String.valueOf(item.getProductId()));
        scoreGoodsBean.setOrderProductId(item.getOrderProductId());
        scoreGoodsBean.setPosition(helper.getLayoutPosition());
        helper.itemView.setTag(scoreGoodsBean);
    }
}
