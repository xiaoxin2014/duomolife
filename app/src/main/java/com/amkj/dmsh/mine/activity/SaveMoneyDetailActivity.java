package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.VipSaveDetailAdapter;
import com.amkj.dmsh.mine.bean.CalculatorEntity;
import com.amkj.dmsh.mine.bean.CalculatorEntity.CalculatorBean;
import com.amkj.dmsh.mine.bean.CalculatorEntity.CalculatorBean.PriceDataBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_SAVE_MONEY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;

/**
 * Created by xiaoxin on 2020/9/2
 * Version:v4.7.0
 * ClassDescription :会员省钱账单
 */
public class SaveMoneyDetailActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.tv_total_save)
    TextView mTvTotalSave;
    @BindView(R.id.tv_open_time)
    TextView mTvOpenTime;
    @BindView(R.id.rv_detail)
    RecyclerView mRvDetail;
    private CalculatorEntity mCalculatorEntity;
    private List<PriceDataBean> mPriceData = new ArrayList<>();
    private VipSaveDetailAdapter mVipSaveDetailAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_save_money_detail;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("多么会员省钱账单");
        mTvHeaderShared.setVisibility(View.GONE);
        mRvDetail.setLayoutManager(new LinearLayoutManager(this));
        mVipSaveDetailAdapter = new VipSaveDetailAdapter(mPriceData);
        mRvDetail.setAdapter(mVipSaveDetailAdapter);
    }

    @Override
    protected void loadData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_BEECONOMICAL, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mPriceData.clear();
                dismissLoadhud(getActivity());
                mCalculatorEntity = GsonUtils.fromJson(result, CalculatorEntity.class);
                if (mCalculatorEntity != null) {
                    if (SUCCESS_CODE.equals(mCalculatorEntity.getCode())) {
                        CalculatorBean calculatorBean = mCalculatorEntity.getResult();
                        mTvTotalSave.setText(getStrings(calculatorBean.getTotalPrice()));
                        mTvOpenTime.setText(getStringsFormat(getActivity(), R.string.open_vip_time, calculatorBean.getStartTime()));
                        List<PriceDataBean> priceData = calculatorBean.getPriceData();
                        if (priceData != null && priceData.size() > 0) {
                            mPriceData.addAll(priceData);
                        }
                    } else {
                        showToast(mCalculatorEntity.getMsg());
                    }
                }
                mVipSaveDetailAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPriceData, mCalculatorEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPriceData, mCalculatorEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlContent;
    }

    @OnClick({R.id.tv_life_back, R.id.tv_save_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //省钱规则
            case R.id.tv_save_rule:
                Intent intent = new Intent(this, WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_SAVE_MONEY);
                startActivity(intent);
                break;
        }
    }
}
