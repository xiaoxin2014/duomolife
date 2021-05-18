package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectEvaluationAdapter;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;


/**
 * Created by xiaoxin on 2019/8/21
 * Version:v4.2.0
 * ClassDescription :商品更多评论
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
    RecyclerView mRvComment;
    private int page = 1;
    private String productId;
    private String productName;
    private String cover;

    private List<GoodsCommentBean> mGoodsComments = new ArrayList<>();
    private GoodsCommentEntity mPostEntity;
    private DirectEvaluationAdapter directEvaluationAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_product_evaluation;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        productName = intent.getStringExtra("productName");
        cover = intent.getStringExtra("cover");
        tv_header_titleAll.setText("Ta们都在说");
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);

        //初始化评论列表
        View headView = View.inflate(this, R.layout.layout_evaluate_headview, null);
        EvaluateHeadView evaluateHeadView = new EvaluateHeadView();
        ButterKnife.bind(evaluateHeadView, headView);
        mRvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvComment.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, AutoSizeUtils.mm2px(this, 10), getResources().getColor(R.color.light_gray_f)));
        directEvaluationAdapter = new DirectEvaluationAdapter(getActivity(), mGoodsComments, true);
        mRvComment.setNestedScrollingEnabled(false);
        mRvComment.setAdapter(directEvaluationAdapter);
        directEvaluationAdapter.addHeaderView(headView);
        GlideImageLoaderUtil.loadCenterCrop(this, evaluateHeadView.mIvCover, cover);
        evaluateHeadView.mTvTitle.setText(productName);
        evaluateHeadView.mIvAddCar.setOnClickListener(v -> {
            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
            baseAddCarProInfoBean.setProductId(getStringChangeIntegers(productId));
            baseAddCarProInfoBean.setProName(productName);
            baseAddCarProInfoBean.setProPic(cover);
            addShopCarGetSku(getActivity(), baseAddCarProInfoBean);
        });
        directEvaluationAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_eva_count:
                    GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
                    if (goodsCommentBean != null) {
                        if (userId > 0) {
                            setProductEvaLike(view);
                        } else {
                            getLoginStatus(getActivity());
                        }
                    }
                    break;
            }
        });
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });
        directEvaluationAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, mRvComment);
    }

    private void setProductEvaLike(View view) {
        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
        TextView tv_eva_like = (TextView) view;
        String url = Url.SHOP_EVA_LIKE;
        Map<String, Object> params = new HashMap<>();
        params.put("id", goodsCommentBean.getId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
        goodsCommentBean.setFavor(!goodsCommentBean.isFavor());
        goodsCommentBean.setLikeNum(goodsCommentBean.isFavor() ? goodsCommentBean.getLikeNum() + 1 : goodsCommentBean.getLikeNum() - 1);
        tv_eva_like.setSelected(!tv_eva_like.isSelected());
        tv_eva_like.setText(ConstantMethod.getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "赞"));
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        getEvaluationData();
    }

    private void getEvaluationData() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", page);
        map.put("showCount", TOTAL_COUNT_TEN);
        map.put("id", productId);
        if (userId > 0) {
            map.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.Q_SHOP_DETAILS_COMMENT, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                mPostEntity = GsonUtils.fromJson(result, GoodsCommentEntity.class);
                if (page == 1) {
                    mGoodsComments.clear();
                }
                if (mPostEntity != null) {
                    String code = mPostEntity.getCode();
                    List<GoodsCommentBean> goodsComments = mPostEntity.getGoodsComments();
                    if (goodsComments != null && goodsComments.size() > 0) {
                        mGoodsComments.addAll(goodsComments);
                        directEvaluationAdapter.loadMoreComplete();

                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mPostEntity.getMsg());
                        directEvaluationAdapter.loadMoreFail();
                    } else {
                        directEvaluationAdapter.loadMoreEnd();
                    }
                } else {
                    directEvaluationAdapter.loadMoreEnd();
                }

                directEvaluationAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsComments, mPostEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsComments, mPostEntity);
            }
        });
    }


    @OnClick({R.id.tv_life_back})
    public void onViewClicked(View view) {
        finish();
    }

    static class EvaluateHeadView {
        @BindView(R.id.iv_cover)
        ImageView mIvCover;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.iv_add_car)
        ImageView mIvAddCar;
    }
}