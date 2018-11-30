package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.shopdetails.adapter.DirectEvaluationAdapter;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumCount;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * Created by atd48 on 2016/8/15.
 * 商品更多评论
 */
public class DirectProductEvaluationActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private List<GoodsCommentBean> goodsComments = new ArrayList();
    private int page = 1;
    private DirectEvaluationAdapter directEvaluationAdapter;
    private String productId;
    private GoodsCommentEntity goodsCommentEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        tv_header_titleAll.setText("全部评价");
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DirectProductEvaluationActivity.this, LinearLayoutManager.VERTICAL, false);
        communal_recycler.setLayoutManager(linearLayoutManager);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                .create());
        directEvaluationAdapter = new DirectEvaluationAdapter(DirectProductEvaluationActivity.this, goodsComments);
        communal_recycler.setAdapter(directEvaluationAdapter);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                getEvaluationData();
                directEvaluationAdapter.loadMoreEnd(true);
            }
        });
        directEvaluationAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_direct_avatar:
                        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag(R.id.iv_avatar_tag);
                        if (goodsCommentBean != null) {
                            Intent intent = new Intent(DirectProductEvaluationActivity.this, UserPagerActivity.class);
                            intent.putExtra("userId", String.valueOf(goodsCommentBean.getUserId()));
                            startActivity(intent);
                        }
                        break;
                    case R.id.tv_eva_count:
                        goodsCommentBean = (GoodsCommentBean) view.getTag();
                        if (goodsCommentBean != null&&!goodsCommentBean.isFavor()) {
                            if (userId > 0) {
                                setProductEvaLike(view);
                            } else {
                                getLoginStatus(DirectProductEvaluationActivity.this);
                            }
                        }
                        break;
                }

            }
        });
        directEvaluationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= directEvaluationAdapter.getItemCount()) {
                    page++;
                    getEvaluationData();
                } else {
                    directEvaluationAdapter.loadMoreComplete();
                    directEvaluationAdapter.loadMoreEnd(false);
                }
            }
        }, communal_recycler);
    }
    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }
    @Override
    protected void loadData() {
        page = 1;
        getEvaluationData();
    }

    private void getEvaluationData() {
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_COMMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("currentPage", page);
        params.put("id", productId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(DirectProductEvaluationActivity.this, url
                , params
                , new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                directEvaluationAdapter.loadMoreComplete();
                if (page == 1) {
                    goodsComments.clear();
                }
                Gson gson = new Gson();
                goodsCommentEntity = gson.fromJson(result, GoodsCommentEntity.class);
                if (goodsCommentEntity != null) {
                    if (goodsCommentEntity.getCode().equals(SUCCESS_CODE)) {
                        goodsComments.addAll(goodsCommentEntity.getGoodsComments());
                        tv_header_titleAll.setText("全部评价(" + goodsCommentEntity.getEvaluateCount() + ")");
                    } else if (!goodsCommentEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(DirectProductEvaluationActivity.this, goodsCommentEntity.getMsg());
                    }
                    directEvaluationAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,goodsComments, goodsCommentEntity);
            }

            @Override
            public void netClose() {
                directEvaluationAdapter.loadMoreComplete();
                smart_communal_refresh.finishRefresh();
                showToast(DirectProductEvaluationActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,goodsComments,goodsCommentEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                directEvaluationAdapter.loadMoreComplete();
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,goodsComments,goodsCommentEntity);
            }
        });
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

    private void setProductEvaLike(View view) {
        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
        TextView tv_eva_like = (TextView) view;
        String url = Url.BASE_URL + Url.SHOP_EVA_LIKE;
        Map<String, Object> params = new HashMap<>();
        params.put("id", goodsCommentBean.getId());
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>(){});
        tv_eva_like.setSelected(!tv_eva_like.isSelected());
        goodsCommentBean.setFavor(!goodsCommentBean.isFavor());
        tv_eva_like.setText(getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "赞"));
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}