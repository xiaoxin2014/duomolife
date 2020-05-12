package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity.MainOrderBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipRefundDetail;


/**
 * Created by xiaoxin on 2020/4/9
 * Version:v4.5.0
 * ClassDescription :售后列表适配器
 */
public class DirectSalesReturnRecordAdapter extends BaseQuickAdapter<MainOrderBean, BaseViewHolder> {
    private final Context context;

    public DirectSalesReturnRecordAdapter(Context context, List<MainOrderBean> orderListBeanList) {
        super(R.layout.adapter_direct_sales_return_record_item, orderListBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MainOrderBean mainOrderBean) {
        if (mainOrderBean == null) return;
        helper.setText(R.id.tv_tag_record_state, mainOrderBean.getStatusText())
                .setText(R.id.tv_goods_indent_code, "申请时间 " + getStrings(mainOrderBean.getCreateTime()));

        //初始化退款商品列表
        RecyclerView communal_recycler = helper.getView(R.id.communal_recycler);
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.setLayoutManager(new LinearLayoutManager(context));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        RefundProductListAdapter refundProductListAdapter = new RefundProductListAdapter(context, mainOrderBean.getOrderProductList());
        refundProductListAdapter.setOnItemClickListener((adapter, view, position) -> skipRefundDetail(context, mainOrderBean.getRefundNo()));
        communal_recycler.setAdapter(refundProductListAdapter);
        helper.itemView.setOnClickListener(v -> skipRefundDetail(context, mainOrderBean.getRefundNo()));
        helper.itemView.setTag(mainOrderBean);
    }
}
