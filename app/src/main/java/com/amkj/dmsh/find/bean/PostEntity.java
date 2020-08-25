package com.amkj.dmsh.find.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.base.BaseRemoveExistProductBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.google.gson.annotations.SerializedName;
import com.amkj.dmsh.user.bean.UserPagerInfoEntity.UserInfoBean;

import java.util.List;

/**
 * Created by xiaoxin on 2019/7/9
 * Version:v4.1.0
 * ClassDescription :帖子列表实体类
 */
public class PostEntity extends BaseEntity {

    @SerializedName(value = "postList", alternate = {"list", "result"})
    private List<PostBean> postList;
    private RelatedGoodsBean productInfo;
    /**
     * sysTime : 2019-09-18 15:38:22
     * showCount : 0
     * totalPage : 0
     * totalResult : 41
     * currentPage : 1
     * homeUserInfo : {"uid":27947,"avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickname":"剑辉","sex":0,"fansNum":4,"focusNum":4,"isFocus":0}
     * list : [{"id":349537,"status":0,"articletype":2,"topicTitle":"家居晒单分享","topicId":"33","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"送给爸爸的，爸爸很喜欢","cover":null,"createTime":"2018年07月09日","favorNum":0,"isFavor":0,"coverWidth":0,"coverHeight":0},{"id":324102,"status":0,"articletype":2,"topicTitle":"家居晒单分享","topicId":"33","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"1111111111","cover":"http://image.domolife.cn/iosRelease/2019072718295089984474.jpg","createTime":"07月27日","favorNum":1,"isFavor":1,"coverWidth":828,"coverHeight":620},{"id":115398,"status":0,"articletype":2,"topicTitle":"七月晒单大集锦","topicId":"19","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"https://item.taobao.com/item.htm?spm=a219t.11817059.0.defcc5......","cover":null,"createTime":"09月09日","favorNum":1,"isFavor":1,"coverWidth":0,"coverHeight":0},{"id":58797,"status":0,"articletype":2,"topicTitle":"有什么面膜适合油性皮肤啊？推荐一下啊~~","topicId":"21","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"测试","cover":"http://image.domolife.cn/iosRelease/2019082416131287393327.jpg","createTime":"08月24日","favorNum":1,"isFavor":0,"coverWidth":720,"coverHeight":720},{"id":58796,"status":0,"articletype":2,"topicTitle":"有什么面膜适合油性皮肤啊？推荐一下啊~~","topicId":"21","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"好","cover":"http://image.domolife.cn/iosRelease/2019082416084944396188.jpg","createTime":"08月24日","favorNum":0,"isFavor":0,"coverWidth":828,"coverHeight":1792},{"id":58795,"status":0,"articletype":2,"topicTitle":"打造精致生活的必备好物","topicId":"24","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"我酸了","cover":"http://image.domolife.cn/iosRelease/2019082416053363457421.jpg","createTime":"08月24日","favorNum":0,"isFavor":0,"coverWidth":828,"coverHeight":1792},{"id":58794,"status":0,"articletype":2,"topicTitle":"打造精致生活的必备好物","topicId":"24","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"啦啦啦啦啦啦","cover":"http://image.domolife.cn/iosRelease/2019082416015377530254.jpg","createTime":"08月24日","favorNum":0,"isFavor":0,"coverWidth":828,"coverHeight":1472},{"id":58791,"status":0,"articletype":2,"topicTitle":"新年立新Flag","topicId":"15","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"123","cover":null,"createTime":"08月22日","favorNum":0,"isFavor":0,"coverWidth":0,"coverHeight":0},{"id":58790,"status":0,"articletype":2,"topicTitle":"新年立新Flag","topicId":"15","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"aa","cover":"https://image.domolife.cn/wx470057dda4b9181d.o6zAJswcAkGJoMf_F-tOjbkqrkNY.4VbzGCwaYJD339d3958a38646fd150add1acff1868b3.jpg","createTime":"08月22日","favorNum":0,"isFavor":0,"coverWidth":540,"coverHeight":591},{"id":58789,"status":0,"articletype":2,"topicTitle":"新年立新Flag","topicId":"15","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"1111","cover":"https://image.domolife.cn/wx470057dda4b9181d.o6zAJswcAkGJoMf_F-tOjbkqrkNY.TTruWx0vQMeXcab4dd9812d4d9adfe298f15f62ecfd4.jpg","createTime":"08月22日","favorNum":0,"isFavor":0,"coverWidth":750,"coverHeight":403},{"id":22119,"status":0,"articletype":2,"topicTitle":"打造精致生活的必备好物","topicId":"24","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"Wwwq ","cover":"http://image.domolife.cn/iosRelease/2019082110405018044812.jpg","createTime":"08月21日","favorNum":0,"isFavor":0,"coverWidth":750,"coverHeight":1334},{"id":22115,"status":0,"articletype":2,"topicTitle":"提高幸福感的小家电","topicId":"26","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"CouponEntity","cover":"http://image.domolife.cn/iosRelease/2019082109522795968550.jpg","createTime":"08月21日","favorNum":0,"isFavor":0,"coverWidth":828,"coverHeight":620},{"id":20159,"status":0,"articletype":1,"topicTitle":"有什么面膜适合油性皮肤啊？推荐一下啊~~","topicId":"21","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"有意思的观点","cover":"https://image.domolife.cn/wx470057dda4b9181d.o6zAJswcAkGJoMf_F-tOjbkqrkNY.UvIxugW06xCX41de0379e473a91a58ada48f14ad0cd8.png","createTime":"08月13日","favorNum":4,"isFavor":1,"coverWidth":120,"coverHeight":120},{"id":20134,"status":0,"articletype":1,"topicTitle":"有什么面膜适合油性皮肤啊？推荐一下啊~~","topicId":"21","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"?????","cover":null,"createTime":"08月12日","favorNum":0,"isFavor":0,"coverWidth":0,"coverHeight":0},{"id":20133,"status":0,"articletype":1,"topicTitle":"美白护肤分享","topicId":"29","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"也可以选择这种小吃桶，一桶一种海鲜，量会更多点。","cover":"https://image.domolife.cn/wx470057dda4b9181d.o6zAJswcAkGJoMf_F-tOjbkqrkNY.i5pE6aHQ6GzR90e7d0b89450ba79190d8d767e73acbc.jpg","createTime":"08月12日","favorNum":1,"isFavor":1,"coverWidth":750,"coverHeight":403},{"id":20106,"status":0,"articletype":2,"topicTitle":"有什么面膜适合油性皮肤啊？推荐一下啊~~","topicId":"21","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":null,"cover":"http://image.domolife.cn/iosRelease/2019080719401612690194.jpg","createTime":"08月07日","favorNum":0,"isFavor":0,"coverWidth":828,"coverHeight":1472},{"id":20105,"status":0,"articletype":2,"topicTitle":"有什么面膜适合油性皮肤啊？推荐一下啊~~","topicId":"21","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"不出","cover":"http://image.domolife.cn/iosRelease/2019080719393696898022.jpg","createTime":"08月07日","favorNum":0,"isFavor":0,"coverWidth":425,"coverHeight":586},{"id":20089,"status":0,"articletype":2,"topicTitle":"老母亲的育儿神器分享","topicId":"25","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"很好","cover":"http://image.domolife.cn/iosRelease/2019080616154542008792.jpg","createTime":"08月06日","favorNum":0,"isFavor":0,"coverWidth":828,"coverHeight":828},{"id":20088,"status":0,"articletype":2,"topicTitle":"老母亲的育儿神器分享","topicId":"25","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"😶😶😶","cover":"http://image.domolife.cn/iosRelease/2019080616090120615213.jpg","createTime":"08月06日","favorNum":0,"isFavor":0,"coverWidth":1280,"coverHeight":1278},{"id":20087,"status":0,"articletype":2,"topicTitle":"老母亲的育儿神器分享","topicId":"25","avatar":"http://image.domolife.cn/iosRelease/20180410143136.jpg","nickName":"剑辉","digest":"？？","cover":"http://image.domolife.cn/iosRelease/2019080616064337404115.jpg","createTime":"08月06日","favorNum":0,"isFavor":0,"coverWidth":1280,"coverHeight":1278}]
     */

    private UserInfoBean homeUserInfo;

    public List<PostBean> getPostList() {
        return postList;
    }

    public void setPostList(List<PostBean> postList) {
        this.postList = postList;
    }

    public UserInfoBean getHomeUserInfo() {
        return homeUserInfo;
    }

    public void setHomeUserInfo(UserInfoBean homeUserInfo) {
        this.homeUserInfo = homeUserInfo;
    }

    public static class PostBean extends BaseRemoveExistProductBean {

        /**
         * id : 19893
         * status : 0
         * topicTitle : null
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/q7qU6Nh1iavpJdwJAtT5ic9MwgDTiaX1QVJVXdHRamZZMFEFKV228SoPzo5B2oYibgKg95Gan7nNfDibziatibRMy4L7w/132
         * nickName : 鸿星
         * digest : 发帖
         * cover : http://image.domolife.cn/201907231645540199914842.jpg
         * createTime : 2019-07-23 16:45:54
         * favorNum : 0
         * isFavor : 0
         */

        private int status;
        private String topicTitle;
        private String avatar;
        @SerializedName(value = "nickName", alternate = "nickname")
        private String nickName;
        private String digest;
        @SerializedName(value = "cover", alternate = {"path", "activityImg"})
        private String cover;
        private String createTime;
        @SerializedName(value = "favorNum", alternate = {"favorCount", "likeCount"})
        private String favorNum;
        @SerializedName(value = "isFavor", alternate = "isLike")
        private String isFavor;
        private int articletype;
        @SerializedName(value = "coverWidth", alternate = "coverwidth")
        private int coverWidth;
        @SerializedName(value = "coverHeight", alternate = "coverheight")
        private int coverHeight;
        private String topicId;

        //心得报告相关字段
        private String activityId;
        private String orderId;
        private String content;
        private String imgs;
        private String star;
        private String isCollect;
        private String productName;
        private String productImg;

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public boolean isCollect() {
            return "1".equals(isCollect);
        }

        public void setIsCollect(boolean isCollect) {
            this.isCollect = isCollect ? "1" : "0";
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public int getCoverWidth() {
            return coverWidth;
        }

        public void setCoverWidth(int coverWidth) {
            this.coverWidth = coverWidth;
        }

        public int getCoverHeight() {
            return coverHeight;
        }

        public void setCoverHeight(int coverHeight) {
            this.coverHeight = coverHeight;
        }

        public int getArticletype() {
            return articletype;
        }

        public void setArticletype(int articletype) {
            this.articletype = articletype;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTopicTitle() {
            return topicTitle;
        }

        public void setTopicTitle(String topicTitle) {
            this.topicTitle = topicTitle;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getFavorNum() {
            return ConstantMethod.getStringChangeIntegers(favorNum);
        }

        public void setFavorNum(int favorNum) {
            this.favorNum = String.valueOf(favorNum);
        }

        public boolean isFavor() {
            return "1".equals(isFavor);
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor ? "1" : "0";
        }
    }

    public RelatedGoodsBean getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(RelatedGoodsBean productInfo) {
        this.productInfo = productInfo;
    }
}
