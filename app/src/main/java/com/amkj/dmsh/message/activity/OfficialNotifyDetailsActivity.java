package com.amkj.dmsh.message.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.message.bean.OfficialNotifyEntity;
import com.amkj.dmsh.message.bean.OfficialNotifyEntity.OfficialNotifyParseBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_MES_OFFICIAL_DETAILS;

;
;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/11
 * class description:官方通知详情
 */
public class OfficialNotifyDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_title;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    //主：评论列表
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_official_details;
    //    文章详情
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private int scrollY;
    private float screenHeight;
    private String notifyId;
    private CommunalDetailAdapter contentOfficialAdapter;
    private List<CommunalDetailObjectBean> itemBodyList = new ArrayList<>();
    private CoverTitleView coverTitleView;
    private OfficialNotifyEntity officialNotifyEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_official_notify;
    }

    @Override
    protected void initViews() {
        tv_header_title.setText("");
        tv_header_shared.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        notifyId = intent.getStringExtra("notifyId");
        //        官方文章内容
        contentOfficialAdapter = new CommunalDetailAdapter(this, itemBodyList);
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.setLayoutManager(new LinearLayoutManager(OfficialNotifyDetailsActivity.this));
        View coverView = LayoutInflater.from(OfficialNotifyDetailsActivity.this)
                .inflate(R.layout.layout_communal_article_cover_title, (ViewGroup) communal_recycler.getParent(), false);
        coverTitleView = new CoverTitleView();
        ButterKnife.bind(coverTitleView, coverView);
        contentOfficialAdapter.addHeaderView(coverView);
        communal_recycler.setAdapter(contentOfficialAdapter);
        contentOfficialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_communal_share ) {
                   return;
                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(OfficialNotifyDetailsActivity.this, view, loadHud);
            }
        });
        smart_official_details.setOnRefreshListener(refreshLayout -> loadData());
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
    }

    @Override
    protected void loadData() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", notifyId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_MES_OFFICIAL_DETAILS,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_official_details.finishRefresh();
                        Gson gson = new Gson();
                        officialNotifyEntity = gson.fromJson(result, OfficialNotifyEntity.class);
                        if (officialNotifyEntity != null) {
                            if (officialNotifyEntity.getCode().equals(SUCCESS_CODE)) {
                                itemBodyList.clear();
                                OfficialNotifyParseBean officialNotifyParseBean = officialNotifyEntity.getOfficialNotifyParseBean();
                                tv_header_title.setText(getStrings(officialNotifyParseBean.getTitle()));
                                setMessageOfficialHeader(officialNotifyEntity.getOfficialNotifyParseBean());
                                List<CommunalDetailBean> contentBeanList = officialNotifyEntity.getOfficialNotifyParseBean().getContentBeanList();
                                if (contentBeanList != null) {
                                    itemBodyList.clear();
                                    itemBodyList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(contentBeanList));
                                }
                            } else if (!officialNotifyEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(officialNotifyEntity.getMsg());
                            }
                            contentOfficialAdapter.setNewData(itemBodyList);
                            NetLoadUtils.getNetInstance().showLoadSir(loadService, itemBodyList, officialNotifyEntity);
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_official_details.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, itemBodyList, officialNotifyEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                        showToast( R.string.invalidData);
                    }
                });
    }

    @Override
    public View getLoadView() {
        return smart_official_details;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void setMessageOfficialHeader(OfficialNotifyParseBean officialNotifyParseBean) {
        //加载数据
        GlideImageLoaderUtil.loadCenterCrop(OfficialNotifyDetailsActivity.this, coverTitleView.img_article_details_bg, getStrings(officialNotifyParseBean.getCover_url()));
        coverTitleView.tv_article_details_title.setText(getStrings(officialNotifyParseBean.getTitle()));
        coverTitleView.tv_article_details_time.setText(getStrings(officialNotifyParseBean.getCreate_time()));
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    class CoverTitleView {
        //        标题
        @BindView(R.id.tv_article_details_title)
        TextView tv_article_details_title;
        //        封面图
        @BindView(R.id.img_article_details_bg)
        ImageView img_article_details_bg;
        //        发布时间
        @BindView(R.id.tv_article_details_time)
        TextView tv_article_details_time;
    }

    public String getNotifyId() {
        return notifyId;
    }
}
