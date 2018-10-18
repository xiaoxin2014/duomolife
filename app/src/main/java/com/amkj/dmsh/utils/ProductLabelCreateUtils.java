package com.amkj.dmsh.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/18
 * version 3.1.8
 * class description:标签创建
 */
public class ProductLabelCreateUtils {
    public static TextView createLabelText(Context context, String labelText, int labelCode) {
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
}
