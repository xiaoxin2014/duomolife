package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.activity.VideoDetailActivity;
import com.amkj.dmsh.homepage.bean.VideoDetailEntity.VideoDetailBean;
import com.amkj.dmsh.homepage.view.JzvdStdTikTok;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;

/**
 * Created by xiaoxin on 2021/2/26
 * Version:v5.0.0
 * ClassDescription :视频详情列表
 */
public class VideoDetailAdapter extends BaseQuickAdapter<VideoDetailBean, BaseViewHolder> {
    private Activity context;

    public VideoDetailAdapter(Activity context, @Nullable List<VideoDetailBean> data) {
        super(R.layout.item_video_detail, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDetailBean item) {
        if (item == null) return;
        ViewPager viewPager = helper.getView(R.id.vp_goods);
        //视频播放
        JZDataSource jzDataSource = new JZDataSource(item.getVideoPath(), item.getTitle());
        jzDataSource.looping = true;
        JzvdStdTikTok jzvdStdTikTok = helper.getView(R.id.videoplayer);
        jzvdStdTikTok.setUp(jzDataSource, Jzvd.SCREEN_NORMAL);
        Glide.with(context).load(item.getCoverPath()).into(jzvdStdTikTok.posterImageView);
        jzvdStdTikTok.setOnStartClickListener(showProduct -> {
            viewPager.setVisibility(showProduct ? View.VISIBLE : View.GONE);
            item.setShowProduct(showProduct);
            if (context instanceof VideoDetailActivity) {
                ((VideoDetailActivity) context).setTop(showProduct);
            }
        });
        jzvdStdTikTok.bottomContainer.setVisibility(item.isShowProduct() ? View.GONE : View.VISIBLE);
        //关联商品列表
        viewPager.setVisibility(item.isShowProduct() ? View.VISIBLE : View.GONE);
        List<VideoDetailBean.ProductInfoListBean> productInfoList = item.getProductInfoList();
        if (productInfoList != null && productInfoList.size() > 0) {
            VideoRelatedAdapter videoRelatedAdapter = new VideoRelatedAdapter(context, productInfoList);
            viewPager.setAdapter(videoRelatedAdapter);
            viewPager.setPageMargin(AutoSizeUtils.mm2px(mAppContext, 30));
        }
        //点击事件
        helper.addOnClickListener(R.id.iv_close)
                .addOnClickListener(R.id.ll_hot);
    }
}
