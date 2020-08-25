package com.amkj.dmsh.mine.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.mine.bean.ZeroReportDetailEntity;
import com.amkj.dmsh.mine.bean.ZeroReportDetailEntity.ZeroReportDetailBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.mine.adapter.ReportContentAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/8/21
 * Version:v4.7.0
 * ClassDescription :0元试用报告详情
 */
public class ZeroReportDetailActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_buy)
    TextView mTvBuy;
    @BindView(R.id.tv_total_num)
    TextView mTvTotalNum;
    @BindView(R.id.rv_rcommend)
    RecyclerView mRvRcommend;
    @BindView(R.id.rv_report)
    RecyclerView mRvReport;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    private String mActivityId;
    private String mOrderId;
    private LikedProductBean mProductInfo;
    private List<PostBean> reports = new ArrayList<>();
    private ReportContentAdapter mReportContentAdapter;
    private ZeroReportDetailEntity mReportDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_zero_report_detail;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("试用详情");
        mTvHeaderShared.setVisibility(View.GONE);
        if (getIntent().getExtras() != null) {
            mActivityId = getIntent().getStringExtra("activityId");
            mOrderId = getIntent().getStringExtra("orderId");
        }

        //初始化报告列表
        mReportContentAdapter = new ReportContentAdapter(this, reports);
        mRvReport.setLayoutManager(new LinearLayoutManager(this));
        mRvReport.setAdapter(mReportContentAdapter);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("activityId", mActivityId);
        map.put("orderId", mOrderId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_REPORT_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mReportDetailEntity = GsonUtils.fromJson(result, ZeroReportDetailEntity.class);
                if (mReportDetailEntity != null) {
                    if (SUCCESS_CODE.equals(mReportDetailEntity.getCode())) {
                        ZeroReportDetailBean reportDetailBean = mReportDetailEntity.getResult();
                        if (reportDetailBean != null) {
                            setDetailInfo(reportDetailBean);
                        }
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, reports, mReportDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, reports, mReportDetailEntity);
            }
        });
    }

    private void setDetailInfo(ZeroReportDetailBean reportDetailBean) {
        //设置商品数据
        mProductInfo = reportDetailBean.getProductInfo();
        if (mProductInfo != null) {
            GlideImageLoaderUtil.loadCenterCrop(this, mIvCover, mProductInfo.getPicUrl());
            mTvName.setText(getStrings(mProductInfo.getName()));
            mTvPrice.setText(getRmbFormat(this, mProductInfo.getPrice(), true, "#333"));
        }

        //设置报告列表
        List<PostBean> reportList = reportDetailBean.getReportList();
        if (reportList != null && reportList.size() > 0) {
            reports.clear();
            reports.addAll(reportList);
            mReportContentAdapter.setNewData(reports.subList(0, 1));
        }
        mTvTotalNum.setText(getIntegralFormat(this, R.string.total_report, reports.size()));
        mTvTotalNum.setVisibility(reports.size() > 1 ? View.VISIBLE : View.GONE);
    }


    @OnClick({R.id.tv_life_back, R.id.rl_product, R.id.tv_total_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.rl_product:
                if (mProductInfo != null) {
                    skipProductUrl(this, 1, mProductInfo.getId());
                }
                break;
            case R.id.tv_total_num:
                mTvTotalNum.setVisibility(View.GONE);
                mReportContentAdapter.setNewData(reports);
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return super.getLoadView();
    }
}
