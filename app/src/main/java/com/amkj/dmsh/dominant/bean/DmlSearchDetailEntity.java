package com.amkj.dmsh.dominant.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean.TagsBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.RelevanceProBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/20
 * class description:请输入类描述
 */

public class DmlSearchDetailEntity extends BaseEntity{

    /**
     * result : {"isfront":2,"articletype":1,"flag":false,"isFavor":true,"isCollect":false,"description":[{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">现在都提倡环保出行，有很多地方也不好停车，有时候搭乘地铁和公交车反而比开车更方便更快，很多同学都有一张地铁卡，在深圳有深圳通，在香港有八达通，可是却常常面临一个问题就是，不晓得怎么搞的，卡片就搞丢不见了，很是烦恼。所以你可以试试，给你的交通卡或者门禁卡配一个可爱的保护套挂在包里，那么遗失率就会降低很多，哔起来也会更方便喔~！&nbsp;<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://item.taobao.com/item.htm?spm=a1z0k.6846577.0.0.gSuma3&id=536441842130&_u=t2dmg8j2611\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756530664002276.jpg\" title=\"1487756530664002276.jpg\" alt=\"1.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">这款塑料卡套是日本动漫萌喵主题的，猫奴们的最爱哈~硬质的双面亚克力材质，手感冰凉舒适。卡套背面带有长槽，要拿出卡片时，只要轻轻推移，卡片就会出来~这款卡套和钥匙扣还是配套使用的，一般只能容纳一张公车卡或两张名片的量哦~超量使用可能会卡住哈~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/1IBHA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756537560034913.jpg\" title=\"1487756537560034913.jpg\" alt=\"2.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">LINE家的周边很多，当然少不了卡套了，PU材质也很经久耐用，正反可以各放一张卡片。而且这款的配饰特别多，都是手工制作而成，一大串拿在手里很带感，如果喜欢简单点的也可以自行拆掉。花色的种类很多，可选性相对比较广。<\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/13YGA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756544707074768.jpg\" title=\"1487756544707074768.jpg\" alt=\"3.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">毛毡这种材质多用于DIY的手作品，无论是从色泽还是饱和度来说都是相对较好的，但这种材质唯一不好的地方就是用久了容易起毛。monopoly 家这款可爱笑脸毛毡交通卡包，性价比很高，内里有1个卡位隔层，可以卡片跟零钱，外带拉链封口用起来也更为放心。挂脖绳是高级仿皮材质制成的，这种简约时尚的系绳戴在脖子也不会显得low哈~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://item.taobao.com/item.htm?spm=a1z0k.7385961.1997985097.d4918997.7jtJrq&id=544351329510&_u=t2dmg8j26111\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756551701039477.jpg\" title=\"1487756551701039477.jpg\" alt=\"4.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">今年一月才推出的粉色的Melody人形卡套，颜值很高。简单经典的的设计，更是满足了Melody控的少女心。别看图片很小，其实他的规格是有14.3*14.7*0.3cm的，足够可以放下你的公交卡。一个好看的公车卡套，可是会让你挤公车也能心情棒棒哒~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/Sk5GA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756565550005465.jpg\" title=\"1487756565550005465.jpg\" alt=\"5.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">有时候这种简单的调调最容易让人找到共鸣感。波点元素永远是设计师百用不厌的时尚元素，将波点跟清新的浅紫色、粉色相结合，素雅大方，还带有一丝调皮的感觉。挂绳处的设计也毫不马虎，金色的鸡眼孔打磨得光滑质感，配上PU的编织挂绳，整体看上去更有质感。<\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/SuoFA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756576221059813.jpg\" title=\"1487756576221059813.jpg\" alt=\"6.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">这款卡套是走卡通趣味路线的，款式种类也很多，不同的颜色搭配不同的水果图案，看起来十分清爽，很有夏天的感觉。背面的卡位是透明的，日常放置名片或者证件一目了然，方便辨识，不需要抽取确认非常方便哦~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/ovOFA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756597933006869.jpg\" title=\"1487756597933006869.jpg\" alt=\"7.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">之前大火的韩剧《鬼怪》里金高银的同款卡套，不过因为出镜时不是什么名画面估计也没有什么人注意到，金高银在剧中用的是亮橙色的，图片上为同款小企鹅款，小企鹅跟浅棕色的奇妙搭配，出奇的很有质感，带了点可爱的同时也不失大气。就连卡套的挂绳也很好看，上身韩范十足。<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://item.taobao.com/item.htm?spm=a1z0k.6846577.0.0.brIVzR&id=37103533689&_u=t2dmg8j26111\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756609573031154.jpg\" title=\"1487756609573031154.jpg\" alt=\"8.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">这款卡套是Sanrio家hellokitty QUESTINA系列的产品，造型非常特别，一个小小的双肩包造型，有前袋跟后袋，前面的口袋空间较小，适合放置硬币的零钱。书包的大口袋可以放置纸币跟卡片，容量会比一般卡套要大，金色的链子箍在手上也很好看，出门回头率也是杠杆的。说到底，QUESTINA系列的产品除了贵还真没有别的缺点了~<\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"goods","content":{"picUrl":"http://image.domolife.cn/platform/20170718/20170718175041425.jpg","marketPrice":"49.00","price":"35.00","name":"可爱河马北极熊苹果手机壳软壳 带支架","newPicUrl":"http://img.domolife.cn/platform/C6ZdcZp8YH1499400239765.jpg","maxPrice":"35.00","id":5964,"itemTypeId":1}},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"coupon","content":{"amount":"1.00","count":28,"title":"11月邀请好友规则","totalCount":30,"mode":1,"picUrl":"http://img.domolife.cn/platform/nPRwxWTrKk1499238561156.jpg","newPirUrl":"http://img.domolife.cn/platform/nPRwxWTrKk1499238561156.jpg","useRange":0,"startFee":"3.00","startTime":"2016-11-01 10:51","id":2,"endTime":"2022-11-30 00:00:00","desc":""}},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><br/><\/p>"},{"type":"goods","content":{"picUrl":"http://image.domolife.cn/platform/20170309/20170309110645522.jpg","marketPrice":"49.00","price":"0.10","name":" 火箭宇宙iPhone6手机壳 带支架","newPicUrl":"http://img.domolife.cn/platform/632zwda3sf1499408024196.jpg","maxPrice":"0.10","id":5966,"itemTypeId":1}},{"type":"text","content":"<p><br/><\/p>"}],"avatar":"http://img.domolife.cn/Uploads/app_img/2016-03-31/56fcc8cd4a039.png","title":"谁说主妇不精明 收纳整理样样行","productCount":2,"picture":[],"tags":[],"uid":1,"path":"http://image.domolife.cn/platform/Rpt36SaW531500362574007.jpg","view":1880,"favor":12,"nickname":"domolife","ctime":"2017-02-22 17:50:03","comment":0,"id":9832,"collect":20,"atList":[],"productList":[{"picUrl":"http://image.domolife.cn/platform/20170718/20170718175041425.jpg","marketPrice":"49.00","price":"35.00","name":"可爱河马北极熊苹果手机壳软壳 带支架","newPicUrl":"http://img.domolife.cn/platform/C6ZdcZp8YH1499400239765.jpg","maxPrice":"35.00","id":5964,"itemTypeId":1},{"picUrl":"http://image.domolife.cn/platform/20170309/20170309110645522.jpg","marketPrice":"49.00","price":"0.10","name":" 火箭宇宙iPhone6手机壳 带支架","newPicUrl":"http://img.domolife.cn/platform/632zwda3sf1499408024196.jpg","maxPrice":"0.10","id":5966,"itemTypeId":1}],"status":1}
     * msg : 请求成功
     * code : 01
     */

    @SerializedName("result")
    private DmlSearchDetailBean dmlSearchDetailBean;

    public DmlSearchDetailBean getDmlSearchDetailBean() {
        return dmlSearchDetailBean;
    }

    public void setDmlSearchDetailBean(DmlSearchDetailBean dmlSearchDetailBean) {
        this.dmlSearchDetailBean = dmlSearchDetailBean;
    }

    public static class DmlSearchDetailBean implements Parcelable {
        /**
         * isfront : 2
         * articletype : 1
         * flag : false
         * isFavor : true
         * isCollect : false
         * description : [{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">现在都提倡环保出行，有很多地方也不好停车，有时候搭乘地铁和公交车反而比开车更方便更快，很多同学都有一张地铁卡，在深圳有深圳通，在香港有八达通，可是却常常面临一个问题就是，不晓得怎么搞的，卡片就搞丢不见了，很是烦恼。所以你可以试试，给你的交通卡或者门禁卡配一个可爱的保护套挂在包里，那么遗失率就会降低很多，哔起来也会更方便喔~！&nbsp;<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://item.taobao.com/item.htm?spm=a1z0k.6846577.0.0.gSuma3&id=536441842130&_u=t2dmg8j2611\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756530664002276.jpg\" title=\"1487756530664002276.jpg\" alt=\"1.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">这款塑料卡套是日本动漫萌喵主题的，猫奴们的最爱哈~硬质的双面亚克力材质，手感冰凉舒适。卡套背面带有长槽，要拿出卡片时，只要轻轻推移，卡片就会出来~这款卡套和钥匙扣还是配套使用的，一般只能容纳一张公车卡或两张名片的量哦~超量使用可能会卡住哈~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/1IBHA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756537560034913.jpg\" title=\"1487756537560034913.jpg\" alt=\"2.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">LINE家的周边很多，当然少不了卡套了，PU材质也很经久耐用，正反可以各放一张卡片。而且这款的配饰特别多，都是手工制作而成，一大串拿在手里很带感，如果喜欢简单点的也可以自行拆掉。花色的种类很多，可选性相对比较广。<\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/13YGA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756544707074768.jpg\" title=\"1487756544707074768.jpg\" alt=\"3.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">毛毡这种材质多用于DIY的手作品，无论是从色泽还是饱和度来说都是相对较好的，但这种材质唯一不好的地方就是用久了容易起毛。monopoly 家这款可爱笑脸毛毡交通卡包，性价比很高，内里有1个卡位隔层，可以卡片跟零钱，外带拉链封口用起来也更为放心。挂脖绳是高级仿皮材质制成的，这种简约时尚的系绳戴在脖子也不会显得low哈~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://item.taobao.com/item.htm?spm=a1z0k.7385961.1997985097.d4918997.7jtJrq&id=544351329510&_u=t2dmg8j26111\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756551701039477.jpg\" title=\"1487756551701039477.jpg\" alt=\"4.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">今年一月才推出的粉色的Melody人形卡套，颜值很高。简单经典的的设计，更是满足了Melody控的少女心。别看图片很小，其实他的规格是有14.3*14.7*0.3cm的，足够可以放下你的公交卡。一个好看的公车卡套，可是会让你挤公车也能心情棒棒哒~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/Sk5GA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756565550005465.jpg\" title=\"1487756565550005465.jpg\" alt=\"5.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">有时候这种简单的调调最容易让人找到共鸣感。波点元素永远是设计师百用不厌的时尚元素，将波点跟清新的浅紫色、粉色相结合，素雅大方，还带有一丝调皮的感觉。挂绳处的设计也毫不马虎，金色的鸡眼孔打磨得光滑质感，配上PU的编织挂绳，整体看上去更有质感。<\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/SuoFA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756576221059813.jpg\" title=\"1487756576221059813.jpg\" alt=\"6.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">这款卡套是走卡通趣味路线的，款式种类也很多，不同的颜色搭配不同的水果图案，看起来十分清爽，很有夏天的感觉。背面的卡位是透明的，日常放置名片或者证件一目了然，方便辨识，不需要抽取确认非常方便哦~<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://s.click.taobao.com/ovOFA5x\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756597933006869.jpg\" title=\"1487756597933006869.jpg\" alt=\"7.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">之前大火的韩剧《鬼怪》里金高银的同款卡套，不过因为出镜时不是什么名画面估计也没有什么人注意到，金高银在剧中用的是亮橙色的，图片上为同款小企鹅款，小企鹅跟浅棕色的奇妙搭配，出奇的很有质感，带了点可爱的同时也不失大气。就连卡套的挂绳也很好看，上身韩范十足。<\/span><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><a href=\"https://item.taobao.com/item.htm?spm=a1z0k.6846577.0.0.brIVzR&id=37103533689&_u=t2dmg8j26111\" target=\"_self\"><img src=\"http://img.domolife.cn/platfrom/20170222/1487756609573031154.jpg\" title=\"1487756609573031154.jpg\" alt=\"8.jpg\"/><\/a><\/p>"},{"type":"text","content":"<p><br/><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\">这款卡套是Sanrio家hellokitty QUESTINA系列的产品，造型非常特别，一个小小的双肩包造型，有前袋跟后袋，前面的口袋空间较小，适合放置硬币的零钱。书包的大口袋可以放置纸币跟卡片，容量会比一般卡套要大，金色的链子箍在手上也很好看，出门回头率也是杠杆的。说到底，QUESTINA系列的产品除了贵还真没有别的缺点了~<\/span><\/p>"},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"goods","content":{"picUrl":"http://image.domolife.cn/platform/20170718/20170718175041425.jpg","marketPrice":"49.00","price":"35.00","name":"可爱河马北极熊苹果手机壳软壳 带支架","newPicUrl":"http://img.domolife.cn/platform/C6ZdcZp8YH1499400239765.jpg","maxPrice":"35.00","id":5964,"itemTypeId":1}},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;; font-size: 14px;\"><br/><\/span><\/p>"},{"type":"coupon","content":{"amount":"1.00","count":28,"title":"11月邀请好友规则","totalCount":30,"mode":1,"picUrl":"http://img.domolife.cn/platform/nPRwxWTrKk1499238561156.jpg","newPirUrl":"http://img.domolife.cn/platform/nPRwxWTrKk1499238561156.jpg","useRange":0,"startFee":"3.00","startTime":"2016-11-01 10:51","id":2,"endTime":"2022-11-30 00:00:00","desc":""}},{"type":"text","content":"<p style=\"line-height: 1.5em;\"><br/><\/p>"},{"type":"goods","content":{"picUrl":"http://image.domolife.cn/platform/20170309/20170309110645522.jpg","marketPrice":"49.00","price":"0.10","name":" 火箭宇宙iPhone6手机壳 带支架","newPicUrl":"http://img.domolife.cn/platform/632zwda3sf1499408024196.jpg","maxPrice":"0.10","id":5966,"itemTypeId":1}},{"type":"text","content":"<p><br/><\/p>"}]
         * avatar : http://img.domolife.cn/Uploads/app_img/2016-03-31/56fcc8cd4a039.png
         * title : 谁说主妇不精明 收纳整理样样行
         * productCount : 2
         * picture : []
         * tags : []
         * uid : 1
         * path : http://image.domolife.cn/platform/Rpt36SaW531500362574007.jpg
         * view : 1880
         * favor : 12
         * nickname : domolife
         * ctime : 2017-02-22 17:50:03
         * comment : 0
         * id : 9832
         * collect : 20
         * atList : []
         * productList : [{"picUrl":"http://image.domolife.cn/platform/20170718/20170718175041425.jpg","marketPrice":"49.00","price":"35.00","name":"可爱河马北极熊苹果手机壳软壳 带支架","newPicUrl":"http://img.domolife.cn/platform/C6ZdcZp8YH1499400239765.jpg","maxPrice":"35.00","id":5964,"itemTypeId":1},{"picUrl":"http://image.domolife.cn/platform/20170309/20170309110645522.jpg","marketPrice":"49.00","price":"0.10","name":" 火箭宇宙iPhone6手机壳 带支架","newPicUrl":"http://img.domolife.cn/platform/632zwda3sf1499408024196.jpg","maxPrice":"0.10","id":5966,"itemTypeId":1}]
         * status : 1
         */

        private int isfront;
        private int articletype;//文章类型
        private boolean flag;
        private boolean isFavor; //点赞数量
        private boolean isCollect;//是否收藏
        private String avatar;
        private String title; //文章标题
        private int productCount;//文章中的推荐商品数量
        private int uid; //文章发布者的uid
        private String path;//文章顶部图
        private int view;
        private int favor; //点赞数量
        private String nickname;
        private String ctime;//发布时间
        private int comment;
        private int id;//文章id
        private int collect;//文章被收藏的次数
        private int status;
        private String category_title;//文章类型
        private String digest;
        private int category_id;
        private List<CommunalDetailBean> description;
        private List<ProductListBean> productList;  //文章中的推荐商品
        private List<LikedProductBean> documentProductList;  //同类推荐商品
        @SerializedName("json")
        private List<RelevanceProBean> relevanceProList;
        private List<TagsBean> tags;

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public List<RelevanceProBean> getRelevanceProList() {
            return relevanceProList;
        }

        public void setRelevanceProList(List<RelevanceProBean> relevanceProList) {
            this.relevanceProList = relevanceProList;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getCategory_title() {
            return category_title;
        }

        public void setCategory_title(String category_title) {
            this.category_title = category_title;
        }

        public List<LikedProductBean> getDocumentProductList() {
            return documentProductList;
        }

        public void setDocumentProductList(List<LikedProductBean> documentProductList) {
            this.documentProductList = documentProductList;
        }

        public int getIsfront() {
            return isfront;
        }

        public void setIsfront(int isfront) {
            this.isfront = isfront;
        }

        public int getArticletype() {
            return articletype;
        }

        public void setArticletype(int articletype) {
            this.articletype = articletype;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public boolean isIsFavor() {
            return isFavor;
        }

        public void setIsFavor(boolean isFavor) {
            this.isFavor = isFavor;
        }

        public boolean isIsCollect() {
            return isCollect;
        }

        public void setIsCollect(boolean isCollect) {
            this.isCollect = isCollect;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getView() {
            return view;
        }

        public void setView(int view) {
            this.view = view;
        }

        public int getFavor() {
            return favor;
        }

        public void setFavor(int favor) {
            this.favor = favor;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public int getComment() {
            return comment;
        }

        public void setComment(int comment) {
            this.comment = comment;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<CommunalDetailBean> getDescription() {
            return description;
        }

        public void setDescription(List<CommunalDetailBean> description) {
            this.description = description;
        }

        public List<ProductListBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductListBean> productList) {
            this.productList = productList;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class ProductListBean {
            /**
             * picUrl : http://image.domolife.cn/platform/20170718/20170718175041425.jpg
             * marketPrice : 49.00
             * price : 35.00
             * name : 可爱河马北极熊苹果手机壳软壳 带支架
             * newPicUrl : http://img.domolife.cn/platform/C6ZdcZp8YH1499400239765.jpg
             * maxPrice : 35.00
             * id : 5964
             * itemTypeId : 1
             */
            @SerializedName(value = "picUrl",alternate = "path")
            private String picUrl;
            private String marketPrice;
            private String price;
            private String name;
            private String newPicUrl;
            private String maxPrice;
            private int id;
            private int itemTypeId;
            private int quality;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNewPicUrl() {
                return newPicUrl;
            }

            public void setNewPicUrl(String newPicUrl) {
                this.newPicUrl = newPicUrl;
            }

            public String getMaxPrice() {
                return maxPrice;
            }

            public void setMaxPrice(String maxPrice) {
                this.maxPrice = maxPrice;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getItemTypeId() {
                return itemTypeId;
            }

            public void setItemTypeId(int itemTypeId) {
                this.itemTypeId = itemTypeId;
            }
        }

        public DmlSearchDetailBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.isfront);
            dest.writeInt(this.articletype);
            dest.writeByte(this.flag ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFavor ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isCollect ? (byte) 1 : (byte) 0);
            dest.writeString(this.avatar);
            dest.writeString(this.title);
            dest.writeInt(this.productCount);
            dest.writeInt(this.uid);
            dest.writeString(this.path);
            dest.writeInt(this.view);
            dest.writeInt(this.favor);
            dest.writeString(this.nickname);
            dest.writeString(this.ctime);
            dest.writeInt(this.comment);
            dest.writeInt(this.id);
            dest.writeInt(this.collect);
            dest.writeInt(this.status);
            dest.writeString(this.category_title);
            dest.writeString(this.digest);
            dest.writeInt(this.category_id);
            dest.writeList(this.description);
            dest.writeList(this.productList);
        }

        protected DmlSearchDetailBean(Parcel in) {
            this.isfront = in.readInt();
            this.articletype = in.readInt();
            this.flag = in.readByte() != 0;
            this.isFavor = in.readByte() != 0;
            this.isCollect = in.readByte() != 0;
            this.avatar = in.readString();
            this.title = in.readString();
            this.productCount = in.readInt();
            this.uid = in.readInt();
            this.path = in.readString();
            this.view = in.readInt();
            this.favor = in.readInt();
            this.nickname = in.readString();
            this.ctime = in.readString();
            this.comment = in.readInt();
            this.id = in.readInt();
            this.collect = in.readInt();
            this.status = in.readInt();
            this.category_title = in.readString();
            this.digest = in.readString();
            this.category_id = in.readInt();
            this.description = new ArrayList<CommunalDetailBean>();
            in.readList(this.description, CommunalDetailBean.class.getClassLoader());
            this.productList = new ArrayList<ProductListBean>();
            in.readList(this.productList, ProductListBean.class.getClassLoader());
        }

        public static final Creator<DmlSearchDetailBean> CREATOR = new Creator<DmlSearchDetailBean>() {
            @Override
            public DmlSearchDetailBean createFromParcel(Parcel source) {
                return new DmlSearchDetailBean(source);
            }

            @Override
            public DmlSearchDetailBean[] newArray(int size) {
                return new DmlSearchDetailBean[size];
            }
        };
    }
}
