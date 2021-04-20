package com.amkj.dmsh.views.alertdialog;

import android.content.Context;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;


/**
 * Created by xiaoxin on 2021/4/20
 * Version:v5.1.0
 * ClassDescription :评价成功弹窗
 */
public class AlertDialogEvaluateImg extends AlertDialogImage {

    public AlertDialogEvaluateImg(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 650);
    }
}
