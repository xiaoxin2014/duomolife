package com.amkj.dmsh.views.alertdialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.dao.GroupDao;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.base.BaseAlertDialog;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.utils.TimeUtils.getCurrentTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifferenceText;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;

/**
 * Created by xiaoxin on 2019/12/30
 * Version:v4.4.0
 * ClassDescription :提示拼团未完成弹窗
 */
public class AlertDialogGroup extends BaseAlertDialog {

    @BindView(R.id.tv_remain)
    TextView mTvRemain;
    @BindView(R.id.tv_gp_price)
    TextView mTvGpPrice;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    @BindView(R.id.tv_invate_group)
    TextView mTvInvate;
    @BindView(R.id.tv_new_user)
    TextView mTvNewUser;

    public AlertDialogGroup(Context context) {
        super(context);
    }

    public void update(RequestStatus requestStatus) {
        mTvRemain.setText(getStringsFormat(context, R.string.remain_person_sucess, requestStatus.getPersonNum()));
        mTvGpPrice.setText(getStringsFormat(context, R.string.only_price, requestStatus.getPrice()));
        GlideImageLoaderUtil.loadCenterCrop(context, mIvCover, requestStatus.getCoverImage());

        String timeStatus;
        String timeDifferent = "";
        String currentTime = getCurrentTime(requestStatus);
        //未结束
        if (!isEndOrStartTime(currentTime, requestStatus.getGpEndTime())) {
            timeStatus = "距结束:";
            timeDifferent = getTimeDifferenceText(getTimeDifference(requestStatus.getGpEndTime(), currentTime));
        } else {
            timeStatus = "已结束";
        }
        mTvEndTime.setText(getStrings(timeStatus + timeDifferent));

        mTvInvate.setOnClickListener(v -> {
            dismiss();
            GroupShopDetailsBean groupShopDetailsBean = new GroupShopDetailsBean();
            groupShopDetailsBean.setCoverImage(requestStatus.getCoverImage());
            groupShopDetailsBean.setGpName(requestStatus.getGpName());
            groupShopDetailsBean.setGpInfoId(requestStatus.getGpInfoId());
            groupShopDetailsBean.setGpRecordId(requestStatus.getGpRecordId());
            groupShopDetailsBean.setType(requestStatus.getType());
            GroupDao.invitePartnerGroup((Activity) context, groupShopDetailsBean);
        });

        mTvNewUser.setVisibility(requestStatus.getRange() == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_group_uncomplete;
    }

    @Override
    protected int getLayoutWith() {
        return AutoSizeUtils.mm2px(mAppContext, 540);
    }
}
