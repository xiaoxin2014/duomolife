package com.amkj.dmsh.shopdetails.activity;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/3/27
 * Version:v4.5.0
 * ClassDescription :退款去向
 */
public class RefundMoneyActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.iv_img_service)
    ImageView mIvImgService;
    @BindView(R.id.fl_header_service)
    FrameLayout mFlHeaderService;
    @BindView(R.id.iv_img_share)
    ImageView mIvImgShare;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.tv_back_account)
    TextView mTvBackAccount;
    @BindView(R.id.tv_auditing_time)
    TextView mTvAuditingTime;
    @BindView(R.id.tv_arrive_time)
    TextView mTvArriveTime;
    private String refundNo;
    private RequestStatus mRequestStatus;

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_money;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("钱款去向");
        mIvImgShare.setVisibility(View.GONE);
        mTlQualityBar.setSelected(true);
        if (getIntent() != null) {
            refundNo = getIntent().getStringExtra("refundNo");
        }
    }

    @Override
    protected void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("refundNo", refundNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_GET_REFUND_GO_INFO, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String resultJson) {

                mRequestStatus = GsonUtils.fromJson(resultJson, RequestStatus.class);
                if (mRequestStatus != null) {
                    if (SUCCESS_CODE.equals(mRequestStatus.getCode())) {
                        RequestStatus.Result result = mRequestStatus.getResult();
                        if (result != null) {
                            mTvAmount.setText(getStrings("¥" + result.getRefundPrice()));
                            mTvBackAccount.setText(getStrings(result.getRefundAccount()));
                            mTvAuditingTime.setText(getStrings(result.getExamineTime()));
                            mTvArriveTime.setText(getStrings(result.getReceiveRefundTime()));
                        }
                    } else {
                        ConstantMethod.showToast( mRequestStatus.getMsg());
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mRequestStatus);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mRequestStatus);
            }
        });
    }


    @OnClick({R.id.tv_life_back, R.id.iv_img_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.iv_img_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(),"钱款去向");
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }
}
