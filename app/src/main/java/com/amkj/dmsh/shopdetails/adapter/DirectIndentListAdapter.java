package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.MainButtonView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_TYPE;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;


/**
 * Created by xiaoxin on 2020/3/19
 * Version:v4.5.0
 * ClassDescription :订单列表适配器重构
 */
public class DirectIndentListAdapter extends BaseQuickAdapter<MainOrderBean, BaseViewHolder> {
    private AppCompatActivity context;
    private DoMoIndentNewFragment fragment;
    private LayoutInflater layoutInflater;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private CountDownTimer mCountDownTimer;


    public DirectIndentListAdapter(DoMoIndentNewFragment fragment, Activity activity, List<MainOrderBean> orderList) {
        super(R.layout.layout_communal_recycler_wrap, orderList);
        this.context = ((AppCompatActivity) activity);
        this.fragment = fragment;
        layoutInflater = LayoutInflater.from(activity);
        CreatCountDownTimer();
    }

    //创建定时任务
    private void CreatCountDownTimer() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(context) {
                @Override
                public void onTick(long millisUntilFinished) {
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
            IntentHView intentHView = (IntentHView) sparseArray.get(sparseArray.keyAt(i));
            if (intentHView != null) {
                MainOrderBean mainOrderBean = intentHView.getTag();
                if (mainOrderBean != null && 0 <= mainOrderBean.getStatus() && mainOrderBean.getStatus() < 10) {
                    mainOrderBean.setSecond(mainOrderBean.getSecond() - 1);//刷新数据
                    setCountTime(intentHView, mainOrderBean);
                }
            }
        }
    }


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
        directProductListAdapter.addHeaderView(headerView);
        //主订单状态
        intentHView.tv_indent_type_show.setText(getStrings(mainOrderBean.getStatusText()));
        //初始化主订单底部
        View footView = layoutInflater.inflate(R.layout.layout_indent_item_new_foot, communal_recycler_wrap, false);
        IntentFView intentFView = new IntentFView();
        ButterKnife.bind(intentFView, footView);
        directProductListAdapter.addFooterView(footView);
        //主订单商品实付
        if (!TextUtils.isEmpty(mainOrderBean.getDeposit())) {
            intentFView.tv_intent_deposit_price.setVisibility(View.VISIBLE);
            intentFView.tv_intent_deposit_price.setText("订金" + (mainOrderBean.getDepositStatus() == 1 ? "已支付：¥" : "待支付：¥") + mainOrderBean.getDeposit());
            intentFView.tv_intent_count_price.setText("尾款" + (!TextUtils.isEmpty(mainOrderBean.getPayTime()) ? "已支付：¥" : "待支付：¥") + mainOrderBean.getPayAmount());
        } else {
            intentFView.tv_intent_deposit_price.setVisibility(View.GONE);
            intentFView.tv_intent_count_price.setText(ConstantMethod.getStringsFormat(context, R.string.pay_amount, mainOrderBean.getPayAmount(), mainOrderBean.isPostage() ? "包邮" : "不包邮"));
        }
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

        //待支付并且倒计时未结束时
        if (0 <= mainOrderBean.getStatus() && mainOrderBean.getStatus() < 10
                && isEndOrStartTimeAddSeconds(mainOrderBean.getCreateTime(), mainOrderBean.getCurrentTime(), mainOrderBean.getSecond())) {
            //判断是否是尾款待支付，并且尾款时间未开始
            if (mainOrderBean.getDepositStatus() == 1 && !isEndOrStartTime(mainOrderBean.getCurrentTime(), mainOrderBean.getOrderPayStartTime())) {
                intentHView.tv_direct_indent_create_time.setVisibility(View.VISIBLE);
                intentHView.tv_countdownTime_direct.setVisibility(View.GONE);
                intentHView.tv_direct_indent_create_time.setText(getStrings(mainOrderBean.getOrderPayStartTime()) + " 开始支付尾款");
            } else {
                intentHView.tv_direct_indent_create_time.setVisibility(View.GONE);
                intentHView.tv_countdownTime_direct.setVisibility(View.VISIBLE);
                intentHView.setTag(mainOrderBean);//绑定数据
                sparseArray.put(helper.getAdapterPosition() - getHeaderLayoutCount(), intentHView);
                //初始化倒计时控件
                setCountTime(intentHView, mainOrderBean);
            }
        } else {
            intentHView.tv_direct_indent_create_time.setVisibility(View.VISIBLE);
            intentHView.tv_countdownTime_direct.setVisibility(View.GONE);
            intentHView.tv_direct_indent_create_time.setText(getStrings(mainOrderBean.getCreateTime()));
        }
        helper.itemView.setTag(mainOrderBean);
    }

    private void setCountTime(final IntentHView intentHView, @NonNull MainOrderBean mainOrderBean) {
        //待支付倒计时
        String currentTime = mainOrderBean.getCurrentTime();
        String createTime = mainOrderBean.getCreateTime();
        long second = mainOrderBean.getSecond();
        if (isEndOrStartTimeAddSeconds(createTime, currentTime, second)) {
            String coutDownTime = getCoutDownTime(second * 1000 - getTimeDifference(currentTime, createTime), false);
            String text = "剩 " + coutDownTime + " 自动关闭";
            intentHView.tv_countdownTime_direct.setText(getSpannableString(text, 2, text.length() - 5, 1.1f, "#0a88fa", true));
        } else {
            //隐藏倒计时显示创建时间
            intentHView.tv_direct_indent_create_time.setVisibility(View.VISIBLE);
            intentHView.tv_direct_indent_create_time.setText(getStrings(mainOrderBean.getCreateTime()));
            intentHView.tv_countdownTime_direct.setVisibility(View.GONE);
            intentHView.tv_indent_type_show.setText("已关闭");
        }
    }

    static class IntentHView {
        //        订单状态
        @BindView(R.id.tv_integ_indent_time)
        TextView tv_indent_type_show;
        //        创建时间
        @BindView(R.id.tv_direct_indent_create_time)
        TextView tv_direct_indent_create_time;
        @BindView(R.id.tv_countdownTime_direct)
        TextView tv_countdownTime_direct;
        private MainOrderBean mMainOrderBean;

        private void setTag(MainOrderBean mainOrderBean) {
            mMainOrderBean = mainOrderBean;
        }

        public MainOrderBean getTag() {
            return mMainOrderBean;
        }
    }

    static class IntentFView {
        @BindView(R.id.tv_intent_count_price)
        TextView tv_intent_count_price;
        @BindView(R.id.tv_intent_deposit_price)
        TextView tv_intent_deposit_price;
        @BindView(R.id.main_button_view)
        MainButtonView main_button_view;
        @BindView(R.id.tv_express_info)
        TextView tv_express_info;
        @BindView(R.id.tv_msg)
        TextView tv_msg;
        @BindView(R.id.ll_express_info)
        LinearLayout ll_express_info;
    }
}
