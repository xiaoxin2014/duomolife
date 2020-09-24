package com.amkj.dmsh.dao;

import android.app.Activity;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;

import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.constant.ConstantVariable.GROUP_LOTTERY;

/**
 * Created by xiaoxin on 2019/12/28
 * Version:v4.4.0
 * ClassDescription :拼团相关Dao类
 */
public class GroupDao {

    //邀请参团
    public static void invitePartnerGroup(Activity activity, GroupShopDetailsBean qualityGroupShareBean) {
        invitePartnerGroup(activity, qualityGroupShareBean, "");
    }

    //邀请参团
    public static void invitePartnerGroup(Activity activity, GroupShopDetailsBean qualityGroupShareBean, String orderNo) {
        if (qualityGroupShareBean != null && isContextExisted(activity)) {
            boolean lotteryGroup = !TextUtils.isEmpty(orderNo) ? "3".equals(qualityGroupShareBean.getType()) : GROUP_LOTTERY.equals(qualityGroupShareBean.getType());
            int gpInfoId = qualityGroupShareBean.getGpInfoId();
            String gpRecordId = qualityGroupShareBean.getGpRecordId();
            String prefix = lotteryGroup ? "pages/LotteryGroup/lotteryGroup" : "pages/groupDetails/groupDetails";
            String suffix = TextUtils.isEmpty(orderNo) ? ((lotteryGroup ? "?gpInfoId=" : "?id=") + gpInfoId + "&gpRecordId=" + gpRecordId) : "?orderNo=" + orderNo;
            new UMShareAction((BaseActivity) activity
                    , qualityGroupShareBean.getCoverImage()
                    , !TextUtils.isEmpty(qualityGroupShareBean.getGpName()) ? qualityGroupShareBean.getGpName() : qualityGroupShareBean.getProductName()
                    , ""
                    , ""
                    , prefix + suffix
                    , qualityGroupShareBean.getGpInfoId(), -1, "1");
        }
    }
}
