package com.amkj.dmsh.shopdetails.adapter;

import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dominant.bean.DmlSearchCommentEntity.DmlSearchCommentBean.ReplyCommListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/7/12
 * Version:v4.1.0
 * ClassDescription :发现-帖子详情-子评论列表适配器
 */
public class PostChidCommentAdapter extends BaseQuickAdapter<ReplyCommListBean, BaseViewHolder> {

    private final BaseActivity mContext;

    public PostChidCommentAdapter(BaseActivity context, @Nullable List<ReplyCommListBean> data) {
        super(R.layout.item_post_child_comment, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyCommListBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadRoundImg(mContext, helper.getView(R.id.civ_comm_comment_inner_avatar), item.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 50));
        helper.setText(R.id.tv_user_name, getStrings(item.getNickname()) + (item.isIsReplyMain() ? "" : " 回复 " + getStrings(item.getNickname1())))
                .setText(R.id.tv_comment_content, getStrings(item.getContent()));
    }
}
