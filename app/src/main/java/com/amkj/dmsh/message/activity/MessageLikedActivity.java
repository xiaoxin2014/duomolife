package com.amkj.dmsh.message.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.ArticleCommentEntity;
import com.amkj.dmsh.bean.ArticleCommentEntity.ArticleCommentBean;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.message.adapter.MessageCommunalAdapterNew;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipPostDetail;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_MES_LIKED;


/**
 * ClassDescription :消息中心-点赞列表
 */
public class MessageLikedActivity extends BaseActivity {
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;

    private List<ArticleCommentBean> articleCommentList = new ArrayList<>();
    public static final String TYPE = "message_liked";
    private int page = 1;
    private MessageCommunalAdapterNew messageCommunalAdapter;
    private ArticleCommentEntity articleCommentEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_communal;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        tv_header_titleAll.setText("赞");
        mTlNormalBar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        messageCommunalAdapter = new MessageCommunalAdapterNew(MessageLikedActivity.this, articleCommentList, TYPE);
        //获取头部消息列表
        communal_recycler.setLayoutManager(new LinearLayoutManager(this));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_ten_dp)
                .create());
        communal_recycler.setAdapter(messageCommunalAdapter);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        messageCommunalAdapter.setOnLoadMoreListener(() -> {
            page++;
            getData();
        }, communal_recycler);
        setFloatingButton(download_btn_communal, communal_recycler);
        messageCommunalAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            try {
                Intent intent = new Intent();
                switch (view.getId()) {
                    case R.id.iv_inv_user_avatar:
                        ArticleCommentBean articleCommentBean1 = (ArticleCommentBean) view.getTag(R.id.iv_avatar_tag);
                        intent.setClass(MessageLikedActivity.this, UserPagerActivity.class);
                        intent.putExtra("userId", String.valueOf(articleCommentBean1.getUid()));
                        startActivity(intent);
                        break;
                    case R.id.rel_adapter_message_communal:
                        ArticleCommentBean articleCommentBean2 = (ArticleCommentBean) view.getTag();
                        if (articleCommentBean2.getStatus() == 1) {
                            switch (articleCommentBean2.getObj()) {
                                case "doc":
                                case "post":
                                    skipPostDetail(getActivity(), String.valueOf(articleCommentBean2.getObject_id()), 2);
                                    break;
                                case "proEvaluate":
                                    intent.setClass(MessageLikedActivity.this, ShopScrollDetailsActivity.class);
                                    intent.putExtra("productId", String.valueOf(articleCommentBean2.getObject_id()));
                                    startActivity(intent);
                                    break;
                            }
                        } else {
                            showToast("已删除");
                        }
                        break;
                    case R.id.tv_follow:
                        ArticleCommentBean articleCommentBean3 = (ArticleCommentBean) view.getTag();
                        SoftApiDao.followUser(getActivity(), articleCommentBean3.getUid(), ((TextView) view), articleCommentBean3, new SoftApiDao.FollowCompleteListener() {
                            @Override
                            public void followComplete(boolean isFollow) {
                                articleCommentBean3.setIsFocus(isFollow);
                            }
                        });
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void finish(View view) {
        finish();
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("to_uid", userId);
        params.put("currentPage", page);
        NetLoadUtils.getNetInstance().loadNetDataPost(MessageLikedActivity.this, H_MES_LIKED
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        messageCommunalAdapter.loadMoreComplete();
                        if (page == 1) {
                            articleCommentList.clear();
                        }

                        //赞
                        articleCommentEntity = GsonUtils.fromJson(result, ArticleCommentEntity.class);
                        if (articleCommentEntity != null) {
                            if (articleCommentEntity.getCode().equals(SUCCESS_CODE)) {
                                articleCommentList.addAll(articleCommentEntity.getArticleCommentList());
                            } else if (articleCommentEntity.getCode().equals(EMPTY_CODE)) {
                                messageCommunalAdapter.loadMoreEnd();
                            } else {
                                showToast( articleCommentEntity.getMsg());
                            }
                        }
                        messageCommunalAdapter.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, articleCommentList, articleCommentEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        messageCommunalAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, articleCommentList, articleCommentEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }
}
