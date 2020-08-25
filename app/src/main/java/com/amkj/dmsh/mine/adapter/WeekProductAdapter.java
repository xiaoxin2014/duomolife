package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :每周会员特价商品
 */
public class WeekProductAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Context context;
    private final int type;
    public static final int WEEK_VIP_GOODS = 1;//每周会员特价

    public WeekProductAdapter(Context context, @Nullable List<LikedProductBean> data, int type) {
        super(R.layout.item_vip_product, data);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean item) {
        if (item == null) return;
        ImageView ivPic = helper.getView(R.id.iv_pic);
        ViewGroup.LayoutParams layoutParams = ivPic.getLayoutParams();
        layoutParams.height = AutoSizeUtils.mm2px(mAppContext, type == WEEK_VIP_GOODS ? 218 : 327);
        ivPic.setLayoutParams(layoutParams);
        GlideImageLoaderUtil.loadSquareImg(context, ivPic, item.getPicUrl(), "", AutoSizeUtils.mm2px(mAppContext, 375));
        helper.setText(R.id.tv_name, getStrings(item.getName()))
                .setText(R.id.tv_quantity, getIntegralFormat(context, R.string.vip_stock, item.getQuantity()))
                .setText(R.id.tv_price, "¥" + getStrings(getStringChangeIntegers(item.getVipPrice()) > 0 ? item.getVipPrice() : item.getPrice()))
                .setGone(R.id.tv_buy_now, type == WEEK_VIP_GOODS);

        helper.itemView.setTag(item.getId());
    }
}
