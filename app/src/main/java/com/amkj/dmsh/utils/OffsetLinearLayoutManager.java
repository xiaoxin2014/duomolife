package com.amkj.dmsh.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/12/7
 * version 3.1.9
 * class description:滑动LinearLayoutManager
 */
public class OffsetLinearLayoutManager extends LinearLayoutManager {

    public OffsetLinearLayoutManager(Context context) {
        super(context);
    }

    private Map<Integer, Integer> heightMap = new HashMap<>();

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            heightMap.put(i, view.getHeight());
        }
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        try {
            int firstVisiblePosition = findFirstVisibleItemPosition();
            View firstVisibleView = findViewByPosition(firstVisiblePosition);
            int offsetY = -(int) (firstVisibleView.getY());
            for (int i = 0; i < firstVisiblePosition; i++) {
                offsetY += heightMap.get(i) == null ? 0 : heightMap.get(i);
            }
            return offsetY;
        } catch (Exception e) {
            return 0;
        }
    }

    public int computeVerticalScrollOffset(){
        if (getChildCount() == 0) {
            return 0;
        }
        try {
            int firstVisiblePosition = findFirstVisibleItemPosition();
            View firstVisibleView = findViewByPosition(firstVisiblePosition);
            int offsetY = -(int) (firstVisibleView.getY());
            for (int i = 0; i < firstVisiblePosition; i++) {
                offsetY += heightMap.get(i) == null ? 0 : heightMap.get(i);
            }
            return offsetY;
        } catch (Exception e) {
            return 0;
        }
    }
}
