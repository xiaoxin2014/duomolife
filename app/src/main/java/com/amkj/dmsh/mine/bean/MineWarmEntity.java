package com.amkj.dmsh.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/10/20.
 */
public class MineWarmEntity {

    /**
     * result : [{"id":10,"title":"Bamboo Kids天然竹子材质宝宝婴儿餐具","price":"0.00","market_price":"168.00","status":2,"end_time":"2017-10-15 16:10:14","path":"/Uploads/goods_img/2016-04-28/5721c90eb5674.jpg","subtitle":"Bamboo Kids天然竹子材质宝宝婴儿餐具","start_time":"1970-01-01 08:00:00","ptime":"1970-01-01 07:30:00"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    private String currentTime;
    private int count;
    /**
     * id : 10
     * title : Bamboo Kids天然竹子材质宝宝婴儿餐具
     * price : 0.00
     * market_price : 168.00
     * status : 2
     * end_time : 2017-10-15 16:10:14
     * path : /Uploads/goods_img/2016-04-28/5721c90eb5674.jpg
     * subtitle : Bamboo Kids天然竹子材质宝宝婴儿餐具
     * start_time : 1970-01-01 08:00:00
     * ptime : 1970-01-01 07:30:00
     */

    @SerializedName("result")
    private List<MineWarmBean> MineWarmList;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MineWarmBean> getMineWarmList() {
        return MineWarmList;
    }

    public void setMineWarmList(List<MineWarmBean> MineWarmList) {
        this.MineWarmList = MineWarmList;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public static class MineWarmBean {
        private int id;
        private String title;
        private String price;
        private String market_price;
        private int status;
        private String end_time;
        private String path;
        private String subtitle;
        private String start_time;
        private String ptime;
        private int backCode;
        private String currentTime;
        private long addSecond;

        public long getAddSecond() {
            return addSecond;
        }

        public void setAddSecond(long addSecond) {
            this.addSecond = addSecond;
        }

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getPtime() {
            return ptime;
        }

        public void setPtime(String ptime) {
            this.ptime = ptime;
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }
    }
}
