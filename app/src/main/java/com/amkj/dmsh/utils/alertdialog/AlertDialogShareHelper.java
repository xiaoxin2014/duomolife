package com.amkj.dmsh.utils.alertdialog;

import android.app.Activity;
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
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/6/6
 * version 3.1.3
 * class description:默认样式dialog
 */
public class AlertDialogShareHelper {
    private final View loadView;
    private final Context context;
    private boolean isFirstSet;
    private AlertSelectShareListener alertSelectShareListener;
    private AlertDialog shareAlertDialog;
    private View dialogView;
    private List<ShareIconTitleBean> iconTitleList = new ArrayList<>();
    private Integer [] shareIcon = {R.drawable.share_wechat_icon,R.drawable.share_circle_icon
            ,  R.drawable.share_qq_icon, R.drawable.share_sina_icon
            , R.drawable.share_copy_icon, R.drawable.share_save_icon};
    private String[] shareTitle = { "微信","微信朋友圈", "QQ", "微博", "复制链接","保存图片"};
    //
    private SHARE_MEDIA[] sharePlatform = {SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,
            SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.POCKET,SHARE_MEDIA.MORE};

    public AlertDialogShareHelper(Activity context) {
        this(context, false);
    }

    public AlertDialogShareHelper(Activity context,boolean showDownImg) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog_share, null, false);
        loadView = dialogView.findViewById(R.id.rel_share_loading);
        builder.setCancelable(true);
//        三个数组必须一一对应
        if (shareIcon.length == shareTitle.length && sharePlatform.length == shareIcon.length) {
            List<Integer> shareIcons = Arrays.asList(shareIcon);
            List<String> shareTitles = Arrays.asList(shareTitle);
            List<SHARE_MEDIA> shareMedia = Arrays.asList(sharePlatform);
            if(!showDownImg){
                shareIcons = shareIcons.subList(0,shareIcons.size()-1);
                shareTitles = shareTitles.subList(0,shareTitles.size()-1);
                shareMedia = shareMedia.subList(0,shareMedia.size()-1);
            }
            ShareIconTitleBean shareIconTitleBean;
            for (int i = 0; i < shareIcons.size(); i++) {
                shareIconTitleBean = new ShareIconTitleBean();
                shareIconTitleBean.setShareTitle(shareTitles.get(i));
                shareIconTitleBean.setShareIconResId(shareIcons.get(i));
                shareIconTitleBean.setSharePlatformType(shareMedia.get(i));
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
        if (!shareAlertDialog.isShowing()
                &&isContextExisted(context)) {
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

    private class ShareIconTitleAdapter extends BaseQuickAdapter<ShareIconTitleBean, BaseViewHolder> {
        private final Context context;

        public ShareIconTitleAdapter(Context context, List<ShareIconTitleBean> iconTitleList) {
            super(R.layout.adapter_share_icon_title, iconTitleList);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, ShareIconTitleBean shareIconTitleBean) {
            GlideImageLoaderUtil.loadFitCenter(context, helper.getView(R.id.iv_share_icon),
                    "android.resource://com.amkj.dmsh/drawable/" + shareIconTitleBean.getShareIconResId());
            helper.setText(R.id.tv_share_title, getStrings(shareIconTitleBean.getShareTitle()))
                    .itemView.setTag(shareIconTitleBean);
        }
    }
}
