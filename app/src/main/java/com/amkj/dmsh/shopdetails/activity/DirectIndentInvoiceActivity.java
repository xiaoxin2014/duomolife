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
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean;
import com.amkj.dmsh.shopdetails.bean.IndentInfoDetailEntity.IndentInfoDetailBean.OrderDetailBean.GoodsDetailBean.OrderProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoiceEntity;
import com.amkj.dmsh.shopdetails.bean.IndentInvoiceEntity.IndentInvoiceBean;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

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
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;

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
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private List goodsBeanList = new ArrayList<>();
    private DirectProductListAdapter directProductListAdapter;
    public static final String INDENT_INVOICE = "directInvoiceGoods";
    private LvHeaderView lvHeaderView;
    private LvFootView lvFootView;
    private int uid;
    private OrderDetailBean orderDetailBean;
    private boolean isOnPause;
    private AlertView downDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_direct_indent_invoice;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
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
        directProductListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderProductInfoBean orderProductInfoBean = (OrderProductInfoBean) view.getTag();
                if (orderProductInfoBean != null) {
                    Intent intent = new Intent(DirectIndentInvoiceActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(orderProductInfoBean.getId()));
                    startActivity(intent);
                }
            }
        });
        if (orderDetailBean != null) {
            setOrderDetailBean(orderDetailBean);
        }
        communal_load.setVisibility(View.VISIBLE);
    }

    private void setOrderDetailBean(OrderDetailBean orderDetailBean) {
        goodsBeanList.clear();
        lvHeaderView.tv_indent_invoice_orderID.setText("订单编号：" + getStrings(orderDetailBean.getNo()));
        lvHeaderView.tv_indent_invoice_orderTime.setText("下单时间：" + getStrings(orderDetailBean.getCreateTime()));
        lvHeaderView.tv_indent_invoice_header_type.setText(INDENT_PRO_STATUS.get(String.valueOf(orderDetailBean.getStatus())));
        lvFootView.tv_indent_direct_pay_price.setText("￥" + orderDetailBean.getPayAmount());
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
        directProductListAdapter.setNewData(goodsBeanList);
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.INVOICE_DETAIL;
        Map<String, Object> params = new HashMap<>();
//        订单号
        if (orderDetailBean != null) {
            params.put("no", orderDetailBean.getNo());
        }
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
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
                if (code.equals("01")) {
                    Gson gson = new Gson();
                    IndentInvoiceEntity indentInvoiceEntity = gson.fromJson(result, IndentInvoiceEntity.class);
                    setInvoiceInfo(indentInvoiceEntity.getIndentInvoiceBean());
                } else if (!code.equals("02")) {
                    showToast(DirectIndentInvoiceActivity.this, msg);
                }
                directProductListAdapter.setNewData(goodsBeanList);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                smart_communal_refresh.finishRefresh();
                communal_error.setVisibility(View.VISIBLE);
                showToast(DirectIndentInvoiceActivity.this, R.string.unConnectedNetwork);
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setInvoiceInfo(IndentInvoiceBean indentInvoiceBean) {
        if (indentInvoiceBean.getInvoice().getStatus() == 2) {
            lvFootView.ll_draw_up_invoice.setVisibility(View.VISIBLE);
            lvFootView.ll_wait_no_invoice.setVisibility(View.GONE);
            lvFootView.tv_indent_invoice_status.setText(indentInvoiceBean.getStatus().get(indentInvoiceBean.getInvoice().getStatus() + ""));
            lvFootView.tv_indent_invoice_type.setText(indentInvoiceBean.getType().get(indentInvoiceBean.getInvoice().getType() + ""));
            lvFootView.tv_indent_invoice_detail.setText(getStrings(indentInvoiceBean.getInvoice().getContent()));
            lvFootView.tv_indent_invoice_title.setText(getStrings(indentInvoiceBean.getInvoice().getTitle()));
            lvFootView.tv_indent_invoice_status_check.setTag(indentInvoiceBean);
        } else {
            lvFootView.ll_draw_up_invoice.setVisibility(View.GONE);
            lvFootView.ll_wait_no_invoice.setVisibility(View.VISIBLE);
            if (indentInvoiceBean.getInvoice().getStatus() == 1) {
                lvFootView.tv_indent_w_n_invoice_status_check.setVisibility(View.GONE);
                lvFootView.tv_indent_w_n_invoice_status.setSelected(true);
            } else {
                lvFootView.tv_indent_w_n_invoice_status_check.setVisibility(View.VISIBLE);
            }
            lvFootView.tv_indent_w_n_invoice_status_check.setTag(indentInvoiceBean);
            lvFootView.tv_indent_w_n_invoice_status.setText(indentInvoiceBean.getStatus().get(indentInvoiceBean.getInvoice().getStatus() + ""));
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
        @BindView(R.id.tv_indent_invoice_type)
        TextView tv_indent_invoice_type;
        //        发票内容
        @BindView(R.id.tv_indent_invoice_detail)
        TextView tv_indent_invoice_detail;
        //        发票抬头
        @BindView(R.id.tv_indent_invoice_title)
        TextView tv_indent_invoice_title;
        //        查看
        @BindView(R.id.tv_indent_invoice_status_check)
        TextView tv_indent_invoice_status_check;

        //        未开 待开
        @BindView(R.id.ll_wait_no_invoice)
        LinearLayout ll_wait_no_invoice;
        //        发票状态
        @BindView(R.id.tv_indent_w_n_invoice_status)
        TextView tv_indent_w_n_invoice_status;
        //        查看 未开
        @BindView(R.id.tv_indent_w_n_invoice_status_check)
        TextView tv_indent_w_n_invoice_status_check;

        //        发票查看
        @OnClick({R.id.tv_indent_invoice_status_check, R.id.tv_indent_w_n_invoice_status_check})
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

//    private void downInvoiceImg(final String invoiceImg) {
//        String invoiceSavePath = Environment.getExternalStorageDirectory().getPath() + "/DownInvoice";
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            createFilePath(invoiceSavePath);
//        }
//        if (!TextUtils.isEmpty(invoiceImg)) {
//            invoiceSavePath = invoiceSavePath + "/" + invoiceImg.substring(invoiceImg.lastIndexOf("/"));
//            if (fileIsExist(invoiceSavePath)) {
//                final String finalInvoiceSavePath = invoiceSavePath;
//                AlertSettingBean alertSettingBean = new AlertSettingBean();
//                AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
//                alertData.setCancelStr("取消");
//                alertData.setDetermineStr("下载");
//                alertData.setFirstDet(false);
//                alertData.setMsg("文件已存在是否重新下载");
//                alertSettingBean.setStyle(AlertView.Style.Alert);
//                alertSettingBean.setAlertData(alertData);
//                downDialog = new AlertView(alertSettingBean, this, new OnAlertItemClickListener() {
//                    @Override
//                    public void onAlertItemClick(Object o, int position) {
//                        if (o.equals(downDialog) && position != AlertView.CANCELPOSITION) {
//                            loadHud.show();
//                            downLoadFile(invoiceImg, finalInvoiceSavePath);
//                        } else {
//                            loadHud.dismiss();
//                        }
//                    }
//                });
//                downDialog.setCancelable(false);
//                downDialog.show();
//            } else {
//                downLoadFile(invoiceImg, invoiceSavePath);
//            }
//        } else {
//            showToast(this, "数据错误,请联系客服");
//        }
//    }
//
//    private void downLoadFile(String invoiceImg, String invoiceSavePath) {
//        XUtil.DownLoadFile(invoiceImg, invoiceSavePath, new MyProgressCallBack<File>() {
//            @Override
//            public void onStarted() {
////                    showToast(IndentInvoiceDownCheckActivity.this, "正在下载");
//            }
//
//            @Override
//            public void onSuccess(final File result) {
//                loadHud.dismiss();
//                Snackbar.make(getWindow().getDecorView(), "发票保存路径为：" + result.getAbsolutePath(), Snackbar.LENGTH_LONG)
//                        .setAction("查看目录", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                File file = new File(result.getAbsolutePath());
////获取父目录
//                                File parentFile = new File(file.getParent());
//                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                intent.setDataAndType(Uri.fromFile(parentFile), "*/*");
//                                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                                startActivity(intent);
//                            }
//                        }).show();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                loadHud.dismiss();
//                showToast(DirectIndentInvoiceActivity.this, "下载失败");
//            }
//        });
//    }
//
//    //判断文件是否存在
//    private boolean fileIsExist(String invoiceSavePath) {
//        File file = new File(invoiceSavePath);
//        return file.exists();
//    }
//
//    //创建文件夹
//    private void createFilePath(String savePath) {
//        File destDir = new File(savePath);
//        if (!destDir.exists()) {
//            destDir.mkdirs();
//        }
//    }

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
