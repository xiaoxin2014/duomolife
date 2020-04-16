package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.SELECT_REFUND_GOODS;


/**
 * Created by xiaoxin on 2020/4/1
 * Version:v4.4.3
 * ClassDescription :多件商品退款-选择退款商品
 */
public class SelectRefundGoodsActivity extends BaseActivity {
    @BindView(R.id.tv_indent_title)
    TextView mTvIndentTitle;
    @BindView(R.id.iv_indent_search)
    ImageView mIvIndentSearch;
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.ll_goods)
    LinearLayout ll_goods;

    private boolean isAll;
    private List<OrderProductNewBean> mGoodsBeanList = new ArrayList<>();
    private DirectProductListAdapter mDirectProductListAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_refund_goods;
    }

    @Override
    protected void initViews() {
        mTvIndentTitle.setText("选择退款商品");
        mIvIndentSearch.setVisibility(View.GONE);
        try {
            if (getIntent().getExtras() == null) return;
            String mGoods = getIntent().getStringExtra("goods");
            if (!TextUtils.isEmpty(mGoods)) {
                mGoodsBeanList = new Gson().fromJson(mGoods, new TypeToken<List<OrderProductNewBean>>() {
                }.getType());
                //初始化退款商品列表
                mCommunalRecycler.setLayoutManager(new LinearLayoutManager(this));
                mCommunalRecycler.setNestedScrollingEnabled(false);
                mCommunalRecycler.addItemDecoration(new ItemDecoration.Builder()
                        // 设置分隔线资源ID
                        .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
                mDirectProductListAdapter = new DirectProductListAdapter(this, mGoodsBeanList, SELECT_REFUND_GOODS);
                mCommunalRecycler.setAdapter(mDirectProductListAdapter);
                mDirectProductListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    OrderProductNewBean orderProductNewBean = (OrderProductNewBean) view.getTag();
                    if (orderProductNewBean != null) {
                        if (view.getId() == R.id.checkbox_refund) {
                            orderProductNewBean.setChecked(!orderProductNewBean.isChecked());
                        } else if (view.getId() == R.id.ll_product) {
                            Intent intent1 = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                            intent1.putExtra("productId", String.valueOf(orderProductNewBean.getProductId()));
                            startActivity(intent1);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mGoodsBeanList != null && mGoodsBeanList.size() > 0) {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        } else {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
        }
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.tv_indent_back, R.id.iv_indent_service, R.id.cb_all, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_indent_back:
                finish();
                break;
            case R.id.iv_indent_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(getActivity(), "选择退款商品");
                break;
            //全选
            case R.id.cb_all:
                isAll = !isAll;
                for (OrderProductNewBean orderProductNewBean : mGoodsBeanList) {
                    orderProductNewBean.setChecked(isAll);
                }
                mDirectProductListAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_confirm:
                List<OrderProductNewBean> goodsBeanList = new ArrayList<>();
                for (OrderProductNewBean orderProductNewBean : mGoodsBeanList) {
                    if (orderProductNewBean.isChecked()) {
                        goodsBeanList.add(orderProductNewBean);
                    }
                }

                if (goodsBeanList.size() > 0) {
                    getIntent().setClass(getActivity(), DirectApplyRefundActivity.class);
                    Bundle extras = getIntent().getExtras();
                    extras.putString("goods", new Gson().toJson(goodsBeanList));
                    getIntent().putExtras(extras);
                    startActivity(getIntent());
                    finish();
                } else {
                    ConstantMethod.showToast(getActivity(), "请选择退款商品");
                }

                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return ll_goods;
    }
}
