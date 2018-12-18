package com.amkj.dmsh.constant;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerStatus;
import com.bigkoo.convenientbanner.holder.Holder;

import org.greenrobot.eventbus.EventBus;

import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/10
 * version 3.1.13
 * class description:广告轮播图片 视频播放
 */
public class CommunalAdHolderView extends Holder<CommunalADActivityBean> {
    private final Context context;
    private final boolean isShowProduct;
    private ImageView iv_ad_image;
    private JzVideoPlayerStatus jvp_ad_video_play;

    public CommunalAdHolderView(View itemView, Context context,boolean isShowProduct) {
        super(itemView);
        this.context = context;
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
            if(isShowProduct){
                jvp_ad_video_play.setVideoStatusListener(new JzVideoPlayerStatus.VideoStatusListener() {

                    @Override
                    public void startTurning() {
                        EventBus.getDefault().post(new EventMessage(START_AUTO_PAGE_TURN,START_AUTO_PAGE_TURN));
                    }

                    @Override
                    public void stopTurning() {
                        EventBus.getDefault().post(new EventMessage(STOP_AUTO_PAGE_TURN,STOP_AUTO_PAGE_TURN));
                    }
                });
            }else{
                jvp_ad_video_play.setVideoSkipData(getStrings(communalADActivityBean.getVideoUrl()), "");
            }
        } else {
            jvp_ad_video_play.setVisibility(View.GONE);
            iv_ad_image.setVisibility(View.VISIBLE);
            if(isShowProduct){
                iv_ad_image.setTag(R.id.iv_tag, communalADActivityBean);
                iv_ad_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommunalADActivityBean communalADActivity = (CommunalADActivityBean) v.getTag(R.id.iv_tag);
                        if (communalADActivity != null) {
                            adClickTotal(communalADActivityBean.getId());
                            setSkipPath(v.getContext(), communalADActivity.getAndroidLink(),true, false);
                        }
                    }
                });
            }
            GlideImageLoaderUtil.loadCenterCrop(context, iv_ad_image, communalADActivityBean.getPicUrl());
        }
    }
}
