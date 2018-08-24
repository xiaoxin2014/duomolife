package com.amkj.dmsh.shopdetails.integration.initview;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;

import com.amkj.dmsh.R;

import razerdp.basepopup.BasePopupWindow;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/13
 * version 3.1.5
 * class description:积分兑换
 */

public class IntegralPopWindows extends BasePopupWindow {
    public IntegralPopWindows(Activity context) {
        super(context);
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popwindow_product_integral);
    }

    @Override
    public View initAnimaView() {
        return null;
    }
}
