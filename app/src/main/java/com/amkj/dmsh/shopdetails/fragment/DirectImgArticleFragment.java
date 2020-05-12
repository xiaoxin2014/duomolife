package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

;

/**
 * Created by atd48 on 2016/8/15.
 */
public class DirectImgArticleFragment extends BaseFragment {
    @BindView(R.id.recycler_shop_details)
    RecyclerView recycler_shop_details_evaluation_comment;
    private CommunalDetailAdapter contentOfficialAdapter;
    private List<CommunalDetailObjectBean> itemBodyBeanList = new ArrayList();

    @Override
    protected int getContentView() {
        return R.layout.fragment_shop_layout_recycler_no_refresh;
    }

    @Override
    protected void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_shop_details_evaluation_comment.setLayoutManager(linearLayoutManager);
        recycler_shop_details_evaluation_comment.setNestedScrollingEnabled(false);
        contentOfficialAdapter = new CommunalDetailAdapter(getActivity(), itemBodyBeanList);
        recycler_shop_details_evaluation_comment.setAdapter(contentOfficialAdapter);
        contentOfficialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_communal_share) {
                    return;
                }
//                不做回调，故用activity
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(getActivity(), view, loadHud);
            }
        });
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("ImgArticleShop")) {
            itemBodyBeanList = new ArrayList<>();
            List<CommunalDetailBean> itemBeanList = (List) message.result;
            itemBodyBeanList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(itemBeanList));
            contentOfficialAdapter.setNewData(itemBodyBeanList);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected boolean isLazy() {
        return false;
    }
}
