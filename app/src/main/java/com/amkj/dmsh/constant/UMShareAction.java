package com.amkj.dmsh.constant;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.alertdialog.AlertDialogShareHelper;
import com.amkj.dmsh.utils.alertdialog.ShareIconTitleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import static com.amkj.dmsh.constant.ConstantMethod.shareRewardSuccess;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/17
 * class description:友盟分享
 */

public class UMShareAction {
    private Activity context;
    final static String FinalOSSThumbFormat = "?@80h_80w.jpg";
    private OnShareSuccessListener onShareSuccessListener;
    private AlertDialogShareHelper alertDialogShareHelper;
    private boolean isSharing = false;
    private String routineUrl;
    private static String routineId = "gh_cdbcf7765273";

    /**
     * UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
     * UMImage image = new UMImage(ShareActivity.this, file);//本地文件
     * UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
     * UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
     * UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
     * 分享内容
     * @param context
     * @param imgUrl
     * @param title
     * @param description
     * @param urlLink
     */
    public UMShareAction(final Activity context, String imgUrl,
                         final String title, final String description, final String urlLink) {
        this(context,imgUrl,title,description,urlLink,null);
    }

    /**
     * 分享内容 加载图片
     * @param context
     * @param imgUrl
     * @param title
     * @param description
     * @param urlLink 正常地址
     * @param routineUrl 小程序地址
     */
    public UMShareAction(final Activity context, String imgUrl,
                         final String title, final String description, final String urlLink,String routineUrl) {
        this.context = context;
        if(!TextUtils.isEmpty(routineUrl)){
            this.routineUrl = routineUrl;
        }
        if (!TextUtils.isEmpty(imgUrl)) {
//        图片
            GlideImageLoaderUtil.loadFinishImgDrawable(context, !TextUtils.isEmpty(imgUrl) ?
                    imgUrl + FinalOSSThumbFormat
                    : "", new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(Bitmap bitmap) {
                    setShareImage(new UMImage(context, bitmap), context, urlLink, title, description);
                }

                @Override
                public void onError(Drawable errorDrawable) {
                    setShareImage(new UMImage(context, R.drawable.domolife_logo), context, urlLink, title, description);
                }
            });
        } else {
            setShareImage(new UMImage(context, R.drawable.domolife_logo), context, urlLink, title, description);
        }
    }
    /**
     * 分享
     *
     * @param context
     * @param drawable
     * @param title
     * @param description
     * @param urlLink
     */
    public UMShareAction(final Activity context, int drawable,
                         final String title, final String description, final String urlLink) {
        this.context = context;
        setShareImage(new UMImage(context, drawable), context, urlLink, title, description);
    }

    /**
     * 设置分享信息
     * @param umImage
     * @param context
     * @param urlLink
     * @param title
     * @param description
     */
    private void setShareImage(@NonNull UMImage umImage, Activity context,
                               String urlLink, String title, String description) {
        umImage.compressFormat = Bitmap.CompressFormat.PNG;
        //        链接地址
        final UMWeb web = new UMWeb(!TextUtils.isEmpty(urlLink) ? urlLink : "");
        web.setTitle(!TextUtils.isEmpty(title) ? title : "多么生活");//标题
        web.setThumb(umImage);  //缩略图
        web.setDescription(!TextUtils.isEmpty(description) ? description : "有你更精彩");//描述
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        config.setTitleText("分享");
        config.setIndicatorVisibility(false);
        config.setCancelButtonVisibility(false);
        alertDialogShareHelper = new AlertDialogShareHelper(context);
        alertDialogShareHelper.show();
        alertDialogShareHelper.setAlertSelectShareListener(new AlertDialogShareHelper.AlertSelectShareListener() {
            @Override
            public void selectShare(ShareIconTitleBean shareIconTitleBean) {
                alertDialogShareHelper.setLoading(0);
                if(!isSharing){
                    isSharing = true;
                    switch (shareIconTitleBean.getSharePlatformType()) {
                        case POCKET:
                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData mClipData = ClipData.newPlainText("Label", !TextUtils.isEmpty(urlLink) ? urlLink : "多么生活");
                            cmb.setPrimaryClip(mClipData);
                            showToast(context, R.string.copy_url_success);
                            if (alertDialogShareHelper != null) {
                                alertDialogShareHelper.dismiss();
                            }
                            alertDialogShareHelper.setLoading(1);
                            break;
                        case SINA:
                            new ShareAction(context).setPlatform(shareIconTitleBean.getSharePlatformType())
                                    .withText((!TextUtils.isEmpty(title) ? title : "")
                                            + (!TextUtils.isEmpty(description) ? description : "")
                                            + (!TextUtils.isEmpty(urlLink) ? urlLink : "多么生活"))
                                    .withMedia(umImage)
                                    .setCallback(umShareListener)
                                    .share();
                            break;
                        case WEIXIN:
                            if(!TextUtils.isEmpty(routineUrl)){
                                UMMin umMin = new UMMin(urlLink);
                                //兼容低版本的网页链接
                                umMin.setThumb(umImage);
                                // 小程序消息封面图片
                                umMin.setTitle(!TextUtils.isEmpty(title) ? title : "多么生活");
                                // 小程序消息title
                                umMin.setDescription(!TextUtils.isEmpty(description) ? description : "有你更精彩");
                                // 小程序消息描述
                                umMin.setPath(routineUrl);
                                //小程序页面路径
                                umMin.setUserName(routineId);
                                // 小程序原始id,在微信平台查询
                                new ShareAction(context)
                                        .withMedia(umMin)
                                        .setPlatform(shareIconTitleBean.getSharePlatformType())
                                        .setCallback(umShareListener).share();
                            }else{
                                //                        分享链接
                                new ShareAction(context).setPlatform(shareIconTitleBean.getSharePlatformType())
                                        .withMedia(web)
                                        .setCallback(umShareListener)
                                        .share();
                            }
                            break;
                        default:
                            //                        分享链接
                            new ShareAction(context).setPlatform(shareIconTitleBean.getSharePlatformType())
                                    .withMedia(web)
                                    .setCallback(umShareListener)
                                    .share();
                            break;
                    }
                }
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            alertDialogShareHelper.setLoading(1);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast(context, getPlatFormText(platform) + " 分享成功啦");
            if (onShareSuccessListener != null) {
                onShareSuccessListener.onShareSuccess();
            }
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.dismiss();
            }
            isSharing = false;
//            分享成功调用后台接口
            if (userId > 0) {
                shareRewardSuccess(userId, context);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            isSharing = false;
            showToast(context, getPlatFormText(platform) + " 分享失败,请检查是否安装该应用");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            isSharing = false;
            showToast(context, getPlatFormText(platform) + " 分享取消了");
            if (alertDialogShareHelper != null) {
                alertDialogShareHelper.dismiss();
            }
        }
    };

    /**
     * 返回分享平台名字
     *
     * @param platform 平台
     * @return
     */
    @NonNull
    private String getPlatFormText(SHARE_MEDIA platform) {
        String platformText = "";
        switch (platform) {
            case QQ:
                platformText = "QQ";
                break;
            case WEIXIN:
                platformText = "微信";
                break;
            case WEIXIN_CIRCLE:
                platformText = "微信朋友圈";
                break;
            case SINA:
                platformText = "新浪微博";
                break;
        }
        return platformText;
    }

    public void setOnShareSuccessListener(OnShareSuccessListener onShareSuccessListener) {
        this.onShareSuccessListener = onShareSuccessListener;
    }

    public interface OnShareSuccessListener {
        void onShareSuccess();
    }
}
