package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity;
import com.amkj.dmsh.homepage.bean.TimeForeShowEntity.SpringSaleBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.timeFormatSwitch;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TIME_REFRESH;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

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
    private List<SpringSaleBean> saleTimeTotalList = new ArrayList();
    //    限时特惠商品排序
    private List<SpringSaleBean> saleTimeTotalSortList = new ArrayList();
    private int page = 1;
    private int scrollY;
    //    当前时间
    private String currentRecordTime = "";
    private SpringSaleRecyclerAdapterNew springSaleRecyclerAdapter;
    private CustomPopWindow mCustomPopWindow;
    private long delayTime = 5000;
    private String showTime;
    private TimeForeShowEntity timeForeShowEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
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
                if (position >= 0) {
                    Intent intent = new Intent();
                    TimeForeShowEntity.SpringSaleBean springSaleBean = (TimeForeShowEntity.SpringSaleBean) view.getTag();
                    if (springSaleBean != null) {
                        if (springSaleBean.getTopic() != null) {
                            intent.setClass(getActivity(), TimeBrandDetailsActivity.class);
                            intent.putExtra("brandId", String.valueOf(springSaleBean.getTopic().getId()));
                        } else {
                            intent.setClass(getActivity(), ShopTimeScrollDetailsActivity.class);
                            intent.putExtra("productId", String.valueOf(springSaleBean.getId()));
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });
        springSaleRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TimeForeShowEntity.SpringSaleBean springSaleBean = (SpringSaleBean) view.getTag();
                if (springSaleBean != null) {
                    switch (view.getId()) {
                        case R.id.iv_pro_time_warm:
//                            设置提醒 取消提醒 是否是第一次设置
                            if (userId != 0) {
                                isFirstRemind(springSaleBean, (ImageView) view);
                            } else {
                                getLoginStatus(SpringSaleFragment.this);
                            }
                            break;
                    }
                }
            }
        });

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
//                刷新时间轴
            EventBus.getDefault().post(new EventMessage(TIME_REFRESH, currentRecordTime));
        });
        springSaleRecyclerAdapter.setEnableLoadMore(false);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
            }
        });
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

    private void isFirstRemind(final SpringSaleBean springSaleBean, final ImageView view) {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus foreShowBean = gson.fromJson(result, RequestStatus.class);
                if (foreShowBean != null) {
                    if (foreShowBean.getCode().equals("01")) {
                        if (foreShowBean.getResult().isHadRemind()) { //已设置过提醒
                            if (view.isSelected()) {
//                                取消提醒
                                cancelWarm(springSaleBean.getId(), view);
                            } else {
//                                设置提醒
                                setWarm(springSaleBean.getId(), view);
                            }
                        } else {
                            setDefaultWarm();
                        }
                    } else if (!foreShowBean.getCode().equals("02")) {
                        showToast(getActivity(), foreShowBean.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(getActivity(), R.string.unConnectedNetwork);
            }
        });
    }

    private void setDefaultWarm() {
//        设置提醒
        View indentPopWindow = LayoutInflater.from(getActivity()).inflate(R.layout.layout_first_time_product_warm, communal_recycler, false);
        AutoUtils.autoSize(indentPopWindow);
        PopupWindowView popupWindowView = new PopupWindowView();
        ButterKnife.bind(popupWindowView, indentPopWindow);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(indentPopWindow)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
        popupWindowView.rp_time_pro_warm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String numberWarm = radioButton.getText().toString().trim();
                Message message = new Message();
                message.arg1 = 1;
                message.obj = numberWarm;
                handler.sendMessageDelayed(message, 618);
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String objData = (String) msg.obj;
            if (msg.arg1 == 1) {
                mCustomPopWindow.dissmiss();
                setWarmTime(getNumber(objData));
            }
            return false;
        }
    });

    private void setWarmTime(String number) {
        String url = Url.BASE_URL + Url.TIME_WARM_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("m_uid", userId);
        params.put("longtime", number);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null && requestStatus.getCode().equals("01")) {
                    showToast(getActivity(), "已设置产品提醒时间，提前" + requestStatus.getLongtime() + "分钟");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(showTime)) {
            page = 1;
            getProductData();
        }else{
            NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getProductData() {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_ALL_SHAFT;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("searchDate", showTime);
        if (userId != 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                springSaleRecyclerAdapter.loadMoreComplete();
                if (page == 1) {
                    //重新加载数据
                    saleTimeTotalList.clear();
                    currentRecordTime = "";
                    saleTimeTotalSortList.clear();
                }
                Gson gson = new Gson();
                timeForeShowEntity = gson.fromJson(result, TimeForeShowEntity.class);
                if (timeForeShowEntity != null) {
                    if (timeForeShowEntity.getCode().equals(SUCCESS_CODE)) {
                        saleTimeTotalSortList.addAll(timeForeShowEntity.getSpringSales());
                        //        获取时间排序范围
                        Collections.sort(saleTimeTotalSortList, new Comparator<SpringSaleBean>() {
                            @Override
                            public int compare(SpringSaleBean first, SpringSaleBean second) {
                                long t1 = timeFormatSwitch(first.getStartTime()).getTime();
                                long t2 = timeFormatSwitch(second.getStartTime()).getTime();
                                return Long.compare(t1, t2);
                            }
                        });
                        for (int i = 0; i < saleTimeTotalSortList.size(); i++) {
                            SpringSaleBean springSaleBean = saleTimeTotalSortList.get(i);
                            if (!TextUtils.isEmpty(timeForeShowEntity.getCurrentTime())) {
                                springSaleBean.setCurrentTime(timeForeShowEntity.getCurrentTime());
                            }
                            if (i == 0 && page == 1 && TextUtils.isEmpty(currentRecordTime)
                                    || !currentRecordTime.equals(springSaleBean.getStartTime())) {
                                currentRecordTime = springSaleBean.getStartTime();
                                SpringSaleBean showTime = new SpringSaleBean();
                                showTime.setStartTime(springSaleBean.getStartTime());
                                showTime.setCurrentTime(springSaleBean.getCurrentTime());
                                showTime.setEndTime(springSaleBean.getEndTime());
                                showTime.setPrevisionFlag(springSaleBean.getPrevisionFlag());
                                showTime.setItemType(TYPE_1);
                                saleTimeTotalList.add(showTime);
                            }
                            saleTimeTotalList.add(springSaleBean);
                        }
                    } else {
                        smart_communal_refresh.finishRefresh();
                        springSaleRecyclerAdapter.loadMoreComplete();
                        if (!timeForeShowEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(getActivity(), timeForeShowEntity.getMsg());
                        }
                    }
                    springSaleRecyclerAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,saleTimeTotalList,timeForeShowEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                springSaleRecyclerAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,saleTimeTotalList,timeForeShowEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                springSaleRecyclerAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,saleTimeTotalList,timeForeShowEntity);
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
            page = 1;
            getProductData();
        }
    }

    @Override
    protected void getReqParams(@NonNull Bundle bundle) {
        super.getReqParams(bundle);
        try {
            showTime = (String) bundle.get("show_time");
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
