package com.amkj.dmsh.bean;

import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/31
 * class description:请输入类描述
 */

public class HomeQualityFloatAdEntity {

    /**
     * result : {"show_time":"0","sub_title":"首页弹","bcolor":"","link":"app://DMLSpringSaleViewController","end_time":"2017-11-02 00:00:00","begin_time":"2017-10-03 00:00:00","android_link":"app://PromotionActivity","title":"首页弹","web_link":"3","object_id":0,"min_time":"2017-11-02 00:00:00","width":"100%","ctime":"2017-10-31 10:07:05","id":232,"pic_url":"http://image.domolife.cn/platform/hAHnsnt4rE1509366955050.png","object":1,"height":"100%"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private CommunalADActivityBean communalADActivityBean;
    private String msg;
    private String code;

    public CommunalADActivityBean getCommunalADActivityBean() {
        return communalADActivityBean;
    }

    public void setCommunalADActivityBean(CommunalADActivityBean communalADActivityBean) {
        this.communalADActivityBean = communalADActivityBean;
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
}
