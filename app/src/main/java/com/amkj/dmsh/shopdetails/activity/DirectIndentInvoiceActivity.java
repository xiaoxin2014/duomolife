package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean.OrderProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoiceEntity;
import com.amkj.dmsh.shopdetails.bean.IndentInvoiceEntity.IndentInvoiceBean;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.google.gson.Gson;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;
;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/24
 * class description:发票详情
 */
public class DirectIndentInvoiceActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List goodsBeanList = new ArrayList<>();
    private DirectProductListAdapter directProductListAdapter;
    public static final String INDENT_INVOICE = "directInvoiceGoods";
    private LvHeaderView lvHeaderView;
    private LvFootView lvFootView;
    private OrderDetailBean orderDetailBean;
    private boolean isOnPause;
    private IndentInvoiceEntity indentInvoiceEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_direct_indent_invoice;
    }

    @Override
    protected void initViews() {
        tl_normal_bar.setSelected(true);
        tv_header_titleAll.setText("发票详情");
        header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
//        订单号
        Bundle bundle = intent.getExtras();
        orderDetailBean = bundle.getParcelable("indentInfo");
        communal_recycler.setLayoutManager(new LinearLayoutManager(DirectIndentInvoiceActivity.this));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        directProductListAdapter = new DirectProductListAdapter(DirectIndentInvoiceActivity.this, goodsBeanList, INDENT_INVOICE);
        View headerView = LayoutInflater.from(DirectIndentInvoiceActivity.this).inflate(R.layout.layout_pro_invoice_detail_header, (ViewGroup) communal_recycler.getParent(), false);
        lvHeaderView = new LvHeaderView();
        ButterKnife.bind(lvHeaderView, headerView);
        View footView = LayoutInflater.from(DirectIndentInvoiceActivity.this).inflate(R.layout.layout_pro_invoice_detail_foot, (ViewGroup) communal_recycler.getParent(), false);
        lvFootView = new LvFootView();
        ButterKnife.bind(lvFootView, footView);
        directProductListAdapter.addHeaderView(headerView);
        directProductListAdapter.addFooterView(footView);
        directProductListAdapter.setEnableLoadMore(false);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
            directProductListAdapter.setEnableLoadMore(false);
        });
        communal_recycler.setAdapter(directProductListAdapter);
        if (orderDetailBean != null) {
            setOrderDetailBean(orderDetailBean);
        }
    }

    private void setOrderDetailBean(OrderDetailBean orderDetailBean) {
        goodsBeanList.clear();
        lvHeaderView.tv_indent_invoice_orderID.setText("订单编号：" + getStrings(orderDetailBean.getNo()));
        lvHeaderView.tv_indent_invoice_orderTime.setText("下单时间：" + getStrings(orderDetailBean.getCreateTime()));
        lvHeaderView.tv_indent_invoice_header_type.setText(INDENT_PRO_STATUS.get(String.valueOf(orderDetailBean.getStatus())));
        lvFootView.tv_indent_direct_pay_price.setText(getStringsChNPrice(DirectIndentInvoiceActivity.this, orderDetailBean.getPayAmount()));
        for (int i = 0; i < orderDetailBean.getGoodDetails().size(); i++) {
            GoodsDetailBean goodsDetailBean = orderDetailBean.getGoodDetails().get(i);
            for (int j = 0; j < goodsDetailBean.getOrderProductInfoList().size(); j++) {
                OrderProductInfoBean orderProductInfoBean = goodsDetailBean.getOrderProductInfoList().get(j);
                if (j == 0 && goodsDetailBean.getActivityInfo() != null) {
                    orderProductInfoBean.setActivityInfoDetailBean(goodsDetailBean.getActivityInfo());
                }
                goodsBeanList.add(orderProductInfoBean);
            }
            if (goodsDetailBean.getActivityInfo() != null && goodsBeanList.size() > 0) {
                OrderProductInfoBean orderProductInfoBean = (OrderProductInfoBean) goodsBeanList.get(goodsBeanList.size() - 1);
                orderProductInfoBean.setShowLine(1);
                goodsBeanList.set(goodsBeanList.size() - 1, orderProductInfoBean);
            }
        }
        directProductListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.INVOICE_DETAIL;
        Map<String, Object> params = new HashMap<>();
//        订单号
        if (orderDetailBean != null) {
            params.put("no", orderDetailBean.getNo());
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals(SUCCESS_CODE)) {
                    Gson gson = new Gson();
                    indentInvoiceEntity = gson.fromJson(result, IndentInvoiceEntity.class);
                    setInvoiceInfo(indentInvoiceEntity.getIndentInvoiceBean());
                } else if (!code.equals(EMPTY_CODE)) {
                    showToast(DirectIndentInvoiceActivity.this, msg);
                }
                directProductListAdapter.notifyDataSetChanged();
                NetLoadUtils.getQyInstance().showLoadSir(loadService, code);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                showToast(DirectIndentInvoiceActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, indentInvoiceEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                showToast(DirectIndentInvoiceActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService, indentInvoiceEntity);
            }
        });
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void setInvoiceInfo(IndentInvoiceBean indentInvoiceBean) {
        IndentInvoiceBean.InvoiceBean invoiceBean = indentInvoiceBean.getInvoice();
        Map<String, String> invoiceBeanStatus = indentInvoiceBean.getStatus();
        Map<String, String> invoiceBeanType = indentInvoiceBean.getType();
        if (invoiceBean == null || invoiceBeanStatus == null || invoiceBeanType == null) {
            lvFootView.ll_draw_up_invoice.setVisibility(View.GONE);
            return;
        }
        switch (invoiceBean.getStatus()) {
            case 1:
            case 2:
                lvFootView.ll_indent_invoice_type.setVisibility(View.VISIBLE);
                lvFootView.ll_indent_invoice_detail.setVisibility(View.VISIBLE);
                lvFootView.ll_indent_invoice_title.setVisibility(View.VISIBLE);
                lvFootView.tv_indent_invoice_type.setText(invoiceBeanType.get(String.valueOf(invoiceBean.getType())));
                lvFootView.tv_indent_invoice_detail.setText(getStrings(invoiceBean.getContent()));
                lvFootView.tv_indent_invoice_title.setText(getStrings(invoiceBean.getTitle()));
                if (invoiceBean.getStatus() == 2) {
                    lvFootView.tv_indent_invoice_status.setText(invoiceBeanStatus.get(String.valueOf(invoiceBean.getStatus())));
                    lvFootView.tv_indent_invoice_status_check.setText("(下载)");
                    lvFootView.tv_indent_invoice_status_check.setEnabled(true);
                    lvFootView.tv_indent_invoice_status_check.setTextColor(getResources().getColor(R.color.promotion_blue));
                    lvFootView.tv_indent_invoice_status_check.setTag(indentInvoiceBean);
                } else {
                    lvFootView.tv_indent_invoice_status.setText("");
                    lvFootView.tv_indent_invoice_status_check.setEnabled(false);
                    lvFootView.tv_indent_invoice_status_check.setTextColor(getResources().getColor(R.color.text_b_red_color));
                    lvFootView.tv_indent_invoice_status_check.setText(invoiceBeanStatus.get(String.valueOf(invoiceBean.getStatus())));
                }
                break;
            default:
                lvFootView.ll_indent_invoice_type.setVisibility(View.GONE);
                lvFootView.ll_indent_invoice_detail.setVisibility(View.GONE);
                lvFootView.ll_indent_invoice_title.setVisibility(View.GONE);
                lvFootView.tv_indent_invoice_status_check.setEnabled(true);
                lvFootView.tv_indent_invoice_status_check.setTag(indentInvoiceBean);
                lvFootView.tv_indent_invoice_status_check.setTextColor(getResources().getColor(R.color.green_base));
                lvFootView.tv_indent_invoice_status_check.setText("(去开发票)");
                lvFootView.tv_indent_invoice_status.setText(invoiceBeanStatus.get(String.valueOf(invoiceBean.getStatus())));
                break;
        }
    }

    class LvHeaderView {
        //        订单状态
        @BindView(R.id.tv_indent_invoice_header_type)
        TextView tv_indent_invoice_header_type;
        //        订单编号
        @BindView(R.id.tv_indent_invoice_orderID)
        TextView tv_indent_invoice_orderID;
        //        订单时间
        @BindView(R.id.tv_indent_invoice_orderTime)
        TextView tv_indent_invoice_orderTime;

        @OnLongClick(R.id.tv_indent_invoice_orderID)
        boolean copyIndentNumber(View view) {
            CommunalCopyTextUtils.showPopWindow(DirectIndentInvoiceActivity.this, (TextView) view, (String) view.getTag());
            return true;
        }
    }

    class LvFootView {
        //        支付金额
        @BindView(R.id.tv_indent_direct_pay_price)
        TextView tv_indent_direct_pay_price;
        //        已开发票
        @BindView(R.id.ll_draw_up_invoice)
        LinearLayout ll_draw_up_invoice;
        //        发票状态
        @BindView(R.id.tv_indent_invoice_status)
        TextView tv_indent_invoice_status;
        //        发票类型
        @BindView(R.id.ll_indent_invoice_type)
        LinearLayout ll_indent_invoice_type;
        @BindView(R.id.tv_indent_invoice_type)
        TextView tv_indent_invoice_type;
        //        发票内容
        @BindView(R.id.ll_indent_invoice_detail)
        LinearLayout ll_indent_invoice_detail;
        @BindView(R.id.tv_indent_invoice_detail)
        TextView tv_indent_invoice_detail;
        //        发票抬头
        @BindView(R.id.ll_indent_invoice_title)
        LinearLayout ll_indent_invoice_title;
        @BindView(R.id.tv_indent_invoice_title)
        TextView tv_indent_invoice_title;
        //        查看
        @BindView(R.id.tv_indent_invoice_status_check)
        TextView tv_indent_invoice_status_check;

        //        发票查看
        @OnClick({R.id.tv_indent_invoice_status_check})
        void checkInvoice(View view) {
            IndentInvoiceBean indentInvoiceBean = (IndentInvoiceBean) view.getTag();
            if (indentInvoiceBean != null) {
                Intent intent = new Intent();
                switch (indentInvoiceBean.getInvoice().getStatus()) {
//                    下载
                    case 2:
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(getStrings(indentInvoiceBean.getInvoice().getImgUrl()));
                        intent.setData(content_url);
                        startActivity(intent);
                        break;
                    case 0: // 去开发票
                        intent.setClass(DirectIndentInvoiceActivity.this, IndentDrawUpInvoiceActivity.class);
                        intent.putExtra("type", "detailsInvoice");
                        intent.putExtra("indentNo", indentInvoiceBean.getInvoice().getOrderNo());
                        startActivity(intent);
                        break;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
