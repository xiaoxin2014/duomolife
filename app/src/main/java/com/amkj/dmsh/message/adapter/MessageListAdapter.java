package com.amkj.dmsh.message.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import q.rorbin.badgeview.Badge;

;import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:请输入类描述
 */

public class MessageListAdapter extends BaseQuickAdapter<QualityTypeBean, MessageListAdapter.BaseViewHolderHelperBadge> {
    private final Context context;

    public MessageListAdapter(Context context, List<QualityTypeBean> messageBeanList) {
        super(R.layout.adapter_message_total, messageBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelperBadge helper, QualityTypeBean qualityTypeBean) {
        ImageView iv_mes_total = helper.getView(R.id.iv_mes_total);
        try {
            iv_mes_total.setImageResource(context.getResources().getIdentifier(qualityTypeBean.getPicUrl(), "drawable", "com.amkj.dmsh"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        helper.setText(R.id.tv_mes_total_name, getStrings(qualityTypeBean.getName()));
        helper.badge.setBadgeNumber(qualityTypeBean.getType());
        helper.itemView.setTag(qualityTypeBean);
    }

    public class BaseViewHolderHelperBadge extends BaseViewHolder {
        Badge badge;
        FrameLayout fra_home_message_total;

        public BaseViewHolderHelperBadge(View view) {
            super(view);
            fra_home_message_total = (FrameLayout) view.findViewById(R.id.fra_home_message_total);
            badge = ConstantMethod.getBadge(context, fra_home_message_total);
        }
    }
}
