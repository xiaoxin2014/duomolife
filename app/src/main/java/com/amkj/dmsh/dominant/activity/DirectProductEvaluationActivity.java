package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.adapter.DirectEvaluationAdapter;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;

;

/**
 * Created by atd48 on 2016/8/15.
 */
public class DirectProductEvaluationActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private List<GoodsCommentBean> goodsComments = new ArrayList();
    private int page = 1;
    private DirectEvaluationAdapter directEvaluationAdapter;
    private String productId;
    private int uid;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        isLoginStatus();
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        tv_header_titleAll.setText("全部评价");
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DirectProductEvaluationActivity.this, LinearLayoutManager.VERTICAL, false);
        communal_recycler.setLayoutManager(linearLayoutManager);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        directEvaluationAdapter = new DirectEvaluationAdapter(DirectProductEvaluationActivity.this, goodsComments);
        communal_recycler.setAdapter(directEvaluationAdapter);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
           page = 1;
                loadData();
                directEvaluationAdapter.loadMoreEnd(true);
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
                        if (goodsCommentBean != null) {
                            if (uid > 0) {
                                setProductEvaLike(view);
                            } else {
                                getLoginStatus();
                            }
                        }
                        break;
                }

            }
        });
        directEvaluationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener(){
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= directEvaluationAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    directEvaluationAdapter.loadMoreComplete();
                    directEvaluationAdapter.loadMoreEnd(false);
                }
            }
        }, communal_recycler);
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_COMMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("currentPage", page);
        params.put("id", productId);
        if (uid > 0) {
            params.put("uid", uid);
        }
        if (NetWorkUtils.checkNet(DirectProductEvaluationActivity.this)) {
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (page == 1) {
                        goodsComments.clear();
                    }
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    directEvaluationAdapter.loadMoreComplete();
                    Gson gson = new Gson();
                    GoodsCommentEntity goodsCommentEntity = gson.fromJson(result, GoodsCommentEntity.class);
                    if (goodsCommentEntity != null) {
                        if (goodsCommentEntity.getCode().equals("01")) {
                            goodsComments.addAll(goodsCommentEntity.getGoodsComments());
                            tv_header_titleAll.setText("全部评价(" + goodsCommentEntity.getEvaluateCount() + ")");
                        } else if (!goodsCommentEntity.getCode().equals("02")) {
                            showToast(DirectProductEvaluationActivity.this, goodsCommentEntity.getMsg());
                        }
                        if (page == 1) {
                            directEvaluationAdapter.setNewData(goodsComments);
                        } else {
                            directEvaluationAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    directEvaluationAdapter.loadMoreComplete();
                    communal_error.setVisibility(View.VISIBLE);
                    smart_communal_refresh.finishRefresh();
                    showToast(DirectProductEvaluationActivity.this, +R.string.unConnectedNetwork);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            showToast(DirectProductEvaluationActivity.this, +R.string.unConnectedNetwork);
            directEvaluationAdapter.loadMoreComplete();
        }
    }

    private void isLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        }else{
            uid = 0;
        }
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(this);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                getLoginStatus();
                loadData();
            }
        }
    }

    private void setProductEvaLike(View view) {
        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
        TextView tv_eva_like = (TextView) view;
        String url = Url.BASE_URL + Url.SHOP_EVA_LIKE;
        Map<String, Object> params = new HashMap<>();
        params.put("id", goodsCommentBean.getId());
        params.put("uid", uid);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
        tv_eva_like.setSelected(!tv_eva_like.isSelected());
        tv_eva_like.setText(ConstantMethod.getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "赞"));
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData() {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(View.GONE);
        page = 1;
        loadData();
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }


}