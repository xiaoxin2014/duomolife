package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.CategoryTypeEntity.CategoryTypeBean;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.activity.ArticleTypeActivity;
import com.amkj.dmsh.homepage.adapter.ArticleListAdapter;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.Url.CATE_DOC_LIST;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_COLLECT;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_DETAILS_FAVOR;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/29
 * class description:文章分类
 */
public class ArticleListFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    private int page = 1;
    private List<CommunalArticleBean> articleTypeList = new ArrayList<>();
    private CategoryTypeBean categoryTypeBean;
    private ArticleListAdapter homeArticleAdapter;
    private CommunalArticleEntity categoryDocBean;
    private String sortType = "1";

    @Override
    protected int getContentView() {
        return R.layout.fragment_artical_list;
    }

    @Override
    protected void initViews() {
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            sortType = "1";
            ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
            ((RadioButton) mRadioGroup.getChildAt(1)).setChecked(false);
            loadData();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        communal_recycler.setLayoutManager(linearLayoutManager);
        homeArticleAdapter = new ArticleListAdapter(getActivity(), articleTypeList);
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
                        //收藏
                        case R.id.tv_com_art_collect_count:
                            if (userId > 0) {
                                setArticleCollected(articleBean);
                                articleTypeList.get(position).setCollect(!articleBean.isCollect());
                                articleTypeList.get(position).setCollect(!articleBean.isCollect() ?
                                        articleBean.getCollect() + 1 : articleBean.getCollect() - 1);
                                homeArticleAdapter.notifyItemChanged(position);
                            } else {
                                getLoginStatus(ArticleListFragment.this);
                            }
                            break;
                        //点赞
                        case R.id.tv_com_art_like_count:
                            if (userId > 0) {
                                setArticleLiked(articleBean);
                                articleTypeList.get(position).setFavor(!articleBean.isFavor());
                                articleTypeList.get(position).setFavor(articleBean.isFavor() ?
                                        articleBean.getFavor() + 1 : articleBean.getFavor() - 1);
                                homeArticleAdapter.notifyItemChanged(position);
                            } else {
                                getLoginStatus(ArticleListFragment.this);
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
                        case R.id.tv_com_art_comment_count:
                            Intent intent = new Intent(getActivity(), ArticleOfficialActivity.class);
                            intent.putExtra("ArtId", String.valueOf(articleBean.getId()));
                            intent.putExtra("scrollToComment", "1");
                            startActivity(intent);
                            break;
                    }
                }
            }
        });
        homeArticleAdapter.setOnLoadMoreListener(() -> {
            page++;
            getArticleTypeData();
        }, communal_recycler);

        mRadioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            if (!loadHud.isShowing()) loadHud.show();
            if (id == R.id.rb_new) {
                sortType = "1";
            } else if (id == R.id.rb_hot) {
                sortType = "2";
            }
            loadData();
        });
    }


    //    收藏
    private void setArticleCollected(CommunalArticleBean articleBean) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", articleBean.getId());
        params.put("type", "document");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), F_ARTICLE_COLLECT, params, null);
    }

    //  点赞
    private void setArticleLiked(CommunalArticleBean articleBean) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //文章id
        params.put("id", articleBean.getId());
        params.put("favortype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), F_ARTICLE_DETAILS_FAVOR, params, null);
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
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("uid", userId);
        }
        if (categoryTypeBean.getId() > 0) {
            params.put("categoryid", categoryTypeBean.getId());
        }

        if (!TextUtils.isEmpty(sortType)) {
            params.put("orderBy", sortType);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), CATE_DOC_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        mSmartLayout.finishRefresh();
                        loadHud.dismiss();
                        Gson gson = new Gson();
                        categoryDocBean = gson.fromJson(result, CommunalArticleEntity.class);
                        if (categoryDocBean != null) {
                            List<CommunalArticleBean> communalArticleList = categoryDocBean.getCommunalArticleList();
                            if (communalArticleList != null && communalArticleList.size() > 0) {
                                if (page == 1) {
                                    articleTypeList.clear();
                                }
                                articleTypeList.addAll(categoryDocBean.getCommunalArticleList());
                                homeArticleAdapter.notifyDataSetChanged();
                                homeArticleAdapter.loadMoreComplete();
                            } else if (ERROR_CODE.equals(categoryDocBean.getCode())) {
                                showToast( categoryDocBean.getMsg());
                                homeArticleAdapter.loadMoreFail();
                            } else {
                                homeArticleAdapter.loadMoreEnd();
                            }
                        } else {
                            homeArticleAdapter.loadMoreEnd();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, articleTypeList, categoryDocBean);
                    }

                    @Override
                    public void onNotNetOrException() {
                        loadHud.dismiss();
                        homeArticleAdapter.loadMoreFail();
                        mSmartLayout.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, articleTypeList, categoryDocBean);
                    }
                });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        categoryTypeBean = (CategoryTypeBean) bundle.get("articleType");
    }
}
