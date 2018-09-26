package com.amkj.dmsh.mine.adapter;

import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.MesPushTypeEntity.MesPushTypeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/28
 * class description:消息推送设置
 */

public class MesPushTypeSetAdapter extends BaseQuickAdapter<MesPushTypeBean, BaseViewHolder> {
    public MesPushTypeSetAdapter(List<MesPushTypeBean> data) {
        super(R.layout.adapter_mes_push_type_clo_open, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MesPushTypeBean mesPushTypeBean) {
        ImageView iv_mes_push_type_clo_open = helper.getView(R.id.iv_mes_push_type_clo_open);
        iv_mes_push_type_clo_open.setSelected(mesPushTypeBean.getIsOpen() > 0);
        helper.setText(R.id.tv_mes_push_type_name, getStrings(mesPushTypeBean.getName())).itemView.setTag(mesPushTypeBean);
    }
}
