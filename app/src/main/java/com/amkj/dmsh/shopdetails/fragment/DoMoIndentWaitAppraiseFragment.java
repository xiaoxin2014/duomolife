package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.ShopCarNewInfoEntity.ShopCarNewInfoBean.CartInfoBean.CartProductInfoBean;
import com.amkj.dmsh.release.activity.ReleaseImgArticleActivity;
import com.amkj.dmsh.shopdetails.activity.DirectIndentWriteActivity;
import com.amkj.dmsh.shopdetails.activity.DirectPublishAppraiseActivity;
import com.amkj.dmsh.shopdetails.adapter.DoMoIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.BASK_READER;
import static com.amkj.dmsh.constant.ConstantVariable.BUY_AGAIN;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.DEL;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;
;

/**
 * Created by atd48 on 2016/8/24.
 * 待评价
 */

public class DoMoIndentWaitAppraiseFragment extends BaseFragment{
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<OrderListBean> orderListBeanList = new ArrayList();
    //根据type类型分类DuomoIndentPayFragment
    private DoMoIndentListAdapter doMoIndentListAdapter;
    //    数据传递
    private List<DirectAppraisePassBean> directAppraisePassList = new ArrayList<>();
    private OrderListBean orderBean;
    private DirectAppraisePassBean directAppraisePassBean;
    private boolean isOnPause;
    private int scrollY = 0;
    private int page = 0;
    private float screenHeight;
    private InquiryOrderEntry inquiryOrderEntry;
    private AlertDialogHelper delOrderDialogHelper;

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
                .setDividerId(R.drawable.item_divider_five_dp)






                .create());
        doMoIndentListAdapter = new DoMoIndentListAdapter(getActivity(), orderListBeanList);
        communal_recycler.setAdapter(doMoIndentListAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            scrollY = 0;
            loadData();
        });
        doMoIndentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= doMoIndentListAdapter.getItemCount()) {
                    page++;
                    getWaitAppraiseData();
                } else {
                    doMoIndentListAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                scrollY += dy;
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
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
        doMoIndentListAdapter.setOnClickViewListener(new DoMoIndentListAdapter.OnClickViewListener() {
            @Override
            public void click(String type, OrderListBean orderListBean) {
                orderBean = orderListBean;
                Intent intent = new Intent();
                Bundle bundle;
                switch (type) {
                    case BUY_AGAIN:
//                        再次购买
                        intent.setClass(getActivity(), DirectIndentWriteActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case PRO_APPRAISE:
                        if (directAppraisePassList != null) {
                            directAppraisePassList.clear();
                        }
//                        评价
                        for (int i = 0; i < orderListBean.getGoods().size(); i++) {
                            GoodsBean goodBean = orderListBean.getGoods().get(i);
//                            判断商品是否已评价
                            if (goodBean.getCombineProductInfoList() != null
                                    && goodBean.getCombineProductInfoList().size() > 0) {
                                for (int k = 0; k < goodBean.getCombineProductInfoList().size(); k++) {
                                    CartProductInfoBean cartProductInfoBean = goodBean.getCombineProductInfoList().get(k);
                                    if (cartProductInfoBean.getStatus() == 30) {
                                        directAppraisePassBean = new DirectAppraisePassBean();
                                        directAppraisePassBean.setProductId(cartProductInfoBean.getId());
                                        directAppraisePassBean.setOrderProductId(cartProductInfoBean.getOrderProductId());
                                        directAppraisePassBean.setPath(cartProductInfoBean.getPicUrl());
                                        directAppraisePassBean.setStar(5);
                                        directAppraisePassList.add(directAppraisePassBean);
                                    }
                                }
                            } else if (goodBean.getStatus() == 30) {
                                directAppraisePassBean = new DirectAppraisePassBean();
                                directAppraisePassBean.setPath(goodBean.getPicUrl());
                                directAppraisePassBean.setProductId(goodBean.getId());
                                directAppraisePassBean.setStar(5);
                                directAppraisePassBean.setOrderProductId(goodBean.getOrderProductId());
                                directAppraisePassList.add(directAppraisePassBean);
                            }
                        }
                        intent.setClass(getActivity(), DirectPublishAppraiseActivity.class);
                        bundle = new Bundle();
                        bundle.putParcelableArrayList("appraiseData", (ArrayList<? extends Parcelable>) directAppraisePassList);
                        intent.putExtras(bundle);
                        intent.putExtra("uid", orderListBean.getUserId());
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    //                        晒单赢积分
                    case BASK_READER:
                        intent.setClass(getActivity(), ReleaseImgArticleActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case DEL:
//                        删除订单
                        if (delOrderDialogHelper == null) {
                            delOrderDialogHelper = new AlertDialogHelper(getActivity());
                            delOrderDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                                    .setMsg("确定要删除该订单？").setCancelText("取消").setConfirmText("确定")
                                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                            delOrderDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    delOrder();
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                        delOrderDialogHelper.show();
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getWaitAppraiseData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getWaitAppraiseData() {
        String url = Url.BASE_URL + Url.Q_INQUIRY_FINISH;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("currentPage", page);
        //        版本号控制 3 组合商品赠品
        params.put("version", 3);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        doMoIndentListAdapter.loadMoreComplete();
                        String code = "";
                        String msg = "";
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = (String) jsonObject.get("code");
                            msg = (String) jsonObject.get("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (page == 1) {
                            orderListBeanList.clear();
                        }
                        if (code.equals(SUCCESS_CODE)) {
                            Gson gson = new Gson();
                            inquiryOrderEntry = gson.fromJson(result, InquiryOrderEntry.class);
                            INDENT_PRO_STATUS = inquiryOrderEntry.getOrderInquiryDateEntry().getStatus();
                            orderListBeanList.addAll(inquiryOrderEntry.getOrderInquiryDateEntry().getOrderList());
                        } else if (!code.equals(EMPTY_CODE)) {
                            showToast(getActivity(), msg);
                        }
                        doMoIndentListAdapter.notifyDataSetChanged();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,code);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        doMoIndentListAdapter.loadMoreComplete();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,inquiryOrderEntry);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        doMoIndentListAdapter.loadMoreComplete();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,inquiryOrderEntry);
                    }
                });
    }

    //  订单删除
    private void delOrder() {
        String url = Url.BASE_URL + Url.Q_INDENT_DEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
        params.put("userId", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus indentInfo = gson.fromJson(result, RequestStatus.class);
                if (indentInfo != null) {
                    if (indentInfo.getCode().equals("01")) {
                        loadData();
                        showToast(getActivity(), String.format(getResources().getString(R.string.doSuccess), "删除订单"));
                    } else {
                        showToast(getActivity(), indentInfo.getResult() != null ?
                                indentInfo.getResult().getMsg() : indentInfo.getMsg());
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        isOnPause = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
        }
    }
}
