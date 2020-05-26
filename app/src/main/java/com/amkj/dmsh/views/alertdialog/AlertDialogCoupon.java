package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.CouponEntity.CouponListEntity.CouponBean;
import com.amkj.dmsh.homepage.adapter.CouponListAdapter;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.base.BaseAlertDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * Created by xiaoxin on 2019/9/23
 * Version:v4.2.2
 * ClassDescription :口令红包-领取优惠券弹窗
 */
public class AlertDialogCoupon extends BaseAlertDialog {

    @BindView(R.id.iv_dialog_image_close)
    ImageView iv_dialog_image_close;
    @BindView(R.id.rv_coupon)
    RecyclerView rvCoupon;
    private CouponListAdapter mCouponListAdapter;
    private List<CouponBean> couponList = new ArrayList<>();

    public AlertDialogCoupon(Context context) {
        super(context);

        iv_dialog_image_close.setOnClickListener(v -> dismiss());

        //查看我的优惠券
        dialogView.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent(context, DirectMyCouponActivity.class);
            context.startActivity(intent);
        });

        //初始化优惠券列表
        mCouponListAdapter = new CouponListAdapter(couponList);
        rvCoupon.setLayoutManager(new LinearLayoutManager(context));
        rvCoupon.setAdapter(mCouponListAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_coupon_get_success;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 500);
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
}
