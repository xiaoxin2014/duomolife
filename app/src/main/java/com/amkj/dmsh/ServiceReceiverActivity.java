package com.amkj.dmsh;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.coreapi.ItemParamsBody;
import cn.xiaoneng.utils.CoreData;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipXNService;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/11
 * class description:通知消息跳转
 */

public class ServiceReceiverActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLoginStatus();
    }

    private void isLoginStatus() {
        ChatParamsBody chatParamsBody = new ChatParamsBody();
        chatParamsBody.startPageTitle = getStrings("消息-状态栏");
        chatParamsBody.startPageUrl = ConstantVariable.DEFAULT_SERVICE_PAGE_URL;
        ItemParamsBody itemParams = chatParamsBody.itemparams;
        itemParams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;
        itemParams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        itemParams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if(personalInfo.isLogin()){
            chatParamsBody.headurl = personalInfo.getAvatar();
        }
        skipXNService(ServiceReceiverActivity.this,chatParamsBody);
        finish();
        overridePendingTransition(0, 0);
    }
}
