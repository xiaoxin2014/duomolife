package com.amkj.dmsh.dominant.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.dominant.bean.GroupShopCommunalInfoEntity;
import com.amkj.dmsh.dominant.bean.GroupShopCommunalInfoEntity.GroupShopCommunalInfoBean.ServicePromiseBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.GROUP_SHOP_COMMUNAL;

;

/**
 * Created by atd48 on 2016/8/15.
 */
public class GroupCustomerServiceFragment extends BaseFragment {
    @BindView(R.id.recycler_shop_details)
    RecyclerView recycler_shop_details;
    private CommunalDetailAdapter directServiceAdapter;
    private List<CommunalDetailObjectBean> itemBodyBeanList = new ArrayList();
    @Override
    protected int getContentView() {
        return R.layout.fragment_shop_layout_recycler_no_refresh;
    }
    @Override
    protected void initViews() {
        recycler_shop_details.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_shop_details.setNestedScrollingEnabled(false);
        directServiceAdapter = new CommunalDetailAdapter(getActivity(), itemBodyBeanList);
        recycler_shop_details.setAdapter(directServiceAdapter);
    }

    @Override
    protected void loadData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),GROUP_SHOP_COMMUNAL,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                GroupShopCommunalInfoEntity communalInfoEntity = gson.fromJson(result, GroupShopCommunalInfoEntity.class);
                if (communalInfoEntity != null) {
                    if (communalInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        itemBodyBeanList.clear();
                        List<ServicePromiseBean> servicePromiseBeanList = communalInfoEntity.getGroupShopCommunalInfoBean().getServicePromise();
                        CommunalDetailObjectBean communalDetailObjectBean;
                        for (int i = 0; i < servicePromiseBeanList.size(); i++) {
                            communalDetailObjectBean = new CommunalDetailObjectBean();
                            ServicePromiseBean servicePromiseBean = servicePromiseBeanList.get(i);
                            if (servicePromiseBean.getType().equals("text")) {
                                communalDetailObjectBean.setContent(servicePromiseBean.getContent());
                                itemBodyBeanList.add(communalDetailObjectBean);
                            }
                        }
                    }
                    directServiceAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
