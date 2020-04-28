package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.ProductsBean.ProductInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.ProductsBean.ProductInfoBean.PresentInfo;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;


/**
 * Created by xiaoxin on 2020/4/2
 * Version:v4.5.0
 * ClassDescription :结算台商品列表
 */
public class WriteProductListAdapter extends BaseQuickAdapter<ProductInfoBean, BaseViewHolder> {
    private final String type;
    private final Activity context;

    public WriteProductListAdapter(Activity context, List<ProductInfoBean> list, String type) {
        super(R.layout.layout_direct_indent_product_item, list);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfoBean productInfoBean) {
        if (productInfoBean == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), productInfoBean.getPicUrl());
        helper.setText(R.id.tv_direct_indent_pro_name, getStrings(productInfoBean.getName()))
                .setText(R.id.tv_direct_indent_pro_sku, getStrings(productInfoBean.getSkuName()))
                .setText(R.id.tv_direct_pro_count, "x" + productInfoBean.getCount())
                .setGone(R.id.iv_indent_pro_use_cp, productInfoBean.getAllowCoupon() != 0)
                .setGone(R.id.tv_details_gray, productInfoBean.getShowLine() == 1)
                .setGone(R.id.tv_move, !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                .addOnClickListener(R.id.rl_cover).setTag(R.id.rl_cover, productInfoBean)
                .setGone(R.id.tv_indent_write_reason, !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                .setText(R.id.tv_indent_write_reason, getStrings(productInfoBean.getNotBuyAreaInfo()));
        switch (type) {
            //普通订单填写
            case INDENT_W_TYPE:
                if (productInfoBean.getActivityInfoBean() != null && productInfoBean.getShowActInfo() == 1) {
                    ActivityInfoBean activityInfoData = productInfoBean.getActivityInfoBean();
                    String activityTags = getStrings(activityInfoData.getActivityTag());
                    helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                            .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityTags))
                            .setText(R.id.tv_communal_activity_tag, activityTags);
                    //订单填写暂时都不允许跳转专场
//                            .addOnClickListener(R.id.ll_communal_activity_tag_rule).setTag(R.id.ll_communal_activity_tag_rule, productInfoBean);
                    helper.setGone(R.id.tv_communal_activity_tag_next, false);
                    //设置规则
                    switch (activityInfoData.getActivityType()) {
                        //显示规则，可进入专场
                        case 0:
                        case 1:
                        case 2:
                        case 4:
                        case 5:
                        case 6:
                        case 8:
                        case 10:
                            helper.setText(R.id.tv_communal_activity_tag_rule, getStrings(activityInfoData.getActivityRule()));
                            break;
                        //不显示规则，显示倒计时，可以进入专场
                        case 3:
                            setCountTime(helper, activityInfoData);
                            break;
                        //不显示规则，也不能进入专场
                        case 7:
                            helper.setText(R.id.tv_communal_activity_tag_rule, "");
                            break;
                    }
                } else {
                    helper.setGone(R.id.ll_communal_activity_topic_tag, false);
                }
                String activityPriceDesc = productInfoBean.getActivitypriceDesc();
                String price = getStringsFormat(context, R.string.shop_cart_rmb_price, getStrings(activityPriceDesc), productInfoBean.getPrice());
                if (!TextUtils.isEmpty(productInfoBean.getActivitypriceDesc())) {
                    helper.setText(R.id.tv_direct_indent_pro_price, getSpannableString(price, 0, activityPriceDesc.length(), 0.86f, null))
                            .setTextColor(R.id.tv_direct_indent_pro_price, context.getResources().getColor(R.color.text_normal_red));
                } else {
                    helper.setText(R.id.tv_direct_indent_pro_price, price)
                            .setTextColor(R.id.tv_direct_indent_pro_price, context.getResources().getColor(R.color.text_black_t));
                }

                //赠品
                helper.setGone(R.id.ll_present, productInfoBean.getPresentInfo() != null);
                if (productInfoBean.getPresentInfo() != null) {
                    PresentInfo presentInfo = productInfoBean.getPresentInfo();
                    helper.setText(R.id.tv_name, presentInfo.getName());
                    GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), presentInfo.getPicUrl());
                }
                break;
            //拼团订单填写
            case INDENT_GROUP_SHOP:
                helper.setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context, productInfoBean.getPrice()));
                break;
        }

        helper.itemView.setTag(productInfoBean);
    }

    private void setCountTime(final BaseViewHolder helper, ActivityInfoBean activityInfoData) {
        String startTime = activityInfoData.getActivityStartTime();
        String endTime = activityInfoData.getActivityEndTime();
        long currentTime = System.currentTimeMillis();
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateStart = formatter.parse(startTime);
            Date dateEnd = formatter.parse(endTime);
            if (currentTime >= dateStart.getTime() && currentTime < dateEnd.getTime()) {
                CountDownTimer countDownTimer = new CountDownTimer((LifecycleOwner) context, dateEnd.getTime() - currentTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        helper.setText(R.id.tv_communal_activity_tag_rule, "距结束 " + getCoutDownTime(millisUntilFinished, true));
                    }

                    @Override
                    public void onFinish() {
                        cancel();
                        helper.setText(R.id.tv_communal_activity_tag_rule, "已结束");
                    }
                };

                countDownTimer.start();
                helper.itemView.setTag(R.id.id_tag, countDownTimer);
            } else {
                helper.setText(R.id.tv_communal_activity_tag_rule, "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            helper.setText(R.id.tv_communal_activity_tag_rule, "");
        }
    }

    //视图被回收时，取消定时器，防止列表滚动复用导致错乱
    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        Object tag = holder.itemView.getTag(R.id.id_tag);
        if (tag != null) {
            ((CountDownTimer) tag).cancel();
        }
    }
}
