package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.mine.bean.MineWarmEntity.MineWarmBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;

;

/**
 * Created by atd48 on 2016/10/20.
 * 我的提醒列表
 */
public class ShopTimeMyWarmAdapter extends BaseQuickAdapter<MineWarmBean, BaseViewHolder> {
    private final Context context;
    private final List<MineWarmBean> mineWarmBeanList;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private Map<Integer, MineWarmBean> beanMap = new HashMap<>();
    private ConstantMethod constantMethod;

    public ShopTimeMyWarmAdapter(Context context, List<MineWarmBean> mineWarmBeanList) {
        super(R.layout.adapter_mine_warm, mineWarmBeanList);
        this.context = context;
        this.mineWarmBeanList = mineWarmBeanList;
        getConstant();
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    public int getItemCount() {
        if (mineWarmBeanList != null && mineWarmBeanList.size() > 0) {
            constantMethod.createSchedule();
            for (int i = 0; i < mineWarmBeanList.size(); i++) {
                beanMap.put(i, mineWarmBeanList.get(i));
            }
        }
        return super.getItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (constantMethod != null) {
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    if (mineWarmBeanList != null && mineWarmBeanList.size() > 0) {
//                刷新数据
                        refreshData();
//                刷新倒计时
                        refreshSchedule();
                    }
                }
            });
        }
    }

    private void refreshData() {
        for (Map.Entry<Integer, MineWarmBean> entry : beanMap.entrySet()) {
            MineWarmBean mineWarmBean = entry.getValue();
            mineWarmBean.setAddSecond(mineWarmBean.getAddSecond() + 1);
            beanMap.put(entry.getKey(), mineWarmBean);
        }
    }

    private void refreshSchedule() {
        for (int i = 0; i < sparseArray.size(); i++) {
            CountdownView countdownView = (CountdownView) sparseArray.get(sparseArray.keyAt(i));
            MineWarmBean mineWarmBean = beanMap.get(sparseArray.keyAt(i));
            setCountTime(mineWarmBean,countdownView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        constantMethod.releaseHandlers();
        constantMethod.stopSchedule();
        super.onDetachedFromRecyclerView(recyclerView);
    }
    private void setCountDownView(int i, CountdownView countdownView) {
        sparseArray.put(i,countdownView);
    }

    private void setCountTime(MineWarmBean mineWarmBean, CountdownView cv_countdownTime_red_hours) {
        if (!isTimeStart(mineWarmBean)) {
            try {
                //格式化开始时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateStart = formatter.parse(mineWarmBean.getStart_time());
                Date dateCurrent;
                if (!TextUtils.isEmpty(mineWarmBean.getCurrentTime())) {
                    dateCurrent = formatter.parse(mineWarmBean.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                cv_countdownTime_red_hours.updateShow(dateStart.getTime() - dateCurrent.getTime()-mineWarmBean.getAddSecond()*1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            cv_countdownTime_red_hours.setVisibility(View.GONE);
        }
        if(!isEndOrStartTimeAddSeconds(mineWarmBean.getCurrentTime()
                ,mineWarmBean.getStart_time()
                ,mineWarmBean.getAddSecond())){
            cv_countdownTime_red_hours.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.setOnCountdownEndListener(null);
                    EventBus.getDefault().post(new EventMessage("refreshTimeShop", cv.getTag()));
                }
            });
        }else{
            cv_countdownTime_red_hours.setOnCountdownEndListener(null);
        }
    }

    private boolean isTimeStart(MineWarmBean mineWarmBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
            Date dateStart = formatter.parse(mineWarmBean.getStart_time());
            Date dateCurrent;
            if (!TextUtils.isEmpty(mineWarmBean.getCurrentTime())) {
                dateCurrent = formatter.parse(mineWarmBean.getCurrentTime());
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
    protected void convert(BaseViewHolder helper, MineWarmBean mineWarmBean) {
        TextView tv_mine_warm_set_status = helper.getView(R.id.tv_mine_warm_set_status);
        helper.setGone(R.id.ll_communal_count_time,true);
//        开团状态
        TextView tv_count_time_before_hours = helper.getView(R.id.tv_count_time_before_hours);
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_mine_warm_product), mineWarmBean.getPath());
        switch (mineWarmBean.getStatus()) {
            case 5: //            商品已失效
                tv_mine_warm_set_status.setSelected(false);
                tv_mine_warm_set_status.setText("删除");
            case 2:
//            已开团
                tv_mine_warm_set_status.setSelected(true);
                tv_mine_warm_set_status.setText("去抢购");
                tv_count_time_before_hours.setSelected(true);
                tv_count_time_before_hours.setText("已开团");
                break;
            case 3:
//                已截团
                tv_mine_warm_set_status.setSelected(false);
                tv_mine_warm_set_status.setText("删除");
                break;
            default:
//            正常 即将开始
                tv_mine_warm_set_status.setSelected(false);
                tv_mine_warm_set_status.setText("取消提醒");
                tv_count_time_before_hours.setSelected(false);
                tv_count_time_before_hours.setText("距离活动 开始还有");
                break;
        }
        helper.setText(R.id.tv_mine_warm_product_name, getStrings(mineWarmBean.getTitle()))
                .setText(R.id.tv_mine_warm_product_price, "¥" + mineWarmBean.getPrice())
                .addOnClickListener(R.id.tv_mine_warm_set_status).setTag(R.id.tv_mine_warm_set_status, mineWarmBean);
        CountdownView cv_countdownTime_red_hours = helper.getView(R.id.cv_countdownTime_red_hours);
        DynamicConfig.Builder dynamicDetails = new DynamicConfig.Builder();
        dynamicDetails.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext,28));
        dynamicDetails.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext,28));
        cv_countdownTime_red_hours.dynamicShow(dynamicDetails.build());

        setCountTime(mineWarmBean,cv_countdownTime_red_hours);
        setCountDownView(helper.getAdapterPosition() - getHeaderLayoutCount(),cv_countdownTime_red_hours);
        helper.itemView.setTag(mineWarmBean);
    }
}
