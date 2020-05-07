package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/27
 * class description:关联话题
 */

public class FindRelTopicEntity {

    /**
     * result : [{"id":3,"title":"丢雷老谋"},{"id":1,"title":"剁手双十二"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    @SerializedName("result")
    private List<FindRelTopicBean> findRelTopicList;

    public static FindRelTopicEntity objectFromData(String str) {

        return GsonUtils.fromJson(str, FindRelTopicEntity.class);
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

    public List<FindRelTopicBean> getFindRelTopicList() {
        return findRelTopicList;
    }

    public void setFindRelTopicList(List<FindRelTopicBean> findRelTopicList) {
        this.findRelTopicList = findRelTopicList;
    }

    public static class FindRelTopicBean {
        /**
         * id : 3
         * title : 丢雷老谋
         */

        private int id;
        private String title;
        private boolean isSelect;
        private int position;

        public static FindRelTopicBean objectFromData(String str) {

            return GsonUtils.fromJson(str, FindRelTopicBean.class);
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

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
