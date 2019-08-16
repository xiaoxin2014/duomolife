package com.amkj.dmsh.dominant.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean.ReplyCommListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/7/27
 * Version:v4.1.0
 * ClassDescription :发现-帖子-评论详情
 */
public class ArticleCommentDetailAdapter extends BaseQuickAdapter<ReplyCommListBean, BaseViewHolder> {
    private final Context context;

    public ArticleCommentDetailAdapter(Context context, List<ReplyCommListBean> replyCommList) {
        super(R.layout.item_child_comment, replyCommList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyCommListBean item) {
        GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.civ_comm_comment_inner_avatar), item.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 50));
        helper.setText(R.id.tv_comm_comment_inner_name, getStrings(item.getNickname() + (item.isReplyMain() ? "" : " 回复 " + item.getNickname1())))
                .setText(R.id.tv_comm_comment_inner_content, getStrings(item.getContent()));
        helper.getView(R.id.civ_comm_comment_inner_avatar).setOnClickListener(v -> ConstantMethod.skipUserCenter(context, item.getUid()));
        helper.itemView.setTag(item);
    }
}
