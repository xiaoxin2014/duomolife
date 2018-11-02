package com.amkj.dmsh.utils.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().fitCenter().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * 加载图片根据控件设置来展示
     *
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadImage(Context context, final ImageView iv, String imgUrl) {
        if (isContextExisted(context)) {
            Glide.with(context).load(imgUrl)
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
            Glide.with(context).load(imgUrl)
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
            Glide.with(context).load(getThumbImgUrl(imgUrl, waterRemark, isDouble))
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
            Glide.with(context).load(imgUrl)
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

    public static String getThumbImgUrl(String imgUrl, int sizeValue) {
        if (!TextUtils.isEmpty(imgUrl)
                && imgUrl.contains(OSS_URL)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            if (sizeValue < 50) {
                sizeValue = 50;
            }
            String ossImgThumbSize = "/resize,w_" + sizeValue;
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgResizeOri + ossImgThumbSize;
            } else {
                return imgUrl + ossPrefix + ossImgResizeOri + ossImgThumbSize;
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
            Glide.with(context).load(imgUrl)
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
                bitmap = Glide.with(context).asBitmap()
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
     * 启动一个子线程去加载图片
     *
     * @param context
     * @param imgUrl
     * @param loaderFinishListener
     * @see ImageLoaderFinishListener
     */
    public static void loadFinishImgDrawable(final Context context, String imgUrl, final ImageLoaderFinishListener loaderFinishListener) {
        if (null != context) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                    RequestOptions requestOptions = new RequestOptions().centerCrop()
                            .placeholder(R.drawable.load_loading_image)
                            .skipMemoryCache(true);
                    Glide.with(context.getApplicationContext()).asBitmap().load(imgUrl)
                            .apply(requestOptions)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    emitter.onComplete();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(@NonNull Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    emitter.onNext(resource);
                                    return true;
                                }
                            })
                            .submit();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(Bitmap bitmap) {
                    if(loaderFinishListener!=null){
                        loaderFinishListener.onSuccess(bitmap);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if(loaderFinishListener!=null){
                        loaderFinishListener.onError();
                    }
                }

                @Override
                public void onComplete() {
                    if(loaderFinishListener!=null){
                        loaderFinishListener.onError();
                    }
                }
            });
        }
    }

    /**
     * 原图加载
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadImgDynamicDrawable(final Context context, final ImageView imageView, String imgUrl) {
        if (null != context) {
            Glide.with(context).asDrawable().load(imgUrl)
                    .apply(new RequestOptions().dontAnimate().placeholder(R.drawable.load_loading_image)
                            .error(R.drawable.load_loading_image)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                    .into(imageView);
        }
    }

    /**
     * 动图加载
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadGif(final Context context, final ImageView imageView, String imgUrl) {
        if (null != context) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().placeholder(R.drawable.load_loading_image)
                            .error(R.drawable.load_loading_image)
                            .skipMemoryCache(true)
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
        if (null != context) {
            Observable.create(new ObservableOnSubscribe<File>() {
                @Override
                public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                    Glide.with(context).download(originalImgUrl).apply(new RequestOptions().skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)).listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            emitter.onError(e);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            emitter.onNext(resource);
                            return true;
                        }
                    }).submit();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<File>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(File resource) {
                    if (null != originalLoaderFinishListener) {
                        originalLoaderFinishListener.onSuccess(resource);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (null != originalLoaderFinishListener) {
                        originalLoaderFinishListener.onError();
                    }
                }

                @Override
                public void onComplete() {
                    if (null != originalLoaderFinishListener) {
                        originalLoaderFinishListener.onError();
                    }
                }
            });
        }
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
            Glide.with(context).pauseRequests();
        }
    }

    /**
     * 停止加载图片
     *
     * @param activity
     */
    public static void stopLoadByActivity(Activity activity) {
        if (null != activity) {
            Glide.with(activity).pauseRequests();
        }
    }

    /**
     * 恢复加载图片
     *
     * @param activity
     */
    public static void resumeLoadByActivity(Activity activity) {
        if (null != activity) {
            Glide.with(activity).resumeRequests();
        }
    }

    /**
     * 恢复加载图片
     *
     * @param context
     */
    public static void resumeLoadByContext(Context context) {
        if (null != context) {
            Glide.with(context).resumeRequests();
        }
    }

    /**
     * 清除图片缓存
     *
     * @param context
     */
    public static void clearMemoryByContext(Context context) {
        if (null != context) {
            Glide.get(context).clearMemory();
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
                bitmap = Glide.with(context).asBitmap()
                        .load(imgUrl).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取图片大小
     *
     * @param path
     * @return
     */
    private static int[] getImageSizePath(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * 获取网络图片尺寸
     *
     * @param imageUrl
     * @return
     */
    private static int[] getImageSizeUrl(String imageUrl) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream inputStream = new URL(imageUrl).openStream();
            BitmapFactory.decodeStream(inputStream, null, options);
            return new int[]{options.outWidth, options.outHeight};
        } catch (IOException e) {
            return new int[]{300, 300};
        }
    }

    public interface ImageLoaderFinishListener {
        void onSuccess(Bitmap bitmap);
        void onError();
    }

    public interface OriginalLoaderFinishListener {

        void onSuccess(File file);

        void onError();
    }

    public interface gifLoaderFinishListener {

        void onSuccess(Drawable drawable);

        void onError(Drawable errorDrawable);
    }

    /**
     * 获取图片宽高
     * @param path
     * @return
     */
    public static int[] getImageWidthHeight(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth,options.outHeight};
    }
}
