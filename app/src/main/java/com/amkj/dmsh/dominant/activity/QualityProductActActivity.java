package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
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
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.Q_ACT_PRO_LIST;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifferenceText;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/17
 * class description:??????????????????
 */
public class QualityProductActActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    ???????????????
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
    private Badge badge;
    private String activityCode;
    private List<LikedProductBean> actProActivityList = new ArrayList<>();
    private GoodProductAdapter qualityTypeProductAdapter;
    private QActivityProView qActivityProView;
    private UserLikedProductEntity likedProductEntity;
    private CountDownTimer mCountDownStartTimer;
    private CountDownTimer mCountDownEndTimer;

    @Override
    protected int getContentView() {
        return R.layout.activity_pro_acitvity;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        activityCode = intent.getStringExtra("activityCode");
        if (TextUtils.isEmpty(activityCode)) {
            showToast(R.string.invalidData);
            finish();
        }

        iv_img_share.setVisibility(VISIBLE);
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
                // ?????????????????????ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());
        qualityTypeProductAdapter.setOnLoadMoreListener(() -> {
            page++;
            getActProActivityData();
        }, communal_recycler);
        setFloatingButton(download_btn_communal, communal_recycler);
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
                        likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        String code = likedProductEntity.getCode();
                        if (page == 1) {
                            actProActivityList.clear();
                        }
                        if (SUCCESS_CODE.equals(code)) {
                            List<LikedProductBean> goodsList = likedProductEntity.getGoodsList();
                            if (goodsList != null) {
                                actProActivityList.addAll(goodsList);
                                qualityTypeProductAdapter.notifyDataSetChanged();
                                if (goodsList.size() >= TOTAL_COUNT_TWENTY) {
                                    qualityTypeProductAdapter.loadMoreComplete();
                                } else {
                                    qualityTypeProductAdapter.loadMoreEnd();
                                }
                            }
                            setActProInfo(likedProductEntity);
                        } else {
                            qualityTypeProductAdapter.notifyDataSetChanged();
                            qualityTypeProductAdapter.loadMoreEnd();
                            if (!EMPTY_CODE.equals(code)) showToast(likedProductEntity.getMsg());
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, actProActivityList, likedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreFail();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, actProActivityList, likedProductEntity);
                    }
                });
    }

    private void setActProInfo(UserLikedProductEntity likedProductEntity) {
        qActivityProView.mIvNextIcon.setVisibility(GONE);
        qActivityProView.mTvProductActivityDescription.setText(getStrings(likedProductEntity.getActivityRule()));
        startActivityDownTime(likedProductEntity);
        tv_header_titleAll.setText(getStrings(likedProductEntity.getTitle()));
    }


    //?????????????????????
    private void startActivityDownTime(UserLikedProductEntity shopPropertyBean) {
        try {
            qActivityProView.mLlProductActivityDetail.setVisibility(VISIBLE);
            String activityStartTime = shopPropertyBean.getActivityStartTime();
            String activityEndTime = shopPropertyBean.getActivityEndTime();
            String currentTime = shopPropertyBean.getCurrentTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            long dateStart = formatter.parse(activityStartTime).getTime();
            long dateEnd = formatter.parse(activityEndTime).getTime();
            long dateCurret = !TextUtils.isEmpty(currentTime) ? formatter.parse(currentTime).getTime() : System.currentTimeMillis();
            //???????????????
            if (!isEndOrStartTime(shopPropertyBean.getCurrentTime(), shopPropertyBean.getActivityStartTime())) {
                qActivityProView.mLlTopEndTime.setVisibility(GONE);
                qActivityProView.mLlTopStartTime.setVisibility(VISIBLE);
                long millisInFuture = dateStart - dateCurret;
                if (mCountDownStartTimer == null) {
                    mCountDownStartTimer = new CountDownTimer(this) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            qActivityProView.mTvStartTime.setText(String.format("?????????%s", getTimeDifferenceText(millisUntilFinished)));
                        }

                        @Override
                        public void onFinish() {
                            //???????????????
                            qActivityProView.mTvStartTime.setText(String.format("?????????%s", getTimeDifferenceText(0)));
                            loadData();
                        }
                    };
                }

                mCountDownStartTimer.setMillisInFuture(millisInFuture);
                mCountDownStartTimer.start();
            } else if (!isEndOrStartTime(currentTime, activityEndTime)) {
                qActivityProView.mLlTopEndTime.setVisibility(VISIBLE);
                qActivityProView.mLlTopStartTime.setVisibility(GONE);
                //????????????????????????
                long millisInFuture = dateEnd + 1 - dateCurret;
                if (mCountDownEndTimer == null) {
                    mCountDownEndTimer = new CountDownTimer(this) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            qActivityProView.mCvCountdownTimeWhiteHours.updateShow(millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                            //???????????????
                            qActivityProView.mCvCountdownTimeWhiteHours.updateShow(0);
                            loadData();
                        }
                    };
                }
                mCountDownEndTimer.setMillisInFuture(millisInFuture);
                mCountDownEndTimer.start();
            } else {
                //???????????????
                qActivityProView.mLlProductActivityDetail.setVisibility(GONE);
            }
        } catch (Exception e) {
            qActivityProView.mLlProductActivityDetail.setVisibility(GONE);
        }
    }


    class QActivityProView {
        @BindView(R.id.tv_product_activity_description)
        TextView mTvProductActivityDescription;
        @BindView(R.id.iv_next_icon)
        ImageView mIvNextIcon;
        @BindView(R.id.rl_product_activity_description)
        LinearLayout mRlProductActivityDescription;
        @BindView(R.id.cv_countdownTime_white_hours)
        CountdownView mCvCountdownTimeWhiteHours;
        @BindView(R.id.ll_top_end_time)
        LinearLayout mLlTopEndTime;
        @BindView(R.id.tv_start_time)
        TextView mTvStartTime;
        @BindView(R.id.ll_top_start_time)
        LinearLayout mLlTopStartTime;
        @BindView(R.id.ll_product_activity_detail)
        LinearLayout mLlProductActivityDetail;

        void init() {
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
        if (likedProductEntity != null && actProActivityList != null
                && actProActivityList.size() > 0) {
            LikedProductBean likedProductBean = actProActivityList.get(0);
            new UMShareAction(QualityProductActActivity.this
                    , likedProductBean.getPicUrl()
                    , getStrings(likedProductEntity.getTitle())
                    , getStrings(likedProductEntity.getActivityDesc())
                    , Url.BASE_SHARE_PAGE_TWO + "common/activitySpecial.html?id=" + likedProductBean.getActivityCode()
                    , "pages/activitySpecial/activitySpecial?id=" + likedProductBean.getActivityCode(), likedProductBean.getId());
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
}
