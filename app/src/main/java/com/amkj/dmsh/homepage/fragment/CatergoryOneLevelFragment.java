package com.amkj.dmsh.homepage.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.catergory.adapter.CatergoryOneLevelAdapter;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;

/**
 * Created by xiaoxin on 2019/4/19 0019
 * Version:v4.0.0
 * ClassDescription :
 */
public class CatergoryOneLevelFragment extends BaseFragment {
    @BindView(R.id.rv_catergory)
    RecyclerView mRvCatergory;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    private String catergoryPid;
    private List<CatergoryOneLevelBean> mCatergoryBeanList = new ArrayList<>();
    private CatergoryOneLevelEntity mCatergoryEntity;
    private CatergoryOneLevelAdapter mOneLevelAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_catergory_defalut;
    }

    @Override
    protected void initViews() {
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            loadData();
        });
        mOneLevelAdapter = new CatergoryOneLevelAdapter(getActivity(), mCatergoryBeanList);
        mRvCatergory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvCatergory.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        mRvCatergory.setAdapter(mOneLevelAdapter);
        mOneLevelAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                //进入文章专题
                case R.id.ll_artical:

                    break;
                //点击进入文章详情
                case R.id.fl_artical_left:

                    break;
                //点击进入文章详情
                case R.id.fl_artical_right:

                    break;
            }
        });
    }

    @Override
    protected void loadData() {
        //获取一级分类页面数据
        getOneLevelData();
    }

    private void getOneLevelData() {
        Map<String, String> map = new HashMap<>();
        map.put("catergoryPid", catergoryPid);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.QUALITY_SHOP_TYPE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mCatergoryEntity = new Gson().fromJson(result, CatergoryOneLevelEntity.class);
                if (mCatergoryEntity != null) {
                    String code = mCatergoryEntity.getCode();
                    List<CatergoryOneLevelBean> catergoryList = mCatergoryEntity.getResult();
                    if (catergoryList == null || catergoryList.size() == 0 || EMPTY_CODE.equals(code)) {
                        mOneLevelAdapter.loadMoreEnd();
                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(mCatergoryEntity.getMsg());
                        mOneLevelAdapter.loadMoreFail();
                    } else {
                        mCatergoryBeanList.clear();
                        mCatergoryBeanList.addAll(catergoryList);
                        mOneLevelAdapter.notifyDataSetChanged();
                        mOneLevelAdapter.loadMoreComplete();
                    }
                } else {
                    mOneLevelAdapter.loadMoreEnd();
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCatergoryBeanList, mCatergoryEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartLayout.finishRefresh();
                mOneLevelAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCatergoryBeanList, mCatergoryEntity);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        if (bundle != null) {
            catergoryPid = bundle.getString("catergoryPid");
        }
    }
}
