package com.amkj.dmsh.mine.activity;

import android.graphics.drawable.Drawable;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.bean.AccountLogoutReasonEntity;
import com.amkj.dmsh.mine.bean.AccountLogoutReasonEntity.AccountLogoutReasonBean;
import com.amkj.dmsh.mine.bean.LogoutAccountResultEntity;
import com.amkj.dmsh.mine.bean.WebDataCommunalEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ACCOUNT_LOGOUT_REASON;
import static com.amkj.dmsh.constant.Url.ACCOUNT_LOGOUT_TIP;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/22
 * version 3.2.0
 * class description:注销账号
 */
public class AccountLogoutActivity extends BaseActivity {
    @BindView(R.id.fl_account_logout_container)
    FrameLayout fl_account_logout_container;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    private CommunalDetailAdapter communalDescriptionAdapter;
    //    详细描述
    private List<CommunalDetailObjectBean> descripDetailList = new ArrayList<>();
    //    存储view
    private List<View> viewList = new ArrayList<>();
    private View currentView;
    private ConfirmRuleHelper confirmRuleHelper;
    private SelectionReasonHelper selectionReasonHelper;
    private LogoutResultHelper logoutResultHelper;
    private List<AccountLogoutReasonBean> logoutReasonList;
    private AlertDialogHelper accountLogoutDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_account_logout;
    }

    @Override
    protected void initViews() {
        tv_header_shared.setVisibility(View.GONE);
        tv_header_title.setText("注销账户");
        View firstView = LayoutInflater.from(this).inflate(R.layout.layout_account_logout_confirm_rule, null, false);
        confirmRuleHelper = new ConfirmRuleHelper();
        ButterKnife.bind(confirmRuleHelper, firstView);
        confirmRuleHelper.initViews();
        View secondView = LayoutInflater.from(this).inflate(R.layout.layout_account_logout_selecte_reason, null, false);
        selectionReasonHelper = new SelectionReasonHelper();
        ButterKnife.bind(selectionReasonHelper, secondView);
        View thirdView = LayoutInflater.from(this).inflate(R.layout.layout_account_logout_result, null, false);
        logoutResultHelper = new LogoutResultHelper();
        ButterKnife.bind(logoutResultHelper, thirdView);
        logoutResultHelper.initViews();
        viewList.add(firstView);
        viewList.add(secondView);
        viewList.add(thirdView);
        currentView = firstView;
        fl_account_logout_container.addView(firstView);
    }

    @Override
    protected void loadData() {
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        int currentPosition = viewList.indexOf(currentView);
        if (currentPosition == -1) {
            return;
        }
        if (currentPosition == 0) {
//            获取注销文案
            getAccountLogoutTip();
        } else if (currentPosition == 1) {
//            选择原因
            getAccountLogoutReason();
        }
    }

    /**
     * 获取注销原因
     */
    private void getAccountLogoutReason() {
        if (selectionReasonHelper.rpAccountLogout.getChildCount() > 0) {
            return;
        }
        NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ACCOUNT_LOGOUT_REASON, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                AccountLogoutReasonEntity accountLogoutReasonEntity = new Gson().fromJson(result, AccountLogoutReasonEntity.class);
                if (accountLogoutReasonEntity != null &&
                        SUCCESS_CODE.equals(accountLogoutReasonEntity.getCode()) &&
                        accountLogoutReasonEntity.getLogoutReasonList() != null &&
                        accountLogoutReasonEntity.getLogoutReasonList().size() > 0) {
                    logoutReasonList = accountLogoutReasonEntity.getLogoutReasonList();
                    setReasonListData(accountLogoutReasonEntity.getLogoutReasonList());
                } else {
                    NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
                }
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            }
        });
    }

    /**
     * 设置注销原因选择
     *
     * @param logoutReasonList
     */
    private void setReasonListData(List<AccountLogoutReasonBean> logoutReasonList) {
        selectionReasonHelper.rpAccountLogout.removeAllViews();
        for (int i = 0; i < logoutReasonList.size(); i++) {
            AccountLogoutReasonBean accountLogoutReasonBean = logoutReasonList.get(i);
            RadioButton childView = (RadioButton) LayoutInflater.from(AccountLogoutActivity.this).inflate(R.layout.layout_logout_reason_rb, selectionReasonHelper.rpAccountLogout, false);
            childView.setText(getStrings(accountLogoutReasonBean.getContent()));
            childView.setTag(accountLogoutReasonBean);
            selectionReasonHelper.rpAccountLogout.addView(childView);
        }
    }

    /**
     * 获取注销文案
     */
    private void getAccountLogoutTip() {
        if (descripDetailList.size() > 0) {
            return;
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ACCOUNT_LOGOUT_TIP, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                WebDataCommunalEntity webDataCommunalEntity = new Gson().fromJson(result, WebDataCommunalEntity.class);
                if (webDataCommunalEntity != null &&
                        SUCCESS_CODE.equals(webDataCommunalEntity.getCode()) &&
                        webDataCommunalEntity.getWebDataCommunalList() != null &&
                        webDataCommunalEntity.getWebDataCommunalList().size() > 0) {
                    descripDetailList.clear();
                    descripDetailList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(webDataCommunalEntity.getWebDataCommunalList()));
                }
                communalDescriptionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, descripDetailList);
            }
        });
    }

    /**
     * 确认注销账号
     * @param accountLogoutReasonBean
     */
    private void setLogoutAccount(AccountLogoutReasonBean accountLogoutReasonBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("causeId", accountLogoutReasonBean.getId());
        String otherReason = selectionReasonHelper.evAccountLogoutReason.getText().toString().trim();
        if (!TextUtils.isEmpty(otherReason)) {
            params.put("content", otherReason);
        }
        LogoutAccountResultEntity logoutAccountResultEntity = new LogoutAccountResultEntity();
        ArrayList<String> strings = new ArrayList<>();
        strings.add("大家好");
        strings.add("好的");
        strings.add("解决的大家都觉得基督教的基督教大家都觉得风口浪尖的客服京东数科叫访客但房价看到积分考虑到积分");
        logoutAccountResultEntity.setCode("00");
        logoutAccountResultEntity.setAccountResultList(strings);
        setLogoutResult(logoutAccountResultEntity);
//        NetLoadUtils.getNetInstance().loadNetDataPost(this, ACCOUNT_LOGOUT_REQUEST, params, new NetLoadListenerHelper() {
//            @Override
//            public void onSuccess(String result) {
//                if (loadHud != null) {
//                    loadHud.dismiss();
//                }
//                LogoutAccountResultEntity logoutAccountResultEntity = new Gson().fromJson(result, LogoutAccountResultEntity.class);
//                if (logoutAccountResultEntity != null) {
//                    setLogoutResult(logoutAccountResultEntity);
//                } else {
//                    showToast(AccountLogoutActivity.this, "注销失败,请重试！");
//                }
//            }
//
//            @Override
//            public void onNotNetOrException() {
//                if (loadHud != null) {
//                    loadHud.dismiss();
//                }
//                showToast(AccountLogoutActivity.this, "注销失败,请重试！");
//            }
//        });
    }

    /**
     * 设置注销结果
     *
     * @param logoutAccountResultEntity
     */
    private void setLogoutResult(LogoutAccountResultEntity logoutAccountResultEntity) {
        logoutResultHelper.tvAccountLogoutResult.setSelected(SUCCESS_CODE.equals(logoutAccountResultEntity.getCode()));
        logoutResultHelper.tvAccountLogoutResult.setText(SUCCESS_CODE.equals(logoutAccountResultEntity.getCode()) ? "注销成功" : "注销失败");
        String logoutReason = "";
        for (String reasonText : logoutAccountResultEntity.getAccountResultList()) {
            logoutReason += (!TextUtils.isEmpty(logoutReason) ? "\n" : "") + "." + reasonText;
        }
        logoutResultHelper.tvAccountLogoutResultReason.setText(logoutReason);
        changeView(1);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        changeView(0);
    }

    /**
     * 确定规则
     */
    public class ConfirmRuleHelper {
        @BindView(R.id.communal_recycler)
        RecyclerView communal_recycler;

        private void initViews() {
            communal_recycler.setLayoutManager(new LinearLayoutManager(AccountLogoutActivity.this));
            communal_recycler.setNestedScrollingEnabled(false);
            communalDescriptionAdapter = new CommunalDetailAdapter(AccountLogoutActivity.this, descripDetailList);
            communal_recycler.setAdapter(communalDescriptionAdapter);
            communalDescriptionAdapter.setEnableLoadMore(false);
            communalDescriptionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(AccountLogoutActivity.this, null, view, loadHud);
                }
            });
        }

        @OnClick({R.id.tv_account_logout_cancel, R.id.tv_account_logout_confirm})
        void clickLogout(View view) {
            changeView(view.getId() == R.id.tv_account_logout_confirm ? 1 : 0);
        }
    }

    /**
     * 切换view
     *
     * @param changCode 1 为切换下一个 0 返回上一个
     */
    private void changeView(int changCode) {
        int currentPosition = viewList.indexOf(currentView);
        fl_account_logout_container.removeView(currentView);
        if (changCode == 1 && currentPosition < viewList.size()) {
            currentView = viewList.get(++currentPosition);
            fl_account_logout_container.addView(currentView);
            requestData();
        } else if (changCode == 0 && currentPosition > 0) {
            currentView = viewList.get(--currentPosition);
            fl_account_logout_container.addView(currentView);
        } else {
            finish();
        }
    }

    /**
     * 注销原因选择
     */
    public class SelectionReasonHelper {
        @BindView(R.id.rp_account_logout)
        RadioGroup rpAccountLogout;
        @BindView(R.id.ev_account_logout_reason)
        EmojiEditText evAccountLogoutReason;

        /**
         * 注销账号
         */
        @OnClick(R.id.tv_account_logout_selected)
        void confirmLogoutAccount() {
            if (rpAccountLogout.getChildCount() < 1) {
                showToast(AccountLogoutActivity.this, "请选择注销原因");
                return;
            }
            RadioButton radioButton = rpAccountLogout.findViewById(rpAccountLogout.getCheckedRadioButtonId());
            AccountLogoutReasonBean accountLogoutReasonBean = (AccountLogoutReasonBean) radioButton.getTag();
            if (logoutReasonList != null &&
                    logoutReasonList.indexOf(accountLogoutReasonBean) != -1 &&
                    logoutReasonList.indexOf(accountLogoutReasonBean) == logoutReasonList.size() - 1) {
                if(TextUtils.isEmpty(evAccountLogoutReason.getText().toString().trim())){
                    showToast(AccountLogoutActivity.this, "请输入注销原因，方便以后更好的为你服务!");
                    return;
                }
            }
            if (accountLogoutDialogHelper == null) {
                accountLogoutDialogHelper = new AlertDialogHelper(AccountLogoutActivity.this);
                accountLogoutDialogHelper.setConfirmTextColor(getResources().getColor(R.color.text_login_gray_s))
                        .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                            @Override
                            public void confirm() {
                                setLogoutAccount(accountLogoutReasonBean);
                            }

                            @Override
                            public void cancel() {
                                finish();
                            }
                        });
                accountLogoutDialogHelper.setTitleVisibility(View.GONE).setMsgTextGravity(Gravity.CENTER_VERTICAL)
                        .setMsg("确定要注销该账户？").setCancelText("取消").setConfirmText("确定");
            }
            accountLogoutDialogHelper.show();
        }
    }

    /**
     * 注销结果
     */
    public class LogoutResultHelper {
        @BindView(R.id.tv_account_logout_result)
        TextView tvAccountLogoutResult;
        @BindView(R.id.tv_account_logout_result_reason)
        TextView tvAccountLogoutResultReason;

        public void initViews() {
            Drawable drawable = getResources().getDrawable(R.drawable.sel_account_logout_result_status);
            drawable.setBounds(0, 0, AutoSizeUtils.mm2px(mAppContext, 40f / drawable.getMinimumHeight() * drawable.getMinimumWidth()), AutoSizeUtils.mm2px(mAppContext, 40));//设置边界
            tvAccountLogoutResult.setCompoundDrawables(null, drawable, null, null);
            tvAccountLogoutResultReason.setMovementMethod(ScrollingMovementMethod.getInstance());
        }

        @OnClick(R.id.tv_account_logout_result_confirm)
        void logoutResult() {
            finish();
        }
    }
}
