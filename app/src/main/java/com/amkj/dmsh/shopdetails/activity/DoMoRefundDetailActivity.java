package com.amkj.dmsh.shopdetails.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.bean.ButtonListBean;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity.MainOrderBean;
import com.amkj.dmsh.shopdetails.bean.RefundGoodsAddressInfoBean;
import com.amkj.dmsh.shopdetails.bean.RefundLogisticEntity;
import com.amkj.dmsh.shopdetails.bean.RefundNewDetailEntity;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.MainButtonView;
import com.amkj.dmsh.views.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.views.alertdialog.AlertDialogWheel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.skipRefundAspect;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.NOGOODS_REFUND;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_ASPECT;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_DETAIL_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_CANCEL_APPLY_NEW;
import static com.amkj.dmsh.constant.Url.Q_INDENT_LOGISTIC_COM;
import static com.amkj.dmsh.constant.Url.Q_INDENT_LOGISTIC_SUB;
import static com.amkj.dmsh.constant.Url.Q_INDENT_REFUND_NEW_DETAIL;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/18
 * class description:??????????????????
 */
public class DoMoRefundDetailActivity extends BaseActivity {
    @BindView(R.id.iv_indent_service)
    ImageView iv_indent_service;
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.sv_layout_refund_detail)
    NestedScrollView sv_layout_refund_detail;
    //    ????????????
    @BindView(R.id.tv_refund_detail_status)
    TextView tv_refund_detail_status;
    //    ??????????????????
    @BindView(R.id.ll_refund_logistic_info)
    LinearLayout ll_refund_logistic_info;
    //    ????????????
    @BindView(R.id.tv_repair_address)
    TextView tv_refund_address;
    //    ???????????????*
    @BindView(R.id.tv_refund_logistic_no)
    TextView tv_refund_logistic_no;
    @BindView(R.id.tv_indent_order_no)
    TextView tv_indent_order_no;
    //    ??????????????????
    @BindView(R.id.et_refund_logistic_no)
    EditText et_refund_logistic_no;
    @BindView(R.id.tv_refund_logistic_sel)
    TextView tv_refund_logistic_sel;
    @BindView(R.id.tv_refund_detail_msg)
    TextView mTvRefundDetailMsg;
    @BindView(R.id.tv_close_time)
    TextView mTvCloseTime;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.tv_refund_type)
    TextView mTvRefundType;
    @BindView(R.id.ll_refund_type)
    LinearLayout mLlRefundType;
    @BindView(R.id.tv_refund_reason)
    TextView mTvRefundReason;
    @BindView(R.id.ll_refund_reason)
    LinearLayout mLlRefundReason;
    @BindView(R.id.tv_refund_amount)
    TextView mTvRefundAmount;
    @BindView(R.id.ll_refund_amount)
    LinearLayout mLlRefundAmount;
    @BindView(R.id.tv_apply_time)
    TextView mTvApplyTime;
    @BindView(R.id.ll_apply_time)
    LinearLayout mLlApplyTime;
    @BindView(R.id.tv_refund_no)
    TextView mTvRefundNo;
    @BindView(R.id.ll_refund_no)
    LinearLayout mLlRefundNo;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.main_button_view)
    MainButtonView mainButtonView;
    @BindView(R.id.tv_change_apply)
    TextView mTvChangeApply;
    @BindView(R.id.tv_cancle_apply)
    TextView mTvCancleApply;
    @BindView(R.id.tv_commit_apply)
    TextView mTvCommitApply;
    @BindView(R.id.tv_refuse_reason)
    TextView mTvRefuseReason;
    @BindView(R.id.tv_refund_logistic_company)
    TextView mTvRefundLogisticCompany;
    @BindView(R.id.ll_refund_logistic_no)
    LinearLayout ll_refund_logistic_no;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.tv_refund_aspect)
    TextView tv_refund_aspect;
    @BindView(R.id.ll_refund_status)
    LinearLayout ll_refund_status;


    private String orderNo;
    private String refundNo;
    private String orderProductId;
    private List<String> mExpressCompanies = new ArrayList<>();
    private MainOrderBean mainOrderBean;
    private RefundNewDetailEntity refundDetailEntity;
    private AlertDialogHelper cancelApplyDialogHelper;
    private DirectProductListAdapter directProductListAdapter;
    private List<OrderProductNewBean> goodsBeanList = new ArrayList<>();
    private String refundAddress;
    private boolean isFirst = true;
    private AlertDialogWheel mAlertDialogExpress;
    private CountDownTimer mCountDownTimer;

    @Override
    protected int getContentView() {
        return R.layout.activity_domo_refund_detail;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        iv_indent_search.setVisibility(GONE);
        tv_indent_title.setText("????????????");
        Intent intent = getIntent();
        refundNo = intent.getStringExtra("refundNo");

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        //???????????????????????????
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        directProductListAdapter = new DirectProductListAdapter(this, goodsBeanList, REFUND_DETAIL_TYPE);
        communal_recycler.setAdapter(directProductListAdapter);
        directProductListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.ll_product) {
                OrderProductNewBean orderProductBean = (OrderProductNewBean) view.getTag();
                if (orderProductBean != null) {
                    Intent intent1 = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                    intent1.putExtra("productId", String.valueOf(orderProductBean.getProductId()));
                    startActivity(intent1);
                }
            }
        });

        //???????????????????????????????????????????????????
        KeyboardUtils.registerSoftInputChangedListener(getActivity(), height -> {
            if (mainOrderBean != null) {
                mTvCommitApply.setVisibility(height == 0 && mainOrderBean.getStatus() == 5 ? VISIBLE : GONE);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sv_layout_refund_detail.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, AutoSizeUtils.mm2px(this, height != 0 ? 0 : 113));
                sv_layout_refund_detail.setLayoutParams(layoutParams);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void loadData() {
        getRefundDetailData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return sv_layout_refund_detail;
    }

    private void getRefundDetailData() {
        Map<String, Object> params = new HashMap<>();
        params.put("refundNo", refundNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_REFUND_NEW_DETAIL, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                refundDetailEntity = GsonUtils.fromJson(result, RefundNewDetailEntity.class);
                if (refundDetailEntity != null) {
                    if (refundDetailEntity.getCode().equals(SUCCESS_CODE)) {
                        mainOrderBean = refundDetailEntity.getRefundDetailBean();
                        if (mainOrderBean != null) {
                            mainOrderBean.setCurrentTime(refundDetailEntity.getCurrentTime());
                            orderNo = mainOrderBean.getOrderNo();
                            setRefundDetailData(mainOrderBean);
                        }
                    } else {
                        showToast(refundDetailEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, refundDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, refundDetailEntity);
            }
        });
    }


    private void getLogisticCompany() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_LOGISTIC_COM, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RefundLogisticEntity refundLogisticEntity = GsonUtils.fromJson(result, RefundLogisticEntity.class);
                if (refundLogisticEntity != null) {
                    if (refundLogisticEntity.getCode().equals(SUCCESS_CODE)) {
                        List<String> expressCompanys = refundLogisticEntity.getExpressCompanys();
                        if (expressCompanys != null && expressCompanys.size() > 0) {
                            mExpressCompanies.addAll(expressCompanys);
                        }
                    }
                }
            }
        });
    }


    /**
     * ??????????????????
     *
     * @param mainOrderBean
     */
    private void setRefundDetailData(MainOrderBean mainOrderBean) {
        //??????????????????
        String statusText = getStrings(mainOrderBean.getRefundStatusText());
        String msg = getStrings(mainOrderBean.getMsg());
        String refuseReason = getStrings(mainOrderBean.getRefuseReason());
        tv_refund_detail_status.setText(statusText);
        mTvRefuseReason.setText(refuseReason);

        if (!TextUtils.isEmpty(msg)) {
            int indexOf = msg.indexOf("????????????");
            mTvRefundDetailMsg.setText(indexOf != -1 ? ConstantMethod.getSpannableString(msg, indexOf, msg.length(), -1, "#0a88fa") : msg);
            mTvRefundDetailMsg.setOnClickListener(v -> {
                if (indexOf != -1) {
                    QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(), "??????????????????");
                }
            });
        }

        ll_refund_status.setVisibility(!TextUtils.isEmpty(statusText) ? View.VISIBLE : GONE);
        mTvRefundDetailMsg.setVisibility(!TextUtils.isEmpty(msg) ? View.VISIBLE : GONE);
        mTvRefuseReason.setVisibility(!TextUtils.isEmpty(refuseReason) ? View.VISIBLE : GONE);

        //????????????????????????
        setCountTime(mainOrderBean);

        //???????????????
        List<ButtonListBean> buttonList = mainOrderBean.getButtonList();
        for (int i = 0; i < buttonList.size(); i++) {
            ButtonListBean buttonListBean = buttonList.get(i);
            if (REFUND_ASPECT == buttonListBean.getId()) {
                tv_refund_aspect.setVisibility(View.VISIBLE);
                buttonList.remove(i);
                break;
            }
        }

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) sv_layout_refund_detail.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, buttonList.size() > 0 ? AutoSizeUtils.mm2px(this, 100) : 0);
        sv_layout_refund_detail.setLayoutParams(layoutParams);
        mainButtonView.updateView(this, mainOrderBean, getSimpleName());

        RefundGoodsAddressInfoBean refundGoodsAddressInfo = mainOrderBean.getRefundGoodsAddressInfo();
        ll_refund_logistic_info.setVisibility(refundGoodsAddressInfo != null ? View.VISIBLE : GONE);
        if (refundGoodsAddressInfo != null) {
            //??????????????????
            String address = getStrings(refundGoodsAddressInfo.getAddress());
            String consignee = getStrings(refundGoodsAddressInfo.getConsignee());
            String mobile = getStrings(refundGoodsAddressInfo.getMobile());
            refundAddress = address + " " + consignee + " " + mobile;
            tv_refund_address.setText(address);
            mTvName.setText((consignee + mobile));

            //??????????????????,????????????????????????
            if (mainOrderBean.getStatus() == 5) {
                getLogisticCompany();
                mTvRefundLogisticCompany.setVisibility(GONE);
                ll_refund_logistic_no.setVisibility(GONE);
                tv_refund_logistic_sel.setVisibility(View.VISIBLE);
                et_refund_logistic_no.setVisibility(View.VISIBLE);
                mTvCommitApply.setVisibility(View.VISIBLE);
                layoutParams.setMargins(0, 0, 0, AutoSizeUtils.mm2px(this, 113));
                sv_layout_refund_detail.setLayoutParams(layoutParams);
            } else {
                //???????????????????????????
                String expressCompany = refundGoodsAddressInfo.getExpressCompany();
                String expressNo = refundGoodsAddressInfo.getExpressNo();
                mTvRefundLogisticCompany.setText(getStrings(expressCompany));
                tv_refund_logistic_no.setText(getStrings(expressNo));
                tv_refund_logistic_sel.setVisibility(GONE);
                et_refund_logistic_no.setVisibility(View.GONE);
                mTvRefundLogisticCompany.setVisibility(!TextUtils.isEmpty(expressCompany) ? View.VISIBLE : GONE);
                ll_refund_logistic_no.setVisibility(!TextUtils.isEmpty(expressNo) ? View.VISIBLE : GONE);
                mTvCommitApply.setVisibility(GONE);
            }
        }

        //????????????????????????
        List<OrderProductNewBean> refundGoodsList = mainOrderBean.getOrderProductList();
        goodsBeanList.clear();
        if (refundGoodsList != null && refundGoodsList.size() > 0) {
            goodsBeanList.addAll(refundGoodsList);
            orderProductId = goodsBeanList.get(0).getOrderProductId();//???????????????????????????
        }
        directProductListAdapter.notifyDataSetChanged();

        //??????????????????
        mTvRefundType.setText(getStrings(mainOrderBean.getRefundType()));
        mTvRefundReason.setText(getStrings(mainOrderBean.getRefunReason()));
        mTvRefundAmount.setText(getStrings(mainOrderBean.getRefundPrice()));
        mTvApplyTime.setText(getStrings(mainOrderBean.getRefundTime()));
        mTvRefundNo.setText(getStrings(mainOrderBean.getRefundNo()));
        tv_indent_order_no.setText(getStrings(mainOrderBean.getOrderNo()));

        //??????????????????????????????
        mTvChangeApply.setVisibility(mainOrderBean.isShowUpdate() ? View.VISIBLE : GONE);
        mTvCancleApply.setVisibility(mainOrderBean.isShowCancel() ? View.VISIBLE : GONE);
    }

    private void setCountTime(MainOrderBean mainOrderBean) {
        String currentTime = mainOrderBean.getCurrentTime();
        String endTime = mainOrderBean.getEndTime();
        try {
            if (isEndOrStartTime(endTime, currentTime)) {
                mTvCloseTime.setVisibility(View.VISIBLE);
                if (mCountDownTimer == null) {
                    mCountDownTimer = new CountDownTimer(getActivity()) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String coutDownTime = getCoutDownTime(millisUntilFinished, false);
                            String coutDownText = "????????????????????????????????????" + coutDownTime;
                            mTvCloseTime.setText(ConstantMethod.getSpannableString(coutDownText, 12, coutDownText.length(), -1, "#3274d9", true));
                        }

                        @Override
                        public void onFinish() {
                            mTvCloseTime.setText("?????????");
                        }
                    };
                }
                mCountDownTimer.setMillisInFuture(getTimeDifference(currentTime,endTime));
                mCountDownTimer.start();
            } else {
                mTvCloseTime.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mTvCloseTime.setVisibility(GONE);
        }
    }


    @OnClick({R.id.tv_indent_back, R.id.iv_indent_service, R.id.ll_qy_service, R.id.tv_cancle_apply, R.id.tv_change_apply, R.id.tv_copy_text,
            R.id.tv_copy_logistic_no, R.id.tv_refund_logistic_sel, R.id.tv_commit_apply, R.id.tv_refund_aspect, R.id.tv_copy_order_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_indent_back:
                finish();
                break;
            case R.id.ll_qy_service:
            case R.id.iv_indent_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(this, "??????????????????");
                break;
            //??????????????????
            case R.id.tv_copy_text:
                if (!TextUtils.isEmpty(refundAddress)) {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", refundAddress);
                    cmb.setPrimaryClip(mClipData);
                    showToast("?????????");
                }
                break;
            //??????????????????
            case R.id.tv_copy_logistic_no:
                String expressNo = tv_refund_logistic_no.getText().toString();
                if (!TextUtils.isEmpty(expressNo)) {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", expressNo);
                    cmb.setPrimaryClip(mClipData);
                    showToast("?????????");
                }
                break;
            //??????????????????
            case R.id.tv_copy_order_no:
                if (!TextUtils.isEmpty(orderNo)) {
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", orderNo);
                    cmb.setPrimaryClip(mClipData);
                    showToast("?????????");
                }
                break;
            //??????????????????
            case R.id.tv_refund_logistic_sel:
                if (mAlertDialogExpress == null) {
                    mAlertDialogExpress = new AlertDialogWheel(this);
                    mAlertDialogExpress.setConfirmListener((key, value) -> {
                        tv_refund_logistic_sel.setText(value);
                    });
                }
                mAlertDialogExpress.updateView(mExpressCompanies);
                mAlertDialogExpress.show(Gravity.BOTTOM);
                break;
            //????????????
            case R.id.tv_refund_aspect:
                skipRefundAspect(getActivity(), directProductListAdapter.getData().size(), orderNo, refundNo);
                break;
            //????????????
            case R.id.tv_cancle_apply:
                if (cancelApplyDialogHelper == null) {
                    cancelApplyDialogHelper = new AlertDialogHelper(getActivity());
                    cancelApplyDialogHelper.setTitleVisibility(GONE).setMsgTextGravity(Gravity.CENTER)
                            .setMsg(getResources().getString(R.string.cancel_invite)).setCancelText("??????")
                            .setConfirmText("??????")
                            .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                    cancelApplyDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {
                            cancelRefund();
                        }

                        @Override
                        public void cancel() {
                        }
                    });
                }
                cancelApplyDialogHelper.show();
                break;
            //????????????
            case R.id.tv_change_apply:
                Intent intent = new Intent();
                //????????????
                if (mainOrderBean.isAfter()) {
                    intent.setClass(getActivity(), SelectRefundTypeActivity.class);
                } else {//???????????????
                    intent.setClass(getActivity(), DirectApplyRefundActivity.class);
                    intent.putExtra("refundType", NOGOODS_REFUND);
                }
                intent.putExtra("orderNo", mainOrderBean.getOrderNo());
                intent.putExtra("refundNo", mainOrderBean.getRefundNo());
                intent.putExtra("goods", GsonUtils.toJson(goodsBeanList));
                startActivity(intent);
                break;
            //??????????????????
            case R.id.tv_commit_apply:
                String logistic = tv_refund_logistic_sel.getText().toString().trim();
                String logisticNo = et_refund_logistic_no.getText().toString().trim();
                if (TextUtils.isEmpty(logistic)) {
                    showToast("?????????????????????");
                } else if (TextUtils.isEmpty(logisticNo)) {
                    showToast("?????????????????????");
                } else {
                    submitLogisticInfo(logistic, logisticNo);
                }
                break;
        }
    }


    /**
     * ????????????????????????
     *
     * @param logistic
     * @param logisticNo
     */
    private void submitLogisticInfo(String logistic, String logisticNo) {
        showLoadhud(this);
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("orderProductId", orderProductId);
        params.put("orderRefundProductId", mainOrderBean.getOrderRefundProductId());
        params.put("userId", userId);
        params.put("expressCompany", logistic);
        params.put("expressNo", logisticNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_LOGISTIC_SUB, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(String.format(getResources().getString(R.string.doSuccess), "??????"));
                        loadData();
                    } else
                        showToastRequestMsg(requestStatus);
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                showToast(R.string.do_failed);
            }
        });
    }


    /**
     * ????????????
     */
    private void cancelRefund() {
        showLoadhud(this);
        Map<String, Object> map = new HashMap<>();
        map.put("refundNo", refundNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_CANCEL_APPLY_NEW, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());

                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast(String.format(getResources().getString(R.string.doSuccess), "??????"));
                        finish();
                    } else {
                        showToastRequestMsg(requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                showToast(R.string.do_failed);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            loadData();
        }
        isFirst = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
