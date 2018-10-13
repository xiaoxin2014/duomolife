package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/12
 * version 3.1.7
 * class description:热销单品时间轴
 */
public class QualityHotSaleShaftEntity extends BaseEntity {
    @SerializedName("result")
    private List<HotSaleShaftBean> hotSaleShaft;

    public List<HotSaleShaftBean> getHotSaleShaft() {
        return hotSaleShaft;
    }

    public void setHotSaleShaft(List<HotSaleShaftBean> hotSaleShaft) {
        this.hotSaleShaft = hotSaleShaft;
    }

    public static class HotSaleShaftBean {
        private String day;
        private String title;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
