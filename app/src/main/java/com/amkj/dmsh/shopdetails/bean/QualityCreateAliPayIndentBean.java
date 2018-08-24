package com.amkj.dmsh.shopdetails.bean;

/**
 * Created by atd48 on 2016/9/24.
 */
public class QualityCreateAliPayIndentBean {

    /**
     * payKey : timestamp=2016-10-25+11%3A37%3A38&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%223.60%22%2C%22subject%22%3A%22%E6%B5%8B%E8%AF%95%E6%A0%87%E9%A2%98%22%2C%22body%22%3A%22%E6%B5%8B%E8%AF%95%E5%86%85%E5%AE%B9%22%2C%22out_trade_no%22%3A%22csX23295X1477366658195%22%7D&sign_type=RSA&notify_url=http%3A%2F%2Fts.domolife.cn%2Fapi%2Fgoods%2Forder%2Falinotify&charset=utf-8&method=alipay.trade.app.pay&app_id=2016081001730573&version=1.0&sign=gD0F9hcHefKDUj0EM%2FC8ujs6uHVOZyPFfYCcvXKgM3B7Hat1gwEi%2By6YeurehAzLHQel1gp4fp47eaeCMNcXAGX9CpLbb5FTTgRw5sbK4quG5LScTAjjd710BVAe0%2BzHs5RkNETydgG4MrCRsFy7jpzUsdrJoZZXStOeCwsfdEI%3D
     * no : cX23295X1099X1477366658195
     */

    private ResultBean result;
    /**
     * result : {"payKey":"timestamp=2016-10-25+11%3A37%3A38&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%223.60%22%2C%22subject%22%3A%22%E6%B5%8B%E8%AF%95%E6%A0%87%E9%A2%98%22%2C%22body%22%3A%22%E6%B5%8B%E8%AF%95%E5%86%85%E5%AE%B9%22%2C%22out_trade_no%22%3A%22csX23295X1477366658195%22%7D&sign_type=RSA&notify_url=http%3A%2F%2Fts.domolife.cn%2Fapi%2Fgoods%2Forder%2Falinotify&charset=utf-8&method=alipay.trade.app.pay&app_id=2016081001730573&version=1.0&sign=gD0F9hcHefKDUj0EM%2FC8ujs6uHVOZyPFfYCcvXKgM3B7Hat1gwEi%2By6YeurehAzLHQel1gp4fp47eaeCMNcXAGX9CpLbb5FTTgRw5sbK4quG5LScTAjjd710BVAe0%2BzHs5RkNETydgG4MrCRsFy7jpzUsdrJoZZXStOeCwsfdEI%3D","no":"cX23295X1099X1477366658195"}
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ResultBean {
        private String payKey;
        private String no;
        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPayKey() {
            return payKey;
        }

        public void setPayKey(String payKey) {
            this.payKey = payKey;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }
    }
}
