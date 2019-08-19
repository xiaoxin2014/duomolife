package com.amkj.dmsh.find.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by yiw on 2016/1/6.
 */
public class ImagePagerActivity extends Activity {
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_IMAGESIZE = "imagesize";
    public static final String INTENT_IMAGE_TYPE = "imageType";
    public static final String IMAGE_PRO = "imagePro";
    public static final String IMAGE_DEF = "imageDef";
    private TextView indicator, tv_sku_value_name, tv_sku_value_indicator;
    public ImageSize imageSize;
    public RelativeLayout rel_shop_sku;
    private int startPos;
    private List<ImageBean> imgUrls;
    private int screenWidth;
    private String imageType;


    public static void startImagePagerActivity(Context context, String imageType, List<ImageBean> imgUrls, int position, ImageSize imageSize) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putParcelableArrayListExtra(INTENT_IMGURLS, (ArrayList<? extends Parcelable>) imgUrls);
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        intent.putExtra(INTENT_IMAGE_TYPE, imageType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepager);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        indicator = (TextView) findViewById(R.id.indicator);
        tv_sku_value_name = (TextView) findViewById(R.id.tv_sku_value_name);
        tv_sku_value_indicator = (TextView) findViewById(R.id.tv_sku_value_indicator);
        rel_shop_sku = (RelativeLayout) findViewById(R.id.rel_shop_sku);
        getIntentData();
        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setImageBeanList(imgUrls);
        mAdapter.setImageSize(imageSize);
        viewPager.setAdapter(mAdapter);
        if (IMAGE_PRO.equals(imageType)) {
            indicator.setVisibility(View.GONE);
            rel_shop_sku.setVisibility(View.VISIBLE);
            setIndicatorData(startPos, imgUrls.size());
        } else {
            indicator.setVisibility(View.VISIBLE);
            rel_shop_sku.setVisibility(View.GONE);
            setIndicatorData(startPos, imgUrls.size());
        }
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWidth = app.getScreenWidth();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicatorData(position, viewPager.getAdapter().getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(startPos);
    }

    private void setIndicatorData(int position, int count) {
        if (position < imgUrls.size()) {
            ImageBean imageBean = imgUrls.get(position);
            CharSequence text = getString(R.string.viewpager_indicator,
                    position + 1, count);
            if (IMAGE_PRO.equals(imageType)) {
                tv_sku_value_name.setText(getStrings(imageBean.getSkuValue()));
                tv_sku_value_indicator.setText(text);
            } else {
                indicator.setText(text);
            }
        }
    }

    private void getIntentData() {
        Intent intent = getIntent();
        startPos = intent.getIntExtra(INTENT_POSITION, 0);
        imgUrls = intent.getParcelableArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) intent.getSerializableExtra(INTENT_IMAGESIZE);
        imageType = intent.getStringExtra(INTENT_IMAGE_TYPE);
        if (imgUrls == null || imgUrls.size() < 1) {
            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private class ImageAdapter extends PagerAdapter {

        private List<ImageBean> imageBeanList = new ArrayList<>();
        private LayoutInflater inflater;
        private Context context;
        private ImageSize imageSize;

        public void setImageBeanList(List<ImageBean> imageBeanList) {
            if (imageBeanList != null)
                this.imageBeanList = imageBeanList;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (imageBeanList == null) return 0;
            return imageBeanList.size();
        }


        @SuppressLint("ClickableViewAccessibility")
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if (view != null) {
//                final PhotoView normal_image = (PhotoView) view.findViewById(R.id.normal_image);
                final SubsamplingScaleImageView big_image = (SubsamplingScaleImageView) view.findViewById(R.id.big_image);
                final GifImageView gif_iv_image = (GifImageView) view.findViewById(R.id.gif_iv_image);
                big_image.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    }
                });
                big_image.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });

                gif_iv_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
//                if (imageSize != null) {
                //预览imageView
//                    smallImageView = new ImageView(context);
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
//                    layoutParams.gravity = Gravity.CENTER;
//                    smallImageView.setLayoutParams(layoutParams);
//                    smallImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    ((FrameLayout) view).addView(smallImageView);
//                } else {
//                    smallImageView = new ImageView(context);
//                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    layoutParams.gravity = Gravity.CENTER;
//                    smallImageView.setLayoutParams(layoutParams);
//                    smallImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    ((FrameLayout) view).addView(smallImageView);
//                }

                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);
                final String imgUrl = imageBeanList.get(position).getPicUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    if (imgUrl.contains(".gif")) {
                        RequestOptions gifOptions = new RequestOptions()
                                .priority(Priority.HIGH)
                                .diskCacheStrategy(DiskCacheStrategy.NONE);
                        Glide.with(ImagePagerActivity.this)
                                .asGif()
                                .load(imgUrl)
                                .apply(gifOptions)
                                .into(new SimpleTarget<GifDrawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull GifDrawable gifDrawable, @Nullable Transition<? super GifDrawable> transition) {
                                        loading.setVisibility(View.GONE);
                                        big_image.setVisibility(View.GONE);
                                        gif_iv_image.setVisibility(View.VISIBLE);
                                        gif_iv_image.setImageDrawable(gifDrawable);
                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        super.onLoadFailed(errorDrawable);
                                        loading.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        RequestOptions options = new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL);
                        Glide.with(ImagePagerActivity.this)
                                .asBitmap()
                                .load(imgUrl)
                                .apply(options)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                        loading.setVisibility(View.GONE);
                                        big_image.setVisibility(View.VISIBLE);
                                        gif_iv_image.setVisibility(View.GONE);
                                        float scaleFloat = screenWidth * 1f / bitmap.getWidth();
                                        big_image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                                        big_image.setMinScale(scaleFloat);
                                        big_image.setMaxScale(scaleFloat * 2f);
                                        big_image.setDoubleTapZoomScale(scaleFloat * 2f);
                                        big_image.setImage(ImageSource.bitmap(bitmap), new ImageViewState(scaleFloat, new PointF(0, 0), 0));
                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        super.onLoadFailed(errorDrawable);
                                        loading.setVisibility(View.GONE);
                                    }
                                });
                    }
                }

                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

    }

    public static class ImageSize implements Serializable {

        private int width;
        private int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

}
