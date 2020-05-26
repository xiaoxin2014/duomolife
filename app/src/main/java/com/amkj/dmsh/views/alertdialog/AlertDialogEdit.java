package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiaoxin on 2019/12/30
 * Version:v4.4.2
 * ClassDescription :带编辑框的AlertDialog
 */
public class AlertDialogEdit extends BaseAlertDialog {

    @BindView(R.id.et_email)
    EditText mEtEmail;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_cover)
    LinearLayout mLlCover;
    @BindView(R.id.tv_alert_cancle)
    TextView mTvAlertCancle;
    private AlertConfirmListener alertConfirmCancelListener;

    public AlertDialogEdit(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pw_send_invoice;
    }

    @OnClick({R.id.tv_alert_confirm, R.id.tv_alert_cancle})
    public void onViewClicked(View view) {
        dismiss();
        if (alertConfirmCancelListener != null && view.getId() == R.id.tv_alert_confirm) {
            alertConfirmCancelListener.confirm(mEtEmail.getText().toString().trim());
        }
    }

    public void setOnAlertListener(AlertConfirmListener alertConfirmCancelListener) {
        this.alertConfirmCancelListener = alertConfirmCancelListener;
    }

    public interface AlertConfirmListener {
        void confirm(String text);
    }

    @Override
    public void show() {
        super.show();
        if (getAlertDialog().getWindow() != null) {
            getAlertDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    public AlertDialogEdit setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public AlertDialogEdit setTitleVisible(int visible) {
        mTvTitle.setVisibility(visible);
        return this;
    }

    public AlertDialogEdit setCoverVisible(int visible) {
        mLlCover.setVisibility(visible);
        return this;
    }


    public AlertDialogEdit setEditHint(String hint) {
        mEtEmail.setHint(hint);
        return this;
    }

    public AlertDialogEdit setEditText(String text) {
        mEtEmail.setText(text);
        mEtEmail.setSelection(text.length());
        return this;
    }

    public AlertDialogEdit setCancleVisible(int visible) {
        mTvAlertCancle.setVisibility(visible);
        return this;
    }
}
