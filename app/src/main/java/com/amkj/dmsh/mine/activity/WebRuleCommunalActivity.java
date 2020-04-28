package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.bean.WebDataCommunalEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_REG_AGREEMENT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;
import static com.amkj.dmsh.constant.Url.USER_PRIVACY_POLICY;
import static com.amkj.dmsh.constant.Url.USER_REGISTER_AGREEMENT;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/1/18
 * version 3.2.0
 * class description:web样式公用activity
 */
public class WebRuleCommunalActivity extends BaseActivity{
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
        webRuleType = intent.getStringExtra(WEB_VALUE_TYPE);
        header_shared.setVisibility(View.GONE);
        if(TextUtils.isEmpty(webRuleType)){
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            return;
        }
        tv_header_title.setText(WEB_TYPE_PRIVACY_POLICY.equals(webRuleType)?"多么生活用户隐私政策":
                WEB_TYPE_REG_AGREEMENT.equals(webRuleType)?"多么生活用户注册协议":"");
        smart_communal_refresh_web.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
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
        String url = "";
        if(WEB_TYPE_REG_AGREEMENT.equals(webRuleType)||
                WEB_TYPE_PRIVACY_POLICY.equals(webRuleType)){
            url = WEB_TYPE_REG_AGREEMENT.equals(webRuleType)?USER_REGISTER_AGREEMENT:USER_PRIVACY_POLICY;
        }else{
            NetLoadUtils.getNetInstance().showLoadSirEmpty(loadService);
            showToast("数据类型错误，请重试！");
            return;
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh_web.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                WebDataCommunalEntity webDataCommunalEntity = new Gson().fromJson(result, WebDataCommunalEntity.class);
                if(webDataCommunalEntity!=null&&
                        SUCCESS_CODE.equals(webDataCommunalEntity.getCode())&&
                        webDataCommunalEntity.getWebDataCommunalList()!=null&&
                        webDataCommunalEntity.getWebDataCommunalList().size()>0){
                    descripDetailList.clear();
                    descripDetailList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(webDataCommunalEntity.getWebDataCommunalList()));
                }
                communalDescriptionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh_web.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
