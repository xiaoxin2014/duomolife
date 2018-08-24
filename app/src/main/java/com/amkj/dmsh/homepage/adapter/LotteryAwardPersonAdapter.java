package com.amkj.dmsh.homepage.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.IntegralLotteryEntity.PreviousInfoBean.WinListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/1
 * version 3.1.5
 * class description:积分夺宝中奖人员
 */
public class LotteryAwardPersonAdapter extends BaseQuickAdapter<WinListBean,BaseViewHolderHelper>{
    private final Context context;

    public LotteryAwardPersonAdapter(Context context, List<WinListBean> winListBeans) {
        super(R.layout.adapter_lottery_award_person,winListBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, WinListBean winListBean) {
        GlideImageLoaderUtil.loadRoundImg(context,helper.getView(R.id.iv_lottery_award_person),winListBean.getAvatar(), AutoUtils.getPercentWidthSize(40),R.drawable.default_ava_img);
        helper.setText(R.id.tv_lottery_award_person_name,getStrings(winListBean.getNickName()))
                .setText(R.id.tv_lottery_award_person_code,getStrings(winListBean.getWinningCode()));
    }
}
