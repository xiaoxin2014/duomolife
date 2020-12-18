package com.amkj.dmsh.find.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.bean.RelatedGoodsBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/7/22
 * Version:v4.1.0
 * ClassDescription :发现-帖子详情-关联商品适配器
 */
public class PostRelatedAdapter extends BaseQuickAdapter<RelatedGoodsBean, BaseViewHolder> {
    private Activity context;

    public PostRelatedAdapter(Activity activity, @Nullable List<RelatedGoodsBean> data) {
        super(R.layout.item_post_multi_goods, data);
        this.context = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, RelatedGoodsBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_cover), item.getPictureUrl());
        helper.setText(R.id.tv_title, getStrings(item.getTitle()))
                .setText(R.id.tv_price, ("¥" + item.getPrice()))
                .setText(R.id.tv_sku_text, getStrings(item.getSkutext()))
                .setGone(R.id.tv_sku_text, !TextUtils.isEmpty(item.getSkutext()));
        helper.itemView.setOnClickListener(v -> {
            if (ConstantMethod.isContextExisted(context)) {
                Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(item.getProductId()));
                context.startActivity(intent);
            }
        });
    }
}
