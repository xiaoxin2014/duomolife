package com.amkj.dmsh.homepage.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.AllSearchEntity;
import com.amkj.dmsh.bean.UserSearchEntity.UserSearchBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.SearchDetailsUserAdapter;
import com.amkj.dmsh.mine.bean.UserAttentionFansEntity.UserAttentionFansBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.USER_SEARCH_KEY;


/**
 * Created by atd48 on 2016/7/4.
 */
public class SearchDetailsUserFragment extends BaseSearchDetailFragment {
    private SearchDetailsUserAdapter userRecyclerAdapter;
    List<UserAttentionFansBean> userAttentionFansList = new ArrayList<>();

    @Override
    protected void initRv() {
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                .create());
        userRecyclerAdapter = new SearchDetailsUserAdapter(getActivity(), userAttentionFansList, "search");
        communal_recycler.setAdapter(userRecyclerAdapter);
        userRecyclerAdapter.setEmptyView(R.layout.layout_search_user_empty, (ViewGroup) communal_recycler.getParent());
        userRecyclerAdapter.setOnLoadMoreListener(() -> {
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.H_HOT_SEARCH_ALL,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        searchSucess = true;
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        if (page == 1) {
                            userAttentionFansList.clear();
                            removeExistUtils.clearData();
                        }
                        Gson gson = new Gson();
                        allSearchEntity = gson.fromJson(result, AllSearchEntity.class);
                        if (allSearchEntity != null && allSearchEntity.getSearchBean() != null) {
                            String code = allSearchEntity.getCode();
                            searchBean = allSearchEntity.getSearchBean();
                            setWordData(searchBean.getWatchword());
                            List<UserSearchBean> userSearchList = searchBean.getUserList();
                            if (SUCCESS_CODE.equals(code) && userSearchList != null && userSearchList.size() > 0) {
                                List<UserSearchBean> list = removeExistUtils.removeExistList(userSearchList);
                                for (UserSearchBean userSearchBean : list) {
                                    UserAttentionFansBean userAttentionFansBean = new UserAttentionFansBean();
                                    userAttentionFansBean.setFavatar(userSearchBean.getAvatar());
                                    userAttentionFansBean.setFlag(userSearchBean.isFllow());
                                    userAttentionFansBean.setFuid(userSearchBean.getId());
                                    userAttentionFansBean.setFnickname(userSearchBean.getNickName());
                                    userAttentionFansList.add(userAttentionFansBean);
                                }
                                userRecyclerAdapter.loadMoreComplete();
                            } else if (allSearchEntity.getCode().equals(ERROR_CODE)) {
                                showToast(getActivity(), allSearchEntity.getMsg());
                                userRecyclerAdapter.loadMoreFail();
                            } else {
                                userRecyclerAdapter.loadMoreEnd();
                            }
                        } else {
                            userRecyclerAdapter.loadMoreEnd();
                        }
                        userRecyclerAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, userAttentionFansList, allSearchEntity);
                        updateSearchNum();
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        userRecyclerAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, userAttentionFansList, allSearchEntity);
                    }
                });
    }

    @Override
    protected String getSearchKey() {
        return USER_SEARCH_KEY;
    }
}
