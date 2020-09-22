package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.VipRecordAdapter;
import com.amkj.dmsh.mine.bean.VipRecordEntity;
import com.amkj.dmsh.mine.bean.VipRecordEntity.VipRecordBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/8/26
 * Version:v4.7.0
 * ClassDescription :会员购买记录
 */
public class VipRecordActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.rv_record)
    RecyclerView mRvRecord;
    List<VipRecordBean> records = new ArrayList<>();
    private VipRecordAdapter mVipRecordAdapter;
    private VipRecordEntity mVipRecordEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_vip_record;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        mTvHeaderTitle.setText("会员购买记录");
        mTvHeaderShared.setVisibility(View.GONE);
        mVipRecordAdapter = new VipRecordAdapter(records);
        mRvRecord.setLayoutManager(new LinearLayoutManager(this));
        mRvRecord.setAdapter(mVipRecordAdapter);
    }

    @Override
    protected void loadData() {
        if (userId == 0) return;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIP_RECORD, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                records.clear();
                mVipRecordEntity = GsonUtils.fromJson(result, VipRecordEntity.class);
                if (mVipRecordEntity != null) {
                    String code = mVipRecordEntity.getCode();
                    List<VipRecordBean> recordBeans = mVipRecordEntity.getResult();
                    if (SUCCESS_CODE.equals(code)) {
                        records.addAll(recordBeans);
                    }
                }
                mVipRecordAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, records, mVipRecordEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, records, mVipRecordEntity);
            }
        });
    }


    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mRvRecord;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IS_LOGIN_CODE:
                loadData();
                break;
        }
    }
}
