package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.activity.ZeroDetailActivity;
import com.amkj.dmsh.mine.bean.ZeroListEntity.ZeroListBean.CurrentActivityListBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amkj.dmsh.mine.adapter.ZeroProductAdapter.ZERO_CURRENT;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;

/**
 * Created by xiaoxin on 2020/8/11
 * Version:v4.7.0
 * ClassDescription :0元试用商品适配器
 */
public class ZeroProductListAdapter extends BaseQuickAdapter<CurrentActivityListBean, BaseViewHolder> {
    private final Context context;
    private CountDownTimer mCountDownTimer;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private String currentTime;

    public ZeroProductListAdapter(Context context, @Nullable List<CurrentActivityListBean> data) {
        super(R.layout.item_zero_product_list, data);
        this.context = context;
        creatCountDownTimer();
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

    @Override
    protected void convert(BaseViewHolder helper, CurrentActivityListBean item) {
        if (item == null) return;
        RecyclerView RecyclerView = helper.getView(R.id.recyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(context));
        ZeroProductAdapter zeroProductAdapter = new ZeroProductAdapter(context, item.getGoodsList(), ZERO_CURRENT);
        RecyclerView.setAdapter(zeroProductAdapter);
        zeroProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String activityId = (String) view.getTag();
                Intent intent = new Intent(context, ZeroDetailActivity.class);
                intent.putExtra("activityId", activityId);
                context.startActivity(intent);
            }
        });
        TextView tvEndTime = helper.getView(R.id.tv_time);
        setCountTime(tvEndTime, item.getEndTime());
        sparseArray.put(helper.getAdapterPosition() - getHeaderLayoutCount(), tvEndTime);
        tvEndTime.setTag(item.getEndTime());
    }

    private void setCountTime(TextView tvTime, String endTime) {
        tvTime.setText(!isEndOrStartTime(currentTime, endTime) ?
                "距截止试用报名： " + getCoutDownTime(getTimeDifference(currentTime, endTime), true) : "");
    }
}
