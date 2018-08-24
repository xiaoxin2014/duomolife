package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/28
 * class description:请输入类描述
 */

public class HomeArticleAdapter extends BaseQuickAdapter<CommunalArticleBean, HomeArticleAdapter.ArticleCoverViewHolder> {
    private final Context context;

    public HomeArticleAdapter(Context context, List<CommunalArticleBean> articleBeanList) {
        super(R.layout.adapter_home_art, articleBeanList);
        this.context = context;
    }

    @Override
    protected void convert(ArticleCoverViewHolder helper, CommunalArticleBean communalArticleBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_article_cover), communalArticleBean.getPath());
        helper.setText(R.id.tv_article_title, getStrings(communalArticleBean.getTitle()))
                .setText(R.id.tv_article_descrip, getStrings(communalArticleBean.getDigest()))
                .setText(R.id.tv_com_art_comment_count, communalArticleBean.getComment() > 0 ? String.valueOf(communalArticleBean.getComment() > 999 ? "999+" : communalArticleBean.getComment()) : "评论")
                .setText(R.id.tv_com_art_like_count, communalArticleBean.getFavor() > 0 ? String.valueOf(communalArticleBean.getFavor() > 999 ? "999+" : communalArticleBean.getFavor()) : "赞")
                .addOnClickListener(R.id.tv_com_art_like_count).setTag(R.id.tv_com_art_like_count, communalArticleBean)
                .addOnClickListener(R.id.tv_article_type).setTag(R.id.tv_article_type, communalArticleBean);
//        文章分类
        TextView tv_article_type = helper.getView(R.id.tv_article_type);
        String tagName = communalArticleBean.getCategory_name();
        if (!TextUtils.isEmpty(tagName)) {
            String content = "来自于 " + tagName;
            Link replyNameLink = new Link(tagName);
//                    回复颜色
            replyNameLink.setTextColor(Color.parseColor("#0a88fa"));
            replyNameLink.setUnderlined(false);
            replyNameLink.setHighlightAlpha(0f);
            tv_article_type.setText(content);
            LinkBuilder.on(tv_article_type)
                    .setText(content)
                    .addLink(replyNameLink)
                    .build();
        }
//        点赞数
        TextView tv_com_art_like_count = helper.getView(R.id.tv_com_art_like_count);
//        是否点赞
        tv_com_art_like_count.setSelected(communalArticleBean.isFavor());

        if (communalArticleBean.getView() > 0) {
            helper.tv_article_view_count.setVisibility(View.VISIBLE);
            helper.tv_article_view_count.setText(String.valueOf(communalArticleBean.getView()));
        } else {
            helper.tv_article_view_count.setVisibility(View.GONE);
        }
        helper.itemView.setTag(communalArticleBean);
    }

    public class ArticleCoverViewHolder extends BaseViewHolderHelper{
        TextView tv_article_view_count;
        public ArticleCoverViewHolder(View view) {
            super(view);
            tv_article_view_count = view.findViewById(R.id.tv_article_view_count);
            if(tv_article_view_count!=null){
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                int radius = (int) AutoUtils.getPercentWidth1px() * 40;
                drawable.setCornerRadius(radius);
                try {
                    drawable.setColor(0x7f000000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_article_view_count.setBackground(drawable);
            }
        }
    }
}
