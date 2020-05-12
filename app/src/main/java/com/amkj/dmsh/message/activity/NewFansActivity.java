package com.amkj.dmsh.message.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.FansEntity;
import com.amkj.dmsh.find.bean.FansEntity.FansBean;
import com.amkj.dmsh.message.adapter.FansAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.NEW_FANS;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_FOLLOW;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_FOLLOW_TITLE;

/**
 * Created by xiaoxin on 2019/7/19
 * Version:v4.1.0
 * ClassDescription :消息中心-新增粉丝
 */
public class NewFansActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.rv_fans)
    RecyclerView mRvFans;
    List<FansBean> mFansList = new ArrayList<>();
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    private FansAdapter fansAdapter;
    private FansEntity mFansEntity;


    @Override
    protected int getContentView() {
        return R.layout.activity_new_fans;
    }

    @Override
    protected void initViews() {
        mTvHeaderShared.setVisibility(View.GONE);
        mTvHeaderTitle.setText("新增粉丝");
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            loadData();
        });
        mRvFans.setLayoutManager(new LinearLayoutManager(this));
        fansAdapter = new FansAdapter(this, mFansList);
        mRvFans.setAdapter(fansAdapter);
    }

    @Override
    protected void loadData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.NEW_FANS, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mFansList.clear();
                mFansEntity = GsonUtils.fromJson(result, FansEntity.class);
                if (mFansEntity != null) {
                    List<FansBean> fansList = mFansEntity.getFansList();
                    List<FansBean> recommendUserList = mFansEntity.getRecommendUserList();
                    if (fansList != null) {
                        for (FansBean fansBean:fansList) {
                            fansBean.setItemType(NEW_FANS);
                        }
                        mFansList.addAll(fansList);

                    }
                    if (recommendUserList != null) {
                        FansBean fansBean = new FansBean();
                        fansBean.setItemType(RECOMMEND_FOLLOW_TITLE);
                        mFansList.add(fansBean);
                        for (FansBean fansBean1:recommendUserList) {
                            fansBean1.setItemType(RECOMMEND_FOLLOW);
                        }
                        mFansList.addAll(recommendUserList);
                    }
                }
                fansAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mFansList, mFansEntity);

            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mFansList, mFansEntity);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }
}
