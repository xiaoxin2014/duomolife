package com.amkj.dmsh.dominant.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.amkj.dmsh.R;

import me.jessyan.autosize.AutoSize;

import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * Created by xiaoxin on 2019/12/30
 * Version:v4.4.2
 * ClassDescription :发送发票到邮箱弹窗
 */
public class AlertDialogSendInvoice {

    private Activity context;
    private AlertDialog imageAlertDialog;
    private View dialogView;
    private boolean isFirstSet;

    public AlertDialogSendInvoice(Activity context) {
        if (!isContextExisted(context)) {
            return;
        }
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.pw_send_invoice, null, false);
        builder.setCancelable(true);
        imageAlertDialog = builder.create();
        isFirstSet = true;
    }

    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    /**
     * 展示dialog
     */
    public void show() {
        if (imageAlertDialog == null) {
            return;
        }
        if (!imageAlertDialog.isShowing() && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal(context);
            imageAlertDialog.show();
        }
        if (isFirstSet) {
            Window window = imageAlertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                window.setContentView(dialogView);
                imageAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            }
        }
        isFirstSet = false;
    }

    public void dismiss() {
        if (imageAlertDialog != null && isContextExisted(context)) {
            imageAlertDialog.dismiss();
        }
    }

    /**
     * dialog 是否在展示
     */
    public boolean isShowing() {
        return imageAlertDialog != null
                && isContextExisted(context) && imageAlertDialog.isShowing();
    }


}
