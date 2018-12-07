package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.HomeArticleAdapter;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_URL;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/11
 * class description:文章分类
 */
public class ArticleTypeActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private List<CommunalArticleBean> articleTypeList = new ArrayList();
    private HomeArticleAdapter homeArticleAdapter;
    private String categoryId;
    private String categoryTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_header_recycler_refresh;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(GONE);
        try {
            Intent intent = getIntent();
            categoryId = intent.getStringExtra("categoryId");
            categoryTitle = intent.getStringExtra("categoryTitle");
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_header_titleAll.setText(getStrings(categoryTitle));

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArticleTypeActivity.this);
        communal_recycler.setLayoutManager(linearLayoutManager);
        homeArticleAdapter = new HomeArticleAdapter(ArticleTypeActivity.this, articleTypeList);
        communal_recycler.setAdapter(homeArticleAdapter);
        homeArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalArticleBean communalArticleBean = (CommunalArticleBean) view.getTag();
                if (communalArticleBean != null) {
                    Intent intent = new Intent(ArticleTypeActivity.this, ArticleOfficialActivity.class);
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
                                getLoginStatus();
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
                                getLoginStatus();
                            }
                            break;
                        case R.id.tv_com_art_comment_count:
                            Intent intent = new Intent(ArticleTypeActivity.this, ArticleOfficialActivity.class);
                            intent.putExtra("ArtId", String.valueOf(articleBean.getId()));
                            intent.putExtra("scrollToComment", true);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        homeArticleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= articleTypeList.size()) {
                    page++;
                    getArticleTypeList();
                } else {
                    homeArticleAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (!personalInfo.isLogin()) {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
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
        getArticleTypeList();
    }

    private void getArticleTypeList() {
        String url = BASE_URL + Url.CATE_DOC_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("uid", userId);
        }
        params.put("categoryid", categoryId);
        NetLoadUtils.getQyInstance().loadNetDataPost(this, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        homeArticleAdapter.loadMoreComplete();
                        if (page == 1) {
                            articleTypeList.clear();
                        }
                        Gson gson = new Gson();
                        CommunalArticleEntity categoryDocBean = gson.fromJson(result, CommunalArticleEntity.class);
                        if (categoryDocBean != null) {
                            if (categoryDocBean.getCode().equals(SUCCESS_CODE)) {
                                articleTypeList.addAll(categoryDocBean.getCommunalArticleList());
                                if (articleTypeList.size() > 0
                                        && TextUtils.isEmpty(articleTypeList.get(0).getCategory_name())) {
                                    tv_header_titleAll.setText(articleTypeList.get(0).getCategory_name());
                                }
                            } else if (!categoryDocBean.getCode().equals(EMPTY_CODE)) {
                                showToast(ArticleTypeActivity.this, categoryDocBean.getMsg());
                            }
                            homeArticleAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, articleTypeList, categoryDocBean);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        homeArticleAdapter.loadMoreComplete();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, articleTypeList, null);
                        showToast(ArticleTypeActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        homeArticleAdapter.loadMoreComplete();
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, articleTypeList, null);
                    }
                });
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

}
