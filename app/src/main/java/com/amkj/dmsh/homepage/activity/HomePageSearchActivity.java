package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.HotSearchTagEntity;
import com.amkj.dmsh.bean.HotSearchTagEntity.HotSearchTagBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.ListHistoryDataSave;
import com.amkj.dmsh.shopdetails.activity.IndentSearchDetailsActivity;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.flowlayout.FlowLayout;
import com.amkj.dmsh.views.flowlayout.TagAdapter;
import com.amkj.dmsh.views.flowlayout.TagFlowLayout;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_ALL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_INDENT;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;

;

/**
 * Created by atd48 on 2016/6/24.
 */
public class HomePageSearchActivity extends BaseActivity {
    @BindView(R.id.et_search_input)
    public EditText et_search_input;
    //    最近搜索
    @BindView(R.id.ll_search_history)
    public LinearLayout ll_search_history;
    @BindView(R.id.tfl_search_history)
    public TagFlowLayout tfl_search_history;
    //    热门搜索
    @BindView(R.id.ll_search_hot)
    public LinearLayout ll_search_hot;
    @BindView(R.id.tfl_search_hot)
    public TagFlowLayout tfl_search_hot;
    private List<HotSearchTagBean> hotSearchList = new ArrayList<>();
    private LinkedList<String> dataHistoryList = new LinkedList<>();
    private String searchType = "allSearch";
    private String SAVE_NAME = "SearchHistory";
    private TagAdapter tagHistoryAdapter;
    private TagAdapter hotSearchAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }
    @Override
    protected void initViews() {
        Intent intent = getIntent();
        String searchType = intent.getStringExtra(SEARCH_TYPE);
        if(!TextUtils.isEmpty(searchType)){
            this.searchType = searchType;
        }
        // 搜索框的键盘搜索键点击回调
        et_search_input.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText view = (EditText) v;
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //跳转页面，模糊搜索
                    skipDetailsPage(view);
                }
                return false;
            }
        });
        tagHistoryAdapter = new TagAdapter<String>(dataHistoryList) {
            @Override
            public View getView(FlowLayout parent, int position, String historyTag) {
                View view = LayoutInflater.from(HomePageSearchActivity.this).inflate(R.layout.hotsearch_tv, null, false);
                TextView textView = (TextView) view.findViewById(R.id.tv_search_tag);
                view.setPadding(0, 20, 24, 0);
                AutoUtils.autoSize(view);
                textView.setText(getStrings(historyTag));
                return view;
            }
        };
        getHistoryData();
        tfl_search_history.setAdapter(tagHistoryAdapter);
        tfl_search_history.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                getTagResult(dataHistoryList.get(position));
                return false;
            }
        });
        hotSearchAdapter = new TagAdapter<HotSearchTagBean>(hotSearchList) {
            @Override
            public View getView(FlowLayout parent, int position, HotSearchTagBean hotSearchTagBean) {
                View view = LayoutInflater.from(HomePageSearchActivity.this).inflate(R.layout.hotsearch_tv, null, false);
                TextView textView = (TextView) view.findViewById(R.id.tv_search_tag);
                if (!TextUtils.isEmpty(hotSearchTagBean.getAndroid_link())) {
                    textView.setSelected(true);
                } else {
                    textView.setSelected(false);
                }
//                view.setPadding(0, 20, 24, 0);
                AutoUtils.autoSize(view);
                textView.setText(hotSearchTagBean.getTag_name());
                return view;
            }
        };
        tfl_search_hot.setAdapter(hotSearchAdapter);
        tfl_search_hot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                HotSearchTagBean hotSearchTagBean = hotSearchList.get(position);
                if (!TextUtils.isEmpty(hotSearchTagBean.getAndroid_link())) {
                    setSkipPath(HomePageSearchActivity.this, hotSearchTagBean.getAndroid_link(), false);
                } else {
                    getTagResult(hotSearchTagBean.getTag_name());
                }
                return false;
            }
        });
    }

    private void getTagResult(String cate) {
        if (!TextUtils.isEmpty(cate)) {
            Intent intent = new Intent();
            if (SEARCH_INDENT.equals(searchType)) {
                intent.setClass(HomePageSearchActivity.this, IndentSearchDetailsActivity.class);
                intent.putExtra("data", cate);
                startActivity(intent);
            } else {
                intent.setClass(HomePageSearchActivity.this, AllSearchDetailsActivity.class);
                intent.putExtra("data", cate);
                startActivity(intent);
            }
            finish();
            overridePendingTransition(0, 0);
            // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在提前
//        插入数据
            insertHistoryData(cate);
        } else {
            showToast(this, "请输入关键词");
        }
    }

    private void insertHistoryData(String text) {
        dataHistoryList.clear();
        ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(HomePageSearchActivity.this, SAVE_NAME);
        dataHistoryList.addAll(listHistoryDataSave.<String>getDataList(searchType));
        if (dataHistoryList.size() > 0) {
            Iterator iterator = dataHistoryList.listIterator();
            boolean hasNext = iterator.hasNext();
            while (hasNext) {
                String str = (String) iterator.next();
                if (str.equals(text)) {
                    dataHistoryList.remove(text);
                    dataHistoryList.addFirst(text);
                    break;
                }
                hasNext = iterator.hasNext();
                if (!hasNext) {
                    dataHistoryList.addFirst(text);
                }
            }
        } else {
            dataHistoryList.addFirst(text);
        }
        listHistoryDataSave.setDataList(searchType, dataHistoryList);
        tagHistoryAdapter.notifyDataChanged();
    }

    //
    private void getHistory() {
        if (dataHistoryList.size() > 0) {
            ll_search_history.setVisibility(View.VISIBLE);
        } else {
            ll_search_history.setVisibility(View.GONE);
        }
    }

    //    查询全部数据
    private void getHistoryData() {
        dataHistoryList.clear();
        try {
            ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(HomePageSearchActivity.this, SAVE_NAME);
            dataHistoryList.addAll(listHistoryDataSave.<String>getDataList(searchType));
            tagHistoryAdapter.notifyDataChanged();
            getHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void skipDetailsPage(EditText view) {
        if (view.getText().toString().trim().length() > 0) {
            String cate = view.getText().toString().trim();
            getTagResult(cate);
        } else {
            showToast(HomePageSearchActivity.this, "你输入为空，请重新输入！");
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refresh")) {
            getTagResult((String) message.result);
        }
    }

    @OnClick(R.id.tv_home_search_cancel)
    void goBack(View view) {
        finish();
    }

    //  清除历史记录
    @OnClick(R.id.iv_search_record)
    void deleteHistoryData(View view) {
        ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(HomePageSearchActivity.this, SAVE_NAME);
        listHistoryDataSave.delDataList(searchType);
        getHistoryData();
    }

    @Override
    protected void loadData() {
        if(!TextUtils.isEmpty(searchType)
                &&searchType.equals(SEARCH_ALL)){
            String url = Url.BASE_URL + Url.H_HOT_SEARCH_LIST;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    hotSearchList.clear();
                    Gson gson = new Gson();
                    HotSearchTagEntity hotSearchTagEntity = gson.fromJson(result, HotSearchTagEntity.class);
                    if (hotSearchTagEntity != null) {
                        if (hotSearchTagEntity.getCode().equals("01")) {
                            hotSearchList.addAll(hotSearchTagEntity.getHotSearchTagList());
                        }
                    }
                    hotSearchAdapter.notifyDataChanged();
                    if (hotSearchList.size() < 1) {
                        ll_search_hot.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (hotSearchList.size() < 1) {
                        ll_search_hot.setVisibility(View.GONE);
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        }else{
            ll_search_hot.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_home_search_cancel)
    void cancel(View view) {
        finish();
    }

    @OnClick(R.id.tv_search_leave_mes)
    void leaveMes(View view) {
        Intent intent = new Intent(HomePageSearchActivity.this, SearchLeaveMessageActivity.class);
        startActivity(intent);
    }

}
