package com.amkj.dmsh.dominant.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.CommentDetailsActivity;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean.ReplyCommListBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_COMMENT_TOTAL_COUNT;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/21
 * class description:评论列表
 */

public class ArticleCommentAdapter extends BaseQuickAdapter<DmlSearchCommentBean, ArticleCommentAdapter.ArticleViewHolder> {
    private final Context context;
    private final String commentType;

    public ArticleCommentAdapter(Context context, List<DmlSearchCommentBean> articleCommentList,String commentType) {
        super(R.layout.adapter_article_comment, articleCommentList);
        this.context = context;
        this.commentType = commentType;
    }

    @Override
    protected void convert(ArticleViewHolder helper, DmlSearchCommentBean dmlSearchCommentBean) {
        GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.civ_comm_comment_avatar), dmlSearchCommentBean.getAvatar());
        TextView tv_comm_comment_like = helper.getView(R.id.tv_comm_comment_like);
        helper.setText(R.id.tv_comm_comment_nick_name, getStrings(dmlSearchCommentBean.getNickname()))
                .setText(R.id.tv_comm_comment_content, getStrings(dmlSearchCommentBean.getContent()))
                .setText(R.id.tv_comm_comment_time, getStrings(dmlSearchCommentBean.getCtime()))
                .addOnClickListener(R.id.tv_comm_comment_like).setTag(R.id.tv_comm_comment_like, dmlSearchCommentBean)
                .addOnClickListener(R.id.tv_comm_comment_receive).setTag(R.id.tv_comm_comment_receive, dmlSearchCommentBean)
                .addOnClickListener(R.id.civ_comm_comment_avatar).setTag(R.id.civ_comm_comment_avatar, R.id.iv_tag, dmlSearchCommentBean)
                .addOnLongClickListener(R.id.tv_comm_comment_content).setTag(R.id.tv_comm_comment_content, dmlSearchCommentBean);
        tv_comm_comment_like.setText(dmlSearchCommentBean.getLike_num() > 0
                ? String.valueOf(dmlSearchCommentBean.getLike_num()) : "赞");
        tv_comm_comment_like.setSelected(dmlSearchCommentBean.isFavor());
        final List<ReplyCommListBean> replyCommList = dmlSearchCommentBean.getReplyCommList();
        for (int i = 0; i < replyCommList.size(); i++) {
            ReplyCommListBean replyCommListBean = replyCommList.get(i);
            replyCommListBean.setMainContentId(dmlSearchCommentBean.getId());
            replyCommList.set(i, replyCommListBean);
        }
        if (replyCommList.size() > DEFAULT_COMMENT_TOTAL_COUNT - 1
                && replyCommList.get(replyCommList.size() - 1).getItemType() != ConstantVariable.TYPE_1) {
            ReplyCommListBean replyCommListBean = new ReplyCommListBean();
            replyCommListBean.setItemType(ConstantVariable.TYPE_1);
            replyCommListBean.setId(dmlSearchCommentBean.getId());
            replyCommListBean.setObj_id(dmlSearchCommentBean.getObj_id());
            replyCommList.set(replyCommList.size() - 1, replyCommListBean);
        }
        ArticleCommentInnerAdapter articleCommentInnerAdapter = new ArticleCommentInnerAdapter(context, replyCommList);
        helper.communal_recycler_wrap.setAdapter(articleCommentInnerAdapter);
        articleCommentInnerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ReplyCommListBean replyCommListBean = (ReplyCommListBean) view.getTag();
                if (replyCommListBean != null) {
                    if (replyCommListBean.getItemType() == ConstantVariable.TYPE_1) {
                        Intent intent = new Intent(context, CommentDetailsActivity.class);
                        intent.putExtra("commentId",String.valueOf(replyCommListBean.getId()));
                        intent.putExtra("objId",String.valueOf(replyCommListBean.getObj_id()));
                        intent.putExtra("commentType",commentType);
                        context.startActivity(intent);
                    }
                }
            }
        });
        articleCommentInnerAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ReplyCommListBean replyCommListBean = (ReplyCommListBean) view.getTag(R.id.iv_tag);
                if (replyCommListBean == null) {
                    replyCommListBean = (ReplyCommListBean) view.getTag();
                }
                if (replyCommListBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_comm_comment_like:
                            List<ReplyCommListBean> replyCommList = adapter.getData();
                            ReplyCommListBean replyCommBean = replyCommList.get(position);
                            replyCommBean.setIsFavor(!replyCommBean.isIsFavor());
                            replyCommBean.setLike_num(replyCommBean.isIsFavor()
                                    ? replyCommBean.getLike_num() + 1 : replyCommBean.getLike_num() - 1 < 0
                                    ? 0 : replyCommBean.getLike_num() - 1);
                            replyCommList.set(position, replyCommBean);
                            isLoginStatus(view, replyCommList);
                            break;
                        case R.id.tv_comm_comment_receive:
                            EventBus.getDefault().post(new EventMessage("showEditView", replyCommListBean));
                            break;
                        case R.id.civ_comm_comment_avatar:
                            skipUserUI(replyCommListBean.getUid());
                            break;
                    }
                }
            }
        });
        articleCommentInnerAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                ReplyCommListBean replyCommListBean = (ReplyCommListBean) view.getTag();
                if (replyCommListBean != null && !TextUtils.isEmpty(replyCommListBean.getContent())) {
                    CommunalCopyTextUtils.showPopWindow(context, (TextView) view, replyCommListBean.getContent());
                }
                return false;
            }
        });
        helper.itemView.setTag(dmlSearchCommentBean);
    }

    private void skipUserUI(int userId) {
        Intent intent = new Intent();
        intent.setClass(context, UserPagerActivity.class);
        intent.putExtra("userId", String.valueOf(userId));
        context.startActivity(intent);
    }

    public void isLoginStatus(View v, List<ReplyCommListBean> replyCommList) {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(context);
        if (personalInfo.isLogin()) {
            //登陆成功，加载信息
            int uid = personalInfo.getUid();
            //登陆成功处理
            setCommentLike(uid, v);
            EventBus.getDefault().post(new EventMessage("replyComm", replyCommList));
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(context, MineLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity) context).startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    private void setCommentLike(int uid, View view) {
        ReplyCommListBean replyCommListBean = (ReplyCommListBean) view.getTag();
        TextView textView = (TextView) view;
        textView.setSelected(!textView.isSelected());
        String url = Url.BASE_URL + Url.FIND_AND_COMMENT_FAV;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", uid);
        //评论id
        params.put("id", replyCommListBean.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(mAppContext,url,params,null);
        String likeCount = getNumber(textView.getText().toString().trim());
        int likeNum = Integer.parseInt(likeCount);
        textView.setText(String.valueOf(textView.isSelected()
                ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : "赞"));
    }

    private String getNumber(String content) {
        String regex = "[0-9]\\d*\\.?\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            return m.group();
        }
        return "0";
    }

    public class ArticleViewHolder extends BaseViewHolder {
        RecyclerView communal_recycler_wrap;

        public ArticleViewHolder(View view) {
            super(view);
            communal_recycler_wrap = (RecyclerView) view.findViewById(R.id.communal_recycler_wrap);
            if(communal_recycler_wrap!=null){
                communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
                communal_recycler_wrap.setBackgroundColor(Color.TRANSPARENT);
                communal_recycler_wrap.setNestedScrollingEnabled(false);
            }
        }
    }
}
