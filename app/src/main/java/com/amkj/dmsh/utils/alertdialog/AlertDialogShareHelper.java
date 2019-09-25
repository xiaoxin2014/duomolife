package com.amkj.dmsh.utils.alertdialog;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.activity.JoinSuccessActivity;
import com.amkj.dmsh.find.activity.PostDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
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

    public AlertDialogShareHelper(Activity context) {
        this(context, false, "");
    }

    public AlertDialogShareHelper(Activity context, boolean showDownImg, String h5Platform) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialogView = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog_share, null, false);
        loadView = dialogView.findViewById(R.id.rel_share_loading);
        builder.setCancelable(true);

        if (TextUtils.isEmpty(h5Platform)) {
            //默认显示微信和朋友圈
            iconTitleList.add(new ShareIconTitleBean(R.drawable.share_wechat_icon, "微信", SHARE_MEDIA.WEIXIN));
            iconTitleList.add(new ShareIconTitleBean(R.drawable.share_circle_icon, "微信朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));

            //晒单成功界面只分享到微信渠道
            if (!JoinSuccessActivity.class.getSimpleName().equals(context.getClass().getSimpleName())) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.share_qq_icon, "QQ", SHARE_MEDIA.QQ));
                iconTitleList.add(new ShareIconTitleBean(R.drawable.share_sina_icon, "微博", SHARE_MEDIA.SINA));
            }

            //默认显示复制链接
            iconTitleList.add(new ShareIconTitleBean(R.drawable.share_copy_icon, "复制链接", SHARE_MEDIA.POCKET));

            //是否保存商品图片
            if (showDownImg) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.share_save_icon, "保存图片", SHARE_MEDIA.MORE));
            }

            //自己的帖子显示删除按钮
            if (PostDetailActivity.class.getSimpleName().equals(context.getClass().getSimpleName()) && ((PostDetailActivity) context).isMyPost()) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.delete_post, "删除帖子", SHARE_MEDIA.LINE));
            }

            //不是自己的帖子显示举报按钮
            if (PostDetailActivity.class.getSimpleName().equals(context.getClass().getSimpleName()) && !((PostDetailActivity) context).isMyPost()) {
                iconTitleList.add(new ShareIconTitleBean(R.drawable.report_post, "举报帖子", SHARE_MEDIA.TUMBLR));
            }
        } else {
            List<String> platforms = Arrays.asList(h5Platform.split(","));
            for (String platform : platforms) {
                switch (platform) {
                    case "1":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_wechat_icon, "微信", SHARE_MEDIA.WEIXIN));
                        break;
                    case "2":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_circle_icon, "微信朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
                        break;
                    case "3":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_qq_icon, "QQ", SHARE_MEDIA.QQ));
                        break;
                    case "4":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_sina_icon, "微博", SHARE_MEDIA.SINA));
                        break;
                    case "5":
                        iconTitleList.add(new ShareIconTitleBean(R.drawable.share_copy_icon, "复制链接", SHARE_MEDIA.POCKET));
                        break;
                }
            }
        }

        RecyclerView communal_recycler_wrap = dialogView.findViewById(R.id.communal_recycler_wrap);
        if (iconTitleList.size() == 2) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) communal_recycler_wrap.getLayoutParams();
            layoutParams.setMargins(AutoSizeUtils.mm2px(mAppContext, 125), 0, AutoSizeUtils.mm2px(mAppContext, 125), 0);
        }
        communal_recycler_wrap.setLayoutManager(new GridLayoutManager(context, iconTitleList.size() <= 4 ? iconTitleList.size() : 3));//4个及4个以内都是一行，超过4个才换行
        ShareIconTitleAdapter shareIconTitleAdapter = new ShareIconTitleAdapter(iconTitleList);
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
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        isFirstSet = true;
    }

    /**
     * 展示dialog
     */
    public void show() {
        if (!shareAlertDialog.isShowing()
                && isContextExisted(context)) {
            AutoSize.autoConvertDensityOfGlobal((Activity) context);
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
        if (shareAlertDialog != null && isContextExisted(context)) {
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

        public ShareIconTitleAdapter(List<ShareIconTitleBean> iconTitleList) {
            super(R.layout.adapter_share_icon_title, iconTitleList);
        }

        @Override
        protected void convert(BaseViewHolder helper, ShareIconTitleBean shareIconTitleBean) {
            ImageView iv_share_icon = helper.getView(R.id.iv_share_icon);
            try {
                iv_share_icon.setImageResource(shareIconTitleBean.getShareIconResId());
            } catch (Exception e) {
                e.printStackTrace();
                iv_share_icon.setImageResource(R.drawable.load_loading_image);
            }
            helper.setText(R.id.tv_share_title, getStrings(shareIconTitleBean.getShareTitle()))
                    .itemView.setTag(shareIconTitleBean);
        }
    }
}
