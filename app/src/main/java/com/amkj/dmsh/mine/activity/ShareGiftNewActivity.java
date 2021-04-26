package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.ShareGiftEntity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.adapter.VipInviteAdapter;
import com.amkj.dmsh.mine.bean.VipInviteEntity;
import com.amkj.dmsh.mine.bean.VipInviteEntity.VipInviteBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
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

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_EIGHTY;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_TYPE_SHARE_GIFT;
import static com.amkj.dmsh.constant.ConstantVariable.WEB_VALUE_TYPE;
import static com.amkj.dmsh.constant.Url.GET_VIP_INVITE_LIST;
import static com.amkj.dmsh.constant.Url.GET_VIP_SHARE_INFO;

/**
 * Created by xiaoxin on 2020/8/31
 * Version:v4.7.0
 * ClassDescription :会员相关-分享有礼
 */
public class ShareGiftNewActivity extends BaseActivity {

    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.tv_invite)
    TextView mTvInvite;
    @BindView(R.id.tv_reward)
    TextView mTvReward;
    @BindView(R.id.tv_invite_count)
    TextView mTvInviteCount;
    @BindView(R.id.rv_invite)
    RecyclerView mRvInvite;
    @BindView(R.id.ll_invite)
    LinearLayout mLlInvite;
    @BindView(R.id.rl_share_gift)
    RelativeLayout mRlShareGift;

    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.tv_amount)
    TextView mTvAmount;
    @BindView(R.id.iv_withdraw)
    ImageView mIvWithdraw;
    @BindView(R.id.iv_invite)
    ImageView mIvInvite;
    private ShareGiftEntity mShareGiftEntity;
    private List<VipInviteBean> mInviteBeanList = new ArrayList<>();
    private VipInviteAdapter mVipInviteAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_share_gift;
    }

    @Override
    protected void initViews() {
        mTvHeaderTitle.setText("分享有礼");
        mTvHeaderShared.setVisibility(View.GONE);
        mRvInvite.setLayoutManager(new LinearLayoutManager(this));
        mVipInviteAdapter = new VipInviteAdapter(mInviteBeanList);
        mRvInvite.setAdapter(mVipInviteAdapter);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        getShareInfo();
        getInviteList();
    }

    private void getShareInfo() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GET_VIP_SHARE_INFO, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mShareGiftEntity = GsonUtils.fromJson(result, ShareGiftEntity.class);
                if (mShareGiftEntity != null) {
                    if (SUCCESS_CODE.equals(mShareGiftEntity.getCode())) {
                        ShareGiftEntity.ShareGiftBean shareGiftBean = mShareGiftEntity.getResult();
                        if (shareGiftBean != null) {
                            String count = "获得¥" + shareGiftBean.getPerCapitaPrice();
                            mTvAmount.setText(getSpannableString(count, 2, count.length(), 1.8f, "#FF0000"));
                            mTvReward.setText(shareGiftBean.getWaitGetPrice());
                            mTvInvite.setText(shareGiftBean.getInviteCount());
                        }
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mShareGiftEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mShareGiftEntity);
            }
        });
    }

    private void getInviteList() {
        Map<String, Object> map = new HashMap<>();
        map.put("currentPage", 1);
        map.put("showCount", TOTAL_COUNT_EIGHTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GET_VIP_INVITE_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mInviteBeanList.clear();
                VipInviteEntity vipInviteEntity = GsonUtils.fromJson(result, VipInviteEntity.class);
                if (vipInviteEntity != null) {
                    if (SUCCESS_CODE.equals(vipInviteEntity.getCode())) {
                        List<VipInviteBean> inviteBeanList = vipInviteEntity.getResult();
                        if (inviteBeanList != null && inviteBeanList.size() > 0) {
                            mTvInviteCount.setText(getIntegralFormat(getActivity(), R.string.vip_invite_num, inviteBeanList.size()));
                            mInviteBeanList.addAll(inviteBeanList);
                        }
                    }
                }
                mVipInviteAdapter.notifyDataSetChanged();
                mLlInvite.setVisibility(mInviteBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNotNetOrException() {
                mLlInvite.setVisibility(mInviteBeanList.size() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mRlShareGift;
    }

    @OnClick({R.id.tv_life_back, R.id.iv_withdraw, R.id.iv_invite2, R.id.iv_invite, R.id.iv_rule})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.iv_invite2:
            case R.id.iv_invite:
                new UMShareAction(this, "http://domolifes.oss-cn-beijing.aliyuncs.com/wechatIcon/share_gift_cover.jpg",
                        "好友帮你省钱啦！", "像好友一样成为多么生活会员，免费领取开卡礼，立享10大权益~",
                        Url.BASE_SHARE_PAGE_TWO + "vip/confirm_order.html?shareUid=" + userId, -1);
                break;
            //提现
            case R.id.iv_withdraw:
                withdraw();
                break;
            case R.id.iv_rule:
                intent = new Intent(this, WebRuleCommunalActivity.class);
                intent.putExtra(WEB_VALUE_TYPE, WEB_TYPE_SHARE_GIFT);
                startActivity(intent);
                break;
        }
    }

    private void withdraw() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.SUB_CASH_APPLY, null, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                        getShareInfo();
                    }
                    showToast(requestStatus.getMsg());
                }
            }

            @Override
            public void onNotNetOrException() {
                super.onNotNetOrException();
            }
        });
    }
}
