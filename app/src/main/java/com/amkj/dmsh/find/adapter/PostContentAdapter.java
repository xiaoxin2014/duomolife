package com.amkj.dmsh.find.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.mine.activity.ZeroReportDetailActivity;
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
import static com.amkj.dmsh.constant.ConstantMethod.skipPostDetail;
import static com.amkj.dmsh.constant.ConstantMethod.skipTopicDetail;
import static com.amkj.dmsh.dao.SoftApiDao.favorPost;
import static com.amkj.dmsh.dao.SoftApiDao.favorReport;

/**
 * Created by xiaoxin on 2019/7/9
 * Version:v4.1.0
 * ClassDescription :帖子列表适配器
 */
public class PostContentAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {

    private final Activity context;
    private final boolean mShowTopictitle;
    private final boolean mShowZeroProduct;//0元商品标志

    public PostContentAdapter(Activity context, @Nullable List<PostBean> data, boolean showTopicTitle) {
        this(context, data, showTopicTitle, false);
    }

    public PostContentAdapter(Activity context, @Nullable List<PostBean> data, boolean showTopicTitle, boolean showZeroProduct) {
        super(R.layout.item_topic_content, data);
        this.context = context;
        mShowTopictitle = showTopicTitle;
        mShowZeroProduct = showZeroProduct;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean item) {
        if (item == null) return;
        ImageView ivCover = helper.getView(R.id.iv_cover);
        if (!TextUtils.isEmpty(item.getCover())) {
            ViewGroup.LayoutParams layoutParams = ivCover.getLayoutParams();
            int coverHeight = item.getCoverHeight();
            int coverWidth = item.getCoverWidth();

            if (item.getCoverHeight() == 0 || item.getCoverWidth() == 0) {
                layoutParams.width = MATCH_PARENT;
                layoutParams.height = AutoSizeUtils.mm2px(mAppContext, 350);
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
            GlideImageLoaderUtil.loadImage(context, ivCover, item.getCover());
        }

        GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.iv_head_icon), item.getAvatar(), AutoSizeUtils.mm2px(context, 40), R.drawable.default_ava_img);
        helper.setText(R.id.tv_topic_name, getStrings(item.getTopicTitle()))
                .setGone(R.id.tv_topic_name, mShowTopictitle && !TextUtils.isEmpty(getStrings(item.getTopicTitle())))
                .setText(R.id.tv_content, getStrings(item.getContent()))
                .setGone(R.id.tv_content, !TextUtils.isEmpty(item.getContent()))
                .setText(R.id.tv_user_name, getStrings(item.getNickName()))
                .setText(R.id.tv_favor, getStrings(String.valueOf(item.getFavorNum() > 0 ? item.getFavorNum() : "赞")))
                .setGone(R.id.iv_cover, !TextUtils.isEmpty(item.getCover()));

        //显示0元试用商品
        if (mShowZeroProduct) {
            helper.getView(R.id.ll_zero_product).setVisibility(View.VISIBLE);
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_zero_cover), item.getProductImg());
            ((TextView) helper.getView(R.id.tv_zero_name)).setText(item.getProductName());
        }

        TextView tvContent = helper.getView(R.id.tv_content);
        tvContent.setMaxLines(!TextUtils.isEmpty(item.getCover()) ? 2 : 5);//纯文本最多显示5行，否则2行
        TextView tvFavor = helper.getView(R.id.tv_favor);
        tvFavor.setSelected(item.isFavor());
        tvFavor.setOnClickListener(v -> {
            if (mShowZeroProduct){
                favorReport(context, item, tvFavor);
            }else {
                favorPost(context, item, tvFavor);
            }
        });

        //跳转话题详情
        helper.getView(R.id.tv_topic_name).setOnClickListener(v -> {
            skipTopicDetail(context, item.getTopicId());
        });
        helper.itemView.setOnClickListener(v -> {
            if (mShowZeroProduct) {
                Intent intent = new Intent(context, ZeroReportDetailActivity.class);
                intent.putExtra("activityId", item.getActivityId());
                intent.putExtra("orderId", item.getOrderId());
                context.startActivity(intent);
            } else {
                skipPostDetail(context, String.valueOf(item.getId()), item.getArticletype());
            }
        });
        helper.itemView.setTag(item);
    }
}
