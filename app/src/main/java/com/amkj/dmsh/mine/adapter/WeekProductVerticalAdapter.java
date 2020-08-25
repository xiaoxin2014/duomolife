package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.glide.RoundedCornersTransformation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :每周会员特价商品纵向列表
 */
public class WeekProductVerticalAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Context context;

    public WeekProductVerticalAdapter(Context context, @Nullable List<LikedProductBean> data) {
        super(R.layout.item_vip_product_vertical, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean item) {
        if (item == null) return;
        TextView view = helper.getView(R.id.tv_market_price);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        view.getPaint().setAntiAlias(true);
        ImageView ivPic = helper.getView(R.id.iv_pic);
        GlideImageLoaderUtil.loadRoundImg(context, ivPic, item.getPicUrl(), AutoSizeUtils.mm2px(mAppContext, 10),
                R.drawable.load_loading_image, RoundedCornersTransformation.CornerType.TOP_LEFT);
        helper.setText(R.id.tv_name, getStrings(item.getName()))
                .setText(R.id.tv_subtitle, getStrings(item.getSubtitle()))
                .setText(R.id.tv_price, ConstantMethod.getRmbFormat(context, item.getVipPrice()))
                .setText(R.id.tv_market_price, "¥" + getStrings(item.getMarketPrice()))
                .setText(R.id.tv_quantity, getIntegralFormat(context, R.string.vip_stock, item.getQuantity()));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipProductUrl(context, 1,item.getId());
            }
        });
        helper.itemView.setTag(item.getId());
    }
}
