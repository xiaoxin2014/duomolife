package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getTopBadge;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/7
 * class description:请输入类描述
 */

public class MineTypeAdapter extends BaseQuickAdapter<QualityTypeBean, MineTypeAdapter.TypeViewHolderHelper> {
    private final Context context;

    public MineTypeAdapter(Context context, List<QualityTypeBean> mineTypeList) {
        super(R.layout.adapter_mine_bottom, mineTypeList);
        this.context = context;
    }

    @Override
    protected void convert(TypeViewHolderHelper helper, QualityTypeBean qualityTypeBean) {
        Badge badge = helper.badge;
        if (badge != null) {
            badge.setBadgeNumber(qualityTypeBean.getType());
        }
        try {
            helper.iv_mine_type_icon.setImageResource(context.getResources().getIdentifier(qualityTypeBean.getPicUrl(), "drawable", "com.amkj.dmsh"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        helper.setText(R.id.tv_mine_type_title, getStrings(qualityTypeBean.getName()));
        helper.itemView.setTag(qualityTypeBean);
    }

    public class TypeViewHolderHelper extends BaseViewHolderHelper{
        Badge badge;
        ImageView iv_mine_type_icon;
        public TypeViewHolderHelper(View view) {
            super(view);
            iv_mine_type_icon = (ImageView) view.findViewById(R.id.iv_mine_type_icon);
            badge = getTopBadge(context, iv_mine_type_icon);
        }
    }
}
