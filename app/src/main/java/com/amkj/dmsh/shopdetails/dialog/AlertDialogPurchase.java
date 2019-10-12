package com.amkj.dmsh.shopdetails.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.adapter.PurchaseCoverAdapter;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo.GoodsListBean;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * Created by xiaoxin on 2019/9/30
 * Version:v4.3.0
 * ClassDescription :加价购活动弹窗
 */
public class AlertDialogPurchase {

    private Context context;
    private AlertDialog imageAlertDialog;
    private AddOrderListener mAddOrderListener;
    private View dialogView;
    private boolean isFirstSet;
    private PurchaseCoverAdapter mPurchaseCoverAdapter;
    private List<GoodsListBean> goodList = new ArrayList<>();
    private TextView mTvDiscountEdition;
    private ImageView mIvLeft;
    private ViewPager mVpGoods;
    private ImageView mIvRight;
    private TextView mTvTitle;
    private TextView mTvDicountPrice;
    private TextView mTvDicountInfo;
    private TextView mTvCancle;
    private TextView mTvAdd;

    public AlertDialogPurchase(Context context) {
        if (!isContextExisted(context)) {
            return;
        }
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog_purchase, null, false);
        imageAlertDialog = builder.create();
        mTvDiscountEdition = dialogView.findViewById(R.id.tv_discount_edition);
        mIvLeft = dialogView.findViewById(R.id.iv_left);
        mVpGoods = dialogView.findViewById(R.id.vp_goods);
        mIvRight = dialogView.findViewById(R.id.iv_right);
        mTvTitle = dialogView.findViewById(R.id.tv_title);
        mTvDicountInfo = dialogView.findViewById(R.id.tv_discount_info);
        mTvDicountPrice = dialogView.findViewById(R.id.tv_dicount_price);
        mTvCancle = dialogView.findViewById(R.id.tv_cancle);
        mTvAdd = dialogView.findViewById(R.id.tv_add);
        mPurchaseCoverAdapter = new PurchaseCoverAdapter(context, goodList);
        mVpGoods.setAdapter(mPurchaseCoverAdapter);
        mVpGoods.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                updateGoodInfo(goodList.get(i), i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mIvLeft.setOnClickListener(v -> {
            int currentItem = mVpGoods.getCurrentItem();
            if (currentItem - 1 >= 0) {
                mVpGoods.setCurrentItem(currentItem - 1);
            }
        });
        mIvRight.setOnClickListener(v -> {
            int currentItem = mVpGoods.getCurrentItem();
            if (currentItem + 1 <= goodList.size()) {
                mVpGoods.setCurrentItem(currentItem + 1);
            }
        });
        mTvCancle.setOnClickListener(v -> dismiss());
        mTvAdd.setOnClickListener(v -> {
            if (mAddOrderListener != null) {
                mAddOrderListener.addOrderClick();
            }
        });
        isFirstSet = true;
    }

    public void updateData(PrerogativeActivityInfo purchaseBean) {
        String activityText = getStrings(purchaseBean.getActivityText());
        int index = activityText.indexOf("'");
        int lastIndex = activityText.lastIndexOf("'");
        if (index != -1 && index != lastIndex) {
            mTvDiscountEdition.setText(getSpannableString(activityText.replaceAll("'", ""), index, lastIndex - 1, 0, "#ff5e6b"));
        } else {
            mTvDiscountEdition.setText(activityText);
        }
        goodList.clear();
        goodList.addAll(purchaseBean.getGoodsList());
        mPurchaseCoverAdapter.notifyDataSetChanged();
        updateGoodInfo(goodList.get(0), 0);
        mIvLeft.setVisibility(goodList.size() > 1 ? View.VISIBLE : View.GONE);
        mIvRight.setVisibility(goodList.size() > 1 ? View.VISIBLE : View.GONE);
    }

    private void updateGoodInfo(GoodsListBean goodsListBean, int currentItem) {
        mTvTitle.setText(getStrings(goodsListBean.getSubTitle() + goodsListBean.getProductName()));
        String priceText = "专享价¥" + getStrings(goodsListBean.getPrice());
        mTvDicountPrice.setText(getSpannableString(priceText, 3, priceText.length(), -1, "#ff5e6b"));
        mTvDicountInfo.setText(getStrings(goodsListBean.getDiscountPrice()));
        mIvLeft.setEnabled(currentItem != 0);
        mIvRight.setEnabled(currentItem!=goodList.size()-1);
    }

    public int getCurrentItem() {
        return mVpGoods.getCurrentItem();
    }

    public void setAddOrderListener(AddOrderListener addOrderListener) {
        this.mAddOrderListener = addOrderListener;
    }

    public interface AddOrderListener {
        void addOrderClick();
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
                params.width = AutoSizeUtils.mm2px(mAppContext, 560);
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
     */
    public boolean isShowing() {
        return imageAlertDialog != null
                && isContextExisted(context) && imageAlertDialog.isShowing();
    }
}
