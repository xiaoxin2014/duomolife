package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity.IndentInvoicePromptBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity.IndentInvoicePromptBean.DescriptionBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/25
 * class description:开具发票内容
 */
public class IndentDrawUpInvoiceActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //    选择按钮
    @BindView(R.id.rp_draw_up_invoice)
    RadioGroup rp_draw_up_invoice;
    //    选择开具发票
    @BindView(R.id.rb_draw_up_invoice)
    RadioButton rb_draw_up_invoice;
    //    开发票展示
    @BindView(R.id.ll_draw_up_invoice)
    LinearLayout ll_draw_up_invoice;
    //    发票抬头
    @BindView(R.id.et_invoice_info_title)
    EditText et_invoice_info_title;
    //    纳税人识别号
    @BindView(R.id.et_invoice_info_num)
    EditText et_invoice_info_num;
    //    底栏完成
    @BindView(R.id.bt_draw_up_invoice_finish)
    Button bt_draw_up_invoice_finish;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    private String invoiceType;
    private String indentNo;
    private String invoiceTitle;
    private List<CommunalDetailObjectBean> descriptionBeanList = new ArrayList<>();
    private CommunalDetailAdapter communalDetailAdapter;
    private String invoiceNum;
    @Override
    protected int getContentView() {
        return R.layout.activity_indent_invoice_draw_up;
    }
    @Override
    protected void initViews() {
        header_shared.setVisibility(View.GONE);
        header_shared.setText("完成");
        header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_titleAll.setText("发票信息");
        Intent intent = getIntent();
        invoiceType = intent.getStringExtra("type");
        invoiceTitle = intent.getStringExtra("invoiceTitle");
        invoiceNum = intent.getStringExtra("invoiceNum");
        if (invoiceType.equals("indentWrite")) {
            ll_draw_up_invoice.setVisibility(View.GONE);
            header_shared.setVisibility(View.VISIBLE);
            bt_draw_up_invoice_finish.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(invoiceTitle)) {
                rb_draw_up_invoice.setChecked(true);
                et_invoice_info_title.setText(invoiceTitle);
                et_invoice_info_title.setSelection(invoiceTitle.length() - 1);
                ll_draw_up_invoice.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(invoiceNum)) {
                et_invoice_info_num.setText(invoiceNum);
                et_invoice_info_title.setSelection(invoiceNum.length() - 1);
            }
            rp_draw_up_invoice.setVisibility(View.VISIBLE);
        } else {
            indentNo = intent.getStringExtra("indentNo");
            ll_draw_up_invoice.setVisibility(View.VISIBLE);
            bt_draw_up_invoice_finish.setVisibility(View.VISIBLE);
            rp_draw_up_invoice.setVisibility(View.GONE);
        }
        rp_draw_up_invoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
//                    开发票
                    case R.id.rb_draw_up_invoice:
                        ll_draw_up_invoice.setVisibility(View.VISIBLE);
                        break;
//                    不开发票
                    case R.id.rb_no_invoice:
                        ll_draw_up_invoice.setVisibility(View.GONE);
                        break;
                }
            }
        });
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(IndentDrawUpInvoiceActivity.this));
        communalDetailAdapter = new CommunalDetailAdapter(IndentDrawUpInvoiceActivity.this, descriptionBeanList);
        communal_recycler_wrap.setAdapter(communalDetailAdapter);
        setEtFilter(et_invoice_info_title);
    }

    @Override
    protected void loadData() {
        getWarmPrompt();
    }

    private void getWarmPrompt() {
        String url = Url.BASE_URL + Url.INDENT_DRAW_UP_INVOICE;
        XUtil.Get(url, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                descriptionBeanList.clear();
                Gson gson = new Gson();
                IndentInvoicePromptEntity indentInvoicePromptEntity = gson.fromJson(result, IndentInvoicePromptEntity.class);
                if (indentInvoicePromptEntity.getCode().equals(SUCCESS_CODE)) {
                    if (indentInvoicePromptEntity != null) {
                        IndentInvoicePromptBean indentInvoicePromptBean = indentInvoicePromptEntity.getIndentInvoicePromptBean();
                        if (indentInvoicePromptBean != null) {
                            List<DescriptionBean> descriptionList = indentInvoicePromptBean.getDescriptionList();
                            CommunalDetailObjectBean detailObjectBean;
                            for (int i = 0; i < descriptionList.size(); i++) {
                                DescriptionBean descriptionBean = descriptionList.get(i);
                                detailObjectBean = new CommunalDetailObjectBean();
                                if (descriptionBean.getType().equals("text")) {
                                    detailObjectBean.setContent(descriptionBean.getContent());
                                    descriptionBeanList.add(detailObjectBean);
                                }
                            }
                            communalDetailAdapter.setNewData(descriptionBeanList);
                        }

                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void setInvoiceFinish(View view) {
        String invoiceTitle = et_invoice_info_title.getText().toString().trim();
        String invoiceNum = et_invoice_info_num.getText().toString().trim();
        if (rb_draw_up_invoice.isChecked()) {
            if (!TextUtils.isEmpty(invoiceTitle)) {
                Intent data = new Intent();
                data.putExtra("invoiceTitle", invoiceTitle);
                if (!TextUtils.isEmpty(invoiceNum)) {
                    data.putExtra("invoiceNum", invoiceNum);
                }
                setResult(RESULT_OK, data);
                finish();
            } else {
                showToast(this, R.string.invoice_title);
            }
        } else {
            Intent data = new Intent();
            data.putExtra("invoiceTitle", "");
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @OnClick(R.id.bt_draw_up_invoice_finish)
    void drawUpInvoice(View view) {
        String invoiceTitle = et_invoice_info_title.getText().toString().trim();
        String invoiceNum = et_invoice_info_num.getText().toString().trim();
        if (!TextUtils.isEmpty(indentNo) && !TextUtils.isEmpty(invoiceTitle)) {
            loadHud.show();
            setInvoice(invoiceTitle, invoiceNum);
        } else {
            if (TextUtils.isEmpty(indentNo)) {
                showToast(this, "订单号为空，请联系客服");
            } else {
                showToast(this, "请输入发票抬头");
            }
        }
    }

    private void setInvoice(String invoiceTitle, String invoiceNum) {
        String url = Url.BASE_URL + Url.INVOICE_DRAW_UP;
        if (NetWorkUtils.checkNet(IndentDrawUpInvoiceActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("no", indentNo);
            params.put("title", invoiceTitle);
            if (!TextUtils.isEmpty(invoiceNum)) {
                params.put("taxpayer_on", invoiceNum);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    loadHud.dismiss();
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            showToast(IndentDrawUpInvoiceActivity.this, "发票开具成功");
                            finish();
                        } else {
                            showToast(IndentDrawUpInvoiceActivity.this, requestStatus.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    loadHud.dismiss();
                    showToast(IndentDrawUpInvoiceActivity.this, R.string.invalidData);
                }
            });
        } else {
            loadHud.dismiss();
            showToast(this, R.string.unConnectedNetwork);
        }
    }
}
