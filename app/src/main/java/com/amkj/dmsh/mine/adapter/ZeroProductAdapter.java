package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.mine.activity.ZeroActivityDetailActivity;
import com.amkj.dmsh.mine.bean.ZeroInfoBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;

/**
 * Created by xiaoxin on 2020/8/11
 * Version:v4.7.0
 * ClassDescription :0元试用商品适配器
 */
public class ZeroProductAdapter extends BaseQuickAdapter<ZeroInfoBean, BaseViewHolder> {
    private final Context context;
    private int type;
    public static final int ZERO_CURRENT = 0;//0本期试用
    public static final int ZERO_BEFORE = 1;// 1往期试用

    public ZeroProductAdapter(Context context, @Nullable List<ZeroInfoBean> data, int type) {
        super(R.layout.item_zero_product, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ZeroInfoBean item) {
        if (item == null) return;
        TextView view = helper.getView(R.id.tv_zero_market_price);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        view.getPaint().setAntiAlias(true);
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_zero_cover), item.getProductImg());
        helper.setText(R.id.tv_zero_name, getStrings(item.getProductName()))
                .setText(R.id.tv_zero_product_subtitle, getStrings(item.getSubtitle()))
                .setText(R.id.tv_zero_price, ConstantMethod.getRmbFormat(context, "0"))
                .setText(R.id.tv_zero_market_price, getStringsChNPrice(context, item.getMarketPrice()))
                .setText(R.id.tv_zero_quantity, getStringsFormat(context, type == 0 ? R.string.limit_quantity : R.string.total_apply_num, type == 0 ? item.getCount() : item.getPartakeCount()))
                .addOnClickListener(R.id.tv_apply).setTag(R.id.tv_apply, item.getActivityId());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZeroActivityDetailActivity.class);
                intent.putExtra("activityId", item.getActivityId());
                context.startActivity(intent);
            }
        });
        TextView tvApply = helper.getView(R.id.tv_apply);
        if (type == 0) {
            tvApply.setText("立即申请");
            tvApply.setTextColor(context.getResources().getColor(R.color.white));
            tvApply.setBackgroundResource(R.drawable.shap_apply_bg);
        } else {
            tvApply.setText("中奖名单");
            tvApply.setTextColor(Color.parseColor("#FFFEB101"));
            tvApply.setBackgroundResource(R.drawable.shap_lottery_bg);
        }

        helper.itemView.setTag(item.getActivityId());
    }
}
