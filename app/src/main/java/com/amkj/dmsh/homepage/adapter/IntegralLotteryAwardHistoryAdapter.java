package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean.WinListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.zhy.autolayout.attr.Attrs;
import com.zhy.autolayout.attr.AutoAttr;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:积分夺宝奖励
 */
public class IntegralLotteryAwardHistoryAdapter extends BaseQuickAdapter<PreviousInfoBean, BaseViewHolderHelper> {

    private final Context context;

    public IntegralLotteryAwardHistoryAdapter(Context context, List<PreviousInfoBean> lotteryInfoListBeans) {
        super(R.layout.adapter_integral_lottery_award_history, lotteryInfoListBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, PreviousInfoBean previousInfoBean) {
        ImageView iv_integral_lottery_award_image = helper.getView(R.id.iv_integral_lottery_award_image);
        FlexboxLayout fbl_integral_lottery_avatar = helper.getView(R.id.fbl_integral_lottery_avatar);
        GlideImageLoaderUtil.loadCenterCrop(context, iv_integral_lottery_award_image, previousInfoBean.getImage());
        helper.setText(R.id.tv_integral_lottery_award_count, "x1")
                .setText(R.id.tv_integral_lottery_award_name, getStrings(previousInfoBean.getPrizeName()))
                .setVisible(R.id.tv_integral_lottery_award_get, false)
                .setText(R.id.tv_integral_lottery_award_time, getStrings(previousInfoBean.getStartTime()))
                .setGone(R.id.ll_integral_lottery_prize, previousInfoBean.getWinList() != null && previousInfoBean.getWinList().size() > 0)
                .setTag(R.id.ll_lottery_award, previousInfoBean)
                .addOnClickListener(R.id.ll_lottery_award);
        if (previousInfoBean.getWinList() != null && previousInfoBean.getWinList().size() > 0) {
            fbl_integral_lottery_avatar.removeAllViews();
            for (WinListBean winListBean : previousInfoBean.getWinList()) {
                fbl_integral_lottery_avatar.addView(createImageView(winListBean));
            }
        }
    }

    private ImageView createImageView(WinListBean winListBean) {
        CircleImageView imageView = new CircleImageView(context);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(60, 60);
        layoutParams.rightMargin = 24;
        imageView.setLayoutParams(layoutParams);
        AutoUtils.auto(imageView, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_WIDTH);
        GlideImageLoaderUtil.loadHeaderImg(context, imageView, getStrings(winListBean.getAvatar()));
        return imageView;
    }
}
