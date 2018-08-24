package com.amkj.dmsh.homepage.adapter;

import android.widget.Button;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.AttendanceDetailEntity.AttendanceDetailBean.ListShowBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/26
 * class description:获取积分途径
 */

public class AttendanceIntegralAdapter extends BaseQuickAdapter<ListShowBean, BaseViewHolderHelper> {
    public AttendanceIntegralAdapter(List<ListShowBean> showBeanList) {
        super(R.layout.adapter_att_integ_obtain, showBeanList);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, ListShowBean listShowBean) {
        Button bt_att_integral_complete = helper.getView(R.id.bt_att_integ_complete);
        helper.setText(R.id.tv_att_obt_title, getStrings(listShowBean.getTitle()))
                .setText(R.id.tv_att_obt_desc, getStrings(listShowBean.getReward()))
                .setGone(R.id.bt_att_integ_complete, !listShowBean.isObtain())
                .setGone(R.id.iv_att_integ_skip, listShowBean.isObtain())
                .setText(R.id.bt_att_integ_complete, listShowBean.isIsOver() ? "已完成" : "去完成");
        bt_att_integral_complete.setSelected(listShowBean.isIsOver());
        helper.itemView.setTag(listShowBean);
    }
}
