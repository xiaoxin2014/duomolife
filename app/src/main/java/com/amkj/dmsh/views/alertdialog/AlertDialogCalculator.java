package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.mine.activity.OpenVipActivity;
import com.amkj.dmsh.mine.adapter.CalculatorAdapter;
import com.amkj.dmsh.mine.bean.CalculatorEntity;
import com.amkj.dmsh.mine.bean.CalculatorEntity.CalculatorBean.PriceDataBean;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isVip;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :省钱计算器弹窗
 */
public class AlertDialogCalculator extends BaseAlertDialog {
    @BindView(R.id.rv_save)
    RecyclerView mRvSave;
    @BindView(R.id.tv_save_year)
    TextView mTvSaveYear;
    @BindView(R.id.tv_open)
    TextView mTvOpen;

    public AlertDialogCalculator(Context context, CalculatorEntity.CalculatorBean calculatorBean) {
        super(context);
        String totalPrice = calculatorBean.getTotalPrice();
        mTvSaveYear.setText(getStrings(totalPrice));
        List<PriceDataBean> priceData = calculatorBean.getPriceData();
        if (priceData != null && priceData.size() > 0) {
            //列表初始化
            mRvSave.setLayoutManager(new LinearLayoutManager(context));
            CalculatorAdapter calculatorAdapter = new CalculatorAdapter(priceData);
            mRvSave.setAdapter(calculatorAdapter);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.alert_calculator;
    }

    @OnClick(R.id.tv_open)
    public void onViewClicked() {
        Intent intent = new Intent(context, OpenVipActivity.class);
        context.startActivity(intent);
        dismiss();
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 560);
    }

    @Override
    protected int getLayoutHeight() {
        return AutoSizeUtils.mm2px(mAppContext, 776);
    }

    @Override
    public void show() {
        super.show();
        mTvOpen.setVisibility(isVip() ? View.GONE : View.VISIBLE);
    }
}
