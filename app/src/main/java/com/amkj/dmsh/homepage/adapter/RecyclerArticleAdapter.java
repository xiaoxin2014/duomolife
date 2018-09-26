package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by atd48 on 2016/6/30.
 */
public class RecyclerArticleAdapter extends BaseQuickAdapter<CommunalArticleBean, BaseViewHolder> {
    private final Context context;
    private final String type;

    public RecyclerArticleAdapter(Context context, List<CommunalArticleBean> communalArticleList, String type) {
        super(R.layout.adapter_art_communal, communalArticleList);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, CommunalArticleBean articleBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) holder.getView(R.id.iv_com_art_cover), articleBean.getPath());
        TextView tv_homepage_hot_tag = holder.getView(R.id.tv_com_art_hot_tag);
        if (type == "ArticleType") {
            tv_homepage_hot_tag.setVisibility(View.GONE);
        } else {
            tv_homepage_hot_tag.setVisibility(View.VISIBLE);
            tv_homepage_hot_tag.setText(getStrings(articleBean.getCategory_name()));
        }
        tv_homepage_hot_tag.setTag(articleBean);
        holder.setText(R.id.tv_com_art_title, getStrings(articleBean.getTitle()))
                .setText(R.id.tv_com_art_digest, getStrings(articleBean.getDigest()))
                .setText(R.id.tv_com_art_comment_count, articleBean.getComment() > 0 ? String.valueOf(articleBean.getComment() > 999
                        ? "999+" : articleBean.getComment()) : "评论")
                .addOnClickListener(R.id.tv_com_art_collect_count).setTag(R.id.tv_com_art_collect_count, articleBean)
                .addOnClickListener(R.id.tv_com_art_like_count).setTag(R.id.tv_com_art_like_count, articleBean);
        TextView tv_com_art_collect_count = holder.getView(R.id.tv_com_art_collect_count);
        TextView tv_com_art_like_count = holder.getView(R.id.tv_com_art_like_count);
//        是否被收藏
        tv_com_art_collect_count.setSelected(articleBean.isCollect());
//        是否点赞
        tv_com_art_like_count.setSelected(articleBean.isFavor());
        tv_com_art_collect_count.setText(articleBean.getCollect() > 0 ? String.valueOf(articleBean.getComment() > 999
                ? "999+" : articleBean.getCollect()) : "收藏");
        tv_com_art_like_count.setText(articleBean.getFavor() > 0 ? String.valueOf(articleBean.getFavor() > 999
                ? "999+" : articleBean.getFavor()) : "赞");
        TextView tv_com_art_view_count = holder.getView(R.id.tv_com_art_view_count);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = (int) AutoUtils.getPercentWidth1px() * 40;
        drawable.setCornerRadius(radius);
        try {
            drawable.setColor(0x7f000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_com_art_view_count.setBackground(drawable);
        if (articleBean.getView() > 0) {
            tv_com_art_view_count.setVisibility(View.VISIBLE);
            tv_com_art_view_count.setText(String.valueOf(articleBean.getView()));
        } else {
            tv_com_art_view_count.setVisibility(View.GONE);
        }
        holder.itemView.setTag(articleBean);
    }
}
