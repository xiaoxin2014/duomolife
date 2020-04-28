package com.amkj.dmsh.homepage.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.find.bean.EventMessageBean;
import com.amkj.dmsh.homepage.ListHistoryDataSave;
import com.amkj.dmsh.homepage.adapter.SearchTabPageNewAdapter;
import com.amkj.dmsh.homepage.fragment.SearchDetailsArticleFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsProductNewFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsTopicFragment;
import com.amkj.dmsh.homepage.fragment.SearchDetailsUserFragment;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.ALL_SEARCH_KEY;
import static com.amkj.dmsh.constant.ConstantVariable.ARTICLE_SEARCH_KEY;
import static com.amkj.dmsh.constant.ConstantVariable.SEARCH_DATA;
import static com.amkj.dmsh.constant.ConstantVariable.TOPIC_SEARCH_KEY;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_SEARCH_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.USER_SEARCH_KEY;


public class AllSearchDetailsNewActivity extends BaseActivity {
    @BindView(R.id.et_search_input)
    EditText et_search_input;
    @BindView(R.id.sliding_search_bar)
    SlidingTabLayout sliding_search_bar;
    @BindView(R.id.vp_search_details_container)
    ViewPager vp_search_details_container;
    private LinkedList<String> dataHistoryList = new LinkedList<>();
    private String SAVE_NAME = "SearchHistory";
    private String defaultTab;//默认选中tab位置
    private SearchTabPageNewAdapter searchTabPageAdapter;
    private String[] title = {"商品", "种草", "话题", "用户"};

    @Override
    protected int getContentView() {
        return R.layout.activity_search_new_details;
    }

    @Override
    protected void initViews() {
        if (getIntent() != null) {
            defaultTab = getIntent().getStringExtra("defaultTab");
        }
        et_search_input.setFocusableInTouchMode(true);
        searchTabPageAdapter = new SearchTabPageNewAdapter(getSupportFragmentManager());
        vp_search_details_container.setAdapter(searchTabPageAdapter);
        vp_search_details_container.setOffscreenPageLimit(searchTabPageAdapter.getCount() - 1);
        sliding_search_bar.setViewPager(vp_search_details_container);
        vp_search_details_container.setCurrentItem(getStringChangeIntegers(defaultTab));
        // 搜索框的键盘搜索键点击回调
        et_search_input.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText view = (EditText) v;
                String searchDate = view.getText().toString().trim();
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    KeyboardUtils.hideSoftInput(AllSearchDetailsNewActivity.this);
                    //跳转页面，模糊搜索
                    if (!TextUtils.isEmpty(searchDate)) {
                        EventBus.getDefault().post(new EventMessage(SEARCH_DATA, new EventMessageBean(getCurrentFragment(), searchDate)));
                        insertHistoryData(searchDate);
                    } else {
                        showToast("请输入搜索内容");
                    }
                }
                return false;
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

    private void insertHistoryData(String text) {
        if (!TextUtils.isEmpty(text)) {
            dataHistoryList.clear();
            ListHistoryDataSave listHistoryDataSave = new ListHistoryDataSave(AllSearchDetailsNewActivity.this, SAVE_NAME);
            dataHistoryList.addAll(listHistoryDataSave.<String>getDataList(getSearchType()));
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
            listHistoryDataSave.setDataList(getSearchType(), dataHistoryList);
        } else {
            showToast("请输入搜索内容");
        }
    }

    @Override
    protected void loadData() {
    }

    @OnClick(R.id.tv_home_search_cancel)
    void cancel(View view) {
        finish();
    }

    /**
     * 点击编辑器外区域隐藏键盘 避免点击搜索完没有隐藏键盘
     */
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
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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

    //返回当前搜索类型在本地所对应的的key
    private String getSearchType() {
        switch (vp_search_details_container.getCurrentItem()) {
            case 0:
                return ALL_SEARCH_KEY;
            case 1:
                return ARTICLE_SEARCH_KEY;
            case 2:
                return TOPIC_SEARCH_KEY;
            case 3:
                return USER_SEARCH_KEY;
            default:
                return ALL_SEARCH_KEY;
        }
    }

    //获取当前tab类名
    private String getCurrentFragment() {
        switch (vp_search_details_container.getCurrentItem()) {
            case 0:
                return SearchDetailsProductNewFragment.class.getSimpleName();
            case 1:
                return SearchDetailsArticleFragment.class.getSimpleName();
            case 2:
                return SearchDetailsTopicFragment.class.getSimpleName();
            case 3:
                return SearchDetailsUserFragment.class.getSimpleName();
            default:
                return "";
        }
    }

    public String getKewords() {
        return et_search_input.getText().toString().trim();
    }

    public void updateKewords(String keywords) {
        et_search_input.setText(keywords);
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (UPDATE_SEARCH_NUM.equals(message.type)) {
            List<String> numList = (List<String>) message.result;
            //修改tab的数量（当前tab或者数量为0时不用显示数量）
            for (int i = 0; i < sliding_search_bar.getTabCount(); i++) {
                int num = getStringChangeIntegers(numList.get(i));
                String pageTitle = title[i];
                TextView titleView = sliding_search_bar.getTitleView(i);
                titleView.setText((pageTitle + (num == 0 ? "" : "·" + (num > 99 ? "99+" : String.valueOf(num)))));
            }
        }
    }
}
