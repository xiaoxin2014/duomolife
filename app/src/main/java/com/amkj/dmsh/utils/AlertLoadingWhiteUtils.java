package com.amkj.dmsh.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.amkj.dmsh.R;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/26
 * class description:等待框
 */

public class AlertLoadingWhiteUtils implements View.OnClickListener {

    private final Dialog loadDialog;
    private final View loadView;
    private final View errorView;
    private onReLoadListener onReLoadListener;

    public AlertLoadingWhiteUtils(Context context) {
        loadDialog = new Dialog(context, R.style.White_Dialog);
        loadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadDialog.setContentView(R.layout.communal_loading_empty_error);
        loadDialog.show();
// 设置对话框大小
        WindowManager.LayoutParams layoutParams = loadDialog.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        loadDialog.getWindow().setAttributes(layoutParams);
        loadView = loadDialog.findViewById(R.id.communal_load);
        errorView = loadDialog.findViewById(R.id.communal_error);
        loadView.setVisibility(View.VISIBLE);
    }

    //    展示
    public void show() {
        if (loadDialog != null) {
            loadDialog.show();
            loadView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
        }
    }

    //    取消
    public void cancel() {
        if (loadDialog != null) {
            loadDialog.cancel();
            loadView.setVisibility(View.GONE);
        }
    }

    public void error() {
        if (loadDialog != null) {
            loadDialog.show();
            loadDialog.findViewById(R.id.rel_communal_error).setOnClickListener(this);
            loadView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (onReLoadListener != null) {
            onReLoadListener.load();
        }
    }

    public void setOnReLoadListener(onReLoadListener onReLoadListener) {
        this.onReLoadListener = onReLoadListener;
    }

    public interface onReLoadListener {
        void load();
    }
}
