package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.adapter.TopicAdapter;
import com.amkj.dmsh.find.adapter.TopicCatergoryAdapter;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.find.bean.HotTopicEntity;
import com.amkj.dmsh.find.bean.HotTopicEntity.HotTopicBean;
import com.amkj.dmsh.find.bean.TopicCaterGoryEntity;
import com.amkj.dmsh.find.bean.TopicCaterGoryEntity.TopicCaterGoryBean;
import com.amkj.dmsh.homepage.activity.AllSearchDetailsNewActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2019/7/16
 * Version:v4.1.0
 * ClassDescription :发现-主题分类
 */
public class TopicCatergoryActivity extends BaseActivity {
    @BindView(R.id.et_search)
    TextView mEtSearch;
    @BindView(R.id.rv_catergory)
    RecyclerView mRvCatergory;
    @BindView(R.id.rv_topic)
    RecyclerView mRvTopic;
    List<TopicCaterGoryBean> mCatergoryList = new ArrayList<>();
    List<HotTopicBean> mTopicList = new ArrayList<>();
    @BindView(R.id.ll_topic)
    LinearLayout mLlTopic;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    private TopicCatergoryAdapter mCatergoryAdapter;
    private TopicAdapter mTopicAdapter;
    private TopicCaterGoryEntity topicCaterGoryEntity;
    private HotTopicEntity hotTopicEntity;


    @Override
    protected int getContentView() {
        return R.layout.activity_topic_catergory;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("全部话题");
        mTlNormalBar.setSelected(true);
        mTvHeaderShared.setVisibility(View.GONE);
        //初始化分类列表
        mCatergoryAdapter = new TopicCatergoryAdapter(mCatergoryList);
        mRvCatergory.setLayoutManager(new LinearLayoutManager(this));
        mRvCatergory.setAdapter(mCatergoryAdapter);
        mCatergoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            TopicCaterGoryBean topicCaterGoryBean = (TopicCaterGoryBean) view.getTag();
            if (topicCaterGoryBean != null) {
                mCatergoryAdapter.setCatergoryId(topicCaterGoryBean.getId());
                mCatergoryAdapter.notifyDataSetChanged();
                getTopic(topicCaterGoryBean.getId());
//                List<FindHotTopicBean> topicList = getTopicList(catergoryName);
//                mTopicAdapter.setNewData(topicList);
            }
        });


        //初始化话题列表
        mTopicAdapter = new TopicAdapter(this, mTopicList);
        mRvTopic.setLayoutManager(new LinearLayoutManager(this));
        mRvTopic.setAdapter(mTopicAdapter);
        mTopicAdapter.setOnItemClickListener((adapter, view, position) -> {
            FindHotTopicBean findHotTopicBean = (FindHotTopicBean) view.getTag();
            if (findHotTopicBean != null) {
                ConstantMethod.skipTopicDetail(getActivity(), String.valueOf(findHotTopicBean.getId()));
            }
        });
    }

    @Override
    protected void loadData() {
        getTopicCatergory();
    }

    //获取话题分类
    private void getTopicCatergory() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_TOPIC_CATERGORY, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mCatergoryList.clear();
                topicCaterGoryEntity = new Gson().fromJson(result, TopicCaterGoryEntity.class);
                if (topicCaterGoryEntity != null) {
                    String code = topicCaterGoryEntity.getCode();
                    List<TopicCaterGoryBean> catergoryList = topicCaterGoryEntity.getCatergoryList();
                    if (SUCCESS_CODE.equals(code) && catergoryList != null && catergoryList.size() > 0) {
                        mCatergoryList.addAll(catergoryList);
                        TopicCaterGoryBean topicCaterGoryBean = catergoryList.get(0);
                        if (topicCaterGoryBean != null) {
                            //默认选中并加载第一个分类相关话题
                            mCatergoryAdapter.setCatergoryId(topicCaterGoryBean.getId());
                            getTopic(topicCaterGoryBean.getId());
                        }
                    }
                }

                mCatergoryAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCatergoryList, topicCaterGoryEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCatergoryList, topicCaterGoryEntity);
            }
        });
    }

    //获取分类话题
    private void getTopic(int categoryId) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_CATERGORY_TOPIC, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mTopicList.clear();
                hotTopicEntity = new Gson().fromJson(result, HotTopicEntity.class);
                if (hotTopicEntity != null) {
                    String code = hotTopicEntity.getCode();
                    List<HotTopicBean> topicList = hotTopicEntity.getHotTopicList();
                    if (SUCCESS_CODE.equals(code)) {
                        mTopicList.addAll(topicList);
                    } else if (ERROR_CODE.equals(code)) {
                        ConstantMethod.showToast(topicCaterGoryEntity.getMsg());
                    }
                }

                mTopicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNotNetOrException() {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.et_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_search:
                Intent intent = new Intent(this, AllSearchDetailsNewActivity.class);
                intent.putExtra("defaultTab", "2");
                startActivity(intent);
                break;
        }
    }


    private List<HotTopicBean> getTopicList(String catergoryName) {
        List<HotTopicBean> list = new ArrayList<>();
        for (HotTopicBean findHotTopicBean : mTopicList) {
            if (catergoryName.equals(findHotTopicBean.getTitle())) {
                list.add(findHotTopicBean);
            }
        }

        return list;
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlTopic;
    }
}
