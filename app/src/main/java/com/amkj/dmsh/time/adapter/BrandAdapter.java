package com.amkj.dmsh.time.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.activity.TimeBrandDetailsActivity;
import com.amkj.dmsh.time.bean.BrandEntity.BrandBean;
import com.amkj.dmsh.time.bean.BrandEntity.BrandBean.BrandProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.NewGridItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2020/9/23
 * Version:v4.8.0
 * ClassDescription :品牌团适配器
 */
public class BrandAdapter extends BaseQuickAdapter<BrandBean, BaseViewHolder> {
    private final Context context;

    public BrandAdapter(Context context, @Nullable List<BrandBean> data) {
        super(R.layout.item_brand, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BrandBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.iv_brand_cover), item.getLogo(), AutoSizeUtils.mm2px(context, 68));
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_background), item.getBackgroundUrl());
        helper.setText(R.id.tv_brand_name, getStrings(item.getTitle()))
                .setText(R.id.tv_brand_desc, getStrings(item.getSubtitle()));
        helper.getView(R.id.rl_brand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TimeBrandDetailsActivity.class);
                intent.putExtra("id", item.getId());
                context.startActivity(intent);
            }
        });
        List<BrandProductBean> productList = item.getProductList();
        if (productList != null && productList.size() > 0) {
            RecyclerView rvProduct = helper.getView(R.id.rv_brand_product);
            rvProduct.setLayoutManager(new GridLayoutManager(context, 3));
            if (rvProduct.getItemDecorationCount() == 0) {
                rvProduct.addItemDecoration(new NewGridItemDecoration.Builder()
                        .setDividerId(R.drawable.item_divider_five_gray_f)
                        .create());
            }
            rvProduct.setAdapter(new BaseQuickAdapter<BrandProductBean, BaseViewHolder>(R.layout.item_brand_product, productList.subList(0, Math.min(productList.size(), 3))) {
                @Override
                protected void convert(BaseViewHolder helper, BrandProductBean item) {
                    GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), item.getPicUrl());
                    TextView tvMarketPrice = helper.getView(R.id.tv_market_price);
                    tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tvMarketPrice.getPaint().setAntiAlias(true);
                    helper.setText(R.id.tv_price, getStringsChNPrice(context, item.getPrice()))
                            .setText(R.id.tv_market_price, getStringsChNPrice(context, item.getMarketPrice()))
                            .setGone(R.id.tv_market_price, !TextUtils.isEmpty(item.getMarketPrice()));
                    helper.itemView.setOnClickListener(v -> skipProductUrl(context, 0, item.getId()));
                }
            });
        }

        helper.itemView.setTag(item);
    }
}
