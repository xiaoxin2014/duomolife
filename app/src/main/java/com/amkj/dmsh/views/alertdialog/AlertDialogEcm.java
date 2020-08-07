package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * Created by xiaoxin on 2020/7/30
 * Version:v4.6.9
 * ClassDescription :购物车结算-保税仓弹窗
 */
public class AlertDialogEcm extends BaseAlertDialog {
    @BindView(R.id.rb_ecm)
    RadioButton mRbEcm;
    @BindView(R.id.rb_other)
    RadioButton mRbOther;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.tv_back)
    TextView mTvBack;
    @BindView(R.id.tv_settle)
    TextView mTvSettle;
    private OnSettleClickListener settleClickListener;

    public AlertDialogEcm(Context context) {
        super(context);
    }

    public void setOnSettleListener(OnSettleClickListener settleClickListener) {
        this.settleClickListener = settleClickListener;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 540);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.alert_dialog_ecm;
    }

    @OnClick({R.id.tv_back, R.id.tv_settle})
    public void onViewClicked(View view) {
        dismiss();
        if (view.getId() == R.id.tv_settle) {
            settleClickListener.settle(mRbEcm.isChecked());
        }
    }

    public interface OnSettleClickListener {
        void settle(boolean isEcm);
    }
}
