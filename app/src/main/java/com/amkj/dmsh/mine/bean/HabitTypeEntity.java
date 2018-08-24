package com.amkj.dmsh.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/21
 * class description:兴趣类型
 */

public class HabitTypeEntity {

    /**
     * result : [{"isOpen":0,"interest_name":"打球","id":1},{"isOpen":0,"interest_name":"看书","id":2},{"isOpen":0,"interest_name":"打豆豆","id":3}]
     * msg : 返回空值
     * code : 02
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<HabitTypeBean> habitTypeBeanList;

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

    public List<HabitTypeBean> getHabitTypeBeanList() {
        return habitTypeBeanList;
    }

    public void setHabitTypeBeanList(List<HabitTypeBean> habitTypeBeanList) {
        this.habitTypeBeanList = habitTypeBeanList;
    }

    public static class HabitTypeBean {
        /**
         * isOpen : 0
         * interest_name : 打球
         * id : 1
         */

        private int isOpen;
        private String interest_name;
        private int id;
        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(int isOpen) {
            this.isOpen = isOpen;
        }

        public String getInterest_name() {
            return interest_name;
        }

        public void setInterest_name(String interest_name) {
            this.interest_name = interest_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
