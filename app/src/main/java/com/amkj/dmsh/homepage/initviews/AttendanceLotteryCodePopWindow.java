package com.amkj.dmsh.homepage.initviews;

import android.content.Context;
import android.view.View;

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
    public View onCreateContentView() {
        return createPopupById(R.layout.layout_attendance_integral_lottery_code);
    }
}
