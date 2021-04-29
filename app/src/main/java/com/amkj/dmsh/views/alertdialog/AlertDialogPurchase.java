package com.amkj.dmsh.views.alertdialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.shopdetails.adapter.PurchaseCoverAdapter;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo;
import com.amkj.dmsh.shopdetails.bean.IndentWriteEntity.IndentWriteBean.PrerogativeActivityInfo.GoodsListBean;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by xiaoxin on 2019/9/30
 * Version:v4.3.0
 * ClassDescription :加价购活动弹窗
 */
public class AlertDialogPurchase extends BaseAlertDialog {

    @BindView(R.id.tv_discount_edition)
    TextView mTvDiscountEdition;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.vp_goods)
    ViewPager mVpGoods;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_dicount_price)
    TextView mTvDicountPrice;
    @BindView(R.id.tv_discount_info)
    TextView mTvDiscountInfo;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    private AddOrderListener mAddOrderListener;
    private PurchaseCoverAdapter mPurchaseCoverAdapter;
    private List<GoodsListBean> goodList = new ArrayList<>();

    public AlertDialogPurchase(Context context) {
        super(context);
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
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_alert_dialog_purchase;
    }


    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 560);
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
        mIvRight.setVisibility(goodList.size() > 1 ? View.VISIBLE : View.GONE);
    }

    private void updateGoodInfo(GoodsListBean goodsListBean, int currentItem) {
        mTvTitle.setText(getStrings(goodsListBean.getSubTitle() + goodsListBean.getProductName()));
        String priceText = "专享价¥" + getStrings(goodsListBean.getPrice());
        mTvDicountPrice.setText(getSpannableString(priceText, 3, priceText.length(), -1, "#ff5e6b"));
        mTvDiscountInfo.setText(getStrings(goodsListBean.getDiscountPrice()));
        mIvLeft.setVisibility(currentItem != 0 ? View.VISIBLE : View.GONE);
        mIvRight.setVisibility(currentItem != goodList.size() - 1 ? View.VISIBLE : View.GONE);
    }

    public int getCurrentItem() {
        return mVpGoods.getCurrentItem();
    }

    public void setAddOrderListener(AddOrderListener addOrderListener) {
        this.mAddOrderListener = addOrderListener;
    }

    @OnClick({R.id.iv_left, R.id.iv_right, R.id.tv_cancle, R.id.tv_add, R.id.ll_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                int currentItem = mVpGoods.getCurrentItem();
                if (currentItem - 1 >= 0) {
                    mVpGoods.setCurrentItem(currentItem - 1);
                }
                break;
            case R.id.iv_right:
                int currentItemRight = mVpGoods.getCurrentItem();
                if (currentItemRight + 1 <= goodList.size()) {
                    mVpGoods.setCurrentItem(currentItemRight + 1);
                }
                break;
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.tv_add:
                if (mAddOrderListener != null) {
                    mAddOrderListener.addOrderClick();
                }
                break;
            case R.id.ll_content:
                if (goodList.size() > 0) {
                    skipProductUrl(context, 1, goodList.get(Math.min(getCurrentItem(), goodList.size() - 1)).getProductId());
                }
                break;
        }
    }

    public interface AddOrderListener {
        void addOrderClick();
    }
}
