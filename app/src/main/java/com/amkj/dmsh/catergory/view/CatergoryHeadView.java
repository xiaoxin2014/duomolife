package com.amkj.dmsh.catergory.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.RelateArticleBean.ArticlesBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.activity.ArticleTypeActivity;
import com.amkj.dmsh.homepage.activity.CatergoryTwoLevelActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TWO_LEVEL_LIST;

/**
 * Created by xiaoxin on 2019/4/22 0022
 * Version:v4.0.0
 * ClassDescription :
 */
public class CatergoryHeadView extends LinearLayout {

    @BindView(R.id.iv_top_cover)
    ImageView mIvTopCover;
    @BindView(R.id.tv_topic_name)
    TextView mTvTopicName;
    @BindView(R.id.tv_more_artical)
    TextView mTvMoreArtical;
    @BindView(R.id.rl_more_artical)
    RelativeLayout mRlMoreArtical;
    @BindView(R.id.iv_left_cover)
    ImageView mIvLeftCover;
    @BindView(R.id.tv_left_title)
    TextView mTvLeftTitle;
    @BindView(R.id.fl_artical_left)
    FrameLayout mFlArticalLeft;
    @BindView(R.id.iv_right_cover)
    ImageView mIvRightCover;
    @BindView(R.id.tv_right_title)
    TextView mTvRightTitle;
    @BindView(R.id.fl_artical_right)
    FrameLayout mFlArticalRight;
    @BindView(R.id.ll_artical)
    LinearLayout mLlArtical;
    @BindView(R.id.rv_child_catergory)
    RecyclerView mRvChildCatergory;
    private Activity mContext;

    public CatergoryHeadView(Activity activity, CatergoryOneLevelBean catergoryOneLevelBean) {
        this(activity, catergoryOneLevelBean, null);
        mContext = activity;
    }

    public CatergoryHeadView(Context context, CatergoryOneLevelBean CatergoryOneLevelBean, AttributeSet attrs) {
        super(context, attrs);
        View headView = LayoutInflater.from(context).inflate(R.layout.item_catergory_head, this, true);
        ButterKnife.bind(this, headView);
        updateView(CatergoryOneLevelBean);
    }

    public void updateView(CatergoryOneLevelBean CatergoryOneLevelBean) {
        if (CatergoryOneLevelBean != null) {
            GlideImageLoaderUtil.loadImage(mContext, mIvTopCover, getStrings(CatergoryOneLevelBean.getPicUrl()));
            //初始化文章数据
            CatergoryOneLevelEntity.CatergoryOneLevelBean.RelateArticleBean relateArticleBean = CatergoryOneLevelBean.getRelateArticle();
            mTvTopicName.setText(getStrings(CatergoryOneLevelBean.getName()));
            mRlMoreArtical.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ArticleTypeActivity.class);
                intent.putExtra("categoryTitle", relateArticleBean.getCategoryName());
                intent.putExtra("categoryId", relateArticleBean.getArticles().get(0).getArticleCategoryId());
                mContext.startActivity(intent);
            });
            mLlArtical.setVisibility(relateArticleBean != null && relateArticleBean.getArticles() != null && relateArticleBean.getArticles().size() > 0 ? View.VISIBLE : View.GONE);
            if (relateArticleBean != null) {
                List<ArticlesBean> articlesList = relateArticleBean.getArticles();
                if (articlesList != null && articlesList.size() > 0) {
                    ArticlesBean articleLeftBean = articlesList.get(0);
                    GlideImageLoaderUtil.loadRoundImg(mContext, mIvLeftCover, articleLeftBean.getDocumentPicurl(), AutoSizeUtils.mm2px(mAppContext, 5));
                    mTvLeftTitle.setText(ConstantMethod.getStrings(articleLeftBean.getDocumentName()));
                    mFlArticalLeft.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, ArticleOfficialActivity.class);
                            intent.putExtra("ArtId", String.valueOf(relateArticleBean.getArticles().get(0).getDocumentId()));
                            mContext.startActivity(intent);
                        }
                    });
                    if (articlesList.size() > 1) {
                        ArticlesBean articleRightBean = articlesList.get(1);
                        GlideImageLoaderUtil.loadRoundImg(mContext, mIvRightCover, articleRightBean.getDocumentPicurl(), AutoSizeUtils.mm2px(mAppContext, 5));
                        mTvRightTitle.setText(getStrings(articleRightBean.getDocumentName()));
                        mFlArticalRight.setOnClickListener(view -> {
                            Intent intent = new Intent(mContext, ArticleOfficialActivity.class);
                            intent.putExtra("ArtId", String.valueOf(relateArticleBean.getArticles().get(1).getDocumentId()));
                            mContext.startActivity(intent);
                        });

                    }
                }
            }

            //初始化二级分类
            mRvChildCatergory.setLayoutManager(new GridLayoutManager(mContext, 4));
            mRvChildCatergory.setNestedScrollingEnabled(false);
            BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter<CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean, BaseViewHolder>(R.layout.item_child_catergory, CatergoryOneLevelBean.getChildCategoryList()) {
                @Override
                protected void convert(BaseViewHolder helper, CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean item) {
                    GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover), item.getPicUrl());
                    helper.setText(R.id.tv_name, item.getName());
                    helper.itemView.setTag(item);
                }
            };
            baseQuickAdapter.setOnItemClickListener((adapter, view, position) -> {
                //进入二级分类页面
                CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean childCategoryListBean = (CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean) view.getTag();
                if (childCategoryListBean != null) {
                    Intent intent = new Intent(mContext, CatergoryTwoLevelActivity.class);
                    intent.putParcelableArrayListExtra(CATEGORY_TWO_LEVEL_LIST, (ArrayList<? extends Parcelable>) CatergoryOneLevelBean.getChildCategoryList());
                    intent.putExtra("position", position);
                    intent.putExtra(ConstantVariable.CATEGORY_NAME, CatergoryOneLevelBean.getName());
                    mContext.startActivity(intent);
                }
            });
            mRvChildCatergory.setAdapter(baseQuickAdapter);
        }
    }
}
