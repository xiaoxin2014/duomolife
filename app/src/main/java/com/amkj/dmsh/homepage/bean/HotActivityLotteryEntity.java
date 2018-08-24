package com.amkj.dmsh.homepage.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/11
 * class description:请输入类描述
 */

public class HotActivityLotteryEntity {

    /**
     * result : {"start_time":"2017-02-07 16:45:19","create_time":"2017-02-06 10:52:26","num":1,"end_time":"2017-02-28 23:59:59","base_path":"http://img.domolife.cn/platform/DMPHNpc5Gc.png","surplusNum":0,"id":1,"title":"二月转盘"}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private LotteryBean lotteryBean;
    private String msg;
    private String code;

    public LotteryBean getLotteryBean() {
        return lotteryBean;
    }

    public void setLotteryBean(LotteryBean lotteryBean) {
        this.lotteryBean = lotteryBean;
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

    public static class LotteryBean {
        /**
         * start_time : 2017-02-07 16:45:19
         * create_time : 2017-02-06 10:52:26
         * num : 1
         * end_time : 2017-02-28 23:59:59
         * base_path : http://img.domolife.cn/platform/DMPHNpc5Gc.png
         * surplusNum : 0
         * id : 1
         * title : 二月转盘
         */

        private String start_time;
        private String create_time;
        private int num;
        private String end_time;
        private String base_path;
        private int surplusNum;
        private int id;
        private String title;

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getBase_path() {
            return base_path;
        }

        public void setBase_path(String base_path) {
            this.base_path = base_path;
        }

        public int getSurplusNum() {
            return surplusNum;
        }

        public void setSurplusNum(int surplusNum) {
            this.surplusNum = surplusNum;
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
    }
}
