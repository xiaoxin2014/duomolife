package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.HotSearchTagEntity;
import com.amkj.dmsh.bean.HotSearchTagEntity.HotSearchTagBean;
import com.amkj.dmsh.homepage.ListHistoryDataSave;
import com.amkj.dmsh.homepage.adapter.HotSearchAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.IndentSearchDetailsActivity;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_ALL;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_INDENT;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_HOT_SEARCH_LIST;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

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
    @BindView(R.id.flex_communal_tag)
    public FlexboxLayout flex_communal_tag;
    //    热门搜索
    @BindView(R.id.ll_search_hot)
    public LinearLayout ll_search_hot;
    @BindView(R.id.communal_recycler_wrap)
    public RecyclerView communal_recycler_wrap;
    private List<HotSearchTagBean> hotSearchList = new ArrayList<>();
    private LinkedList<String> dataHistoryList = new LinkedList<>();
    private String searchType = "allSearch";
    private String SAVE_NAME = "SearchHistory";
    private HotSearchAdapter hotSearchAdapter;
//    搜索 获取传递信息参数
    public static final String SEARCH_DATA = "searchData";

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_search;
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
        flex_communal_tag.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
        flex_communal_tag.setDividerDrawable(getResources().getDrawable(R.drawable.item_divider_nine_dp_white));
        getHistoryData();
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        communal_recycler_wrap.setLayoutManager(flexboxLayoutManager);
        FlexboxItemDecoration flexboxItemDecoration = new FlexboxItemDecoration(HomePageSearchActivity.this);
        flexboxItemDecoration.setDrawable(getResources().getDrawable(R.drawable.item_divider_nine_dp_white));
        communal_recycler_wrap.addItemDecoration(flexboxItemDecoration);
        hotSearchAdapter = new HotSearchAdapter(hotSearchList);
        communal_recycler_wrap.setAdapter(hotSearchAdapter);
        hotSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HotSearchTagBean hotSearchTagBean = (HotSearchTagBean) view.getTag();
                if(hotSearchTagBean!=null){
                    if (!TextUtils.isEmpty(hotSearchTagBean.getAndroid_link())) {
                        setSkipPath(HomePageSearchActivity.this, hotSearchTagBean.getAndroid_link(), false);
                    } else {
                        getTagResult(hotSearchTagBean.getTag_name());
                    }
                }
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height == 0) {
                    ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).requestFocus();
                }
            }
        });
    }

    private void getTagResult(String cate) {
        if (!TextUtils.isEmpty(cate)) {
            Intent intent = new Intent();
            if (SEARCH_INDENT.equals(searchType)) {
                intent.setClass(HomePageSearchActivity.this, IndentSearchDetailsActivity.class);
                intent.putExtra(SEARCH_DATA, cate);
                startActivity(intent);
            } else {
                intent.setClass(HomePageSearchActivity.this, AllSearchDetailsActivity.class);
                intent.putExtra(SEARCH_DATA, cate);
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
        getHistoryData();
    }

    //    查询全部数据
    private void getHistoryData() {
        dataHistoryList.clear();
        try {
            ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(HomePageSearchActivity.this, SAVE_NAME);
            dataHistoryList.addAll(listHistoryDataSave.getDataList(searchType));
            if (dataHistoryList.size() > 0) {
                ll_search_history.setVisibility(View.VISIBLE);
                flex_communal_tag.removeAllViews();
                for (String historyData :dataHistoryList) {
                    TextView historySearchView = getLabelInstance().createHistorySearchRecord(HomePageSearchActivity.this, historyData);
                    historySearchView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tag = (String) v.getTag();
                            if(!TextUtils.isEmpty(tag)){
                                getTagResult(tag);
                            }
                        }
                    });
                    flex_communal_tag.addView(historySearchView);
                }
            } else {
                ll_search_history.setVisibility(View.GONE);
            }
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
    @OnClick(R.id.tv_search_history_hint)
    void deleteHistoryData(View view) {
        ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(HomePageSearchActivity.this, SAVE_NAME);
        listHistoryDataSave.delDataList(searchType);
        getHistoryData();
    }

    @Override
    protected void loadData() {
        if(!TextUtils.isEmpty(searchType)
                &&searchType.equals(SEARCH_ALL)){
            NetLoadUtils.getNetInstance().loadNetDataPost(this,H_HOT_SEARCH_LIST,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    hotSearchList.clear();
                    Gson gson = new Gson();
                    HotSearchTagEntity hotSearchTagEntity = gson.fromJson(result, HotSearchTagEntity.class);
                    if (hotSearchTagEntity != null) {
                        if (hotSearchTagEntity.getCode().equals(SUCCESS_CODE)) {
                            hotSearchList.addAll(hotSearchTagEntity.getHotSearchTagList());
                        }
                    }
                    hotSearchAdapter.notifyDataSetChanged();
                    if (hotSearchList.size() < 1) {
                        ll_search_hot.setVisibility(View.GONE);
                    }else{
                        ll_search_hot.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNotNetOrException() {
                    if (hotSearchList.size() < 1) {
                        ll_search_hot.setVisibility(View.GONE);
                    }
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }
}
