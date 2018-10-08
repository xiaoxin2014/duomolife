package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.bean.QualityGroupEntity.QualityGroupBean;
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
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTimeAddSeconds;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/6
 * class description:拼团列表
 */

public class QualityGroupShopAdapter extends BaseQuickAdapter<QualityGroupBean, BaseViewHolder> {
    private final Context context;
    private final List<QualityGroupBean> qualityGroupBeanList;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private Map<Integer, QualityGroupBean> beanMap = new HashMap<>();
    private ConstantMethod constantMethod;

    public QualityGroupShopAdapter(Context context, List<QualityGroupBean> qualityGroupBeanList) {
        super(R.layout.adapter_layout_ql_gp_sp, qualityGroupBeanList);
        this.qualityGroupBeanList = qualityGroupBeanList;
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
        if (qualityGroupBeanList != null && qualityGroupBeanList.size() > 0) {
            constantMethod.createSchedule();
            for (int i = 0; i < qualityGroupBeanList.size(); i++) {
                QualityGroupBean qualityGroupBean = qualityGroupBeanList.get(i);
                beanMap.put(i, qualityGroupBean);
            }
        }
        return super.getItemCount();
    }


    private void refreshData() {
        for (Map.Entry<Integer, QualityGroupBean> entry : beanMap.entrySet()) {
            QualityGroupBean qualityGroupBean = entry.getValue();
            qualityGroupBean.setAddSecond(qualityGroupBean.getAddSecond() + 1);
            beanMap.put(entry.getKey(), qualityGroupBean);
        }
    }

    private void refreshSchedule() {
        for (int i = 0; i < sparseArray.size(); i++) {
            CountdownView countdownView = (CountdownView) sparseArray.get(sparseArray.keyAt(i));
            QualityGroupBean groupShopJoinBean = beanMap.get(sparseArray.keyAt(i));
           setCountTime(groupShopJoinBean,countdownView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (constantMethod != null) {
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    if (qualityGroupBeanList != null && qualityGroupBeanList.size() > 0) {
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

    private void setCountTime(QualityGroupBean qualityGroupBean, CountdownView cv_countdownTime) {
        if (isTimeStart(qualityGroupBean)) {
            try {
                //格式化结束时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                Date dateEnd = formatter.parse(qualityGroupBean.getGpEndTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(qualityGroupBean.getCurrentTime())) {
                    dateCurrent = formatter.parse(qualityGroupBean.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                cv_countdownTime.updateShow(dateEnd.getTime() - dateCurrent.getTime()-qualityGroupBean.getAddSecond()*1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                //格式化开始时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date dateStart = formatter.parse(qualityGroupBean.getGpStartTime());
                Date dateCurrent;
                if (!TextUtils.isEmpty(qualityGroupBean.getCurrentTime())) {
                    dateCurrent = formatter.parse(qualityGroupBean.getCurrentTime());
                } else {
                    dateCurrent = new Date();
                }
                cv_countdownTime.updateShow(dateStart.getTime() - dateCurrent.getTime()-qualityGroupBean.getAddSecond()*1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(!isEndOrStartTimeAddSeconds(qualityGroupBean.getCurrentTime()
                ,qualityGroupBean.getGpEndTime()
                ,qualityGroupBean.getAddSecond())){
            cv_countdownTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.setOnCountdownEndListener(null);
                    EventBus.getDefault().post(new EventMessage("refreshGroupShop", cv.getTag()));
                }
            });
        }else{
            cv_countdownTime.setOnCountdownEndListener(null);
        }
    }

    private boolean isTimeStart(QualityGroupBean qualityGroupBean) {
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
            Date dateStart = formatter.parse(qualityGroupBean.getGpStartTime());
            Date dateCurrent;
            if (!TextUtils.isEmpty(qualityGroupBean.getCurrentTime())) {
                dateCurrent = formatter.parse(qualityGroupBean.getCurrentTime());
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
    protected void convert(BaseViewHolder helper, QualityGroupBean qualityGroupBean) {
//        封面图
        TextView tv_gp_sp_count = helper.getView(R.id.tv_gp_sp_count);
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_communal_img_bg), qualityGroupBean.getCoverImage());
        helper.setText(R.id.tv_communal_time_status, !isTimeStart(qualityGroupBean) ? "距开始" : "距结束")
                .setText(R.id.tv_gp_sp_name, getStrings(qualityGroupBean.getName()))
                .setText(R.id.tv_gp_sp_per_count, qualityGroupBean.getGpType())//拼团人数
                .setText(R.id.tv_gp_sp_per_price, "￥" + qualityGroupBean.getGpPrice())
                .setText(R.id.tv_gp_sp_nor_price, "单买价￥" + qualityGroupBean.getPrice())
                .setText(R.id.tv_gp_sp_open_count, "" + qualityGroupBean.getGpCount())
                .setText(R.id.tv_gp_sp_inventory, qualityGroupBean.getGpProductQuantity() + "")
                .setText(R.id.tv_gp_sp_count, qualityGroupBean.getGpProductQuantity() > 0 ? "去开团" : "已抢光")
                .setGone(R.id.tv_ql_gp_sp_new, qualityGroupBean.getRange() == 1 );
        tv_gp_sp_count.setSelected(qualityGroupBean.getGpProductQuantity() > 0);
        CountdownView ct_communal_time_details = helper.getView(R.id.ct_communal_time_details);
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext,22));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext,22));
        ct_communal_time_details.dynamicShow(dynamic.build());
        setCountTime(qualityGroupBean,ct_communal_time_details);
        setCountDownView(helper.getAdapterPosition()-getHeaderLayoutCount(),ct_communal_time_details);
        helper.itemView.setTag(qualityGroupBean);
    }

    private void setCountDownView(int i, CountdownView countdownView) {
        sparseArray.put(i,countdownView);
    }
}
