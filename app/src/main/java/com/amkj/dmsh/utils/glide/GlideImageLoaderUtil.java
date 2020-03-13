package com.amkj.dmsh.utils.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.GLES10;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.utils.FileStreamUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.amkj.dmsh.constant.CommunalSavePutValueVariable.FILE_IMAGE;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.isContextExisted;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
        loadFitCenter(context, iv, imgUrl, false);
    }

    public static void loadFitCenter(Context context, ImageView iv, String imgUrl, boolean isBase64) {
        if (null != context && iv != null) {
            Glide.with(context).load(isBase64 ? base64ToBitmap(imgUrl) : imgUrl)
                    .apply(new RequestOptions().fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }


    //base64转bitmap
    public static Bitmap base64ToBitmap(String base64String) {
        try {
            //不占用内存的情况下获取bitmap宽高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            byte[] decode1 = Base64.decode(base64String.replaceAll("data:image/png;base64,", ""), Base64.DEFAULT);
            BitmapFactory.decodeByteArray(decode1, 0, decode1.length, options);
            int imgWidth = options.outWidth;

            //采样率压缩
            int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
            if (imgWidth > screenWidth) {
                double inSampleSize = imgWidth * 1.0f / screenWidth;
                options.inSampleSize = (int) Math.round(inSampleSize);//采样率只支持整数，所以这里采取4舍5入
            }

            //质量压缩
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //如果有前缀解码时会抛异常java.lang.IllegalArgumentException: bad base-64
            byte[] decode = Base64.decode(base64String.replaceAll("data:image/png;base64,", ""), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length, options);
        } catch (Exception e) {
            return null;
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
        if (isContextExisted(context) && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
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
        if (null != context && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .into(iv);
        }
    }

    /**
     * @param context
     * @param iv
     * @param imgUrl
     */
    public static void loadCenterCropListener(Context context, final ImageView iv, String imgUrl
            , ImageLoaderListener imageLoaderListener) {
        if (null != context && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .skipMemoryCache(true)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (imageLoaderListener != null) {
                                imageLoaderListener.onError();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (imageLoaderListener != null) {
                                imageLoaderListener.onSuccess();
                            }
                            return false;
                        }
                    })
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
    public static void loadThumbCenterCrop(Context context, final ImageView iv, String
            imgUrl, String waterRemark) {
        if (null != context && iv != null) {
            Glide.with(context).load(getThumbImgUrl(imgUrl, waterRemark))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
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
        if (null != context && iv != null) {
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
        if (null != context && iv != null) {
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
        if (null != context && iv != null) {
            Glide.with(context).load(getHeaderThumbImgUrl(imgUrl))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop()
                            .placeholder(R.drawable.default_ava_img)
                            .error(R.drawable.default_ava_img))
                    .into(iv);
        }
    }

    /**
     * 加载正方形图片
     */
    public static void loadSquareImg(Context context, final ImageView iv, String imgUrl, String
            waterRemark, int sizeValue) {
        if (null != context && iv != null) {
            Glide.with(context).load(getSquareImgUrl(imgUrl, sizeValue, waterRemark))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop().error(R.drawable.load_loading_image))
                    .transition(withCrossFade())
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
        return getThumbImgUrl(imgUrl, "");
    }

    public static String getThumbImgUrl(String imgUrl, String waterRemark) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgResizeOri = "/auto-orient,1";
            String ossImgThreeThumb = "/resize,w_300";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgThreeThumb + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            } else {
                return imgUrl + ossPrefix + ossImgThreeThumb + ossImgResizeOri + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            }
        }
        return imgUrl;
    }

    public static String getThumbImgUrl(String imgUrl, int sizeValue) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl)
                && imgUrl.contains(ossDataUrl)) {
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

    //裁剪指定尺寸的正方形图片(先缩放成指定尺寸，然后居中裁剪成正方形)
    public static String getSquareImgUrl(String imgUrl, int sizeValue, String waterRemark) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImg = "/resize,m_fill,w_" + sizeValue + ",limit_0/auto-orient,1" + (!imgUrl.contains(".gif") ? "/format,jpg" : "");//转成jpg格式，修复透明底色封面图水印显示不全的问题
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImg + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
            } else {
                return imgUrl + ossPrefix + ossImg + (!TextUtils.isEmpty(waterRemark) ? ("/" + getStrings(waterRemark)) : "");
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
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
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
     * 获取矩形圆角
     *
     * @param radius
     * @return
     */
    public static String getCornerImg(String imgUrl, int radius) {
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
            String ossPrefix = "?x-oss-process=image";
            String ossImgRadius = "/rounded-corners,r_" + String.valueOf(radius) + "/format,png";
            if (imgUrl.contains(ossPrefix)) {
                return imgUrl + ossImgRadius;
            } else {
                return imgUrl + ossPrefix + ossImgRadius;
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
        TinkerBaseApplicationLike applicationLike = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        String ossDataUrl = applicationLike.getOSSDataUrl();
        if (!TextUtils.isEmpty(imgUrl) && imgUrl.contains(ossDataUrl)) {
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
        if (null != context && iv != null) {
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions()
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
        if (null != context && iv != null) {
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
        if (null != context && iv != null) {
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = "android.resource://com.amkj.dmsh/" + defaultImgResource;
            }
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .placeholder(defaultImgResource)
                            .error(defaultImgResource)
                            .transforms(new CenterCrop(), new RoundedCornersTransformation(radius, 0)))
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
     *
     */
    public static void loadRoundImg(final Context context, final ImageView iv,
                                    String imgUrl, int radius, RoundedCornersTransformation.CornerType cornerType) {
        if (null != context && iv != null) {
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = "android.resource://com.amkj.dmsh/" + R.drawable.load_loading_image;
            }
            Glide.with(context).load(imgUrl)
                    .apply(new RequestOptions().dontAnimate()
                            .placeholder(R.drawable.load_loading_image)
                            .error(R.drawable.load_loading_image)
                            .transforms(new CenterCrop(), new RoundedCornersTransformation(radius, 0, cornerType)))
                    .into(iv);
        }
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
    public static void loadFinishImgDrawable(final Context context, String imgUrl,
                                             final ImageLoaderFinishListener loaderFinishListener) {
        if (null != context) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> emitter) {
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
                    if (loaderFinishListener != null) {
                        loaderFinishListener.onSuccess(bitmap);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (loaderFinishListener != null) {
                        loaderFinishListener.onError();
                    }
                }

                @Override
                public void onComplete() {
                    if (loaderFinishListener != null) {
                        loaderFinishListener.onError();
                    }
                }
            });
        }
    }

    /**
     * 自定义尺寸加载图片(原图加载避免大图无法加载 )
     *
     * @param with 图片指定尺寸  -1表示屏幕宽度
     */
    public static void loadImgDynamicDrawable(final Context context,
                                              final ImageView imageView, String imgUrl, int with) {
        if (null != context && imageView != null && !TextUtils.isEmpty(imgUrl)) {
            Map<String, Object> params = new HashMap<>();
            params.put("imgUrl", imgUrl);
            params.put("imgView", imageView);
            Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
                @Override
                public void subscribe(ObservableEmitter<Map<String, Object>> emitter) {
                    int[] imageUrlWidthHeight = getImageUrlWidthHeight(imgUrl);
                    if (imageUrlWidthHeight.length < 2) {
                        imageUrlWidthHeight = new int[]{0, 0};
                    }
                    params.put("imgSize", imageUrlWidthHeight);
                    emitter.onNext(params);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String, Object>>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(Map<String, Object> params) {
                    int imgWidth = Target.SIZE_ORIGINAL;
                    int imgHeight = Target.SIZE_ORIGINAL;
                    String imgUrlX = null;
                    ImageView imageViewX = null;
                    int[] imageUrlWidthHeight = new int[]{imgWidth, imgHeight};
                    try {
                        imgUrlX = (String) params.get("imgUrl");
                        imageViewX = (ImageView) params.get("imgView");
                        imageUrlWidthHeight = (int[]) params.get("imgSize");
                        int screenWidth = (with == -1 ? ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth() : with);
                        if (imageUrlWidthHeight != null && imageUrlWidthHeight.length > 1) {
                            imgWidth = imageUrlWidthHeight[0];
                            imgHeight = imageUrlWidthHeight[1];
                            //判断图片宽度是否大于屏幕宽度
//                            if (imgWidth > screenWidth) {
                            int height = screenWidth * imgHeight / imgWidth;
                            imgWidth = screenWidth;
                            imgHeight = height;
//                            }

                            //判断缩放后的高度是否大于限制高度
                            int limitHeight = getOpenglRenderLimitValue();
                            if (imgHeight > limitHeight) {
                                imgWidth = (int) (limitHeight * 1.0f / (imgHeight * 1.0f) * imgWidth);
                                imgHeight = limitHeight;
                            }
                        }

                        if (isContextExisted(context) && imageViewX != null) {
                            Glide.with(context).load(imgUrlX)
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.load_loading_image)
                                            .error(R.drawable.load_loading_image)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                        }
                    } catch (Exception e) {
                        if (isContextExisted(context) && imageViewX != null) {
                            Glide.with(context).asDrawable().load(imgUrl)
                                    .apply(new RequestOptions().dontAnimate()
                                            .placeholder(R.drawable.load_loading_image)
                                            .error(R.drawable.load_loading_image)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (disposable != null) {
                        disposable.dispose();
                    }
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }


    public static int getOpenglRenderLimitValue() {
        int maxsize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maxsize = getOpenglRenderLimitEqualAboveLollipop();
        } else {
            maxsize = getOpenglRenderLimitBelowLollipop();
        }
        return maxsize == 0 ? 3574 : maxsize;
    }

    private static int getOpenglRenderLimitBelowLollipop() {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private static int getOpenglRenderLimitEqualAboveLollipop() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);
        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {// TROUBLE! No config found.
        }
        EGLConfig config = configs[0];
        int[] surfAttr = {
                EGL10.EGL_WIDTH, 64,
                EGL10.EGL_HEIGHT, 64,
                EGL10.EGL_NONE
        };
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;// missing in EGL10
        int[] ctxAttrib = {
                EGL_CONTEXT_CLIENT_VERSION, 1,
                EGL10.EGL_NONE
        };
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }

    /**
     * 动图加载
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public static void loadGif(final Context context, final ImageView imageView, String imgUrl) {
        if (null != context && imageView != null) {
            // 原图加载避免大图无法加载 预判尺寸是否大于内存最大值
            Map<String, Object> params = new HashMap<>();
            params.put("imgUrl", imgUrl);
            params.put("imgView", imageView);
            Observable.create(new ObservableOnSubscribe<Map<String, Object>>() {
                @Override
                public void subscribe(ObservableEmitter<Map<String, Object>> emitter) {
                    int[] imageUrlWidthHeight = getImageUrlWidthHeight(imgUrl);
                    if (imageUrlWidthHeight.length < 2) {
                        imageUrlWidthHeight = new int[]{0, 0};
                    }
                    params.put("imgSize", imageUrlWidthHeight);
                    emitter.onNext(params);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String, Object>>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                }

                @Override
                public void onNext(Map<String, Object> params) {
                    int imgWidth = Target.SIZE_ORIGINAL;
                    int imgHeight = Target.SIZE_ORIGINAL;
                    String imgUrlX = null;
                    ImageView imageViewX = null;
                    int[] imageUrlWidthHeight;
                    try {
                        imgUrlX = (String) params.get("imgUrl");
                        imageViewX = (ImageView) params.get("imgView");
                        imageUrlWidthHeight = (int[]) params.get("imgSize");
                        if (imageUrlWidthHeight.length > 1) {
                            imgWidth = imageUrlWidthHeight[0];
                            imgHeight = imageUrlWidthHeight[1];
                            if (imageUrlWidthHeight[1] > 3574) {
                                imgWidth = (int) (3574f / imgHeight * imgWidth);
                                imgHeight = 3574;
                            }
                        }
                        if (isContextExisted(context) && imageViewX != null) {
                            Glide.with(context).load(imgUrlX)
                                    .apply(new RequestOptions()
                                            .error(R.drawable.load_loading_image)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isContextExisted(context) && imageViewX != null) {
                            Glide.with(context).load(imgUrlX)
                                    .apply(new RequestOptions()
                                            .error(R.drawable.load_loading_image)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                                            .override(imgWidth, imgHeight))
                                    .into(imageViewX);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }

    /**
     * 动态图片带回调
     *
     * @param context
     * @param originalImgUrl
     * @param originalLoaderFinishListener
     */
    public static void downOriginalImg(Context context, String originalImgUrl,
                                       final OriginalLoaderFinishListener originalLoaderFinishListener) {
        if (null != context) {
            Observable.create(new ObservableOnSubscribe<File>() {
                @Override
                public void subscribe(ObservableEmitter<File> emitter) {
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
     * 监听图片加载过程
     */
    public interface ImageLoaderListener {

        void onSuccess();

        void onError();

    }

    /**
     * 保存图片文件到app存储
     * 子线程调用
     *
     * @param picUrl
     */
    public static void saveImageToFile(Context context, String picUrl) {
        if (TextUtils.isEmpty(picUrl)) {
            return;
        }
        createExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String filePath = context.getDir(FILE_IMAGE, MODE_PRIVATE).getAbsolutePath();
                createFilePath(filePath);
                String imageFilePath = filePath + "/" + picUrl.substring(picUrl.lastIndexOf("/"));
                if (!fileIsExist(imageFilePath)) {
                    try {
//                必须为子线程调用，否则阻塞线程
                        File file = Glide.with(context).downloadOnly().load(picUrl)
                                .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.DATA)).submit().get();
                        if (file != null) {
                            FileStreamUtils.forChannel(file, new File(imageFilePath));
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 保存图片文件到app存储
     * 子线程调用
     *
     * @param picUrl
     */
    public static void saveImageToFile(Context context, String picUrl, String
            saveFilePath, OriginalLoaderFinishListener imageLoaderListener) {
        if (TextUtils.isEmpty(picUrl)) {
            return;
        }
        createExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String saveFileName = FILE_IMAGE;
                if (!TextUtils.isEmpty(saveFilePath)) {
                    saveFileName = saveFilePath;
                }
                String filePath = context.getDir(saveFileName, MODE_PRIVATE).getAbsolutePath();
                createFilePath(filePath);
                String imageFilePath = filePath + picUrl.substring(picUrl.lastIndexOf("/"));
                if (!fileIsExist(imageFilePath)) {
                    try {
//                必须为子线程调用，否则阻塞线程
                        File file = Glide.with(context).downloadOnly().load(picUrl)
                                .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.DATA)).submit().get();
                        if (file != null) {
                            try {
                                File saveFile = new File(imageFilePath);
                                FileStreamUtils.forChannel(file, saveFile);
                                if (imageLoaderListener != null) {
                                    imageLoaderListener.onSuccess(saveFile);
                                }
                            } catch (Exception e) {
                                if (imageLoaderListener != null) {
                                    imageLoaderListener.onError();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (imageLoaderListener != null) {
                        imageLoaderListener.onSuccess(new File(imageFilePath));
                    }
                }
            }
        });
    }

    /**
     * 获取当前文件路径 仅限
     *
     * @param context
     * @param picUrl
     * @return
     */
    public static String getImageFilePath(Context context, String picUrl) {
        if (TextUtils.isEmpty(picUrl)) {
            return "";
        }
        String filePath = context.getDir(FILE_IMAGE, MODE_PRIVATE).getAbsolutePath();
        createFilePath(filePath);
        int indexCode = picUrl.lastIndexOf("/");
        if (indexCode > -1) {
            return filePath + "/" + picUrl.substring(indexCode);
        } else {
            return "";
        }
    }

    /**
     * 获取图片宽高
     *
     * @param path
     * @return
     */
    public static int[] getImageUrlWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        InputStream inputStream = null;
        try {
            URL url = new URL(path);
            inputStream = url.openStream();
            BitmapFactory.decodeStream(inputStream, null, options); // 此时返回的bitmap为null
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * 网络图片获取宽高
     *
     * @param imgUrl
     * @return
     */
    public static int[] getImageWidthHeight(String imgUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUrl, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }


    //获取分享封面图（整个页面控件减去状态栏和标题栏的高度）
    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = null;
        try {
            b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredWidth(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            int left = v.getLeft();
            int top = v.getTop();
            int right = v.getRight();
            int bottom = v.getBottom();
            v.layout(left, top, right, bottom);
            // Draw background
            Drawable bgDrawable = v.getBackground();
            //绘制图片本身的背景
//            if (bgDrawable != null)
//                bgDrawable.draw(c);
//            else
            //手动设置背景
            c.drawColor(Color.WHITE);
            // Draw view to canvas
            v.draw(c);
        } catch (Exception e) {
            return null;
        }

        return b;

    }
}
