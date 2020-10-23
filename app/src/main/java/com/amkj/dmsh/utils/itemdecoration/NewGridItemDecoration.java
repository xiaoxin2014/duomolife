package com.amkj.dmsh.utils.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.amkj.dmsh.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Created by Oubowu on 2016/7/21 15:38.
 * <p>这个是单独一个布局的标签</p>
 * <p>porting from https://github.com/takahr/pinned-section-item-decoration</p>
 * <p>注意：标签所在最外层布局不能设置marginTop，因为往上滚动遮不住真正的标签;marginBottom还有问题待解决</p>
 */
public class NewGridItemDecoration extends RecyclerView.ItemDecoration {

    private boolean isLastDraw;
    private boolean isFirstDraw;
    private int mDividerId;

    private Drawable mDrawable;
    private RecyclerView.Adapter mAdapter;

    // 缓存的标签
    private View mPinnedHeaderView;

    // 缓存的标签位置
    private int mPinnedHeaderPosition = -1;

    // 顶部标签的Y轴偏移值
    private int mPinnedHeaderOffset;

    // 用于锁定画布绘制范围
    private Rect mClipBounds;

    // 父布局的左间距
    private int mRecyclerViewPaddingLeft;
    // 父布局的顶间距
    private int mRecyclerViewPaddingTop;

    private int mHeaderLeftMargin;
    private int mHeaderTopMargin;
    private int mHeaderRightMargin;

    private int maxItemType = 200;

    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    private int mFirstVisiblePosition;

    private RecyclerView mParent;

    // 当我们调用mRecyclerView.addItemDecoration()方法添加decoration的时候，RecyclerView在绘制的时候，去会绘制decorator，即调用该类的onDraw和onDrawOver方法，
    // 1.onDraw方法先于drawChildren
    // 2.onDrawOver在drawChildren之后，一般我们选择复写其中一个即可。
    // 3.getItemOffsets 可以通过outRect.set()为每个Item设置一定的偏移量，主要用于绘制Decorator。

    private NewGridItemDecoration(Builder builder) {
        mDividerId = builder.dividerId;
        isLastDraw = builder.isLastDraw;
        isFirstDraw = builder.isFirstDraw;
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {

        checkCache(parent);

        if (mDrawable == null) {
            mDrawable = ContextCompat.getDrawable(parent.getContext(), mDividerId != 0 ? mDividerId : R.drawable.item_divider_five_gray_f);
        }

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            if (!isPinnedHeader(parent, view)) {
                final int spanCount = getSpanCount(parent);
                int position = parent.getChildAdapterPosition(view);
                if (parent.getAdapter().getItemCount() > 0) {
                    if (isHeader(parent, position)) {
                        outRect.set(0, 0, 0, 0);
                    } else {
                        //除了第一列，显示左间距
                        outRect.set(isFirstColumn(parent, position, spanCount) ? 0 : mDrawable.getIntrinsicWidth(), isFirstRow(parent, position, spanCount) ? 0 : mDrawable.getIntrinsicHeight(), 0, 0);
                    }
                }
            } else {
                // 标签画底部分隔线
                outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
            }
        }
    }

    private boolean isFirstRow(RecyclerView parent, int position, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount);
            if (isHeader(parent, 0)) {
                spanIndex--;
            }
//            int spanIndex = position / spanCount + 1;
            final int headerPosition = findPinnedHeaderPosition(position);
            if (headerPosition >= 0 && (position - (headerPosition + 1)) % spanCount == 0) {
                // 找到头部位置减去包括头部位置之前的个数
                return true;
            } else if (spanIndex == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 适用于网格布局，用于判断是否是第一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @return
     */
    private boolean isFirstColumn(RecyclerView parent, int pos, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(pos, spanCount);
            final int headerPosition = findPinnedHeaderPosition(pos);
            if (headerPosition >= 0 && (pos - (headerPosition + 1)) % spanCount == 0) {
                // 找到头部位置减去包括头部位置之前的个数
                return true;
            } else if (spanIndex == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isHeader(RecyclerView parent, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (parent.getAdapter().getItemViewType(position) > maxItemType) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        // 检测到标签存在的时候，将标签强制固定在顶部
        createPinnedHeader(parent);

        if (mPinnedHeaderView != null && mFirstVisiblePosition >= mPinnedHeaderPosition) {

            mClipBounds = c.getClipBounds();
            // getTop拿到的是它的原点(它自身的padding值包含在内)相对parent的顶部距离，加上它的高度后就是它的底部所处的位置
            final int headEnd = mPinnedHeaderView.getTop() + mPinnedHeaderView.getHeight();
            // 根据坐标查找view，headEnd + 1找到的就是mPinnedHeaderView底部下面的view
            final View belowView = parent.findChildViewUnder(c.getWidth() / 2, headEnd + 1);
            if (isPinnedHeader(parent, belowView)) {
                // 如果是标签的话，缓存的标签就要同步跟此标签移动
                // 根据belowView相对顶部距离计算出缓存标签的位移
                mPinnedHeaderOffset = belowView.getTop() - (mRecyclerViewPaddingTop + mPinnedHeaderView.getHeight() + mHeaderTopMargin);
                // 锁定的矩形顶部为v.getTop(趋势是mPinnedHeaderView.getHeight()->0)
                mClipBounds.top = belowView.getTop();
            } else {
                mPinnedHeaderOffset = 0;
                mClipBounds.top = mRecyclerViewPaddingTop + mPinnedHeaderView.getHeight();
            }
            // 锁定画布绘制范围，记为A
            c.clipRect(mClipBounds);
        }
        drawDivider(c, parent);

    }

    // 画分隔线
    private void drawDivider(Canvas c, RecyclerView parent) {

        if (mAdapter == null) {
            // checkCache的话RecyclerView未设置之前mAdapter为空
            return;
        }

        // 不让分隔线画出界限
        c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getWidth() - parent.getPaddingRight(), parent.getHeight() - parent.getPaddingBottom());

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int childCount = parent.getChildCount();
            final int spanCount = getSpanCount(parent);
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                // 要考虑View的重用啊
                int realPosition = parent.getChildAdapterPosition(child);
                if (isPinnedHeaderType(mAdapter.getItemViewType(realPosition))) {
                    DividerHelper.drawBottomAlignItem(c, mDrawable, child, params);
                } else {
                    if (parent.getAdapter().getItemCount() > 1) {
                        if (isHeader(parent, realPosition)) {
//                            return;
                        } else if (isFirstColumn(parent, realPosition, spanCount)) {
//                        是否是第一列第一行
                            if (isFirstRow(parent, realPosition, spanCount)) {
                                DividerHelper.drawLeft(c, mDrawable, child, params);
                                DividerHelper.drawRight(c, mDrawable, child, params);
                            } else {
                                // 第一列要多画左边
                                DividerHelper.drawLeft(c, mDrawable, child, params);
                                DividerHelper.drawTop(c, mDrawable, child, params);
                                DividerHelper.drawRight(c, mDrawable, child, params);
                            }
                        } else if (isFirstRow(parent, realPosition, spanCount)) {
                            // 第一行要只画右边
                            DividerHelper.drawRight(c, mDrawable, child, params);
                        } else {
                            DividerHelper.drawTop(c, mDrawable, child, params);
                            DividerHelper.drawRight(c, mDrawable, child, params);
                        }
                    }
                }
            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int childCount = parent.getChildCount();
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    if (parent.getAdapter().getItemViewType(i) < maxItemType) {
                        DividerHelper.drawRightAlignItem(c, mDrawable, child, params);
                    }
                }
            } else {
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    if (parent.getAdapter().getItemViewType(i) < maxItemType) {
                        if (i + 1 != childCount || !isLastDraw) {
                            DividerHelper.drawBottomAlignItem(c, mDrawable, child, params);
                        }
                    }
                }
            }
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                if (isPinnedHeader(parent, child)) {
                    DividerHelper.drawBottomAlignItem(c, mDrawable, child, params);
                } else {
                    DividerHelper.drawLeft(c, mDrawable, child, params);
                    DividerHelper.drawBottom(c, mDrawable, child, params);
                    DividerHelper.drawRight(c, mDrawable, child, params);
                }
            }
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (mPinnedHeaderView != null && mFirstVisiblePosition >= mPinnedHeaderPosition) {
            c.save();

            mClipBounds.top = mRecyclerViewPaddingTop + mHeaderTopMargin;
            // 锁定画布绘制范围，记为B
            // REVERSE_DIFFERENCE，实际上就是求得的B和A的差集范围，即B－A，只有在此范围内的绘制内容才会被显示
            // 因此,只绘制(0,0,parent.getWidth(),belowView.getTop())这个范围，然后画布移动了mPinnedHeaderTop，所以刚好是绘制顶部标签移动的范围
            // 低版本不行，换回Region.Op.UNION并集
            c.clipRect(mClipBounds, Region.Op.UNION);
            c.translate(mRecyclerViewPaddingLeft + mHeaderLeftMargin, mPinnedHeaderOffset + mRecyclerViewPaddingTop + mHeaderTopMargin);
            mPinnedHeaderView.draw(c);

            c.restore();

        }
    }

    /**
     * 查找到view对应的位置从而判断出是否标签类型
     *
     * @param parent
     * @param view
     * @return
     */
    private boolean isPinnedHeader(RecyclerView parent, View view) {
        final int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return false;
        }
        final int type = mAdapter.getItemViewType(position);
        return isPinnedHeaderType(type);
    }

    /**
     * 创建标签强制固定在顶部
     *
     * @param parent
     */
    private void createPinnedHeader(final RecyclerView parent) {

        if (mAdapter == null) {
            // checkCache的话RecyclerView未设置之前mAdapter为空
            return;
        }

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        // 获取第一个可见的item位置
        mFirstVisiblePosition = findFirstVisiblePosition(layoutManager);

        // 获取标签的位置，
        int pinnedHeaderPosition = findPinnedHeaderPosition(mFirstVisiblePosition);
        if (pinnedHeaderPosition >= 0 && mPinnedHeaderPosition != pinnedHeaderPosition) {

            // 标签位置有效并且和缓存的位置不同
            mPinnedHeaderPosition = pinnedHeaderPosition;
            // 获取标签的type
            final int type = mAdapter.getItemViewType(mPinnedHeaderPosition);

            // 手动调用创建标签
            final RecyclerView.ViewHolder holder = mAdapter.createViewHolder(parent, type);
            mAdapter.bindViewHolder(holder, mPinnedHeaderPosition);
            // 缓存标签
            mPinnedHeaderView = holder.itemView;

            ViewGroup.LayoutParams lp = mPinnedHeaderView.getLayoutParams();
            if (lp == null) {
                // 标签默认宽度占满parent
                lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mPinnedHeaderView.setLayoutParams(lp);
            }

            // 对高度进行处理
            int heightMode = View.MeasureSpec.getMode(lp.height);
            int heightSize = View.MeasureSpec.getSize(lp.height);

            if (heightMode == View.MeasureSpec.UNSPECIFIED) {
                heightMode = View.MeasureSpec.EXACTLY;
            }

            mRecyclerViewPaddingLeft = parent.getPaddingLeft();
            int recyclerViewPaddingRight = parent.getPaddingRight();
            mRecyclerViewPaddingTop = parent.getPaddingTop();
            int recyclerViewPaddingBottom = parent.getPaddingBottom();

            if (lp instanceof ViewGroup.MarginLayoutParams) {
                final ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
                mHeaderLeftMargin = mlp.leftMargin;
                mHeaderTopMargin = mlp.topMargin;
                mHeaderRightMargin = mlp.rightMargin;
            }

            // 最大高度为RecyclerView的高度减去padding
            final int maxHeight = parent.getHeight() - mRecyclerViewPaddingTop - recyclerViewPaddingBottom;
            // 不能超过maxHeight
            heightSize = Math.min(heightSize, maxHeight);

            // 因为标签默认宽度占满parent，所以宽度强制为RecyclerView的宽度减去padding
            final int widthSpec = View.MeasureSpec
                    .makeMeasureSpec(parent.getWidth() - mRecyclerViewPaddingLeft - recyclerViewPaddingRight - mHeaderLeftMargin - mHeaderRightMargin,
                            View.MeasureSpec.EXACTLY);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            // 强制测量
            mPinnedHeaderView.measure(widthSpec, heightSpec);

            mLeft = mRecyclerViewPaddingLeft + mHeaderLeftMargin;
            mRight = mPinnedHeaderView.getMeasuredWidth() + mLeft;
            mTop = mRecyclerViewPaddingTop + mHeaderTopMargin;
            mBottom = mPinnedHeaderView.getMeasuredHeight() + mTop;

            // 位置强制布局在顶部
            mPinnedHeaderView.layout(mLeft, mTop, mRight, mBottom);

        }

    }

    /**
     * 从传入位置递减找出标签的位置
     *
     * @param formPosition
     * @return
     */
    private int findPinnedHeaderPosition(int formPosition) {

        for (int position = formPosition; position >= 0; position--) {
            // 位置递减，只要查到位置是标签，立即返回此位置
            final int type = mAdapter.getItemViewType(position);
            if (isPinnedHeaderType(type)) {
                return position;
            }
        }

        return -1;
    }

    /**
     * 通过适配器告知类型是否为标签
     *
     * @param type
     * @return
     */
    private boolean isPinnedHeaderType(int type) {
        return false;
    }

    /**
     * 找出第一个可见的Item的位置
     *
     * @param layoutManager
     * @return
     */
    private int findFirstVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        int firstVisiblePosition = 0;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisiblePosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(into);
            firstVisiblePosition = Integer.MAX_VALUE;
            for (int pos : into) {
                firstVisiblePosition = Math.min(pos, firstVisiblePosition);
            }
        }
        return firstVisiblePosition;
    }

    /**
     * 检查缓存
     *
     * @param parent
     */
    private void checkCache(final RecyclerView parent) {

        if (mParent != parent) {
            mParent = parent;
        }

        final RecyclerView.Adapter adapter = parent.getAdapter();
        if (mAdapter != adapter) {
            // 适配器为null或者不同，清空缓存
            mPinnedHeaderView = null;
            mPinnedHeaderPosition = -1;
            mAdapter = adapter;
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    reset();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    reset();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    super.onItemRangeChanged(positionStart, itemCount, payload);
                    reset();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    reset();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    reset();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    reset();
                }
            });
        }
    }

    private void reset() {
        mPinnedHeaderPosition = -1;
        mPinnedHeaderView = null;
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public static class Builder {

        private int dividerId;
        private boolean isLastDraw = true;
        private boolean isFirstDraw = true;

        /**
         * 构造方法
         */
        public Builder() {
        }

        /**
         * 设置分隔线资源ID
         *
         * @param dividerId 资源ID，若不设置这个并且enableDivider=true时，使用默认的分隔线
         * @return 构建者
         */
        public Builder setDividerId(int dividerId) {
            this.dividerId = dividerId;
            return this;
        }

        /**
         * 仅针对linear vertical
         *
         * @param isLastDraw
         * @return
         */
        public Builder setLastDraw(boolean isLastDraw) {
            this.isLastDraw = isLastDraw;
            return this;
        }


        /**
         * 仅针对grid
         * 第一行是否显示分割线
         *
         * @param isFirstDraw
         * @return
         */
        public Builder setFirstDraw(boolean isFirstDraw) {
            this.isFirstDraw = isFirstDraw;
            return this;
        }

        public NewGridItemDecoration create() {
            return new NewGridItemDecoration(this);
        }

    }


}
