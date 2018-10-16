package com.amkj.dmsh.dominant.initviews;

import android.app.Activity;
import android.view.View;

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
    public View onCreateContentView() {
        return createPopupById(R.layout.product_type_popwindow);
    }
}
