package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import android.util.AttributeSet;

import com.amkj.dmsh.R;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;

import androidx.appcompat.app.AlertDialog;
import cn.jzvd.JZUserActionStd;
import cn.jzvd.JzvdStd;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/22
 * version 3.1.3
 * class description:请输入类描述
 */
public class JzVideoPlayerStatusDialog extends JzvdStd {
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
    public int getLayoutId() {
        return R.layout.jz_layout_std_my;
    }

    @Override
    public void showWifiDialog() {
        if (alertDialogHelper == null) {
            alertDialogHelper = new AlertDialogHelper(getContext());
            alertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                @Override
                public void confirm() {
                    onEvent(JZUserActionStd.ON_CLICK_START_WIFIDIALOG);
                    startVideo();
                    WIFI_TIP_DIALOG_SHOWED = true;
                }

                @Override
                public void cancel() {
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
