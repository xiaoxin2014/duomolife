package com.amkj.dmsh.wxapi;


import android.text.TextUtils;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM;


public class WXEntryActivity extends WXCallbackActivity {
    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        if (resp.getType() == COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            if (!TextUtils.isEmpty(extraData)) {
                setSkipPath(this, extraData, false);
            }
        }
    }
}
