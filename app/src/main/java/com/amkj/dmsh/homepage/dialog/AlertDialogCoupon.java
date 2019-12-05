package com.amkj.dmsh.homepage.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.CouponEntity.CouponListEntity.CouponBean;
import com.amkj.dmsh.homepage.adapter.CouponListAdapter;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * Created by xiaoxin on 2019/9/23
 * Version:v4.2.2
 * ClassDescription :口令红包-领取优惠券弹窗
 */
public class AlertDialogCoupon {

    private Context context;
    private RecyclerView rvCoupon;
    private AlertDialog imageAlertDialog;
    private View dialogView;
    private boolean isFirstSet;
    private CouponListAdapter mCouponListAdapter;
    private List<CouponBean> couponList = new ArrayList<>();
    private ImageView iv_dialog_image_close;

    public AlertDialogCoupon(Context context) {
        if (!isContextExisted(context)) {
            return;
        }
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_coupon_get_success, null, false);
        rvCoupon = dialogView.findViewById(R.id.rv_coupon);
        iv_dialog_image_close = dialogView.findViewById(R.id.iv_dialog_image_close);
        iv_dialog_image_close.setOnClickListener(v -> dismiss());
        builder.setCancelable(true);
        imageAlertDialog = builder.create();
        dialogView.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent(context, DirectMyCouponActivity.class);
            context.startActivity(intent);
        });
        mCouponListAdapter = new CouponListAdapter(couponList);
        rvCoupon.setLayoutManager(new LinearLayoutManager(context));
        rvCoupon.setAdapter(mCouponListAdapter);
        isFirstSet = true;
    }

    public void setCouponList(List<CouponBean> list) {
        couponList.clear();
        if (list != null) {
            couponList.addAll(list);
        }
        mCouponListAdapter.notifyDataSetChanged();
    }

    //隐藏关闭按钮
    public void hideCloseBtn() {
        iv_dialog_image_close.setVisibility(View.GONE);
    }

    /**
     * 展示dialog
     */
    public void show() {
        if (imageAlertDialog == null) {
            return;
        }
        if (!imageAlertDialog.isShowing() && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal((Activity) context);
            imageAlertDialog.show();
        }
        if (isFirstSet) {
            Window window = imageAlertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = AutoSizeUtils.mm2px(mAppContext, 500);
                window.setAttributes(params);
                window.setContentView(dialogView);
            }
        }
        isFirstSet = false;
    }

    public void dismiss() {
        if (imageAlertDialog != null && isContextExisted(context)) {
            imageAlertDialog.dismiss();
        }
    }

    /**
     * dialog 是否在展示
     *
     * @return
     */
    public boolean isShowing() {
        return imageAlertDialog != null
                && isContextExisted(context) && imageAlertDialog.isShowing();
    }
}
