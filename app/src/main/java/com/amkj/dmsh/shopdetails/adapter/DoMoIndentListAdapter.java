package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.find.activity.IndentScoreListActivity;
import com.amkj.dmsh.find.activity.JoinTopicActivity;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity;
import com.amkj.dmsh.mine.bean.CartProductInfoBean;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTimeAddSeconds;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.INVITE_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.REMIND_DELIVERY;
import static com.amkj.dmsh.shopdetails.activity.DoMoIndentAllActivity.INDENT_TYPE;

;


/**
 * Created by atd48 on 2016/8/23.
 *
 * @author Liuguipeng
 * 订单列表
 */
public class DoMoIndentListAdapter extends BaseQuickAdapter<OrderListBean, DoMoIndentListAdapter.IndentListViewHolder> implements View.OnClickListener {
    private final Activity context;
    private final LayoutInflater layoutInflater;
    private final List<OrderListBean> orderList;
    private OnClickViewListener listener;
    private SparseArray<Object> sparseArray = new SparseArray<>();
    private Map<Integer, OrderListBean> beanMap = new HashMap<>();
    private ConstantMethod constantMethod;

    public DoMoIndentListAdapter(Activity context, List<OrderListBean> orderList) {
        super(R.layout.layout_communal_recycler_wrap, orderList);
        this.context = context;
        this.orderList = orderList;
        layoutInflater = LayoutInflater.from(context);
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
                OrderListBean orderListBean = orderList.get(i);
                if (0 <= orderListBean.getStatus() && orderListBean.getStatus() < 10) {
                    beanMap.put(i, orderListBean);
                }
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
                    if (orderList != null && orderList.size() > 0) {
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
        for (Map.Entry<Integer, OrderListBean> entry : beanMap.entrySet()) {
            OrderListBean orderListBean = entry.getValue();
            orderListBean.setSecond(orderListBean.getSecond() - 1);
            beanMap.put(entry.getKey(), orderListBean);
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
     * @param orderListBean
     */
    @Override
    protected void convert(IndentListViewHolder helper, final OrderListBean orderListBean) {
        //        订单件数
        int totalCount = 0;
        for (int i = 0; i < orderListBean.getGoods().size(); i++) {
            totalCount += orderListBean.getGoods().get(i).getCount();
        }
        DirectProductListAdapter directProductListAdapter = new DirectProductListAdapter(context, orderListBean.getGoods(), INDENT_TYPE);
        View headerView = layoutInflater.inflate(R.layout.layout_indent_item_header, helper.communal_recycler_wrap, false);
        View footView = layoutInflater.inflate(R.layout.layout_indent_item_foot, helper.communal_recycler_wrap, false);
        IntentHView intentHView = new IntentHView();
        ButterKnife.bind(intentHView, headerView);
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        intentHView.cv_countdownTime_direct.dynamicShow(dynamic.build());
        IntentFView intentFView = new IntentFView();
        ButterKnife.bind(intentFView, footView);
        directProductListAdapter.addHeaderView(headerView);
        directProductListAdapter.addFooterView(footView);
        helper.communal_recycler_wrap.setAdapter(directProductListAdapter);
        String countPrice = String.format(context.getString(R.string.price_count), totalCount, orderListBean.getAmount());
        int start = countPrice.indexOf("¥");
        int end = countPrice.indexOf(".");
        intentFView.tv_intent_count_price.setText(getSpannableString(countPrice, start + 1, end == -1 ? countPrice.length() : end, 1.2f, ""));
        setIntentStatus(intentHView, intentFView, orderListBean);
        if (0 <= orderListBean.getStatus() && orderListBean.getStatus() < 10) {
            //            展示倒计时
            if (!TextUtils.isEmpty(orderListBean.getCurrentTime())
                    && isEndOrStartTimeAddSeconds(orderListBean.getCreateTime()
                    , orderListBean.getCurrentTime()
                    , orderListBean.getSecond())) {
                intentHView.tv_direct_indent_create_time.setVisibility(View.GONE);
                intentHView.ll_direct_count_time.setVisibility(View.VISIBLE);
                sparseArray.put(helper.getAdapterPosition() - getHeaderLayoutCount(), intentHView);
                setCountTime(intentHView, orderListBean);
            } else {
                intentHView.tv_direct_indent_create_time.setVisibility(View.VISIBLE);
                intentHView.ll_direct_count_time.setVisibility(View.GONE);
            }
        }
        intentHView.tv_direct_indent_create_time.setText(getStrings(orderListBean.getCreateTime()));
        helper.itemView.setTag(orderListBean);
        directProductListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, DirectExchangeDetailsActivity.class);
                intent.putExtra("orderNo", orderListBean.getNo());
                context.startActivity(intent);
            }
        });
    }

    private void setIntentStatus(IntentHView intentHView, IntentFView intentFView, final OrderListBean orderListBean) {
        intentFView.tv_border_second_blue.setBackgroundResource(R.drawable.border_circle_three_blue_white);
        intentFView.tv_border_second_blue.setTextColor(context.getResources().getColor(R.color.text_login_blue_z));
        intentFView.tv_indent_border_zero_gray.setVisibility(orderListBean.getIsShowPresentLogistics() == 1 ? View.VISIBLE : View.GONE);
        intentFView.tv_indent_border_zero_gray.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(context, DirectLogisticsDetailsActivity.class);
            intent.putExtra("orderNo", orderListBean.getNo());
            context.startActivity(intent);
        });
        int statusCode = orderListBean.getStatus();
        intentHView.tv_indent_type_show.setText(getStrings(ConstantVariable.INDENT_PRO_STATUS.get(String.valueOf(statusCode))));
        if (-20 <= statusCode && statusCode <= -10 || (-26 <= statusCode && statusCode <= -24)) {
            intentFView.ll_indent_bottom.setVisibility(View.GONE);
        } else if (0 <= statusCode && statusCode < 10) {
//          底栏 件数
            intentFView.tv_border_first_gray.setVisibility(View.VISIBLE);
            intentFView.tv_border_second_blue.setVisibility(View.VISIBLE);
            intentFView.tv_border_second_blue.setText("立即付款");
            intentFView.tv_border_first_gray.setText("取消订单");
            intentFView.tv_border_first_gray.setTag(R.id.tag_first, CANCEL_ORDER);
            intentFView.tv_border_first_gray.setTag(R.id.tag_second, orderListBean);
            intentFView.tv_border_first_gray.setOnClickListener(this);
            intentFView.tv_border_second_blue.setTag(R.id.tag_first, PAY);
            intentFView.tv_border_second_blue.setTag(R.id.tag_second, orderListBean);
            intentFView.tv_border_second_blue.setOnClickListener(this);
        } else if (statusCode == 14) {
            intentFView.ll_indent_bottom.setVisibility(View.VISIBLE);
            intentFView.tv_border_first_gray.setVisibility(View.GONE);
            intentFView.tv_border_second_blue.setVisibility(View.VISIBLE);
            intentFView.tv_border_second_blue.setText("邀请参团");
            intentFView.tv_border_second_blue.setTag(R.id.tag_first, INVITE_GROUP);
            intentFView.tv_border_second_blue.setTag(R.id.tag_second, orderListBean);
            intentFView.tv_border_second_blue.setOnClickListener(this);
        } else if (10 <= statusCode && statusCode < 20) {
            if (10 == statusCode) {
                boolean isRefund = true;
                for (int i = 0; i < orderListBean.getGoods().size(); i++) {
                    OrderListBean.GoodsBean goodsBean = orderListBean.getGoods().get(i);
                    if (goodsBean.getStatus() != 10) {
                        isRefund = false;
                        break;
                    }
                }
                if (isRefund) {
//            取消订单
                    intentFView.ll_indent_bottom.setVisibility(View.VISIBLE);
                    intentFView.tv_border_second_blue.setVisibility(View.GONE);
                    intentFView.tv_border_first_gray.setVisibility(View.VISIBLE);
                    intentFView.tv_border_first_gray.setText("取消订单");
                    intentFView.tv_border_first_gray.setTag(R.id.tag_first, CANCEL_PAY_ORDER);
                    intentFView.tv_border_first_gray.setTag(R.id.tag_second, orderListBean);
                    intentFView.tv_border_first_gray.setOnClickListener(this);
                } else {
                    //            不可取消订单
                    intentFView.ll_indent_bottom.setVisibility(View.GONE);
                    intentFView.tv_border_second_blue.setVisibility(View.GONE);
                    intentFView.tv_border_first_gray.setVisibility(View.GONE);
                }
            } else if (12 == statusCode) {
                intentFView.ll_indent_bottom.setVisibility(View.VISIBLE);
                intentFView.tv_border_second_blue.setVisibility(View.GONE);
                intentFView.tv_border_first_gray.setVisibility(View.VISIBLE);
                intentFView.tv_border_first_gray.setText("查看物流");
                intentFView.tv_border_first_gray.setTag(R.id.tag_first, LITTER_CONSIGN);
                intentFView.tv_border_first_gray.setTag(R.id.tag_second, orderListBean);
                intentFView.tv_border_first_gray.setOnClickListener(this);
            } else {
//            不可取消订单
                intentFView.ll_indent_bottom.setVisibility(View.GONE);
                intentFView.tv_border_second_blue.setVisibility(View.GONE);
                intentFView.tv_border_first_gray.setVisibility(View.GONE);
            }
            if (orderListBean.isWaitDeliveryFlag()) {
                intentFView.ll_indent_bottom.setVisibility(View.VISIBLE);
                intentFView.tv_border_second_blue.setVisibility(View.VISIBLE);
                intentFView.tv_border_second_blue.setText("提醒发货");
                intentFView.tv_border_second_blue.setTag(R.id.tag_first, REMIND_DELIVERY);
                intentFView.tv_border_second_blue.setTag(R.id.tag_second, orderListBean);
                intentFView.tv_border_second_blue.setOnClickListener(this);
            }
        } else if (20 <= statusCode && statusCode < 30) {
            intentFView.tv_border_first_gray.setVisibility(View.VISIBLE);
            intentFView.tv_border_second_blue.setVisibility(View.VISIBLE);
            intentFView.tv_border_first_gray.setText("查看物流");
            intentFView.tv_border_second_blue.setText("确认收货");
            intentFView.tv_border_first_gray.setTag(R.id.tag_first, CHECK_LOG);
            intentFView.tv_border_first_gray.setTag(R.id.tag_second, orderListBean);
            intentFView.tv_border_first_gray.setOnClickListener(this);
//          确认订单
            intentFView.tv_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
            intentFView.tv_border_second_blue.setTag(R.id.tag_second, orderListBean);
            intentFView.tv_border_second_blue.setOnClickListener(this);
        } else if (30 <= statusCode && statusCode <= 40) {
            //待评价和部分评价状态都要显示评分按钮
            if (statusCode == 30 || statusCode == 31) {
                intentFView.ll_indent_bottom.setVisibility(View.VISIBLE);
                //列表暂时不展示发票信息
                intentFView.tv_border_first_gray.setVisibility(View.GONE);
                intentFView.tv_border_second_blue.setText(R.string.write_score_comment);
                intentFView.tv_border_second_blue.setBackgroundResource(R.drawable.border_circle_three_yellow_white);
                intentFView.tv_border_second_blue.setTextColor(context.getResources().getColor(R.color.max_score_yellow));
                intentFView.tv_max_reward.setText(orderListBean.getMaxRewardTip());
                //未评价的数量
                int noShowEvaluateNum = 0;
                GoodsBean goodsBean = null;
                List<GoodsBean> goods = orderListBean.getGoods();
                for (GoodsBean bean : goods) {
                    if (bean.getCombineProductInfoList() == null) {
                        //普通商品
                        if (bean.getStatus() == 30) {
                            noShowEvaluateNum++;
                            goodsBean = bean;
                        }
                    } else if (bean.getCombineProductInfoList().size() > 0) {
                        int num = 0;
                        List<CartProductInfoBean> combineProductInfoList = bean.getCombineProductInfoList();
                        //判断组合购是否所有子商品待评价
                        for (CartProductInfoBean cartProductInfoBean : combineProductInfoList) {
                            if (cartProductInfoBean.getStatus() == 30) {
                                num++;
                            }
                        }
                        if (num == combineProductInfoList.size()) {
                            noShowEvaluateNum++;
                            goodsBean = bean;
                        }
                    }
                }
                GoodsBean finalGoodsBean = goodsBean;
                int finalNoShowEvaluateNum = noShowEvaluateNum;
                intentFView.ll_indent_bottom.setVisibility(finalNoShowEvaluateNum > 0 ? View.VISIBLE : View.GONE);
                intentFView.tv_max_reward.setVisibility(finalNoShowEvaluateNum > 0 ? View.VISIBLE : View.GONE);
                intentFView.tv_border_second_blue.setOnClickListener(v -> {
                    if (finalNoShowEvaluateNum == 1) {
                        //直接跳转评分界面
                        ScoreGoodsEntity.ScoreGoodsBean scoreGoodsBean = new ScoreGoodsEntity.ScoreGoodsBean();
                        scoreGoodsBean.setOrderNo(orderListBean.getNo());
                        scoreGoodsBean.setCover(finalGoodsBean.getPicUrl());
                        scoreGoodsBean.setOrderProductId(String.valueOf(finalGoodsBean.getOrderProductId()));
                        scoreGoodsBean.setProductId(String.valueOf(finalGoodsBean.getId()));
                        scoreGoodsBean.setProductName(finalGoodsBean.getName());
                        Intent intent = new Intent(context, JoinTopicActivity.class);
                        intent.putExtra("maxRewardTip", orderListBean.getMaxRewardTip());
                        intent.putExtra("scoreGoods", scoreGoodsBean);
                        context.startActivity(intent);
                    } else if (finalNoShowEvaluateNum > 1) {
                        //跳转评分列表
                        Intent intent = new Intent(context, IndentScoreListActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        context.startActivity(intent);
                    }
                });
            } else if (statusCode == 40) {//已评价
                intentFView.ll_indent_bottom.setVisibility(View.GONE);
            }
        } else {
            intentFView.ll_indent_bottom.setVisibility(View.GONE);
        }
    }

    private void setCountTime(final IntentHView holder, OrderListBean orderListBean) {
        try {
            //格式化结束时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateCreate = formatter.parse(orderListBean.getCreateTime());
            long overTime = orderListBean.getSecond() * 1000;
            Date dateCurrent;
            if (!TextUtils.isEmpty(orderListBean.getCurrentTime())) {
                dateCurrent = formatter.parse(orderListBean.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            holder.cv_countdownTime_direct.updateShow(dateCreate.getTime() + overTime - dateCurrent.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (isEndOrStartTimeAddSeconds(orderListBean.getCreateTime()
                , orderListBean.getCurrentTime()
                , orderListBean.getSecond())) {
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

    public void setOnClickViewListener(OnClickViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.click((String) v.getTag(R.id.tag_first), (OrderListBean) v.getTag(R.id.tag_second));
        }
    }

    public interface OnClickViewListener {
        void click(String type, OrderListBean orderListBean);
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
        //        底栏
        @BindView(R.id.ll_indent_bottom)
        LinearLayout ll_indent_bottom;
        //        查看赠品物流
        @BindView(R.id.tv_indent_border_zero_gray)
        TextView tv_indent_border_zero_gray;
        @BindView(R.id.tv_indent_border_first_gray)
        TextView tv_border_first_gray;
        @BindView(R.id.tv_indent_border_second_blue)
        TextView tv_border_second_blue;
        //        商品 件数 价格 统计
        @BindView(R.id.tv_intent_count_price)
        TextView tv_intent_count_price;
        @BindView(R.id.tv_max_reward)
        TextView tv_max_reward;
    }

    public class IndentListViewHolder extends BaseViewHolder {
        private RecyclerView communal_recycler_wrap;

        public IndentListViewHolder(View view) {
            super(view);
            communal_recycler_wrap = (RecyclerView) view.findViewById(R.id.communal_recycler_wrap);
            if (communal_recycler_wrap != null) {
                communal_recycler_wrap.setNestedScrollingEnabled(false);
                communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
                communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                        // 设置分隔线资源ID
                        .setDividerId(R.drawable.item_divider_gray_f_two_px)
                        .create());
            }
        }
    }
}
