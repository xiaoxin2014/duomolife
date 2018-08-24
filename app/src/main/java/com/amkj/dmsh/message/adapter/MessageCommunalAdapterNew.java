package com.amkj.dmsh.message.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.message.bean.MessageCommentEntity.MessageCommentBean;
import com.amkj.dmsh.message.bean.MessageSysEntity.MessageSysBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.List;
import java.util.regex.Pattern;

import static com.amkj.dmsh.R.id.tv_receiver_content;
import static com.amkj.dmsh.R.id.tv_user_product_description;
import static com.amkj.dmsh.constant.ConstantVariable.MES_ADVISE;
import static com.amkj.dmsh.constant.ConstantVariable.MES_FEEDBACK;

/**
 * Created by atd48 on 2016/7/11.
 */
public class MessageCommunalAdapterNew extends BaseQuickAdapter<Object, BaseViewHolderHelper> {
    private final String type;
    private final Context context;
    private final String MESSAGE_LIKED = "message_liked";
    private final String MESSAGE_SYS = "message_sys_message";
    private final String MESSAGE_COMMENT = "message_comment";

    public MessageCommunalAdapterNew(Context context, List allList, String type) {
        super(R.layout.adapter_user_liked_comment, allList);
        this.type = type;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, Object item) {
        TextView tv_user_liked_collect = helper.getView(R.id.tv_mes_com_receiver);
        ImageView img_user_product = helper.getView(R.id.img_user_product);
        switch (type) {
            case MESSAGE_LIKED:
                ArticleCommentBean articleCommentBean = (ArticleCommentBean) item;
                helper.setGone(R.id.tv_mes_com_receiver, false);
                switch (articleCommentBean.getType()) {
                    case "comment":
                        helper.setText(tv_receiver_content, "赞了这条评论");
                        break;
                    case "document":
                        helper.setText(tv_receiver_content, "赞了这篇文章");
                        break;
                    case "goods":
                        helper.setText(tv_receiver_content, "赞了这个产品");
                        break;
                }
                helper.setText(R.id.tv_recommend_comment_time, articleCommentBean.getCreate_time())
                        .setText(R.id.tv_user_proName, articleCommentBean.getNickname());
                //头像
                GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.iv_inv_user_avatar), articleCommentBean.getAvatar());
                if (!TextUtils.isEmpty(articleCommentBean.getPath())) {
                    img_user_product.setVisibility(View.VISIBLE);
                    GlideImageLoaderUtil.loadCenterCrop(context, img_user_product, articleCommentBean.getPath());
                } else {
                    img_user_product.setVisibility(View.GONE);
                }
//                判断是否有描述内容
                if (!TextUtils.isEmpty(articleCommentBean.getDescription())) {
                    helper.setGone(R.id.rel_adapter_message_communal, true)
                            .setText(tv_user_product_description, articleCommentBean.getDescription());
                } else {
                    helper.setGone(R.id.rel_adapter_message_communal, false);
                }
                if (articleCommentBean.getStatus() != 1) {
                    helper.setText(R.id.tv_user_product_description, "已删除");
                }
                helper.addOnClickListener(R.id.iv_inv_user_avatar).setTag(R.id.iv_inv_user_avatar, R.id.iv_avatar_tag, articleCommentBean)
                        .addOnClickListener(R.id.tv_user_product_description).setTag(R.id.tv_user_product_description, articleCommentBean);
                break;
            case MESSAGE_SYS:
                MessageSysBean messageMeBean = (MessageSysBean) item;
                helper.setGone(R.id.tv_mes_com_receiver, false)
                        .setText(R.id.tv_user_proName, "通知")
                        .setImageResource(R.id.iv_inv_user_avatar, R.drawable.domolife_logo)
                        .setText(R.id.tv_recommend_comment_time, messageMeBean.getCtime())
                        .setGone(R.id.rel_adapter_message_communal, false);
                String mContent = messageMeBean.getM_content();
                if (!TextUtils.isEmpty(mContent)) {
                    helper.setText(R.id.tv_receiver_content, mContent);
                    String regex = "[1-9]\\d*\\.?\\d*";
                    Pattern p = Pattern.compile(regex);
                    Link redNum = new Link(p);
                    //        @用户昵称
                    redNum.setTextColor(Color.parseColor("#ff5e5e"));
                    redNum.setUnderlined(false);
                    redNum.setHighlightAlpha(0f);
                    LinkBuilder.on((TextView) helper.getView(R.id.tv_receiver_content))
                            .setText(mContent)
                            .addLink(redNum)
                            .build();
                } else {
                    helper.setGone(R.id.tv_receiver_content, false);
                }
                break;
            case MESSAGE_COMMENT:
                MessageCommentBean messageCommentBean = (MessageCommentBean) item;
                tv_user_liked_collect.setVisibility(View.VISIBLE);
                tv_user_liked_collect.setBackgroundResource(R.drawable.message_me_receiver_border);
                tv_user_liked_collect.setTextColor(Color.parseColor("#6f6f6f"));
                tv_user_liked_collect.setText("回复");
                tv_user_liked_collect.setCompoundDrawables(null, null, null, null);
                helper.setGone(R.id.tv_receiver_content, true)
                        .setText(R.id.tv_recommend_comment_time, messageCommentBean.getCtime())
                        .setText(R.id.tv_user_proName, messageCommentBean.getNickname1());
                //头像
                GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.iv_inv_user_avatar), messageCommentBean.getAvatar());
                if (!TextUtils.isEmpty(messageCommentBean.getPath())) {
                    img_user_product.setVisibility(View.VISIBLE);
                    GlideImageLoaderUtil.loadCenterCrop(context, img_user_product, messageCommentBean.getPath());
                } else {
                    img_user_product.setVisibility(View.GONE);
                }
//                判断是否有描述内容
                if (!TextUtils.isEmpty(messageCommentBean.getDescription())) {
                    helper.setGone(R.id.rel_adapter_message_communal, true);
                    switch (messageCommentBean.getObj()) {
                        case MES_ADVISE:
                            helper.setText(tv_user_product_description, "留言：" + messageCommentBean.getDescription());
                            break;
                        case MES_FEEDBACK:
                            helper.setText(tv_user_product_description, "建议反馈：" + messageCommentBean.getDescription());
                            break;
                        default:
                            helper.setText(tv_user_product_description, messageCommentBean.getDescription());
                            break;
                    }
                } else {
                    helper.setGone(R.id.rel_adapter_message_communal, false);
                }
//                是否删除
                if (messageCommentBean.getStatus() != 1) {
                    helper.setText(R.id.tv_user_product_description, "已删除");
                }
//                内容
                String comment = messageCommentBean.getContent();
                if (!TextUtils.isEmpty(comment)) {
                    TextView tv_receiver_content = helper.getView(R.id.tv_receiver_content);
                    //是否是回复
                    if (messageCommentBean.getIs_reply() == 1) {
                        comment = "回复：" + comment;
                    }
                    tv_receiver_content.setText(comment);
                } else {
                    helper.setGone(R.id.tv_receiver_content, false);
                }
                helper.addOnClickListener(R.id.iv_inv_user_avatar).addOnClickListener(R.id.tv_mes_com_receiver)
                        .addOnClickListener(R.id.tv_user_product_description).setTag(R.id.tv_mes_com_receiver, messageCommentBean)
                        .setTag(R.id.iv_inv_user_avatar, R.id.iv_avatar_tag, messageCommentBean).setTag(R.id.tv_user_product_description, messageCommentBean);
                break;
        }
    }
}
