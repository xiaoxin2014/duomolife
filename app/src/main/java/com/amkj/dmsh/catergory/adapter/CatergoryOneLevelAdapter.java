package com.amkj.dmsh.catergory.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.RelateArticleBean;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.RelateArticleBean.ArticlesBean;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.activity.CatergoryTwoLevelActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TWO_LEVEL_LIST;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新版首页 viewPager适配器
 */

public class CatergoryOneLevelAdapter extends BaseQuickAdapter<CatergoryOneLevelBean, BaseViewHolder> {

    private final Context mContext;

    public CatergoryOneLevelAdapter(Context context, @Nullable List<CatergoryOneLevelBean> data) {
        super(R.layout.item_catergory, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CatergoryOneLevelBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_top_cover), getStrings(item.getPicUrl()));
        //初始化文章数据
        RelateArticleBean relateArticle = item.getRelateArticle();
        helper.getView(R.id.ll_artical).setVisibility(relateArticle != null && relateArticle.getArticles() != null && relateArticle.getArticles().size() > 0 ? View.VISIBLE : View.GONE);
        helper.getView(R.id.iv_top_cover).setOnClickListener(view -> {
            skipCatergoryTwoLevel(item, 0);
        });
        if (relateArticle != null) {
            List<RelateArticleBean.ArticlesBean> articlesList = relateArticle.getArticles();
            if (articlesList != null && articlesList.size() > 0) {
                ArticlesBean articleLeftBean = articlesList.get(0);
                GlideImageLoaderUtil.loadRoundImg(mContext, helper.getView(R.id.iv_left_cover), articleLeftBean.getDocumentPicurl(), AutoSizeUtils.mm2px(mAppContext, 10));
                helper.setText(R.id.tv_left_title, articleLeftBean.getDocumentName())
                        .addOnClickListener(R.id.fl_artical_left).setTag(R.id.fl_artical_left, relateArticle).
                        addOnClickListener(R.id.rl_more_artical).setTag(R.id.rl_more_artical, relateArticle);
                if (articlesList.size() > 1) {
                    ArticlesBean articleRightBean = articlesList.get(1);
                    GlideImageLoaderUtil.loadRoundImg(mContext, helper.getView(R.id.iv_right_cover), articleRightBean.getDocumentPicurl(), AutoSizeUtils.mm2px(mAppContext, 10));
                    helper.setText(R.id.tv_right_title, articleRightBean.getDocumentName())
                            .addOnClickListener(R.id.fl_artical_right).setTag(R.id.fl_artical_right, relateArticle);
                }
            }
        }

        //初始化二级分类
        RecyclerView rvTwoCatergory = helper.getView(R.id.rv_child_catergory);
        rvTwoCatergory.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvTwoCatergory.setNestedScrollingEnabled(false);
        BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter<ChildCategoryListBean, BaseViewHolder>(R.layout.item_child_catergory, item.getChildCategoryList()) {
            @Override
            protected void convert(BaseViewHolder helper, ChildCategoryListBean item) {
                GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover), item.getPicUrl());
                helper.setText(R.id.tv_name, item.getName());
                helper.itemView.setTag(item);
            }
        };
        baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
            //进入二级分类页面
            ChildCategoryListBean childCategoryListBean = (ChildCategoryListBean) view.getTag();
            if (childCategoryListBean != null) {
                skipCatergoryTwoLevel(item, position+1);
            }
        });
        rvTwoCatergory.setAdapter(baseQuickAdapter);
    }

    private void skipCatergoryTwoLevel(CatergoryOneLevelBean item, int i) {
        Intent intent = new Intent(mContext, CatergoryTwoLevelActivity.class);
        intent.putParcelableArrayListExtra(CATEGORY_TWO_LEVEL_LIST, (ArrayList<? extends Parcelable>) item.getChildCategoryList());
        intent.putExtra("position", i);
        intent.putExtra(ConstantVariable.CATEGORY_PID, item.getId());
        intent.putExtra(ConstantVariable.CATEGORY_NAME, item.getName());
        mContext.startActivity(intent);
    }
}
