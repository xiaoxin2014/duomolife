package com.amkj.dmsh.message.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by atd48 on 2016/9/9.
 */
public class MessageSysEntity {

    /**
     * result : [{"uid":23317,"kdid":4114,"title":"个人游玩","status":-1,"nickname":"lucci😳😳","description":"<p>\r\n\t上次团购买了五双，这次又添了两双，真心便宜又软！！码数也合适，连妈妈都说买的好\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-19_578dff1162748.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-19_578dff11ece0e.JPG\" />\r\n<\/p>","at_nickname":"1507084023","isfront":1,"path":"http://img.domolife.cn/platform/ktmpEHT6Tx.png","at_uid":23328,"avatar":"http://img.domolife.cn/test/20161008170915.jpg","ctime":"2016-09-22 09:36:32"},{"uid":23326,"kdid":4069,"status":2,"nickname":"15070840241","description":"<p>\r\n\t可以直接当床笠用，很方便，实用\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c90e731d70.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c90eb31cfa.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c90eda21ba.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c90f017fb0.JPG\" />\r\n<\/p>","at_nickname":"1507084023","isfront":1,"path":"http://img.domolife.cn/platform/YnjwyYj6Gr.jpg","at_uid":23328,"avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","ctime":"2016-09-19 10:03:30"},{"uid":23326,"kdid":4067,"status":2,"nickname":"15070840241","description":"<p>\r\n\t家里自己在用，这次买了一个送人，这个又实用又实惠，很不错的。\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c8638a4d52.JPG\" />\r\n<\/p>","at_nickname":"1507084023","isfront":1,"path":"http://img.domolife.cn/platform/20161022/20161022114959356.jpg","at_uid":23328,"avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","ctime":"2016-09-18 05:53:43"},{"uid":23326,"kdid":4065,"status":1,"nickname":"15070840241","description":"<p>\r\n\t大大的一卷很实惠，就是包装简陋，拆开后不好装 \r\n吸水性吸油效果都非常好，厚厚的\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c7da637fe2.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c7da8a62f6.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c7dab73fe6.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c7db0bbb02.JPG\" />\r\n<\/p>\r\n<p>\r\n\t韧性极好，反复冲洗都不会破，直接省掉了抹布，干净卫生\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c7db358134.JPG\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o81ak3dv2.bkt.clouddn.com/2016-07-18_578c7db6ac439.JPG\" />\r\n<\/p>","at_nickname":"1507084023","isfront":1,"path":"http://img.domolife.cn/platform/20161022/20161022114701044.jpg","at_uid":23328,"avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","ctime":"2016-09-14 10:08:42"},{"uid":23326,"kdid":4064,"title":"个人游玩","status":1,"nickname":"15070840241","description":"<p>\r\n\t<img src=\"http://o6wxayr69.bkt.clouddn.com/2016-07-18_578c7c8f88dd7.jpg\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o6wxayr69.bkt.clouddn.com/2016-07-18_578c7c9101651.jpg\" />\r\n<\/p>\r\n<p>\r\n\t<img src=\"http://o6wxayr69.bkt.clouddn.com/2016-07-18_578c7c9224780.jpg\" />\r\n<\/p>\r\n<p>\r\n\t传说中的牙膏哈哈\r\n<\/p>","at_nickname":"1507084023","isfront":1,"path":"http://img.domolife.cn/platform/20161022/20161022114658336.jpg","at_uid":23328,"avatar":"http://tva1.sinaimg.cn/crop.0.0.180.180.50/6e6f989cjw1e8qgp5bmzyj2050050aa8.jpg","ctime":"2016-09-14 09:53:43"}]
     * code : 01
     * msg : 请求成功
     */

    private String code;
    private String msg;
    /**
     * uid : 23317
     * kdid : 4114
     * title : 个人游玩
     * status : -1
     * nickname : lucci😳😳
     * description : <p>
     * 上次团购买了五双，这次又添了两双，真心便宜又软！！码数也合适，连妈妈都说买的好
     * </p>
     * <p>
     * <img src="http://o81ak3dv2.bkt.clouddn.com/2016-07-19_578dff1162748.JPG" />
     * </p>
     * <p>
     * <img src="http://o81ak3dv2.bkt.clouddn.com/2016-07-19_578dff11ece0e.JPG" />
     * </p>
     * at_nickname : 1507084023
     * isfront : 1
     * path : http://img.domolife.cn/platform/ktmpEHT6Tx.png
     * at_uid : 23328
     * avatar : http://img.domolife.cn/test/20161008170915.jpg
     * ctime : 2016-09-22 09:36:32
     */

    @SerializedName("result")
    private List<MessageSysBean> messageSysBeanList;

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

    public List<MessageSysBean> getMessageSysBeanList() {
        return messageSysBeanList;
    }

    public void setMessageSysBeanList(List<MessageSysBean> messageSysBeanList) {
        this.messageSysBeanList = messageSysBeanList;
    }

    public static class MessageSysBean {
        private int uid;
        private int m_uid;
        private int kdid;
        private String title;
        private int status;
        private String nickname;
        private String description;
        private String at_nickname;
        private int isfront;
        private String path;
        //        系统消息类型
        private String obj;
        //        系统消息内容
        private String m_content;

        //        系统消息title
        private String m_title;
        //        赞消息 收藏对象ID
        private String object_id;
        //        系统消息跳转帖子 文章 ID
        private String obj_id;
        private int m_type;
        private int at_uid;
        private String avatar;
        private String ctime;
        private int backCode;

        public int getBackCode() {
            return backCode;
        }

        public void setBackCode(int backCode) {
            this.backCode = backCode;
        }

        public int getM_uid() {
            return m_uid;
        }

        public void setM_uid(int m_uid) {
            this.m_uid = m_uid;
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

        public String getM_title() {
            return m_title;
        }

        public void setM_title(String m_title) {
            this.m_title = m_title;
        }

        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        public String getObj_id() {
            return obj_id;
        }

        public void setObj_id(String obj_id) {
            this.obj_id = obj_id;
        }

        public int getM_type() {
            return m_type;
        }

        public void setM_type(int m_type) {
            this.m_type = m_type;
        }

        /**
         * "m_uid": 23295,
         * "description": null,
         * "path": "http://img.domolife.cn/201610291709440587118602.jpg",
         * "obj": "doc",
         * "m_content": "您的帖子通过加精获得100积分",
         * "m_obj": "4199",
         * "m_title": "积分通知",
         * "m_id": 37295,
         * "obj_id": "4199",
         * "ctime": "2016-11-04 17:03:55",
         * "m_type": 8
         *
         * @return
         */

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getKdid() {
            return kdid;
        }

        public void setKdid(int kdid) {
            this.kdid = kdid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAt_nickname() {
            return at_nickname;
        }

        public void setAt_nickname(String at_nickname) {
            this.at_nickname = at_nickname;
        }

        public int getIsfront() {
            return isfront;
        }

        public void setIsfront(int isfront) {
            this.isfront = isfront;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getAt_uid() {
            return at_uid;
        }

        public void setAt_uid(int at_uid) {
            this.at_uid = at_uid;
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
    }
}
