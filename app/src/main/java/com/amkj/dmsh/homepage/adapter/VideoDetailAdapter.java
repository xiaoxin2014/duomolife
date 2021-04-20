package com.amkj.dmsh.homepage.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.dao.SoftApiDao;
import com.amkj.dmsh.find.bean.PostEntity;
import com.amkj.dmsh.homepage.activity.VideoDetailActivity;
import com.amkj.dmsh.homepage.bean.VideoDetailEntity.VideoDetailBean;
import com.amkj.dmsh.homepage.view.JzvdStdTikTok;
import com.amkj.dmsh.views.alertdialog.TimePostCommentPw;
import com.amkj.dmsh.views.indicator.BannerIndicator;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.XPopup;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantVariable.COMMENT_VIDEO_TYPE;

/**
 * Created by xiaoxin on 2021/2/26
 * Version:v5.0.0
 * ClassDescription :视频详情列表
 */
public class VideoDetailAdapter extends BaseQuickAdapter<VideoDetailBean, BaseViewHolder> {
    private BaseActivity context;

    public VideoDetailAdapter(BaseActivity context, @Nullable List<VideoDetailBean> data) {
        super(R.layout.item_video_detail, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDetailBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_favor, String.valueOf(item.getFavorNum()))
                .setText(R.id.tv_message, item.getCommentNum())
                .setText(R.id.tv_share, item.getShareNum())
                .setText(R.id.tv_content, item.getContent());
        //点赞
        TextView tvFavor = helper.getView(R.id.tv_favor);
        tvFavor.setSelected(item.isCollect());
        tvFavor.setText(item.getFavorNum() > 0 ? String.valueOf(item.getFavorNum()) : "赞");
        ImageView ivFavor = helper.getView(R.id.iv_favor);
        ivFavor.setSelected(item.isCollect());
        ivFavor.setOnClickListener(v -> {
            SoftApiDao.collectVideo(context, item, tvFavor);
            ivFavor.setSelected(item.isCollect());
        });

        //留言
        helper.getView(R.id.iv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostEntity.PostBean postBean = new PostEntity.PostBean();
                postBean.setIsFavor(item.isCollect());
                postBean.setFavorNum(item.getFavorNum());
                postBean.setId(getStringChangeIntegers(item.getId()));
                postBean.setAuthorUid(113);
                lookAllComment(postBean);
            }
        });

        //分享
        helper.getView(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UMShareAction(context, item.getCoverPath(), item.getTitle(), item.getContent(), item.getVideoPath(), getStringChangeIntegers(item.getId()));
            }
        });

        //视频播放
        ViewPager viewPager = helper.getView(R.id.vp_goods);
        LinearLayout lLGoods = helper.getView(R.id.ll_goods);
        JZDataSource jzDataSource = new JZDataSource(item.getVideoPath(), item.getTitle());
        jzDataSource.looping = true;
        JzvdStdTikTok jzvdStdTikTok = helper.getView(R.id.videoplayer);
        jzvdStdTikTok.setUp(jzDataSource, Jzvd.SCREEN_NORMAL);
        Glide.with(context).load(item.getCoverPath()).into(jzvdStdTikTok.posterImageView);
        jzvdStdTikTok.setOnStartClickListener(showProduct -> {
            lLGoods.setVisibility(showProduct ? View.VISIBLE : View.INVISIBLE);
            item.setShowProduct(showProduct);
            if (context instanceof VideoDetailActivity) {
                ((VideoDetailActivity) context).setTop(showProduct);
            }
        });
        jzvdStdTikTok.bottomContainer.setVisibility(item.isShowProduct() ? View.GONE : View.VISIBLE);

        //关联商品列表
        lLGoods.setVisibility(item.isShowProduct() ? View.VISIBLE : View.INVISIBLE);
        List<VideoDetailBean.ProductInfoListBean> productInfoList = item.getProductInfoList();
        if (productInfoList != null && productInfoList.size() > 0) {
            VideoRelatedAdapter videoRelatedAdapter = new VideoRelatedAdapter(context, productInfoList);
            viewPager.setAdapter(videoRelatedAdapter);
            viewPager.setPageMargin(AutoSizeUtils.mm2px(mAppContext, 30));
        }
        BannerIndicator bannerIndicator = helper.getView(R.id.indicator);
        bannerIndicator.setUpWidthViewPager(viewPager);
        //点击事件
        helper.addOnClickListener(R.id.iv_close)
                .addOnClickListener(R.id.ll_hot);
    }


    private void lookAllComment(PostEntity.PostBean item) {
        new XPopup.Builder(context)
                .hasShadowBg(false)
                .navigationBarColor(mContext.getResources().getColor(R.color.white))
                .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                .enableDrag(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCustom(new TimePostCommentPw(context, item, COMMENT_VIDEO_TYPE))
                .show();
    }
}
