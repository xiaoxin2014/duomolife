package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.catergory.bean.CatergoryOneLevelEntity.CatergoryOneLevelBean.ChildCategoryListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :二级分类名称适配器
 */
public class CatergoryNameAdapter extends BaseQuickAdapter<ChildCategoryListBean, BaseViewHolder> {

    private int mSelectPosition;

    public CatergoryNameAdapter(Context context, @Nullable List<ChildCategoryListBean> data, int position) {
        super(R.layout.item_catergory_name, data);
        mSelectPosition = position;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChildCategoryListBean childCategoryListBean) {
        helper.setText(R.id.tv_catergory_name, childCategoryListBean.getName());
        helper.itemView.setVisibility(helper.getPosition() != mSelectPosition ? View.VISIBLE : View.GONE);
    }

    public void setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;
    }
}
