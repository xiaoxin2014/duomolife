package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :二级分类名称适配器
 */
public class CatergoryNameAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public CatergoryNameAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_catergory_name, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String caterGoryName) {
        helper.setText(R.id.tv_catergory_name,caterGoryName);
    }

}
