package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.BASE_SHARE_PAGE_TWO;
import static com.amkj.dmsh.constant.Url.Q_ACT_PRO_LIST;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/10/17
 * class description:营销活动专场
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
    private List<LikedProductBean> actProActivityList = new ArrayList();
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private QActivityProView qActivityProView;
    private UserLikedProductEntity likedProductEntity;

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

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        qualityTypeProductAdapter = new QualityTypeProductAdapter(QualityProductActActivity.this, actProActivityList);
        View headerView = LayoutInflater.from(QualityProductActActivity.this).inflate(R.layout.layout_act_topic_no_count_time, null, false);
        qActivityProView = new QActivityProView();
        ButterKnife.bind(qActivityProView, headerView);
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
        qualityTypeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(QualityProductActActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        qualityTypeProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                loadHud.show();
                UserLikedProductEntity.LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    if (userId > 0) {
                        switch (view.getId()) {
                            case R.id.iv_pro_add_car:
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                                baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                                baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(QualityProductActActivity.this, baseAddCarProInfoBean, loadHud);
                                constantMethod.setAddOnCarListener(new ConstantMethod.OnAddCarListener() {
                                    @Override
                                    public void onAddCarSuccess() {
                                        getCarCount();
                                    }
                                });
                                break;
                        }
                    } else {
                        loadHud.dismiss();
                        getLoginStatus(QualityProductActActivity.this);
                    }
                }
            }
        });
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
            getCarCount();
        }
    }

    @Override
    protected void loadData() {
        page = 1;
        getActProActivityData();
        getCarCount();
    }

    @Override
    protected View getLoadView() {
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
                        qualityTypeProductAdapter.loadMoreComplete();
                        if (page == 1) {
                            actProActivityList.clear();
                        }
                        Gson gson = new Gson();
                        likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                actProActivityList.addAll(likedProductEntity.getLikedProductBeanList());
                                setActProInfo(likedProductEntity);
                            } else if (likedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityTypeProductAdapter.loadMoreEnd();
                            } else {
                                showToast(QualityProductActActivity.this, likedProductEntity.getMsg());
                            }
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, actProActivityList, likedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, actProActivityList, likedProductEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(QualityProductActActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(QualityProductActActivity.this, R.string.invalidData);
                    }
                });
    }

    private void setActProInfo(UserLikedProductEntity likedProductEntity) {
        qActivityProView.tv_communal_pro_tag.setVisibility(View.VISIBLE);
        qActivityProView.tv_communal_pro_tag.setText(getStrings(likedProductEntity.getActivityTag()));
        qActivityProView.tv_pro_act_desc.setText(getStrings(likedProductEntity.getActivityRule()));
        if (!ConstantMethod.isTimeSameYear(likedProductEntity.getActivityStartTime(), likedProductEntity.getActivityEndTime())) {
            qActivityProView.tv_pro_act_s_end_time.setText(("活动时间:"
                    + ConstantMethod.getDateFormat(likedProductEntity.getActivityStartTime(), "yyyy-MM-dd HH:mm")
                    + "~"
                    + ConstantMethod.getDateFormat(likedProductEntity.getActivityEndTime(), "yyyy-MM-dd HH:mm"))
            );
        } else {
            qActivityProView.tv_pro_act_s_end_time.setText(("活动时间:"
                    + ConstantMethod.getDateFormat(likedProductEntity.getActivityStartTime(), "MM-dd HH:mm")
                    + "~"
                    + ConstantMethod.getDateFormat(likedProductEntity.getActivityEndTime(), "MM-dd HH:mm"))
            );
        }
        tv_header_titleAll.setText(getStrings(likedProductEntity.getTitle()));
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_QUERY_CAR_COUNT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals(EMPTY_CODE)) {
                            showToastRequestMsg(QualityProductActActivity.this, requestStatus);
                        }
                    }
                }
            });
        }
    }

    class QActivityProView {
        @BindView(R.id.tv_communal_pro_red_tag)
        TextView tv_communal_pro_tag;
        @BindView(R.id.tv_product_activity_description)
        TextView tv_pro_act_desc;
        @BindView(R.id.tv_pro_act_s_end_time)
        TextView tv_pro_act_s_end_time;
        AlertDialog alertDialog;

        /**
         * 弹框打开活动规则详情
         *
         * @param view
         */
        @OnClick(R.id.ll_act_topic_no_coun_time)
        void openActivityRule(View view) {
            if (likedProductEntity != null) {
                if (alertDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QualityProductActActivity.this, R.style.CustomTransDialog);
                    View dialogView = LayoutInflater.from(QualityProductActActivity.this).inflate(R.layout.layout_act_topic_rule, null, false);
                    RuleDialogView ruleDialog = new RuleDialogView();
                    ButterKnife.bind(ruleDialog, dialogView);
                    ruleDialog.initViews();
                    ruleDialog.setData();
                    alertDialog = builder.create();
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    int dialogHeight = (int) (app.getScreenHeight() * 0.4 + 1);
                    Window window = alertDialog.getWindow();
                    window.getDecorView().setPadding(0, 0, 0, 0);
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, dialogHeight);
                    window.setGravity(Gravity.BOTTOM);//底部出现
                    window.setContentView(dialogView);
                } else {
                    alertDialog.show();
                }
            }
        }

        class RuleDialogView {
            @BindView(R.id.tv_act_topic_rule_title)
            TextView tv_act_topic_rule_title;
            @BindView(R.id.tv_act_topic_rule_skip)
            TextView tv_act_topic_rule_skip;
            @BindView(R.id.communal_recycler)
            RecyclerView communal_recycler;
            CommunalDetailAdapter actRuleDetailsAdapter;
            List<CommunalDetailObjectBean> communalDetailBeanList = new ArrayList<>();

            public void initViews() {
                tv_act_topic_rule_skip.setVisibility(View.GONE);
                communal_recycler.setNestedScrollingEnabled(false);
                communal_recycler.setLayoutManager(new LinearLayoutManager(QualityProductActActivity.this));
                communal_recycler.setNestedScrollingEnabled(false);
                actRuleDetailsAdapter = new CommunalDetailAdapter(QualityProductActActivity.this, communalDetailBeanList);
                communal_recycler.setLayoutManager(new LinearLayoutManager(QualityProductActActivity.this));
                communal_recycler.setAdapter(actRuleDetailsAdapter);
            }

            public void setData() {
                tv_act_topic_rule_title.setText("活动规则");
                tv_act_topic_rule_skip.setText(String.format(getResources().getString(R.string.skip_topic)
                        , getStrings(likedProductEntity.getActivityTag())));
                if (likedProductEntity.getActivityRuleDetailList() != null && likedProductEntity.getActivityRuleDetailList().size() > 0) {
                    communalDetailBeanList.clear();
                    communalDetailBeanList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(likedProductEntity.getActivityRuleDetailList()));
                    actRuleDetailsAdapter.notifyDataSetChanged();
                }
            }
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
        if (likedProductEntity != null && likedProductEntity.getLikedProductBeanList() != null
                && likedProductEntity.getLikedProductBeanList().size() > 0) {
            LikedProductBean likedProductBean = likedProductEntity.getLikedProductBeanList().get(0);
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
}
