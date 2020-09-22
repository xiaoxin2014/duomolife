package com.amkj.dmsh.mine.bean;

import com.amkj.dmsh.base.BaseEntity;
import com.amkj.dmsh.find.bean.PostEntity;

import java.util.List;

/**
 * Created by xiaoxin on 2020/9/1
 * Version:v4.7.0
 */
public class CollectReportEntity extends BaseEntity {

    /**
     * sysTime : 2020-09-01 15:24:30
     * showCount : 40
     * totalPage : 0
     * totalResult : 0
     * currentPage : 1
     * result : [{"productId":"","activityId":"","orderId":"14","content":"222","imgs":"http://image.domolife.cn/202008271627003415595247.jpg,http://image.domolife.cn/202008271627001819965912.jpg,http://image.domolife.cn/202008271627214355022823.jpg","star":"5","likeCount":"1","isLike":"0","isCollect":"1","avatar":"http://image.domolife.cn/platform/hjXPrGpMaJ1534473782022.jpg","nickName":"疯狂的小新z","createTime":"2020-08-27 16:27:00"},{"productId":"","activityId":"","orderId":"25","content":"这是一条提交测试啊\nad丰盛的风格大师法规的双方各三大法宝第三方百度闪付部分干部根深蒂固是的非官方的时光我告诉对方哥哥是大法官随时告诉对方告诉对方不是发货人挺好是根深蒂固帝国时代根深蒂固OK节能环保老师带你管理费VB手动阀价格jg我跟你说的氟苯尼考立即红是不是看见的功能十分靠近虹山水库加分那个风格和松还是低功耗看见SV红烧豆腐价格hi","imgs":"http://image.domolife.cn/platform/20200831/20200831111605079.jpg,http://image.domolife.cn/platform/20200831/20200831111608801.gif,http://image.domolife.cn/platform/20200831/20200831111611845.jpg,http://image.domolife.cn/platform/20200831/20200831111616306.gif,http://image.domolife.cn/platform/20200831/20200831111620091.jpg,http://image.domolife.cn/platform/20200831/20200831111623064.jpg,http://image.domolife.cn/platform/20200831/20200831111627786.jpg,http://image.domolife.cn/platform/20200831/20200831111632689.gif","star":"3","likeCount":"1","isLike":"0","isCollect":"1","avatar":"https://image.domolife.cn/test/20200807152848b3282.jpg","nickName":"1767949****","createTime":"2020-08-31 11:16:34"},{"productId":"","activityId":"","orderId":"23","content":"sdmfsfgxdmsgzksnszgdmkrkkdsjdgsekjseghjsdgkdfhkfjydrkyjrd","imgs":"https://image.domolife.cn/wx470057dda4b9181d.o6zAJsys41ZaqPIjfAhm_J2_scSI.PWAz3jaBBAsCeb1f54846f31a1cbcd79c77d1a3423c2.png,https://image.domolife.cn/wx470057dda4b9181d.o6zAJsys41ZaqPIjfAhm_J2_scSI.ftbFPDYLXmgi524522372210ca0918f087dc5adfb79f.png","star":"4","likeCount":"1","isLike":"0","isCollect":"1","avatar":"https://thirdwx.qlogo.cn/mmopen/vi_32/SCA2YoAfIR3Q3c3icl0S8m7dyFoRSVftvtzhx9DEMzDhK0ibmrB8okEpcpLjCswWhADxXL9N09Fmal947ftjyR7g/132","nickName":"213","createTime":"2020-08-28 17:07:32"}]
     */

    private List<PostEntity.PostBean> result;


    public List<PostEntity.PostBean> getResult() {
        return result;
    }

    public void setResult(List<PostEntity.PostBean> result) {
        this.result = result;
    }
}
