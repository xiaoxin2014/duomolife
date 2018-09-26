package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.QualityGoodProductEntity.Attribute;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:好物
 */

public class QualityGoodNewProAdapter extends BaseQuickAdapter<Attribute, BaseViewHolder> {
    private final Context context;

    public QualityGoodNewProAdapter(Context context, List<Attribute> goodsProList) {
        super(R.layout.adapter_layout_qt_pro, goodsProList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Attribute attribute) {
        LinearLayout ll_quality_product = helper.getView(R.id.ll_quality_product);
        ImageView iv_quality_good_product_ad = helper.getView(R.id.iv_quality_good_product_ad);
        ll_quality_product.setVisibility(View.GONE);
        iv_quality_good_product_ad.setVisibility(View.GONE);
        if(attribute==null){
            return;
        }
        switch (getStrings(attribute.getObjectType())) {
            case "product":
                ll_quality_product.setVisibility(View.VISIBLE);
                LikedProductBean likedProductBean = (LikedProductBean) attribute;
                GlideImageLoaderUtil.loadThumbCenterCrop(context, (ImageView) helper.getView(R.id.iv_qt_pro_img)
                        , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), true);
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
                break;
            case "ad":
                CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) attribute;
                iv_quality_good_product_ad.setVisibility(View.VISIBLE);
                GlideImageLoaderUtil.loadCenterCrop(context,iv_quality_good_product_ad,getStrings(communalADActivityBean.getPicUrl()));
                break;
        }
        helper.itemView.setTag(attribute);
    }
}
