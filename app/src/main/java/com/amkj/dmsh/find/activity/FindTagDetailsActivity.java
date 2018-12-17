package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.find.adapter.PullUserInvitationAdapter;
import com.amkj.dmsh.find.adapter.TagDetailHorizontalAdapter;
import com.amkj.dmsh.find.adapter.TagRelProAdapter;
import com.amkj.dmsh.find.bean.RelevanceTagInfoEntity;
import com.amkj.dmsh.find.bean.RelevanceTagInfoEntity.RelevanceTagInfoBean;
import com.amkj.dmsh.find.bean.RelevanceTagInfoEntity.RelevanceTagInfoBean.TagBean;
import com.amkj.dmsh.find.bean.RelevanceTagInfoEntity.RelevanceTagInfoBean.TopTagListBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.release.bean.RelevanceProEntity.RelevanceProBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.CommunalCopyTextUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_C_ARTICLE;
import static com.amkj.dmsh.constant.Url.FIND_RELEVANCE_TAG;
import static com.amkj.dmsh.constant.Url.FIND_RELEVANCE_TAG_INFO;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_COLLECT;
import static com.amkj.dmsh.constant.Url.F_ARTICLE_DETAILS_FAVOR;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/4
 * class description:标签详情
 */
public class FindTagDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private int scrollY = 0;
    private float screenHeight;
    private String type = "recommend";
    private PullUserInvitationAdapter adapterInvitationAdapter;
    private List<InvitationDetailBean> invitationSearchList = new ArrayList();
    private List<RelevanceProBean> relevanceProList = new ArrayList();
    private String tagId;
    private RelevanceTagInfoBean relevanceTagInfoBean;
    private TagHeaderView tagHeaderView;
    private TagRelProAdapter tagRelProAdapter;
    private List<TopTagListBean> topTagList = new ArrayList<>();
    private TagDetailHorizontalAdapter tagDetailHorizontalAdapter;
    private InvitationDetailEntity invitationDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(View.GONE);
        try {
            Intent intent = getIntent();
            tagId = intent.getStringExtra("tagId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(tagId)) {
            showToast(this, R.string.unConnectedNetwork);
            finish();
        }
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        communal_recycler.setBackgroundColor(getResources().getColor(R.color.white));
        communal_recycler.setLayoutManager(new LinearLayoutManager(FindTagDetailsActivity.this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)


                .create());
        adapterInvitationAdapter = new PullUserInvitationAdapter(FindTagDetailsActivity.this, invitationSearchList, type);
        View view = LayoutInflater.from(FindTagDetailsActivity.this).inflate(R.layout.layout_tag_relevance_header, null, false);
        tagHeaderView = new TagHeaderView();
        ButterKnife.bind(tagHeaderView, view);
        tagHeaderView.initViews();
        adapterInvitationAdapter.addHeaderView(view);
        communal_recycler.setAdapter(adapterInvitationAdapter);
        adapterInvitationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getRelevanceTagList();
            }
        }, communal_recycler);
        adapterInvitationAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalCopyTextUtils.showPopWindow(FindTagDetailsActivity.this
                        , (TextView) view, (String) view.getTag(R.id.copy_text_content));
                return false;
            }
        });
        adapterInvitationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InvitationDetailBean invitationSearch = (InvitationDetailBean) view.getTag();
                if (invitationSearch != null) {
                    Intent intent = new Intent();
                    switch (invitationSearch.getArticletype()) {
                        case 1:
                            intent.setClass(FindTagDetailsActivity.this, ArticleInvitationDetailsActivity.class);
                            break;
                        case 3:
                            break;
                        default:
                            intent.setClass(FindTagDetailsActivity.this, ArticleDetailsImgActivity.class);
                            break;
                    }
                    intent.putExtra("ArtId", String.valueOf(invitationSearch.getId()));
                    startActivity(intent);
                }
            }
        });
        adapterInvitationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                InvitationDetailBean invitationDetailBean = (InvitationDetailBean) view.getTag();
                if (invitationDetailBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_com_art_collect_count:
                            if (userId > 0) {
                                loadHud.show();
                                setArticleCollect(invitationDetailBean, view);
                            } else {
                                getLoginStatus(FindTagDetailsActivity.this);
                            }
                            break;
                        case R.id.tv_com_art_like_count:
                            if (userId > 0) {
                                setArticleLiked(invitationDetailBean, view);
                            } else {
                                getLoginStatus(FindTagDetailsActivity.this);
                            }
                            break;
                    }
                }
            }
        });
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
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected void getData() {
        getRelevanceTagList();
        getRelevanceTagOtherInfo();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getRelevanceTagList() {
        Map<String, Object> params = new HashMap<>();
        if (userId > 0) {
            params.put("fuid", userId);
        }
        params.put("currentPage", page);
        params.put("tagId", tagId);
        params.put("version", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(FindTagDetailsActivity.this, FIND_RELEVANCE_TAG
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        adapterInvitationAdapter.loadMoreComplete();
                        if (page == 1) {
                            invitationSearchList.clear();
                        }
                        Gson gson = new Gson();
                        invitationDetailEntity = gson.fromJson(result, InvitationDetailEntity.class);
                        if (invitationDetailEntity != null) {
                            if (invitationDetailEntity.getCode().equals(SUCCESS_CODE)) {
                                invitationSearchList.addAll(invitationDetailEntity.getInvitationSearchList());
                            } else if (invitationDetailEntity.getCode().equals(EMPTY_CODE)) {
                                adapterInvitationAdapter.loadMoreEnd();
                            } else {
                                showToast(FindTagDetailsActivity.this, invitationDetailEntity.getMsg());
                            }
                            adapterInvitationAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        adapterInvitationAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void netClose() {
                        showToast(FindTagDetailsActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(FindTagDetailsActivity.this, R.string.invalidData);
                    }
                });
    }

    /**
     * 关联标签其它信息
     */
    private void getRelevanceTagOtherInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("tagId", tagId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, FIND_RELEVANCE_TAG_INFO, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RelevanceTagInfoEntity relevanceTagInfoEntity = gson.fromJson(result, RelevanceTagInfoEntity.class);
                if (relevanceTagInfoEntity != null) {
                    if (relevanceTagInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        relevanceTagInfoBean = relevanceTagInfoEntity.getRelevanceTagInfoBean();
                        if (relevanceTagInfoBean.getProductList() != null && relevanceTagInfoBean.getProductList().size() > 0) {
                            relevanceProList.clear();
                            relevanceProList.addAll(relevanceTagInfoBean.getProductList());
                            tagHeaderView.rel_tag_rel_pro.setVisibility(View.VISIBLE);
                        } else {
                            tagHeaderView.rel_tag_rel_pro.setVisibility(View.GONE);
                        }
                        setTagData(relevanceTagInfoBean);
                    } else if (!relevanceTagInfoEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(FindTagDetailsActivity.this, relevanceTagInfoEntity.getMsg());
                    }
                    tagRelProAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setTagData(RelevanceTagInfoBean relevanceTagInfoBean) {
        if (relevanceTagInfoBean != null) {
            tagHeaderView.ll_tag_info.setVisibility(View.VISIBLE);
            TagBean tagBean = relevanceTagInfoBean.getTag();
            if (tagBean != null) {
                tagHeaderView.rel_tag_info.setVisibility(View.VISIBLE);
                GlideImageLoaderUtil.loadCenterCrop(FindTagDetailsActivity.this
                        , tagHeaderView.iv_rev_tag_cover, getStrings(tagBean.getImg_url())
                        , R.drawable.domolife_logo);
                tagHeaderView.tv_tag_rel_tag_name.setText(getStrings(tagBean.getTag_name()));
//                标签
                tv_header_title.setText(getStrings(tagBean.getTag_name()));
                tagHeaderView.tv_tag_rel_tag_count.setText("帖子 " + relevanceTagInfoBean.getDocCount());
            } else {
                tagHeaderView.rel_tag_info.setVisibility(View.GONE);
            }
            if (relevanceTagInfoBean.getTopTagList() != null && relevanceTagInfoBean.getTopTagList().size() > 0) {
                tagHeaderView.rv_tag_detail.setVisibility(View.VISIBLE);
                topTagList.clear();
                topTagList.addAll(relevanceTagInfoBean.getTopTagList());
                tagDetailHorizontalAdapter.notifyDataSetChanged();
            } else {
                tagHeaderView.rv_tag_detail.setVisibility(View.GONE);
            }
        } else {
            tagHeaderView.ll_tag_info.setVisibility(View.GONE);
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

    //    文章收藏
    private void setArticleCollect(final InvitationDetailBean invitationDetailBean, View view) {
        final TextView tv_collect = (TextView) view;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("uid", userId);
        //文章id
        params.put("object_id", invitationDetailBean.getId());
        params.put("type", TYPE_C_ARTICLE);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_COLLECT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        tv_collect.setSelected(!tv_collect.isSelected());
                        tv_collect.setText(ConstantMethod.getNumCount(tv_collect.isSelected(), invitationDetailBean.isCollect(), invitationDetailBean.getCollect(), "收藏"));
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(FindTagDetailsActivity.this, String.format(getResources().getString(R.string.collect_failed), "文章"));
            }

            @Override
            public void netClose() {
                showToast(FindTagDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void setArticleLiked(InvitationDetailBean invitationDetailBean, View view) {
        TextView tv_like = (TextView) view;
        Map<String, Object> params = new HashMap<>();
        //用户id
        params.put("tuid", userId);
        //关注id
        params.put("id", invitationDetailBean.getId());
        params.put("favortype", "doc");
        NetLoadUtils.getNetInstance().loadNetDataPost(this, F_ARTICLE_DETAILS_FAVOR, params, null);
        tv_like.setSelected(!tv_like.isSelected());
        tv_like.setText(ConstantMethod.getNumCount(tv_like.isSelected(), invitationDetailBean.isFavor(), invitationDetailBean.getFavor(), "赞"));
    }


    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare(View view) {
        if (relevanceTagInfoBean != null && relevanceTagInfoBean.getTag() != null) {
            TagBean tagBean = relevanceTagInfoBean.getTag();
            new UMShareAction(FindTagDetailsActivity.this
                    , getStrings(tagBean.getImg_url())
                    , getStrings(tagBean.getTag_name())
                    , ""
                    , "");
        }
    }

    class TagHeaderView {
        @BindView(R.id.iv_rev_tag_cover)
        ImageView iv_rev_tag_cover;
        @BindView(R.id.tv_tag_rel_tag_name)
        TextView tv_tag_rel_tag_name;
        @BindView(R.id.tv_tag_rel_tag_count)
        TextView tv_tag_rel_tag_count;
        @BindView(R.id.ll_tag_info)
        LinearLayout ll_tag_info;
        @BindView(R.id.rel_tag_info)
        RelativeLayout rel_tag_info;
        @BindView(R.id.rv_tag_detail)
        RecyclerView rv_tag_detail;
        @BindView(R.id.rel_tag_rel_pro)
        RelativeLayout rel_tag_rel_pro;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initViews() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindTagDetailsActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            communal_recycler_wrap.setLayoutManager(linearLayoutManager);
            tagRelProAdapter = new TagRelProAdapter(FindTagDetailsActivity.this, relevanceProList);
            communal_recycler_wrap.setAdapter(tagRelProAdapter);
            communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_img_white)


                    .create());
            tagRelProAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    RelevanceProBean relevanceProBean = (RelevanceProBean) view.getTag();
                    if (relevanceProBean != null) {
                        Intent intent = new Intent(FindTagDetailsActivity.this, ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(relevanceProBean.getProductId()));
                        startActivity(intent);
                    }
                }
            });
            rv_tag_detail.setLayoutManager(new LinearLayoutManager(FindTagDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            tagDetailHorizontalAdapter = new TagDetailHorizontalAdapter(topTagList);
            rv_tag_detail.setAdapter(tagDetailHorizontalAdapter);
            tagDetailHorizontalAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    TopTagListBean topTagListBean = (TopTagListBean) view.getTag();
                    if (topTagListBean != null) {
                        tagId = String.valueOf(topTagListBean.getId());
                        loadData();
                        NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                    }
                }
            });
        }
    }
}
