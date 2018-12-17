package com.amkj.dmsh.shopdetails.bean;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/10
 * version 3.1.9
 * class description:发票信息
 */
public class InvoiceInfoBean {
    private String invoiceName;
    private String invoiceContent;

    public InvoiceInfoBean(String invoiceName, String invoiceContent) {
        this.invoiceName = invoiceName;
        this.invoiceContent = invoiceContent;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }
}
