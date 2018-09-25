package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.find.adapter.FindImageListAdapter;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import emojicon.EmojiconTextView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_3;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;

/**
 * Created by atd48 on 2016/8/15.
 */
public class DirectEvaluationAdapter extends BaseMultiItemQuickAdapter<GoodsCommentBean, DirectEvaluationAdapter.ProductEvaViewHolder> {
    private final Context context;
    //    private final static String OSS_URL = "http://img.domolife.cn";

    public DirectEvaluationAdapter(Context context, List<GoodsCommentBean> goodsCommentBeanList) {
        super(goodsCommentBeanList);
        this.context = context;
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_direct_evaluation);
        addItemType(ConstantVariable.TYPE_1, R.layout.communal_comment_not);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
    }

    @Override
    protected void convert(ProductEvaViewHolder helper, GoodsCommentBean goodsCommentBean) {
        switch (helper.getItemViewType()) {
            case ConstantVariable.TYPE_0:
                LinearLayout ll_eva_comment_reply = helper.getView(R.id.ll_eva_comment_reply);
                TextView tv_direct_evaluation = helper.getView(R.id.tv_direct_evaluation);
                TextView tv_eva_count = helper.getView(R.id.tv_eva_count);
                MaterialRatingBar ratingBar = helper.getView(R.id.ratingBar_direct_count);
                EmojiconTextView emo_direct_eva_reply = helper.getView(R.id.emo_direct_eva_reply);
                if (!TextUtils.isEmpty(goodsCommentBean.getImages())) {
                    helper.rv_pro_eva.setVisibility(View.VISIBLE);
                    setEvaImages(helper.rv_pro_eva, goodsCommentBean.getImages());
                } else {
                    helper.rv_pro_eva.setVisibility(View.GONE);
                }
                GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.img_direct_avatar), goodsCommentBean.getAvatar());
                helper.setTag(R.id.img_direct_avatar, R.id.iv_avatar_tag, goodsCommentBean)
                        .addOnClickListener(R.id.img_direct_avatar)
                        .setTag(R.id.tv_eva_count, goodsCommentBean)
                        .setGone(R.id.ratingBar_direct_count, goodsCommentBean.getStar() < 1)
                        .setText(R.id.tv_eva_count, goodsCommentBean.getLikeNum() > 0
                                ? goodsCommentBean.getLikeNum() + "" : "赞")
                        .addOnClickListener(R.id.tv_eva_count)
                        .setTag(R.id.tv_eva_count, goodsCommentBean)
                        .setText(R.id.tv_eva_user_name, getStrings(goodsCommentBean.getNickname()));
                ratingBar.setNumStars(goodsCommentBean.getStar());
                ratingBar.setMax(goodsCommentBean.getStar());
                if (!TextUtils.isEmpty(goodsCommentBean.getContent())) {
                    tv_direct_evaluation.setVisibility(View.VISIBLE);
                    tv_direct_evaluation.setText(goodsCommentBean.getContent());
                } else {
                    tv_direct_evaluation.setVisibility(View.GONE);
                }
                if (goodsCommentBean.getIs_reply() != 0) {
                    ll_eva_comment_reply.setVisibility(View.VISIBLE);
                    String reply_content = goodsCommentBean.getReply_content();
                    String replyName = getStrings("多么生活：");
                    reply_content = getStrings(replyName + (TextUtils.isEmpty(reply_content) ?
                            context.getResources().getString(R.string.direct_eva_hint) : reply_content));
                    Link replyNameLink = new Link(replyName);
//                    回复颜色
                    replyNameLink.setTextColor(Color.parseColor("#8e8e8e"));
                    replyNameLink.setUnderlined(false);
                    replyNameLink.setHighlightAlpha(0f);
                    emo_direct_eva_reply.setText(getStrings(reply_content));
                    LinkBuilder.on(emo_direct_eva_reply)
                            .setText(reply_content)
                            .addLink(replyNameLink)
                            .build();
                } else {
                    ll_eva_comment_reply.setVisibility(View.GONE);
                }
                tv_eva_count.setSelected(goodsCommentBean.isFavor());
                break;
            case ConstantVariable.TYPE_1:
                helper.setGone(R.id.hasDataNull, false);
                break;
        }

    }

    private void setEvaImages(RecyclerView rvProductEva, String images) {
        if (!TextUtils.isEmpty(images)) {
            final List<String> originalPhotos = new ArrayList<>();
            final List<PictureBean> pictureBeanList = new ArrayList<>();
            String[] evaImages = images.split(",");
            PictureBean pictureBean;
            originalPhotos.addAll(Arrays.asList(evaImages));
            for (int i = 0; i < evaImages.length; i++) {
                pictureBean = new PictureBean();
                pictureBean.setItemType(TYPE_3);
                pictureBean.setIndex(i);
                pictureBean.setPath(evaImages[i]);
                pictureBean.setOriginalList(originalPhotos);
                pictureBeanList.add(pictureBean);
            }
            FindImageListAdapter findImageListAdapter = new FindImageListAdapter(context, pictureBeanList);
            rvProductEva.setAdapter(findImageListAdapter);
            findImageListAdapter.setNewData(pictureBeanList);
            findImageListAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PictureBean pictureBean = (PictureBean) view.getTag();
                    if (pictureBean != null) {
                        ImageBean imageBean = null;
                        List<ImageBean> imageBeanList = new ArrayList<>();
                        for (String picUrl:pictureBean.getOriginalList()) {
                            imageBean = new ImageBean();
                            imageBean.setPicUrl(picUrl);
                            imageBeanList.add(imageBean);
                        }
                        ImagePagerActivity.startImagePagerActivity(context,IMAGE_DEF, imageBeanList
                                , pictureBean.getIndex() < pictureBean.getOriginalList().size() ? pictureBean.getIndex() : 0, null);
                    }
                }
            });
        } else {
            rvProductEva.setVisibility(View.GONE);
        }
    }


    public class ProductEvaViewHolder extends BaseViewHolderHelper {
        RecyclerView rv_pro_eva;

        public ProductEvaViewHolder(View view) {
            super(view);
            rv_pro_eva = view.findViewById(R.id.rv_pro_eva);
            if(rv_pro_eva!=null){
                rv_pro_eva.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }
}