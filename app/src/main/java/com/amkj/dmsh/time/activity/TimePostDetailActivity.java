package com.amkj.dmsh.time.activity;

import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.time.adapter.TimePostDetailAdapter;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;

/**
 * Created by xiaoxin on 2020/9/30
 * Version:v4.8.0
 * ClassDescription :团购商品种草帖子
 */
public class TimePostDetailActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.rv_time_post)
    RecyclerView mRvTimePost;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton mDownloadBtnCommunal;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout mSmartLayout;
    List<PostBean> mPostDetailBeanList = new ArrayList<>();
    private TimePostDetailAdapter mTimePostDetailAdapter;
    private PostEntity mDetailEntity;
    private String mId;
    private int page = 1;

    public void setStatusBar() {
        //状态栏白底黑字
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).keyboardEnable(false).navigationBarEnable(false).statusBarDarkFont(true).fitsSystemWindows(true).init();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_time_post_detail;
    }

    @Override
    protected void initViews() {
        if (getIntent().getExtras() != null) {
            mId = getIntent().getStringExtra("id");
        } else {
            showToast("数据有误，请重试");
            finish();
        }

        mTvHeaderTitle.setText("好物种草");
        mTvHeaderShared.setVisibility(View.GONE);
        //初始化帖子列表
        mRvTimePost.setLayoutManager(new LinearLayoutManager(this));
        mTimePostDetailAdapter = new TimePostDetailAdapter(this, mPostDetailBeanList);
        mRvTimePost.setAdapter(mTimePostDetailAdapter);
        mTimePostDetailAdapter.setOnLoadMoreListener(() -> {
            page++;
            loadData();
        }, mRvTimePost);
        mSmartLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            loadData();
        });
    }

    @Override
    protected void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("docId", mId);
        map.put("currentPage", page);
        map.put("showCount", TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_DOCUMENT_DETAILLIST_PAGE, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartLayout.finishRefresh();
                mDetailEntity = GsonUtils.fromJson(result, PostEntity.class);
                if (page == 1) {
                    mPostDetailBeanList.clear();
                }
                String code = mDetailEntity.getCode();
                if (SUCCESS_CODE.equals(code)) {
                    List<PostBean> detailBeans = mDetailEntity.getPostList();
                    if (detailBeans != null) {
                        mPostDetailBeanList.addAll(detailBeans);
                        mTimePostDetailAdapter.notifyDataSetChanged();
                        if (detailBeans.size() >= TOTAL_COUNT_TEN) {
                            mTimePostDetailAdapter.loadMoreComplete();
                        } else {
                            mTimePostDetailAdapter.loadMoreEnd();
                        }
                    }
                } else {
                    mTimePostDetailAdapter.notifyDataSetChanged();
                    mTimePostDetailAdapter.loadMoreEnd();
                    if (!EMPTY_CODE.equals(code)) showToast(mDetailEntity.getMsg());
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostDetailBeanList, mDetailEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mPostDetailBeanList, mDetailEntity);
            }
        });
    }


    @OnClick(R.id.tv_life_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartLayout;
    }
}
