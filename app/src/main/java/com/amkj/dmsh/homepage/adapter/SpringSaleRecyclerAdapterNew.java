package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.BaseTimeProductTopicBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeForeShowBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeShaftBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeTopicBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_3;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/2
 * class description:限时特惠商品
 */
public class SpringSaleRecyclerAdapterNew extends BaseMultiItemQuickAdapter<BaseTimeProductTopicBean, BaseViewHolderHelper> {
    private final Context context;

    public SpringSaleRecyclerAdapterNew(Context context, List<BaseTimeProductTopicBean> saleTimeTotalList) {
        super(saleTimeTotalList);
        this.context = context;
        addItemType(TYPE_0, R.layout.adapter_promotion_pro_item);
        addItemType(TYPE_1, R.layout.adapter_promotion_foreshow_topic);
        addItemType(TYPE_2, R.layout.adapter_promotion_foreshow_date_header);
        addItemType(TYPE_3, R.layout.adapter_promotion_foreshow_recommend_header);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, BaseTimeProductTopicBean productTopicBean) {
        switch (helper.getItemViewType()) {
//            单品
            case TYPE_0:
                TimeForeShowBean timeForeShowBean = (TimeForeShowBean) productTopicBean;
                if (timeForeShowBean.getQuantity() < 1) {
                    helper.setGone(R.id.img_spring_sale_tag_out, true);
                } else {
                    helper.setGone(R.id.img_spring_sale_tag_out, false);
                }
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_springSale_product_image), timeForeShowBean.getPicUrl());
                if (!TextUtils.isEmpty(timeForeShowBean.getMaxPrice())
                        && getFloatNumber(timeForeShowBean.getPrice()) < getFloatNumber(timeForeShowBean.getMaxPrice())) {
                    helper.setText(R.id.tv_product_promotion_price, getStringsChNPrice(context, timeForeShowBean.getPrice()) + "+");
                } else {
                    helper.setText(R.id.tv_product_promotion_price, getStringsChNPrice(context, timeForeShowBean.getPrice()));
                }
                helper.setText(R.id.tv_springSale_introduce, getStrings(timeForeShowBean.getName()))
                        .setGone(R.id.tv_product_promotion_join_count, !TextUtils.isEmpty(timeForeShowBean.getFlashBuyClickCount()))
                        .setText(R.id.tv_product_promotion_join_count, String.format(context.getResources().getString(R.string.time_join_group_count)
                                , getStrings(timeForeShowBean.getFlashBuyClickCount())))
                        .itemView.setTag(timeForeShowBean);

//                ImageView iv_pro_time_warm = helper.getView(R.id.iv_pro_time_warm);
//                if (!isTimeStart(springSaleBean)) {
//                    helper.setGone(R.id.iv_pro_time_warm, true);
//                    iv_pro_time_warm.setSelected(springSaleBean.getIsRemind() != 0);
//                } else {
//                    helper.setGone(R.id.iv_pro_time_warm, false);
//                }
//                helper.addOnClickListener(R.id.iv_pro_time_warm).setTag(R.id.iv_pro_time_warm, productTopicBean);
                break;
//            品牌团
            case TYPE_1:
                TimeTopicBean timeTopicBean = (TimeTopicBean) productTopicBean;
                GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_time_spring_topic_image), getStrings(timeTopicBean.getPicUrl()));
                helper.setText(R.id.tv_time_spring_topic_name, getStrings(timeTopicBean.getTitle())).itemView.setTag(timeTopicBean);
                break;
            case TYPE_2:
                TimeShaftBean timeShaftBean = (TimeShaftBean) productTopicBean;
                helper.setGone(R.id.tv_foreShow_product_time_week, !TextUtils.isEmpty(timeShaftBean.getTimeDayWeek()))
                        .setText(R.id.tv_foreShow_product_time_week, getStrings(timeShaftBean.getTimeDayWeek()))
                        .setText(R.id.tv_spring_time_shaft, String.format(context.getResources().getString(R.string.time_product_clock_new), getStrings(timeShaftBean.getTimeDayHour())));
                break;
        }
    }
}
