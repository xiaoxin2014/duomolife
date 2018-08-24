package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.shopdetails.bean.DirectAppraisedEntity.DirectAppraisedBean.OrderListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.MultiDirectImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;

;

/**
 * Created by atd48 on 2016/11/1.
 */
public class DoMoIndentAppraisedAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolderHelper> {
    private final Context context;

    public DoMoIndentAppraisedAdapter(Context context, List<OrderListBean> orderListBeanList) {
        super(R.layout.adapter_direct_appraise, orderListBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, OrderListBean orderListBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_appraise_goods), orderListBean.getGoods().getPicUrl());
        if (!TextUtils.isEmpty(orderListBean.getImages())) {
            String[] images = orderListBean.getImages().split(",");
            typeHandlers((LinearLayout) helper.getView(R.id.viewStub_direct_appraise), images);
        } else {
            helper.setGone(R.id.viewStub_direct_appraise, false);
        }
        helper.setRating(R.id.ratingBar_direct_count, orderListBean.getStar(), 5)
                .setText(R.id.tv_direct_delivered_appraise, "评价状态：" + (orderListBean.getStatus() < 1 ? "审核中" : orderListBean.getStatus() > 1 ? "未通过" : "已发布"))
                .setText(R.id.tv_direct_appraise_goods_name, getStrings(orderListBean.getGoods().getName()));
        if (!TextUtils.isEmpty(orderListBean.getContent())) {
            helper.setText(R.id.tv_direct_evaluation, orderListBean.getContent());
        } else {
            helper.setGone(R.id.tv_direct_evaluation, false);
        }
    }

    private void typeHandlers(LinearLayout viewStub, String[] images) {
        if (viewStub.getChildCount() != 0) {
            viewStub.removeAllViews();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.layout_direct_evaluation_image, viewStub, false);
        viewStub.addView(view);
        MultiDirectImageView multiImageView = (MultiDirectImageView) view.findViewById(R.id.multiImagView);
        final List<String> thumbnailPhotos = new ArrayList<>();
        final List<String> originalPhotos = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            thumbnailPhotos.add(images[i] + Url.IMAGE_RESIZE + "/resize,w_" + 150 + Url.IMAGE_RESIZE_ORI);
            originalPhotos.add(images[i]);
        }
        if (thumbnailPhotos != null && thumbnailPhotos.size() > 0) {
            multiImageView.setVisibility(View.VISIBLE);
            multiImageView.setList(thumbnailPhotos);
            multiImageView.setOnItemClickListener(new MultiDirectImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //imagesize是作为loading时的图片size
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                    ImageBean imageBean = null;
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    for (String picUrl:originalPhotos) {
                        imageBean = new ImageBean();
                        imageBean.setPicUrl(picUrl);
                        imageBeanList.add(imageBean);
                    }
                    ImagePagerActivity.startImagePagerActivity(context,IMAGE_DEF, imageBeanList, position, imageSize);
                }
            });
        } else {
            multiImageView.setVisibility(View.GONE);
        }
    }
}
