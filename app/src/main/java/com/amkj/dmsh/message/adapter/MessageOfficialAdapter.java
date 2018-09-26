package com.amkj.dmsh.message.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.message.bean.MessageOfficialEntity.MessageOfficialBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/1
 * class description:请输入类描述
 */

public class MessageOfficialAdapter extends BaseQuickAdapter<MessageOfficialBean, BaseViewHolder> {
    private final Context context;

    public MessageOfficialAdapter(Context context, List<MessageOfficialBean> officialBeanList) {
        super(R.layout.adapter_mes_official, officialBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageOfficialBean messageOfficialBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_mes_official_cover), messageOfficialBean.getCover_url());
        helper.setText(R.id.tv_mes_official_time, getStrings(messageOfficialBean.getCreate_time()))
                .setText(R.id.tv_mes_official_title, getStrings(messageOfficialBean.getTitle()))
                .setText(R.id.tv_mes_official_descrip, getStrings(messageOfficialBean.getDescription()))
                .setGone(R.id.tv_mes_official_tag, !messageOfficialBean.isRead());
        helper.itemView.setTag(messageOfficialBean);
    }
}
