package com.amkj.dmsh.dominant.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.dao.GroupDao;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import me.jessyan.autosize.AutoSize;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.amkj.dmsh.utils.TimeUtils.getCurrentTime;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifferenceText;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;

/**
 * Created by xiaoxin on 2019/12/30
 * Version:v4.4.0
 * ClassDescription :提示拼团未完成弹窗
 */
public class AlertDialogGroup {

    private Activity context;
    private AlertDialog imageAlertDialog;
    private View dialogView;
    private boolean isFirstSet;
    private TextView mTvRemain;
    private TextView mTvGpPrice;
    private ImageView mIvCover;
    private TextView mTvEndTime;
    private TextView mTvInvate;
    private TextView mTvNewUser;

    public AlertDialogGroup(Activity context) {
        if (!isContextExisted(context)) {
            return;
        }
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_group_uncomplete, null, false);
        builder.setCancelable(true);
        imageAlertDialog = builder.create();

        mTvRemain = dialogView.findViewById(R.id.tv_remain);
        mTvGpPrice = dialogView.findViewById(R.id.tv_gp_price);
        mIvCover = dialogView.findViewById(R.id.iv_cover);
        mTvEndTime = dialogView.findViewById(R.id.tv_end_time);
        mTvInvate = dialogView.findViewById(R.id.tv_invate_group);
        mTvNewUser = dialogView.findViewById(R.id.tv_new_user);
        isFirstSet = true;
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
            GroupDao.invitePartnerGroup(context, groupShopDetailsBean);
        });

        mTvNewUser.setVisibility(requestStatus.getRange() == 1 ? View.VISIBLE : View.GONE);
    }


    /**
     * 展示dialog
     */
    public void show() {
        if (imageAlertDialog == null) {
            return;
        }
        if (!imageAlertDialog.isShowing() && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal(context);
            imageAlertDialog.show();
        }
        if (isFirstSet) {
            Window window = imageAlertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                window.setContentView(dialogView);
            }
        }
        isFirstSet = false;
    }

    public void dismiss() {
        if (imageAlertDialog != null && isContextExisted(context)) {
            imageAlertDialog.dismiss();
        }
    }

    /**
     * dialog 是否在展示
     */
    public boolean isShowing() {
        return imageAlertDialog != null
                && isContextExisted(context) && imageAlertDialog.isShowing();
    }
}
