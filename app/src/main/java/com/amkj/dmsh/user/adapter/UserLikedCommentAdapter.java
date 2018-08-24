package com.amkj.dmsh.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.user.bean.UserLikedCommentBean.ULikedComment;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.List;
import java.util.regex.Pattern;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by LGuipeng on 2016/9/11.
 */
public class UserLikedCommentAdapter extends BaseQuickAdapter<ULikedComment, BaseViewHolderHelper> {
    private final Context context;

    public UserLikedCommentAdapter(Context context, List<ULikedComment> articleList) {
        super(R.layout.adapter_user_liked_comment, articleList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, ULikedComment bean) {
        TextView tv_user_product_description = helper.getView(R.id.tv_user_product_description);
//点赞的评论头像
        GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.iv_inv_user_avatar), bean.getAvatar());
        //点赞的文章 帖子产品 图片
        helper.setText(R.id.tv_user_proName, getStrings(bean.getNickname()))
                .setText(R.id.tv_recommend_comment_time, getStrings(bean.getCtime()))
                .setText(R.id.tv_mes_com_receiver, bean.getLike_num() + "")
                .addOnClickListener(R.id.tv_user_product_description).setTag(R.id.tv_user_product_description, bean);
        if (bean.getStatus() == -1) {
            switch (bean.getObj()) {
                case "doc":
                    tv_user_product_description.setText("该文章已删除");
                    break;
                case "goods":
                    tv_user_product_description.setText("该产品已删除");
                    break;
                default:
                    tv_user_product_description.setText("该帖子已删除");
                    break;
            }
        } else {
            tv_user_product_description.setText(getStrings(bean.getTitle()));
        }
        helper.setGone(R.id.img_user_product, true);
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_user_product), bean.getPath());
        String content = bean.getContent();
        if (!TextUtils.isEmpty(content)) {
            TextView tv_receiver_content = helper.getView(R.id.tv_receiver_content);
            tv_receiver_content.setText(content);
            //是否是回复
            if (bean.getIs_reply() == 1) {
                String replyName = bean.getNickname1() + " : ";
                content = "回复 " + replyName + content;
                Link replyNameLink = new Link(replyName);
//                    回复颜色
                replyNameLink.setTextColor(Color.parseColor("#8e8e8e"));
                replyNameLink.setUnderlined(false);
                replyNameLink.setHighlightAlpha(0f);
                tv_receiver_content.setText(content);
                //是否是@用户
                if (bean.getIs_at() == 1 || bean.getAtList().size() > 0) {
                    final List<ULikedComment.AtListBean> atList = bean.getAtList();
                    String regex = "@[^@\\s]+\\s";
                    Pattern p = Pattern.compile(regex);
                    Link atPerson = new Link(p);
                    //        @用户昵称
                    atPerson.setTextColor(Color.parseColor("#5faeff"));
                    atPerson.setUnderlined(false);
                    atPerson.setHighlightAlpha(0f);
                    tv_receiver_content.setText(content);
                    atPerson.setOnClickListener(new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
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
                    LinkBuilder.on(tv_receiver_content)
                            .setText(content)
                            .addLink(atPerson)
                            .addLink(replyNameLink)
                            .build();
                } else {
                    LinkBuilder.on(tv_receiver_content)
                            .setText(content)
                            .addLink(replyNameLink)
                            .build();
                }
            } else {
                tv_receiver_content.setText(content);
                //是否是@用户
                if (bean.getIs_at() == 1 || bean.getAtList().size() > 0) {
                    final List<ULikedComment.AtListBean> atList = bean.getAtList();
                    String regex = "@[^@\\s]+\\s";
                    Pattern p = Pattern.compile(regex);
                    Link atPerson = new Link(p);
                    //        @用户昵称
                    atPerson.setTextColor(Color.parseColor("#5faeff"));
                    atPerson.setUnderlined(false);
                    atPerson.setHighlightAlpha(0f);
                    atPerson.setOnClickListener(new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
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
                    LinkBuilder.on(tv_receiver_content)
                            .setText(content)
                            .addLink(atPerson)
                            .build();
                }
            }
        } else {
            helper.setGone(R.id.tv_receiver_content, false);
        }
        tv_user_product_description.setTag(bean);
        helper.itemView.setTag(bean);
    }
}
