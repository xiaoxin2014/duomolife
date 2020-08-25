package com.amkj.dmsh.mine.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.amkj.dmsh.mine.bean.ZeroLotteryEntity.ZeroLotteryBean;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * Created by xiaoxin on 2020/8/14
 * Version:v4.7.0
 * ClassDescription :0元试用中奖名单适配器
 */
public class ZeroLotteryAdapter extends BaseQuickAdapter<ZeroLotteryBean, BaseViewHolder> {
    private final Context context;

    public ZeroLotteryAdapter(Context context, @Nullable List<ZeroLotteryBean> data) {
        super(R.layout.item_zero_lottery, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ZeroLotteryBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.iv_pic), item.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 64));
        helper.setText(R.id.tv_name, item.getNickName());
    }
}
