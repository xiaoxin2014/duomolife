package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.IntegralDetailAdapter;
import com.amkj.dmsh.message.bean.IntegrationDetailsEntity;
import com.amkj.dmsh.message.bean.IntegrationDetailsEntity.IntegrationDetailsBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/23
 * version 3.1.5
 * class description:积分明细
 */
public class IntegralDetailActivity extends BaseActivity {
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
    private IntegralDetailChart integralDetailChart;
    private int page;
    private IntegralDetailAdapter integralDetailAdapter;
    private List<IntegrationDetailsBean> integrationDetailsList = new ArrayList();

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_get;
    }

    @Override
    protected void initViews() {
        getLoginStatus(IntegralDetailActivity.this);
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("积分明细");
        communal_recycler.setLayoutManager(new LinearLayoutManager(IntegralDetailActivity.this));
        integralDetailAdapter = new IntegralDetailAdapter(integrationDetailsList);
        communal_recycler.setAdapter(integralDetailAdapter);
        View view = LayoutInflater.from(IntegralDetailActivity.this).inflate(R.layout.layout_integral_detail_total, null,false);
        integralDetailChart = new IntegralDetailChart();
        ButterKnife.bind(integralDetailChart, view);
        integralDetailChart.initViews();
        integralDetailAdapter.addHeaderView(view);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
            loadData();
        });
        integralDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getIntegralDetailData();
            }
        }, communal_recycler);
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
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getIntegralDetailData();
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        getIntegralDetailData();
    }

    private void getIntegralDetailData() {
        if (userId > 0) {
            if (NetWorkUtils.isConnectedByState(IntegralDetailActivity.this)) {
                Map<String, Object> params = new HashMap<>();
                params.put("uid", userId);
                params.put("currentPage", page);
                params.put("showCount", TOTAL_COUNT_FORTY);
                String url = Url.BASE_URL + Url.H_ATTENDANCE_INTEGRAl_DETAIL;
                XUtil.Post(url, params, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        integralDetailAdapter.loadMoreComplete();
                        communal_load.setVisibility(GONE);
                        communal_error.setVisibility(GONE);
                        if (page == 1) {
                            integrationDetailsList.clear();
                        }
                        Gson gson = new Gson();
                        IntegrationDetailsEntity integrationDetailsEntity = gson.fromJson(result, IntegrationDetailsEntity.class);
                        if (integrationDetailsEntity != null) {
                            if (SUCCESS_CODE.equals(integrationDetailsEntity.getCode())) {
                                integrationDetailsList.addAll(integrationDetailsEntity.getIntegrationDetailsList());
                                integralDetailChart.tv_integral_detail_score.setText(String.valueOf(integrationDetailsEntity.getNowScore()));
                                setIntegralDetailData(integrationDetailsEntity);
                            } else {
                                if (!integrationDetailsEntity.getCode().equals("02")) {
                                    showToast(IntegralDetailActivity.this, integrationDetailsEntity.getMsg());
                                }
                                integralDetailAdapter.loadMoreEnd();
                            }
                        }
                        integralDetailAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        smart_communal_refresh.finishRefresh();
                        integralDetailAdapter.loadMoreComplete();
                        communal_error.setVisibility(View.VISIBLE);
                        showToast(IntegralDetailActivity.this, R.string.invalidData);
                        super.onError(ex, isOnCallback);
                    }
                });
            } else {
                smart_communal_refresh.finishRefresh();
                integralDetailAdapter.loadMoreComplete();
                communal_error.setVisibility(View.VISIBLE);
                showToast(this, R.string.unConnectedNetwork);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        loadData();
    }

    public class IntegralDetailChart {
        @BindView(R.id.tv_integral_detail_score)
        TextView tv_integral_detail_score;
        @BindView(R.id.pie_detail_section)
        PieChart pie_detail_section;

        public void initViews() {
            pie_detail_section.setTransparentCircleRadius(AutoUtils.getPercentWidthSize(150));
//            实心圆 去除半透明
            pie_detail_section.setHoleRadius(0f);
            pie_detail_section.setTransparentCircleRadius(0f);
//            去除角标
            Description description = new Description();
            description.setText("");
            pie_detail_section.setDescription(description);
//            点击放大
            pie_detail_section.setHighlightPerTapEnabled(true);
//            饼图里面显示label
            pie_detail_section.setDrawEntryLabels(false);
//            设置图例跟标签
            Legend legend = pie_detail_section.getLegend();
            legend.setEnabled(true);                    //是否启用图列（true：下面属性才有意义）
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setForm(Legend.LegendForm.SQUARE); //设置图例的形状
            legend.setFormSize(10);					  //设置图例的大小
            legend.setFormToTextSpace(13);			  //设置每个图例实体中标签和形状之间的间距
//            legend.set
            legend.setDrawInside(false);
//            legend.setWordWrapEnabled(true);			  //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
//            legend.setXEntrySpace(10f);				  //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
            legend.setYEntrySpace(8f);				  //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
            legend.setYOffset(0f);					  //设置比例块Y轴偏移量
            legend.setTextSize(12);					  //设置图例标签文本的大小
            legend.setTextColor(Color.parseColor("#666666"));//设置图例标签文本的颜色
        }
    }

    /**
     * 设置饼图的数据
     *
     * @param integrationDetailsEntity
     */
    private void setIntegralDetailData(IntegrationDetailsEntity integrationDetailsEntity) {
        ArrayList<PieEntry> pieEntryList = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        float totalScore = integrationDetailsEntity.getShopScore()+integrationDetailsEntity.getSignScore()
                +integrationDetailsEntity.getTaskScore()+integrationDetailsEntity.getOtherScore();
        PieEntry pieEntry;
        int colorValue;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    pieEntry = new PieEntry(integrationDetailsEntity.getShopScore()/totalScore*100, "购物");
                    colorValue = Color.parseColor("#f4cc6e");
                    break;
                case 1:
                    pieEntry = new PieEntry(integrationDetailsEntity.getSignScore()/totalScore*100, "签到");
                    colorValue = Color.parseColor("#f99c76");
                    break;
                case 2:
                    pieEntry = new PieEntry(integrationDetailsEntity.getTaskScore()/totalScore*100, "任务");
                    colorValue = Color.parseColor("#acf5ef");
                    break;
                default:
                    pieEntry = new PieEntry(integrationDetailsEntity.getOtherScore()/totalScore*100, "其它");
                    colorValue = Color.parseColor("#5ad5d9");
                    break;
            }
            colors.add(colorValue);
            pieEntryList.add(pieEntry);
        }
        //饼状图数据集 PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setSliceSpace(5f);           //设置饼状Item之间的间隙
        pieDataSet.setSelectionShift(8f);      //设置饼状Item被选中时变化的距离
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
//        pieData.setValueTextColor(R.color.text_login_gray_s);  //设置所有DataSet内数据实体（百分比）的文本颜色
//        pieData.setValueTextSize(AutoUtils.getPercentWidthSize(24));          //设置所有DataSet内数据实体（百分比）的文本字体大小

//        pieData.setValueFormatter(new PercentFormatter());//设置所有DataSet内数据实体（百分比）的文本字体格式
        integralDetailChart.pie_detail_section.setData(pieData);
        integralDetailChart.pie_detail_section.highlightValues(null);
        integralDetailChart.pie_detail_section.invalidate();                    //将图表重绘以显示设置的属性和数据
    }

}
