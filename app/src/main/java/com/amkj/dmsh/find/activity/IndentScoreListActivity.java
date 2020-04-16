package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.IndentScoreGoodsAdapter;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity;
import com.amkj.dmsh.homepage.bean.ScoreGoodsEntity.ScoreGoodsBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipJoinTopic;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;

/**
 * Created by xiaoxin on 2019/7/17
 * Version:v4.1.0
 * ClassDescription :订单-多件商品评分
 */
public class IndentScoreListActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.rv_indent_goods)
    RecyclerView mRvIndentGoods;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    private List<ScoreGoodsBean> mGoodsList = new ArrayList<>();
    private IndentScoreGoodsAdapter mScoreGoodsAdapter;
    private ScoreGoodsEntity mScoreGoodsEntity;
    private String orderNo;
    private boolean isFirst=true;

    @Override
    protected int getContentView() {
        return R.layout.activity_indent_goods_score_list;
    }

    @Override
    protected void initViews() {
        if (getIntent() != null) {
            orderNo = getIntent().getStringExtra("orderNo");
            if (TextUtils.isEmpty(orderNo)) {
                showToast("数据错误，请重试");
                return;
            }
        }
        mTlNormalBar.setSelected(true);
        mTvHeaderShared.setVisibility(View.GONE);
        mTvHeaderShared.setCompoundDrawables(null, null, null, null);
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            loadData();
        });

        //初始化评分商品
        mRvIndentGoods.setLayoutManager(new LinearLayoutManager(this));
        mScoreGoodsAdapter = new IndentScoreGoodsAdapter(this, mGoodsList);
        mRvIndentGoods.setAdapter(mScoreGoodsAdapter);
        mRvIndentGoods.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(this, 20), getResources().getColor(R.color.light_gray_f)));
        mScoreGoodsAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //写点评
            ScoreGoodsBean scoreGoodsBean = (ScoreGoodsBean) view.getTag();
            skipJoinTopic(this, scoreGoodsBean, mScoreGoodsEntity);
        });
    }

    @Override
    protected void loadData() {
        getGoods();
    }

    //获取已购商品
    private void getGoods() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", TOTAL_COUNT_FORTY);
        params.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_SCORE_PRODUCT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mGoodsList.clear();
                mScoreGoodsEntity = new Gson().fromJson(result, ScoreGoodsEntity.class);
                if (mScoreGoodsEntity != null) {
                    mScoreGoodsAdapter.setRewardReminder(mScoreGoodsEntity.getMaxRewardTip());
                    List<ScoreGoodsBean> goodsList = mScoreGoodsEntity.getGoodsList();
                    if (SUCCESS_CODE.equals(mScoreGoodsEntity.getCode()) && goodsList != null && goodsList.size() > 0) {
                        mGoodsList.addAll(goodsList);
                    } else if (ERROR_CODE.equals(mScoreGoodsEntity.getCode())) {
                        showToast(mScoreGoodsEntity.getMsg());
                    }
                }

                if (mGoodsList.size() == 1) {
                    ScoreGoodsBean scoreGoodsBean = mGoodsList.get(0);
                    Intent intent = new Intent(getActivity(), JoinTopicActivity.class);
                    intent.putExtra("reminder", mScoreGoodsEntity.getContentReminder());
                    intent.putExtra("rewardtip", mScoreGoodsEntity.getRewardTip());
                    intent.putExtra("maxRewardTip", mScoreGoodsEntity.getMaxRewardTip());
                    intent.putExtra("scoreGoods", scoreGoodsBean);
                    startActivity(intent);
                    finish();
                } else {
                    mTvHeaderTitle.setText("写点评");
                    mScoreGoodsAdapter.notifyDataSetChanged();
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList, mScoreGoodsEntity);
                }
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mGoodsList, mScoreGoodsEntity);
            }
        });
    }


    @OnClick({R.id.tv_life_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartLayout;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            getGoods();
        }
        isFirst = false;
    }
}
