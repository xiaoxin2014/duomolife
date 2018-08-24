package com.amkj.dmsh.message.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.message.bean.MessageIndentEntity.MessageIndentBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:请输入类描述
 */

public class MessageIndentAdapter extends BaseQuickAdapter<MessageIndentBean, BaseViewHolderHelper> {
    private final Context context;

    public MessageIndentAdapter(Context context, List<MessageIndentBean> officialBeanList) {
        super(R.layout.adapter_mes_indent, officialBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, MessageIndentBean messageIndentBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_mes_indent_cover), messageIndentBean.getJson() != null ? messageIndentBean.getJson().getProduct_img_url() : "");
        helper.setText(R.id.tv_mes_indent_time, getStrings(messageIndentBean.getCtime()))
                .setText(R.id.tv_mes_indent_status, getStrings(messageIndentBean.getM_title()))
                .setText(R.id.tv_mes_indent_content, getStrings(messageIndentBean.getM_content()));
        helper.itemView.setTag(messageIndentBean);
    }
}
