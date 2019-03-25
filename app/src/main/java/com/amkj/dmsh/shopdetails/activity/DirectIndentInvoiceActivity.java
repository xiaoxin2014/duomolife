package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.adapter.InvoiceInfoShowAdapter;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean.OrderProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoiceEntity;
import com.amkj.dmsh.shopdetails.bean.IndentInvoiceEntity.IndentInvoiceBean;
import com.amkj.dmsh.shopdetails.bean.InvoiceInfoBean;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.INVOICE_DETAIL;

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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)


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
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
                directProductListAdapter.setEnableLoadMore(false);
            }
        });
        communal_recycler.setAdapter(directProductListAdapter);
        if (orderDetailBean != null) {
            setOrderDetailBean(orderDetailBean);
        }
    }

    private void setOrderDetailBean(OrderDetailBean orderDetailBean) {
        goodsBeanList.clear();
        lvHeaderView.tv_indent_invoice_orderID.setText("订单编号：" + getStrings(orderDetailBean.getNo()));
        lvHeaderView.tv_indent_invoice_orderID.setTag(getStrings(orderDetailBean.getNo()));
        lvHeaderView.tv_indent_invoice_orderTime.setText("下单时间：" + getStrings(orderDetailBean.getCreateTime()));
        lvHeaderView.tv_indent_invoice_header_type.setText(getStrings(INDENT_PRO_STATUS != null ?
                INDENT_PRO_STATUS.get(String.valueOf(orderDetailBean.getStatus())) : ""));
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
        Map<String, Object> params = new HashMap<>();
//        订单号
        if (orderDetailBean != null) {
            params.put("no", orderDetailBean.getNo());
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, INVOICE_DETAIL, params, new NetLoadListenerHelper() {
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
                NetLoadUtils.getNetInstance().showLoadSir(loadService, code);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, indentInvoiceEntity);
            }

            @Override
            public void netClose() {
                showToast(DirectIndentInvoiceActivity.this, R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(DirectIndentInvoiceActivity.this, R.string.invalidData);
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
                lvFootView.ll_indent_invoice_type.setVisibility(View.VISIBLE);
                lvFootView.tv_indent_invoice_type.setText(invoiceBeanType.get(String.valueOf(invoiceBean.getType())));
                List<InvoiceInfoBean> invoiceInfoBeans = new ArrayList<>();
                setInvoiceInfo(invoiceInfoBeans, invoiceBean.getContent(), "发票内容：");
                setInvoiceInfo(invoiceInfoBeans, invoiceBean.getTitle(), "发票抬头：");
                setInvoiceInfo(invoiceInfoBeans, invoiceBean.getTaxpayer_on(), "纳税人识别号：");
                setInvoiceInfo(invoiceInfoBeans, invoiceBean.getAddress(), "公司地址：");
                setInvoiceInfo(invoiceInfoBeans, invoiceBean.getMobile(),  "电  话：");
                setInvoiceInfo(invoiceInfoBeans, invoiceBean.getBankOfDeposit(), "开户行：");
                setInvoiceInfo(invoiceInfoBeans, invoiceBean.getAccount(), "账  号：");
                if(invoiceInfoBeans.size()>0){
                    lvFootView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                    if(lvFootView.communal_recycler_wrap.getAdapter()==null){
                        lvFootView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(this));
                        lvFootView.communal_recycler_wrap.setNestedScrollingEnabled(false);
                        InvoiceInfoShowAdapter invoiceInfoShowAdapter = new InvoiceInfoShowAdapter(invoiceInfoBeans);
                        lvFootView.communal_recycler_wrap.setAdapter(invoiceInfoShowAdapter);
                    }
                    lvFootView.communal_recycler_wrap.getAdapter().notifyDataSetChanged();
                }else{
                    lvFootView.communal_recycler_wrap.setVisibility(View.GONE);
                }
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
                lvFootView.communal_recycler_wrap.setVisibility(View.GONE);
                lvFootView.tv_indent_invoice_status_check.setEnabled(true);
                lvFootView.tv_indent_invoice_status_check.setTag(indentInvoiceBean);
                lvFootView.tv_indent_invoice_status_check.setTextColor(getResources().getColor(R.color.green_base));
                lvFootView.tv_indent_invoice_status_check.setText("(去开发票)");
                lvFootView.tv_indent_invoice_status.setText(invoiceBeanStatus.get(String.valueOf(invoiceBean.getStatus())));
                break;
        }
    }

    /**
     * 设置发票信息
     * @param invoiceInfoBeans
     * @param content
     * @param invoiceType
     */
    private void setInvoiceInfo(List<InvoiceInfoBean> invoiceInfoBeans, String content, String invoiceType) {
        if (!TextUtils.isEmpty(content)) {
            invoiceInfoBeans.add(new InvoiceInfoBean(invoiceType, content));
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
        //        发票信息
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
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
