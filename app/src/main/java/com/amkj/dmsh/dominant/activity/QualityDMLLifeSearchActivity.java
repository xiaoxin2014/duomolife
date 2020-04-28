package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.RecyclerArticleAdapter;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity;
import com.amkj.dmsh.homepage.bean.CommunalArticleEntity.CommunalArticleBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_COLLECT;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_DETAILS_FAVOR;
import static com.amkj.dmsh.constant.Url.Q_DML_SEARCH_LIST;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;


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

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
                scrollY = 0;
            }
        });
        recyclerArticleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getSearchData();
            }
        }, communal_recycler);

        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show();
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
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
        getCarCount(getActivity());
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }


    // 分类文章列表
    private void getSearchData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        if (userId > 0) {
            params.put("fuid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityDMLLifeSearchActivity.this, Q_DML_SEARCH_LIST,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        recyclerArticleAdapter.loadMoreComplete();
                        if (page == 1) {
                            communalArtList.clear();
                        }
                        Gson gson = new Gson();
                        communalArticleEntity = gson.fromJson(result, CommunalArticleEntity.class);
                        if (communalArticleEntity != null) {
                            if (communalArticleEntity.getCode().equals(SUCCESS_CODE)) {
                                tv_header_titleAll.setText(getStrings(communalArticleEntity.getTitle()));
                                communalArtList.addAll(communalArticleEntity.getCommunalArticleList());
                            } else if (communalArticleEntity.getCode().equals(EMPTY_CODE)) {
                                recyclerArticleAdapter.loadMoreEnd();
                            } else {
                                showToast(communalArticleEntity.getMsg());
                            }
                            recyclerArticleAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, communalArtList, communalArticleEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        recyclerArticleAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, communalArtList, communalArticleEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
    }

    //    收藏
    private void setArticleCollected(final CommunalArticleBean articleBean, final int position) {
        loadHud.show();
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", articleBean.getId());
        params.put("type", ConstantVariable.TYPE_C_WELFARE);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_COLLECT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                communalArtList.get(position).setCollect(!articleBean.isCollect());
                communalArtList.get(position).setCollect(!articleBean.isCollect() ?
                        articleBean.getCollect() + 1 : articleBean.getCollect() - 1);
                recyclerArticleAdapter.notifyItemChanged(position);
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
                showToast( String.format(getResources().getString(R.string.collect_failed), "文章"));
            }
        });
    }

    //  点赞
    private void setArticleLiked(CommunalArticleBean articleBean) {
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //文章id
        params.put("id", articleBean.getId());
        params.put("favortype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_DETAILS_FAVOR, params, null);
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


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge!=null){
                badge.setBadgeNumber((int) message.result);
            }
        }
    }
}
