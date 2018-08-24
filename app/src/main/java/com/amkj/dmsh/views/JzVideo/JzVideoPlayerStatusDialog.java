package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;

import cn.jzvd.JZUserActionStandard;
import cn.jzvd.JZVideoPlayerStandard;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/22
 * version 3.1.3
 * class description:请输入类描述
 */
public class JzVideoPlayerStatusDialog extends JZVideoPlayerStandard {
    public AlertDialog wifiAlertView;
    private AlertDialogHelper alertDialogHelper;

    public JzVideoPlayerStatusDialog(Context context) {
        super(context);
    }

    public JzVideoPlayerStatusDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public void showWifiDialog() {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(getContext());
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    alertDialogHelper.dismiss();
                    onEvent(JZUserActionStandard.ON_CLICK_START_WIFIDIALOG);
                    startVideo();
                    WIFI_TIP_DIALOG_SHOWED = true;
                }

                @Override
                public void cancel() {
                    alertDialogHelper.dismiss();
                    clearFloatScreen();
                }
            });
            alertDialogHelper.setTitle("权限提示")
                    .setMsg(getResources().getString(R.string.tips_not_wifi))
                    .setConfirmText(getResources().getString(R.string.tips_not_wifi_confirm))
                    .setCancelText(getResources().getString(R.string.tips_not_wifi_cancel));
        }
        alertDialogHelper.show();
    }
}
