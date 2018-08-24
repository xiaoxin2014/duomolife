package com.amkj.dmsh.user.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by LGuipeng on 2016/9/11.
 */
public class UserLikedArticleAdapter extends BaseQuickAdapter<CommunalArticleBean, BaseViewHolderHelper> {
    private final Context context;

    public UserLikedArticleAdapter(Context context, List<CommunalArticleBean> articleList) {
        super(R.layout.adapter_art_communal, articleList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, CommunalArticleBean bean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_com_art_cover), bean.getPath());
        helper.setGone(R.id.tv_com_art_hot_tag, true)
                .setText(R.id.tv_com_art_hot_tag, getStrings(bean.getCategory_name()))
                .addOnClickListener(R.id.tv_com_art_hot_tag).setTag(R.id.tv_com_art_hot_tag, bean)
                .setText(R.id.tv_com_art_title, getStrings(bean.getTitle()));
//                .setText(R.id.tv_homepage_hot_comment,"" + bean.getComment())
//                .setText(R.id.tv_homepage_hot_collect,"" + bean.getFavor());
        helper.itemView.setTag(bean);
    }
}
