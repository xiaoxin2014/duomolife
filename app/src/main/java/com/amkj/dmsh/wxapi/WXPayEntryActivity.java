package com.amkj.dmsh.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import static com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_PAY_BY_WX;

;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_call_back);
        if (WXPay.getInstance() != null) {
            WXPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (WXPay.getInstance() != null) {
            WXPay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseResp) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == COMMAND_PAY_BY_WX) {
            if (WXPay.getInstance() != null) {
                WXPay.getInstance().onResp(baseResp.errCode);
                finish();
            }
        }
    }
}
