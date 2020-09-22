package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.activity.ZeroActivityDetailActivity;
import com.amkj.dmsh.mine.bean.MyZeroApplyEntity.MyZeroApplyBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.TimeUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;

/**
 * Created by xiaoxin on 2020/8/17
 * Version:v4.7.0
 * ClassDescription :我的0元试用申请列表
 */
public class MyZeroApplyAdapter extends BaseQuickAdapter<MyZeroApplyBean, BaseViewHolder> {
    private Context context;
    private String status;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private String currentTime;
    private CountDownTimer mCountDownTimer;


    public MyZeroApplyAdapter(Context context, @Nullable List<MyZeroApplyBean> data, String status) {
        super(R.layout.item_zero_apply, data);
        this.context = context;
        this.status = status;
        if ("0".equals(status)) {
            creatCountDownTimer();
        }
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }


    //创建定时任务
    private void creatCountDownTimer() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(context) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //刷新当前时间
                    setCurrentTime(TimeUtils.getMilliSecondDate(TimeUtils.getDateMilliSecond(currentTime) + 1000));
                    //刷新倒计时
                    refreshSchedule();
                }

                @Override
                public void onFinish() {

                }
            };
            mCountDownTimer.setMillisInFuture(3600 * 24 * 30 * 1000L);
        }
        mCountDownTimer.start();
    }

    private void refreshSchedule() {
        for (int i = 0; i < sparseArray.size(); i++) {
            TextView tvTime = (TextView) sparseArray.get(sparseArray.keyAt(i));
            if (tvTime != null) {
                String endTime = (String) tvTime.getTag();
                if (!TextUtils.isEmpty(endTime)) {
                    setCountTime(tvTime, endTime);
                }
            }
        }
    }

    private void setCountTime(TextView tvTime, String endTime) {
        tvTime.setText(!isEndOrStartTime(currentTime, endTime) ?
                "剩余" + getCoutDownTime(getTimeDifference(currentTime, endTime), false) : "");
    }

    @Override
    protected void convert(BaseViewHolder helper, MyZeroApplyBean item) {
        if (item == null) return;
        TextView tvMarketPrice = helper.getView(R.id.tv_market_price);
        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvMarketPrice.getPaint().setAntiAlias(true);
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_pic), item.getProductImg());
        helper.setText(R.id.tv_apply_time, getStringsFormat(context, R.string.zero_apply_time, item.getApplyTime()))
                .setText(R.id.tv_status, getStatus(status))
                .setText(R.id.tv_price, getStringsChNPrice(context, item.getPrice()))
                .setText(R.id.tv_market_price, getStringsChNPrice(context, item.getMarketPrice()))
                .setText(R.id.tv_msg, getStrings(item.getMsgX()))
                .setText(R.id.tv_button, getButtonText(item.getStatus()))
                .setGone(R.id.tv_button, !TextUtils.isEmpty(item.getStatus()))
                .setText(R.id.tv_name, getStrings(item.getProductName()))
                .setGone(R.id.ll_status, !"0".equals(status))//申请中不显示底部按钮
                .setGone(R.id.tv_end_time, "0".equals(status))//只有申请中才需要显示倒计时
                .setGone(R.id.tv_detail, !TextUtils.isEmpty(item.getOrderId()))
                .addOnClickListener(R.id.tv_button).setTag(R.id.tv_button, item)
                .addOnClickListener(R.id.tv_detail).setTag(R.id.tv_detail, item);
        helper.getView(R.id.rl_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZeroActivityDetailActivity.class);
                intent.putExtra("activityId", item.getActivityId());
                context.startActivity(intent);
            }
        });
        //只有申请中才需要显示倒计时
        TextView tvEndTime = helper.getView(R.id.tv_end_time);
        if ("0".equals(status)) {
            setCountTime(tvEndTime, item.getEndTime());
            sparseArray.put(helper.getAdapterPosition() - getHeaderLayoutCount(), tvEndTime);
            tvEndTime.setTag(item.getEndTime());
        }
        helper.itemView.setTag(item);
    }

    private String getStatus(String status) {
        if ("0".equals(status)) {
            return "已申请";
        } else if ("1".equals(status)) {
            return "中选";
        } else if ("2".equals(status)) {
            return "未中选";
        } else {
            return "---";
        }
    }

    //status：1:去领取，2，提交 3，修改报告，4，查看报告
    private String getButtonText(String status) {
        if ("1".equals(status)) {
            return "去领取";
        } else if ("2".equals(status)) {
            return "提交";
        } else if ("3".equals(status)) {
            return "修改报告";
        } else if ("4".equals(status)) {
            return "查看报告";
        } else {
            return "---";
        }
    }
}
