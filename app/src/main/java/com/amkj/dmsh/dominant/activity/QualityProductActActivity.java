package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.CountDownUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getCarCount;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.BASE_SHARE_PAGE_TWO;
import static com.amkj.dmsh.constant.Url.Q_ACT_PRO_LIST;
import static com.amkj.dmsh.utils.CountDownUtils.isTimeStart;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/17
 * class description:活动商品专场
 */
public class QualityProductActActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private Badge badge;
    private String activityCode;
    private List<LikedProductBean> actProActivityList = new ArrayList<>();
    private GoodProductAdapter qualityTypeProductAdapter;
    private QActivityProView qActivityProView;
    private UserLikedProductEntity likedProductEntity;
    private ConstantMethod constantMethod;
    private long addSecond;

    @Override
    protected int getContentView() {
        return R.layout.activity_pro_acitvity;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        activityCode = intent.getStringExtra("activityCode");
        if (TextUtils.isEmpty(activityCode)) {
            showToast(this, R.string.invalidData);
            finish();
        }

        iv_img_share.setVisibility(View.VISIBLE);
        tv_header_titleAll.setText("");
        tl_quality_bar.setSelected(true);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityProductActActivity.this, 2));

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        qualityTypeProductAdapter = new GoodProductAdapter(QualityProductActActivity.this, actProActivityList);
        View headerView = LayoutInflater.from(QualityProductActActivity.this).inflate(R.layout.layout_product_activity_detail, null, false);
        qActivityProView = new QActivityProView();
        ButterKnife.bind(qActivityProView, headerView);
        qActivityProView.init();
        qualityTypeProductAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getActProActivityData();
            }
        }, communal_recycler);
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show();
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
        badge = getBadge(QualityProductActActivity.this, fl_header_service);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getCarCount(getActivity());
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getActProActivityData();
        getCarCount(getActivity());
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getActProActivityData() {
        final Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("activityCode", activityCode);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityProductActActivity.this, Q_ACT_PRO_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        if (page == 1) {
                            actProActivityList.clear();
                        }
                        Gson gson = new Gson();
                        likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            List<LikedProductBean> goodsList = likedProductEntity.getGoodsList();
                            if (likedProductEntity.getCode().equals(SUCCESS_CODE) && goodsList != null && goodsList.size() > 0) {
                                setActProInfo(likedProductEntity);
                                actProActivityList.addAll(likedProductEntity.getGoodsList());
                                qualityTypeProductAdapter.loadMoreComplete();
                            } else if (likedProductEntity.getCode().equals(ERROR_CODE)) {
                                showToast(QualityProductActActivity.this, likedProductEntity.getMsg());
                            } else {
                                qualityTypeProductAdapter.loadMoreEnd();
                            }
                        } else {
                            qualityTypeProductAdapter.loadMoreEnd();
                        }
                        qualityTypeProductAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, actProActivityList, likedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, actProActivityList, likedProductEntity);
                    }
                });
    }

    private void setActProInfo(UserLikedProductEntity likedProductEntity) {
        qActivityProView.mIvNextIcon.setVisibility(GONE);
        qActivityProView.mTvProductActivityDescription.setText(likedProductEntity.getActivityRule());
        startCountDownTime(qActivityProView.mTvCountTimeBeforeWhite, qActivityProView.mCvCountdownTimeWhiteHours, likedProductEntity);
        tv_header_titleAll.setText(getStrings(likedProductEntity.getTitle()));
    }

    //开启倒计时
    private void startCountDownTime(TextView tipView, CountdownView countDownTimeView, UserLikedProductEntity likedProductEntity) {
        try {
            String activityStartTime = likedProductEntity.getActivityStartTime();
            String activityEndTime = likedProductEntity.getActivityEndTime();
            String currentTime = likedProductEntity.getCurrentTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long dateStart = formatter.parse(activityStartTime).getTime();
            long dateEnd = formatter.parse(activityEndTime).getTime();
            long dateCurret = !TextUtils.isEmpty(currentTime) ? formatter.parse(currentTime).getTime() : System.currentTimeMillis();
            //活动未开始
            if (!isTimeStart(likedProductEntity.getActivityStartTime(), likedProductEntity.getCurrentTime())) {
                tipView.setText(tipView.getId() == R.id.tv_count_time_before_white ? "距开始还有" : "距开始");
                CountDownTimer countDownTimer = new CountDownTimer(this, dateStart + 1 - dateCurret, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        countDownTimeView.updateShow(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        cancel();
                        //活动已开始
                        countDownTimeView.updateShow(0);
                        loadData();
                    }
                };

                countDownTimer.start();
            } else if (!CountDownUtils.isTimeEnd(activityEndTime, currentTime)) {
                //活动已开始未结束
                tipView.setText(tipView.getId() == R.id.tv_count_time_before_white ? "距结束还有" : "距结束");
                CountDownTimer countDownTimer = new CountDownTimer(this, dateEnd + 1 - dateCurret, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        countDownTimeView.updateShow(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        cancel();
                        //活动已结束
                        countDownTimeView.updateShow(0);
                        loadData();
                    }
                };

                countDownTimer.start();
            } else {
                //活动已结束
                qActivityProView.mRlProductActivityDetail.setVisibility(GONE);
            }
        } catch (Exception e) {
            //活动已结束
            qActivityProView.mRlProductActivityDetail.setVisibility(GONE);
        }
    }


    class QActivityProView {
        @BindView(R.id.tv_product_activity_description)
        TextView mTvProductActivityDescription;
        @BindView(R.id.iv_next_icon)
        ImageView mIvNextIcon;
        @BindView(R.id.rl_product_activity_detail)
        LinearLayout mRlProductActivityDetail;
        @BindView(R.id.tv_count_time_before_white)
        TextView mTvCountTimeBeforeWhite;
        @BindView(R.id.cv_countdownTime_white_hours)
        CountdownView mCvCountdownTimeWhiteHours;
        @BindView(R.id.ll_communal_time_hours)
        LinearLayout mLlCommunalTimeHours;

        void init() {
            //设置上面活动倒计时
            DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
            dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
            dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
            dynamic.setSuffixGravity(Gravity.CENTER);
            DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
            backgroundInfo.setColor(getResources().getColor(R.color.text_normal_red))
                    .setBorderRadius((float) AutoSizeUtils.mm2px(mAppContext, 5))
                    .setBorderColor(getResources().getColor(R.color.text_normal_red))
                    .setShowTimeBgBorder(true);
            dynamic.setBackgroundInfo(backgroundInfo);
            mCvCountdownTimeWhiteHours.dynamicShow(dynamic.build());
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar(View view) {
        Intent intent = new Intent(QualityProductActActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_img_share)
    void setShare() {
        if (likedProductEntity != null && likedProductEntity.getGoodsList() != null
                && likedProductEntity.getGoodsList().size() > 0) {
            LikedProductBean likedProductBean = likedProductEntity.getGoodsList().get(0);
            new UMShareAction(QualityProductActActivity.this
                    , likedProductBean.getPicUrl()
                    , getStrings(likedProductEntity.getTitle())
                    , getStrings(likedProductEntity.getActivityDesc())
                    , BASE_SHARE_PAGE_TWO + "m/template/common/activitySpecial.html?id=" + likedProductBean.getActivityCode()
                    , "pages/activitySpecial/activitySpecial?id=" + likedProductBean.getActivityCode(), likedProductBean.getId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", activityCode);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", activityCode);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }


    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge != null) {
                badge.setBadgeNumber((int) message.result);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (constantMethod != null) {
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
        super.onDestroy();
    }
}
