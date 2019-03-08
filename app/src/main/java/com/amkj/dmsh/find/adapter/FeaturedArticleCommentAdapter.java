package com.amkj.dmsh.find.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.FeaturedArticleCommentEntity.FeaturedArticleCommentBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/7
 * version 3.3.0
 * class description:精选留言--精选文章
 */
public class FeaturedArticleCommentAdapter extends BaseQuickAdapter<FeaturedArticleCommentBean, BaseViewHolder> {
    private final Context context;

    public FeaturedArticleCommentAdapter(Context context, List<FeaturedArticleCommentBean> featuredArticleCommentBeans) {
        super(R.layout.adapter_layout_featured_article_comment, featuredArticleCommentBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FeaturedArticleCommentBean featuredArticleCommentBean) {
        GlideImageLoaderUtil.loadHeaderImg(context,helper.getView(R.id.civ_feature_comment_avatar),featuredArticleCommentBean.getAvatar());
        helper.addOnClickListener(R.id.tv_featured_comment_liked)
                .setTag(R.id.tv_featured_comment_liked,featuredArticleCommentBean)
                .setText(R.id.tv_featured_comment_nickname,getStrings(featuredArticleCommentBean.getNickName()))
                .setText(R.id.tv_featured_comment_time,getStrings(featuredArticleCommentBean.getTime()))
                .setText(R.id.tv_featured_comment_liked,String.valueOf(featuredArticleCommentBean.getLikeCount()))
                .setText(R.id.tv_featured_comment_content,getStrings(featuredArticleCommentBean.getContent()))
                .setTag(R.id.tv_featured_comment_time, featuredArticleCommentBean);
    }
}
