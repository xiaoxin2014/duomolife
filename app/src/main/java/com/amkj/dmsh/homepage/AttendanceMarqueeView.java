package com.amkj.dmsh.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.AttendanceDetailEntity.LogListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.gongwen.marqueen.MarqueeFactory;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.regex.Pattern;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/20
 * version 3.1.5
 * class description:签到跑马灯
 */
public class AttendanceMarqueeView extends MarqueeFactory<LinearLayout, LogListBean> {

    private final LayoutInflater inflater;

    public AttendanceMarqueeView(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    protected LinearLayout generateMarqueeItemView(LogListBean logListBean) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.layout_attendance_marquee, null);
        AutoUtils.auto(view);
        ImageView iv_attendance_avatar = view.findViewById(R.id.iv_attendance_avatar);
        TextView tv_attendance_nick_name = view.findViewById(R.id.tv_attendance_nick_name);
        TextView tv_attendance_score = view.findViewById(R.id.tv_attendance_score);
        GlideImageLoaderUtil.loadRoundImg(mContext,iv_attendance_avatar,getStrings(logListBean.getAvatar()),AutoUtils.getPercentWidthSize(30),R.drawable.default_ava_img);
        String nickName = getStrings(logListBean.getNickname());
        tv_attendance_nick_name.setText(nickName);
        String integralScore = String.format(mContext.getResources().getString(R.string.integral_score_marquee),logListBean.getScore());
        Link link = new Link(Pattern.compile(REGEX_NUM));
        link.setTextColor(0xfff6c004);
        link.setUnderlined(false);
        link.setHighlightAlpha(0f);
        link.setTextSize(AutoUtils.getPercentWidthSize(30));
        tv_attendance_score.setText(integralScore);
        LinkBuilder.on(tv_attendance_score)
                .setText(integralScore)
                .addLink(link)
                .build();
        return view;
    }
}
