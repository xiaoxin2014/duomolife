package com.amkj.dmsh.homepage.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity.IndentInvoicePromptBean;
import com.amkj.dmsh.shopdetails.bean.IndentInvoicePromptEntity.IndentInvoicePromptBean.DescriptionBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;

;



/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/26
 * class description:积分规则
 */
public class IntegRuleActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.smart_scroll_communal_refresh)
    RefreshLayout smart_scroll_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    private CommunalDetailAdapter directServiceAdapter;
    private List<CommunalDetailObjectBean> integRuleList = new ArrayList();
    @Override
    protected int getContentView() {
        return R.layout.activity_integ_rule;
    }
    @Override
    protected void initViews() {
        tv_header_titleAll.setText("积分规则");
        header_shared.setVisibility(View.GONE);
        communal_recycler.setLayoutManager(new LinearLayoutManager(IntegRuleActivity.this));
        communal_recycler.setNestedScrollingEnabled(false);
        directServiceAdapter = new CommunalDetailAdapter(IntegRuleActivity.this, integRuleList);
        communal_recycler.setAdapter(directServiceAdapter);
        smart_scroll_communal_refresh.setOnRefreshListener((refreshLayout) -> {
           loadData();
        });
        communal_load.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {
        if (NetWorkUtils.checkNet(IntegRuleActivity.this)) {
            String url = Url.BASE_URL + Url.H_ATT_INTEG;
            XUtil.Post(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_scroll_communal_refresh.finishRefresh();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    IndentInvoicePromptEntity directGoodsServerEntity = gson.fromJson(result, IndentInvoicePromptEntity.class);
                    if (directGoodsServerEntity != null) {
                        if (directGoodsServerEntity.getCode().equals("01")) {
                            integRuleList.clear();
                            IndentInvoicePromptBean invoicePromptBean = directGoodsServerEntity.getIndentInvoicePromptBean();
                            if (invoicePromptBean != null) {
                                List<DescriptionBean> descriptionBeanList = invoicePromptBean.getDescriptionList();
                                CommunalDetailObjectBean communalDetailObjectBean;
                                for (int i = 0; i < descriptionBeanList.size(); i++) {
                                    communalDetailObjectBean = new CommunalDetailObjectBean();
                                    DescriptionBean descriptionBean = descriptionBeanList.get(i);
                                    communalDetailObjectBean.setContent(descriptionBean.getContent());
                                    integRuleList.add(communalDetailObjectBean);
                                }
                            }
                            directServiceAdapter.setNewData(integRuleList);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.VISIBLE);
                    showToast(IntegRuleActivity.this, R.string.no_data);
                }
            });
        } else {
            communal_load.setVisibility(View.GONE);
            communal_error.setVisibility(View.VISIBLE);
            smart_scroll_communal_refresh.finishRefresh();
            showToast(IntegRuleActivity.this, R.string.unConnectedNetwork);
        }
    }

    @OnClick(R.id.tv_life_back)
    void finish(View view) {
        finish();
    }

}
