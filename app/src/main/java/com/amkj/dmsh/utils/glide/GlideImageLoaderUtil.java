package com.amkj.dmsh.utils.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.concurrent.ExecutionException;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.OSS_URL;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

;

/**
 * 图片加载工具类
 *
 * @author liangzx
 */
public class GlideImageLoaderUtil {
    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadFitCenter(Context context, ImageView iv, String imgUrl) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).load(imgUrl)
                    .apply(new RequestOptions().fitCenter().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * 加载图片根据控件设置来展示
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadImage(Context context, final ImageView iv, String imgUrl) {
        if (isContextExisted(context)) {
            Glide.with(context.getApplicationContext()).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .error(R.drawable.load_loading_image)
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadCenterCrop(Context context, final ImageView iv, String imgUrl) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * 加载双列商品图片 方形
     *
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadThumbCenterCrop(Context context, final ImageView iv, String imgUrl, String waterRemark, boolean isDouble) {
        if (null != context && iv != null) {
            Glide.with(context.getApplicationContext()).load(getThumbImgUrl(imgUrl, waterRemark, isDouble))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .dontAnimate()
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadCenterCrop(Context context, ImageView iv, String imgUrl,
                                      int defaultImgResource) {
        if (null != context) {
            loadCenterCrop(context, iv, imgUrl, defaultImgResource, R.drawable.load_loading_image);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadCenterCrop(Context context, ImageView iv, String imgUrl,
                                      int defaultImgResource, int errorImgResource) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .placeholder(defaultImgResource)
                            .error(errorImgResource)).transition(withCrossFade()).into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadHeaderImg(final Context context, final ImageView iv, String imgUrl) {
        if (null != context) {
            Glide.with(context).load(getHeaderThumbImgUrl(imgUrl))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop()
                            .placeholder(R.drawable.default_ava_img)
                            .error(R.drawable.default_ava_img)
                            .dontAnimate())
                    .into(iv);
        }
    }

    /**
     * 获取缩略图
     *
     * @param imgUrl
     * @return
     */
    public static String getThumbImgUrl(String imgUrl) {
        return getThumbImgUrl(imgUrl, "", true);
    }

    public static String getThumbImgUrl(String imgUrl, String waterRemark, boolean isDouble) {
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(OSS_URL)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            String ossImgDoubleThumb = "/resize,w_400";
            String ossImgThreeThumb = "/resize,w_300";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + (isDouble ? ossImgDoubleThumb : ossImgThreeThumb) + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            } else {
                return imgUrl + ossPrefix + (isDouble ? ossImgDoubleThumb : ossImgThreeThumb) + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            }
        }
        return imgUrl;
    }

    /**
     * 获取水印图片
     *
     * @param imgUrl
     * @param waterRemark
     * @return
     */
    public static String getWaterMarkImgUrl(String imgUrl, String waterRemark) {
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(OSS_URL)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            } else {
                return imgUrl + ossPrefix + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            }
        }
        return imgUrl;
    }

    /**
     * 获取头像缩略图
     *
     * @param imgUrl
     * @return
     */
    private static String getHeaderThumbImgUrl(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(OSS_URL)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            String ossImgHeaderThumb = "/resize,w_200";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgHeaderThumb + ossImgResizeOri;
            } else {
                return imgUrl + ossPrefix + ossImgHeaderThumb + ossImgResizeOri;
            }
        }
        return imgUrl;
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource
     */
    public static void loadWithFitCenter(Context context, ImageView iv, String imgUrl,
                                         int defaultImgResource) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .fitCenter().placeholder(defaultImgResource))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl  加载矩形圆角
     */
    public static void loadRoundImg(final Context context, final ImageView iv,
                                    String imgUrl, int radius) {
        if (null != context) {
            loadRoundImg(context, iv, imgUrl, radius, R.drawable.load_loading_image);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     * @param defaultImgResource 加载矩形圆角
     */
    public static void loadRoundImg(final Context context, final ImageView iv,
                                    String imgUrl, int radius, int defaultImgResource) {
        if (null != context) {
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = "android.resource://com.amkj.dmsh/" + defaultImgResource;
            }
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .placeholder(defaultImgResource)
                            .error(defaultImgResource)
                            .transforms(new GlideRoundTransform(radius)))
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param imgUrl
     * @return
     */
    public static Bitmap get(Context context, String imgUrl) {
        Bitmap bitmap = null;
        try {
            if (null != context) {
                bitmap = Glide.with(context.getApplicationContext()).asBitmap()
                        .load(imgUrl).submit(100, 100).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 带回调的，例如启动页广告需要加载完成在展示
     *
     * @param context
     * @param imgUrl
     * @param loaderFinishListener
     * @see ImageLoaderFinishListener
     */
    public static void loadFinishImgDrawable(final Context context, String imgUrl, final ImageLoaderFinishListener loaderFinishListener) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).asBitmap().load(imgUrl)
                    .apply(new RequestOptions().centerCrop().placeholder(R.drawable.load_loading_image))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onStart() {
                            if (null != loaderFinishListener) {
                                loaderFinishListener.onStart();
                            }
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (null != loaderFinishListener) {
                                loaderFinishListener.onSuccess(resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(Drawable errorDrawable) {
                            if (null != loaderFinishListener) {
                                loaderFinishListener.onError(errorDrawable);
                            }
                        }
                    });
        }
    }

    /**
     * 动态修改图片尺寸 回调
     *
     * @param context
     * @param imgUrl
     * @param loaderFinishListener
     */
    public static void loadImgDynamicDrawable(final Context context, String imgUrl, final ImageLoaderFinishListener loaderFinishListener) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).asBitmap().load(imgUrl)
                    .apply(new RequestOptions().dontAnimate())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onStart() {
                            if (null != loaderFinishListener) {
                                loaderFinishListener.onStart();
                            }
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (null != loaderFinishListener) {
                                loaderFinishListener.onSuccess(resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(Drawable errorDrawable) {
                            if (null != loaderFinishListener) {
                                loaderFinishListener.onError(errorDrawable);
                            }
                        }
                    });
        }
    }

    /**
     * 原图加载
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadImgDynamicDrawable(final Context context, final ImageView imageView, String imgUrl) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).asBitmap().load(imgUrl)
                    .apply(new RequestOptions().dontAnimate().placeholder(R.drawable.load_loading_image)
                            .error(R.drawable.load_loading_image).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                    .into(imageView);
        }
    }

    /**
     * 动图加载
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadGif(final Context context, final ImageView imageView, String imgUrl) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).load(imgUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.load_loading_image)
                            .error(R.drawable.load_loading_image)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .override(Target.SIZE_ORIGINAL))
                    .into(imageView);
        }
    }

    /**
     * 动态图片带回调
     *
     * @param context
     * @param originalImgUrl
     * @param originalLoaderFinishListener
     */
    public static void downOriginalImg(Context context, String originalImgUrl, final OriginalLoaderFinishListener originalLoaderFinishListener) {
        Glide.with(context).download(originalImgUrl).apply(new RequestOptions().override(300)).into(new SimpleTarget<File>() {
            @Override
            public void onStart() {
                if (null != originalLoaderFinishListener) {
                    originalLoaderFinishListener.onStart();
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (null != originalLoaderFinishListener) {
                    originalLoaderFinishListener.onError(errorDrawable);
                }
            }

            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                if (null != originalLoaderFinishListener) {
                    originalLoaderFinishListener.onSuccess(resource);
                }
            }
        });
    }

    //判断文件是否存在
    public static boolean fileIsExist(String invoiceSavePath) {
        File file = new File(invoiceSavePath);
        return file.exists();
    }

    //创建文件夹
    public static void createFilePath(String savePath) {
        File destDir = new File(savePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 停止加载图片
     *
     * @param context
     */
    public static void stopLoadByContext(Context context) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).pauseRequests();
        }
    }

    /**
     * 停止加载图片
     *
     * @param activity
     */
    public static void stopLoadByActivity(Activity activity) {
        if (null != activity) {
            Glide.with(activity.getApplicationContext()).pauseRequests();
        }
    }

    /**
     * 恢复加载图片
     *
     * @param activity
     */
    public static void resumeLoadByActivity(Activity activity) {
        if (null != activity) {
            Glide.with(activity.getApplicationContext()).resumeRequests();
        }
    }

    /**
     * 恢复加载图片
     *
     * @param context
     */
    public static void resumeLoadByContext(Context context) {
        if (null != context) {
            Glide.with(context.getApplicationContext()).resumeRequests();
        }
    }

    /**
     * 清除图片缓存
     *
     * @param context
     */
    public static void clearMemoryByContext(Context context) {
        if (null != context) {
            Glide.get(context.getApplicationContext()).clearMemory();
        }
    }

    /**
     * 清除图片缓存
     *
     * @param activity
     */
    public static void clearMemoryByActivity(Activity activity) {
        if (null != activity) {
            Glide.get(activity.getApplicationContext()).clearMemory();
        }
    }

    public static Bitmap getBitmap(Context context, String imgUrl) {
        Bitmap bitmap = null;
        try {
            if (null != context) {
                bitmap = Glide.with(context.getApplicationContext()).asBitmap()
                        .load(imgUrl).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public interface ImageLoaderFinishListener {

        void onSuccess(Bitmap bitmap);

        void onStart();

        void onError(Drawable errorDrawable);

    }

    public interface OriginalLoaderFinishListener {

        void onSuccess(File file);

        void onStart();

        void onError(Drawable errorDrawable);
    }

    public interface gifLoaderFinishListener {

        void onSuccess(Drawable drawable);

        void onError(Drawable errorDrawable);
    }
}
