package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:商品列表
 */

public class QualityTypeProductAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Context context;

    public QualityTypeProductAdapter(Context context, List<LikedProductBean> likedProductBeanList) {
        super(R.layout.adapter_layout_qt_pro, likedProductBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
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
    }
}
