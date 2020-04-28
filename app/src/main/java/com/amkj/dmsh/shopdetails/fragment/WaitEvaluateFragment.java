package com.amkj.dmsh.shopdetails.fragment;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.WaitEvaluaterProductEntity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity.ScoreGoodsBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.WaitEvaluateProductsAdapter;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.WindowUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.OrderLoadMoreView;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipJoinTopic;
import static com.amkj.dmsh.constant.ConstantVariable.DEMO_LIFE_FILE;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_WAIT_EVALUATE_INDENT_LIST;

/**
 * Created by xiaoxin on 2020/4/18
 * Version:v4.5.0
 * ClassDescription :待评价商品订单列表
 */
public class WaitEvaluateFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<OrderProductNewBean> orderProducts = new ArrayList<>();
    private int page = 1;
    private WaitEvaluateProductsAdapter doMoIndentListAdapter;
    private WaitEvaluaterProductEntity mWaitEvaluaterEntity;
    private PopupWindow mPwScore;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp).create());
        doMoIndentListAdapter = new WaitEvaluateProductsAdapter(getActivity(), orderProducts);
        doMoIndentListAdapter.setLoadMoreView(new OrderLoadMoreView());
        communal_recycler.setAdapter(doMoIndentListAdapter);
        doMoIndentListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (view.getTag() != null) {
                ScoreGoodsBean scoreGoodsBean = (ScoreGoodsBean) view.getTag();
                getScorePop(scoreGoodsBean);
            }
        });
        //解决调用notifyItemChanged闪烁问题
        SimpleItemAnimator itemAnimator = (SimpleItemAnimator) communal_recycler.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setSupportsChangeAnimations(false);
        }
        setFloatingButton(download_btn_communal, communal_recycler);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            loadData();
        });
        doMoIndentListAdapter.setOnLoadMoreListener(() -> {
            page++;
            getIndentList();
        }, communal_recycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getIndentList();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //获取订单列表数据
    private void getIndentList() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_WAIT_EVALUATE_PRODUCTS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mWaitEvaluaterEntity = new Gson().fromJson(result, WaitEvaluaterProductEntity.class);
                String code = mWaitEvaluaterEntity.getCode();
                if (page == 1) {
                    orderProducts.clear();
                }
                if (SUCCESS_CODE.equals(code)) {
                    List<OrderProductNewBean> orderList = mWaitEvaluaterEntity.getResult();
                    if (orderList != null) {
                        orderProducts.addAll(orderList);
                        doMoIndentListAdapter.notifyDataSetChanged();
                        if (orderList.size() >= TOTAL_COUNT_TEN) {
                            doMoIndentListAdapter.loadMoreComplete();
                        } else {
                            doMoIndentListAdapter.loadMoreEnd();
                        }
                    }
                } else {
                    doMoIndentListAdapter.notifyDataSetChanged();
                    doMoIndentListAdapter.loadMoreEnd();
                    if (!EMPTY_CODE.equals(code)) showToast(mWaitEvaluaterEntity.getMsg());
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderProducts, mWaitEvaluaterEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, orderProducts, mWaitEvaluaterEntity);
            }
        });
    }

    //获取点评弹窗
    private void getScorePop(ScoreGoodsBean scoreGoodsBean) {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.Q_GET_TAKE_DELIVERY_POPUP, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = new Gson().fromJson(result, RequestStatus.class);
                boolean isOpen = requestStatus.isOpen();
                String imgUrl = requestStatus.getImgUrl();
                if (isOpen && !TextUtils.isEmpty(imgUrl)) {
                    GlideImageLoaderUtil.loadFinishImgDrawable(getActivity(), imgUrl, new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            int joinCount = (int) SharedPreUtils.getParam(DEMO_LIFE_FILE, "IndentJoinCount", 0);
                            if (joinCount < 2) {
                                if (mPwScore == null) {
                                    mPwScore = WindowUtils.getAlphaPw(getActivity(), R.layout.pw_join_tips, Gravity.CENTER);
                                }

                                GlideImageLoaderUtil.loadCenterCrop(getActivity(), mPwScore.getContentView().findViewById(R.id.iv_cover), imgUrl);
                                mPwScore.getContentView().setOnClickListener((View v1) -> {
                                    mPwScore.dismiss();
                                    //写点评
                                    skipJoinTopic(getActivity(), scoreGoodsBean, null);

                                });
                                WindowUtils.showPw(getActivity(), mPwScore, Gravity.CENTER);
                                SharedPreUtils.setParam(DEMO_LIFE_FILE, "IndentJoinCount", joinCount + 1);
                            } else {
                                //写点评
                                skipJoinTopic(getActivity(), scoreGoodsBean, null);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });

                }
            }
        });
    }

    @Override
    protected boolean isDataInitiated() {
        return false;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (UPDATE_WAIT_EVALUATE_INDENT_LIST.equals(message.type)) {
            int position = (int) message.result;
            if (position >= 0 && position <= orderProducts.size() - 1) {
                doMoIndentListAdapter.remove(position);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowUtils.closePw(mPwScore);
    }
}
