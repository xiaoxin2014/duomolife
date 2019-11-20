package com.amkj.dmsh.constant;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerStatus;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.Holder;

import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/10
 * version 3.1.13
 * class description:广告轮播图片 视频播放
 */
public class CommunalAdHolderView extends Holder<CommunalADActivityBean> {
    private Activity context;
    private boolean isShowProduct;
    private ConvenientBanner convenientBanner;
    private ImageView iv_ad_image;
    private JzVideoPlayerStatus jvp_ad_video_play;

    public CommunalAdHolderView(View itemView, Activity context, boolean isShowProduct) {
        super(itemView);
        this.context = context;
        this.isShowProduct = isShowProduct;
    }

    public CommunalAdHolderView(View itemView, Activity context, ConvenientBanner convenientBanner, boolean isShowProduct) {
        super(itemView);
        this.context = context;
        this.convenientBanner = convenientBanner;
        this.isShowProduct = isShowProduct;
    }

    @Override
    protected void initView(View itemView) {
        iv_ad_image = itemView.findViewById(R.id.iv_ad_image);
        jvp_ad_video_play = itemView.findViewById(R.id.jvp_ad_video_play);
    }

    @Override
    public void updateUI(CommunalADActivityBean communalADActivityBean) {
        if (!TextUtils.isEmpty(communalADActivityBean.getVideoUrl())) {
            jvp_ad_video_play.setVisibility(View.VISIBLE);
            iv_ad_image.setVisibility(View.GONE);
            GlideImageLoaderUtil.loadCenterCrop(context, jvp_ad_video_play.thumbImageView, getStrings(communalADActivityBean.getPicUrl()));
            jvp_ad_video_play.setVideoSkipData(getStrings(communalADActivityBean.getVideoUrl()), getStrings(communalADActivityBean.getAndroidLink()));
            jvp_ad_video_play.setVideoStatusListener(new JzVideoPlayerStatus.VideoStatusListener() {
                @Override
                public void startTurning() {
                    //视频暂停时恢复翻页
                    if (convenientBanner != null) {
                        convenientBanner.startTurning();
                        convenientBanner.setPointViewVisible(true);
                    }

                }

                @Override
                public void stopTurning() {
                    //视频播放时停止翻页
                    if (convenientBanner != null) {
                        convenientBanner.stopTurning();
                        convenientBanner.setPointViewVisible(false);
                    }
                }
            });
            if (!isShowProduct) {
                jvp_ad_video_play.setVideoSkipData(getStrings(communalADActivityBean.getVideoUrl()), "");
                if (NetWorkUtils.isWifiByType(context)) {
                    jvp_ad_video_play.setVolumeOn(false);
                    jvp_ad_video_play.startButton.performClick();
                }
            }
        } else {
            jvp_ad_video_play.setVisibility(View.GONE);
            iv_ad_image.setVisibility(View.VISIBLE);
            if (isShowProduct) {
                iv_ad_image.setTag(R.id.iv_tag, communalADActivityBean);
                iv_ad_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommunalADActivityBean communalADActivity = (CommunalADActivityBean) v.getTag(R.id.iv_tag);
                        if (communalADActivity != null) {
                            adClickTotal(context, communalADActivityBean.getId());
                            setSkipPath(context, communalADActivity.getAndroidLink(), true, false);
                        }
                    }
                });
            }
            GlideImageLoaderUtil.loadCenterCrop(context, iv_ad_image, communalADActivityBean.getPicUrl());
        }
    }
}
