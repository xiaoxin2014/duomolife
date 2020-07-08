package com.amkj.dmsh.utils;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/27
 * class description:动态选择器
 */

public class SelectorUtil {
    public static ColorStateList getColorStateList(String normalColor, String checkColor) {
        int[] colors = new int[]{Color.parseColor((normalColor)), Color.parseColor((checkColor))};
        int[][] states = new int[2][];
        states[0] = new int[]{-android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_checked};
        return new ColorStateList(states, colors);
    }

    public static StateListDrawable getDrawableStateList(Drawable checkDrawable, Drawable normalDrawable) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_checked}, checkDrawable);
        drawable.addState(new int[]{-android.R.attr.state_checked}, normalDrawable);
        drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, 32f / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, 32f)); //设置边界
        return drawable;
    }

    public static StateListDrawable getDrawableStateList(int checkResId, int normalResId) {
        Drawable checkDrawable = mAppContext.getDrawable(checkResId);
        Drawable normalDrawable = mAppContext.getDrawable(normalResId);
        return getDrawableStateList(checkDrawable, normalDrawable);
    }

    public static StateListDrawable getDrawableStateList(String checkPicPath, String normalPicPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(normalPicPath);
        Bitmap secBitmap = BitmapFactory.decodeFile(checkPicPath);
        BitmapDrawable checkDrawable = new BitmapDrawable(null, secBitmap);
        BitmapDrawable normalDrawable = new BitmapDrawable(null, bitmap);
        return getDrawableStateList(checkDrawable, normalDrawable);
    }
}
