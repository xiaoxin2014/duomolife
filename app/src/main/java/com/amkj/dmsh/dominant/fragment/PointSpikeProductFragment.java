package com.amkj.dmsh.dominant.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.adapter.PointSpikeProductAdapter;
import com.amkj.dmsh.dominant.bean.PointSpikeProductEntity;
import com.amkj.dmsh.dominant.bean.PointSpikeProductEntity.TimeAxisProductListBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTime;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REQUEST_NOTIFICATION_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_POINT_SPIKE_PRODUCT;
import static com.amkj.dmsh.constant.Url.Q_POINT_SPIKE_PRODUCT_STATUS;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/2
 * version 3.3.0
 * class description:整点秒杀
 */
public class PointSpikeProductFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communalRecycler;
    private PointSpikeProductAdapter pointSpikeProductAdapter;
    private List<TimeAxisProductListBean> timeAxisProductListBeans = new ArrayList<>();
    private String pointSpikeId;
    private String pointSpikeEndTime;
    private String pointSpikeStartTime;
    private PointSpikeHelper pointSpikeHelper;
    private ConstantMethod constantMethod;
    private WeakReference<Activity> activityWeakReference;
    private AlertDialogHelper notificationAlertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler;
    }

    @Override
    protected void initViews() {
        if (TextUtils.isEmpty(pointSpikeId)) {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            return;
        }
        activityWeakReference = new WeakReference<Activity>(getActivity());
        communalRecycler.setLayoutManager(new LinearLayoutManager(activityWeakReference.get()));
        pointSpikeProductAdapter = new PointSpikeProductAdapter(activityWeakReference.get(), timeAxisProductListBeans);
        pointSpikeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TimeAxisProductListBean timeAxisProductListBean = (TimeAxisProductListBean) view.getTag();
                if (timeAxisProductListBean != null) {
                    Intent intent = new Intent(activityWeakReference.get(), ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(timeAxisProductListBean.getProductId()));
                    startActivity(intent);
                }
            }
        });
        pointSpikeProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TimeAxisProductListBean timeAxisProductListBean = (TimeAxisProductListBean) view.getTag();
                if (timeAxisProductListBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_point_spike_done:
                            if (timeAxisProductListBean.getStatusCode() == 0) {
                                if (userId > 0) {
                                    if (timeAxisProductListBean.getIsNotice() == 0 && !getDeviceAppNotificationStatus(activityWeakReference.get())) {
                                        if (notificationAlertDialogHelper == null) {
                                            notificationAlertDialogHelper = new AlertDialogHelper(activityWeakReference.get());
                                            notificationAlertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                                @Override
                                                public void confirm() {
                                                    // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", activityWeakReference.get().getPackageName(), null);
                                                    intent.setData(uri);
                                                    startActivityForResult(intent, REQUEST_NOTIFICATION_STATUS);
                                                    notificationAlertDialogHelper.dismiss();
                                                }

                                                @Override
                                                public void cancel() {
                                                    notificationAlertDialogHelper.dismiss();
                                                }
                                            });
                                        }
                                        notificationAlertDialogHelper.setTitle("是否打开推送通知")
                                                .setTitleGravity(Gravity.CENTER)
                                                .setMsg("打开推送通知\n活动开始前会立即通知你哦~")
                                                .setMsgTextGravity(Gravity.CENTER)
                                                .setConfirmText("好的")
                                                .setCancelText("不需要")
                                                .setCancelTextColor(activityWeakReference.get().getResources().getColor(R.color.text_gray_hint_n));
                                        notificationAlertDialogHelper.show();
                                        return;
                                    }
                                    setPointProductStatus(timeAxisProductListBean);
                                    timeAxisProductListBean.setIsNotice(timeAxisProductListBean.getIsNotice() == 1 ? 0 : 1);
                                    CheckedTextView checkedTextView = (CheckedTextView) view;
                                    //        先判断抢购状态
                                    String proStatus;
                                    if (timeAxisProductListBean.getStatusCode() == 0) {
                                        checkedTextView.setChecked(timeAxisProductListBean.getIsNotice() == 0);
                                        proStatus = timeAxisProductListBean.getIsNotice() == 0?"提醒我":"已设置";
                                        checkedTextView.setChecked(timeAxisProductListBean.getIsNotice() == 1);
                                    } else if (timeAxisProductListBean.getStatusCode() == 1) {
                                        checkedTextView.setSelected(true);
                                        proStatus = "马上抢";
                                    } else {
                                        proStatus = "已过期";
                                        checkedTextView.setChecked(true);
                                    }
                                    checkedTextView.setText(proStatus);
                                } else {
                                    getLoginStatus(PointSpikeProductFragment.this);
                                }
                            } else {
                                Intent intent = new Intent(activityWeakReference.get(), ShopScrollDetailsActivity.class);
                                intent.putExtra("productId", String.valueOf(timeAxisProductListBean.getProductId()));
                                startActivity(intent);
                            }
                            break;
                    }

                }
            }
        });
        pointSpikeProductAdapter.setEnableLoadMore(false);
        View pointSpikeTimeView = LayoutInflater.from(activityWeakReference.get()).inflate(R.layout.layout_point_spike_time, communalRecycler, false);
        pointSpikeHelper = new PointSpikeHelper();
        communalRecycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        ButterKnife.bind(pointSpikeHelper, pointSpikeTimeView);
        pointSpikeProductAdapter.addHeaderView(pointSpikeTimeView);
        communalRecycler.setAdapter(pointSpikeProductAdapter);
    }

    @Override
    protected void loadData() {
        Map<String, Object> params = new HashMap<>();
        params.put("timeaxis_id", pointSpikeId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(activityWeakReference.get(), Q_POINT_SPIKE_PRODUCT
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        timeAxisProductListBeans.clear();
                        PointSpikeProductEntity pointSpikeProductEntity = new Gson().fromJson(result, PointSpikeProductEntity.class);
                        if (pointSpikeProductEntity != null) {
                            if (pointSpikeProductEntity.getCode().equals(SUCCESS_CODE)) {
                                int productStatus = 0;
                                String timeStatus = "距离开始还有";
//                                是否已开始
                                if (isEndOrStartTime(pointSpikeProductEntity.getSystemTime(), pointSpikeStartTime)) {
                                    productStatus = 1;
                                    timeStatus = "距离结束还有";
//                                是否已结束
                                    if (isEndOrStartTime(pointSpikeProductEntity.getSystemTime(), pointSpikeEndTime)) {
                                        productStatus = 2;
                                        timeStatus = "已结束";
                                    }
                                }
                                pointSpikeHelper.tv_show_communal_time_status.setText(timeStatus);
                                setPointSpikeTime(pointSpikeProductEntity);
                                for (TimeAxisProductListBean timeAxisProductListBean : pointSpikeProductEntity.getTimeAxisProductList()) {
                                    timeAxisProductListBean.setStatusCode(productStatus);
                                }
                                timeAxisProductListBeans.addAll(pointSpikeProductEntity.getTimeAxisProductList());
                            } else if (!EMPTY_CODE.equals(pointSpikeProductEntity.getCode())) {
                                showToast(pointSpikeProductEntity.getMsg());
                            }
                            pointSpikeProductAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, timeAxisProductListBeans, pointSpikeProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        if (timeAxisProductListBeans.size() < 1) {
                            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
                        }
                    }
                });
    }

    /**
     * 设置整点秒杀
     *
     * @param pointSpikeProductEntity
     */
    private void setPointSpikeTime(PointSpikeProductEntity pointSpikeProductEntity) {
        //格式化结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date currentTime;
        Date startTime;
        Date endTime;
        try {
            if (TextUtils.isEmpty(pointSpikeProductEntity.getSystemTime())) {
                currentTime = new Date();
            } else {
                currentTime = formatter.parse(pointSpikeProductEntity.getSystemTime());
            }
            startTime = formatter.parse(pointSpikeStartTime);
            endTime = formatter.parse(pointSpikeEndTime);
            long milliseconds = startTime.getTime() > currentTime.getTime()
                    ? startTime.getTime() - currentTime.getTime() : currentTime.getTime() < endTime.getTime() ? endTime.getTime() - currentTime.getTime() : 0;
            DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
            dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
            dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
            if (milliseconds < 1000 * 60 * 60 * 99) {
                dynamic.setConvertDaysToHours(true);
                dynamic.setShowDay(false);
            }
            pointSpikeHelper.ct_time_communal_show_bg.dynamicShow(dynamic.build());
            final long[] seconds = {0};
            if (milliseconds > 0) {
                pointSpikeHelper.ct_time_communal_show_bg.setVisibility(View.VISIBLE);
                if (constantMethod == null) {
                    constantMethod = new ConstantMethod();
                }
                constantMethod.createSchedule();
                constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                    @Override
                    public void refreshTime() {
                        seconds[0] += 1;
                        if (milliseconds - (seconds[0] * 1000) < 1) {
                            constantMethod.stopSchedule();
                        }
                        pointSpikeHelper.ct_time_communal_show_bg.updateShow(milliseconds - seconds[0] * 1000);
                    }
                });
                pointSpikeHelper.ct_time_communal_show_bg.updateShow(milliseconds - seconds[0] * 1000);
            } else {
                pointSpikeHelper.ct_time_communal_show_bg.setVisibility(View.GONE);
            }
            pointSpikeHelper.ct_time_communal_show_bg.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    pointSpikeHelper.ct_time_communal_show_bg.stop();
                    pointSpikeHelper.ct_time_communal_show_bg.setOnCountdownEndListener(null);
                    loadData();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置整点秒商品状态
     *
     * @param axisProductListBean
     */
    private void setPointProductStatus(TimeAxisProductListBean axisProductListBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("timeaxis_id", pointSpikeId);
        params.put("product_id", axisProductListBean.getProductId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(activityWeakReference.get(), Q_POINT_SPIKE_PRODUCT_STATUS
                , params, null);
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        pointSpikeId = (String) bundle.get("pointSpikeId");
        pointSpikeStartTime = (String) bundle.get("pointSpikeStartTime");
        pointSpikeEndTime = (String) bundle.get("pointSpikeEndTime");
    }

    /**
     * 整点秒杀
     */
    public class PointSpikeHelper {
        @BindView(R.id.tv_show_communal_time_status)
        TextView tv_show_communal_time_status;
        @BindView(R.id.ct_time_communal_show_bg)
        CountdownView ct_time_communal_show_bg;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                loadData();
            }
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (constantMethod != null) {
            constantMethod.stopSchedule();
        }
        pointSpikeHelper.ct_time_communal_show_bg.stop();
    }
}
