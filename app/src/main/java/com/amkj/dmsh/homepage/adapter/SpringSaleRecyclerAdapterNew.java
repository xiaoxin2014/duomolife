package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.SpringSaleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/2
 * class description:限时特惠商品
 */
public class SpringSaleRecyclerAdapterNew extends BaseMultiItemQuickAdapter<SpringSaleBean, BaseViewHolderHelper> {
    private final List<SpringSaleBean> saleTimeTotalList;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private Map<Integer, SpringSaleBean> beanMap = new HashMap<>();
    private final Context context;
    private ConstantMethod constantMethod;

    public SpringSaleRecyclerAdapterNew(Context context, List<SpringSaleBean> saleTimeTotalList) {
        super(saleTimeTotalList);
        this.saleTimeTotalList = saleTimeTotalList;
        this.context = context;
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_promotion_pro_item);
        addItemType(ConstantVariable.TYPE_1, R.layout.adapter_promotion_foreshow_date_header);
        getConstant();
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    public int getItemCount() {
        if (saleTimeTotalList != null && saleTimeTotalList.size() > 0) {
            synchronized (this) {
                for (int i = 0; i < saleTimeTotalList.size(); i++) {
                    SpringSaleBean springSaleBean = saleTimeTotalList.get(i);
                    if (springSaleBean.getItemType() == 1) {
                        SpringSaleBean oldSpringSaleBean = beanMap.get(i);
                        if (oldSpringSaleBean == null || !oldSpringSaleBean.getCurrentTime().equals(springSaleBean.getCurrentTime())) {
                            constantMethod.createSchedule();
                            beanMap.put(i, springSaleBean);
                        }
                    }
                }
            }
        }
        return super.getItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (constantMethod != null) {
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    if (saleTimeTotalList != null && saleTimeTotalList.size() > 0) {
//                刷新数据
                        refreshData();
                    }
                }
            });
        }
    }

    private void refreshData() {
        for (Map.Entry<Integer, SpringSaleBean> entry : beanMap.entrySet()) {
            SpringSaleBean springSaleBean = entry.getValue();
            springSaleBean.setAddSecond(springSaleBean.getAddSecond() + 1);
            beanMap.put(entry.getKey(), springSaleBean);
            if(springSaleBean.getTimeObject()!=null){
                setCountTime(springSaleBean, (CountdownView) springSaleBean.getTimeObject());
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        constantMethod.releaseHandlers();
        constantMethod.stopSchedule();
        super.onDetachedFromRecyclerView(recyclerView);
    }


    private void setCountTime(SpringSaleBean springSaleBean, CountdownView cv_countdownTime) {
        DynamicConfig.Builder dynamicConfigBuilder = new DynamicConfig.Builder();
        if (springSaleBean != null) {
            if (isTimeStart(springSaleBean)) {
                try {
                    //格式化结束时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    Date dateEnd = formatter.parse(springSaleBean.getEndTime());
                    Date dateCurrent;
                    if (!TextUtils.isEmpty(springSaleBean.getCurrentTime())) {
                        dateCurrent = formatter.parse(springSaleBean.getCurrentTime());
                    } else {
                        dateCurrent = new Date();
                    }
                    DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
                    backgroundInfo
                            .setBorderRadius(AutoUtils.getPercentWidth1px() * 5)
                            .setSize(AutoUtils.getPercentWidth1px() * 16)
                            .setColor(context.getResources().getColor(R.color.ct_deep_red_bg))
                            .setShowTimeBgBorder(false);
                    dynamicConfigBuilder.setBackgroundInfo(backgroundInfo);
                    cv_countdownTime.dynamicShow(dynamicConfigBuilder.build());
                    cv_countdownTime.updateShow(dateEnd.getTime() - dateCurrent.getTime() - springSaleBean.getAddSecond() * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
//            特惠预告
                try {
                    //格式化开始时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                    Date dateStart = formatter.parse(springSaleBean.getStartTime());
                    Date dateCurrent;
                    if (!TextUtils.isEmpty(springSaleBean.getCurrentTime())) {
                        dateCurrent = formatter.parse(springSaleBean.getCurrentTime());
                    } else {
                        dateCurrent = new Date();
                    }
                    DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
                    backgroundInfo
                            .setBorderRadius(AutoUtils.getPercentWidth1px() * 10)
                            .setSize(AutoUtils.getPercentWidth1px() * 16)
                            .setColor(context.getResources().getColor(R.color.blackColor))
                            .setShowTimeBgBorder(false);
                    dynamicConfigBuilder.setBackgroundInfo(backgroundInfo);
                    cv_countdownTime.dynamicShow(dynamicConfigBuilder.build());
                    cv_countdownTime.updateShow(dateStart.getTime() - dateCurrent.getTime() - springSaleBean.getAddSecond() * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!ConstantMethod.isEndOrStartTimeAddSeconds(springSaleBean.getCurrentTime()
                    , springSaleBean.getEndTime()
                    , springSaleBean.getAddSecond())) {
                cv_countdownTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        cv.setOnCountdownEndListener(null);
                        EventBus.getDefault().post(new EventMessage("onTime", cv.getTag()));
                    }
                });
            } else {
                cv_countdownTime.setOnCountdownEndListener(null);
            }
        }
    }


    private boolean isTimeStart(SpringSaleBean springSaleBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
            Date dateStart = formatter.parse(springSaleBean.getStartTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(springSaleBean.getCurrentTime())) {
                dateCurrent = formatter.parse(springSaleBean.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            if (dateCurrent.getTime() >= dateStart.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, SpringSaleBean springSaleBean) {
        switch (helper.getItemViewType()) {
//            单品
            case ConstantVariable.TYPE_0:
                if (springSaleBean.getQuantity() < 1) {
                    helper.setGone(R.id.img_spring_sale_tag_out, true);
                } else {
                    helper.setGone(R.id.img_spring_sale_tag_out, false);
                }
                GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_springSale_product), springSaleBean.getPicUrl());
                if (!TextUtils.isEmpty(springSaleBean.getMaxPrice())
                        && getFloatNumber(springSaleBean.getPrice()) < getFloatNumber(springSaleBean.getMaxPrice())) {
                    helper.setText(R.id.tv_product_duomolife_price, getStringsChNPrice(context,springSaleBean.getPrice()) + "+");
                } else {
                    helper.setText(R.id.tv_product_duomolife_price, getStringsChNPrice(context,springSaleBean.getPrice()));
                }
                helper.setText(R.id.tv_springSale_introduce, getStrings(springSaleBean.getName()));
                TextView tv_product_original_price = helper.getView(R.id.tv_product_original_price);
                tv_product_original_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_product_original_price.setText(getStringsChNPrice(context,springSaleBean.getMarketPrice()));
                ImageView iv_pro_time_warm = helper.getView(R.id.iv_pro_time_warm);
                if (!isTimeStart(springSaleBean)) {
                    helper.setGone(R.id.iv_pro_time_warm, true);
                    iv_pro_time_warm.setSelected(springSaleBean.getIsRemind() != 0);
                } else {
                    helper.setGone(R.id.iv_pro_time_warm, false);
                }
                helper.addOnClickListener(R.id.iv_pro_time_warm).setTag(R.id.iv_pro_time_warm, springSaleBean);
                springSaleBean.setTimeObject(null);
                helper.itemView.setTag(springSaleBean);
                break;
//            品牌团
            case ConstantVariable.TYPE_1:
                helper.setIsRecyclable(false);
                helper.setText(R.id.tv_foreShow_pro_status, isTimeStart(springSaleBean) ? "抢购中" : "即将开始")
                        .setText(R.id.tv_show_communal_time_status, isTimeStart(springSaleBean) ? "距结束" : "距开始");
                CountdownView ct_time_communal_show_bg = helper.getView(R.id.ct_time_communal_show_bg);
                springSaleBean.setTimeObject(ct_time_communal_show_bg);
                setCountTime(springSaleBean, ct_time_communal_show_bg);
                break;
        }
    }
}
