package com.amkj.dmsh.shopdetails.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.EditTextWithClear;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_COUPON_PRODUCT_TYPE_LIST;

/**
 * Created by xiaoxin on 2020/4/7
 * Version:v4.5.0
 * ClassDescription :搜索优惠券相关商品
 */
public class SearchCouponGoodsActivity extends BaseActivity {

    @BindView(R.id.et_search_input)
    EditTextWithClear etSearchInput;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton downloadBtnCommunal;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;
    private GoodProductAdapter mGoodProductAdapter;
    private UserLikedProductEntity likedProductEntity;
    //    商品列表
    private List<LikedProductBean> goodsList = new ArrayList<>();
    private int page = 1;
    private Map<String, Object> mParams;
    private boolean isFirst = true;


    @Override
    protected int getContentView() {
        return R.layout.activity_search_coupon_goods;
    }

    @Override
    protected void initViews() {
        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra("params"))) {
            showToast("数据错误");
            return;
        }

        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        mParams = JSON.parseObject(getIntent().getStringExtra("params"));
        etSearchInput.setHint("输入商品关键词");
        etSearchInput.setFocusableInTouchMode(true);
        communal_recycler.setLayoutManager(new GridLayoutManager(this, 2));
        mGoodProductAdapter = new GoodProductAdapter(this, goodsList);
        communal_recycler.setAdapter(mGoodProductAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                .setDividerId(R.drawable.item_divider_five_dp).create());
        mGoodProductAdapter.setOnLoadMoreListener(() -> {
            page++;
            getQualityTypePro();
        }, communal_recycler);
        setFloatingButton(downloadBtnCommunal, communal_recycler);

        // 搜索框的键盘搜索键点击回调
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    KeyboardUtils.hideSoftInput(getActivity());
                    //跳转页面，模糊搜索
                    page = 1;
                    NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                    getQualityTypePro();
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

    @Override
    protected void loadData() {
        if (!isFirst) {
            getQualityTypePro();
        }
    }


    private void getQualityTypePro() {
        mParams.put("currentPage", page);
        mParams.put("searchTitle", etSearchInput.getText().toString().trim());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_COUPON_PRODUCT_TYPE_LIST
                , mParams, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        if (page == 1) {
                            goodsList.clear();
                        }

                        likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            List<LikedProductBean> resultList = likedProductEntity.getGoodsList();
                            String code = likedProductEntity.getCode();
                            String msg = likedProductEntity.getMsg();
                            if (SUCCESS_CODE.equals(code)) {
                                goodsList.addAll(resultList);
                                mGoodProductAdapter.notifyDataSetChanged();
                                //不满一页
                                if (resultList.size() < TOTAL_COUNT_TWENTY) {
                                    mGoodProductAdapter.loadMoreEnd();
                                } else {
                                    mGoodProductAdapter.loadMoreComplete();
                                }
                            } else if (!EMPTY_CODE.equals(code)) {
                                showToast (msg);
                                mGoodProductAdapter.loadMoreFail();
                            } else {
                                mGoodProductAdapter.loadMoreEnd();
                            }
                        } else {
                            mGoodProductAdapter.loadMoreEnd();
                        }

                        mGoodProductAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, goodsList, likedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        mGoodProductAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, goodsList, likedProductEntity);
                    }
                });
    }

    @OnClick(R.id.tv_home_search_cancel)
    void cancel(View view) {
        finish();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return fl_content;
    }

    @Override
    protected String getEmptyText() {
        return "没有找到相关商品\n建议您换个搜索词试试";
    }

    @Override
    protected int getEmptyResId() {
        return R.drawable.search_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirst = false;
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
        if (v instanceof EditText) {
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
}
