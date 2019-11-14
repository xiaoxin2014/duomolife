package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.bean.CouponZoneEntity.CouponZoneBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_COVER;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_ONE_COLUMN;
import static com.amkj.dmsh.constant.ConstantVariable.COUPON_THREE_COLUMN;

/**
 * Created by xiaoxin on 2019/11/10
 * Version:v4.3.6
 * ClassDescription :优惠券专区适配器
 */
public class CouponZoneAdapter extends BaseMultiItemQuickAdapter<CouponZoneBean, BaseViewHolder> {

    private boolean mIsStart = false;
    private Activity mContext;

    public CouponZoneAdapter(Activity context, List<CouponZoneBean> data) {
        super(data);
        mContext = context;
        addItemType(COUPON_COVER, R.layout.item_coupon_cover);
        addItemType(COUPON_ONE_COLUMN, R.layout.item_coupon_one_column);
        addItemType(COUPON_THREE_COLUMN, R.layout.item_coupon_three_column);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponZoneBean item) {
        if (item == null) return;
        if (item.getItemType() == ConstantVariable.COUPON_COVER) {
            GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_coupon_cover), item.getCover());
        } else {
            helper.setText(R.id.tv_amount, getSpannableString("¥" + item.getCouponAmount(), 0, 1, 0.4f, ""))
                    .setText(R.id.tv_desc, getStrings(item.getCouponDesc()))
                    .setText(R.id.tv_edition, getStrings(item.getUseDesc()))
                    .setText(R.id.tv_name, getStrings(item.getTitle()));
            helper.itemView.setTag(item);
        }

        helper.itemView.setEnabled(mIsStart && item.isHave());
        if (item.getItemType() == COUPON_THREE_COLUMN) {
            ImageView ivGet = helper.getView(R.id.iv_get);
            ivGet.setImageResource(getImgSource(item));
        } else if (item.getItemType() == COUPON_ONE_COLUMN) {
            helper.itemView.setBackgroundResource(getImgSourceBg(item));
        }

    }


    private int getImgSource(CouponZoneBean item) {
        //待开始
        if (!mIsStart) {
            return R.drawable.coupon_wait;
        } else {
            //已抢光
            if (!item.isHave()) {
                return R.drawable.coupon_empty;
            } else if (item.isAlreadyGet()) {
                //已领取
                return R.drawable.coupon_already_get;
            } else {
                //立即领取
                return R.drawable.coupon_get_now;
            }
        }
    }

    private int getImgSourceBg(CouponZoneBean item) {
        //待开始
        if (!mIsStart) {
            return R.drawable.coupon_wait_bg;
        } else {
            //已抢光
            if (!item.isHave()) {
                return R.drawable.coupon_empty_bg;
            } else if (item.isAlreadyGet()) {
                //已领取
                return R.drawable.coupon_already_get_bg;
            } else {
                //立即领取
                return R.drawable.coupon_get_now_bg;
            }
        }
    }

    public void setStart(boolean start) {
        mIsStart = start;
    }
}
