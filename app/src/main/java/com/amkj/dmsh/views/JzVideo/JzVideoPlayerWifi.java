package com.amkj.dmsh.views.JzVideo;

import android.content.Context;
import android.util.AttributeSet;

import com.amkj.dmsh.R;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;

import cn.jzvd.JzvdStd;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/22
 * version 3.1.3
 * class description:请输入类描述
 */
public class JzVideoPlayerWifi extends JzvdStd {
    private AlertDialogHelper alertDialogHelper;

    public JzVideoPlayerWifi(Context context) {
        super(context);
    }

    public JzVideoPlayerWifi(Context context, AttributeSet attrs) {
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
                    WIFI_TIP_DIALOG_SHOWED = true;
                    if (state == STATE_PAUSE) {
                        startButton.performClick();
                    } else {
                        startVideo();
                    }
                }

                @Override
                public void cancel() {
                    releaseAllVideos();
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
