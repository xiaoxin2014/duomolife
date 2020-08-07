package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/8/5
 * Version:v4.6.9
 * ClassDescription :税费说明弹窗
 */
public class AlertDialogTax extends BaseAlertDialog {
    @BindView(R.id.tv_tax_explain)
    TextView mTvTaxExplain;
    @BindView(R.id.tv_tax_point)
    TextView mTvTaxPoint;
    @BindView(R.id.tv_tax_point_desc)
    TextView mTvTaxPointDesc;
    @BindView(R.id.ll_tax_point)
    LinearLayout mLlTaxPoint;

    public AlertDialogTax(Context context, ShopDetailsEntity.ShopPropertyBean shopPropertyBean) {
        super(context);
        if (shopPropertyBean != null) {
            mLlTaxPoint.setVisibility(View.VISIBLE);
            mTvTaxExplain.setText(getStrings(shopPropertyBean.getTaxExplain()));
            mTvTaxPoint.setText(getStrings(shopPropertyBean.getTaxPoint()));
            mTvTaxPointDesc.setText(ConstantMethod.getStringsFormat(context, R.string.tax_point_desc, shopPropertyBean.getTaxPoint()));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.alert_dialog_tax;
    }

    @OnClick(R.id.tv_confirm)
    public void onViewClicked() {
        dismiss();
    }
}
