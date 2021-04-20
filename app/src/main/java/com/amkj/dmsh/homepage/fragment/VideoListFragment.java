package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.activity.VideoDetailActivity;
import com.amkj.dmsh.homepage.adapter.VideoProductAdapter;
import com.amkj.dmsh.homepage.bean.VideoDetailEntity.VideoDetailBean;
import com.amkj.dmsh.homepage.bean.VideoProductEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.StaggeredDividerItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;

/**
 * Created by xiaoxin on 2021/2/23
 * Version:v5.0.0
 * ClassDescription :视频商品列表
 */
public class VideoListFragment extends BaseFragment {
    @BindView(R.id.communal_recycler)
    RecyclerView mCommunalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mDownloadBtnCommunal;
    private List<VideoDetailBean> videoList = new ArrayList<>();
    private VideoProductAdapter mVideoProductAdapter;
    private VideoProductEntity mVideoProductEntity;
    private int mPage = 1;
    private String mSortType;
    private boolean mIsCollect;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        mCommunalRecycler.setBackgroundColor(getResources().getColor(R.color.light_gray_f));
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mCommunalRecycler.setLayoutManager(layoutManager);
        mCommunalRecycler.setItemAnimator(null);
        mCommunalRecycler.addItemDecoration(new StaggeredDividerItemDecoration(AutoSizeUtils.mm2px(mAppContext, 5), false));
        mCommunalRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[layoutManager.getSpanCount()];
                layoutManager.findFirstCompletelyVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });
        mVideoProductAdapter = new VideoProductAdapter(getActivity(), videoList);
        mVideoProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoDetailBean productBean = (VideoDetailBean) view.getTag();
                if (productBean != null) {
                    //如果有抽屉，先关闭抽屉再跳转
                    if (getActivity() instanceof VideoDetailActivity) {
                        ((VideoDetailActivity) getActivity()).closeDrawer();
                    }
                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                    intent.putExtra("id", String.valueOf(productBean.getId()));
                    intent.putExtra("sortType", mSortType);
                    startActivity(intent);
                }

            }
        });
        mCommunalRecycler.setAdapter(mVideoProductAdapter);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> {
            mPage = 1;
            loadData();
        });

        mVideoProductAdapter.setOnLoadMoreListener(() -> {
            mPage++;
            loadData();
        }, mCommunalRecycler);
        setFloatingButton(mDownloadBtnCommunal, mCommunalRecycler);
    }

    @Override
    protected void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", mPage);
        map.put("showCount", TOTAL_COUNT_TEN);
        map.put("sortType", TextUtils.isEmpty(mSortType) ? "1" : mSortType);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), mIsCollect ? Url.GTE_VIDEO_COLLECT : Url.GET_VIDEO_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                if (mPage == 1) {
                    videoList.clear();
                }
                int positionStart = videoList.size();
                mVideoProductEntity = GsonUtils.fromJson(result, VideoProductEntity.class);
                if (mVideoProductEntity != null) {
                    String code = mVideoProductEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        List<VideoDetailBean> videoProductList = mVideoProductEntity.getResult();
                        if (videoProductList != null) {
                            videoList.addAll(videoProductList);
                            mVideoProductAdapter.notifyDataSetChanged();
                            if (videoProductList.size() >= TOTAL_COUNT_TEN) {
                                mVideoProductAdapter.loadMoreComplete();
                            } else {
                                mVideoProductAdapter.loadMoreEnd();
                            }
                        }
                    } else {
                        mVideoProductAdapter.notifyDataSetChanged();
                        mVideoProductAdapter.loadMoreEnd();
                        if (!EMPTY_CODE.equals(code)) showToast(mVideoProductEntity.getMsg());
                    }
                }

                if (mPage == 1) {
                    mVideoProductAdapter.notifyItemRangeChanged(0, videoList.size());
                } else {
                    mVideoProductAdapter.notifyItemRangeInserted(positionStart, videoList.size());
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, videoList, mVideoProductEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                mVideoProductAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, videoList, mVideoProductEntity);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        super.getReqParams(bundle);
        if (bundle != null) {
            mSortType = bundle.getString("sortType");
            mIsCollect = bundle.getBoolean("isCollect");
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected boolean isLazy() {
        return !TextUtils.isEmpty(mSortType);
    }
}
