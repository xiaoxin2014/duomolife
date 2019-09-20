package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/31
 * class description:请输入类描述
 */

public class SpecialTopicAdapter extends BaseQuickAdapter<TopicSpecialBean, SpecialTopicAdapter.ArticleCoverViewHolder> {
    private final Context context;

    public SpecialTopicAdapter(Context context, List<TopicSpecialBean> specialSearList) {
        super(R.layout.adapter_home_art, specialSearList);
        this.context = context;
    }

    @Override
    protected void convert(ArticleCoverViewHolder helper, TopicSpecialBean item) {
        if (item == null) return;
        helper.setGone(R.id.rl_bottom, false);
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_article_cover), item.getPic_url());

        helper.setText(R.id.tv_article_title, getStrings(item.getTitle()))
                .setText(R.id.tv_article_descrip, getStrings(item.getDescription()));
        TextView tvView = helper.getView(R.id.tv_article_view_count);
        if (getStringChangeIntegers(item.getView()) > 0) {
            tvView.setVisibility(View.VISIBLE);
            tvView.setText(String.valueOf(item.getView()));
        } else {
            tvView.setVisibility(View.GONE);
        }

        helper.itemView.setTag(item);
    }

    public class ArticleCoverViewHolder extends BaseViewHolder {
        TextView tv_article_view_count;

        public ArticleCoverViewHolder(View view) {
            super(view);
            tv_article_view_count = view.findViewById(R.id.tv_article_view_count);
            if (tv_article_view_count != null) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                int radius = AutoSizeUtils.mm2px(mAppContext, 40);
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
