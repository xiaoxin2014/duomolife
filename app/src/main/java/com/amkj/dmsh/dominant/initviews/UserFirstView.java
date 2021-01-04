package com.amkj.dmsh.dominant.initviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.adapter.UserFirstAdapter;
import com.amkj.dmsh.time.bean.UserFirstEntity;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2021/1/4
 * Version:v4.9.5
 * ClassDescription :新人专享-首单0元购模块
 */
public class UserFirstView extends LinearLayout {
    @BindView(R.id.tv_first_amount)
    TextView mTvFirstAmount;
    @BindView(R.id.rv_first_goods)
    RecyclerView mRvFirstGoods;
    @BindView(R.id.ll_new_user_first)
    LinearLayout mLlNewUserFirst;
    private List<UserFirstEntity.UserFirstBean> mUserFirstList = new ArrayList<>();
    private UserFirstAdapter mUserFirstAdapter;


    public UserFirstView(Context context) {
        super(context);
        View headView = LayoutInflater.from(context).inflate(R.layout.layout_new_user_first, this, true);
        ButterKnife.bind(this, headView);
        //初始化新人首单0元购商品列表
        mUserFirstAdapter = new UserFirstAdapter(context, mUserFirstList);
        mRvFirstGoods.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        mRvFirstGoods.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_10_mm_transparent)
                .setFirstDraw(true)
                .create());
        mRvFirstGoods.setAdapter(mUserFirstAdapter);
    }

    public void updateView(UserFirstEntity userFirstEntity) {
        mUserFirstList.clear();
        if (userFirstEntity != null) {
            List<UserFirstEntity.UserFirstBean> userFirstBeans = userFirstEntity.getResult();
            if (userFirstBeans != null && userFirstBeans.size() > 0) {
                mTvFirstAmount.setText("满¥" + userFirstEntity.getMinStartPrice());
                mUserFirstList.addAll(userFirstBeans);
            }
        }
        mUserFirstAdapter.notifyDataSetChanged();
        mLlNewUserFirst.setVisibility(mUserFirstList.size() > 0 ? VISIBLE : GONE);
    }

}
