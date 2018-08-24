package com.amkj.dmsh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/1/20
 * class description:指导页
 */

public class GuideImagesPagerAdapter extends PagerAdapter {
    private final Context context;
    private final List<Integer> localImages;

    public GuideImagesPagerAdapter(Context context, List<Integer> localImages) {
        this.context = context;
        this.localImages = localImages;
    }

    @Override
    public int getCount() {
        return localImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundColor(context.getResources().getColor(R.color.white));
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        GlideImageLoaderUtil.loadCenterCrop(context,imageView,"android.resource://com.amkj.dmsh/"+localImages.get(position));
        imageView.setTag(R.id.iv_tag,position);
        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.iv_tag);
                if (position == localImages.size() - 1) {
                    SharedPreferences sp = context.getSharedPreferences("duomolife", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean("isFirstRun", false);
                    edit.apply();
                    skipWelcome();
                }
            }
        });
        return imageView;
    }

    private void skipWelcome() {
        Intent intent = new Intent(context, WelcomeLaunchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
