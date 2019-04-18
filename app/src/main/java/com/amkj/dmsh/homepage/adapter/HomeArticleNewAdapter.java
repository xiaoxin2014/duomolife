package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/4/16 0016
 * Version:v4.0.0
 * ClassDescription :新版首页文章适配（种草特辑）
 */

public class HomeArticleNewAdapter extends BaseQuickAdapter<CommunalArticleBean, BaseViewHolder> {

    private final Context mContext;

    public HomeArticleNewAdapter(Context context, @Nullable List<CommunalArticleBean> data) {
        super(R.layout.item_home_artical_new, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunalArticleBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_artical_cover), item.getPath());
        helper.setText(R.id.tv_artical_title, getStrings(item.getTitle()))
                .setText(R.id.tv_artical_desc, getStrings(item.getDigest()));
        helper.itemView.setTag(item);
    }
}
