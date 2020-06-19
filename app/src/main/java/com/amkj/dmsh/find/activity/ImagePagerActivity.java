package com.amkj.dmsh.find.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.photoimage.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
    private String imageType;


    public static void startImagePagerActivity(Context context, String imageType, List<ImageBean> imgUrls, int position) {
        try {
            Intent intent = new Intent(context, ImagePagerActivity.class);
            intent.putParcelableArrayListExtra(INTENT_IMGURLS, (ArrayList<? extends Parcelable>) imgUrls);
            intent.putExtra(INTENT_POSITION, position);
            intent.putExtra(INTENT_IMAGE_TYPE, imageType);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepager);
        //全屏|隐藏状态栏
        ImmersionBar.with(this).fullScreen(true).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
        final ViewPager viewPager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        tv_sku_value_name = findViewById(R.id.tv_sku_value_name);
        tv_sku_value_indicator = findViewById(R.id.tv_sku_value_indicator);
        rel_shop_sku = findViewById(R.id.rel_shop_sku);
        getIntentData();
        ImageAdapter mAdapter = new ImageAdapter(this);
        mAdapter.setImageBeanList(imgUrls);
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
        if (imgUrls == null) {
            imgUrls = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getImageBeanList();
        }
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
        private Activity context;

        public void setImageBeanList(List<ImageBean> imageBeanList) {
            if (imageBeanList != null)
                this.imageBeanList = imageBeanList;
        }

        public ImageAdapter(Activity context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (imageBeanList == null) return 0;
            return imageBeanList.size();
        }


        @NonNull
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if (view != null) {
                PhotoView big_image = view.findViewById(R.id.big_image);
                ProgressBar loading = view.findViewById(R.id.progressbar);
                big_image.setMaxScale(2);
                big_image.enable();
                big_image.setOnClickListener(v -> finish());
                final String imgUrl = imageBeanList.get(position).getPicUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    if (imgUrl.contains(".gif")) {
                        GlideImageLoaderUtil.setLoadGifFinishListener(context, imgUrl, new GlideImageLoaderUtil.GifLoaderFinishListener() {
                            @Override
                            public void onSuccess(GifDrawable drawable) {
                                loading.setVisibility(View.GONE);
                                Glide.with(context).load(drawable).into(big_image);
                            }

                            @Override
                            public void onError() {
                                loading.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        GlideImageLoaderUtil.setLoadDynamicFinishListener(context, big_image, imgUrl, -1, new GlideImageLoaderUtil.ImageLoaderFinishListener() {
                            @Override
                            public void onSuccess(Bitmap bitmap) {
                                loading.setVisibility(View.GONE);
                                big_image.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onError() {
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
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, @NonNull Object object) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
