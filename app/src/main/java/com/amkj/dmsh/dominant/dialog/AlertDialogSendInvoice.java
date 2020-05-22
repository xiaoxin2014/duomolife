package com.amkj.dmsh.dominant.dialog;

import android.content.Context;
import android.view.WindowManager;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.alertdialog.BaseAlertDialogHelper;

/**
 * Created by xiaoxin on 2019/12/30
 * Version:v4.4.2
 * ClassDescription :发送发票到邮箱弹窗
 */
public class AlertDialogSendInvoice extends BaseAlertDialogHelper {

    public AlertDialogSendInvoice(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pw_send_invoice;
    }

    @Override
    public void show() {
        super.show();
        if (getAlertDialog(). getWindow()!=null){
            getAlertDialog(). getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }
}
