package com.amkj.dmsh.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.activity.ProductLabelDetailActivity;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/18
 * version 3.1.8
 * class description:标签创建
 */
public class ProductLabelCreateUtils implements View.OnClickListener {
    private static ProductLabelCreateUtils productLabelCreateUtils;
    private Context context;

    private ProductLabelCreateUtils() {
    }

    public static ProductLabelCreateUtils getLabelInstance() {
        if (productLabelCreateUtils == null) {
            synchronized (QyServiceUtils.class) {
                if (productLabelCreateUtils == null) {
                    productLabelCreateUtils = new ProductLabelCreateUtils();
                }
            }
        }
        return productLabelCreateUtils;
    }

    /**
     * @param context
     * @param labelText
     * @param labelCode 1 为红色 活动 0 为黄色 营销
     * @return
     */
    public TextView createLabelText(Context context, String labelText, int labelCode) {
        this.context = context;
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        int tenLeftRight = AutoSizeUtils.mm2px(context, 7);
        int fiveTopBottom = AutoSizeUtils.mm2px(context, 3);
        textView.setPadding(tenLeftRight, fiveTopBottom, tenLeftRight, fiveTopBottom);
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (labelCode == 1) {
            gradientDrawable.setColor(context.getResources().getColor(R.color.text_pink_red));
        } else {
            gradientDrawable.setColor(context.getResources().getColor(R.color.text_yel_f_s));
        }
        gradientDrawable.setCornerRadius(AutoSizeUtils.mm2px(context, 4));
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setBackground(gradientDrawable);
        textView.setText(labelText);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, 22));
        return textView;
    }

    /**
     *
     * @param context
     * @param marketLabelBean 活动 营销
     * @return
     */
    public TextView createLabelClickText(Context context, MarketLabelBean marketLabelBean) {
        this.context = context;
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        int tenLeftRight = AutoSizeUtils.mm2px(context, 15);
        int fiveTopBottom = AutoSizeUtils.mm2px(context, 7);
        textView.setPadding(tenLeftRight, fiveTopBottom, tenLeftRight, fiveTopBottom);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(R.color.white));
        int colorValue;
        if (marketLabelBean.getLabelCode() == 1) {
            colorValue = context.getResources().getColor(R.color.text_pink_red);
        } else {
            colorValue = context.getResources().getColor(R.color.text_yel_f_s);
        }
        gradientDrawable.setStroke(1, colorValue);
        gradientDrawable.setCornerRadius(AutoSizeUtils.mm2px(context, 4));
        textView.setTextColor(colorValue);
        textView.setBackground(gradientDrawable);
        textView.setText(getStringsFormat(context,R.string.communal_go_string,getStrings(marketLabelBean.getTitle())));
        textView.setTag(marketLabelBean);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, 24));
        textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void onClick(View view) {
        MarketLabelBean marketLabelBean = (MarketLabelBean) view.getTag();
        if (marketLabelBean != null) {
            Intent intent = new Intent();
            if(marketLabelBean.getId() > 0){
                intent.setClass(context,ProductLabelDetailActivity.class);
                intent.putExtra("productLabelId",String.valueOf(marketLabelBean.getId()));
            }else if(!TextUtils.isEmpty(marketLabelBean.getActivityCode())){
                intent.setClass(context, QualityProductActActivity.class);
                intent.putExtra("activityCode", marketLabelBean.getActivityCode());
            }else{
                return;
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
