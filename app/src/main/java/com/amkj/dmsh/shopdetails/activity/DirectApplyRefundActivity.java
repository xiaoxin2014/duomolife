package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter;
import com.amkj.dmsh.release.bean.ImagePathBean;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity;
import com.amkj.dmsh.shopdetails.bean.RefundInfoEntity;
import com.amkj.dmsh.shopdetails.dialog.AlertDialogWheel;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.skipRefundDetail;
import static com.amkj.dmsh.constant.ConstantVariable.APPLY_REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RETURN_GOODS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_INDENT_APPLY_REFUND_NEW;
import static com.amkj.dmsh.utils.ImageFormatUtils.getImageFormatInstance;


/**
 * Created by atd48 on 2016/10/27.
 * 申请退款
 */
public class DirectApplyRefundActivity extends BaseActivity {
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;
    //    退款类型选择
    @BindView(R.id.tv_dir_indent_apply_type_sel)
    TextView tv_dir_indent_apply_type_sel;
    //    退款说明 退款金额
    @BindView(R.id.ll_refund_price)
    LinearLayout ll_refund_price;
    @BindView(R.id.tv_indent_reply_reason_tint)
    TextView tv_indent_reply_reason_tint;
    //    退款原因选择
    @BindView(R.id.tv_dir_indent_apply_reason_sel)
    TextView tv_dir_indent_apply_reason_sel;
    //    退款金额
    @BindView(R.id.tv_dir_indent_apply_price)
    TextView tv_dir_indent_apply_price;
    //    退款说明输入
    @BindView(R.id.et_dir_indent_apply_explain)
    EditText et_dir_indent_apply_explain;
    @BindView(R.id.rv_apply_refund_img)
    RecyclerView rv_apply_refund_img;
    //    提交按钮
    @BindView(R.id.tv_submit_apply_refund)
    TextView tv_submit_apply_refund;
    @BindView(R.id.sv_layout_refund)
    NestedScrollView sv_layout_refund;
    @BindView(R.id.tv_mobile)
    EditText mTvMobile;
    @BindView(R.id.ll_mobile)
    LinearLayout ll_mobile;
    @BindView(R.id.tv_refund_tips)
    TextView mTvRefundTips;
    @BindView(R.id.ll_apply_explain)
    LinearLayout mLlApplyExplain;
    private Map<String, String> refundTypeMap = new HashMap<>();    //申请类型
    private Map<String, Map<String, String>> refundReasonMap = new HashMap<>();//申请原因
    private ImgGridRecyclerAdapter imgGridRecyclerAdapter;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private List<ImagePathBean> imagePathBeans = new ArrayList<>();
    //    已上传图片保存
    private List<String> updatedImages = new ArrayList<>();
    private final int REQUEST_PERMISSIONS = 60;
    private int maxSelImg = 5;
    private int adapterPosition;
    private MainOrderListEntity.MainOrderBean mReFundInfoBean;
    private String orderNo;
    private String refundNo;
    private String refundType;
    private String images;
    private String goods;
    private RefundInfoEntity mRefundInfoEntity;
    private AlertDialogWheel mAlertDialogWheelType;
    private AlertDialogWheel mAlertDialogWheelReason;
    private String refundReasonId;

    @Override
    protected int getContentView() {
        return R.layout.activity_direct_indent_apply_refund;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        iv_indent_search.setVisibility(View.GONE);
        tv_indent_title.setText("申请退款");
        if (getIntent().getExtras() == null) return;
        orderNo = getIntent().getStringExtra("orderNo");
        refundNo = getIntent().getStringExtra("refundNo");
        refundType = getIntent().getStringExtra("refundType");
        goods = getIntent().getStringExtra("goods");
        if (!TextUtils.isEmpty(goods)) {
            List<OrderProductNewBean> goodsBeanList = GsonUtils.fromJson(goods, new TypeToken<List<OrderProductNewBean>>() {
            }.getType());
            //初始化退款商品列表
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(this));
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
            DirectProductListAdapter directProductListAdapter = new DirectProductListAdapter(this, goodsBeanList, APPLY_REFUND_TYPE);
            communal_recycler_wrap.setAdapter(directProductListAdapter);
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
        }

        rv_apply_refund_img.setVisibility(VISIBLE);
        //        图片
        rv_apply_refund_img.setLayoutManager(new GridLayoutManager(DirectApplyRefundActivity.this, 4));
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }
        rv_apply_refund_img.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                .create());
        imgGridRecyclerAdapter = new ImgGridRecyclerAdapter(this, imagePathBeans);
        rv_apply_refund_img.setAdapter(imgGridRecyclerAdapter);
        rv_apply_refund_img.setNestedScrollingEnabled(false);
        imgGridRecyclerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.delete) {
                adapterPosition = (int) view.getTag();
                getImageFormatInstance().delImageBean(imagePathBeans, adapterPosition);
                mSelectPath.clear();
                mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
                imgGridRecyclerAdapter.notifyDataSetChanged();
            }
        });
        imgGridRecyclerAdapter.setOnItemClickListener((adapter, view, position) -> {
            //隐藏键盘
            if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                CommonUtils.hideSoftInput(DirectApplyRefundActivity.this, et_dir_indent_apply_explain);
            }
            pickImage(position);
        });
        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (height == 0) {
                ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
            }
        });
    }

    @Override
    protected void loadData() {
        getApplyRefund();
    }

    @Override
    public View getLoadView() {
        return sv_layout_refund;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getApplyRefund() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("goods", goods);
        params.put("type", RETURN_GOODS.equals(refundType) ? 2 : 1);//退货退款2 仅退款1
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_INDENT_APPLY_REFUND_NEW, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                mRefundInfoEntity = GsonUtils.fromJson(result, RefundInfoEntity.class);
                if (mRefundInfoEntity != null) {
                    if (mRefundInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        mReFundInfoBean = mRefundInfoEntity.getRefundInfo();
                        if (mReFundInfoBean != null) {
                            setRefundApplyData();
                        }
                    } else {
                        showToast(mRefundInfoEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mRefundInfoEntity);
            }

            @Override
            public void onNotNetOrException() {
                showToast(R.string.invalidData);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mRefundInfoEntity);
            }
        });
    }

    private void setRefundApplyData() {
        //退款金额
        tv_dir_indent_apply_price.setText(getStringsChNPrice(this, mReFundInfoBean.getRefundPrice()));
        //退款说明
        mTvRefundTips.setText(getStrings(mReFundInfoBean.getMsg()));
        mTvRefundTips.setVisibility(!TextUtils.isEmpty(mReFundInfoBean.getMsg()) ? VISIBLE : View.GONE);
        mLlApplyExplain.setVisibility(!TextUtils.isEmpty(mReFundInfoBean.getMsg()) ? VISIBLE : View.GONE);
        //退款类型
        refundTypeMap = mReFundInfoBean.getRefundTypeMap();
        //只有一种退款类型时默认选中
        if (refundTypeMap != null && refundTypeMap.size() == 1) {
            for (Map.Entry<String, String> entry : refundTypeMap.entrySet()) {
                refundType = entry.getKey();
                tv_dir_indent_apply_type_sel.setText(entry.getValue());
                tv_dir_indent_apply_type_sel.setSelected(true);
                break;
            }
        }
        //退款原因
        refundReasonMap = mReFundInfoBean.getRefundReasonMap();
        //联系电话
        ll_mobile.setVisibility(!TextUtils.isEmpty(mReFundInfoBean.getMobile()) ? VISIBLE : View.GONE);
        mTvMobile.setText(getStrings(mReFundInfoBean.getMobile()));
    }

    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(() -> {
            int imgLength = imagePathBeans.size() - 1;
            if (position == imgLength && DEFAULT_ADD_IMG.equals(imagePathBeans.get(imgLength).getPath())) {
                PictureSelectorUtils.getInstance()
                        .resetVariable()
                        .isCrop(false)
                        .selImageList(mSelectPath)
                        .selMaxNum(maxSelImg)
                        .imageMode(PictureConfigC.MULTIPLE)
                        .isShowGif(true)
                        .openGallery(DirectApplyRefundActivity.this);
            } else {
                Intent intent = new Intent(DirectApplyRefundActivity.this, ImagePagerActivity.class);
                intent.putExtra(ImagePagerActivity.INTENT_POSITION, position);
                List<ImageBean> imageBeanList = new ArrayList<>();
                ImageBean imageBean;
                for (String imgUrl : mSelectPath) {
                    imageBean = new ImageBean();
                    imageBean.setPicUrl(imgUrl);
                    imageBeanList.add(imageBean);
                }
                intent.putParcelableArrayListExtra(ImagePagerActivity.INTENT_IMGURLS, (ArrayList<? extends Parcelable>) imageBeanList);
                startActivity(intent);
            }
        });
        constantMethod.getPermissions(this, Permission.Group.STORAGE);
    }

    /**
     * 申诉信息 图片上传
     */
    private void submit() {
        loadHud.show();
        if (mSelectPath.size() < 1) {
            submitRefundData();
        } else {
            if (updatedImages.size() > 0) {
                setRefundImageData(updatedImages);
            } else {
                ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setUrl(DirectApplyRefundActivity.this, mSelectPath);
                imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                    @Override
                    public void finishData(@NonNull List<String> data, Handler handler) {
                        updatedImages.clear();
                        updatedImages.addAll(data);
                        // 已上传不可删除 不可更换图片
                        getImageFormatInstance().submitChangeIconStatus(imagePathBeans, false);
                        imgGridRecyclerAdapter.notifyDataSetChanged();
                        setRefundImageData(data);
                        submitRefundData();
                        handler.removeCallbacksAndMessages(null);
                    }

                    @Override
                    public void finishError(String error) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        showToast("网络异常");
                    }

                    @Override
                    public void finishSingleImg(String singleImg, Handler handler) {
                    }
                });
            }
        }
    }

    private void setRefundImageData(@NonNull List<String> data) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                stringBuffer.append(data.get(i));
            } else {
                stringBuffer.append(",").append(data.get(i));
            }
        }

        images = stringBuffer.toString();
    }

    /**
     * 修改/提交退款申请
     */
    private void submitRefundData() {
        String refundDetail = et_dir_indent_apply_explain.getText().toString().trim();//申请说明
        String mobile = mTvMobile.getText().toString().trim();//联系电话
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(refundNo)) {
            params.put("refundNo", refundNo);
        }
        if (!TextUtils.isEmpty(orderNo)) {
            params.put("orderNo", orderNo);
        }
        params.put("refundType", refundType);
        params.put("refundReasonId", refundReasonId);
        params.put("refundDetail", refundDetail);
        params.put("images", images);
        params.put("mobile", mobile);
        params.put("goods", goods);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, !TextUtils.isEmpty(refundNo) ? Url.Q_INDENT_CHANGE_REFUND_SUB_NEW : Url.Q_INDENT_APPLY_REFUND_SUB_NEW
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        dismissLoadhud(getActivity());

                        RequestStatus requestInfo = GsonUtils.fromJson(result, RequestStatus.class);
                        if (requestInfo != null) {
                            if (requestInfo.getCode().equals(SUCCESS_CODE)) {
                                //申请退款成功跳转退款详情
                                if (TextUtils.isEmpty(refundNo)) {
                                    skipRefundDetail(getActivity(), requestInfo.getRefundNo());
                                }
                                finish();
                            } else {
                                showToastRequestMsg(requestInfo);
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        showToast(R.string.do_failed);
                        dismissLoadhud(getActivity());
                    }
                });
    }


    @OnClick({R.id.tv_indent_back, R.id.iv_indent_service, R.id.tv_submit_apply_refund, R.id.ll_select_apply_type, R.id.ll_select_apply_reason})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_indent_back:
                finish();
                break;
            case R.id.iv_indent_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(this, "申请退款");
                break;
            //提交退款申请
            case R.id.tv_submit_apply_refund:
                if (TextUtils.isEmpty(refundType)) {
                    showToast("请选择申请类型");
                    return;
                }

                if (TextUtils.isEmpty(refundReasonId)) {
                    showToast("请选择申请原因");
                    return;
                }

                String mobile = mTvMobile.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    showToast("请输入联系电话");
                    return;
                }

                submit();
                break;
            //选择退款类型
            case R.id.ll_select_apply_type:
                if (mAlertDialogWheelType == null) {
                    mAlertDialogWheelType = new AlertDialogWheel(this);
                    mAlertDialogWheelType.setConfirmListener((key, value) -> {
                        refundType = key;
                        tv_dir_indent_apply_type_sel.setText(value);
                        tv_dir_indent_apply_type_sel.setSelected(true);
                    });
                }
                mAlertDialogWheelType.show(Gravity.BOTTOM);
                mAlertDialogWheelType.updateView(refundTypeMap);
                break;
            //选择退款原因
            case R.id.ll_select_apply_reason:
                if (TextUtils.isEmpty(refundType)) {
                    showToast("请先选择申请类型");
                    return;
                }
                if (mAlertDialogWheelReason == null) {
                    mAlertDialogWheelReason = new AlertDialogWheel(this);
                    mAlertDialogWheelReason.setConfirmListener((key, value) -> {
                        refundReasonId = key;
                        tv_dir_indent_apply_reason_sel.setText(value);
                        tv_dir_indent_apply_reason_sel.setSelected(true);
                    });
                }
                mAlertDialogWheelReason.show(Gravity.BOTTOM);
                mAlertDialogWheelReason.updateView(refundReasonMap.get(refundType));
                break;
        }
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
        } else if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
            List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
            if (localMediaList != null && localMediaList.size() > 0) {
                imagePathBeans.clear();
                for (LocalMediaC localMedia : localMediaList) {
                    if (!TextUtils.isEmpty(localMedia.getPath())) {
                        imagePathBeans.add(new ImagePathBean(localMedia.getPath(), true));
                    }
                }
                if (imagePathBeans.size() < maxSelImg) {
                    imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
                }
                mSelectPath.clear();
                mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
                imgGridRecyclerAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_PERMISSIONS) {
            showToast("请到应用管理授予权限");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        if (mAlertDialogWheelReason != null) {
            mAlertDialogWheelReason.dismiss();
        }
        if (mAlertDialogWheelType != null) {
            mAlertDialogWheelType.dismiss();
        }
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
        if (v != null && (v instanceof EditText)) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
