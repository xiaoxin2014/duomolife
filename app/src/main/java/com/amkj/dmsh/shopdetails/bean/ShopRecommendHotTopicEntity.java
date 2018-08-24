package com.amkj.dmsh.shopdetails.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/12/29
 * version 3.6
 * class description:商品详情热销 热门专题
 */

public class ShopRecommendHotTopicEntity {

    /**
     * result : [{"path":"http://image.domolife.cn/platform/20171016/20171016160412369.jpg","quantity":378,"category_id":32,"goods_brand_id":237,"type_id":1,"price":"82.00","favor_num":0,"subtitle":"一日五餐，百搭花式吃法","id":9866,"title":"鲜8纯芝麻酱1瓶+黑芝麻油1瓶装"},{"path":"http://image.domolife.cn/platform/20170323/20170323183124398.jpg","quantity":62,"category_id":32,"goods_brand_id":33,"type_id":1,"price":"59.00","favor_num":0,"subtitle":"水洗处理，深烘焙","id":6255,"title":"三顿半 手工挂耳咖啡印度尼西亚 曼特宁 G1 8包入"},{"path":"http://image.domolife.cn/platform/20170812/20170812111323193.jpg","quantity":455,"category_id":32,"goods_brand_id":33,"type_id":1,"price":"89.00","favor_num":0,"subtitle":"","id":8868,"title":"三顿半 奶萃/冷萃咖啡拼配套装"},{"path":"http://image.domolife.cn/platform/20171129/20171129173322598.jpg","quantity":1372,"category_id":32,"goods_brand_id":278,"type_id":1,"price":"99.00","favor_num":0,"subtitle":"好糖新标准，自用送人首选","id":10420,"title":"0号黑糖小彩盒2罐"},{"path":"http://image.domolife.cn/platform/20170707/20170707145142095.png","quantity":1401,"category_id":32,"goods_brand_id":151,"type_id":1,"price":"55.00","favor_num":0,"subtitle":"北部湾红树林松花皮蛋","id":8192,"activity_code":"XSG1512975268","title":"红树湾北部湾松花皮蛋"},{"path":"http://image.domolife.cn/platform/20171016/20171016155931140.jpg","quantity":136,"category_id":32,"goods_brand_id":237,"type_id":1,"price":"68.00","favor_num":0,"subtitle":"麻酱的核心人物，新鲜营养","id":9865,"title":"鲜8纯芝麻酱2瓶装"},{"path":"http://image.domolife.cn/platform/20170812/20170812110639879.jpg","quantity":211,"category_id":32,"goods_brand_id":33,"type_id":1,"price":"59.00","favor_num":0,"subtitle":"","id":8867,"title":"三顿半 秘鲁 新挂耳咖啡粉包 8包"},{"path":"http://image.domolife.cn/platform/20171129/20171129152409390.jpg","quantity":0,"category_id":32,"goods_brand_id":275,"type_id":1,"price":"125.00","favor_num":0,"subtitle":"一天一个新惊喜","id":10411,"title":"日本北海道白色恋人巧克力圣诞挂历款"},{"path":"http://image.domolife.cn/platform/20170626/20170626165839148.jpg","quantity":1893,"category_id":32,"goods_brand_id":142,"type_id":1,"price":"73.00","favor_num":0,"subtitle":"新西兰传统工艺，无盐无负担","id":7988,"activity_code":"XSG1512975268","title":"新西兰PIC\u2019S花生酱"},{"path":"http://image.domolife.cn/platform/20171012/20171012152711640.jpg","quantity":1260,"category_id":32,"goods_brand_id":232,"type_id":1,"price":"29.90","favor_num":0,"subtitle":"口感醇正，含钙量高","id":9821,"activity_code":"XSG1512975268","title":"泰国阿华田豆浆"},{"path":"http://image.domolife.cn/platform/20170812/20170812104545563.jpg","quantity":393,"category_id":32,"goods_brand_id":33,"type_id":1,"price":"59.00","favor_num":0,"subtitle":"","id":8866,"title":"三顿半 奶萃/冷萃咖啡拼配补充装"},{"path":"http://image.domolife.cn/platform/20171129/20171129152017422.jpg","quantity":0,"category_id":32,"goods_brand_id":274,"type_id":1,"price":"85.00","favor_num":0,"subtitle":"送礼佳品！甜进你心","id":10410,"title":"日本ROYCE 圣诞限定牛奶＆甜蜜波浪巧克力礼盒"},{"path":"http://image.domolife.cn/platform/20170605/20170605184126357.jpg","quantity":329,"category_id":32,"goods_brand_id":110,"type_id":1,"price":"48.00","favor_num":0,"subtitle":"当鲜花与红茶浪漫相遇！","id":7676,"activity_code":"XSG1512975268","title":"【独家定制】多么生活独家定制玫瑰红茶"},{"path":"http://image.domolife.cn/platform/20170911/20170911145032971.jpg","quantity":4977,"category_id":32,"goods_brand_id":206,"type_id":1,"price":"36.80","favor_num":0,"subtitle":"","id":9373,"title":"越南进口viloe维乐益椰子水100%椰汁饮料纯果汁饮品椰汁330ml*3罐"},{"path":"http://image.domolife.cn/platform/20170810/20170810170740647.jpg","quantity":451,"category_id":32,"goods_brand_id":175,"type_id":1,"price":"52.00","favor_num":0,"subtitle":"麻辣鲜香，一个人的美味","id":8857,"title":"晓真自煮懒人火锅2盒（新疆地区暂停发货）"},{"path":"http://image.domolife.cn/platform/20171124/20171124170646007.jpg","quantity":670,"category_id":32,"goods_brand_id":272,"type_id":1,"price":"68.00","favor_num":0,"subtitle":"肉质细腻，干面甜糯","id":10343,"title":"温县垆土铁棍山药"}]
     * msg : 请求成功
     * code : 01
     */

    private String msg;
    private String code;
    private String recommendFlag;
    @SerializedName("result")
    private List<ShopRecommendHotTopicBean> ShopRecommendHotTopicList;

    public String getRecommendFlag() {
        return recommendFlag;
    }

    public void setRecommendFlag(String recommendFlag) {
        this.recommendFlag = recommendFlag;
    }

    public static ShopRecommendHotTopicEntity objectFromData(String str) {

        return new Gson().fromJson(str, ShopRecommendHotTopicEntity.class);
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

    public List<ShopRecommendHotTopicBean> getShopRecommendHotTopicList() {
        return ShopRecommendHotTopicList;
    }

    public void setShopRecommendHotTopicList(List<ShopRecommendHotTopicBean> ShopRecommendHotTopicList) {
        this.ShopRecommendHotTopicList = ShopRecommendHotTopicList;
    }

    public static class ShopRecommendHotTopicBean implements MultiItemEntity{
        /**
         * path : http://image.domolife.cn/platform/20171016/20171016160412369.jpg
         * quantity : 378
         * category_id : 32
         * goods_brand_id : 237
         * type_id : 1
         * price : 82.00
         * favor_num : 0
         * subtitle : 一日五餐，百搭花式吃法
         * id : 9866
         * title : 鲜8纯芝麻酱1瓶+黑芝麻油1瓶装
         * activity_code : XSG1512975268
         */

        private String path;
        private int quantity;
        private int category_id;
        private int type_id;
        private String price;
        private int favor_num;
        private String subtitle;
        private int id;
        private String title;
        private String activity_code;
        private int itemType;
        /**
         * androidLink : app://ArticleOfficialActivity?ArtId=14516
         * iosLink : app://DMLActicleDetailViewController?aid=14516
         * description : 一、文章管理
         * ctime : 2017-12-26 10:11:51
         * pic_url : http://image.domolife.cn/platform/HCadiJSY771514254318689.jpg
         * type : 1
         */

        private String androidLink;
        private String pic_url;
        private String type;

        public static ShopRecommendHotTopicBean objectFromData(String str) {

            return new Gson().fromJson(str, ShopRecommendHotTopicBean.class);
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getFavor_num() {
            return favor_num;
        }

        public void setFavor_num(int favor_num) {
            this.favor_num = favor_num;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
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

        public String getActivity_code() {
            return activity_code;
        }

        public void setActivity_code(String activity_code) {
            this.activity_code = activity_code;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getAndroidLink() {
            return androidLink;
        }

        public void setAndroidLink(String androidLink) {
            this.androidLink = androidLink;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
