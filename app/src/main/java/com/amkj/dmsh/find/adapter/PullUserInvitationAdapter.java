package com.amkj.dmsh.find.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.find.XFLinearLayoutManager;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.RelevanceProBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.user.adapter.InvitationProAdapter;
import com.amkj.dmsh.utils.ImageConverterUtils;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipPostDetail;
import static com.amkj.dmsh.constant.ConstantMethod.skipTopicDetail;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_URL;
import static com.amkj.dmsh.constant.ConstantVariable.TOPIC_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;



/**
 * Created by atd48 on 2016/9/14.
 * ?????????????????????
 */
public class PullUserInvitationAdapter extends BaseQuickAdapter<InvitationDetailBean, PullUserInvitationAdapter.InvitationViewHolder> {
    private final Activity context;
    private final String type;
    private List<RelevanceProBean> relevanceProList = new ArrayList<>();
    //    ??????-????????????
    private List<PictureBean> pictureBeanList;
    private List<PictureBean> pathList = new ArrayList<>();
    private String descriptionContent;

    public PullUserInvitationAdapter(Activity context, List<InvitationDetailBean> invitationSearchList, String type) {
        super(R.layout.adapter_find_recommend, invitationSearchList);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(final InvitationViewHolder helper, final InvitationDetailBean invitationDetailBean) {
        descriptionContent = "";
//        ??????
        TextView tv_live_attention = helper.getView(R.id.tv_inv_live_att);
//        ????????????
        ImageView iv_user_avatar = helper.getView(R.id.iv_inv_user_avatar);
        helper.addOnClickListener(R.id.tv_inv_live_att).setTag(R.id.tv_inv_live_att, invitationDetailBean)
                .setTag(R.id.tv_inv_live_att, R.id.tag_position, (helper.getLayoutPosition() - this.getHeaderLayoutCount()));
        helper.tv_inv_live_content.setTag(invitationDetailBean);
//        ????????????
        final RelativeLayout rel_tag_layout = helper.getView(R.id.rel_tag_layout);
        FlexboxLayout flex_communal_tag = helper.getView(R.id.flex_communal_tag);
        if (type.equals("??????")) {
            //???????????? ??????
            tv_live_attention.setVisibility(View.VISIBLE);
            tv_live_attention.setText("??????");
            tv_live_attention.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            tv_live_attention.setVisibility(View.GONE);
        }
        GlideImageLoaderUtil.loadHeaderImg(context, iv_user_avatar, ImageConverterUtils.getFormatImg(invitationDetailBean.getAvatar()));
        iv_user_avatar.setOnClickListener(v -> {
            if (userId != invitationDetailBean.getUid()) {
                Intent intent = new Intent();
                intent.setClass(context, UserPagerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", String.valueOf(invitationDetailBean.getUid()));
                context.startActivity(intent);
            }
        });
        TextView tv_com_art_like_count = helper.getView(R.id.tv_com_art_like_count);
        TextView tv_com_art_collect_count = helper.getView(R.id.tv_com_art_collect_count);
        tv_com_art_like_count.setSelected(invitationDetailBean.isFavor());
        tv_com_art_collect_count.setSelected(invitationDetailBean.isCollect());
        helper.setText(R.id.tv_inv_user_name, getStrings(invitationDetailBean.getNickname()))
                .setText(R.id.tv_recommend_invitation_time, getStrings(invitationDetailBean.getCtime()))
//                ?????????
                .setText(R.id.tv_com_art_like_count, invitationDetailBean.getFavor() > 0 ?
                        String.valueOf(invitationDetailBean.getFavor() > 999 ? "999+" : invitationDetailBean.getFavor()) : "???")
                .addOnClickListener(R.id.tv_com_art_like_count)
                .setTag(R.id.tv_com_art_like_count, invitationDetailBean)
                .setText(R.id.tv_com_art_comment_count, invitationDetailBean.getComment() > 0 ?
                        String.valueOf(invitationDetailBean.getComment() > 999 ? "999+" : invitationDetailBean.getComment()) : "??????")
                .setGone(R.id.tv_com_art_collect_count, false)
                .addOnClickListener(R.id.tv_com_art_collect_count)
                .setTag(R.id.tv_com_art_collect_count, invitationDetailBean)
                .addOnClickListener(R.id.tv_com_art_comment_count).setTag(R.id.tv_com_art_comment_count, invitationDetailBean);
        ;
        ImageView iv_find_rec_tag = helper.getView(R.id.iv_find_rec_tag);
        if (!TextUtils.isEmpty(invitationDetailBean.getRecommendType())) {
            String recommendType = invitationDetailBean.getRecommendType();
            if (recommendType.equals("1")) {
                iv_find_rec_tag.setVisibility(View.VISIBLE);
                iv_find_rec_tag.setSelected(true);
            } else if (recommendType.equals("2")) {
                iv_find_rec_tag.setVisibility(View.VISIBLE);
                iv_find_rec_tag.setSelected(false);
            } else {
                iv_find_rec_tag.setVisibility(View.GONE);
            }
        } else {
            iv_find_rec_tag.setVisibility(View.GONE);
        }
        switch (invitationDetailBean.getArticletype()) {
            case 1:
                ImageView iv_find_article_cover = helper.getView(R.id.iv_find_article_cover);
                GlideImageLoaderUtil.loadImgDynamicDrawable(context, iv_find_article_cover, invitationDetailBean.getPath(), -1);
                helper.setGone(R.id.rel_find_article, true)
                        .setGone(R.id.tv_inv_live_content, false)
                        .setText(R.id.tv_find_article_digest, getStrings(invitationDetailBean.getDigest()));
                helper.rv_find_image.setVisibility(View.GONE);
                break;
            default:
                //            ??????
                pathList = new ArrayList<>();
                pathList.addAll(invitationDetailBean.getPictureList());
                descriptionContent = getStrings(invitationDetailBean.getDescription());
                helper.setGone(R.id.rel_find_article, false);
                if (pathList.size() < 1) {
                    helper.rv_find_image.setVisibility(View.GONE);
                } else {
                    helper.rv_find_image.setVisibility(View.VISIBLE);
                    helper.rv_find_image.setLayoutManager(new XFLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    setImagePath(helper.rv_find_image, pathList, invitationDetailBean);
                }
                //????????????
                if (!TextUtils.isEmpty(invitationDetailBean.getDescription())) {
                    helper.tv_inv_live_content.setVisibility(View.VISIBLE);
                    helper.tv_inv_live_content.setTag(R.id.copy_text_content, descriptionContent);
                    helper.tv_inv_live_content.setText(descriptionContent);
                    helper.setGone(R.id.rel_find_article, false);
                    setAtPerson(invitationDetailBean, helper.tv_inv_live_content);
                    //        ??????????????????
                    List<Link> links = new ArrayList<>();
                    if (!TextUtils.isEmpty(invitationDetailBean.getTopic_title())) {
                        String formatTopic = String.format(context.getResources().getString(R.string.topic_format), getStrings(invitationDetailBean.getTopic_title()));
                        Link link = new Link(getStrings(formatTopic));
                        link.setTextColor(0xff0a88fa);
                        link.setUnderlined(false);
                        link.setHighlightAlpha(0f);
                        links.add(link);
                        link.setOnClickListener(clickedText -> {
                            if (!TOPIC_TYPE.equals(type) && !TextUtils.isEmpty(invitationDetailBean.getTopic_title()) && invitationDetailBean.getTopic_id() > 0) {
                                skipTopicDetail(context, String.valueOf(invitationDetailBean.getTopic_id()));
                            }
                        });
                    }
                    /**
                     * ????????????
                     */
                    Link discernUrl = new Link(Pattern.compile(REGEX_URL));
                    discernUrl.setTextColor(0xff0a88fa);
                    discernUrl.setUnderlined(false);
                    discernUrl.setHighlightAlpha(0f);
                    discernUrl.setUrlReplace(true);
                    discernUrl.setOnClickListener(clickedText -> {
                        if (!TextUtils.isEmpty(clickedText)) {
                            setSkipPath(context, clickedText, false);
                        } else {
                            showToast("????????????????????????");
                        }
                    });
                    links.add(discernUrl);
                    LinkBuilder.on(helper.tv_inv_live_content).
                            maxLength(55).
                            addLinks(links).
                            build();
                } else {
                    helper.tv_inv_live_content.setVisibility(View.GONE);
                }
                break;
        }
        if (invitationDetailBean.getTags() != null && invitationDetailBean.getTags().size() > 0) {
            rel_tag_layout.setVisibility(View.VISIBLE);
            flex_communal_tag.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
            flex_communal_tag.setDividerDrawable(context.getResources().getDrawable(R.drawable.item_divider_nine_dp_white));
            flex_communal_tag.removeAllViews();
            for (int i = 0; i < invitationDetailBean.getTags().size(); i++) {
                if (i == 0) {
                    flex_communal_tag.addView(ProductLabelCreateUtils.createArticleIcon(context));
                }
                flex_communal_tag.addView(ProductLabelCreateUtils.createArticleClickTag(context, invitationDetailBean.getTags().get(i)));
            }
        } else {
            rel_tag_layout.setVisibility(View.INVISIBLE);
        }
//        ??????????????????
        if (invitationDetailBean.getRelevanceProList() != null && invitationDetailBean.getRelevanceProList().size() > 0) {
            helper.rel_inv_pro.setVisibility(View.VISIBLE);
            relevanceProList = new ArrayList<>();
            if (invitationDetailBean.getRelevanceProList().size() > 2) {
                for (int i = 0; i < 2; i++) {
                    relevanceProList.add(invitationDetailBean.getRelevanceProList().get(i));
                }
                RelevanceProBean relevanceProBean = new RelevanceProBean();
                relevanceProBean.setItemType(ConstantVariable.TYPE_1);
                relevanceProBean.setSaveObject(invitationDetailBean.getRelevanceProList());
                relevanceProList.add(relevanceProBean);
            } else {
                relevanceProList.addAll(invitationDetailBean.getRelevanceProList());
            }
            InvitationProAdapter invitationProAdapter = new InvitationProAdapter(context, relevanceProList);
            helper.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
            helper.communal_recycler_wrap.setNestedScrollingEnabled(false);
            helper.communal_recycler_wrap.setAdapter(invitationProAdapter);
            invitationProAdapter.setNewData(relevanceProList);
            invitationProAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    RelevanceProBean relevanceProBean = (RelevanceProBean) view.getTag();
                    if (relevanceProBean != null) {
                        if (relevanceProBean.getItemType() == ConstantVariable.TYPE_0) {
                            Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                            intent.putExtra("productId", String.valueOf(relevanceProBean.getProductId()));
                            context.startActivity(intent);
                        } else {
                            List<RelevanceProBean> relevanceProList = (List<RelevanceProBean>) relevanceProBean.getSaveObject();
                            adapter.setNewData(relevanceProList);
                        }
                    }
                }
            });
        } else {
            helper.rel_inv_pro.setVisibility(View.GONE);
        }
        helper.itemView.setTag(invitationDetailBean);
    }

    private void setImagePath(RecyclerView rv_find_image, List<PictureBean> pathList, InvitationDetailBean invitationDetailBean) {
        pictureBeanList = new ArrayList<>();
        if (pathList.size() > 5) {
            for (int i = 0; i < 5; i++) {
                pictureBeanList.add(pathList.get(i));
            }
            PictureBean pictureBean = new PictureBean();
//            ????????????????????????Id
            pictureBean.setObject_id(invitationDetailBean.getId());
//            ????????????????????????
            pictureBean.setType(invitationDetailBean.getArticletype());
            pictureBean.setItemType(TYPE_2);
            pictureBeanList.add(pictureBean);
        } else {
            pictureBeanList.addAll(pathList);
        }
        final List<String> originalPhotos = new ArrayList<>();
        for (int i = 0; i < pictureBeanList.size(); i++) {
            PictureBean pictureBean = pictureBeanList.get(i);
            if (pictureBean.getItemType() != TYPE_2
                    && !TextUtils.isEmpty(pictureBean.getPath())) {
                originalPhotos.add(pictureBean.getPath());
            }
        }

        for (int i = 0; i < pictureBeanList.size(); i++) {
            PictureBean pictureBean = pictureBeanList.get(i);
            if (pictureBean.getItemType() != TYPE_2) {
                pictureBean.setOriginalList(originalPhotos);
                pictureBean.setIndex(i);
            }
        }
        FindImageListAdapter findImageListAdapter = new FindImageListAdapter(context, pictureBeanList);
        rv_find_image.setAdapter(findImageListAdapter);
        findImageListAdapter.setNewData(pictureBeanList);
        findImageListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PictureBean pictureBean = (PictureBean) view.getTag();
                if (pictureBean != null && pictureBean.getOriginalList() != null) {
                    if (TYPE_2 == pictureBean.getItemType()) {
                        skipPostDetail(context, String.valueOf(pictureBean.getObject_id()), pictureBean.getType());
                    } else {
                        showImageActivity(context, IMAGE_DEF,
                                pictureBean.getIndex() < pictureBean.getOriginalList().size() ? pictureBean.getIndex() : 0,
                                pictureBean.getOriginalList());
                    }
                }
            }
        });
    }

    private void setAtPerson(final InvitationDetailBean invitationDetailBean, TextView tv_user_live_content) {
        //?????????@??????
        if (invitationDetailBean.getAtList() != null && invitationDetailBean.getAtList().size() > 0) {
            String regex = "@[^@\\s]+\\s";
            Pattern p = Pattern.compile(regex);
            Link atPerson = new Link(p);
            //        @????????????
            atPerson.setTextColor(Color.parseColor("#5faeff"));
            atPerson.setUnderlined(false);
            atPerson.setHighlightAlpha(0f);
            atPerson.setOnClickListener(new Link.OnClickListener() {
                @Override
                public void onClick(String clickedText) {
                    List<InvitationDetailBean.AtListBean> atList = invitationDetailBean.getAtList();
                    for (int i = 0; i < atList.size(); i++) {
                        String appendName = "@" + atList.get(i).getNickname() + " ";
                        if (clickedText.equals(appendName)) {
                            Intent intent = new Intent(context, UserPagerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("userId", String.valueOf(atList.get(i).getUid()));
                            context.startActivity(intent);
                        }
                    }
                }
            });
            LinkBuilder.on(tv_user_live_content)
                    .setText(invitationDetailBean.getDescription())
                    .addLink(atPerson)
                    .build();
        }
    }


    public class InvitationViewHolder extends BaseViewHolder {
        private RecyclerView communal_recycler_wrap;
        private RecyclerView rv_find_image;
        private RelativeLayout rel_inv_pro;
        private TextView tv_inv_live_content, tv_find_look_more;

        public InvitationViewHolder(View view) {
            super(view);
            communal_recycler_wrap = (RecyclerView) view.findViewById(R.id.communal_recycler_wrap);
            rv_find_image = view.findViewById(R.id.rv_find_image);
            rel_inv_pro = (RelativeLayout) view.findViewById(R.id.rel_inv_pro);
            tv_inv_live_content = (TextView) view.findViewById(R.id.tv_inv_live_content);
            tv_find_look_more = (TextView) view.findViewById(R.id.tv_find_look_more);
        }
    }
}
