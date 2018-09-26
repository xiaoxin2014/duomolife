package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.CommentDetailEntity.CommentDetailBean.ReplyCommBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/21
 * class description:请输入类描述
 */
public class ArticleCommentDetailAdapter extends BaseQuickAdapter<ReplyCommBean, BaseViewHolder> {
    private final Context context;

    public ArticleCommentDetailAdapter(Context context, List<ReplyCommBean> replyCommList) {
        super(R.layout.adapter_art_comment_detail, replyCommList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyCommBean replyCommBean) {
        TextView tv_comm_comment_like = helper.getView(R.id.tv_comm_comment_like);
        GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.civ_comm_comment_inner_avatar), replyCommBean.getAvatar());
        helper.setText(R.id.tv_comm_comment_inner_name, replyCommBean.getIs_reply() == 1 ?
                (getStrings(replyCommBean.getNickname()) + " 回复 " + getStrings(replyCommBean.getNickname1()))
                : getStrings(replyCommBean.getNickname()))
                .setText(R.id.tv_comm_comment_inner_content, getStrings(replyCommBean.getContent()))
                .addOnClickListener(R.id.tv_comm_comment_like).setTag(R.id.tv_comm_comment_like, replyCommBean)
                .addOnClickListener(R.id.tv_comm_comment_receive).setTag(R.id.tv_comm_comment_receive, replyCommBean)
                .addOnClickListener(R.id.civ_comm_comment_inner_avatar).setTag(R.id.civ_comm_comment_inner_avatar, R.id.iv_tag, replyCommBean)
                .addOnLongClickListener(R.id.tv_comm_comment_inner_content).setTag(R.id.tv_comm_comment_inner_content, replyCommBean);
        tv_comm_comment_like.setSelected(replyCommBean.isFavor());
        tv_comm_comment_like.setText(replyCommBean.getLike_num() > 0
                ? String.valueOf(replyCommBean.getLike_num()) : "赞");
    }
}
