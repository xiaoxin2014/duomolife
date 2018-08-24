package com.amkj.dmsh.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by atd48 on 2016/9/18.
 */
public class FindTags {

    /**
     * result : [{"total":0,"id":1,"status":1,"is_recommend":1,"tag_name":"防嗮"},{"total":0,"id":2,"status":1,"is_recommend":1,"tag_name":"美白"},{"total":0,"id":3,"status":1,"is_recommend":1,"tag_name":"时尚"},{"total":0,"id":4,"status":1,"is_recommend":1,"tag_name":"复古"},{"total":0,"id":5,"status":1,"is_recommend":1,"tag_name":"创意家居"},{"total":0,"id":6,"status":1,"is_recommend":1,"tag_name":"日韩"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * total : 0
     * id : 1
     * status : 1
     * is_recommend : 1
     * tag_name : 防嗮
     */

    @SerializedName("result")
    private List<TagBean> tags;

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

    public List<TagBean> getTags() {
        return tags;
    }

    public void setTags(List<TagBean> tags) {
        this.tags = tags;
    }

    public static class TagBean implements Serializable {
        private int total;
        private int id;
        private int status;
        private int is_recommend;
        private String tag_name;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }
    }
}
