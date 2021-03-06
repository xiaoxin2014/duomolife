package com.amkj.dmsh.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.activity.ProductLabelDetailActivity;
import com.amkj.dmsh.dominant.activity.QualityNewUserActivity;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.bean.GroupShopDetailsEntity.GroupShopDetailsBean.ParticipantInfoBean.GroupShopJoinBean;
import com.amkj.dmsh.find.activity.FindTagDetailsActivity;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean.TagsBean;
import com.amkj.dmsh.user.bean.MarketLabelBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/18
 * version 3.1.8
 * class description:标签创建
 */
public class ProductLabelCreateUtils {

    /**
     * 商品列表
     *
     * @param context
     * @param labelText
     * @param labelType 0为黄色 营销 1为红色 活动 2会员专享 3优惠券
     * @return
     */
    public static View createLabelText(Context context, String labelText, int labelType) {
        if (labelType == 2) {
            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AutoSizeUtils.mm2px(context, 147), AutoSizeUtils.mm2px(context, 40));
            imageView.setImageResource(R.drawable.vip_tag);
            imageView.setLayoutParams(layoutParams);
            return imageView;
        } else {
            TextView textView = new TextView(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            int tenLeftRight = AutoSizeUtils.mm2px(context, 7);
            textView.setPadding(tenLeftRight, 0, tenLeftRight, 0);
            textView.setTextColor(context.getResources().getColor(R.color.white));
            textView.setText(labelText);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, 22));

            GradientDrawable gradientDrawable = new GradientDrawable();
            if (labelType == 0) {
                gradientDrawable.setColor(context.getResources().getColor(R.color.vip_color));
            } else if (labelType==1){
                gradientDrawable.setColor(context.getResources().getColor(R.color.text_pink_red));
            }else if (labelType==3){
                gradientDrawable.setColor(context.getResources().getColor(R.color.time_show_color));
            }
            gradientDrawable.setCornerRadius(AutoSizeUtils.mm2px(context, 4));
            textView.setBackground(gradientDrawable);
            return textView;
        }
    }

    /**
     * 商品详情可点击 营销标签
     *
     * @param context
     * @param marketLabelBean 活动 营销
     * @return
     */
    public static TextView createLabelClickText(Context context, MarketLabelBean marketLabelBean) {
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WRAP_CONTENT, AutoSizeUtils.mm2px(context, 40));
        textView.setLayoutParams(layoutParams);
        int tenLeftRight = AutoSizeUtils.mm2px(context, 15);
        textView.setPadding(tenLeftRight, 0, tenLeftRight, 0);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(R.color.white));
        int colorValue;
        if (marketLabelBean.getLabelCode() == 1) {
            colorValue = context.getResources().getColor(R.color.text_normal_red);
        } else {
            colorValue = context.getResources().getColor(R.color.text_yel_f_s);
        }
        gradientDrawable.setStroke(2, colorValue);
        gradientDrawable.setCornerRadius(AutoSizeUtils.mm2px(context, 8));
        textView.setTextColor(colorValue);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(gradientDrawable);
        textView.setText(getStrings(marketLabelBean.getTitle()));
        textView.setTag(marketLabelBean);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, 24));
        textView.setOnClickListener(v -> {
            MarketLabelBean marketLabelBean1 = (MarketLabelBean) v.getTag();
            if (marketLabelBean1 != null) {
                Intent intent = new Intent();
                if (marketLabelBean1.getId() > 0) {
                    intent.setClass(context, ProductLabelDetailActivity.class);
                    intent.putExtra("productLabelId", String.valueOf(marketLabelBean1.getId()));
                } else if (!TextUtils.isEmpty(marketLabelBean1.getActivityCode())) {
                    intent.setClass(context, QualityProductActActivity.class);
                    intent.putExtra("activityCode", marketLabelBean1.getActivityCode());
                } else if (marketLabelBean1.isNewUserTag()) {
                    intent.setClass(context, QualityNewUserActivity.class);
                } else {
                    return;
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return textView;
    }


    /**
     * 配置商品标签
     *
     * @param context
     * @param productTag
     * @return
     */
    public static TextView createProductTag(Context context, String productTag) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_product_tag, null, false);
        textView.setText(getStrings(productTag));
        return textView;
    }

    /**
     * 文章标签图标
     *
     * @param context
     * @return
     */
    public static View createArticleIcon(Context context) {
        ImageView iv_label = new ImageView(context);
        iv_label.setImageResource(R.drawable.tag_label_icon);
        iv_label.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        return iv_label;
    }

    /**
     * 文章标签
     *
     * @param context
     * @param tagsBean
     * @return
     */
    public static TextView createArticleClickTag(Context context, TagsBean tagsBean) {
        TextView tv_tag = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_article_tag, null, false);
        int tagPadding = AutoSizeUtils.mm2px(context, 10);
        tv_tag.setPadding(tagPadding, tagPadding, tagPadding, tagPadding);
        tv_tag.setTag(R.id.tag_obj, tagsBean);
        tv_tag.setText(getStrings(tagsBean.getTag_name()));
        tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagsBean tagsBean = (TagsBean) v.getTag(R.id.tag_obj);
                if (tagsBean != null) {
                    Intent intent = new Intent(context, FindTagDetailsActivity.class);
                    intent.putExtra("tagId", String.valueOf(tagsBean.getTag_id()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        return tv_tag;
    }

    /**
     * 配置开团用户信息
     *
     * @param context
     * @param memberListBean
     * @return
     */
    public static View createOpenGroupUserInfo(Context context, GroupShopJoinBean memberListBean) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_gp_join_avator, null, false);
        ImageView imageView = view.findViewById(R.id.iv_dm_gp_open_ava);
        TextView tv_dm_gp_name = view.findViewById(R.id.tv_dm_gp_name);
        TextView tvStatus = view.findViewById(R.id.tv_status);
        GlideImageLoaderUtil.loadRoundImg(context, imageView, memberListBean.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 105), R.drawable.default_ava_img);
        String name = getStrings(memberListBean.getNickName());
        if (name.length() > 5) {
            name = name.substring(0, 5) + "...";
        }
        tv_dm_gp_name.setText(name);
        String statusText = memberListBean.getStatusText();
        tvStatus.setVisibility(!TextUtils.isEmpty(statusText) ? View.VISIBLE : View.GONE);
        tvStatus.setText(statusText);
        return view;
    }

    /**
     * 配置搜索记录
     *
     * @param context
     * @param historyTag
     * @return
     */
    public static TextView createHistorySearchRecord(Context context, String historyTag) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layoyt_hotsearch_tag, null, false);
        int leftRightValue = AutoSizeUtils.mm2px(context, 10);
        int topBottomValue = AutoSizeUtils.mm2px(context, 7);
        textView.setPadding(leftRightValue, topBottomValue, leftRightValue, topBottomValue);
        textView.setText(getStrings(historyTag));
        textView.setTag(historyTag);
        return textView;
    }
}
