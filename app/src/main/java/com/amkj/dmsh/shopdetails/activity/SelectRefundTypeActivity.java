package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantVariable.SELECT_REFUND_TYPE;

/**
 * Created by xiaoxin on 2020/3/31
 * Version:v4.5.0
 * ClassDescription :选择售后类型
 */
public class SelectRefundTypeActivity extends BaseActivity {
    @BindView(R.id.tv_indent_back)
    TextView mTvIndentBack;
    @BindView(R.id.tv_indent_title)
    TextView mTvIndentTitle;
    @BindView(R.id.iv_indent_search)
    ImageView mIvIndentSearch;
    @BindView(R.id.iv_indent_service)
    ImageView mIvIndentService;
    @BindView(R.id.pb_down_invoice)
    ProgressBar mPbDownInvoice;
    @BindView(R.id.fl_service)
    FrameLayout mFlService;
    @BindView(R.id.tb_indent_bar)
    Toolbar mTbIndentBar;
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.rl_only_refund)
    RelativeLayout mRlOnlyRefund;
    @BindView(R.id.rl_return_goods)
    RelativeLayout mRlReturnGoods;
    @BindView(R.id.rl_exchange_goods)
    RelativeLayout mRlExchangeGoods;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_refund_type;
    }

    @Override
    protected void initViews() {
        mIvIndentSearch.setVisibility(View.GONE);
        mTvIndentTitle.setText("选择售后类型");
    }

    @Override
    protected void loadData() {
        if (getIntent() == null) return;
        String mGoods = getIntent().getStringExtra("goods");
        if (!TextUtils.isEmpty(mGoods)) {
            List<OrderProductNewBean> goodsBeanList = GsonUtils.fromJson(mGoods, new TypeToken<List<OrderProductNewBean>>() {
            }.getType());
            //初始化退款商品列表
            mCommunalRecycler.setLayoutManager(new LinearLayoutManager(this));
            mCommunalRecycler.setNestedScrollingEnabled(false);
            mCommunalRecycler.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
            DirectProductListAdapter directProductListAdapter = new DirectProductListAdapter(this, goodsBeanList, SELECT_REFUND_TYPE);
            mCommunalRecycler.setAdapter(directProductListAdapter);
            directProductListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (view.getId() == R.id.ll_product) {
                    OrderProductNewBean orderProductBean = (OrderProductNewBean) view.getTag();
                    if (orderProductBean != null) {
                        Intent intent1 = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                        intent1.putExtra("productId", String.valueOf(orderProductBean.getProductId()));
                        startActivity(intent1);
                    }
                }
            });
        }
    }

    @OnClick({R.id.tv_indent_back, R.id.iv_indent_service, R.id.rl_only_refund, R.id.rl_return_goods, R.id.rl_exchange_goods})
    public void onViewClicked(View view) {
        Intent intent = getIntent();
        intent.setClass(this, DirectApplyRefundActivity.class);
        switch (view.getId()) {
            case R.id.tv_indent_back:
                finish();
                break;
            case R.id.iv_indent_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(this, "选择售后类型");
                break;
            //仅退款(包含收到货和未收到货两种情况，所以这里无法确定refundType)
            case R.id.rl_only_refund:
                intent.putExtra("refundType", "");
                startActivity(intent);
                finish();
                break;
            //退货退款
            case R.id.rl_return_goods:
                intent.putExtra("refundType", ConstantVariable.RETURN_GOODS);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_exchange_goods:
                ConstantMethod.showImportantToast(this,
                        "亲，暂不支持换货功能，如果是因为质量问题或者发错货，建议您申请退货退款后重拍。"
                );
                break;
        }
    }
}
