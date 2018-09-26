package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.IntegralLotteryAwardEntity.LotteryInfoListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:积分夺宝奖励
 */
public class IntegralLotteryAwardAdapter extends BaseQuickAdapter<LotteryInfoListBean, BaseViewHolder> {

    private final Context context;

    public IntegralLotteryAwardAdapter(Context context, List<LotteryInfoListBean> lotteryInfoListBeans) {
        super(R.layout.adapter_integral_lottery_award, lotteryInfoListBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LotteryInfoListBean lotteryInfoListBean) {
        ImageView iv_integral_lottery_award_image = helper.getView(R.id.iv_integral_lottery_award_image);
        ImageView iv_integral_lottery_award_tag = helper.getView(R.id.iv_integral_lottery_award_tag);
        if (lotteryInfoListBean.getStatus() == 3 || lotteryInfoListBean.getStatus() == 4) {
            iv_integral_lottery_award_tag.setVisibility(View.VISIBLE);
            iv_integral_lottery_award_tag.setSelected(lotteryInfoListBean.getStatus() == 3);
        } else {
            iv_integral_lottery_award_tag.setVisibility(View.GONE);
        }
        GlideImageLoaderUtil.loadCenterCrop(context, iv_integral_lottery_award_image, lotteryInfoListBean.getImage());
        helper.setText(R.id.tv_integral_lottery_award_count, "x1")
                .setText(R.id.tv_integral_lottery_award_name, getStrings(lotteryInfoListBean.getPrizeName()))
                .setVisible(R.id.tv_integral_lottery_award_get, lotteryInfoListBean.getStatus() == 2)
                .setText(R.id.tv_integral_lottery_award_time,getStrings(lotteryInfoListBean.getActivityCode()))
                .itemView.setTag(lotteryInfoListBean);
    }
}
