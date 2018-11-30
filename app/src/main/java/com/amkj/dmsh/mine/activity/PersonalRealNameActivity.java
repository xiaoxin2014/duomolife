package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.bean.CommunalUserInfoEntity;
import com.amkj.dmsh.bean.CommunalUserInfoEntity.CommunalUserInfoBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/11
 * version 3.7
 * class description:设置实名信息
 */

public class PersonalRealNameActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    //    真实名字
    @BindView(R.id.et_per_real_name)
    EditText et_per_real_name;
    //    身份证号码
    @BindView(R.id.et_per_real_id_card)
    EditText et_per_real_id_card;

    @Override
    protected int getContentView() {
        return R.layout.activity_persional_set_real_name;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("实名信息");
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("完成");
        setEtFilter(et_per_real_name);
        setEtFilter(et_per_real_id_card);
    }

    @Override
    protected void loadData() {
        if (userId > 0) {
            String url = Url.BASE_URL + Url.MINE_PAGE;
            Map<String, Object> params = new HashMap<>();
            params.put("uid", userId);
            NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url
                    , params, new NetLoadUtils.NetLoadListener() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            CommunalUserInfoEntity communalUserInfoEntity = gson.fromJson(result, CommunalUserInfoEntity.class);
                            if (communalUserInfoEntity != null) {
                                if (communalUserInfoEntity.getCode().equals(SUCCESS_CODE)) {
                                    CommunalUserInfoBean communalUserInfoBean = communalUserInfoEntity.getCommunalUserInfoBean();
                                    setPersonalData(communalUserInfoBean);
                                } else if (!communalUserInfoEntity.getCode().equals(EMPTY_CODE)) {
                                    showToast(PersonalRealNameActivity.this, communalUserInfoEntity.getMsg());
                                }
                            }
                        }

                        @Override
                        public void netClose() {

                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    });
        }
    }

    /**
     * 修改真实信息
     */
    private void changeRealName() {
        String realName = et_per_real_name.getText().toString().trim();
        String idCard = et_per_real_id_card.getText().toString().trim();
        if (!TextUtils.isEmpty(realName) && !TextUtils.isEmpty(idCard)) {
            if (userId > 0) {
                if (loadHud != null) {
                    loadHud.show();
                }
                if (NetWorkUtils.checkNet(PersonalRealNameActivity.this)) {
                    String url = Url.BASE_URL + Url.MINE_RESET_REAL_NAME;
                    Map<String, Object> params = new HashMap<>();
                    params.put("uid", userId);
                    params.put("idcard", idCard);
                    params.put("realName", realName);
                    XUtil.Post(url, params, new MyCallBack<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                            if (requestStatus != null) {
                                if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                                    showToast(PersonalRealNameActivity.this, String.format(getResources().getString(R.string.doSuccess), "修改"));
                                    finish();
                                } else {
                                    showToast(PersonalRealNameActivity.this, requestStatus.getMsg());
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(PersonalRealNameActivity.this, R.string.do_failed);
                        }
                    });
                }
            }
        } else if (TextUtils.isEmpty(realName)) {
            showToast(PersonalRealNameActivity.this, "请输入收货人姓名");
        } else {
            showToast(PersonalRealNameActivity.this, "请输入收货人身份证号码");
        }

    }

    private void setPersonalData(CommunalUserInfoBean communalUserInfoBean) {
        if (!TextUtils.isEmpty(communalUserInfoBean.getIdcard())) {
            String idCard = getStrings(communalUserInfoBean.getIdcard());
            String realName = getStrings(communalUserInfoBean.getReal_name());
            et_per_real_id_card.setText(getStringFilter(idCard));
            et_per_real_id_card.setSelection(idCard.length());
            et_per_real_name.setText(getStringFilter(realName));
            et_per_real_name.setSelection(realName.length());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    public void inputSuccess() {
        changeRealName();
    }
}
