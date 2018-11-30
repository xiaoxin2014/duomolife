package com.amkj.dmsh.find.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * Created by atd48 on 2016/7/14.
 */
public class AdapterReceiverComment extends BaseMultiItemQuickAdapter<ArticleCommentBean, BaseViewHolder> {
    private final Context context;

    public AdapterReceiverComment(Context context, List<ArticleCommentBean> articleCommentList) {
        super(articleCommentList);
        addItemType(ArticleCommentBean.NORMAL_CODE, R.layout.comment_layout);
        addItemType(ArticleCommentBean.FOOT_EMPTY_CODE, R.layout.communal_comment_not);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ArticleCommentBean articleCommentBean) {
        switch (helper.getItemViewType()) {
            case ArticleCommentBean.NORMAL_CODE:
                helper.setText(R.id.tv_user_proName, getStrings(articleCommentBean.getNickname()))
                        .setText(R.id.tv_recommend_comment_time, getStrings(articleCommentBean.getCtime()))
                        .setGone(R.id.tv_pro_comment_liked, true)
                        .addOnClickListener(R.id.tv_receiver_content)
                        .setTag(R.id.tv_receiver_content, articleCommentBean)
                        .setText(R.id.tv_pro_comment_liked, articleCommentBean.getLike_num() > 0 ? String.valueOf(articleCommentBean.getLike_num()) : "赞");
                TextView tv_pro_comment_liked = helper.getView(R.id.tv_pro_comment_liked);
                tv_pro_comment_liked.setSelected(articleCommentBean.isFavor());
                tv_pro_comment_liked.setTag(articleCommentBean);
                tv_pro_comment_liked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //具体点赞处理代码
                        isLoginStatus(v);
                    }
                });
                String content = getStrings(articleCommentBean.getContent());
                TextView tv_receiver_content = helper.getView(R.id.tv_receiver_content);
                tv_receiver_content.clearComposingText();
                tv_receiver_content.setTag(R.id.copy_text_content, content);
                //是否是回复
                if (articleCommentBean.getIs_reply() == 1) {
                    String replyName = articleCommentBean.getNickname1() + " : ";
                    content = "回复 " + replyName + content;
                    Link replyNameLink = new Link(replyName);
//                    回复颜色
                    replyNameLink.setTextColor(Color.parseColor("#8e8e8e"));
                    replyNameLink.setUnderlined(false);
                    replyNameLink.setHighlightAlpha(0f);
                    tv_receiver_content.setText(content);
                    //是否是@用户
                    if (articleCommentBean.getIs_at() == 1 || articleCommentBean.getAtList().size() > 0) {
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
                                List<ArticleCommentBean.AtListBean> atList = articleCommentBean.getAtList();
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
                    if (articleCommentBean.getIs_at() == 1 || articleCommentBean.getAtList() != null && articleCommentBean.getAtList().size() > 0) {
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
                                List<ArticleCommentBean.AtListBean> atList = articleCommentBean.getAtList();
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
                tv_receiver_content.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CommunalCopyTextUtils.showPopWindow(context, (TextView) v, (String) v.getTag(R.id.copy_text_content));
                        return true;
                    }
                });
                ImageView img_user_avatar = helper.getView(R.id.iv_inv_user_avatar);
                GlideImageLoaderUtil.loadHeaderImg(context, img_user_avatar, articleCommentBean.getAvatar());
                img_user_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  2016/9/21  跳转用户主页
                        Intent intent = new Intent(context, UserPagerActivity.class);
                        intent.putExtra("userId", String.valueOf(articleCommentBean.getUid()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                break;
            case ArticleCommentBean.FOOT_EMPTY_CODE:
                helper.getView(R.id.hasDataNull).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new EventMessage("showEditView", "show"));
                    }
                });
                break;
        }
    }

    public void isLoginStatus(View v) {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(context);
        if (personalInfo.isLogin()) {
            //登陆成功，加载信息
            int uid = personalInfo.getUid();
            //登陆成功处理
            setLikedFlag(uid, v);
//                上传数据
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(context, MineLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity) context).startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    public void setLikedFlag(int uid, final View v) {
        final TextView textView = (TextView) v;
        textView.setEnabled(false);
        final ArticleCommentBean commentBean = (ArticleCommentBean) v.getTag();
        String url = Url.BASE_URL + Url.FIND_AND_COMMENT_FAV;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", uid);
        //评论id
        params.put("id", commentBean.getId());
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                textView.setEnabled(true);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (commentBean.isFavor()) {
                            if (!textView.isSelected()) {
                                textView.setSelected(true);
                                textView.setText(commentBean.getLike_num() + "");
                                showToast(context, "已点赞");
                            } else {
                                textView.setSelected(false);
                                textView.setText(commentBean.getLike_num() - 1 + "");
                                showToast(context, "已取消点赞");
                            }
                        } else {
                            if (!textView.isSelected()) {
                                textView.setSelected(true);
                                textView.setText(commentBean.getLike_num() + 1 + "");
                                showToast(context, "已点赞");
                            } else {
                                textView.setSelected(false);
                                textView.setText(commentBean.getLike_num() + "");
                                showToast(context, "已取消点赞");
                            }
                        }

                    } else {
                        showToast(context, requestStatus.getMsg());
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                textView.setEnabled(true);
                super.onError(ex, isOnCallback);
            }
        });
    }
}
