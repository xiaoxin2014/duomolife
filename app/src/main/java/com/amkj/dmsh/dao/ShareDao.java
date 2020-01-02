package com.amkj.dmsh.dao;

import android.app.Activity;
import android.text.TextUtils;

import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * Created by xiaoxin on 2019/12/28
 * Version:v4.4.1
 * ClassDescription :分享相关Dao类
 */
public class ShareDao {
    //邀请参团
    public static void invitePartnerGroup(Activity activity, QualityGroupShareBean qualityGroupShareBean, String orderNo) {
        if (qualityGroupShareBean != null && isContextExisted(activity)) {
            new UMShareAction((BaseActivity) activity
                    , qualityGroupShareBean.getGpPicUrl()
                    , qualityGroupShareBean.getName()
                    , getStrings(qualityGroupShareBean.getSubtitle())
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                    + "&record=" + qualityGroupShareBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupShareBean.getGpInfoId()
                    + (TextUtils.isEmpty(orderNo) ? "&gpRecordId=" + qualityGroupShareBean.getGpRecordId() : "&order=" + orderNo), qualityGroupShareBean.getGpInfoId(), -1, "1");
        }
    }
}
