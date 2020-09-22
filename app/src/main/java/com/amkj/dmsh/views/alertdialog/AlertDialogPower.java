package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.mine.activity.OpenVipActivity;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * Created by xiaoxin on 2020/9/8
 * Version:v4.7.0
 * ClassDescription :会员权益相关弹窗
 */
public class AlertDialogPower extends BaseAlertDialog {
    @BindView(R.id.tv_power_desc)
    TextView mTvPowerDesc;
    @BindView(R.id.tv_open)
    TextView mTvOpen;

    public AlertDialogPower(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.alert_dialog_power;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(context, 560);
    }

    @OnClick(R.id.tv_open)
    public void onViewClicked() {
        Intent intent = new Intent(context, OpenVipActivity.class);
        context.startActivity(intent);
        dismiss();
    }

    public BaseAlertDialog setDesc(String desc) {
        mTvPowerDesc.setText(desc);
        return this;
    }
}
