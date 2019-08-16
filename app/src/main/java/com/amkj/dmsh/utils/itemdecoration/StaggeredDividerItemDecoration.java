package com.amkj.dmsh.utils.itemdecoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class StaggeredDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int interval;
    private boolean hasHead;

    public StaggeredDividerItemDecoration(int interval, boolean hasHead) {
        this.interval = interval;
        this.hasHead = hasHead;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        int position = parent.getChildAdapterPosition(view);
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        // 获取item在span中的下标
        int spanIndex = params.getSpanIndex();

        if (!(parent.getChildAdapterPosition(view) == 0 && hasHead)) {
            if (spanIndex % 2 == 0) {
                outRect.left = interval;
//                outRect.right = interval / 2;
            } else {
//                outRect.left = interval / 2;
                outRect.right = interval;
            }
//            outRect.top = interval;
        }
    }
}