package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/13
 * version 3.1.5
 * class description:双倍积分 自定义专区
 */
public class QualityCustomTopicAdapter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolderHelper> {

    private final Context context;

    public QualityCustomTopicAdapter(Context context, List<LikedProductBean> likedProductBeans) {
        super(likedProductBeans);
        this.context = context;
        addItemType(TYPE_0, R.layout.adapter_layout_qt_pro);
        addItemType(TYPE_1, R.layout.adapter_integral_double_product);
        addItemType(TYPE_2, R.layout.layout_integral_pro_header);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, LikedProductBean likedProductBean) {
        if(helper.getItemViewType() == TYPE_0){
            GlideImageLoaderUtil.loadThumbCenterCrop(context, (ImageView) helper.getView(R.id.iv_qt_pro_img)
                    , likedProductBean.getPicUrl(),likedProductBean.getWaterRemark(),true);
            helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                    .setText(R.id.tv_qt_pro_descrip, getStrings(likedProductBean.getSubtitle()))
                    .setText(R.id.tv_qt_pro_name, !TextUtils.isEmpty(likedProductBean.getName()) ?
                            getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                    .setText(R.id.tv_qt_pro_price, "￥" + likedProductBean.getPrice())
                    .setGone(R.id.tv_qt_pro_wait_buy, getStrings(likedProductBean.getSellStatus()).equals("待售"))
                    .setGone(R.id.tv_qt_pro_activity_tag, !TextUtils.isEmpty(likedProductBean.getTagContent()))
                    .setText(R.id.tv_qt_pro_activity_tag, getStrings(likedProductBean.getTagContent()))
                    .addOnClickListener(R.id.iv_pro_add_car).setTag(R.id.iv_pro_add_car, likedProductBean)
                    .setGone(R.id.tv_communal_pro_red_tag, !TextUtils.isEmpty(likedProductBean.getActivityTag()))
                    .setText(R.id.tv_communal_pro_red_tag, getStrings(likedProductBean.getActivityTag()));
            helper.itemView.setTag(likedProductBean);
        }else if (helper.getItemViewType() == TYPE_1) {
            GlideImageLoaderUtil.loadThumbCenterCrop(context, (ImageView) helper.getView(R.id.iv_integral_double_product_img)
                    , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), true);
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
