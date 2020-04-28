package com.amkj.dmsh.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amkj.dmsh.R;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/7
 * class description:复制粘贴
 */

public class CommunalCopyTextUtils {

    private static PopupWindow popupWindow;
    private static int popLocationX;
    private static int popLocationY;
    private static View popWindows;
    private static String[] contents = new String[1];

    public static void showPopWindow(final Context context, final TextView popLocationView, String content) {
        int[] location = new int[2];
        popLocationView.getLocationOnScreen(location);
        contents[0] = content;
        if (popupWindow == null) {
            popWindows = LayoutInflater.from(context).inflate(R.layout.layout_pop_windows_copy, null);
            popWindows.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            TextView copyText = (TextView) popWindows.findViewById(R.id.tv_communal_copy_text);
            popupWindow = new PopupWindow(popWindows, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popLocationX = (int) (location[0] + (popLocationView.getMeasuredWidth() - popWindows.getMeasuredWidth()) / 2.0);
            popLocationY = location[1] - popWindows.getMeasuredHeight() - DensityUtil.dip2px(context, 10);
            popupWindow.showAtLocation(popLocationView, Gravity.NO_GRAVITY, popLocationX, popLocationY);
            copyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", !TextUtils.isEmpty(contents[0]) ? contents[0] : "");
                    cmb.setPrimaryClip(mClipData);
                    showToast("已复制");
                }
            });
        } else {
            popLocationX = (int) (location[0] + (popLocationView.getMeasuredWidth() - popWindows.getMeasuredWidth()) / 2.0);
            popLocationY = location[1] - popWindows.getMeasuredHeight() - DensityUtil.dip2px(context, 10);
            popupWindow.showAtLocation(popLocationView, Gravity.NO_GRAVITY, popLocationX, popLocationY);
        }
    }
}
