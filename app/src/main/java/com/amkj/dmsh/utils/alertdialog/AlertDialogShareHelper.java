package com.amkj.dmsh.utils.alertdialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/6/6
 * version 3.1.3
 * class description:默认样式dialog
 */
public class AlertDialogShareHelper {
    private final View loadView;
    private boolean isFirstSet;
    private AlertSelectShareListener alertSelectShareListener;
    private AlertDialog shareAlertDialog;
    private View dialogView;
    private List<ShareIconTitleBean> iconTitleList = new ArrayList<>();
    private int[] shareIcon = {R.drawable.umeng_socialize_wxcircle, R.drawable.umeng_socialize_wechat
            , R.drawable.umeng_socialize_qq, R.drawable.umeng_socialize_sina
            , R.drawable.umeng_socialize_copyurl};
    private String[] shareTitle = {"微信朋友圈", "微信", "QQ", "微博", "复制链接"};
    //
    private SHARE_MEDIA[] sharePlatform = {SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN,
            SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.POCKET};

    public AlertDialogShareHelper(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog_share, null, false);
        AutoUtils.auto(dialogView);
        loadView = dialogView.findViewById(R.id.rel_share_loading);
        builder.setCancelable(true);
//        三个数组必须一一对应
        if (shareIcon.length == shareTitle.length && sharePlatform.length == shareIcon.length) {
            ShareIconTitleBean shareIconTitleBean;
            for (int i = 0; i < shareIcon.length; i++) {
                shareIconTitleBean = new ShareIconTitleBean();
                shareIconTitleBean.setShareTitle(shareTitle[i]);
                shareIconTitleBean.setShareIconResId(shareIcon[i]);
                shareIconTitleBean.setSharePlatformType(sharePlatform[i]);
                iconTitleList.add(shareIconTitleBean);
            }
        }
        RecyclerView communal_recycler_wrap = dialogView.findViewById(R.id.communal_recycler_wrap);
        communal_recycler_wrap.setLayoutManager(new GridLayoutManager(context, 3));
        ShareIconTitleAdapter shareIconTitleAdapter = new ShareIconTitleAdapter(context, iconTitleList);
        communal_recycler_wrap.setAdapter(shareIconTitleAdapter);
        shareIconTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShareIconTitleBean shareIconTitleBean = (ShareIconTitleBean) view.getTag();
                if (shareIconTitleBean != null && alertSelectShareListener != null) {
                    alertSelectShareListener.selectShare(shareIconTitleBean);
                }
            }
        });
        shareAlertDialog = builder.create();
        Window window = shareAlertDialog.getWindow();
        if(window!=null){
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        isFirstSet = true;
    }

    /**
     * 展示dialog
     */
    public void show() {
        if (!shareAlertDialog.isShowing()) {
            shareAlertDialog.show();
        }
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
        if (isFirstSet) {
            Window window = shareAlertDialog.getWindow();
            if (window != null) {
                window.setGravity(Gravity.BOTTOM);
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(attributes);
                shareAlertDialog.setContentView(dialogView);
            }
        }
        isFirstSet = false;
    }

    public void dismiss() {
        if (shareAlertDialog != null) {
            shareAlertDialog.dismiss();
        }
        if (loadView != null) {
            loadView.setVisibility(View.GONE);
        }
    }

    public void setLoading(int code) {
        if (loadView != null) {
            if (code == 1) {
                loadView.setVisibility(View.GONE);
            } else {
                loadView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setAlertSelectShareListener(AlertSelectShareListener alertSelectShareListener) {
        this.alertSelectShareListener = alertSelectShareListener;
    }

    public interface AlertSelectShareListener {
        void selectShare(ShareIconTitleBean shareIconTitleBean);
    }

    private class ShareIconTitleAdapter extends BaseQuickAdapter<ShareIconTitleBean, BaseViewHolderHelper> {
        private final Context context;

        public ShareIconTitleAdapter(Context context, List<ShareIconTitleBean> iconTitleList) {
            super(R.layout.adapter_share_icon_title, iconTitleList);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolderHelper helper, ShareIconTitleBean shareIconTitleBean) {
            GlideImageLoaderUtil.loadFitCenter(context, helper.getView(R.id.iv_share_icon),
                    "android.resource://com.amkj.dmsh/drawable/" + shareIconTitleBean.getShareIconResId());
            helper.setText(R.id.tv_share_title, getStrings(shareIconTitleBean.getShareTitle()))
                    .itemView.setTag(shareIconTitleBean);
        }
    }
}
