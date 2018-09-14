package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.dominant.activity.TimeBrandDetailsActivity;
import com.amkj.dmsh.homepage.adapter.SpringSaleRecyclerAdapterNew;
import com.amkj.dmsh.homepage.bean.BaseTimeProductTopicBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeForeShowBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeShaftBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeTopicBean;
import com.amkj.dmsh.homepage.bean.TimeShowEntity.TimeShowBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TIME_REFRESH;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/5/2
 * class description:限时特惠
 */
public class SpringSaleFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    //    商品总数
    private List<BaseTimeProductTopicBean> saleTimeTotalList = new ArrayList();
    private int page = 1;
    //    当前时间
    private String currentRecordTime = "";
    private SpringSaleRecyclerAdapterNew springSaleRecyclerAdapter;
    private CustomPopWindow mCustomPopWindow;
    private TimeForeShowEntity timeForeShowEntity;
    private List<TimeShowBean> showTimeList;
    private String searchDateDay;
    private String oldDateDay;
    private String searchDateHour;
    private boolean isShowClearData;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        if (showTimeList == null||showTimeList.size()<1) {
            return;
        }
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        springSaleRecyclerAdapter = new SpringSaleRecyclerAdapterNew(getActivity(), saleTimeTotalList);
        springSaleRecyclerAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return (saleTimeTotalList.get(position).getItemType() == TYPE_0) ? 1 : gridLayoutManager.getSpanCount();
            }
        });
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_product)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        communal_recycler.setAdapter(springSaleRecyclerAdapter);
        springSaleRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseTimeProductTopicBean baseTimeProductTopicBean = (BaseTimeProductTopicBean) view.getTag();
                Intent intent;
                switch (baseTimeProductTopicBean.getItemType()) {
                    case TYPE_0:
                        intent = new Intent();
                        TimeForeShowBean timeForeShowBean = (TimeForeShowBean) baseTimeProductTopicBean;
                        intent.setClass(getActivity(), ShopTimeScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(timeForeShowBean.getId()));
                        startActivity(intent);
                        break;
                    case TYPE_1:
                        intent = new Intent();
                        TimeTopicBean timeTopicBean = (TimeTopicBean) baseTimeProductTopicBean;
                        intent.setClass(getActivity(), TimeBrandDetailsActivity.class);
                        intent.putExtra("brandId", String.valueOf(timeTopicBean.getId()));
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        springSaleRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

//                TimeForeShowEntity.SpringSaleBean springSaleBean = (SpringSaleBean) view.getTag();
//                if (springSaleBean != null) {
//                    switch (view.getId()) {
//                        case R.id.iv_pro_time_warm:
////                            设置提醒 取消提醒 是否是第一次设置
//                            if (userId != 0) {
//                                isFirstRemind(springSaleBean, (ImageView) view);
//                            } else {
//                                getLoginStatus(SpringSaleFragment.this);
//                            }
//                            break;
//                    }
//                }
            }
        });

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
//                刷新时间轴
            EventBus.getDefault().post(new EventMessage(TIME_REFRESH, currentRecordTime));
        });
        springSaleRecyclerAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

            }
        }, communal_recycler);
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
        springSaleRecyclerAdapter.openLoadAnimation(null);
    }

//    private void isFirstRemind(final SpringSaleBean springSaleBean, final ImageView view) {
//        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_WARM;
//        Map<String, Object> params = new HashMap<>();
//        params.put("uid", userId);
//        XUtil.Post(url, params, new MyCallBack<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Gson gson = new Gson();
//                RequestStatus foreShowBean = gson.fromJson(result, RequestStatus.class);
//                if (foreShowBean != null) {
//                    if (foreShowBean.getCode().equals("01")) {
//                        if (foreShowBean.getResult().isHadRemind()) { //已设置过提醒
//                            if (view.isSelected()) {
////                                取消提醒
//                                cancelWarm(springSaleBean.getId(), view);
//                            } else {
////                                设置提醒
//                                setWarm(springSaleBean.getId(), view);
//                            }
//                        } else {
//                            setDefaultWarm();
//                        }
//                    } else if (!foreShowBean.getCode().equals("02")) {
//                        showToast(getActivity(), foreShowBean.getMsg());
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                showToast(getActivity(), R.string.unConnectedNetwork);
//            }
//        });
//    }
//
//    private void setDefaultWarm() {
////        设置提醒
//        View indentPopWindow = LayoutInflater.from(getActivity()).inflate(R.layout.layout_first_time_product_warm, communal_recycler, false);
//        AutoUtils.autoSize(indentPopWindow);
//        PopupWindowView popupWindowView = new PopupWindowView();
//        ButterKnife.bind(popupWindowView, indentPopWindow);
//        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
//                .setView(indentPopWindow)
//                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
//                .setBgDarkAlpha(0.7f) // 控制亮度
//                .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
//        popupWindowView.rp_time_pro_warm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
//                String numberWarm = radioButton.getText().toString().trim();
//                Message message = new Message();
//                message.arg1 = 1;
//                message.obj = numberWarm;
//                handler.sendMessageDelayed(message, 618);
//            }
//        });
//    }
//
//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            String objData = (String) msg.obj;
//            if (msg.arg1 == 1) {
//                mCustomPopWindow.dissmiss();
//                setWarmTime(getNumber(objData));
//            }
//            return false;
//        }
//    });
//
//    private void setWarmTime(String number) {
//        String url = Url.BASE_URL + Url.TIME_WARM_PRO;
//        Map<String, Object> params = new HashMap<>();
//        params.put("m_uid", userId);
//        params.put("longtime", number);
//        XUtil.Post(url, params, new MyCallBack<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Gson gson = new Gson();
//                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
//                if (requestStatus != null && requestStatus.getCode().equals("01")) {
//                    showToast(getActivity(), "已设置产品提醒时间，提前" + requestStatus.getLongtime() + "分钟");
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//            }
//        });
//    }

    @Override
    protected void loadData() {
        if (showTimeList != null && showTimeList.size() > 0) {
            page = 1;
            isShowClearData = true;
            TimeShowBean timeShowBean = showTimeList.get(0);
            searchDateDay = timeShowBean.getDate();
            searchDateHour = timeShowBean.getHourShaft()[0];
            getProductData(timeShowBean);
        } else {
            NetLoadUtils.getQyInstance().showLoadSirEmpty(loadService);
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getProductData(TimeShowBean timeShowBean) {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_TOPIC_SHAFT;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("searchDate", searchDateDay);
        params.put("searchHour", searchDateHour);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId != 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        springSaleRecyclerAdapter.loadMoreComplete();
                        if (isShowClearData && page == 1) {
                            //重新加载数据
                            saleTimeTotalList.clear();
                            isShowClearData = false;
                        }
                        Gson gson = new Gson();
                        timeForeShowEntity = gson.fromJson(result, TimeForeShowEntity.class);
                        if (timeForeShowEntity != null) {
                            if (timeForeShowEntity.getCode().equals(SUCCESS_CODE)) {
                                if (page == 1) {
                                    TimeShaftBean timeShaftBean = new TimeShaftBean();
                                    timeShaftBean.setTimeDayHour(searchDateHour);
                                    timeShaftBean.setItemType(TYPE_2);
//                            日期是否变更
                                    if (!searchDateDay.equals(oldDateDay)) {
                                        timeShaftBean.setTimeDayWeek(timeShowBean.getName());
                                    }
                                    saleTimeTotalList.add(timeShaftBean);
                                }
//                                商品列表
                                if (timeForeShowEntity.getTimeForeShowList() != null
                                        && timeForeShowEntity.getTimeForeShowList().size() > 0) {
                                    saleTimeTotalList.addAll(timeForeShowEntity.getTimeForeShowList());
                                }
//                                品牌团
                                if (timeForeShowEntity.getTimeTopicBean() != null) {
                                    TimeTopicBean timeTopicBean = timeForeShowEntity.getTimeTopicBean();
                                    timeTopicBean.setItemType(TYPE_1);
                                    saleTimeTotalList.add(timeTopicBean);
                                }
                            } else {
                                if (timeForeShowEntity.getCode().equals(EMPTY_CODE)) {
                                    for (int i = 0; i < timeShowBean.getHourShaft().length; i++) {
                                        if (searchDateHour.equals(timeShowBean.getHourShaft()[i])) {
                                            page = 1;
                                            if (i == timeShowBean.getHourShaft().length - 1) {
//                                                更换天数时间轴
//                                                是否是最后一天
                                                for (int j = 0; j < showTimeList.size(); j++) {
                                                    if (timeShowBean.getDate().equals(showTimeList.get(j).getDate())) {
                                                        if (j == showTimeList.size() - 1) {
                                                            springSaleRecyclerAdapter.loadMoreEnd();
                                                        } else {
                                                            TimeShowBean timeShowNewBean = showTimeList.get(++j);
                                                            searchDateHour = timeShowNewBean.getHourShaft()[0];
                                                            oldDateDay = searchDateDay;
                                                            searchDateDay = timeShowNewBean.getDate();
                                                            getProductData(timeShowNewBean);
                                                        }
                                                        break;
                                                    }
                                                }
                                            } else {
//                                                更换小时时间轴
                                                searchDateHour = timeShowBean.getHourShaft()[++i];
                                                oldDateDay = searchDateDay;
                                                getProductData(timeShowBean);
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    springSaleRecyclerAdapter.loadMoreEnd();
                                    showToast(getActivity(), timeForeShowEntity.getMsg());
                                }
                            }
                            springSaleRecyclerAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, saleTimeTotalList, timeForeShowEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        springSaleRecyclerAdapter.loadMoreComplete();
                        showToast(getActivity(), R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, saleTimeTotalList, timeForeShowEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        springSaleRecyclerAdapter.loadMoreComplete();
                        showToast(getActivity(), R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, saleTimeTotalList, timeForeShowEntity);
                    }
                });
    }

    private void cancelWarm(int productId, View view) {
        final ImageView imageView = (ImageView) view;
        String url = Url.BASE_URL + Url.CANCEL_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        imageView.setSelected(false);
                        showToast(getActivity(), "已取消提醒");
                    } else {
                        showToast(getActivity(), status.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(getActivity(), R.string.unConnectedNetwork);
            }
        });
    }

    private void setWarm(int productId, View view) {
        final ImageView imageView = (ImageView) view;
        String url = Url.BASE_URL + Url.ADD_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals("01")) {
                        imageView.setSelected(true);
                        showToast(getActivity(), "已设置提醒");
                    } else {
                        showToast(getActivity(), status.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(getActivity(), R.string.unConnectedNetwork);
            }
        });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("onTime")) {
            loadData();
        }
    }

    @Override
    protected void getReqParams(@NonNull Bundle bundle) {
        super.getReqParams(bundle);
        try {
            showTimeList = bundle.getParcelableArrayList("showTime");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PopupWindowView {
        @BindView(R.id.rp_time_pro_warm)
        RadioGroup rp_time_pro_warm;
    }

    private String getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                return m.group();
        }
        return "3";
    }

    @Override
    public void onDestroyView() {
        new ConstantMethod().releaseHandlers();
        super.onDestroyView();
    }
}
