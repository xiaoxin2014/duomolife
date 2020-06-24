package com.amkj.dmsh.qyservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.amkj.dmsh.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.UnicornImageLoader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/27
 * version 3.1.6
 * class description:七鱼图片下载
 */
public class QYGlideImageLoader implements UnicornImageLoader {
    private Context context;

    public QYGlideImageLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Nullable
    @Override
    public Bitmap loadImageSync(String uri, int width, int height) {
        return null;
    }

    @Override
    public void loadImage(String uri, int width, int height, ImageLoaderListener listener) {
        Glide.with(context).asBitmap().load(uri)
                .apply(new RequestOptions().centerCrop().placeholder(R.drawable.load_loading_image))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        if (listener != null) {
                            listener.onLoadComplete(resource);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        if (listener != null) {
                            listener.onLoadFailed(null);
                        }
                    }

                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (listener != null) {
                            listener.onLoadFailed(null);
                        }
                    }
                });
    }
}
