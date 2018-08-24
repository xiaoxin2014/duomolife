package com.amkj.dmsh.dominant.initviews;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;

import com.amkj.dmsh.R;

import razerdp.basepopup.BasePopupWindow;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/3/13
 * version 3.0.9
 * class description:位于控件底下popWindows
 */

public class BottomPopWindows extends BasePopupWindow {
    public BottomPopWindows(Activity context) {
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
        return createPopupById(R.layout.product_type_popwindow);
    }

    @Override
    public View initAnimaView() {
        return null;
    }
}
