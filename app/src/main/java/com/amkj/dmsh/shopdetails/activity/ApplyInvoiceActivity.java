package com.amkj.dmsh.shopdetails.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantVariable.INVOICE_APPLY_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2020/3/10
 * Version:v4.3.7
 * ClassDescription :申请发票
 */
public class ApplyInvoiceActivity extends BaseActivity {
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
    @BindView(R.id.rb_personal)
    RadioButton mRbPersonal;
    @BindView(R.id.rb_company)
    RadioButton mRbCompany;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_taxpayer_on)
    EditText mEtTaxpayerOn;
    @BindView(R.id.tv_save)
    TextView mTvSave;
    @BindView(R.id.rg_type)
    RadioGroup mRgType;
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.ll_taxpayer_on)
    LinearLayout mLlTaxpayerOn;
    private String mOrderNo;

    @Override
    protected int getContentView() {
        return R.layout.activity_apply_invoice;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("申请发票");
        mTlQualityBar.setSelected(true);
        mIvImgShare.setVisibility(View.GONE);
        if (getIntent() != null) {
            mOrderNo = getIntent().getStringExtra("orderNo");
        }
    }

    @Override
    protected void loadData() {
        mRgType.setOnCheckedChangeListener((group, checkedId) -> {
            mLlTaxpayerOn.setVisibility(checkedId == R.id.rb_company ? View.VISIBLE : View.GONE);
        });
    }


    @OnClick({R.id.tv_life_back, R.id.iv_img_service, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.iv_img_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(),"申请发票");
                break;
            //保存发票
            case R.id.tv_save:
                saveVoince();
                break;
        }
    }


    private void saveVoince() {
        String title = mEtTitle.getText().toString();
        String taxpayerOn = mEtTaxpayerOn.getText().toString();
        if (TextUtils.isEmpty(title)) {
            showToast("请输入发票抬头");
            return;
        }
        if (mRbCompany.isChecked() && TextUtils.isEmpty(taxpayerOn)) {
            showToast("请输入纳税人识别号");
            return;
        }

        showLoadhud(this);
        Map<String, Object> map = new HashMap<>();
        map.put("no", mOrderNo);
        map.put("title", title);
        if (mRbCompany.isChecked()) {
            map.put("taxpayer_on", taxpayerOn);
        }
        map.put("userType", mRbPersonal.isChecked() ? 1 : 2);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.INVOICE_DRAW_UP, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        showToast("发票开具成功");
                        EventBus.getDefault().post(new EventMessage(INVOICE_APPLY_SUCCESS, 0));
                        new LifecycleHandler(getActivity()).postDelayed(() -> finish(), 500);
                    } else {
                        showToastRequestMsg( requestStatus);
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
                showToast("发票开具失败");
            }
        });
    }
}
