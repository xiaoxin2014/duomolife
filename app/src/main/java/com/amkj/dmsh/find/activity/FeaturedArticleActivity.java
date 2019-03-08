package com.amkj.dmsh.find.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.find.adapter.FeaturedArticleAdapter;
import com.amkj.dmsh.find.bean.FeaturedArticleEntity.FeaturedArticleBean;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getDeviceAppNotificationStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.REQUEST_NOTIFICATION_STATUS;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2019/3/7
 * version 3.3.0
 * class description:精选文章
 */
public class FeaturedArticleActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.communal_recycler)
    RecyclerView communalRecycler;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smartCommunalRefresh;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton downloadBtnCommunal;
    private int screenHeight;
    private List<FeaturedArticleBean> featuredArticleList = new ArrayList();
    private FeaturedArticleAdapter featuredArticleAdapter;
    private int scrollY;
    private int page = 1;
    private FeaturedArticleHelper featuredArticleHelper;
    private AlertDialogHelper notificationAlertDialogHelper;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_floating_header;
    }

    @Override
    protected void initViews() {
        tvHeaderTitle.setText("");
        communalRecycler.setLayoutManager(new LinearLayoutManager(FeaturedArticleActivity.this));
        smartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
        FeaturedArticleBean featuredArticleBean;
        for (int i = 0; i < 10; i++) {
            featuredArticleBean = new FeaturedArticleBean();
            featuredArticleBean.setCommentCount(i);
            featuredArticleBean.setLikeCount(i);
            featuredArticleBean.setContent("内容");
            featuredArticleBean.setTime("2019-03-08");
            featuredArticleBean.setTitle("标题");
            featuredArticleBean.setPicUrl("http://image.domolife.cn/2016-04-26_571f105476475.jpg");
            featuredArticleList.add(featuredArticleBean);
        }
        featuredArticleAdapter = new FeaturedArticleAdapter(FeaturedArticleActivity.this, featuredArticleList);
        View featuredArticleView = LayoutInflater.from(this).inflate(R.layout.layout_featured_article_header, communalRecycler, false);
        featuredArticleHelper = new FeaturedArticleHelper();
        ButterKnife.bind(featuredArticleHelper, featuredArticleView);
        featuredArticleAdapter.addHeaderView(featuredArticleView);
        communalRecycler.setAdapter(featuredArticleAdapter);
        communalRecycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_white).create());
        featuredArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        featuredArticleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FeaturedArticleBean featuredArticleBean = (FeaturedArticleBean) view.getTag();
                if(featuredArticleBean==null){
                    return;
                }
                switch (view.getId()) {
                    case R.id.tv_featured_article_count_like:
                        if (userId > 0) {
                            int likeNum = featuredArticleBean.getLikeCount();
                            TextView textView = (TextView) view;
                            textView.setSelected(!textView.isSelected());
                            featuredArticleBean.setLikeCount(textView.isSelected()
                                    ? likeNum + 1 : likeNum - 1 > 0 ? likeNum - 1 : 0);
                            textView.setText(String.valueOf(featuredArticleBean.getLikeCount()));
                        } else {
                            getLoginStatus(FeaturedArticleActivity.this);
                        }
                        break;
                    case R.id.tv_featured_article_count_comment:
                        Intent intent = new Intent(FeaturedArticleActivity.this, FeaturedArticleCommentActivity.class);
                        intent.putExtra("feaId", "");
                        startActivity(intent);
                        break;
                }
            }
        });
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communalRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (downloadBtnCommunal.getVisibility() == GONE) {
                        downloadBtnCommunal.setVisibility(VISIBLE);
                        downloadBtnCommunal.hide(false);
                    }
                    if (!downloadBtnCommunal.isVisible()) {
                        downloadBtnCommunal.show();
                    }
                } else {
                    if (downloadBtnCommunal.isVisible()) {
                        downloadBtnCommunal.hide();
                    }
                }
            }
        });
        downloadBtnCommunal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communalRecycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communalRecycler.scrollToPosition(mVisibleCount);
                }
                communalRecycler.smoothScrollToPosition(0);
            }
        });
        featuredArticleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getFeaturedArticleData();
            }
        }, communalRecycler);
    }

    @Override
    protected void loadData() {
        page = 1;
        getFeaturedArticleData();
    }

    /**
     * 获取精选文章数据
     */
    private void getFeaturedArticleData() {
        smartCommunalRefresh.finishRefresh();
    }

    public class FeaturedArticleHelper {
        @BindView(R.id.tv_featured_article_header_title)
        TextView tvFeaturedArticleTitle;
        @BindView(R.id.tv_first_featured_article_subscribe_hint)
        TextView tvFirstFeaturedArticleSubscribeHint;
        @BindView(R.id.tv_featured_article_header_description)
        TextView tvFeaturedArticleDescription;

        @OnClick(R.id.tv_featured_article_header_subscribe)
        void subscribeArticle(TextView textView) {
            if (userId > 0) {
                checkNotificationStatus(textView);
            } else {
                getLoginStatus(FeaturedArticleActivity.this);
            }
        }
    }

    /**
     * 检查通知状态
     *
     * @param textView
     */
    private void checkNotificationStatus(TextView textView) {
        boolean subscriptionStatus = textView.isSelected();
//        是否订阅
        if (!subscriptionStatus && !getDeviceAppNotificationStatus(FeaturedArticleActivity.this)) {
            if (notificationAlertDialogHelper == null) {
                notificationAlertDialogHelper = new AlertDialogHelper(FeaturedArticleActivity.this);
                notificationAlertDialogHelper.setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                    @Override
                    public void confirm() {
                        // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_NOTIFICATION_STATUS);
                        notificationAlertDialogHelper.dismiss();
                    }

                    @Override
                    public void cancel() {
                        notificationAlertDialogHelper.dismiss();
                    }
                });
            }
            notificationAlertDialogHelper.setTitle("是否打开推送通知")
                    .setTitleGravity(Gravity.CENTER)
                    .setMsg("打开推送通知\n活动开始前会立即通知你哦~")
                    .setMsgTextGravity(Gravity.CENTER)
                    .setConfirmText("好的")
                    .setCancelText("不需要")
                    .setCancelTextColor(getResources().getColor(R.color.text_gray_hint_n));
            notificationAlertDialogHelper.show();
            return;
        }
        changeSubscribeStatus();
        subscriptionStatus = !subscriptionStatus;
        textView.setText(subscriptionStatus?"已订阅":"订阅");
        textView.setSelected(subscriptionStatus);
    }

    /**
     * 更改精选文章订阅状态
     */
    private void changeSubscribeStatus() {

    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick({R.id.tv_header_shared})
    void setFeaturedArticleShare() {}
}
