package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.bean.WebDataCommunalEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_GROUP_BUY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_SAVE_MONEY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_SHARE_GIFT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/18
 * version 3.2.0
 * class description:web样式公用activity
 */
public class WebRuleCommunalActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    @BindView(R.id.smart_refresh_communal_details)
    SmartRefreshLayout smart_communal_refresh_web;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler_wrap;
    public String webRuleType;
    private CommunalDetailAdapter communalDescriptionAdapter;
    //    详细描述
    private List<CommunalDetailObjectBean> descripDetailList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_refresh_scroll_header;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        header_shared.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(getWebUrl(getIntent().getStringExtra(WEB_VALUE_TYPE)))) {
            webRuleType = intent.getStringExtra(WEB_VALUE_TYPE);
        } else {
            showToast("数据有误，请重试");
            finish();
        }
        tv_header_title.setText(getWebTitle(webRuleType));
        smart_communal_refresh_web.setOnRefreshListener(refreshLayout -> loadData());
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(WebRuleCommunalActivity.this));
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communalDescriptionAdapter = new CommunalDetailAdapter(WebRuleCommunalActivity.this, descripDetailList);
        communal_recycler_wrap.setAdapter(communalDescriptionAdapter);
        communalDescriptionAdapter.setEnableLoadMore(false);
        communalDescriptionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(WebRuleCommunalActivity.this, null, view, loadHud);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh_web;
    }

    @Override
    protected void loadData() {
        String webUrl = getWebUrl(webRuleType);
        Map<String, Object> map = new HashMap<>();
        if (Url.GET_VIP_REALTED_RULE.equals(webUrl)) {
            map.put("reminderType", WEB_TYPE_SAVE_MONEY.equals(webRuleType) ? 30 : 31);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, getWebUrl(webRuleType), map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                descripDetailList.clear();
                smart_communal_refresh_web.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                WebDataCommunalEntity webDataCommunalEntity = GsonUtils.fromJson(result, WebDataCommunalEntity.class);
                if (webDataCommunalEntity != null && SUCCESS_CODE.equals(webDataCommunalEntity.getCode())) {
                    List<CommunalDetailBean> webDataCommunalList = webDataCommunalEntity.getWebDataCommunalList();
                    if (webDataCommunalList != null && webDataCommunalList.size() > 0) {
                        descripDetailList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(webDataCommunalList));
                    }
                }
                communalDescriptionAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, descripDetailList);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh_web.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, descripDetailList);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }


    private String getWebTitle(String webRuleType) {
        if (WEB_TYPE_PRIVACY_POLICY.equals(webRuleType)) {
            return "多么生活用户隐私政策";
        } else if (WEB_TYPE_REG_AGREEMENT.equals(webRuleType)) {
            return "多么生活用户注册协议";
        } else if (WEB_TYPE_SAVE_MONEY.equals(webRuleType)) {
            return "省钱规则";
        } else if (WEB_TYPE_SHARE_GIFT.equals(webRuleType)) {
            return "分享有礼规则";
        } else if (WEB_TYPE_GROUP_BUY.equals(webRuleType)) {
            return "团购规则";
        } else {
            return "";
        }
    }

    private String getWebUrl(String webRuleType) {
        if (WEB_TYPE_PRIVACY_POLICY.equals(webRuleType)) {
            return Url.USER_PRIVACY_POLICY;
        } else if (WEB_TYPE_REG_AGREEMENT.equals(webRuleType)) {
            return Url.USER_REGISTER_AGREEMENT;
        } else if (WEB_TYPE_SAVE_MONEY.equals(webRuleType) || WEB_TYPE_SHARE_GIFT.equals(webRuleType)) {
            return Url.GET_VIP_REALTED_RULE;
        } else if (WEB_TYPE_GROUP_BUY.equals(webRuleType)) {
            return Url.GET_GROUP_RULE;
        } else {
            return "";
        }
    }
}
