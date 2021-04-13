package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.activity.QuestionsEntity.ResultBean.QuestionInfoBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.skipQuestionDetail;

/**
 * Created by xiaoxin on 2021/4/8
 * Version:v5.1.0
 * ClassDescription :问大家-问题列表适配器
 */
public class QuestionListAdapter extends BaseQuickAdapter<QuestionInfoBean, BaseViewHolder> {
    private Activity context;
    private String productId;

    public QuestionListAdapter(Activity context, @Nullable List<QuestionInfoBean> data, String productId) {
        super(R.layout.item_question, data);
        this.context = context;
        this.productId = productId;
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionInfoBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_question, item.getContent())
                .setText(R.id.tv_answer, item.getReplyContent())
                .setGone(R.id.ll_answer, item.getReplyCount() > 0)
                .setText(R.id.tv_reply_num, item.getReplyCount() > 0 ? ConstantMethod.getIntegralFormat(context, R.string.all_reply_num, item.getReplyCount()) : "暂无回答")
                .setTextColor(R.id.tv_reply_num, context.getResources().getColor(item.getReplyCount() > 0 ? R.color.text_normal_red : R.color.text_login_gray_s));
        helper.itemView.setOnClickListener(v -> skipQuestionDetail(context, item.getQuestionId(), productId));
    }
}
