package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.SpringSaleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTimeAddSeconds;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/3
 * class description:轮播品牌团
 */

public class LoopForeShowTimeAdapter extends RecyclerView.Adapter<LoopForeShowTimeAdapter.LoopAdapterViewHolder> implements View.OnClickListener {
    private final List<SpringSaleBean> saleTimeTopicList;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private Set<CountdownView> viewSet = new HashSet<>();
    private Map<Integer, SpringSaleBean> beanMap = new HashMap<>();
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private ConstantMethod constantMethod;

    public LoopForeShowTimeAdapter(Context context, List<SpringSaleBean> saleTimeTopicList) {
        this.saleTimeTopicList = saleTimeTopicList;
        this.context = context;
        getConstant();
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    public int getItemCount() {
        if (saleTimeTopicList != null && saleTimeTopicList.size() > 0) {
            constantMethod.createSchedule();
            for (int i = 0; i < saleTimeTopicList.size(); i++) {
                SpringSaleBean springSaleBean = saleTimeTopicList.get(i);
                SpringSaleBean oldSpringSaleBean = beanMap.get(i);
                if (oldSpringSaleBean == null || (oldSpringSaleBean != null
                        && !oldSpringSaleBean.getCurrentTime().equals(springSaleBean.getCurrentTime()))) {
                    beanMap.put(i, springSaleBean);
                }
            }
        }
        return saleTimeTopicList != null ? saleTimeTopicList.size() : 0;
    }

    private void refreshData() {
        for (Map.Entry<Integer, SpringSaleBean> entry : beanMap.entrySet()) {
            SpringSaleBean springSaleBean = entry.getValue();
            springSaleBean.setAddSecond(springSaleBean.getAddSecond() + 1);
            beanMap.put(entry.getKey(), springSaleBean);
        }
    }

    private void refreshSchedule() {
        Iterator<CountdownView> iterator = viewSet.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            CountdownView countdownView = iterator.next();
            if (countdownView != null) {
                int position = (int) countdownView.getTag();
                SpringSaleBean springSaleBean = beanMap.get(position);
                setCountTime(springSaleBean, countdownView);
            }
        }
    }

    private void setCountTime(SpringSaleBean springSaleBean, CountdownView cv_countdownTime) {
        if (isTimeStart(springSaleBean)) {
            try {
                //格式化结束时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                Date dateEnd = formatter.parse(springSaleBean.getEndTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(springSaleBean.getCurrentTime())) {
                    dateCurrent = formatter.parse(springSaleBean.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                cv_countdownTime.updateShow(dateEnd.getTime() - dateCurrent.getTime() - springSaleBean.getAddSecond() * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
//            特惠预告
            try {
                //格式化开始时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateStart = formatter.parse(springSaleBean.getStartTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(springSaleBean.getCurrentTime())) {
                    dateCurrent = formatter.parse(springSaleBean.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                cv_countdownTime.updateShow(dateStart.getTime() - dateCurrent.getTime() - springSaleBean.getAddSecond() * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(!ConstantMethod.isEndOrStartTimeAddSeconds(springSaleBean.getCurrentTime()
                ,springSaleBean.getEndTime()
                ,springSaleBean.getAddSecond())){
            cv_countdownTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.setOnCountdownEndListener(null);
                    EventBus.getDefault().post(new EventMessage("onTimeTopic", cv.getTag()));
                }
            });
        }else{
            cv_countdownTime.setOnCountdownEndListener(null);
        }
    }

    private boolean isTimeStart(SpringSaleBean springSaleBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
            Date dateStart = formatter.parse(springSaleBean.getStartTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(springSaleBean.getCurrentTime())) {
                dateCurrent = formatter.parse(springSaleBean.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            if (dateCurrent.getTime() >= dateStart.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (constantMethod != null) {
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    if (saleTimeTopicList != null && saleTimeTopicList.size() > 0) {
//                刷新数据
                        refreshData();
//                刷新倒计时
                        refreshSchedule();
                    }
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        constantMethod.releaseHandlers();
        constantMethod.stopSchedule();
        super.onDetachedFromRecyclerView(recyclerView);
    }

    //    private void setCountDownView(int i, CountdownView countdownView) {
//        sparseArray.put(i, countdownView);
//    }
    private void setCountDownView(CountdownView countdownView) {
        viewSet.add(countdownView);
    }

    @Override
    public LoopAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_promotion_springsale_topic, parent, false);
        LoopAdapterViewHolder loopAdapterViewHolder = new LoopAdapterViewHolder(view);
        ButterKnife.bind(loopAdapterViewHolder, view);
        view.setOnClickListener(this);
        return loopAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(LoopAdapterViewHolder holder, int position) {
        SpringSaleBean springSaleBean = saleTimeTopicList.get(position);
        GlideImageLoaderUtil.loadCenterCrop(context, holder.iv_springSale_topic, springSaleBean.getTopic() != null ? springSaleBean.getTopic().getPicUrl() : springSaleBean.getPicUrl());
        holder.tv_fore_show_topic_name.setText(springSaleBean.getTopic() != null ? springSaleBean.getTopic().getTitle() : springSaleBean.getName());
        holder.tv_foreShow_time_topic_status.setText(isTimeStart(springSaleBean) ? "距结束" : "距开始");
        setCountTime(springSaleBean, holder.ct_show_time_topic);
        holder.ct_show_time_topic.setTag(position);
//        setCountDownView(position, holder.ct_show_time_topic);
        setCountDownView(holder.ct_show_time_topic);
        holder.itemView.setTag(springSaleBean);
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(v);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view);
    }

    public class LoopAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ct_show_time_topic)
        CountdownView ct_show_time_topic;
        @BindView(R.id.iv_springSale_topic)
        ImageView iv_springSale_topic;
        @BindView(R.id.tv_fore_show_topic_name)
        TextView tv_fore_show_topic_name;
        @BindView(R.id.tv_foreShow_time_topic_status)
        TextView tv_foreShow_time_topic_status;

        public LoopAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
