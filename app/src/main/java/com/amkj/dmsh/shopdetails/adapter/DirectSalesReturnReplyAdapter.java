package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.adapter.SalesReturnItemAdapter;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/23
 * class description:退货申诉
 */

public class DirectSalesReturnReplyAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolder> {
    private final LayoutInflater layoutInflater;
    private Context context;

    public DirectSalesReturnReplyAdapter(Context context, List<OrderListBean> orderListBeanList) {
        super(R.layout.layout_communal_recycler_wrap, orderListBeanList);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderListBean orderListBean) {
        RecyclerView communal_recycler_wrap = helper.getView(R.id.communal_recycler_wrap);
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
        SalesReturnItemAdapter salesReturnItemAdapter = new SalesReturnItemAdapter(context, orderListBean.getGoods(), orderListBean.getNo());
        View headerView = layoutInflater.inflate(R.layout.adapter_sales_return_header, communal_recycler_wrap, false);
        ReturnHeaderView returnHeaderView = new ReturnHeaderView();
        ButterKnife.bind(returnHeaderView, headerView);
        salesReturnItemAdapter.setHeaderView(headerView);
        communal_recycler_wrap.setAdapter(salesReturnItemAdapter);
        setHeaderData(returnHeaderView, orderListBean);
        salesReturnItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, DirectExchangeDetailsActivity.class);
                intent.putExtra("orderNo", orderListBean.getNo());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void setHeaderData(ReturnHeaderView returnHeaderView, OrderListBean orderListBean) {
        returnHeaderView.tv_tag_record_state.setVisibility(View.GONE);
        returnHeaderView.tv_goods_indent_code.setText("订单号：" + orderListBean.getNo());
    }

    class ReturnHeaderView {
        @BindView(R.id.tv_tag_record_state)
        TextView tv_tag_record_state;
        @BindView(R.id.tv_goods_indent_code)
        TextView tv_goods_indent_code;
    }
}
