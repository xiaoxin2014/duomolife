package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.HomeArticleAdapter;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
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
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private int uid;
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
        isLoginStatus();
        tv_header_shared.setVisibility(GONE);
        try {
            Intent intent = getIntent();
            categoryId = intent.getStringExtra("categoryId");
            categoryTitle = intent.getStringExtra("categoryTitle");
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_header_titleAll.setText(getStrings(categoryTitle));

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
            loadData();
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
                            if (uid > 0) {
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
                            if (uid > 0) {
                                setArticleLiked(articleBean);
                                articleTypeList.get(position).setFavor(!articleBean.isFavor());
                                articleTypeList.get(position).setFavor(articleBean.isFavor() ?
                                        articleBean.getFavor() + 1 : articleBean.getFavor() - 1);
                                homeArticleAdapter.notifyItemChanged(position);
                            } else {
                                getLoginStatus();
                            }
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
                    loadData();
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
                    BaseApplication app = (BaseApplication) getApplication();
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

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(this, MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getLoginStatus();
            loadData();
        }
    }

    //    收藏
    private void setArticleCollected(CommunalArticleBean articleBean) {
        String url = BASE_URL + Url.F_ARTICLE_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", uid);
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
        params.put("tuid", uid);
        //文章id
        params.put("id", articleBean.getId());
        params.put("favortype", "doc");
        XUtil.Post(url, params, new MyCallBack<String>() {
        });
    }

    @Override
    protected void loadData() {
        if (NetWorkUtils.checkNet(ArticleTypeActivity.this) && !TextUtils.isEmpty(categoryId)) {
            String url = BASE_URL + Url.CATE_DOC_LIST;
            Map<String, Object> params = new HashMap<>();
            params.put("currentPage", page);
            if (uid > 0) {
                params.put("uid", uid);
            }
            params.put("categoryid", categoryId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    communal_load.setVisibility(GONE);
                    communal_error.setVisibility(GONE);
                    smart_communal_refresh.finishRefresh();
                    homeArticleAdapter.loadMoreComplete();
                    if (page == 1) {
                        articleTypeList.clear();
                    }
                    Gson gson = new Gson();
                    CommunalArticleEntity categoryDocBean = gson.fromJson(result, CommunalArticleEntity.class);
                    if (categoryDocBean != null) {
                        if (categoryDocBean.getCode().equals("01")) {
                            articleTypeList.addAll(categoryDocBean.getCommunalArticleList());
                            if(articleTypeList.size()>0&&TextUtils.isEmpty(articleTypeList.get(0).getCategory_name())){
                                tv_header_titleAll.setText(articleTypeList.get(0).getCategory_name());
                            }
                        } else if (!categoryDocBean.getCode().equals("02")) {
                            showToast(ArticleTypeActivity.this, categoryDocBean.getMsg());
                        }
                        if (page == 1) {
                            homeArticleAdapter.setNewData(articleTypeList);
                        } else {
                            homeArticleAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(GONE);
                    communal_error.setVisibility(View.VISIBLE);
                    homeArticleAdapter.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            communal_load.setVisibility(GONE);
            communal_error.setVisibility(View.VISIBLE);
            showToast(ArticleTypeActivity.this, R.string.unConnectedNetwork);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(GONE);
        communal_error.setVisibility(GONE);
        loadData();
    }

}
