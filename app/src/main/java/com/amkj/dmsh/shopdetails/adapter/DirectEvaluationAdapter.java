package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipUserCenter;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.dao.SoftApiDao.favorGoodsComment;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;

/**
 * Created by xiaoxin on 2020/1/10
 * Version:v4.4.1
 * ClassDescription :新版评论适配器
 */
public class DirectEvaluationAdapter extends BaseQuickAdapter<GoodsCommentBean, BaseViewHolder> {
    private Activity context;

    public DirectEvaluationAdapter(Activity context, List<GoodsCommentBean> goodsCommentBeanList) {
        super(R.layout.adapter_direct_evaluation2, goodsCommentBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsCommentBean goodsCommentBean) {
        TextView tv_eva_count = helper.getView(R.id.tv_eva_count);
        ImageView ivAvatar = helper.getView(R.id.img_direct_avatar);
        MaterialRatingBar ratingBar = helper.getView(R.id.ratingBar_direct_count);
        RecyclerView rvEva = helper.getView(R.id.rv_pro_eva);
        LinearLayout llEvaluate = helper.getView(R.id.ll_evaluate);
        EmojiTextView tvEvaluate = helper.getView(R.id.tv_direct_evaluation);
        ViewGroup.LayoutParams itemLayoutParams = helper.itemView.getLayoutParams();
        String images = goodsCommentBean.getImages();
        int length = 0;
        if (!TextUtils.isEmpty(images)) {
            length = images.split(",").length > 3 ? 3 : images.split(",").length;//最多小显示三张图片
        }
        tvEvaluate.setMaxLines(length > 0 ? 3 : 8);
        //设置item宽度
        if (length > 0) {
            rvEva.setVisibility(View.VISIBLE);
            rvEva.setLayoutManager(new GridLayoutManager(context, length));
            if (length == 1) {
                itemLayoutParams.width = AutoSizeUtils.mm2px(mAppContext, 310);
            } else if (length == 2) {
                itemLayoutParams.width = AutoSizeUtils.mm2px(mAppContext, 390);
            } else if (length == 3) {
                itemLayoutParams.width = AutoSizeUtils.mm2px(mAppContext, 562);
            }

            setEvaImages(rvEva, images, length);
        } else {
            rvEva.setVisibility(View.GONE);
            itemLayoutParams.width = AutoSizeUtils.mm2px(mAppContext, 310);
        }
        helper.itemView.setLayoutParams(itemLayoutParams);

        //设置间距
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llEvaluate.getLayoutParams();
        layoutParams.rightMargin = (length == 0 || length == 1) ? 0 : AutoSizeUtils.mm2px(mAppContext, 20);
        llEvaluate.setLayoutParams(layoutParams);

        GlideImageLoaderUtil.loadRoundImg(context, ivAvatar, goodsCommentBean.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 64),R.drawable.default_ava_img);
        helper.setGone(R.id.tv_direct_evaluation, length != 1)//除单张图外都要显示文本
                .setText(R.id.tv_direct_evaluation, goodsCommentBean.getContent())
                .setText(R.id.tv_eva_count, goodsCommentBean.getLikeNum() > 0 ? goodsCommentBean.getLikeNum() + "" : "赞")
                .setText(R.id.tv_eva_user_name, getStrings(goodsCommentBean.getNickname()));
        ratingBar.setVisibility(goodsCommentBean.getStar() < 1 ? View.GONE : View.VISIBLE);
        ratingBar.setNumStars(goodsCommentBean.getStar());
        ratingBar.setMax(goodsCommentBean.getStar());
        tv_eva_count.setSelected(goodsCommentBean.isFavor());

        //点击跳转用户主页
        ivAvatar.setOnClickListener(v -> skipUserCenter(context, goodsCommentBean.getUserId()));
        //商品评论点赞
        tv_eva_count.setOnClickListener(v -> favorGoodsComment(context, goodsCommentBean, tv_eva_count));
//        //点击跳转帖子详情
//        helper.itemView.setOnClickListener(v -> ConstantMethod.skipPostDetail(context, String.valueOf(goodsCommentBean.getId()), 2));
    }

    private void setEvaImages(RecyclerView rvProductEva, String images, int length) {
        if (!TextUtils.isEmpty(images)) {
            final List<PictureBean> pictureBeanList = new ArrayList<>();
            String[] evaImages = images.split(",");
            final List<String> originalPhotos = new ArrayList<>(Arrays.asList(evaImages));
            for (int i = 0; i < length; i++) {
                PictureBean pictureBean = new PictureBean();
                pictureBean.setItemType(length == 1 ? TYPE_1 : TYPE_2);
                pictureBean.setIndex(i);
                pictureBean.setPath(evaImages[i]);
                pictureBean.setOriginalList(originalPhotos);
                pictureBeanList.add(pictureBean);
            }
            EvaluateImageListAdapter evaluateAdapter = new EvaluateImageListAdapter(context, pictureBeanList);
            rvProductEva.setAdapter(evaluateAdapter);
            evaluateAdapter.setNewData(pictureBeanList);
            evaluateAdapter.setOnItemClickListener((adapter, view, position) -> {
                PictureBean pictureBean1 = (PictureBean) view.getTag(R.id.iv_tag);
                if (pictureBean1 != null) {
                    ImageBean imageBean = null;
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    for (String picUrl : pictureBean1.getOriginalList()) {
                        imageBean = new ImageBean();
                        imageBean.setPicUrl(picUrl);
                        imageBeanList.add(imageBean);
                    }
                    ImagePagerActivity.startImagePagerActivity(context, IMAGE_DEF, imageBeanList
                            , pictureBean1.getIndex() < pictureBean1.getOriginalList().size() ? pictureBean1.getIndex() : 0, null);
                }
            });
        } else {
            rvProductEva.setVisibility(View.GONE);
        }
    }
}