package com.amkj.dmsh.utils.alertdialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/16
 * version 3.1.5
 * class description:弹窗广告
 */
public class AlertDialogImage {

    private ImageView iv_ad_image;
    private AlertImageClickListener alertImageClickListener;

    public AlertDialog createAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog_image, null, false);
        iv_ad_image = dialogView.findViewById(R.id.iv_ad_image);
        AutoUtils.auto(dialogView);
        builder.setCancelable(true);
        AlertDialog imageAlertDialog = builder.create();
        imageAlertDialog.show();
        Window window = imageAlertDialog.getWindow();
        if(window!=null){
            window.setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = AutoUtils.getPercentWidthSize(500);
            window.setAttributes(params);
            window.setContentView(dialogView);
        }
        iv_ad_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertImageClickListener!=null){
                    alertImageClickListener.imageClick();
                }
            }
        });
        return imageAlertDialog;
    }

    public void setImage(Bitmap bitmap){
        iv_ad_image.setImageBitmap(bitmap);
    }

    public void setAlertClickListener(AlertImageClickListener alertImageClickListener) {
        this.alertImageClickListener = alertImageClickListener;
    }

    public interface AlertImageClickListener {
        void imageClick();
    }
}
