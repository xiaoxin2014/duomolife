package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.homepage.bean.VideoDetailEntity.VideoDetailBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2021/2/23
 * Version:v5.0.0
 * ClassDescription :视频商品列表适配器
 */
public class VideoProductAdapter extends BaseQuickAdapter<VideoDetailBean, BaseViewHolder> {
    private Activity context;

    public VideoProductAdapter(Activity context, @Nullable List<VideoDetailBean> data) {
        super(R.layout.item_video_product, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDetailBean item) {
        if (item == null) return;
        ImageView ivCover = helper.getView(R.id.iv_cover);
        if (!TextUtils.isEmpty(item.getCoverPath())) {
            ViewGroup.LayoutParams layoutParams = ivCover.getLayoutParams();
            int coverHeight = item.getHeight();
            int coverWidth = item.getWidth();

            if (item.getHeight() == 0 || item.getWidth() == 0) {
                layoutParams.width = MATCH_PARENT;
                layoutParams.height = AutoSizeUtils.mm2px(mAppContext, 360);
            } else {
                int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
                int with = (screenWidth - 3 * AutoSizeUtils.mm2px(context, 10)) / 2;
                int height = (int) ((coverHeight * 1.0f / coverWidth * 1.0f) * with);

                if (height > 640) {
                    height = 640;
                }
                layoutParams.height = height;
                Log.d(TAG, with + "");
                Log.d(TAG, height + "");
            }

            ivCover.setLayoutParams(layoutParams);
            GlideImageLoaderUtil.loadImage(context, ivCover, item.getCoverPath());
        }
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), item.getCoverPath());
        helper.setText(R.id.tv_title, getStrings(item.getTitle()))
                .setText(R.id.tv_time, getStrings(getMinuteSecond(item.getSecond())))
                .setGone(R.id.tv_time, item.getSecond() > 0);
        TextView tvFavor = helper.getView(R.id.tv_favor);
        tvFavor.setSelected(item.isCollect());
        tvFavor.setText(item.getFavorNum() > 0 ? String.valueOf(item.getFavorNum()) : "赞");
        tvFavor.setOnClickListener(v -> SoftApiDao.collectVideo(context, item, tvFavor));
        helper.itemView.setTag(item);
    }

    //秒转化成分钟+秒
    private String getMinuteSecond(int secondCount) {
        int minute = secondCount / 60;
        int second = secondCount % 60;
        return (minute > 10 ? "" : "0") + minute + ":" + (second > 10 ? "" : "0") + second;
    }
}
