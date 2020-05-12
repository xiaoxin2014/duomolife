package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity.IndentInvoicePromptBean;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUMBER_BAR;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.INDENT_DRAW_UP_INVOICE;
import static com.amkj.dmsh.constant.Url.INVOICE_DRAW_UP;

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
    //    公司地址
    @BindView(R.id.et_invoice_company_address)
    EditText et_invoice_company_address;
    //    公司电话
    @BindView(R.id.et_invoice_company_phone)
    EditText et_invoice_company_phone;
    //    开户行
    @BindView(R.id.et_invoice_company_bank_deposit)
    EditText et_invoice_company_bank_deposit;
    //    开户行账号
    @BindView(R.id.et_invoice_company_account_number)
    EditText et_invoice_company_account_number;
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
        InputFilter phoneFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern phone = Pattern.compile(REGEX_NUMBER_BAR);
                Matcher phoneMatcher = phone.matcher(source);
                if (phoneMatcher.find()) {
                    return source;
                }
                return null;
            }
        };
        et_invoice_company_phone.setFilters(new InputFilter[]{phoneFilter});
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getWarmPrompt();
    }

    private void getWarmPrompt() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, INDENT_DRAW_UP_INVOICE, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                descriptionBeanList.clear();

                IndentInvoicePromptEntity indentInvoicePromptEntity = GsonUtils.fromJson(result, IndentInvoicePromptEntity.class);
                if (indentInvoicePromptEntity != null) {
                    if (indentInvoicePromptEntity.getCode().equals(SUCCESS_CODE)) {
                        IndentInvoicePromptBean indentInvoicePromptBean = indentInvoicePromptEntity.getIndentInvoicePromptBean();
                        if (indentInvoicePromptBean != null) {
                            List<IndentInvoicePromptEntity.IndentInvoicePromptBean.DescriptionBean> descriptionList = indentInvoicePromptBean.getDescriptionList();
                            CommunalDetailObjectBean detailObjectBean;
                            for (int i = 0; i < descriptionList.size(); i++) {
                                IndentInvoicePromptEntity.IndentInvoicePromptBean.DescriptionBean descriptionBean = descriptionList.get(i);
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
                showToast(R.string.invoice_title);
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
                showToast("订单号为空，请联系客服");
            } else if (TextUtils.isEmpty(invoiceTitle)) {
                showToast("请输入发票抬头");
            }
        }
    }

    private void setInvoice(String invoiceTitle, String invoiceNum) {
        Map<String, Object> params = new HashMap<>();
        params.put("no", indentNo);
        params.put("title", invoiceTitle);
        if (!TextUtils.isEmpty(invoiceNum)) {
            params.put("taxpayer_on", invoiceNum);
        }
        String companyAddress = et_invoice_company_address.getText().toString();
        if (!TextUtils.isEmpty(companyAddress)) {
            params.put("address", companyAddress);
        }
        String companyPhone = et_invoice_company_phone.getText().toString();
        if (!TextUtils.isEmpty(companyPhone)) {
            Pattern phone = Pattern.compile(REGEX_NUMBER_BAR);
            Matcher phoneMatcher = phone.matcher(companyPhone);
            if (!phoneMatcher.matches()) {
                loadHud.dismiss();
                showToast("电话包含特殊字符，请重新更改提交");
                return;
            }
            params.put("mobile", companyPhone);
        }
        String companyBankDeposit = et_invoice_company_bank_deposit.getText().toString();
        if (!TextUtils.isEmpty(companyBankDeposit)) {
            params.put("bankOfDeposit", companyBankDeposit);
        }
        String companyAccount = et_invoice_company_account_number.getText().toString();
        if (!TextUtils.isEmpty(companyAccount)) {
            params.put("account", companyAccount);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, INVOICE_DRAW_UP, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast("发票开具成功");
                        finish();
                    } else {
                        showToastRequestMsg( requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast( R.string.do_failed);
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
