package com.amkj.dmsh.shopdetails.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/13
 * class description:发票温馨提示
 */

public class IndentInvoicePromptEntity {

    /**
     * result : {"description":[{"type":"text","content":"<p style=\"text-align: center;\">发票发票。正宗的发票<\/p>"},{"type":"text","content":"<p style=\"text-align: center;\"><img src=\"http://img.domolife.cn/platfrom/20170712/1499841623898054952.png\" title=\"1499841623898054952.png\" alt=\"1.png\"/><\/p>"}]}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private IndentInvoicePromptBean indentInvoicePromptBean;
    private String msg;
    private String code;

    public IndentInvoicePromptBean getIndentInvoicePromptBean() {
        return indentInvoicePromptBean;
    }

    public void setIndentInvoicePromptBean(IndentInvoicePromptBean indentInvoicePromptBean) {
        this.indentInvoicePromptBean = indentInvoicePromptBean;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class IndentInvoicePromptBean {
        @SerializedName("description")
        private List<DescriptionBean> descriptionList;

        public List<DescriptionBean> getDescriptionList() {
            return descriptionList;
        }

        public void setDescriptionList(List<DescriptionBean> descriptionList) {
            this.descriptionList = descriptionList;
        }

        public static class DescriptionBean {
            /**
             * type : text
             * content : <p style="text-align: center;">发票发票。正宗的发票</p>
             */

            private String type;
            private String content;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
