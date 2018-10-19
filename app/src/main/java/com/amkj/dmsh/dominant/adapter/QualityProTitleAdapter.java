package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:无推荐语 无加入购物车
 */

public class QualityProTitleAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Context context;

    public QualityProTitleAdapter(Context context, List<LikedProductBean> likedProductBeanList) {
        super(R.layout.adapter_layout_qt_pro, likedProductBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        GlideImageLoaderUtil.loadThumbCenterCrop(context, (ImageView) helper.getView(R.id.iv_qt_pro_img)
                , likedProductBean.getPicUrl(),likedProductBean.getWaterRemark(),true);
        helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                .setText(R.id.tv_qt_pro_descrip, !TextUtils.isEmpty(likedProductBean.getName()) ?
                        getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                .setText(R.id.tv_qt_pro_price, "￥" + likedProductBean.getPrice())
                .setGone(R.id.iv_pro_add_car, false)
                .setGone(R.id.tv_qt_pro_name, false);
        FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
        if(!TextUtils.isEmpty(likedProductBean.getActivityTag())||(likedProductBean.getMarketLabelList()!=null
                &&likedProductBean.getMarketLabelList().size()>0)){
            fbl_market_label.setVisibility(View.VISIBLE);
            fbl_market_label.removeAllViews();
            if(!TextUtils.isEmpty(likedProductBean.getActivityTag())){
                fbl_market_label.addView(getLabelInstance().createLabelText(context,likedProductBean.getActivityTag(),1));
            }
            if(likedProductBean.getMarketLabelList()!=null
                    &&likedProductBean.getMarketLabelList().size()>0){
                for (MarketLabelBean marketLabelBean:likedProductBean.getMarketLabelList()) {
                    if(!TextUtils.isEmpty(marketLabelBean.getTitle())){
                        fbl_market_label.addView(getLabelInstance().createLabelText(context,marketLabelBean.getTitle(),0));
                    }
                }
            }
        }else{
            fbl_market_label.setVisibility(View.GONE);
        }
        helper.itemView.setTag(likedProductBean);
    }
}
