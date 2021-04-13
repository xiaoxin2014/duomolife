package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.bean.MyQuestionEntity.MyQuestionBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2021/4/8
 * Version:v5.1.0
 * ClassDescription :与我相关问题列表适配器
 */
public class MyQuestionListAdapter extends BaseQuickAdapter<MyQuestionBean, BaseViewHolder> {
    private Activity context;

    public MyQuestionListAdapter(Activity context, @Nullable List<MyQuestionBean> data) {
        super(R.layout.item_my_question, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyQuestionBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), item.getProductImg());
        helper.setText(R.id.tv_name, getStrings(item.getProductName()))
                .setGone(R.id.ll_delete, item.isShowDel())
                .setText(R.id.tv_question, item.getContent())
                .setText(R.id.tv_answer, item.getReplyContent())
                .setGone(R.id.ll_answer, item.getReplyCount() > 0)
                .setText(R.id.tv_reply_num, item.getReplyCount() > 0 ? ConstantMethod.getIntegralFormat(context, R.string.all_reply_num, item.getReplyCount()) : "暂无回答")
                .setTextColor(R.id.tv_reply_num, context.getResources().getColor(item.getReplyCount() > 0 ? R.color.text_normal_red : R.color.text_login_gray_s))
                .setEnabled(R.id.tv_reply_num, item.getReplyCount() > 0)
                .addOnClickListener(R.id.ll_delete).setTag(R.id.ll_delete, item)
                .addOnClickListener(R.id.ll_question).setTag(R.id.ll_question, item)
                .setEnabled(R.id.ll_question, "1".equals(item.getStatus()));
        helper.itemView.setTag(item);
    }
}
