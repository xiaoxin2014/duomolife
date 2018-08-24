package com.amkj.dmsh.message.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/9.
 */
public class MessageMeNewEntity {

    /**
     * result : [{"uid":23406,"id":4334,"atList":[{"uid":23400,"nickname":"LXQ"}],"title":"asdsada","nickname":"15070840249","path":"http://img.domolife.cn/platform/20170111/20170111155529847.jpg","obj":"goods","m_content":"迪瓦恨你呦","m_obj":"3099","m_id":38129,"avatar":"http://img.domolife.cn/test/20170116153312.jpg","ctime":"2017-01-16 16:05:28","m_type":11},{"uid":23406,"id":4439,"atList":[{"uid":23400,"nickname":"LXQ"}],"title":"小红帽","nickname":"15070840249","path":"http://img.domolife.cn/test/2017011316221128403104.jpg","obj":"doc","m_content":"迪瓦爱你呦","m_obj":"3098","m_id":38128,"avatar":"http://img.domolife.cn/test/20170116153312.jpg","ctime":"2017-01-16 15:56:49","m_type":12},{"uid":23406,"id":4439,"atList":[{"uid":23400,"nickname":"LXQ"}],"title":"小红帽","nickname":"15070840249","path":"http://img.domolife.cn/test/2017011316221128403104.jpg","obj":"doc","m_content":"这是个骚货","m_obj":"3096","m_id":38127,"avatar":"http://img.domolife.cn/test/20170116153312.jpg","ctime":"2017-01-16 15:50:35","m_type":12},{"uid":23406,"id":4439,"atList":[{"uid":23400,"nickname":"LXQ"}],"title":"小红帽","nickname":"15070840249","path":"http://img.domolife.cn/test/2017011316221128403104.jpg","obj":"doc","m_content":"这是个骚货","m_obj":"3095","m_id":38126,"avatar":"http://img.domolife.cn/test/20170116153312.jpg","ctime":"2017-01-16 15:42:54","m_type":12},{"uid":23350,"id":4431,"atList":[{"uid":23400,"nickname":"LXQ"}],"title":"王宝强于8月14日凌晨1点提出对马蓉的搜索离婚申请http://baidu.com/2015-11-28/565920e403df4.jpg","nickname":"15070850249","path":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","obj":"post","m_content":"15070850249@了你","m_obj":"4431","m_id":38094,"avatar":null,"ctime":"2017-01-09 11:16:20","m_type":3},{"uid":23339,"id":4363,"atList":[{"uid":23400,"nickname":"LXQ"}],"title":"hidjdd@LXQ ","nickname":"🙃☹️😕🙁","path":"http://img.domolife.cn/test/2016120910474755894159.jpg","obj":"post","m_content":"LXQ@了你","m_obj":"4363","m_id":37655,"avatar":"http://img.domolife.cn/platform/20170111/20170111175340802.png","ctime":"2016-12-09 10:47:48","m_type":3}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    @SerializedName("result")
    private List<MessageMeNewBean> messageMeNewBeanList;

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

    public List<MessageMeNewBean> getMessageMeNewBeanList() {
        return messageMeNewBeanList;
    }

    public void setMessageMeNewBeanList(List<MessageMeNewBean> messageMeNewBeanList) {
        this.messageMeNewBeanList = messageMeNewBeanList;
    }

    public static class MessageMeNewBean {
        /**
         * uid : 23406
         * id : 4334
         * atList : [{"uid":23400,"nickname":"LXQ"}]
         * title : asdsada
         * nickname : 15070840249
         * path : http://img.domolife.cn/platform/20170111/20170111155529847.jpg
         * obj : goods
         * m_content : 迪瓦恨你呦
         * m_obj : 3099
         * m_id : 38129
         * avatar : http://img.domolife.cn/test/20170116153312.jpg
         * ctime : 2017-01-16 16:05:28
         * m_type : 11
         */

        private int uid;
        private int id;
        private String title;
        private String nickname;
        private String path;
        private String obj;
        private String m_content;
        private String m_obj;
        private int m_id;
        private String avatar;
        private String ctime;
        private int m_type;
        private List<AtListBean> atList;
        private int backCode;
        private Object to_uid;
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getTo_uid() {
            return to_uid;
        }

        public void setTo_uid(Object to_uid) {
            this.to_uid = to_uid;
        }

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public String getM_content() {
            return m_content;
        }

        public void setM_content(String m_content) {
            this.m_content = m_content;
        }

        public String getM_obj() {
            return m_obj;
        }

        public void setM_obj(String m_obj) {
            this.m_obj = m_obj;
        }

        public int getM_id() {
            return m_id;
        }

        public void setM_id(int m_id) {
            this.m_id = m_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getM_type() {
            return m_type;
        }

        public void setM_type(int m_type) {
            this.m_type = m_type;
        }

        public List<AtListBean> getAtList() {
            return atList;
        }

        public void setAtList(List<AtListBean> atList) {
            this.atList = atList;
        }

        public static class AtListBean {
            /**
             * uid : 23400
             * nickname : LXQ
             */

            private int uid;
            private String nickname;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
