package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.RecyclerArticleAdapter;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kingja.loadsir.callback.SuccessCallback;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:生活研究所 良品-种草营
 */
public class QualityDMLLifeSearchActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private List<CommunalArticleBean> communalArtList = new ArrayList<>();
    private RecyclerArticleAdapter recyclerArticleAdapter;
    private Badge badge;
    private CommunalArticleEntity communalArticleEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        iv_img_share.setVisibility(View.GONE);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        tv_header_titleAll.setText("");
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityDMLLifeSearchActivity.this));
        recyclerArticleAdapter = new RecyclerArticleAdapter(QualityDMLLifeSearchActivity.this, communalArtList, "ArticleType");
        communal_recycler.setAdapter(recyclerArticleAdapter);
        recyclerArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position >= 0) {
                    CommunalArticleBean communalArticleBean = communalArtList.get(position);
                    Intent intent = new Intent(QualityDMLLifeSearchActivity.this, ArticleOfficialActivity.class);
                    intent.putExtra("ArtId", String.valueOf(communalArticleBean.getId()));
                    startActivity(intent);
                }
            }
        });
        recyclerArticleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalArticleBean articleBean = (CommunalArticleBean) view.getTag();
                if (articleBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_com_art_collect_count:
                            if (userId > 0) {
                                setArticleCollected(articleBean, position + adapter.getHeaderLayoutCount());
                            } else {
                                getLoginStatus(QualityDMLLifeSearchActivity.this);
                            }
                            break;
                        case R.id.tv_com_art_like_count:
                            if (userId > 0) {
                                setArticleLiked(articleBean);
                                communalArtList.get(position).setFavor(!articleBean.isFavor());
                                communalArtList.get(position).setFavor(articleBean.isFavor() ?
                                        articleBean.getFavor() + 1 : articleBean.getFavor() - 1);
                                recyclerArticleAdapter.notifyItemChanged(position);
                            } else {
                                getLoginStatus(QualityDMLLifeSearchActivity.this);
                            }
                            break;
                    }
                }
            }
        });
        recyclerArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalArticleBean articleBean = (CommunalArticleBean) view.getTag();
                if (articleBean != null) {
                    Intent intent = new Intent(QualityDMLLifeSearchActivity.this, DmlLifeSearchDetailActivity.class);
                    intent.putExtra("searchId", String.valueOf(articleBean.getId()));
                    startActivity(intent);
                }
            }
        });

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
            loadData();
            scrollY = 0;

        });
        recyclerArticleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * 10 <= recyclerArticleAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    recyclerArticleAdapter.loadMoreEnd();
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
                    download_btn_communal.setVisibility(View.GONE);
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
        badge = ConstantMethod.getBadge(QualityDMLLifeSearchActivity.this, fl_header_service);
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected void getData() {
        //列表
        getSearchData();
        getCarCount();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals("02")) {
                            showToast(QualityDMLLifeSearchActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    // 分类文章列表
    private void getSearchData() {
        String url = Url.BASE_URL + Url.Q_DML_SEARCH_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("fuid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(QualityDMLLifeSearchActivity.this, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                recyclerArticleAdapter.loadMoreComplete();
                Gson gson = new Gson();
                communalArticleEntity = gson.fromJson(result, CommunalArticleEntity.class);
                if (communalArticleEntity != null) {
                    if (communalArticleEntity.getCode().equals(SUCCESS_CODE)) {
                        if (page == 1) {
                            communalArtList.clear();
                        }
                        tv_header_titleAll.setText(getStrings(communalArticleEntity.getTitle()));
                        communalArtList.addAll(communalArticleEntity.getCommunalArticleList());
                    } else if (!communalArticleEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(QualityDMLLifeSearchActivity.this, communalArticleEntity.getMsg());
                    }
                    NetLoadUtils.getQyInstance().showLoadSir(loadService,communalArtList,communalArticleEntity);
                    recyclerArticleAdapter.notifyDataSetChanged();
                }else{
                    if(loadService!=null){
                        loadService.showCallback(SuccessCallback.class);
                    }
                }
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                recyclerArticleAdapter.loadMoreComplete();
                showToast(QualityDMLLifeSearchActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,communalArtList,communalArticleEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                recyclerArticleAdapter.loadMoreComplete();
                showToast(QualityDMLLifeSearchActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,communalArtList,communalArticleEntity);
            }
        });
    }

    //    收藏
    private void setArticleCollected(final CommunalArticleBean articleBean, final int position) {
        loadHud.show();
        String url = Url.BASE_URL + Url.F_ARTICLE_COLLECT;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", articleBean.getId());
        params.put("type", ConstantVariable.TYPE_C_WELFARE);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                communalArtList.get(position).setCollect(!articleBean.isCollect());
                communalArtList.get(position).setCollect(!articleBean.isCollect() ?
                        articleBean.getCollect() + 1 : articleBean.getCollect() - 1);
                recyclerArticleAdapter.notifyItemChanged(position);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                showToast(QualityDMLLifeSearchActivity.this, String.format(getResources().getString(R.string.collect_failed), "文章"));
                super.onError(ex, isOnCallback);
            }
        });
    }

    //  点赞
    private void setArticleLiked(CommunalArticleBean articleBean) {
        String url = Url.BASE_URL + Url.F_ARTICLE_DETAILS_FAVOR;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipService(View view) {
        Intent intent = new Intent(QualityDMLLifeSearchActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }
}
