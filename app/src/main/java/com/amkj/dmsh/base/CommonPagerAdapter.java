package com.amkj.dmsh.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by xiaoxin on 2016/6/8.
 */
public abstract class CommonPagerAdapter<T> extends PagerAdapter {

    private List<String> mTitleList;
    private List<T> mDatas;
    protected Context mContext;
    private int mItemLayoutId;

    public CommonPagerAdapter(Context context, List<T> datas, int itemLayoutId) {
        mContext = context;
        mDatas = datas;
        mItemLayoutId = itemLayoutId;
    }

    public CommonPagerAdapter(Context context, List<T> datas, int itemLayoutId, List<String> titleList) {
        mContext = context;
        mDatas = datas;
        mItemLayoutId = itemLayoutId;
        mTitleList = titleList;
    }

    public void refresh(List<T> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, mItemLayoutId, null);
        convert(new ViewHolder(view), position, mDatas.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitleList != null) {
            return mTitleList.get(position);
        }
        return super.getPageTitle(position);
    }

    public abstract void convert(ViewHolder helper, int position, T item);

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
    }

    public void setOnClickListener() {

    }
}
