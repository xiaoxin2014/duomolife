package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.yanzhenjie.permission.Permission;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter.DEFAULT_ADD_IMG;

;


/**
 * Created by atd48 on 2016/8/25.
 * 退货申诉
 */
public class SalesReturnAppealActivity extends BaseActivity implements OnAlertItemClickListener {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    //产品图片
    @BindView(R.id.iv_direct_indent_pro)
    ImageView img_direct_indent_product;
    //产品名字
    @BindView(R.id.tv_direct_indent_pro_name)
    TextView tv_direct_indent_product_name;
    //产品价格
    @BindView(R.id.tv_direct_indent_pro_price)
    TextView tv_direct_indent_product_price;
    //产品数量
    @BindView(R.id.tv_goods_count)
    TextView tv_goods_count;
    //申诉原因
    @BindView(R.id.et_appeal_reason)
    EditText et_appeal_reason;
    //问题描述
    @BindView(R.id.et_appeal_question_description)
    EditText et_appeal_question_description;
    @BindView(R.id.rv_sale_return_img)
    RecyclerView rv_sale_return_img;
    //    进度条
    @BindView(R.id.rel_progressBar)
    RelativeLayout progressBar;
    private ImgGridRecyclerAdapter imgGridRecyclerAdapter;
    private ArrayList<String> dataPath = new ArrayList<>();
    //图片路径
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private AlertView dialog;
    private String orderNo;
    private AlertView commitDialog;
    private GoodsBean goodsBean;
    private int adapterPosition;
    private final int REQUEST_PERMISSIONS = 60;
    private int maxSelImg = 5;

    @Override
    protected int getContentView() {
        return R.layout.activity_sales_return_appeal;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("退货申诉");
        header_shared.setCompoundDrawables(null, null, null, null);
        header_shared.setTextColor(getResources().getColor(R.color.textColor_blue));
        header_shared.setText("提交");
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        goodsBean = intent.getParcelableExtra("goodsBean");
        if (dataPath != null) {
            dataPath.clear();
            dataPath.add(ConstantVariable.DEFAULT_ADD_IMG);
        }
        BaseApplication app = (BaseApplication) getApplication();
        if (app.getScreenWidth() >= AutoUtils.getPercentWidthSizeBigger(600)) {
            rv_sale_return_img.setLayoutManager(new GridLayoutManager(SalesReturnAppealActivity.this, 5));
        } else {
            rv_sale_return_img.setLayoutManager(new GridLayoutManager(SalesReturnAppealActivity.this, 3));
        }
        if (dataPath.size() < 1) {
            dataPath.add(DEFAULT_ADD_IMG);
        }
        rv_sale_return_img.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        imgGridRecyclerAdapter = new ImgGridRecyclerAdapter(this, dataPath);
        rv_sale_return_img.setAdapter(imgGridRecyclerAdapter);
        imgGridRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    adapterPosition = (int) view.getTag();
                    if (dataPath.size() > dataPath.size() - 1 && !dataPath.get(dataPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                        dataPath.set(dataPath.size() - 1, DEFAULT_ADD_IMG);
                    } else {
                        dataPath.remove(adapterPosition);
                        if (!dataPath.get(dataPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                            dataPath.add(DEFAULT_ADD_IMG);
                        }
                    }
                    mSelectPath = new ArrayList();
                    mSelectPath.addAll(dataPath);
                    if (mSelectPath.size() > 0 && mSelectPath.get(mSelectPath.size() - 1).equals(DEFAULT_ADD_IMG))
                        mSelectPath.remove(mSelectPath.size() - 1);
                    imgGridRecyclerAdapter.setNewData(dataPath);
                }
            }
        });
        imgGridRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                隐藏键盘
                if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                    CommonUtils.hideSoftInput(SalesReturnAppealActivity.this, et_appeal_question_description);
                }
                pickImage(position);
            }
        });
        setData();
    }

    private void setData() {
        if (goodsBean != null) {
            GlideImageLoaderUtil.loadCenterCrop(SalesReturnAppealActivity.this, img_direct_indent_product, goodsBean.getPicUrl());
            tv_direct_indent_product_name.setText(getStrings(goodsBean.getName()));
            tv_direct_indent_product_price.setText("￥" + goodsBean.getPrice());
            tv_goods_count.setText("x" + goodsBean.getCount());
        }
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfigC.CHOOSE_REQUEST) {
            List<LocalMediaC> localMediaList = PictureSelector.obtainMultipleResult(data);
            if (localMediaList != null && localMediaList.size() > 0) {
                dataPath.clear();
                for (LocalMediaC localMedia : localMediaList) {
                    if (!TextUtils.isEmpty(localMedia.getPath())) {
                        dataPath.add(localMedia.getPath());
                    }
                }
                dataPath.remove(ConstantVariable.DEFAULT_ADD_IMG);
                if (dataPath.size() < maxSelImg) {
                    dataPath.add(DEFAULT_ADD_IMG);
                }
                mSelectPath.clear();
                mSelectPath.addAll(dataPath);
                if (mSelectPath.size() > 0 && mSelectPath.get(mSelectPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                    mSelectPath.remove(mSelectPath.size() - 1);
                }
                imgGridRecyclerAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_PERMISSIONS) {
            showToast(this, "请到应用管理授予权限");
            return;
        }
    }


    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                int imgLength = dataPath.size() - 1;
                if (position == imgLength && dataPath.get(imgLength).equals(DEFAULT_ADD_IMG)) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(false)
                            .selImageList(mSelectPath)
                            .selMaxNum(maxSelImg)
                            .imageMode(PictureConfigC.MULTIPLE)
                            .isShowGif(true)
                            .openGallery(SalesReturnAppealActivity.this);
                } else {
                    Intent intent = new Intent(SalesReturnAppealActivity.this, ImagePagerActivity.class);
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
            }
        });
        constantMethod.getPermissions(this, Permission.Group.STORAGE);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(et_appeal_reason.getWindowToken(), 0); //强制隐藏键盘)
        }
        if (et_appeal_reason.getText().toString().trim().length() < 1 && imgGridRecyclerAdapter.getItemCount() < 2
                && et_appeal_question_description.getText().toString().trim().length() < 1) {
            finish();
        } else if (dialog == null || !dialog.isShowing()) {
            //弹窗
            AlertSettingBean alertSettingBean = new AlertSettingBean();
            AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
            alertData.setCancelStr("取消");
            alertData.setDetermineStr("确定");
            alertData.setFirstDet(true);
            alertData.setMsg("申诉未完成，确定要离开吗？");
            alertSettingBean.setStyle(AlertView.Style.Alert);
            alertSettingBean.setAlertData(alertData);
            dialog = new AlertView(alertSettingBean, this, SalesReturnAppealActivity.this);
            dialog.setCancelable(true);
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    @OnClick(R.id.tv_header_shared)
    void sendAppeal(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(et_appeal_reason.getWindowToken(), 0); //强制隐藏键盘)
        }
        if (et_appeal_reason.getText().toString().trim().length() > 0
                && et_appeal_question_description.getText().toString().trim().length() > 0) {
            header_shared.setText("提交中…");
            header_shared.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            final DirectAppraisePassBean directAppraisePassBean = new DirectAppraisePassBean();
            directAppraisePassBean.setId(goodsBean.getId() + "");
            directAppraisePassBean.setOrderProductId(goodsBean.getOrderProductId());
            directAppraisePassBean.setCount(goodsBean.getCount() + "");
            directAppraisePassBean.setReason(et_appeal_reason.getText().toString().trim());
            directAppraisePassBean.setContent(et_appeal_question_description.getText().toString().trim());
            if (mSelectPath.size() > 0) {
                final StringBuffer imgCountAppend = new StringBuffer();
                //                上传图片
                ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                imgUrlHelp.setUrl(SalesReturnAppealActivity.this, mSelectPath);
                imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                    @Override
                    public void finishData(List<String> data, Handler handler) {
                        for (int i = 0; i < data.size(); i++) {
                            if (i == 0) {
                                imgCountAppend.append(data.get(i));
                            } else {
                                imgCountAppend.append("," + data.get(i));
                            }
                        }
                        directAppraisePassBean.setImages(imgCountAppend.toString());
                        submit(directAppraisePassBean);
                        handler.removeCallbacksAndMessages(null);
                    }


                    @Override
                    public void finishError(String error) {
                        progressBar.setVisibility(View.GONE);
                        header_shared.setText("提交");
                        header_shared.setEnabled(true);
                        showToast(SalesReturnAppealActivity.this, "网络异常");
                    }

                    @Override
                    public void finishSingleImg(String singleImg, Handler handler) {
                    }
                });
            } else {
                submit(directAppraisePassBean);
            }
        } else {
            showToast(this, "请填写申诉原因和问题描述，已方便我们尽快处理");
        }
    }


    private void submit(DirectAppraisePassBean directAppraisePassBean) {
        List<DirectAppraisePassBean> list = new ArrayList<>();
        list.add(directAppraisePassBean);
        String url = Url.BASE_URL + Url.Q_INDENT_REFUND;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        params.put("goods", new Gson().toJson(list));
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus indentInfo = gson.fromJson(result, RequestStatus.class);
                if (indentInfo != null) {
                    if (indentInfo.getCode().equals("01")) {
                        AlertSettingBean alertSettingBean = new AlertSettingBean();
                        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                        alertData.setCancelStr("取消");
                        alertData.setDetermineStr("确定");
                        alertData.setFirstDet(true);
                        alertData.setTitle("提交成功");
                        alertData.setMsg("我们会尽快为你处理");
                        alertSettingBean.setStyle(AlertView.Style.Alert);
                        alertSettingBean.setAlertData(alertData);
                        commitDialog = new AlertView(alertSettingBean, SalesReturnAppealActivity.this, SalesReturnAppealActivity.this);
                        commitDialog.show();
                        commitDialog.setCancelable(false);
                    } else {
                        showToast(SalesReturnAppealActivity.this, indentInfo.getResult() != null ?
                                indentInfo.getResult().getMsg() : indentInfo.getMsg());
                    }
                    header_shared.setEnabled(true);
                    header_shared.setText("提交");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                header_shared.setEnabled(true);
                header_shared.setText("提交");
                progressBar.setVisibility(View.GONE);
                showToast(SalesReturnAppealActivity.this, R.string.invalidData);
            }
        });
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == dialog && position != AlertView.CANCELPOSITION) {
            finish();
        } else if (o == commitDialog && position == AlertView.CANCELPOSITION) {
            finish();
        }
    }
}
