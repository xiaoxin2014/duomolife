package com.amkj.dmsh.utils.alertdialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.amkj.dmsh.R;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/6/6
 * version 3.1.3
 * class description:默认样式dialog
 */
public class AlertDialogHelper {
    private Context context;
    private String msg;
    private boolean isFirstSet = true;
    private AlertConfirmCancelListener alertConfirmCancelListener;
    private AlertDialog defaultAlertDialog;
    private TextView tv_alert_title;
    private TextView tv_alert_confirm;
    private TextView tv_alert_cancel;
    private TextView tv_alert_message;
    private String CANCEL = "cancel";
    private String CONFIRM = "confirm";
    private View dialogView;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            String clickType = (String) v.getTag();
            if (alertConfirmCancelListener != null) {
                if (CONFIRM.equals(clickType)) {
                    alertConfirmCancelListener.confirm();
                } else if (CANCEL.equals(clickType)) {
                    alertConfirmCancelListener.cancel();
                }
            }
        }
    };

    public AlertDialogHelper() {
    }


    public AlertDialogHelper(Context context) {
        this(context, 0);
    }

    public AlertDialogHelper(Context context, int layoutId) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.service_dialog_theme);
        dialogView = LayoutInflater.from(context).inflate(layoutId == 0 ? R.layout.layout_alert_dialog_video_network : layoutId, null, false);
        builder.setCancelable(true);
        tv_alert_title = dialogView.findViewById(R.id.tv_alert_title);
        tv_alert_message = dialogView.findViewById(R.id.tv_alert_message);
        tv_alert_confirm = dialogView.findViewById(R.id.tv_alert_confirm);
        tv_alert_cancel = dialogView.findViewById(R.id.tv_alert_cancel);

        tv_alert_confirm.setOnClickListener(onClickListener);
        tv_alert_confirm.setTag(CONFIRM);
        tv_alert_cancel.setOnClickListener(onClickListener);
        tv_alert_cancel.setTag(CANCEL);
        isFirstSet = true;
        defaultAlertDialog = builder.create();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Window window = defaultAlertDialog.getWindow();
            if (window != null) {
                window.getDecorView()
                        .getViewTreeObserver()
                        .addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                            @Override
                            public void onWindowAttached() {
                            }

                            @Override
                            public void onWindowDetached() {
                                onClickListener = null;
                            }
                        });
            }
        }
    }


    public AlertDialog getAlertDialog() {
        return defaultAlertDialog;
    }


    /**
     * @param cancelable
     * @return
     */
    public AlertDialogHelper setCancelable(boolean cancelable) {
        defaultAlertDialog.setCancelable(cancelable);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public AlertDialogHelper setTitle(String title) {
        tv_alert_title.setText(getStrings(title));
        return this;
    }

    /**
     * 设置标题是否展示
     *
     * @param visibility
     * @return
     */
    public AlertDialogHelper setTitleVisibility(int visibility) {
        tv_alert_title.setVisibility(visibility);
        return this;
    }

    /**
     * 设置标题位置显示
     *
     * @param gravity
     * @return
     */
    public AlertDialogHelper setTitleGravity(int gravity) {
        tv_alert_title.setGravity(gravity);
        return this;
    }

    /**
     * 设置描述信息
     *
     * @param msg
     * @return
     */
    public AlertDialogHelper setMsg(String msg) {
        this.msg = msg;
        tv_alert_message.setText(getStrings(msg));
        return this;
    }

    /**
     * 设置描述信息
     *
     * @param msgResId
     * @return
     */
    public AlertDialogHelper setMsg(int msgResId) {
        this.msg = context.getResources().getString(msgResId);
        tv_alert_message.setText(getStrings(msg));
        return this;
    }

    /**
     * 设置meg文本位置显示
     *
     * @param msgTextGravity
     * @return
     */
    public AlertDialogHelper setMsgTextGravity(int msgTextGravity) {
        tv_alert_message.setGravity(msgTextGravity);
        return this;
    }

    /**
     * 获取信息文本
     *
     * @return
     */
    public TextView getMsgTextView() {
        return tv_alert_message;
    }

    /**
     * 设置取消字样
     *
     * @param cancelText
     * @return
     */
    public AlertDialogHelper setCancelText(String cancelText) {
        tv_alert_cancel.setText(getStringFilter(cancelText));
        return this;
    }

    /**
     * 设置取消字样颜色
     *
     * @param cancelTextColor
     * @return
     */
    public AlertDialogHelper setCancelTextColor(int cancelTextColor) {
        tv_alert_cancel.setTextColor(cancelTextColor);
        return this;
    }

    /**
     * 设置确认字样
     *
     * @param confirmText
     * @return
     */
    public AlertDialogHelper setConfirmText(String confirmText) {
        tv_alert_confirm.setText(getStringFilter(confirmText));
        return this;
    }

    /**
     * 设置确认字样颜色
     *
     * @param confirmTextColor
     * @return
     */
    public AlertDialogHelper setConfirmTextColor(int confirmTextColor) {
        tv_alert_confirm.setTextColor(confirmTextColor);
        return this;
    }

    /**
     * 设置单个按钮显示
     * 隐藏cancel按钮
     *
     * @param singleButton
     * @return
     */
    public AlertDialogHelper setSingleButton(boolean singleButton) {
        tv_alert_cancel.setVisibility(singleButton ? View.GONE : View.VISIBLE);
        return this;
    }

    /**
     * 展示dialog
     */
    public void show() {
        if (!defaultAlertDialog.isShowing()
                && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal((Activity) context);
            defaultAlertDialog.show();
        }
        if (isFirstSet) {
            Window window = defaultAlertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = AutoSizeUtils.mm2px(mAppContext, 500);
                window.setAttributes(params);
                window.setContentView(dialogView);
            }
        }
        isFirstSet = false;
    }

    /**
     * 关掉dialog
     */
    public void dismiss() {
        if (isShowing()) {
            defaultAlertDialog.dismiss();
        }
    }

    /**
     * dialog 是否在展示
     *
     * @return
     */
    public boolean isShowing() {
        return defaultAlertDialog != null
                && isContextExisted(context) && defaultAlertDialog.isShowing();
    }

    public void setAlertListener(AlertConfirmCancelListener alertConfirmCancelListener) {
        this.alertConfirmCancelListener = alertConfirmCancelListener;
    }

    public interface AlertConfirmCancelListener {
        void confirm();

        void cancel();
    }


    public View getDialogView() {
        return dialogView;
    }
}
