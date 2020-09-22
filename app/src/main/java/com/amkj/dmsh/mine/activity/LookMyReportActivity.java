package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.ImgsListAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.luck.picture.lib.decoration.RecycleViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/8/25
 * Version:v4.7.0
 * ClassDescription :0元订单列表-查看报告
 */
public class LookMyReportActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.tv_more)
    TextView mTvMore;
    @BindView(R.id.tv_buy_now)
    TextView mTvBuyNow;
    @BindView(R.id.rl_detail)
    RelativeLayout mRlDetail;
    @BindView(R.id.rv_imgs)
    RecyclerView mRvImgs;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    private int mProductId;
    private String mOrderId;
    private String mActivityId;
    private List<String> imgs = new ArrayList<>();
    private ImgsListAdapter mImgsListAdapter;
    private RequestStatus.Result reportDetailBean;
    private RequestStatus mReportDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_look_report;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("试用报告详情");
        if (getIntent().getExtras() != null) {
            mOrderId = getIntent().getStringExtra("orderId");
            mActivityId = getIntent().getStringExtra("activityId");
        }

        //初始化报告列表
        mImgsListAdapter = new ImgsListAdapter(this, imgs);
        mRvImgs.setLayoutManager(new LinearLayoutManager(this));
        mRvImgs.setAdapter(mImgsListAdapter);
        mRvImgs.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, AutoSizeUtils.mm2px(this, 20), Color.parseColor("#ffffff")));
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", mOrderId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_MY_REPORT, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mReportDetailEntity = GsonUtils.fromJson(result, RequestStatus.class);
                imgs.clear();
                if (mReportDetailEntity != null) {
                    if (SUCCESS_CODE.equals(mReportDetailEntity.getCode())) {
                        reportDetailBean = mReportDetailEntity.getResult();
                        mProductId = getStringChangeIntegers(reportDetailBean.getProductId());
                        mTvContent.setText(getStrings(reportDetailBean.getContent()));
                        mTvContent.setVisibility(!TextUtils.isEmpty(reportDetailBean.getContent()) ? View.VISIBLE : View.GONE);
                        if (!TextUtils.isEmpty(reportDetailBean.getImgs())) {
                            mRvImgs.setVisibility(View.VISIBLE);
                            imgs.addAll(Arrays.asList(reportDetailBean.getImgs().split(",")));
                        } else {
                            mRvImgs.setVisibility(View.GONE);
                        }
                    }
                }

                mImgsListAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, reportDetailBean, mReportDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, reportDetailBean, mReportDetailEntity);
            }
        });
    }


    @OnClick({R.id.tv_life_back, R.id.tv_header_shared, R.id.tv_more, R.id.tv_buy_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.tv_header_shared:
                if (reportDetailBean != null && !TextUtils.isEmpty(reportDetailBean.getImgs())) {
                    new UMShareAction(this, reportDetailBean.getImgs().split(",")[0], "试用报告", "精选好物0元试用",
                            Url.BASE_SHARE_PAGE_TWO + "vip/tryout_report_detail.html?id=" + mActivityId, getStringChangeIntegers(mActivityId));
                }
                break;
            //更多心得
            case R.id.tv_more:
                Intent intent = new Intent(this, ZeroReportListActivity.class);
                startActivity(intent);
                break;
            //立即下单
            case R.id.tv_buy_now:
                if (reportDetailBean != null) {
                    skipProductUrl(this, 1, mProductId);
                }
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mRlDetail;
    }
}
