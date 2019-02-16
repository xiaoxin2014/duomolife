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
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean.MemberListBean;
import com.amkj.dmsh.find.activity.FindTagDetailsActivity;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean.TagsBean;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import java.lang.ref.WeakReference;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/10/18
 * version 3.1.8
 * class description:标签创建
 */
public class ProductLabelCreateUtils implements View.OnClickListener {
    private static volatile ProductLabelCreateUtils productLabelCreateUtils;
    private WeakReference<Context> contextWeakReference;

    private ProductLabelCreateUtils() {
    }

    public static ProductLabelCreateUtils getLabelInstance() {
        if (productLabelCreateUtils == null) {
            synchronized (ProductLabelCreateUtils.class) {
                if (productLabelCreateUtils == null) {
                    productLabelCreateUtils = new ProductLabelCreateUtils();
                }
            }
        }
        return productLabelCreateUtils;
    }

    /**
     * 商品列表
     *
     * @param context
     * @param labelText
     * @param labelCode 1 为红色 活动 0 为黄色 营销
     * @return
     */
    public TextView createLabelText(Context context, String labelText, int labelCode) {
        contextWeakReference = new WeakReference<>(context);
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        int tenLeftRight = AutoSizeUtils.mm2px(context, 7);
        int fiveTopBottom = AutoSizeUtils.mm2px(context, 3);
        textView.setPadding(tenLeftRight, fiveTopBottom, tenLeftRight, fiveTopBottom);
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (labelCode == 1) {
            gradientDrawable.setColor(context.getResources().getColor(R.color.text_pink_red));
        } else {
            gradientDrawable.setColor(context.getResources().getColor(R.color.text_yel_f_s));
        }
        gradientDrawable.setCornerRadius(AutoSizeUtils.mm2px(context, 4));
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setBackground(gradientDrawable);
        textView.setText(labelText);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, 22));
        return textView;
    }

    /**
     * 商品详情可点击 营销标签
     *
     * @param context
     * @param marketLabelBean 活动 营销
     * @return
     */
    public TextView createLabelClickText(Context context, MarketLabelBean marketLabelBean) {
        contextWeakReference = new WeakReference<>(context);;
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        int tenLeftRight = AutoSizeUtils.mm2px(context, 15);
        int fiveTopBottom = AutoSizeUtils.mm2px(context, 7);
        textView.setPadding(tenLeftRight, fiveTopBottom, tenLeftRight, fiveTopBottom);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(context.getResources().getColor(R.color.white));
        int colorValue;
        if (marketLabelBean.getLabelCode() == 1) {
            colorValue = context.getResources().getColor(R.color.text_pink_red);
        } else {
            colorValue = context.getResources().getColor(R.color.text_yel_f_s);
        }
        gradientDrawable.setStroke(1, colorValue);
        gradientDrawable.setCornerRadius(AutoSizeUtils.mm2px(context, 4));
        textView.setTextColor(colorValue);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(gradientDrawable);
        textView.setText(getStringsFormat(context, R.string.communal_go_string, getStrings(marketLabelBean.getTitle())));
        textView.setTag(marketLabelBean);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(context, 24));
        textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void onClick(View view) {
        MarketLabelBean marketLabelBean = (MarketLabelBean) view.getTag();
        Context context = contextWeakReference.get();
        if (marketLabelBean != null&&context!=null) {
            Intent intent = new Intent();
            if (marketLabelBean.getId() > 0) {
                intent.setClass(context, ProductLabelDetailActivity.class);
                intent.putExtra("productLabelId", String.valueOf(marketLabelBean.getId()));
            } else if (!TextUtils.isEmpty(marketLabelBean.getActivityCode())) {
                intent.setClass(context, QualityProductActActivity.class);
                intent.putExtra("activityCode", marketLabelBean.getActivityCode());
            } else {
                return;
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 配置商品标签
     *
     * @param context
     * @param productTag
     * @return
     */
    public TextView createProductTag(Context context, String productTag) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_product_tag, null, false);
        textView.setText(getStrings(productTag));
        return textView;
    }

    /**
     * 文章标签图标
     * @param context
     * @return
     */
    public View createArticleIcon(Context context){
        ImageView iv_label = new ImageView(context);
        iv_label.setImageResource(R.drawable.tag_label_icon);
        iv_label.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return iv_label;
    }

    /**
     * 文章标签
     * @param context
     * @param tagsBean
     * @return
     */
    public TextView createArticleClickTag(Context context, TagsBean tagsBean) {
        TextView tv_tag = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_article_tag, null, false);
        int tagPadding = AutoSizeUtils.mm2px(context, 10);
        tv_tag.setPadding(tagPadding,tagPadding,tagPadding,tagPadding);
        tv_tag.setTag(R.id.tag_obj, tagsBean);
        tv_tag.setText(getStrings(tagsBean.getTag_name()));
        tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagsBean tagsBean = (TagsBean) v.getTag(R.id.tag_obj);
                if(tagsBean!=null){
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
     * @param context
     * @param memberListBean
     * @return
     */
    public View createOpenGroupUserInfo(Context context, MemberListBean memberListBean) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_gp_join_avator, null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_dm_gp_open_ava);
        TextView tv_dm_gp_name = (TextView) view.findViewById(R.id.tv_dm_gp_name);
        GlideImageLoaderUtil.loadRoundImg(context, imageView, memberListBean.getAvatar(), AutoSizeUtils.mm2px(mAppContext, 80),R.drawable.default_ava_img);
        String name = getStrings(memberListBean.getNickname());
        if (name.length() > 7) {
            name = name.substring(0, 7) + "...";
        }
        tv_dm_gp_name.setText(name);
        return view;
    }

    /**
     * 配置搜索记录
     * @param context
     * @param historyTag
     * @return
     */
    public TextView createHistorySearchRecord(Context context,String historyTag){
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layoyt_hotsearch_tag, null, false);
        int leftRightValue = AutoSizeUtils.mm2px(context, 10);
        int topBottomValue = AutoSizeUtils.mm2px(context, 7);
        textView.setPadding(leftRightValue, topBottomValue, leftRightValue, topBottomValue);
        textView.setText(getStrings(historyTag));
        textView.setTag(historyTag);
        return textView;
    }
}
