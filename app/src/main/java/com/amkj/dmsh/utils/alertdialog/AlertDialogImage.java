package com.amkj.dmsh.utils.alertdialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amkj.dmsh.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/16
 * version 3.1.5
 * class description:弹窗广告
 */
public class AlertDialogImage {

    private Context context;
    private ImageView iv_ad_image;
    private AlertImageClickListener alertImageClickListener;
    private AlertDialog imageAlertDialog;
    private View dialogView;
    private boolean isFirstSet;

    public AlertDialogImage(Context context) {
        if(!isContextExisted(context)){
            return;
        }
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog_image, null, false);
        iv_ad_image = dialogView.findViewById(R.id.iv_ad_image);
        builder.setCancelable(true);
        imageAlertDialog = builder.create();
        iv_ad_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertImageClickListener != null) {
                    alertImageClickListener.imageClick();
                }
            }
        });
        isFirstSet = true;
    }

    public void setImage(@NonNull Bitmap bitmap) {
        try {
            iv_ad_image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAlertClickListener(AlertImageClickListener alertImageClickListener) {
        this.alertImageClickListener = alertImageClickListener;
    }

    public interface AlertImageClickListener {
        void imageClick();
    }

    /**
     * 展示dialog
     */
    public void show() {
        if(imageAlertDialog==null){
            return;
        }
        if (!imageAlertDialog.isShowing()&& isContextExisted(context)) {
            imageAlertDialog.show();
        }
        if (isFirstSet) {
            Window window = imageAlertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = AutoSizeUtils.mm2px(mAppContext,500);
                window.setAttributes(params);
                window.setContentView(dialogView);
            }
        }
        isFirstSet = false;
    }

    public void dismiss() {
        if (imageAlertDialog != null && isContextExisted(context)) {
            imageAlertDialog.dismiss();
        }
    }
}
