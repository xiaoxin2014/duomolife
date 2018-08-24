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
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

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
    //    分享链接
    private final SHARE_MEDIA[] displayList = new SHARE_MEDIA[]{
            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
            SHARE_MEDIA.QQ};
    private Context context;
    final static String FinalOSSThumbFormat = "?@80h_80w.jpg";
    private OnShareSuccessListener onShareSuccessListener;

    /**
     * UMImage image = new UMImage(ShareActivity.this, "imageurl");//网络图片
     * UMImage image = new UMImage(ShareActivity.this, file);//本地文件
     * UMImage image = new UMImage(ShareActivity.this, R.drawable.xxx);//资源文件
     * UMImage image = new UMImage(ShareActivity.this, bitmap);//bitmap文件
     * UMImage image = new UMImage(ShareActivity.this, byte[]);//字节流
     */
    public UMShareAction(final Context context, String imgUrl, final String title, final String description, final String urlLink) {
        this.context = context;
        if(!TextUtils.isEmpty(imgUrl)){
//        图片
            GlideImageLoaderUtil.loadFinishImgDrawable(context, !TextUtils.isEmpty(imgUrl) ?
                    imgUrl + FinalOSSThumbFormat
                    : "", new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                @Override
                public void onStart() {}

                @Override
                public void onSuccess(Bitmap bitmap) {
                    setShareImage(new UMImage(context, bitmap), context, urlLink, title, description);
                }

                @Override
                public void onError(Drawable errorDrawable) {
                    setShareImage(new UMImage(context, R.drawable.domolife_logo), context, urlLink, title, description);
                }
            });
        }else{
            setShareImage(new UMImage(context, R.drawable.domolife_logo), context, urlLink, title, description);
        }
    }

    /**
     * 分享
     * @param context
     * @param drawable
     * @param title
     * @param description
     * @param urlLink
     */
    public UMShareAction(final Context context, int drawable, final String title, final String description, final String urlLink) {
        this.context = context;
        setShareImage(new UMImage(context, drawable),context,urlLink,title,description);
    }

    private void setShareImage(@NonNull UMImage umImage, Context context, String urlLink, String title, String description) {
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
        new ShareAction((Activity) context)
//                        分享平台
                .setDisplayList(displayList)
//                        自定义按钮
                .addButton("复制链接", "umeng_share_c_url", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mKeyword.equals("umeng_share_c_url")) {
                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData mClipData = ClipData.newPlainText("Label", !TextUtils.isEmpty(urlLink) ? urlLink : "多么生活");
                            cmb.setPrimaryClip(mClipData);
                            showToast(context, R.string.copy_url_success);
                        } else {
                            if (share_media != null) {
                                if (share_media.equals(SHARE_MEDIA.SINA)) {
                                    new ShareAction((Activity) context).setPlatform(share_media)
                                            .withText((!TextUtils.isEmpty(title) ? title : "")
                                                    + (!TextUtils.isEmpty(description) ? description : "")
                                                    + (!TextUtils.isEmpty(urlLink) ? urlLink : "多么生活"))
                                            .withMedia(umImage)
                                            .setCallback(umShareListener)
                                            .share();
                                } else {
//                        分享链接
                                    new ShareAction((Activity) context).setPlatform(share_media)
                                            .withMedia(web)
                                            .setCallback(umShareListener)
                                            .share();
                                }
                            }
                        }
                    }

                })
                .open(config);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast(context, getPlatFormText(platform) + " 分享成功啦");
            if (onShareSuccessListener != null) {
                onShareSuccessListener.onShareSuccess();
            }
//            分享成功调用后台接口
            if (userId > 0) {
                shareRewardSuccess(userId,context);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToast(context, getPlatFormText(platform) + " 分享失败,请检查是否安装该应用");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToast(context, getPlatFormText(platform) + " 分享取消了");
        }
    };

    /**
     * 返回分享平台名字
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
