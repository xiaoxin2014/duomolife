package com.amkj.dmsh.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.amkj.dmsh.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2016/12/7
 * class description:数据加载 等待窗
 */

public class LoadDataDialog {

    private AlertDialog waitingDialog;
    private LoadDialogView loadDialogView;
    private OnReLoadDataListener onReLoadDataListener;

    public void loadDing(Context context, View decorView) {
        waitingDialog = new AlertDialog.Builder(context, R.style.CustomDialog)
                .create();
        waitingDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_request_net_waitting_progress, (ViewGroup) decorView,false);
        waitingDialog.setContentView(view);
        Window win = waitingDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0,0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        loadDialogView = new LoadDialogView();
        ButterKnife.bind(loadDialogView, view);
        waitingDialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        if (waitingDialog != null) {
            loadDialogView.dialog_load_error.setVisibility(View.GONE);
            waitingDialog.show();
        }
    }

    public void dismiss() {
        if (waitingDialog != null) {
            loadDialogView.dialog_load_error.setVisibility(View.GONE);
            waitingDialog.dismiss();
        }
    }

    public void loadError() {
        if (waitingDialog != null) {
            waitingDialog.show();
            loadDialogView.dialog_load_error.setVisibility(View.VISIBLE);
        }
    }

    class LoadDialogView {
        @BindView(R.id.dialog_load_error)
        View dialog_load_error;

        @OnClick(R.id.dialog_load_error)
        public void reLoad(){
            if(onReLoadDataListener!=null){
                show();
                onReLoadDataListener.onReLoadData();
            }
        }
    }

    public interface OnReLoadDataListener {
        void onReLoadData();
    }

    public void setOnReLoadDataListener(OnReLoadDataListener onReLoadDataListener){
        this.onReLoadDataListener = onReLoadDataListener;
    }
}
