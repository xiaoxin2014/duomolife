package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.MineBabyEntity.BabyBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/27
 * class description:宝宝信息
 */

public class MineBabyInfoAdapter extends BaseQuickAdapter<BabyBean, BaseViewHolder> {
    private final Context context;

    public MineBabyInfoAdapter(Context context, List<BabyBean> babyBeanList) {
        super(R.layout.adapter_mine_baby_info, babyBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BabyBean babyBean) {
        ImageView iv_baby_info_img = helper.getView(R.id.iv_baby_info_img);
        TextView tv_baby_info_status = helper.getView(R.id.tv_baby_info_status);
        try {
            iv_baby_info_img.setImageResource(context.getResources().getIdentifier(babyBean.getPicStatus(), "drawable", "com.amkj.dmsh"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        iv_baby_info_img.getDrawable().setLevel(babyBean.getBaby_status() > 0 ? 1 : 0);
        tv_baby_info_status.setSelected(babyBean.getBaby_status() > 0 ? true : false);
        tv_baby_info_status.setText(getStrings(babyBean.getStatusName()));
        helper.itemView.setTag(babyBean);
        helper.itemView.setTag(R.id.iv_tag, iv_baby_info_img);
        helper.itemView.setTag(R.id.iv_two_tag, tv_baby_info_status);
    }
}
