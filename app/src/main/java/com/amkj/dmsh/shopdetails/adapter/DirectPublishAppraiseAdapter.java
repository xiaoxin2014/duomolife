package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisePassBean;
import com.amkj.dmsh.utils.CommonUtils;
import com.amkj.dmsh.utils.TextWatchListener;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.pictureselector.PictureSelectorUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.config.PictureConfigC;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.yanzhenjie.permission.Permission;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.release.adapter.ImgGridRecyclerAdapter.DEFAULT_ADD_IMG;

;

/**
 * Created by atd48 on 2016/10/28.
 */
public class DirectPublishAppraiseAdapter extends BaseQuickAdapter<DirectAppraisePassBean, BaseViewHolderHelperAppraise> {
    private final Context context;
    private final int screenWidth;
    private ArrayList<String> mDataPath = new ArrayList<>();
    private ArrayList<String> selectedPath = new ArrayList<>();
    //    商品评论的全部图片
    private List<ArrayList<String>> mDataPathAll = new ArrayList<>();
    private List<DirectAppraisePassBean> directAppraisePassList;
    private int maxSelImg = 5;

    public DirectPublishAppraiseAdapter(Context context, List<DirectAppraisePassBean> directAppraisePassList) {
        super(R.layout.adapter_layout_public_appraise, directAppraisePassList);
        this.directAppraisePassList = directAppraisePassList;
        this.context = context;
        BaseApplication app = (BaseApplication) ((Activity) context).getApplication();
        screenWidth = app.getScreenWidth();
    }

    @Override
    protected void convert(final BaseViewHolderHelperAppraise helper, DirectAppraisePassBean directPassBean) {
        RatingBar ratingBar_direct_eva_count = helper.getView(R.id.ratingBar_direct_eva_count);
        RecyclerView rv_product_eva = helper.getView(R.id.rv_product_eva);
        rv_product_eva.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_img_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        final int adapterPosition = helper.getAdapterPosition();
        helper.et_indent_appraise_input.setTag(adapterPosition);
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_appraise_product_img), directPassBean.getPath());
        String[] path = directPassBean.getImagePaths().split(",");
        mDataPath = new ArrayList<>();
        for (int i = 0; i < path.length; i++) {
            mDataPath.add(path[i]);
        }
        if (mDataPathAll.size() - 1 >= helper.getAdapterPosition()) {
            mDataPathAll.set(adapterPosition, mDataPath);
        } else {
            mDataPathAll.add(mDataPath);
        }
        if (screenWidth >= AutoUtils.getPercentWidthSizeBigger(600)) {
            rv_product_eva.setLayoutManager(new GridLayoutManager(context, 5));
        } else {
            rv_product_eva.setLayoutManager(new GridLayoutManager(context, 3));
        }
        rv_product_eva.setNestedScrollingEnabled(false);
        final ImgGridRecyclerEvaAdapter imgGridRecyclerEvaAdapter = new ImgGridRecyclerEvaAdapter(context, mDataPathAll.get(adapterPosition), adapterPosition);
        rv_product_eva.setAdapter(imgGridRecyclerEvaAdapter);
        imgGridRecyclerEvaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.delete) {
                    int adapterPosition = (int) view.getTag();
                    int evaPosition = (int) view.getTag(R.id.img_eva_list);
                    ArrayList<String> images = mDataPathAll.get(evaPosition);
                    if (images.size() > images.size() - 1 && !images.get(images.size() - 1).equals(DEFAULT_ADD_IMG)) {
                        images.set(images.size() - 1, DEFAULT_ADD_IMG);
                    } else {
                        images.remove(adapterPosition);
                        if (!images.get(images.size() - 1).equals(DEFAULT_ADD_IMG)) {
                            images.add(DEFAULT_ADD_IMG);
                        }
                    }
                    DirectAppraisePassBean directAppraisePassBean = directAppraisePassList.get(evaPosition);
                    StringBuffer spiltPath = new StringBuffer();
                    for (int i = 0; i < images.size(); i++) {
                        if (i == 0) {
                            spiltPath.append(images.get(i));
                        } else {
                            spiltPath.append("," + images.get(i));
                        }
                    }
                    directAppraisePassBean.setImages(spiltPath.toString());
                    directAppraisePassList.set(evaPosition, directAppraisePassBean);
                    EventBus.getDefault().post(new EventMessage("appraiseDate", directAppraisePassList));
                    adapter.setNewData(images);
                }
            }
        });
        imgGridRecyclerEvaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                隐藏键盘
                int evaItemPosition = (int) view.getTag();
                ArrayList<String> images = mDataPathAll.get(evaItemPosition);
                if (((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                    CommonUtils.hideSoftInput(context, helper.et_indent_appraise_input);
                }
                pickImage(images, position, evaItemPosition);
            }
        });
        if (!TextUtils.isEmpty(directPassBean.getContent())) {
            helper.et_indent_appraise_input.setText(directPassBean.getContent());
            helper.et_indent_appraise_input.setSelection(directPassBean.getContent().length());
        }
        ratingBar_direct_eva_count.setRating(directPassBean.getStar() < 1 ? 5 : directPassBean.getStar());
        ratingBar_direct_eva_count.setTag(helper.getLayoutPosition());
        ratingBar_direct_eva_count.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int position = (Integer) ratingBar.getTag();
                DirectAppraisePassBean directAppraisePassBean = directAppraisePassList.get(position);
                directAppraisePassBean.setStar((int) rating);
                directAppraisePassList.set(position, directAppraisePassBean);
                EventBus.getDefault().post(new EventMessage("appraiseDate", directAppraisePassList));
            }
        });
        helper.et_indent_appraise_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                }
            }
        });
        helper.et_indent_appraise_input.addTextChangedListener(new TextWatchListener() {
            @Override
            public void afterTextChanged(Editable s) {
                int position = (Integer) helper.et_indent_appraise_input.getTag();
                DirectAppraisePassBean directAppraisePassBean = directAppraisePassList.get(position);
                directAppraisePassBean.setContent(s.toString());
                directAppraisePassList.set(position, directAppraisePassBean);
                EventBus.getDefault().post(new EventMessage("appraiseDate", directAppraisePassList));
            }
        });
    }

    private void pickImage(final ArrayList<String> images, final int position, final int evaItemPosition) {
        ConstantMethod constantMethod = new ConstantMethod();
        constantMethod.setOnGetPermissionsSuccess(new ConstantMethod.OnGetPermissionsSuccessListener() {
            @Override
            public void getPermissionsSuccess() {
                int imgLength = images.size() - 1;
                selectedPath = new ArrayList<>();
                selectedPath.addAll(images);
                if (selectedPath.size() > 0 && selectedPath.get(selectedPath.size() - 1).equals(DEFAULT_ADD_IMG)) {
                    selectedPath.remove(selectedPath.size() - 1);
                }
                if (position == imgLength && images.get(imgLength).equals(DEFAULT_ADD_IMG)) {
                    PictureSelectorUtils.getInstance()
                            .resetVariable()
                            .isCrop(false)
                            .imageMode(PictureConfigC.MULTIPLE)
                            .selImageList(selectedPath)
                            .selMaxNum(maxSelImg)
                            .isShowGif(true)
                            .appraiseCurrentEva(evaItemPosition)
                            .openGallery((Activity) context);
                } else {
                    Intent intent = new Intent(context, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.INTENT_POSITION, position);List<ImageBean> imageBeanList = new ArrayList<>();
                    ImageBean imageBean;
                    for (String imgUrl:selectedPath) {
                        imageBean = new ImageBean();
                        imageBean.setPicUrl(imgUrl);
                        imageBeanList.add(imageBean);
                    }
                    intent.putParcelableArrayListExtra(ImagePagerActivity.INTENT_IMGURLS, (ArrayList<? extends Parcelable>) imageBeanList);
                    context.startActivity(intent);
                }
            }
        });
        constantMethod.getPermissions(context, Permission.Group.STORAGE);
    }
}
