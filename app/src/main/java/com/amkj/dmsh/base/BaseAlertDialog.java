package com.amkj.dmsh.base;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.amkj.dmsh.R;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import butterknife.ButterKnife;
import me.jessyan.autosize.AutoSize;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.NO_GRAVITY;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * Created by xiaoxin on 2020/5/20
 * Version:v4.5.2
 * ClassDescription :AlertDialog基类
 */
public abstract class BaseAlertDialog implements LifecycleObserver {
    protected Context context;
    private AlertDialog defaultAlertDialog;
    protected View dialogView;


    public BaseAlertDialog(Context context) {
        this(context, 0);
    }

    protected BaseAlertDialog(Context context, int layoutId) {
        this.context = context;
        //这里生命周期拥有者是被观察者，实现LifecycleObserver的类是观察者，当LifecycleOwner生命周期发生变化时，就会通知观察者，进行回调
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context,getThemeResId());
        dialogView = LayoutInflater.from(context).inflate(layoutId == 0 ? getLayoutId() : layoutId, null, false);
        ButterKnife.bind(this, dialogView);
        builder.setCancelable(true);
        defaultAlertDialog = builder.create();
    }

    protected abstract int getLayoutId();

    protected int getLayoutWith() {
        return MATCH_PARENT;
    }

    protected int getThemeResId() {
        return R.style.service_dialog_theme;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().removeObserver(this);
            if (isShowing()) dismiss();
        }
    }

    public AlertDialog getAlertDialog() {
        return defaultAlertDialog;
    }

    /**
     * 展示dialog
     */
    public void show() {
        show(NO_GRAVITY);
    }


    /**
     * @param gravity 显示的位置
     */
    public void show(int gravity) {
        if (!defaultAlertDialog.isShowing() && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal((Activity) context);
            defaultAlertDialog.show();
        }
        Window window = defaultAlertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            // 获取自定义的Dialog所在的窗体的属性参数
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            // 设置窗体沉在底部
            layoutParams.gravity = (gravity != NO_GRAVITY ? gravity : CENTER);
            // 设置窗体宽度全屏
            layoutParams.width = getLayoutWith();
            // 获取窗体顶层View对象，并设置内边距为0（保证顶层视图宽度全屏）
            window.getDecorView().setPadding(0, 0, 0, 0);
            // 提交窗体属性参数
            window.setAttributes(layoutParams);
            window.setContentView(dialogView);
        }
    }


    /**
     * 关闭dialog
     */
    public void dismiss() {
        if (isShowing()) {
            defaultAlertDialog.dismiss();
        }
    }

    //dialog 是否在展示
    public boolean isShowing() {
        return defaultAlertDialog != null
                && isContextExisted(context) && defaultAlertDialog.isShowing();
    }

    public View getDialogView() {
        return dialogView;
    }
}
