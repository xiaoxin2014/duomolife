package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.IntegralDetailAdapter;
import com.amkj.dmsh.message.bean.IntegrationDetailsEntity;
import com.amkj.dmsh.message.bean.IntegrationDetailsEntity.IntegrationDetailsBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_INTEGRAl_DETAIL;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/23
 * version 3.1.5
 * class description:????????????
 */
public class IntegralDetailActivity extends BaseActivity {
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
    private IntegralDetailChart integralDetailChart;
    private int page;
    private IntegralDetailAdapter integralDetailAdapter;
    private List<IntegrationDetailsBean> integrationDetailsList = new ArrayList();
    private IntegrationDetailsEntity integrationDetailsEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_get;
    }

    @Override
    protected void initViews() {
        getLoginStatus(IntegralDetailActivity.this);
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("????????????");
        communal_recycler.setLayoutManager(new LinearLayoutManager(IntegralDetailActivity.this));
        integralDetailAdapter = new IntegralDetailAdapter(integrationDetailsList);
        communal_recycler.setAdapter(integralDetailAdapter);
        View view = LayoutInflater.from(IntegralDetailActivity.this).inflate(R.layout.layout_integral_detail_total, null, false);
        integralDetailChart = new IntegralDetailChart();
        ButterKnife.bind(integralDetailChart, view);
        integralDetailChart.initViews();
        integralDetailAdapter.addHeaderView(view);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        integralDetailAdapter.setOnLoadMoreListener(() -> {
            page++;
            getIntegralDetailData();
        }, communal_recycler);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        page = 1;
        getIntegralDetailData();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getIntegralDetailData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_FORTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(IntegralDetailActivity.this, H_ATTENDANCE_INTEGRAl_DETAIL
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        integralDetailAdapter.loadMoreComplete();
                        if (page == 1) {
                            integrationDetailsList.clear();
                        }

                        integrationDetailsEntity = GsonUtils.fromJson(result, IntegrationDetailsEntity.class);
                        if (integrationDetailsEntity != null) {
                            if (SUCCESS_CODE.equals(integrationDetailsEntity.getCode())) {
                                integrationDetailsList.addAll(integrationDetailsEntity.getIntegrationDetailsList());
                                integralDetailChart.tv_integral_detail_score.setText(String.valueOf(integrationDetailsEntity.getNowScore()));
                                setIntegralDetailData(integrationDetailsEntity);
                            } else {
                                if (!integrationDetailsEntity.getCode().equals(EMPTY_CODE)) {
                                    showToast( integrationDetailsEntity.getMsg());
                                }
                                integralDetailAdapter.loadMoreEnd();
                            }
                        }
                        integralDetailAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integrationDetailsList, integrationDetailsEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        integralDetailAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integrationDetailsList, integrationDetailsEntity);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
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

    public class IntegralDetailChart {
        @BindView(R.id.tv_integral_detail_score)
        TextView tv_integral_detail_score;
        @BindView(R.id.pie_detail_section)
        PieChart pie_detail_section;

        public void initViews() {
            pie_detail_section.setTransparentCircleRadius(AutoSizeUtils.mm2px(mAppContext, 150));
//            ????????? ???????????????
            pie_detail_section.setHoleRadius(0f);
            pie_detail_section.setTransparentCircleRadius(0f);
//            ????????????
            Description description = new Description();
            description.setText("");
            pie_detail_section.setDescription(description);
//            ????????????
            pie_detail_section.setHighlightPerTapEnabled(true);
//            ??????????????????label
            pie_detail_section.setDrawEntryLabels(false);
//            ?????????????????????
            Legend legend = pie_detail_section.getLegend();
            legend.setEnabled(true);                    //?????????????????????true??????????????????????????????
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setForm(Legend.LegendForm.SQUARE); //?????????????????????
            legend.setFormSize(10);                      //?????????????????????
            legend.setFormToTextSpace(13);              //?????????????????????????????????????????????????????????
//            legend.set
            legend.setDrawInside(false);
//            legend.setWordWrapEnabled(true);			  //??????????????????(????????????????????????,?????????legend??????????????????)
//            legend.setXEntrySpace(10f);				  //???????????????????????????X???????????????setOrientation = HORIZONTAL?????????
            legend.setYEntrySpace(8f);                  //???????????????????????????Y???????????????setOrientation = VERTICAL ?????????
            legend.setYOffset(0f);                      //???????????????Y????????????
            legend.setTextSize(12);                      //?????????????????????????????????
            legend.setTextColor(Color.parseColor("#666666"));//?????????????????????????????????
        }
    }

    /**
     * ?????????????????????
     *
     * @param integrationDetailsEntity
     */
    private void setIntegralDetailData(IntegrationDetailsEntity integrationDetailsEntity) {
        ArrayList<PieEntry> pieEntryList = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        float totalScore = integrationDetailsEntity.getShopScore() + integrationDetailsEntity.getSignScore()
                + integrationDetailsEntity.getTaskScore() + integrationDetailsEntity.getOtherScore();
        PieEntry pieEntry;
        int colorValue;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    pieEntry = new PieEntry(integrationDetailsEntity.getShopScore() / totalScore * 100, "??????");
                    colorValue = Color.parseColor("#f4cc6e");
                    break;
                case 1:
                    pieEntry = new PieEntry(integrationDetailsEntity.getSignScore() / totalScore * 100, "??????");
                    colorValue = Color.parseColor("#f99c76");
                    break;
                case 2:
                    pieEntry = new PieEntry(integrationDetailsEntity.getTaskScore() / totalScore * 100, "??????");
                    colorValue = Color.parseColor("#acf5ef");
                    break;
                default:
                    pieEntry = new PieEntry(integrationDetailsEntity.getOtherScore() / totalScore * 100, "??????");
                    colorValue = Color.parseColor("#5ad5d9");
                    break;
            }
            colors.add(colorValue);
            pieEntryList.add(pieEntry);
        }
        //?????????????????? PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setSliceSpace(5f);           //????????????Item???????????????
        pieDataSet.setSelectionShift(8f);      //????????????Item???????????????????????????
        pieDataSet.setColors(colors);           //???DataSet??????????????????????????????(??????Item??????)
        //???????????? PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);            //??????????????????????????????(????????????true:????????????????????????)
//        pieData.setValueTextColor(R.color.text_login_gray_s);  //????????????DataSet?????????????????????????????????????????????
//        pieData.setValueTextSize(AutoSizeUtils.mm2px(mAppContext,24));          //????????????DataSet???????????????????????????????????????????????????

//        pieData.setValueFormatter(new PercentFormatter());//????????????DataSet???????????????????????????????????????????????????
        integralDetailChart.pie_detail_section.setData(pieData);
        integralDetailChart.pie_detail_section.highlightValues(null);
        integralDetailChart.pie_detail_section.invalidate();                    //????????????????????????????????????????????????
    }

}
