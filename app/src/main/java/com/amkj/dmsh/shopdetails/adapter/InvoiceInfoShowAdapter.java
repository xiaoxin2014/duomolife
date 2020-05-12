package com.amkj.dmsh.shopdetails.adapter;

import androidx.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.InvoiceInfoBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/10
 * version 3.1.9
 * class description:发票信息展示
 */
public class InvoiceInfoShowAdapter extends BaseQuickAdapter<InvoiceInfoBean,BaseViewHolder> {
    public InvoiceInfoShowAdapter(@Nullable List<InvoiceInfoBean> data) {
        super(R.layout.adapter_invoice_info,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InvoiceInfoBean invoiceInfoBean) {
        helper.setText(R.id.tv_invoice_info_name,getStrings(invoiceInfoBean.getInvoiceName()))
                .setText(R.id.tv_invoice_info_content,getStrings(invoiceInfoBean.getInvoiceContent()));
    }
}
