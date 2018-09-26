package com.amkj.dmsh.homepage.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.AttendanceDetailEntity.AttendanceDetailBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/2
 * @update 3.1.5
 * class description:签到奖励
 */

public class AttendanceAwardAdapter extends BaseQuickAdapter<AttendanceDetailBean, BaseViewHolder> {
    public AttendanceAwardAdapter(List<AttendanceDetailBean> attendanceDetailBeans) {
        super(R.layout.adapter_attendance_award, attendanceDetailBeans);
    }

    @Override
    protected void convert(BaseViewHolder helper, AttendanceDetailBean attendanceDetailBean) {
        TextView tv_attendance_award_text = helper.getView(R.id.tv_attendance_award_text);
        ImageView iv_attendance_award_icon = helper.getView(R.id.iv_attendance_award_icon);
        tv_attendance_award_text.setText(getStrings(attendanceDetailBean.getToWeek()));
        iv_attendance_award_icon.setEnabled(true);
        iv_attendance_award_icon.setImageResource(R.drawable.sel_attendance_award);
        switch (attendanceDetailBean.getWeekCode()) {
            case 1:
                iv_attendance_award_icon.setSelected(true);
                break;
            case 2:
                iv_attendance_award_icon.setSelected(false);
                break;
            default:
                iv_attendance_award_icon.setEnabled(false);
                break;

        }
        if("周日".equals(attendanceDetailBean.getToWeek())){
            iv_attendance_award_icon.setImageResource(R.drawable.sign_gift);
        }
        helper.itemView.setTag(attendanceDetailBean);
    }
}
