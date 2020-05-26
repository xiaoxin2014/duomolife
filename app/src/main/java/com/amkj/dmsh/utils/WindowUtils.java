package com.amkj.dmsh.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;

import java.util.Objects;

import me.jessyan.autosize.AutoSize;


public class WindowUtils {

    // 获取popwindow
    public static PopupWindow getFullPw(final Activity activity, int layoutId, int gravity) {
        // 创建PopupWindow，参数4 false为不获取焦点
        View pwView = LayoutInflater.from(activity).inflate(layoutId, null, false);
        PopupWindow popupWindow = new PopupWindow(pwView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                true);
        popupWindow.setTouchable(true);
        // 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置弹出动画
        popupWindow.setAnimationStyle(getAnimaitionStyle(gravity));
        //  弹出窗监听
        popupWindow.setOnDismissListener(() -> setWindowAlpha(activity, 1));
        return popupWindow;
    }


    // 获取popwindow
    public static PopupWindow getAlphaPw(final Activity activity, int layoutId, int gravity) {
        // 创建PopupWindow，参数4 false为不获取焦点
        View pwView = LayoutInflater.from(activity).inflate(layoutId, null, false);
        PopupWindow popupWindow = new PopupWindow(pwView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        // 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置弹出动画
        popupWindow.setAnimationStyle(getAnimaitionStyle(gravity));
        //  弹出窗监听
        popupWindow.setOnDismissListener(() -> setWindowAlpha(activity, 1));
        return popupWindow;
    }

    // 获取popwindow
    public static PopupWindow getAlphaPw(final Activity activity, View view, int gravity) {
        PopupWindow popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        // 设置背景颜色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置弹出动画
        popupWindow.setAnimationStyle(getAnimaitionStyle(gravity));
        //  弹出窗监听
        popupWindow.setOnDismissListener(() -> setWindowAlpha(activity, 1));
        return popupWindow;
    }

    //获取popwindow(按返回无法取消)
    public static PopupWindow getUncanclePw(final Activity activity, int layoutId, int gravity) {
        PopupWindow alphaPw = getAlphaPw(activity, layoutId, gravity);
        alphaPw.setFocusable(false);
        alphaPw.setOutsideTouchable(true);
        alphaPw.getContentView().setFocusable(true);
        alphaPw.getContentView().setOnKeyListener((view, i, keyEvent) -> i == KeyEvent.KEYCODE_BACK);
        return alphaPw;
    }

    //获取dialog
    public static Dialog getDialog(final Activity activity, int layoutId) {
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        //保持dialog不关闭的方法
        try {
            java.lang.reflect.Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //点击对话框外面,对话框不消失
        dialog.setCanceledOnTouchOutside(false);
        // 返回键不可关闭
        dialog.setOnKeyListener((dialog1, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0);
        // 关联布局文件
        View view = View.inflate(activity, layoutId, null);
        // 设置弹出动画
        Objects.requireNonNull(dialog.getWindow()).setWindowAnimations(R.style.pw_slide_center);
        dialog.setView(view);
        return dialog;
    }


    //选择弹窗动画(坐标原点为左上角)
    public static int getAnimaitionStyle(int gravity) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return R.style.pw_slide_up;
            case Gravity.CENTER:
                return R.style.pw_slide_center;
            case Gravity.TOP:
                return R.style.pw_slide_down;
            default:
                return R.style.pw_slide_abc;
        }
    }

    //关闭弹窗
    public static void closePw(PopupWindow... pw) {
        for (PopupWindow aPw : pw) {
            if (aPw != null && aPw.isShowing()) {
                aPw.dismiss();
            }
        }
    }

    //显示弹窗
    public static void showPw(Activity activity, PopupWindow pw, int gravity) {
        if (ConstantMethod.isContextExisted(activity)) {
            AutoSize.autoConvertDensityOfGlobal(activity);
            WindowUtils.setWindowAlpha(activity, 0.6f);
            pw.showAtLocation(activity.getWindow().getDecorView(), gravity, 0, 0);
        }
    }


    //显示在某个控件下方
    public static void showPw(Activity activity, PopupWindow pw, View view, int x, int y) {
        if (ConstantMethod.isContextExisted(activity)) {
            AutoSize.autoConvertDensityOfGlobal(activity);
            WindowUtils.setWindowAlpha(activity, 0.6f);
            pw.showAsDropDown(view, x, y);
        }
    }


    //延时显示弹窗
    public static void showPwDelay(Activity activity, PopupWindow pw, int gravity) {
        new Handler().postDelayed(() -> showPw(activity, pw, gravity), 200);
    }


    //设置窗体透明度
    private static void setWindowAlpha(Activity activity, float alpha) {
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = alpha;
        activity.getWindow().setAttributes(params);
    }
}
