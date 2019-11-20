package com.amkj.dmsh.dominant.bean;

import com.amkj.dmsh.base.BaseTimeEntity;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2019/11/10
 * Version:v4.3.6
 * ClassDescription :优惠券专区实体类
 */
public class CouponZoneEntity extends BaseTimeEntity {

    /**
     * code : 01
     * msg : 请求成功
     * sysTime : 2019-11-10 10:25:12
     * id : 10
     * cover : http://image.domolife.cn/platform/20191023/20191023110231698.jpeg
     * title : 11点优惠券专场
     * startTime : 2019-11-08 11:00:00
     * couponList : [{"id":70,"title":"天天","cover":"","couponId":584,"useDesc":"来来来快活啊","couponAmount":"10","couponDesc":"无门槛","isHave":1,"isOverLimit":0},{"id":69,"title":"日日","cover":"","couponId":585,"useDesc":"来啊快活啊","couponAmount":"10","couponDesc":"无门槛","isHave":1,"isOverLimit":0},{"id":68,"title":"优惠券名称","cover":"","couponId":586,"useDesc":"惠券描述","couponAmount":"1","couponDesc":"满111元 可用","isHave":1,"isOverLimit":0},{"id":71,"title":"帅比测试","cover":"","couponId":583,"useDesc":"啊法师都法师法","couponAmount":"1","couponDesc":"满100元 可用","isHave":1,"isOverLimit":0},{"id":82,"title":"外卖券","cover":"","couponId":591,"useDesc":"只可以点鸡腿","couponAmount":"12","couponDesc":"满100元 可用","isHave":0,"isOverLimit":0},{"id":83,"title":"用品券","cover":"","couponId":590,"useDesc":"仅限双十一用品可用","couponAmount":"500","couponDesc":"满100元 可用","isHave":1,"isOverLimit":1},{"id":84,"title":"家居用品券","cover":"","couponId":589,"useDesc":"仅限家居类可以使用","couponAmount":"500","couponDesc":"无门槛","isHave":0,"isOverLimit":0},{"id":85,"title":"耳机券","cover":"","couponId":588,"useDesc":"购买苹果耳机可用","couponAmount":"100","couponDesc":"满1000元 可用","isHave":1,"isOverLimit":1},{"id":86,"title":"美食券","cover":"","couponId":587,"useDesc":"仅限美食类可用","couponAmount":"5","couponDesc":"无门槛","isHave":1,"isOverLimit":1},{"id":87,"title":"吴测试2","cover":"","couponId":576,"useDesc":"吴测试2","couponAmount":"3","couponDesc":"满4元 可用","isHave":1,"isOverLimit":0},{"id":88,"title":"我适配的","cover":"","couponId":575,"useDesc":"好好哦。我","couponAmount":"30","couponDesc":"无门槛","isHave":1,"isOverLimit":1},{"id":89,"title":"神劵bbb","cover":"","couponId":574,"useDesc":"fdsfasfsdfe","couponAmount":"50","couponDesc":"无门槛","isHave":1,"isOverLimit":0},{"id":90,"title":"吴测试","cover":"","couponId":573,"useDesc":"测试小程序","couponAmount":"1.50","couponDesc":"满2元 可用","isHave":1,"isOverLimit":0},{"id":91,"title":"曾测试1","cover":"","couponId":572,"useDesc":"测试测试","couponAmount":"5","couponDesc":"满100元 可用","isHave":1,"isOverLimit":0},{"id":92,"title":"中秋专题券","cover":"","couponId":582,"useDesc":"仅限中秋专题可用","couponAmount":"90","couponDesc":"满100元 可用","isHave":1,"isOverLimit":0},{"id":93,"title":"中秋专享券","cover":"","couponId":581,"useDesc":"部分特价商品不可用","couponAmount":"10","couponDesc":"满80元 可用","isHave":1,"isOverLimit":0},{"id":94,"title":"美心月饼专属优惠券","cover":"","couponId":580,"useDesc":"仅限美心月饼可用","couponAmount":"19","couponDesc":"满20元 可用","isHave":1,"isOverLimit":0},{"id":95,"title":"吴木嘎测试","cover":"","couponId":579,"useDesc":"仅限部分商品可用","couponAmount":"5","couponDesc":"满10元 可用","isHave":1,"isOverLimit":0},{"id":96,"title":"adfasf","cover":"","couponId":578,"useDesc":"fqfqdf","couponAmount":"1","couponDesc":"满100元 可用","isHave":1,"isOverLimit":0},{"id":97,"title":"吴测试纯牛奶优惠券","cover":"","couponId":577,"useDesc":"吴测试纯牛奶优惠券","couponAmount":"4","couponDesc":"满5元 可用","isHave":1,"isOverLimit":0},{"id":98,"title":"高丽雅娜精华眼霜95-29","cover":"","couponId":561,"useDesc":"高丽雅娜精华眼霜95-29","couponAmount":"29","couponDesc":"满95元 可用","isHave":1,"isOverLimit":0},{"id":99,"title":"多么定制内衣宽肩带174-63","cover":"","couponId":560,"useDesc":"多么定制内衣宽肩带174-63","couponAmount":"63","couponDesc":"满174元 可用","isHave":1,"isOverLimit":0},{"id":100,"title":"多么定制内衣宽肩带116-40","cover":"","couponId":559,"useDesc":"多么定制内衣宽肩带116-40","couponAmount":"40","couponDesc":"满116元 可用","isHave":1,"isOverLimit":0},{"id":101,"title":"多么定制内衣宽肩带348-132","cover":"","couponId":558,"useDesc":"多么定制内衣宽肩带348-132","couponAmount":"132","couponDesc":"满348元 可用","isHave":1,"isOverLimit":0},{"id":102,"title":"pree全家福系列冰淇凌178-43","cover":"","couponId":557,"useDesc":"pree全家福系列冰淇凌178-43","couponAmount":"43","couponDesc":"满178元 可用","isHave":1,"isOverLimit":0}]
     */
    private int id;
    private String cover;
    private String title;
    private String startTime;
    private List<CouponZoneBean> couponList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public List<CouponZoneBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponZoneBean> couponList) {
        this.couponList = couponList;
    }

    public static class CouponZoneBean implements MultiItemEntity {
        /**
         * id : 70
         * title : 天天
         * cover :
         * couponId : 584
         * useDesc : 来来来快活啊
         * couponAmount : 10
         * couponDesc : 无门槛
         * isHave : 1
         * isOverLimit : 0
         */

        private int id;
        private String title;
        private String cover;
        private int couponId;
        private String useDesc;
        private String couponAmount;
        private String couponDesc;
        private int isHave;
        private int isOverLimit;
        private int itemType = 3;


        public void setItemType(int itemType) {
            this.itemType = itemType;
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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

        public String getUseDesc() {
            return useDesc;
        }

        public void setUseDesc(String useDesc) {
            this.useDesc = useDesc;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getCouponDesc() {
            return couponDesc;
        }

        public void setCouponDesc(String couponDesc) {
            this.couponDesc = couponDesc;
        }

        public boolean isHave() {
            return isHave == 1;
        }

        public void setIsHave(int isHave) {
            this.isHave = isHave;
        }

        public boolean isOverLimit() {
            return isOverLimit == 1;
        }

        public void setIsOverLimit(int isOverLimit) {
            this.isOverLimit = isOverLimit;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
