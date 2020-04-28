package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.bean.AllSearchEntity;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.homepage.activity.ArticleOfficialActivity;
import com.amkj.dmsh.homepage.adapter.SpecialTopicAdapter;
import com.amkj.dmsh.homepage.bean.TopicSpecialEntity.TopicSpecialBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.ARTICLE_SEARCH_KEY;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.Url.H_HOT_SEARCH_ALL;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/27
 * class description:搜索文章
 */
public class SearchDetailsArticleFragment extends BaseSearchDetailFragment {
    private List<TopicSpecialBean> specialSearList = new ArrayList<>();
    private SpecialTopicAdapter specialTopicAdapter;

    @Override
    protected void initRv() {
        setFloatingButton(download_btn_communal, communal_recycler);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        specialTopicAdapter = new SpecialTopicAdapter(getActivity(), specialSearList);
        specialTopicAdapter.setOnItemClickListener((adapter, view, position) -> {
            TopicSpecialBean topicSpecialBean = (TopicSpecialBean) view.getTag();
            if (topicSpecialBean != null) {
                String type = topicSpecialBean.getType();
                Intent intent = new Intent();
                if ("1".equals(type)) {//文章
                    intent.setClass(getActivity(), ArticleOfficialActivity.class);
                    intent.putExtra("ArtId", String.valueOf(topicSpecialBean.getId()));
                    startActivity(intent);
                } else if ("2".equals(type)) {//福利社专题
                    intent.setClass(getActivity(), DoMoLifeWelfareDetailsActivity.class);
                    intent.putExtra("welfareId", String.valueOf(topicSpecialBean.getId()));
                    startActivity(intent);
                }
            }
        });
        communal_recycler.setAdapter(specialTopicAdapter);

        specialTopicAdapter.setOnLoadMoreListener(() -> {
            page++;
            getSearchDetail();
        }, communal_recycler);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    protected void getSearchDetail() {
        if (TextUtils.isEmpty(getKeywords())) return;
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", getKeywords());
        params.put("searchType", getSearchType());
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_HOT_SEARCH_ALL,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        searchSucess = true;
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        if (page == 1) {
                            specialSearList.clear();
                            removeExistUtils.clearData();
                        }
                        Gson gson = new Gson();
                        allSearchEntity = gson.fromJson(result, AllSearchEntity.class);
                        if (allSearchEntity != null && allSearchEntity.getSearchBean() != null) {
                            String code = allSearchEntity.getCode();
                            searchBean = allSearchEntity.getSearchBean();
                            setWordData(searchBean.getWatchword());
                            List<TopicSpecialBean> topics = searchBean.getDocumentList();
                            if (code.equals(SUCCESS_CODE) && topics != null && topics.size() > 0) {
                                specialSearList.addAll(removeExistUtils.removeExistList(searchBean.getDocumentList()));
                                specialTopicAdapter.loadMoreComplete();
                            } else if (allSearchEntity.getCode().equals(ERROR_CODE)) {
                                showToast( allSearchEntity.getMsg());
                                specialTopicAdapter.loadMoreFail();
                            } else {
                                specialTopicAdapter.loadMoreEnd();
                            }
                        } else {
                            specialTopicAdapter.loadMoreEnd();
                        }
                        specialTopicAdapter.notifyDataSetChanged();

                        NetLoadUtils.getNetInstance().showLoadSir(loadService, specialSearList, allSearchEntity);
                        updateSearchNum();
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        specialTopicAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, specialSearList, allSearchEntity);
                    }
                });
    }


    @Override
    protected String getSearchKey() {
        return ARTICLE_SEARCH_KEY;
    }
}
