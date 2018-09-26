package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.activity.SalesReturnAppealActivity;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by atd48 on 2016/8/25.
 */
public class SalesReturnItemAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {
    private final Context context;
    private final String orderNo;

    public SalesReturnItemAdapter(Context context, List<GoodsBean> goodsBeanList, String orderNo) {
        super(R.layout.adapter_direct_sales_return_item, goodsBeanList);
        this.context = context;
        this.orderNo = orderNo;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean goodsBean) {
        TextView tv_tag_apply_appeal = helper.getView(R.id.tv_tag_apply_appeal);
        tv_tag_apply_appeal.setTag(goodsBean);
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), goodsBean.getPicUrl());
        helper.setGone(R.id.tv_tag_apply_appeal, true)
                .setText(R.id.tv_direct_indent_pro_name, getStrings(goodsBean.getName()))
                .setText(R.id.tv_direct_indent_pro_price, "￥" + goodsBean.getPrice())
                .setText(R.id.tv_goods_count, "x" + goodsBean.getCount());
        if (goodsBean.getStatus() >= 30) {
            tv_tag_apply_appeal.setEnabled(true);
            tv_tag_apply_appeal.setText("申诉");
        } else if (goodsBean.getStatus() >= -30) {
            tv_tag_apply_appeal.setEnabled(false);
            tv_tag_apply_appeal.setText("审核中");
        } else if (goodsBean.getStatus() == -40) {
            tv_tag_apply_appeal.setEnabled(false);
            tv_tag_apply_appeal.setText("已退款");
        }
        tv_tag_apply_appeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsBean goodsBean = (GoodsBean) v.getTag();
                if (goodsBean != null) {
                    Intent intent = new Intent(context, SalesReturnAppealActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("goodsBean", goodsBean);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("orderNo", orderNo);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }
}
