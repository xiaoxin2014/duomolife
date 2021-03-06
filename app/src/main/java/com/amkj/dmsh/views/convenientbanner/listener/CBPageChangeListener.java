package com.amkj.dmsh.views.convenientbanner.listener;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sai on 15/7/29.
 * 翻页指示器适配器
 */
public class CBPageChangeListener implements OnPageChangeListener {
    private ArrayList<ImageView> pointViews;
    private int[] page_indicatorId;
    private OnPageChangeListener onPageChangeListener;
    private ViewGroup loPageTurningPoint;

    public CBPageChangeListener(ViewGroup loPageTurningPoint, ArrayList<ImageView> pointViews, int page_indicatorId[]) {
        this.loPageTurningPoint = loPageTurningPoint;
        this.pointViews = pointViews;
        this.page_indicatorId = page_indicatorId;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (onPageChangeListener != null)
            onPageChangeListener.onScrollStateChanged(recyclerView, newState);
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (onPageChangeListener != null) onPageChangeListener.onScrolled(recyclerView, dx, dy);
        //滚动时指示器自动显示
        if (loPageTurningPoint != null) loPageTurningPoint.setVisibility(View.VISIBLE);
    }

    public void onPageSelected(int index) {
        for (int i = 0; i < pointViews.size(); i++) {
            pointViews.get(index).setImageResource(page_indicatorId[1]);
            if (index != i) {
                pointViews.get(i).setImageResource(page_indicatorId[0]);
            }
        }
        if (onPageChangeListener != null) onPageChangeListener.onPageSelected(index);

    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
