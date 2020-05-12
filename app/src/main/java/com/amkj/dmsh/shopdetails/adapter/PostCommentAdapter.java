package com.amkj.dmsh.shopdetails.adapter;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.dominant.activity.CommentDetailsActivity;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean;
import com.amkj.dmsh.dominant.bean.PostCommentEntity.PostCommentBean.ReplyCommListBean;
import com.amkj.dmsh.find.view.PostReplyPw;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipUserCenter;

/**
 * Created by xiaoxin on 2019/7/12
 * Version:v4.1.0
 * ClassDescription :发现-帖子详情-评论列表适配器
 */
public class PostCommentAdapter extends BaseQuickAdapter<PostCommentBean, BaseViewHolder> {

    private final BaseActivity context;
    private final List<PostCommentBean> mDatas;
    private PostReplyPw mChildPostCommentPw;
    private String commentType;

    public PostCommentAdapter(BaseActivity context, @Nullable List<PostCommentBean> data, String commentType) {
        super(R.layout.item_post_comment, data);
        mDatas = data;
        this.context = context;
        this.commentType = commentType;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostCommentBean item) {
        if (item == null) return;
        ImageView ivHead = helper.getView(R.id.iv_head);
        GlideImageLoaderUtil.loadRoundImg(context, ivHead, item.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 70), R.drawable.default_ava_img);
        helper.setText(R.id.tv_user_name, getStrings(item.getNickname()))
                .setText(R.id.tv_time, getStrings(item.getCtime()))
                .setText(R.id.tv_comment_content, getStrings(item.getContent()))
                .setText(R.id.tv_favor, getStrings(String.valueOf(item.getLike_num() > 0 ? item.getLike_num() : "赞")));
        ConstantMethod.setTextLink(context, helper.getView(R.id.tv_comment_content));
        View view = helper.getView(R.id.view_divider);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = helper.getAdapterPosition() == mDatas.size() - 1 ? 0 : AutoSizeUtils.mm2px(mAppContext, 120);

        TextView tvFavor = helper.getView(R.id.tv_favor);
        tvFavor.setSelected(item.isFavor());
        TextView tvMoreChild = helper.getView(R.id.tv_more_child_comment);

        //评论点赞
        tvFavor.setOnClickListener(v -> SoftApiDao.favorComment(context, item, tvFavor));

        //进入用户中心
        ivHead.setOnClickListener(v -> skipUserCenter(context, item.getUid()));

        //获取更多子评论
        tvMoreChild.setOnClickListener(v -> {
            Intent intent = new Intent(context, CommentDetailsActivity.class);
            intent.putExtra("commentId", String.valueOf(item.getId()));
            intent.putExtra("objId", String.valueOf(item.getObj_id()));
            intent.putExtra("commentType", commentType);
            context.startActivity(intent);
        });

        //初始化子评论列表
        RecyclerView rvComment = helper.getView(R.id.rv_child_comment);
        rvComment.setNestedScrollingEnabled(false);
        List<ReplyCommListBean> replyCommList = item.getReplyCommList();
        if (replyCommList != null && replyCommList.size() > 0) {
            rvComment.setVisibility(View.VISIBLE);
            tvMoreChild.setVisibility(replyCommList.size() >= 20 ? View.VISIBLE : View.GONE);
            BaseQuickAdapter chidCommentAdapter = new BaseQuickAdapter<ReplyCommListBean, BaseViewHolder>(R.layout.item_post_child_comment, replyCommList) {
                @Override
                protected void convert(BaseViewHolder helper, ReplyCommListBean item) {
                    if (item == null) return;
                    GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.civ_comm_comment_inner_avatar), item.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 50), R.drawable.default_ava_img);
                    helper.setText(R.id.tv_comm_comment_inner_name, getStrings(item.getNickname() + (item.isReplyMain() ? "" : " 回复 " + item.getNickname1())))
                            .setText(R.id.tv_comm_comment_inner_content, getStrings(item.getContent()));
                    ConstantMethod.setTextLink(context, helper.getView(R.id.tv_comm_comment_inner_content));
                    helper.getView(R.id.civ_comm_comment_inner_avatar).setOnClickListener(v -> skipUserCenter(context, item.getUid()));
                    helper.itemView.setOnLongClickListener(v -> {
                        mChildPostCommentPw = new PostReplyPw(context, item.getId(), 2) {
                            @Override
                            public void onCommentClick() {
                                EventBus.getDefault().post(new EventMessage("replyChildComment", item));
                            }
                        };

                        mChildPostCommentPw.showAsDropDown(helper.getView(R.id.tv_comm_comment_inner_content), 30, 0);
                        return true;
                    });
                    helper.itemView.setOnClickListener((View v) -> EventBus.getDefault().post(new EventMessage("replyChildComment", item)));
                }
            };
            rvComment.setLayoutManager(new LinearLayoutManager(context));
            rvComment.setAdapter(chidCommentAdapter);
        } else {
            rvComment.setVisibility(View.GONE);
            tvMoreChild.setVisibility(View.GONE);
        }

        helper.itemView.setTag(item);
    }

    public PostReplyPw getChildPostCommentPw() {
        return mChildPostCommentPw;
    }
}
