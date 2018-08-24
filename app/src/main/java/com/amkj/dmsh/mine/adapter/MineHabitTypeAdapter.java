package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.mine.bean.HabitTypeEntity.HabitTypeBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/21
 * class description:兴趣类型
 */

public class MineHabitTypeAdapter extends BaseQuickAdapter<HabitTypeBean, BaseViewHolderHelper> {
    private final Context context;

    public MineHabitTypeAdapter(Context context, @Nullable List<HabitTypeBean> habitTypeList) {
        super(R.layout.adapter_mint_habit_type, habitTypeList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, HabitTypeBean habitTypeBean) {
        ImageView iv_habit_type = helper.getView(R.id.iv_habit_type);
        ImageView iv_round_habit_type_fg = helper.getView(R.id.iv_round_habit_type_fg);
        GlideImageLoaderUtil.loadRoundImg(context, iv_habit_type, habitTypeBean.getImg()
                , 4);
        iv_habit_type.setSelected(habitTypeBean.getIsOpen() > 0);
        if (iv_habit_type.isSelected()) {
            iv_round_habit_type_fg.setVisibility(View.VISIBLE);
        } else {
            iv_round_habit_type_fg.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_habit_type_name, getStrings(habitTypeBean.getInterest_name()));
        helper.itemView.setTag(habitTypeBean);
        helper.itemView.setTag(R.id.iv_two_tag, iv_round_habit_type_fg);
    }
}
