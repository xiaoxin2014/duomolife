package com.amkj.dmsh.message.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean;
import com.amkj.dmsh.message.bean.MessageCommentEntity.MessageCommentBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.R.id.tv_receiver_content;
import static com.amkj.dmsh.R.id.tv_user_product_description;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by atd48 on 2016/7/11.
 */
public class MessageCommunalAdapterNew extends BaseQuickAdapter<Object, BaseViewHolder> {
    private final String type;
    private final Context context;
    private final String MESSAGE_LIKED = "message_liked";
    private final String MESSAGE_COMMENT = "message_comment";

    public MessageCommunalAdapterNew(Context context, List allList, String type) {
        super(R.layout.adapter_user_liked_comment, allList);
        this.type = type;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        if (item == null) return;
        TextView tv_reply = helper.getView(R.id.tv_mes_com_receiver);
        ImageView img_user_product = helper.getView(R.id.img_user_product);
        TextView tvFollow = helper.getView(R.id.tv_follow);
        switch (type) {
            case MESSAGE_LIKED:
                ArticleCommentBean articleCommentBean = (ArticleCommentBean) item;
                tv_reply.setVisibility(View.GONE);
                tvFollow.setVisibility(View.VISIBLE);
                tvFollow.setSelected(articleCommentBean.getIsFocus());
                tvFollow.setText(articleCommentBean.getIsFocus() ? "已关注" : "关注");

                helper.setText(R.id.tv_recommend_comment_time, getStrings(articleCommentBean.getCreate_time()))
                        .setText(R.id.tv_user_proName, getStrings(articleCommentBean.getNickname()));
                //头像
                GlideImageLoaderUtil.loadHeaderImg(context, helper.getView(R.id.iv_inv_user_avatar), articleCommentBean.getAvatar());
                if (!TextUtils.isEmpty(articleCommentBean.getPath())) {
                    img_user_product.setVisibility(View.VISIBLE);
                    GlideImageLoaderUtil.loadCenterCrop(context, img_user_product, articleCommentBean.getPath());
                } else {
                    img_user_product.setVisibility(View.GONE);
                }

                String description = articleCommentBean.getDescription();
                String content = articleCommentBean.getContent();

                switch (articleCommentBean.getType()) {
                    case "comment":
                        helper.setText(tv_receiver_content, "赞了你的评论");
                        helper.setGone(R.id.rel_adapter_message_communal, !TextUtils.isEmpty(content));
                        helper.setText(tv_user_product_description, "@" + articleCommentBean.getCommentUserName() + ":" + getStrings(content));
                        img_user_product.setVisibility(View.GONE);
                        break;
                    case "document":
                        helper.setText(tv_receiver_content, "赞了你的帖子");
                        helper.setGone(R.id.rel_adapter_message_communal, !TextUtils.isEmpty(description) || !TextUtils.isEmpty(articleCommentBean.getPath()));
                        helper.setText(tv_user_product_description, getStrings(description));
                        break;
                    case "proEvaluate":
                        helper.setText(tv_receiver_content, "赞了你的评价");
                        helper.setGone(R.id.rel_adapter_message_communal, !TextUtils.isEmpty(description) || !TextUtils.isEmpty(articleCommentBean.getPath()));
                        helper.setText(tv_user_product_description, getStrings(description));
                        break;
                }
                if (articleCommentBean.getStatus() == -1) {
                    helper.setText(R.id.tv_user_product_description, "已删除");
                }
                helper.addOnClickListener(R.id.iv_inv_user_avatar).setTag(R.id.iv_inv_user_avatar, R.id.iv_avatar_tag, articleCommentBean)
                        .addOnClickListener(R.id.rel_adapter_message_communal).setTag(R.id.rel_adapter_message_communal, articleCommentBean)
                        .addOnClickListener(R.id.tv_follow).setTag(R.id.tv_follow, articleCommentBean);
                break;
            case MESSAGE_COMMENT:
                MessageCommentBean messageCommentBean = (MessageCommentBean) item;
                tv_reply.setVisibility(View.VISIBLE);
                tvFollow.setVisibility(View.GONE);
                helper.setGone(R.id.tv_receiver_content, true)
                        .setText(R.id.tv_recommend_comment_time, getStrings(messageCommentBean.getCtime()))
                        .setText(R.id.tv_user_proName, getStrings(messageCommentBean.getNickname1()));
                //头像
                GlideImageLoaderUtil.loadHeaderImg(context, helper.getView(R.id.iv_inv_user_avatar), messageCommentBean.getAvatar());
                if (!TextUtils.isEmpty(messageCommentBean.getPath())) {
                    img_user_product.setVisibility(View.VISIBLE);
                    GlideImageLoaderUtil.loadCenterCrop(context, img_user_product, messageCommentBean.getPath());
                } else {
                    img_user_product.setVisibility(View.GONE);
                }

                //回复评论
                if (!TextUtils.isEmpty(messageCommentBean.getNickname2())) {
                    img_user_product.setVisibility(View.GONE);
                    helper.setText(R.id.tv_user_product_description, "@" + messageCommentBean.getNickname2() + ":" + messageCommentBean.getDescription());
                    helper.setGone(R.id.rel_adapter_message_communal, !TextUtils.isEmpty(messageCommentBean.getDescription()));
                } else {//回复帖子
                    helper.setText(R.id.tv_user_product_description, getStrings(messageCommentBean.getDescription()));
                    helper.setGone(R.id.rel_adapter_message_communal, !TextUtils.isEmpty(messageCommentBean.getDescription()) || !TextUtils.isEmpty(messageCommentBean.getPath()));
                }

                //是否删除
                if (messageCommentBean.getStatus() == -1) {
                    helper.setText(R.id.tv_user_product_description, "已删除");
                }
                //内容
                String comment = messageCommentBean.getContent();
                if (!TextUtils.isEmpty(comment)) {
                    TextView tv_receiver_content = helper.getView(R.id.tv_receiver_content);
//                    //是否是回复
//                    if (messageCommentBean.getIs_reply() == 1) {
//                        comment = "回复：" + comment;
//                    }
                    tv_receiver_content.setText(comment);
                } else {
                    helper.setGone(R.id.tv_receiver_content, false);
                }
                helper.addOnClickListener(R.id.iv_inv_user_avatar)
                        .addOnClickListener(R.id.tv_mes_com_receiver).setTag(R.id.tv_mes_com_receiver, messageCommentBean)
                        .addOnClickListener(R.id.rel_adapter_message_communal).setTag(R.id.rel_adapter_message_communal, messageCommentBean)
                        .setTag(R.id.iv_inv_user_avatar, R.id.iv_avatar_tag, messageCommentBean);
                break;
        }
    }
}
