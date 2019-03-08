package com.amkj.dmsh.find.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.FeaturedArticleEntity.FeaturedArticleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/7
 * version 3.3.0
 * class description:精选文章
 */
public class FeaturedArticleAdapter extends BaseQuickAdapter<FeaturedArticleBean, BaseViewHolder> {
    private final Context context;

    public FeaturedArticleAdapter(Context context, List<FeaturedArticleBean> featuredArticleBeanList) {
        super(R.layout.adapter_layout_featured_article, featuredArticleBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FeaturedArticleBean featuredArticleBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_featured_article_cover), featuredArticleBean.getPicUrl());
        helper.setText(R.id.tv_featured_article_title, getStrings(featuredArticleBean.getTitle()))
                .setText(R.id.tv_featured_article_content, getStrings(featuredArticleBean.getContent()))
                .setText(R.id.tv_featured_article_publish_time, getStrings(featuredArticleBean.getTime()))
                .setText(R.id.tv_featured_article_count_like, featuredArticleBean.getLikeCount() > 0 ? String.valueOf(featuredArticleBean.getLikeCount()) : "赞")
                .setText(R.id.tv_featured_article_count_comment, featuredArticleBean.getCommentCount() > 0 ? String.valueOf(featuredArticleBean.getCommentCount()) : "评论")
                .addOnClickListener(R.id.tv_featured_article_count_like)
                .setTag(R.id.tv_featured_article_count_like, featuredArticleBean)
                .addOnClickListener(R.id.tv_featured_article_count_comment)
                .setTag(R.id.tv_featured_article_count_comment, featuredArticleBean);
        RecyclerView communal_recycler_wrap = helper.getView(R.id.communal_recycler_wrap);
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
        FeaturedRelevanceProductAdapter featuredRelevanceProductAdapter = new FeaturedRelevanceProductAdapter(context, new ArrayList<>());
        communal_recycler_wrap.setAdapter(featuredRelevanceProductAdapter);
        featuredRelevanceProductAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }
}
