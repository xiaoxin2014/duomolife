package com.amkj.dmsh.homepage.fragment;

import android.content.Context;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/14 0014
 * Version:v4.0.0
 * ClassDescription :
 */
public class HomeWelfareAdapter extends BaseQuickAdapter<DMLThemeBean,BaseViewHolder> {

    public HomeWelfareAdapter(Context context, @Nullable List<DMLThemeBean> data) {
        super(R.layout.item_home_walfare, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DMLThemeBean item) {

    }
}
