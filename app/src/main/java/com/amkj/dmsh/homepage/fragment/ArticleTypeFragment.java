package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.bean.CategoryTypeEntity.CategoryTypeBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.activity.ArticleTypeActivity;
import com.amkj.dmsh.homepage.adapter.HomeArticleAdapter;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_URL;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/29
 * class description:文章分类
 */
public class ArticleTypeFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private int page = 1;
    private List<CommunalArticleBean> articleTypeList = new ArrayList();
    private CategoryTypeBean categoryTypeBean;
    private HomeArticleAdapter homeArticleAdapter;
    private CommunalArticleEntity categoryDocBean;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_recycler_loading;
    }

    @Override
    protected void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        communal_recycler.setLayoutManager(linearLayoutManager);
        homeArticleAdapter = new HomeArticleAdapter(getActivity(), articleTypeList);
        communal_recycler.setAdapter(homeArticleAdapter);
        homeArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalArticleBean communalArticleBean = (CommunalArticleBean) view.getTag();
                if (communalArticleBean != null) {
                    Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                    intent.putExtra("ArtId", String.valueOf(communalArticleBean.getId()));
                    startActivity(intent);
                }
            }
        });
        homeArticleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalArticleBean articleBean = (CommunalArticleBean) view.getTag();
                if (articleBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_com_art_collect_count:
                            if (userId > 0) {
                                setArticleCollected(articleBean);
                                articleTypeList.get(position).setCollect(!articleBean.isCollect());
                                articleTypeList.get(position).setCollect(!articleBean.isCollect() ?
                                        articleBean.getCollect() + 1 : articleBean.getCollect() - 1);
                                homeArticleAdapter.notifyItemChanged(position);
                            } else {
                                getLoginStatus(ArticleTypeFragment.this);
                            }
                            break;
                        case R.id.tv_com_art_like_count:
                            if (userId > 0) {
                                setArticleLiked(articleBean);
                                articleTypeList.get(position).setFavor(!articleBean.isFavor());
                                articleTypeList.get(position).setFavor(articleBean.isFavor() ?
                                        articleBean.getFavor() + 1 : articleBean.getFavor() - 1);
                                homeArticleAdapter.notifyItemChanged(position);
                            } else {
                                getLoginStatus(ArticleTypeFragment.this);
                            }
                            break;
                        case R.id.tv_article_type:
                            if (articleBean.getCategory_id() > 0
                                    && !TextUtils.isEmpty(articleBean.getCategory_name())) {
                                Intent intent = new Intent(getActivity(), ArticleTypeActivity.class);
                                intent.putExtra("categoryId", String.valueOf(articleBean.getCategory_id()));
                                intent.putExtra("categoryTitle", getStrings(articleBean.getCategory_name()));
                                startActivity(intent);
                            }
                            break;
                    }
                }
            }
        });
        homeArticleAdapter.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= articleTypeList.size()) {
                page++;
                getArticleTypeData();
            } else {
                homeArticleAdapter.loadMoreEnd();
            }
        }, communal_recycler);
    }

    //    收藏
    private void setArticleCollected(CommunalArticleBean articleBean) {
        String url = BASE_URL + Url.F_ARTICLE_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", articleBean.getId());
        params.put("type", "document");
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    //  点赞
    private void setArticleLiked(CommunalArticleBean articleBean) {
        String url = BASE_URL + Url.F_ARTICLE_DETAILS_FAVOR;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //文章id
        params.put("id", articleBean.getId());
        params.put("favortype", "doc");
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getArticleTypeData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    public void getArticleTypeData() {
        String url = BASE_URL + Url.CATE_DOC_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("uid", userId);
        }
        if (categoryTypeBean.getId() > 0) {
            params.put("categoryid", categoryTypeBean.getId());
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                homeArticleAdapter.loadMoreComplete();
                if (page == 1) {
                    articleTypeList.clear();
                }
                Gson gson = new Gson();
                categoryDocBean = gson.fromJson(result, CommunalArticleEntity.class);
                if (categoryDocBean != null) {
                    if (categoryDocBean.getCode().equals(SUCCESS_CODE)) {
                        articleTypeList.addAll(categoryDocBean.getCommunalArticleList());
                    } else if (!categoryDocBean.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), categoryDocBean.getMsg());
                    }
                    homeArticleAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,articleTypeList,categoryDocBean);
            }

            @Override
            public void netClose() {
                homeArticleAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,articleTypeList,categoryDocBean);
            }

            @Override
            public void onError(Throwable throwable) {
                homeArticleAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,articleTypeList,categoryDocBean);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        categoryTypeBean = (CategoryTypeBean) bundle.get("articleType");
    }
}
