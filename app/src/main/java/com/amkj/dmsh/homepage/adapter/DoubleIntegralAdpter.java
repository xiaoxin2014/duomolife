package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/13
 * version 3.1.5
 * class description:双倍积分适配器
 */
public class DoubleIntegralAdpter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolder> {

    private final Context context;

    public DoubleIntegralAdpter(Context context, List<LikedProductBean> likedProductBeans) {
        super(likedProductBeans);
        this.context = context;
        addItemType(TYPE_1, R.layout.adapter_integral_double_product);
        addItemType(TYPE_2, R.layout.layout_integral_pro_header);
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (helper.getItemViewType() == TYPE_1) {
            GlideImageLoaderUtil.loadSquareImg(context, helper.getView(R.id.iv_integral_double_product_img)
                    , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), AutoSizeUtils.mm2px(mAppContext, 350));
            helper.setGone(R.id.iv_integral_double_product_tag_out, likedProductBean.getQuantity() < 1)
                    .setText(R.id.tv_integral_double_product_name, !TextUtils.isEmpty(likedProductBean.getName()) ?
                            getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                    .setText(R.id.tv_integral_double_product_price, getStringsChNPrice(context, likedProductBean.getPrice()))
                    .setText(R.id.tv_integral_double_product_return, String.format(context.getResources().getString(R.string.buy_return_integral_total), likedProductBean.getBuyIntegral()))
                    .setGone(R.id.tv_integral_double_product_return, likedProductBean.getBuyIntegral() > 0)
                    .itemView.setTag(likedProductBean);
        } else if (helper.getItemViewType() == TYPE_2) {
            helper.setText(R.id.tv_integral_pro_shop, "双倍积分")
                    .addOnClickListener(R.id.tv_integral_pro_type);
        }
    }
}
