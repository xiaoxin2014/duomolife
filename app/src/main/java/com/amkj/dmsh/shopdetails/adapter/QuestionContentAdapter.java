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
 * ClassDescription :商品详情-问答列表适配器
 */
public class QuestionContentAdapter extends BaseQuickAdapter<QuestionInfoBean, BaseViewHolder> {
    private String productId;
    private Activity context;

    public QuestionContentAdapter(Activity context, @Nullable List<QuestionInfoBean> data, String productId) {
        super(R.layout.item_question_content, data);
        this.context = context;
        this.productId = productId;
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionInfoBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_question, item.getContent())
                .setText(R.id.tv_reply_num, item.getReplyCount() > 0 ? ConstantMethod.getIntegralFormat(context, R.string.reply_num, item.getReplyCount()) : "暂无回答");
        helper.itemView.setOnClickListener(v -> skipQuestionDetail(context, item.getQuestionId(), productId));
    }
}
