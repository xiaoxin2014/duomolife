package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.ActivityInfoBean;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.ProductsBean.ProductInfoBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_GROUP_SHOP;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_W_TYPE;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.getCurrentTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;


/**
 * Created by xiaoxin on 2020/4/2
 * Version:v4.5.0
 * ClassDescription :结算台商品列表
 */
public class WriteProductListAdapter extends BaseQuickAdapter<ProductInfoBean, BaseViewHolder> {
    private final String type;
    private final Activity context;
    private CountDownTimer mCountDownTimer;
    private SparseArray<Object> sparseArray = new SparseArray<>();

    public WriteProductListAdapter(Activity context, List<ProductInfoBean> list, String type) {
        super(R.layout.layout_direct_indent_product_item, list);
        this.context = context;
        this.type = type;
        CreatCountDownTimer();
    }

    //创建定时任务
    private void CreatCountDownTimer() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(context) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //刷新倒计时
                    refreshSchedule();
                }

                @Override
                public void onFinish() {

                }
            };
            mCountDownTimer.setMillisInFuture(3600 * 24 * 30 * 1000L);
        }
        mCountDownTimer.start();
    }

    private void refreshSchedule() {
        for (int i = 0; i < sparseArray.size(); i++) {
            TextView tvRule = (TextView) sparseArray.get(sparseArray.keyAt(i));
            if (tvRule != null) {
                ActivityInfoBean activityInfoBean = (ActivityInfoBean) tvRule.getTag();
                if (activityInfoBean != null && activityInfoBean.getActivityType() == 3) {
                    setCountTime(tvRule, activityInfoBean);
                }
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfoBean productInfoBean) {
        if (productInfoBean == null) return;
        //主商品
        if (!productInfoBean.isPresent()) {
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), productInfoBean.getPicUrl());
            //商品名称
            CharSequence name = getStrings(productInfoBean.getName());
            String ecmTag = productInfoBean.getEcmTag();
            //跨境标识
            if (!TextUtils.isEmpty(ecmTag)) {
                Link link = new Link("\t" + ecmTag + "\t");
                link.setTextColor(Color.parseColor("#ffffff"));
                link.setTextSize(AutoSizeUtils.mm2px(mAppContext, 24));
                link.setBgColor(Color.parseColor("#ffb20b"));
                link.setBgRadius(AutoSizeUtils.mm2px(context, 5));
                link.setUnderlined(false);
                link.setHighlightAlpha(0f);
                name = LinkBuilder.from(context, link.getText() + "\t" + name)
                        .addLink(link)
                        .build();
            }

            helper.setText(R.id.tv_direct_indent_pro_name, name)
                    .setText(R.id.tv_direct_indent_pro_sku, getStrings(productInfoBean.getSkuName()))
                    .setText(R.id.tv_direct_pro_count, "x" + productInfoBean.getCount())
                    .setGone(R.id.iv_indent_pro_use_cp, productInfoBean.getAllowCoupon() != 0)
                    .setGone(R.id.tv_details_gray, productInfoBean.getShowLine() == 1)
                    .setGone(R.id.tv_move, !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                    .addOnClickListener(R.id.rl_cover).setTag(R.id.rl_cover, productInfoBean)
                    .setGone(R.id.tv_indent_write_reason, !TextUtils.isEmpty(productInfoBean.getNotBuyAreaInfo()))
                    .setText(R.id.tv_indent_write_reason, getStrings(productInfoBean.getNotBuyAreaInfo()))
                    .setGone(R.id.ll_product, true)
                    .setGone(R.id.ll_present, false);
            switch (type) {
                //普通订单填写
                case INDENT_W_TYPE:
                    ActivityInfoBean activityInfoBean = productInfoBean.getActivityInfoBean();
                    if (activityInfoBean != null && !TextUtils.isEmpty(activityInfoBean.getActivityTag()) && productInfoBean.getShowActInfo() == 1) {
                        ActivityInfoBean activityInfoData = productInfoBean.getActivityInfoBean();
                        String activityTags = getStrings(activityInfoData.getActivityTag());
                        helper.setGone(R.id.ll_communal_activity_topic_tag, true)
                                .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityTags))
                                .setText(R.id.tv_communal_activity_tag, activityTags);
                        //订单填写暂时都不允许跳转专场
//                            .addOnClickListener(R.id.ll_communal_activity_tag_rule).setTag(R.id.ll_communal_activity_tag_rule, productInfoBean);
                        helper.setGone(R.id.tv_communal_activity_tag_next, false);
                        //设置规则
                        TextView tvRule = helper.getView(R.id.tv_communal_activity_tag_rule);
                        switch (activityInfoData.getActivityType()) {
                            //不显示规则，显示倒计时，可以进入专场
                            case 3:
                                if (showTime(activityInfoData)) {
                                    sparseArray.put(helper.getAdapterPosition() - getHeaderLayoutCount(), tvRule);
                                    tvRule.setTag(activityInfoData);
                                }
                                setCountTime(tvRule, activityInfoData);
                                break;
                            //不显示规则，也不能进入专场
                            case 7:
                                tvRule.setText("");
                                break;
                            //显示规则，可进入专场
                            default:
                                tvRule.setText(getStrings(activityInfoData.getActivityRule()));
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


                    break;
                //拼团订单填写
                case INDENT_GROUP_SHOP:
                    helper.setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context, productInfoBean.getPrice()));
                    break;
            }
        } else {
            //赠品
            helper.setGone(R.id.ll_present, true)
                    .setGone(R.id.ll_communal_activity_topic_tag, false)
                    .setGone(R.id.ll_product, false)
                    .setGone(R.id.tv_details_gray, false);

            helper.setText(R.id.tv_name, productInfoBean.getName())
                    .setText(R.id.tv_count, "×" + productInfoBean.getCount());
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), productInfoBean.getPicUrl());
        }


        helper.itemView.setTag(productInfoBean);
    }

    private void setCountTime(TextView tvRule, ActivityInfoBean activityInfoData) {
        tvRule.setText(showTime(activityInfoData) ?
                "距结束 " + getCoutDownTime(getTimeDifference(getCurrentTime(), activityInfoData.getActivityEndTime()), true) : "");
    }

    //是否显示倒计时(活动已开始未结束)
    private boolean showTime(ActivityInfoBean activityInfoData) {
        String startTime = activityInfoData.getActivityStartTime();
        String endTime = activityInfoData.getActivityEndTime();
        String currentTime = getCurrentTime();
        return isEndOrStartTime(currentTime, startTime) && !isEndOrStartTime(currentTime, endTime);
    }
}
