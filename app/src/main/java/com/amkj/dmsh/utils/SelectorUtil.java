package com.amkj.dmsh.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.RadioButton;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/27
 * class description:动态选择器
 */

public class SelectorUtil {
    public static void selectorTextColor(String checkColor,String normalColor,  RadioButton radioButton) {
        int[] colors = new int[]{Color.parseColor((checkColor)), Color.parseColor((normalColor))};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{-android.R.attr.state_checked};
        radioButton.setTextColor(new ColorStateList(states, colors));
    }
}
