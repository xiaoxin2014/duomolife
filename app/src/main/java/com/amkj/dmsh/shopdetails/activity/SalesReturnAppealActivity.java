package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter;
import com.amkj.dmsh.release.bean.ImagePathBean;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.ImgUrlHelp;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfigC;
import com.luck.picture.lib.entity.LocalMediaC;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.utils.ImageFormatUtils.getImageFormatInstance;

;


/**
 * Created by atd48 on 2016/8/25.
 * 退货申诉
 */
public class SalesReturnAppealActivity extends BaseActivity {
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
    private List<ImagePathBean> imagePathBeans = new ArrayList<>();
    //图片路径
    private ArrayList<String> mSelectPath = new ArrayList<>();
    //    已上传图片保存
    private List<String> updatedImages = new ArrayList<>();
    private String orderNo;
    private GoodsBean goodsBean;
    private int adapterPosition;
    private final int REQUEST_PERMISSIONS = 60;
    private int maxSelImg = 5;
    private AlertDialogHelper commitDialogHelper;
    private AlertDialogHelper confirmDialogHelper;

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
        if (imagePathBeans != null) {
            imagePathBeans.clear();
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        if (app.getScreenWidth() >= AutoSizeUtils.mm2px(mAppContext, 600)) {
            rv_sale_return_img.setLayoutManager(new GridLayoutManager(SalesReturnAppealActivity.this, 5));
        } else {
            rv_sale_return_img.setLayoutManager(new GridLayoutManager(SalesReturnAppealActivity.this, 3));
        }
        if (imagePathBeans.size() < 1) {
            imagePathBeans.add(getImageFormatInstance().getDefaultAddImage());
        }
        rv_sale_return_img.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)


                .create());
        imgGridRecyclerAdapter = new ImgGridRecyclerAdapter(this, imagePathBeans);
        rv_sale_return_img.setAdapter(imgGridRecyclerAdapter);
        imgGridRecyclerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    adapterPosition = (int) view.getTag();
                    getImageFormatInstance().delImageBean(imagePathBeans, adapterPosition);
                    mSelectPath.clear();
                    mSelectPath.addAll(getImageFormatInstance().formatStringPathRemoveDefault(imagePathBeans));
                    imgGridRecyclerAdapter.notifyDataSetChanged();
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
            showToast(this, "请到应用管理授予权限");
        }
    }


    private void pickImage(final int position) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                int imgLength = imagePathBeans.size() - 1;
                if (position == imgLength
                        && DEFAULT_ADD_IMG.equals(imagePathBeans.get(imgLength).getPath())) {
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
        } else if (confirmDialogHelper == null || !confirmDialogHelper.isShowing()) {
            if (confirmDialogHelper == null) {
                confirmDialogHelper = new AlertDialogHelper(SalesReturnAppealActivity.this);
                confirmDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER)
                        .setMsg("申诉未完成，确定要离开吗？").setConfirmText("确定").setCancelText("取消")
                        .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                confirmDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        finish();
                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
            confirmDialogHelper.show();
        } else {
            confirmDialogHelper.dismiss();
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
                //                上传图片
                if (updatedImages.size() > 0) {
                    setSalesReturnImageData(updatedImages, directAppraisePassBean);
                } else {
                    ImgUrlHelp imgUrlHelp = new ImgUrlHelp();
                    imgUrlHelp.setUrl(SalesReturnAppealActivity.this, mSelectPath);
                    imgUrlHelp.setOnFinishListener(new ImgUrlHelp.OnFinishDataListener() {
                        @Override
                        public void finishData(List<String> data, Handler handler) {
                            setSalesReturnImageData(data, directAppraisePassBean);
                            //                            已上传不可删除 不可更换图片
                            getImageFormatInstance().submitChangeIconStatus(imagePathBeans, false);
                            imgGridRecyclerAdapter.notifyDataSetChanged();
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
                }
            } else {
                submit(directAppraisePassBean);
            }
        } else {
            showToast(this, "请填写申诉原因和问题描述，已方便我们尽快处理");
        }
    }

    private void setSalesReturnImageData(List<String> data, DirectAppraisePassBean directAppraisePassBean) {
        StringBuffer imgCountAppend = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                imgCountAppend.append(data.get(i));
            } else {
                imgCountAppend.append("," + data.get(i));
            }
        }
        directAppraisePassBean.setImages(imgCountAppend.toString());
    }


    private void submit(DirectAppraisePassBean directAppraisePassBean) {
        List<DirectAppraisePassBean> list = new ArrayList<>();
        list.add(directAppraisePassBean);
        String url = Url.BASE_URL + Url.Q_INDENT_REFUND;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("userId", userId);
        params.put("goods", new Gson().toJson(list));
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus indentInfo = gson.fromJson(result, RequestStatus.class);
                if (indentInfo != null) {
                    if (indentInfo.getCode().equals(SUCCESS_CODE)) {
                        if (commitDialogHelper == null) {
                            commitDialogHelper = new AlertDialogHelper(SalesReturnAppealActivity.this);
                            commitDialogHelper.setTitle("提交成功").setMsgTextGravity(Gravity.CENTER)
                                    .setMsg("我们会尽快为你处理").setConfirmText("确定").setSingleButton(true).setCancelable(false)
                                    .setCancelTextColor(getResources().getColor(R.color.text_login_gray_s));
                            commitDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                                @Override
                                public void confirm() {
                                    finish();
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }
                        commitDialogHelper.show();
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
            public void onNotNetOrException() {
                header_shared.setEnabled(true);
                header_shared.setText("提交");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(SalesReturnAppealActivity.this, R.string.invalidData);
            }

            @Override
            public void netClose() {
                showToast(SalesReturnAppealActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commitDialogHelper != null && commitDialogHelper.isShowing()) {
            commitDialogHelper.dismiss();
        }
        if (confirmDialogHelper != null && confirmDialogHelper.isShowing()) {
            confirmDialogHelper.dismiss();
        }
    }
}
