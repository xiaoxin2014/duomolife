package com.amkj.dmsh.utils.alertdialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * Created by xiaoxin on 2020/5/21
 * Version:v4.5.2
 * ClassDescription :AlertDialog默认样式
 */
public class AlertDialogHelper extends BaseAlertDialogHelper {

    @BindView(R.id.tv_alert_title)
    TextView tv_alert_title;
    @BindView(R.id.tv_alert_message)
    TextView tv_alert_message;
    @BindView(R.id.tv_alert_confirm)
    TextView tv_alert_confirm;
    @BindView(R.id.tv_alert_cancel)
    TextView tv_alert_cancel;
    private AlertConfirmCancelListener alertConfirmCancelListener;

    public AlertDialogHelper(Context context) {
        super(context);
    }

    public AlertDialogHelper(Context context, int layoutId) {
        super(context, layoutId);
    }


    @OnClick({R.id.tv_alert_confirm, R.id.tv_alert_cancel})
    public void onViewClicked(View view) {
        dismiss();
        if (alertConfirmCancelListener != null) {
            switch (view.getId()) {
                case R.id.tv_alert_confirm:
                    alertConfirmCancelListener.confirm();
                    break;
                case R.id.tv_alert_cancel:
                    alertConfirmCancelListener.cancel();
                    break;
            }
        }
    }


    public void setAlertListener(AlertConfirmCancelListener alertConfirmCancelListener) {
        this.alertConfirmCancelListener = alertConfirmCancelListener;
    }

    public interface AlertConfirmCancelListener {
        void confirm();

        void cancel();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_alert_dialog_video_network;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 500);
    }


    public View getDialogView() {
        return dialogView;
    }

    //设置标题
    public AlertDialogHelper setTitle(String title) {
        tv_alert_title.setText(getStrings(title));
        return this;
    }

    //设置标题是否展示
    public AlertDialogHelper setTitleVisibility(int visibility) {
        tv_alert_title.setVisibility(visibility);
        return this;
    }

    //设置标题位置显示
    public AlertDialogHelper setTitleGravity(int gravity) {
        tv_alert_title.setGravity(gravity);
        return this;
    }

    //设置描述信息
    public AlertDialogHelper setMsg(String msg) {
        tv_alert_message.setText(getStrings(msg));
        return this;
    }

    //设置描述信息
    public AlertDialogHelper setMsg(int msgResId) {
        tv_alert_message.setText(getStrings(context.getResources().getString(msgResId)));
        return this;
    }

    //设置meg文本位置显示
    public AlertDialogHelper setMsgTextGravity(int msgTextGravity) {
        tv_alert_message.setGravity(msgTextGravity);
        return this;
    }

    //获取信息文本
    public TextView getMsgTextView() {
        return tv_alert_message;
    }

    //设置取消字样
    public AlertDialogHelper setCancelText(String cancelText) {
        tv_alert_cancel.setText(getStringFilter(cancelText));
        return this;
    }

    //设置取消字样颜色
    public AlertDialogHelper setCancelTextColor(int cancelTextColor) {
        tv_alert_cancel.setTextColor(cancelTextColor);
        return this;
    }

    //设置确认字样
    public AlertDialogHelper setConfirmText(String confirmText) {
        tv_alert_confirm.setText(getStringFilter(confirmText));
        return this;
    }

    //设置确认字样颜色
    public AlertDialogHelper setConfirmTextColor(int confirmTextColor) {
        tv_alert_confirm.setTextColor(confirmTextColor);
        return this;
    }

    //设置单个按钮显示，隐藏cancel按钮
    public AlertDialogHelper setSingleButton(boolean singleButton) {
        tv_alert_cancel.setVisibility(singleButton ? View.GONE : View.VISIBLE);
        return this;
    }

    //是否可以取消
    public AlertDialogHelper setCancelable(boolean cancelable) {
        getAlertDialog().setCancelable(cancelable);
        return this;
    }
}
