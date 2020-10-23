package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.time.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.dominant.activity.TimeBrandDetailsActivity;
import com.amkj.dmsh.homepage.adapter.SpringSaleRecyclerAdapterNew;
import com.amkj.dmsh.homepage.bean.BaseTimeProductTopicBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeForeShowBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeShaftBean;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.TimeTopicBean;
import com.amkj.dmsh.homepage.bean.TimeShaftRecordBean;
import com.amkj.dmsh.homepage.bean.TimeShowShaftEntity.TimeShowShaftBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TIME_REFRESH;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_3;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_4;
import static com.amkj.dmsh.constant.Url.TIME_SHOW_PRODUCT_TOPIC_SHAFT;
import static com.amkj.dmsh.constant.Url.TIME_SHOW_PRO_TOP_PRODUCT;
import static com.amkj.dmsh.constant.Url.TIME_SHOW_TAOBAO_PRODUCT;



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
    @BindView(R.id.tv_time_product_shaft)
    TextView tv_time_product_shaft;
    //    商品总数
    private List<BaseTimeProductTopicBean> saleTimeTotalList = new ArrayList();
    private int page = 1;
    private SpringSaleRecyclerAdapterNew springSaleRecyclerAdapter;
    private TimeForeShowEntity timeForeShowEntity;
    private List<TimeShowShaftBean> showTimeList;
    private String searchDateDay;
    private String oldDateDay;
    private String searchDateHour;
    private boolean isShowClearData;
    private TimeShowShaftBean timeShowBean;
    //    总共滚动距离
    private int scrollY = 0;
    //    下拉触发距离
    private int dropDownScroll = 0;
    //    相同时间是否第一次加入时间轴
    private boolean isSameDayFirst;
    //    记录时间轴
    private List<TimeShaftRecordBean> timeShaftList = new ArrayList<>();
    private View timeShaftView;
    private PopupShaftViewHelper popupShaftViewHelper;
    private TimeShaftRecordAdapter timeShaftRecordAdapter;
    private CustomPopWindow customPopWindow;
    private PopupWindow popupWindow;
    private boolean isClickSelect;
    private int[] location = new int[2];
    private RemoveExistUtils removeExistUtils;
    private String position;

    @Override
    protected int getContentView() {
        return R.layout.fragment_spring_sale_product;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        springSaleRecyclerAdapter = new SpringSaleRecyclerAdapterNew(getActivity(), saleTimeTotalList);
        springSaleRecyclerAdapter.setSpanSizeLookup((gridLayoutManager, position) -> (saleTimeTotalList.get(position).getItemType() == TYPE_0) ? 1 : gridLayoutManager.getSpanCount());
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_white).create());
        communal_recycler.setAdapter(springSaleRecyclerAdapter);
        communal_recycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                addItemClick(view);
            }
        });
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                scrollY = 0;
//                刷新时间轴
                EventBus.getDefault().post(new EventMessage(TIME_REFRESH, "timeShaft"));
            }
        });
        if (showTimeList != null && showTimeList.size() > 0) {
            springSaleRecyclerAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    page++;
                    getProductData();
                }
            }, communal_recycler);
        } else {
            springSaleRecyclerAdapter.setEnableLoadMore(false);
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{0, 0, AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25)
                , AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25), 0, 0});
        gradientDrawable.setColor(getResources().getColor(R.color.bg_trans_80_ffs_pink));
        tv_time_product_shaft.setBackground(gradientDrawable);
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (dy < 0) {
                    dropDownScroll += Math.abs(dy);
                } else if (dy > 8) {
//                    重新计算
                    dropDownScroll = 0;
                }
                if (timeShaftList.size() > 2 && dropDownScroll > 300 && tv_time_product_shaft.getVisibility() == View.GONE) {
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.down_in_visiable);
                    animation.setDuration(500);
                    tv_time_product_shaft.startAnimation(animation);
                    tv_time_product_shaft.setVisibility(View.VISIBLE);
                } else if (dy > 0 && tv_time_product_shaft.getVisibility() == View.VISIBLE) {
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.down_out_hide);
                    animation.setDuration(500);
                    tv_time_product_shaft.startAnimation(animation);
                    tv_time_product_shaft.setVisibility(View.GONE);
                }
                if (!isClickSelect) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager == null) {
                        return;
                    }
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();//可见范围内的第一项的位置
                    if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < saleTimeTotalList.size()) {
                        BaseTimeProductTopicBean baseTimeProductTopicBean = saleTimeTotalList.get(firstVisibleItemPosition);
                        if (baseTimeProductTopicBean.getItemType() == TYPE_2) {
                            TimeShaftBean timeShaftBean = (TimeShaftBean) baseTimeProductTopicBean;
                            setTimeShaftSelect(timeShaftBean.getTimeDayWeek());
                        }
                    }
                } else {
                    isClickSelect = false;
                }
            }
        });
        timeShaftView = getLayoutInflater().inflate(R.layout.layout_communal_recycler_wrap_wrap, null);
        popupShaftViewHelper = new PopupShaftViewHelper();
        ButterKnife.bind(popupShaftViewHelper, timeShaftView);
        popupShaftViewHelper.initPopView();
        springSaleRecyclerAdapter.openLoadAnimation(null);
        removeExistUtils = new RemoveExistUtils();

    }

    private void addItemClick(View view) {
        BaseTimeProductTopicBean baseTimeProductTopicBean = (BaseTimeProductTopicBean) view.getTag();
        if (baseTimeProductTopicBean != null) {
            Intent intent;
            switch (baseTimeProductTopicBean.getItemType()) {
                case TYPE_0:
                    intent = new Intent();
                    TimeForeShowBean timeForeShowBean = (TimeForeShowBean) baseTimeProductTopicBean;
                    intent.setClass(getActivity(), ShopTimeScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(timeForeShowBean.getId()));
                    intent.putExtra("isTaobao", timeForeShowBean.getIsTaoBao());
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
    }

    private void setTimeShaftSelect(String weekName) {
        for (TimeShaftRecordBean timeShaftRecordBean : timeShaftList) {
            if (getStrings(weekName).equals(timeShaftRecordBean.getWeekName())) {
                timeShaftRecordBean.setSelect(true);
                tv_time_product_shaft.setText(getStrings(timeShaftRecordBean.getWeekName()));
            } else {
                timeShaftRecordBean.setSelect(false);
            }
        }
        timeShaftRecordAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {
        if (showTimeList != null && showTimeList.size() > 0) {
            page = 1;
            oldDateDay = "";
            isShowClearData = true;
            isSameDayFirst = false;
            timeShowBean = showTimeList.get(0);
            searchDateDay = timeShowBean.getDate();
            searchDateHour = timeShowBean.getHourShaft().get(0);
            getProductData();
        } else {
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            saleTimeTotalList.clear();
            getTopRecommendData();
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected String getEmptyText() {
        return "暂时没有商品哦";
    }

    private void getProductData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("searchDate", searchDateDay);
        params.put("searchHour", searchDateHour);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId != 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), TIME_SHOW_PRODUCT_TOPIC_SHAFT
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        springSaleRecyclerAdapter.loadMoreComplete();
                        if (isShowClearData && page == 1) {
                            //重新加载数据
                            saleTimeTotalList.clear();
                            isShowClearData = false;
                            timeShaftList.clear();
                            removeExistUtils.clearData();
                        }

                        timeForeShowEntity = GsonUtils.fromJson(result, TimeForeShowEntity.class);
                        if (timeForeShowEntity != null) {
                            if (timeForeShowEntity.getCode().equals(SUCCESS_CODE)) {
                                springSaleRecyclerAdapter.setEnableLoadMore(true);
                                if (page == 1) {
                                    TimeShaftBean timeShaftBean = new TimeShaftBean();
                                    timeShaftBean.setTimeDayHour(searchDateHour);
                                    timeShaftBean.setItemType(TYPE_2);
//                            日期是否变更
                                    if (!searchDateDay.equals(oldDateDay)
                                            || (searchDateDay.equals(oldDateDay) && !isSameDayFirst)) {
                                        isSameDayFirst = true;
                                        timeShaftBean.setTimeDayWeek(timeShowBean.getName());
                                        TimeShaftRecordBean timeShaftRecordBean = new TimeShaftRecordBean();
                                        timeShaftRecordBean.setScrollPosition(saleTimeTotalList.size() > 0 ? saleTimeTotalList.size() - 1 : 0);
                                        timeShaftRecordBean.setWeekName(getStrings(timeShowBean.getName()));
                                        timeShaftList.add(timeShaftRecordBean);
                                        if (timeShaftList.size() == 1) {
                                            tv_time_product_shaft.setText(getStrings(timeShaftList.get(0).getWeekName()));
                                        }
                                        timeShaftRecordAdapter.notifyDataSetChanged();
                                    }
                                    saleTimeTotalList.add(timeShaftBean);
                                }
//                                商品列表
                                if (timeForeShowEntity.getTimeForeShowList() != null
                                        && timeForeShowEntity.getTimeForeShowList().size() > 0) {
                                    saleTimeTotalList.addAll(removeExistUtils.removeExistList(timeForeShowEntity.getTimeForeShowList()));
                                }
//                                品牌团
                                if (timeForeShowEntity.getTimeTopicBean() != null && timeForeShowEntity.getTimeTopicBean().getId() > 0) {
                                    TimeTopicBean timeTopicBean = timeForeShowEntity.getTimeTopicBean();
                                    timeTopicBean.setItemType(TYPE_1);
                                    saleTimeTotalList.add(timeTopicBean);
                                }
                            } else {
                                springSaleRecyclerAdapter.setEnableLoadMore(false);
                                if (timeForeShowEntity.getCode().equals(EMPTY_CODE)) {
                                    int currentPosition = timeShowBean.getHourShaft().lastIndexOf(searchDateHour);
                                    if (currentPosition != -1) {
                                        page = 1;
                                        if (currentPosition == timeShowBean.getHourShaft().size() - 1) {
//                                                更换天数时间轴
//                                                是否是最后一天
                                            int currentDayIndex = showTimeList.indexOf(timeShowBean);
                                            if (currentDayIndex != -1) {
                                                if (currentDayIndex == showTimeList.size() - 1) {
//                                                            最后一天
                                                    getTopRecommendData();
                                                } else {
                                                    timeShowBean = showTimeList.get(currentDayIndex + 1);
                                                    searchDateHour = timeShowBean.getHourShaft().get(0);
                                                    oldDateDay = searchDateDay;
                                                    searchDateDay = timeShowBean.getDate();
//                                                            更换天数 重置同一天只展示一次week
                                                    isSameDayFirst = false;
                                                    getProductData();
                                                }
                                            }
                                        } else {
//                                                更换小时时间轴
                                            searchDateHour = timeShowBean.getHourShaft().get(currentPosition + 1);
                                            oldDateDay = searchDateDay;
                                            getProductData();
                                        }
                                    }
                                } else {
                                    springSaleRecyclerAdapter.loadMoreEnd();
                                    showToast(timeForeShowEntity.getMsg());
                                }
                            }
                            springSaleRecyclerAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, saleTimeTotalList, timeForeShowEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        springSaleRecyclerAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, saleTimeTotalList, timeForeShowEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.unConnectedNetwork);
                    }
                });
    }

    /**
     * 获取团品推荐
     */
    private void getTopRecommendData() {
        Map<String, Object> params = new HashMap<>();
        params.put("recommendType", "top");
        if (userId != 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), TIME_SHOW_PRO_TOP_PRODUCT
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        springSaleRecyclerAdapter.loadMoreComplete();

                        timeForeShowEntity = GsonUtils.fromJson(result, TimeForeShowEntity.class);
                        if (timeForeShowEntity != null) {
                            if (timeForeShowEntity.getCode().equals(SUCCESS_CODE)) {
                                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                                if (timeForeShowEntity.getTimeForeShowList() != null
                                        && timeForeShowEntity.getTimeForeShowList().size() > 0) {
                                    BaseTimeProductTopicBean baseTimeProductTopicBean = new BaseTimeProductTopicBean();
                                    baseTimeProductTopicBean.setItemType(TYPE_3);
                                    saleTimeTotalList.add(baseTimeProductTopicBean);
                                    saleTimeTotalList.addAll(timeForeShowEntity.getTimeForeShowList());
                                }
                                springSaleRecyclerAdapter.notifyDataSetChanged();
                            }
                            springSaleRecyclerAdapter.loadMoreEnd();
                        }

                        //获取淘宝关联商品数据
                        if ("0".equals(position)) {
                            getTaoBaoData();
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        springSaleRecyclerAdapter.loadMoreEnd(true);
                    }
                });
    }

    /**
     * 获取淘宝商品
     */
    private void getTaoBaoData() {
        Map<String, Object> params = new HashMap<>();
        if (userId != 0) {
            params.put("uid", userId);
        }
        params.put("currentPage", 1);
        params.put("showCount", TOTAL_COUNT_FORTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), TIME_SHOW_TAOBAO_PRODUCT
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        springSaleRecyclerAdapter.loadMoreComplete();

                        timeForeShowEntity = GsonUtils.fromJson(result, TimeForeShowEntity.class);
                        if (timeForeShowEntity != null) {
                            if (timeForeShowEntity.getCode().equals(SUCCESS_CODE)) {
                                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                                List<TimeForeShowBean> timeForeShowList = timeForeShowEntity.getTimeForeShowList();
                                if (timeForeShowList != null
                                        && timeForeShowList.size() > 0) {
                                    BaseTimeProductTopicBean baseTimeProductTopicBean = new BaseTimeProductTopicBean();
                                    baseTimeProductTopicBean.setItemType(TYPE_4);
                                    saleTimeTotalList.add(baseTimeProductTopicBean);
                                    for (TimeForeShowBean timeForeShowBean : timeForeShowList) {
                                        timeForeShowBean.setTaoBao("1");//设置淘你所爱商品标志
                                    }
                                    saleTimeTotalList.addAll(timeForeShowList);
                                }
                                springSaleRecyclerAdapter.notifyDataSetChanged();
                            }
                            springSaleRecyclerAdapter.loadMoreEnd();
                        }

                    }

                    @Override
                    public void onNotNetOrException() {
                        springSaleRecyclerAdapter.loadMoreEnd(true);
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
            position = bundle.getString("position", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PopupWindowView {
        @BindView(R.id.rp_time_pro_warm)
        RadioGroup rp_time_pro_warm;
    }

    @OnClick(value = R.id.tv_time_product_shaft)
    public void clickSkipShaft(View view) {
        if (timeShaftList.size() > 1) {
            view.setVisibility(View.GONE);
            tv_time_product_shaft.getLocationOnScreen(location);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                if (customPopWindow == null) {
                    customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                            .setView(timeShaftView)
                            .setFocusable(true)
                            .setOutsideTouchable(false)
                            .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    view.setVisibility(View.VISIBLE);
                                }
                            })
                            .create();
                }
                customPopWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, location[1]);
            } else {
                if (popupWindow == null) {
                    popupWindow = new PopupWindow();
                    popupWindow.setContentView(timeShaftView);
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            view.setVisibility(View.VISIBLE);
                        }
                    });
                }
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, location[1]);
            }
        } else {
            setScrollPosition(0);
        }
    }

    class PopupShaftViewHelper {
        @BindView(R.id.communal_recycler_wrap_wrap)
        RecyclerView communal_recycler_wrap_wrap;

        void initPopView() {
            communal_recycler_wrap_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadii(new float[]{0, 0, AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25)
                    , AutoSizeUtils.mm2px(mAppContext, 25), AutoSizeUtils.mm2px(mAppContext, 25), 0, 0});
            gradientDrawable.setColor(getResources().getColor(R.color.bg_trans_80_ffs_pink));
            communal_recycler_wrap_wrap.setBackground(gradientDrawable);
            timeShaftRecordAdapter = new TimeShaftRecordAdapter(timeShaftList);
            communal_recycler_wrap_wrap.setAdapter(timeShaftRecordAdapter);
            timeShaftRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    TimeShaftRecordBean timeShaftRecordBean = (TimeShaftRecordBean) view.getTag();
                    if (timeShaftRecordBean != null && communal_recycler.getChildCount() > 0) {
                        setPopWindowDismiss();
                        tv_time_product_shaft.setVisibility(View.VISIBLE);
                        tv_time_product_shaft.setText(timeShaftRecordBean.getWeekName());
                        isClickSelect = true;
                        setScrollPosition(timeShaftRecordBean.getScrollPosition());
                        setTimeShaftSelect(timeShaftRecordBean.getWeekName());
                    }
                }
            });
        }
    }

    private void setScrollPosition(int scrollPosition) {
        GridLayoutManager layoutManager = (GridLayoutManager) communal_recycler.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(scrollPosition, 0);
    }

    private class TimeShaftRecordAdapter extends BaseQuickAdapter<TimeShaftRecordBean, BaseViewHolder> {
        public TimeShaftRecordAdapter(List<TimeShaftRecordBean> timeShaftList) {
            super(R.layout.adapter_tv_time_shaft_record, timeShaftList);
        }

        @Override
        protected void convert(BaseViewHolder helper, TimeShaftRecordBean timeShaftRecordBean) {
            TextView textView = helper.getView(R.id.tv_spring_time_week_tag);
            textView.setSelected(timeShaftRecordBean.isSelect());
            textView.setText(getStrings(timeShaftRecordBean.getWeekName()));
            helper.itemView.setTag(timeShaftRecordBean);
        }
    }

    private void setPopWindowDismiss() {
        if (customPopWindow != null) {
            customPopWindow.dissmiss();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
