package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity.MainOrderBean;
import com.amkj.dmsh.shopdetails.fragment.DoMoIndentNewFragment;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.MainButtonView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_TYPE;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;


/**
 * Created by xiaoxin on 2020/3/19
 * Version:v4.4.3
 * ClassDescription :订单列表适配器重构
 */
public class DirectIndentListAdapter extends BaseQuickAdapter<MainOrderBean, BaseViewHolder> implements LifecycleObserver {
    private AppCompatActivity context;
    private DoMoIndentNewFragment fragment;
    private LayoutInflater layoutInflater;
    private List<MainOrderBean> orderList;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private Map<Integer, MainOrderBean> beanMap = new HashMap<>();
    private ConstantMethod constantMethod;


    public DirectIndentListAdapter(DoMoIndentNewFragment fragment, Activity activity, List<MainOrderBean> orderList) {
        super(R.layout.layout_communal_recycler_wrap, orderList);
        this.context = ((AppCompatActivity) activity);
        this.fragment = fragment;
        context.getLifecycle().addObserver(this);
        this.orderList = orderList;
        layoutInflater = LayoutInflater.from(activity);
        getConstant();
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }

    @Override
    public int getItemCount() {
        if (orderList != null && orderList.size() > 0) {
            constantMethod.createSchedule();
            for (int i = 0; i < orderList.size(); i++) {
                MainOrderBean OrderListNewBean = orderList.get(i);
                if (0 <= OrderListNewBean.getStatus() && OrderListNewBean.getStatus() < 10) {
                    beanMap.put(i, OrderListNewBean);
                }
            }
        }
        return super.getItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (constantMethod != null) {
            constantMethod.setRefreshTimeListener(() -> {
                if (orderList != null && orderList.size() > 0) {
                    //刷新数据
                    refreshData();
                    //刷新倒计时
                    refreshSchedule();
                }
            });
        }
    }

    private void refreshData() {
        for (Map.Entry<Integer, MainOrderBean> entry : beanMap.entrySet()) {
            MainOrderBean OrderListNewBean = entry.getValue();
            OrderListNewBean.setSecond(OrderListNewBean.getSecond() - 1);
            beanMap.put(entry.getKey(), OrderListNewBean);
        }
    }

    private void refreshSchedule() {
        for (int i = 0; i < sparseArray.size(); i++) {
            IntentHView intentHView = (IntentHView) sparseArray.get(sparseArray.keyAt(i));
            setCountTime(intentHView, beanMap.get(sparseArray.keyAt(i)));
        }
    }


    /**
     * @param helper
     * @param mainOrderBean
     */
    @Override
    protected void convert(BaseViewHolder helper, final MainOrderBean mainOrderBean) {
        if (mainOrderBean == null) return;
        List<OrderProductNewBean> orderProductList = mainOrderBean.getOrderProductList();
        RecyclerView communal_recycler_wrap = helper.getView(R.id.communal_recycler_wrap);
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
        if (communal_recycler_wrap.getTag() == null) {
            communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_gray_f_one_px)
                    .create());
        }
        DirectProductListAdapter directProductListAdapter = new DirectProductListAdapter(context, orderProductList, INDENT_TYPE);
        communal_recycler_wrap.setAdapter(directProductListAdapter);
        directProductListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_product:
                case R.id.ll_present:
                    fragment.setCurrentOrderNo(mainOrderBean.getOrderNo());
                    fragment.setCurrentPosition(helper.getLayoutPosition() - getHeaderLayoutCount());
                    Intent intent = new Intent(context, DirectExchangeDetailsActivity.class);
                    intent.putExtra("orderNo", mainOrderBean.getOrderNo());
                    context.startActivity(intent);
                    break;
            }
        });

        //初始化主订单头部
        View headerView = layoutInflater.inflate(R.layout.layout_indent_item_header, communal_recycler_wrap, false);
        IntentHView intentHView = new IntentHView();
        ButterKnife.bind(intentHView, headerView);
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        intentHView.cv_countdownTime_direct.dynamicShow(dynamic.build());
        directProductListAdapter.addHeaderView(headerView);
        //主订单状态
        intentHView.tv_indent_type_show.setText(getStrings(mainOrderBean.getStatusText()));
        //初始化主订单底部
        View footView = layoutInflater.inflate(R.layout.layout_indent_item_new_foot, communal_recycler_wrap, false);
        IntentFView intentFView = new IntentFView();
        ButterKnife.bind(intentFView, footView);
        directProductListAdapter.addFooterView(footView);
        //主订单商品实付
        intentFView.tv_intent_count_price.setText(ConstantMethod.getStringsFormat(context, R.string.pay_amount, mainOrderBean.getPayAmount(), mainOrderBean.isPostage() ? "包邮" : "不包邮"));
        //主订单按钮操作
        intentFView.main_button_view.updateView(context, mainOrderBean, fragment.getClass().getSimpleName(), (key, value) -> {
            fragment.setCurrentOrderNo(mainOrderBean.getOrderNo());
            fragment.setCurrentPosition(helper.getLayoutPosition() - getHeaderLayoutCount());
        });
        //主订单物流信息
        List<String> logistics = mainOrderBean.getLogisticsStrList();
        if (logistics != null && logistics.size() > 0) {
            intentFView.tv_express_info.setText(getStrings(logistics.get(0)));
        }
        intentFView.ll_express_info.setVisibility(logistics != null && logistics.size() > 0 ? View.VISIBLE : View.GONE);
        intentFView.ll_express_info.setOnClickListener(v -> {
            Intent intent = new Intent(context, DirectLogisticsDetailsActivity.class);
            intent.putExtra("orderNo", mainOrderBean.getOrderNo());
            context.startActivity(intent);
        });

        intentFView.tv_msg.setVisibility(!TextUtils.isEmpty(mainOrderBean.getMsg()) ? View.VISIBLE : View.GONE);
        intentFView.tv_msg.setText(getStrings(mainOrderBean.getMsg()));

        //待支付倒计时
        if (0 <= mainOrderBean.getStatus() && mainOrderBean.getStatus() < 10) {
            //            展示倒计时
            if (!TextUtils.isEmpty(mainOrderBean.getCurrentTime())
                    && isEndOrStartTimeAddSeconds(mainOrderBean.getCreateTime()
                    , mainOrderBean.getCurrentTime()
                    , mainOrderBean.getSecond())) {
                intentHView.tv_direct_indent_create_time.setVisibility(View.GONE);
                intentHView.ll_direct_count_time.setVisibility(View.VISIBLE);
                sparseArray.put(helper.getAdapterPosition() - getHeaderLayoutCount(), intentHView);
                setCountTime(intentHView, mainOrderBean);
            } else {
                intentHView.tv_direct_indent_create_time.setVisibility(View.VISIBLE);
                intentHView.ll_direct_count_time.setVisibility(View.GONE);
            }
        }
        intentHView.tv_direct_indent_create_time.setText(getStrings(mainOrderBean.getCreateTime()));
        helper.itemView.setTag(mainOrderBean);
    }

    private void setCountTime(final IntentHView holder, MainOrderBean orderListNewBean) {
        try {
            //格式化结束时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateCreate = formatter.parse(orderListNewBean.getCreateTime());
            long overTime = orderListNewBean.getSecond() * 1000;
            Date dateCurrent;
            if (!TextUtils.isEmpty(orderListNewBean.getCurrentTime())) {
                dateCurrent = formatter.parse(orderListNewBean.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            holder.cv_countdownTime_direct.updateShow(dateCreate.getTime() + overTime - dateCurrent.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (isEndOrStartTimeAddSeconds(orderListNewBean.getCreateTime()
                , orderListNewBean.getCurrentTime()
                , orderListNewBean.getSecond())) {
            holder.cv_countdownTime_direct.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.setOnCountdownEndListener(null);
                }
            });
        } else {
            holder.cv_countdownTime_direct.setVisibility(View.GONE);
            holder.tv_indent_type_show.setText("已关闭");
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (constantMethod != null) {
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
    }

    class IntentHView {
        //        订单状态
        @BindView(R.id.tv_integ_indent_time)
        TextView tv_indent_type_show;
        //        创建时间
        @BindView(R.id.tv_direct_indent_create_time)
        TextView tv_direct_indent_create_time;
        //        倒计时布局
        @BindView(R.id.ll_direct_count_time)
        LinearLayout ll_direct_count_time;
        //        倒计时
        @BindView(R.id.tv_count_time_before)
        TextView tv_count_time_before;
        //        倒计时前
        @BindView(R.id.cv_countdownTime_direct)
        CountdownView cv_countdownTime_direct;
        //        倒计时后
        @BindView(R.id.tv_count_time_after)
        TextView tv_count_time_after;
    }

    class IntentFView {
        @BindView(R.id.tv_intent_count_price)
        TextView tv_intent_count_price;
        @BindView(R.id.main_button_view)
        MainButtonView main_button_view;
        @BindView(R.id.tv_express_info)
        TextView tv_express_info;
        @BindView(R.id.tv_msg)
        TextView tv_msg;
        @BindView(R.id.ll_express_info)
        LinearLayout ll_express_info;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        context.getLifecycle().removeObserver(this);
    }
}
