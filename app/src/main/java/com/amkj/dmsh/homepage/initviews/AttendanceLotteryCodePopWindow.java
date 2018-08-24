package com.amkj.dmsh.homepage.initviews;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.amkj.dmsh.R;

import razerdp.basepopup.BasePopupWindow;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/31
 * version 3.1.5
 * class description:签到 规则
 */
public class AttendanceLotteryCodePopWindow extends BasePopupWindow {
    public AttendanceLotteryCodePopWindow(Context context) {
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
        return createPopupById(R.layout.layout_attendance_integral_lottery_code);
    }

    @Override
    public View initAnimaView() {
        return null;
    }
}
