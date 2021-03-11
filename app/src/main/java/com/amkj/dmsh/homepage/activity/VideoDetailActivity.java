package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dao.AddClickDao;
import com.amkj.dmsh.homepage.adapter.VideoDetailAdapter;
import com.amkj.dmsh.homepage.bean.VideoDetailEntity;
import com.amkj.dmsh.homepage.bean.VideoDetailEntity.VideoDetailBean;
import com.amkj.dmsh.homepage.view.JzvdStdTikTok;
import com.amkj.dmsh.homepage.view.OnViewPagerListener;
import com.amkj.dmsh.homepage.view.ViewPagerLayoutManager;
import com.amkj.dmsh.netloadpage.NetLoadCallback;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.xcpulltoloadmorelistview.XCPullToLoadMoreListView;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

import static com.amkj.dmsh.constant.ConstantMethod.saveSourceId;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

/**
 * Created by xiaoxin on 2021/2/24
 * Version:v5.0.0
 * ClassDescription :视频商品详情
 */
public class VideoDetailActivity extends BaseActivity {


    @BindView(R.id.xc_load)
    XCPullToLoadMoreListView mXcLoad;
    @BindView(R.id.content_frame)
    FrameLayout mContentFrame;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.ll_menu)
    LinearLayout mLlMenu;
    @BindView(R.id.rl_top)
    RelativeLayout mRlTop;
    private String mId;
    private VideoDetailEntity mVideoDetailEntity;
    private List<VideoDetailBean> mVideoList = new ArrayList<>();
    private int mCurrentPosition;
    private VideoDetailAdapter mVideoDetailAdapter;
    private List<String> mIdList = new ArrayList<>();
    private int pageNum = 9;//每一页的数量（奇数方便计算偏移量）
    private VideoDetailEntity mCurrentDetailEntity;
    private RecyclerView mRvTiktok;
    private String mSortType = "1";//默认热门排序

    @Override
    protected int getContentView() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void setStatusBar() {
        loadService.setCallBack(NetLoadCallback.class, (context, view) -> {
            ViewGroup viewGroup = view.findViewById(R.id.fl_net_loading);
            viewGroup.setBackgroundColor(getResources().getColor(R.color.black));
        });
        ImmersionBar.with(this).statusBarColor(R.color.transparent).keyboardEnable(true).navigationBarEnable(false).statusBarDarkFont(false).fullScreen(true).init();
    }

    @Override
    protected void initViews() {
        String sortType = getIntent().getStringExtra("sortType");
        if (!TextUtils.isEmpty(sortType)) {
            mSortType = sortType;
        }
        mId = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(mId)) {
            showToast("数据有误，请重试");
            finish();
        }
        saveSourceId(getSimpleName(), mId);
        //初始化抽屉菜单
        View mMenu = mDrawerLayout.getChildAt(1);
        ViewGroup.LayoutParams params = mMenu.getLayoutParams();
        params.width = TinkerBaseApplicationLike.getInstance().getScreenWidth() * 3 / 4;
        mMenu.setLayoutParams(params);
        mDrawerLayout.setScrimColor(Color.parseColor("#33000000"));
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //抽屉打开时暂停播放视频
                videoPause();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                //抽屉关闭时恢复播放视频
                videoPlay();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //初始化视频列表
        mRvTiktok = mXcLoad.getListView();
        mVideoDetailAdapter = new VideoDetailAdapter(this, mVideoList);
        mVideoDetailAdapter.setPreLoadNumber(1);
        mVideoDetailAdapter.setEnableLoadMore(true);
        ViewPagerLayoutManager viewPagerLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mRvTiktok.setLayoutManager(viewPagerLayoutManager);
        mRvTiktok.setAdapter(mVideoDetailAdapter);
        //向上翻页
        mXcLoad.setOnRefreshListener(new XCPullToLoadMoreListView.OnRefreshListener() {
            @Override
            public void onPullDownLoadMore() {
                if (mIdList.size() > 0 && mVideoList.size() > 0) {
                    for (int i = 0; i < mIdList.size(); i++) {
                        if (mIdList.get(i).equals(mVideoList.get(0).getId())) {
                            List<String> idList = mIdList.subList(Math.max(i - pageNum, 0), i);
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int j = 0; j < idList.size(); j++) {
                                stringBuilder.append(idList.get(j)).append((j == idList.size() - 1) ? "" : ",");
                            }
                            mId = stringBuilder.toString();
                            if (!TextUtils.isEmpty(mId)) {
                                getPreLoadLodata(mId, true, false);
                            } else {      //已无更多数据
                                new LifecycleHandler(getActivity()).postDelayed(() -> mXcLoad.onRefreshComplete(true), 1000);
                            }
                            break;
                        }
                    }
                } else {
                    mXcLoad.onRefreshComplete(true);
                }
            }
        });
        //向下翻页
        mVideoDetailAdapter.setOnLoadMoreListener(() -> {
            if (mIdList.size() > 0 && mVideoList.size() > 0) {
                for (int i = 0; i < mIdList.size(); i++) {
                    if (mIdList.get(i).equals(mVideoList.get(mVideoList.size() - 1).getId())) {
                        List<String> idList = mIdList.subList(i + 1, Math.min(i + 1 + pageNum, mIdList.size()));
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < idList.size(); j++) {
                            stringBuilder.append(idList.get(j)).append((j == idList.size() - 1) ? "" : ",");
                        }
                        mId = stringBuilder.toString();
                        if (!TextUtils.isEmpty(mId)) {
                            getPreLoadLodata(mId, false, false);
                        } else {     //已无更多数据
                            mVideoDetailAdapter.loadMoreEnd();
                        }
                        break;
                    }
                }
            } else {
                mVideoDetailAdapter.loadMoreEnd();
            }
        }, mRvTiktok);
        viewPagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                //自动播放第一条
                if (mCurrentPosition == 0) {
                    autoPlayVideo(0);
                }
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (mCurrentPosition == position) {
                    Jzvd.releaseAllVideos();
                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                //切换视频自动播放
                if (mCurrentPosition == position) {
                    return;
                }
                autoPlayVideo(position);
                mCurrentPosition = position;
                //判断是否显示'大家都在买'按钮
                if (mVideoList.size() > position) {
                    setTop(mVideoList.get(position).isShowProduct());
                }
            }
        });
    }

    private void videoPause() {
        if (mRvTiktok != null) {
            JzvdStdTikTok jzvdStdTikTok = (JzvdStdTikTok) mVideoDetailAdapter.getViewByPosition(mRvTiktok, mCurrentPosition, R.id.videoplayer);
            if (jzvdStdTikTok != null) {
                jzvdStdTikTok.mediaInterface.pause();
                jzvdStdTikTok.onStatePause();
            }
        }
    }

    private void videoPlay() {
        if (mRvTiktok != null) {
            JzvdStdTikTok jzvdStdTikTok = (JzvdStdTikTok) mVideoDetailAdapter.getViewByPosition(mRvTiktok, mCurrentPosition, R.id.videoplayer);
            if (jzvdStdTikTok != null) {
                jzvdStdTikTok.mediaInterface.start();
                jzvdStdTikTok.onStatePlaying();
            }
        }
    }

    private void autoPlayVideo(int position) {
        if (mRvTiktok != null) {
            JzvdStdTikTok jzvdStdTikTok = (JzvdStdTikTok) mVideoDetailAdapter.getViewByPosition(mRvTiktok, position, R.id.videoplayer);
            if (jzvdStdTikTok != null) {
                jzvdStdTikTok.startVideoAfterPreloading();
            }
            //统计视频播放量
            AddClickDao.addVideoClick(getActivity(), mVideoList.size() >= position + 1 ? mVideoList.get(position).getId() : "0");
        }
    }


    @Override
    protected void loadData() {

        //获取第一页数据
        Map<String, String> map = new HashMap<>();
        map.put("ids", mId);
        map.put("sortType", mSortType);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_VIDEO_DETAIl, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mCurrentDetailEntity = GsonUtils.fromJson(result, VideoDetailEntity.class);
                if (mCurrentDetailEntity != null) {
                    String code = mCurrentDetailEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        List<String> idList = mCurrentDetailEntity.getIdList();
                        if (idList != null) {
                            mIdList.clear();
                            mIdList.addAll(idList);
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < idList.size(); i++) {
                                if (mId.equals(idList.get(i))) {
                                    List<String> ids = mIdList.subList(i, Math.min(i + pageNum, mIdList.size()));
                                    for (int j = 0; j < ids.size(); j++) {
                                        stringBuilder.append(ids.get(j)).append((j == ids.size() - 1) ? "" : ",");
                                    }
                                    getPreLoadLodata(stringBuilder.toString(), false, true);
                                    break;
                                }
                            }
                        }
                    }
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCurrentDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mCurrentDetailEntity);
            }
        });
    }

    //获取预加载数据
    private void getPreLoadLodata(String id, boolean isRefresh, boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("ids", id);
        map.put("sortType", mSortType);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_VIDEO_DETAIl, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (isFirst) {//第一次请求的数据要清除否则会数据重复
                    mVideoList.clear();
                }
                mVideoDetailEntity = GsonUtils.fromJson(result, VideoDetailEntity.class);
                if (mVideoDetailEntity != null) {
                    String code = mVideoDetailEntity.getCode();
                    if (SUCCESS_CODE.equals(code)) {
                        List<String> idList = mVideoDetailEntity.getIdList();
                        if (idList != null) {
                            mIdList.clear();
                            mIdList.addAll(idList);
                        }
                        List<VideoDetailBean> datas = mVideoDetailEntity.getResult();
                        if (datas != null) {
                            if (isRefresh) {
                                mVideoList.addAll(0, datas);
                            } else {
                                mVideoList.addAll(datas);
                            }
                            mVideoDetailAdapter.notifyDataSetChanged();
                            if (datas.size() >= pageNum) {
                                mVideoDetailAdapter.loadMoreComplete();
                            } else {
                                mVideoDetailAdapter.loadMoreEnd();
                            }
                        }
                    } else {
                        mVideoDetailAdapter.notifyDataSetChanged();
                        mVideoDetailAdapter.loadMoreEnd();
                        if (!EMPTY_CODE.equals(code)) showToast(mVideoDetailEntity.getMsg());
                    }
                }

                if (isRefresh) {
                    mXcLoad.onRefreshComplete(false);
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVideoList, mVideoDetailEntity);
            }


            @Override
            public void onNotNetOrException() {
                if (isRefresh) {
                    mXcLoad.onRefreshComplete(false);
                }
                mVideoDetailAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mVideoList, mVideoDetailEntity);
            }
        });
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mId = intent.getStringExtra("id");
        loadData();
    }

    @OnClick({R.id.ll_hot, R.id.iv_close, R.id.iv_close_draw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.ll_hot:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.iv_close_draw:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void setTop(boolean showProduct) {
        mRlTop.setVisibility(showProduct ? View.VISIBLE : View.GONE);
    }
}
