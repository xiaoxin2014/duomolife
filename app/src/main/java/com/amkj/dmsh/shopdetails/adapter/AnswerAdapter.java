package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.activity.QuestionsEntity.ResultBean.ReplyBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2021/4/9
 * Version:v5.1.0
 * ClassDescription :问题详情-回答列表
 */
public class AnswerAdapter extends BaseQuickAdapter<ReplyBean, BaseViewHolder> {
    private final Activity context;

    public AnswerAdapter(Activity context, @Nullable List<ReplyBean> data) {
        super(R.layout.item_answer, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.iv_head), item.getAvatar(), AutoSizeUtils.mm2px(context, 30), R.drawable.default_ava_img);
        helper.setText(R.id.tv_name, getStrings(item.getNickName()))
                .setImageResource(R.id.iv_user_type, item.isAdmin() ? R.drawable.seller : R.drawable.buyer)
                .setGone(R.id.ll_delete, item.isShowDel())
                .setText(R.id.tv_answer, item.getReplyContent())
                .setText(R.id.tv_favor, item.getLikeCount() > 0 ? String.valueOf(item.getLikeCount()) : "有用")
                .addOnClickListener(R.id.ll_delete).setTag(R.id.ll_delete, item)
                .addOnClickListener(R.id.tv_favor).setTag(R.id.tv_favor, item);
        helper.getView(R.id.tv_favor).setSelected(item.isLike());
    }
}
