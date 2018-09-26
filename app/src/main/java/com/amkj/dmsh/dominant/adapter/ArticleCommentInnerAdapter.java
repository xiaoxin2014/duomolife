package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean.ReplyCommListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/21
 * class description:内部评论
 */

public class ArticleCommentInnerAdapter extends BaseMultiItemQuickAdapter<ReplyCommListBean, BaseViewHolder> {
    private final Context context;

    public ArticleCommentInnerAdapter(Context context, List<ReplyCommListBean> replyCommList) {
        super(replyCommList);
        addItemType(ConstantVariable.TYPE_0, R.layout.adapter_article_comment_inner);
        addItemType(ConstantVariable.TYPE_1, R.layout.adapter_article_comment_inner_more);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyCommListBean replyCommListBean) {
        switch (helper.getItemViewType()) {
            case ConstantVariable.TYPE_0:
                TextView tv_comm_comment_like = helper.getView(R.id.tv_comm_comment_like);
                GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) helper.getView(R.id.civ_comm_comment_inner_avatar), replyCommListBean.getAvatar());
                helper.setText(R.id.tv_comm_comment_inner_name, replyCommListBean.isIsReplyMain() ?
                        getStrings(replyCommListBean.getNickname()) :
                        (getStrings(replyCommListBean.getNickname()) + " 回复 " + getStrings(replyCommListBean.getNickname1())))
                        .setText(R.id.tv_comm_comment_inner_content, getStrings(replyCommListBean.getContent()))
                        .addOnClickListener(R.id.tv_comm_comment_like).setTag(R.id.tv_comm_comment_like, replyCommListBean)
                        .addOnClickListener(R.id.tv_comm_comment_receive).setTag(R.id.tv_comm_comment_receive, replyCommListBean)
                        .addOnClickListener(R.id.civ_comm_comment_inner_avatar).setTag(R.id.civ_comm_comment_inner_avatar, R.id.iv_tag, replyCommListBean)
                        .addOnLongClickListener(R.id.tv_comm_comment_inner_content).setTag(R.id.tv_comm_comment_inner_content, replyCommListBean);
                tv_comm_comment_like.setSelected(replyCommListBean.isIsFavor());
                tv_comm_comment_like.setText(replyCommListBean.getLike_num() > 0
                        ? String.valueOf(replyCommListBean.getLike_num()) : "赞");
                break;
            case ConstantVariable.TYPE_1:
                helper.itemView.setTag(replyCommListBean);
//                helper.addOnClickListener(R.id.tv_art_comment_inner_more).setTag(R.id.tv_art_comment_inner_more, replyCommListBean);
                break;
        }
    }
}
