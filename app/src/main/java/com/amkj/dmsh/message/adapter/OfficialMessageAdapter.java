package com.amkj.dmsh.message.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.message.bean.MessageOfficialEntity.MessageOfficialBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by atd48 on 2016/7/11.
 */
public class OfficialMessageAdapter extends BaseQuickAdapter<MessageOfficialBean, BaseViewHolderHelper> {
    private final Context context;

    public OfficialMessageAdapter(Context context, List<MessageOfficialBean> messageArticleList) {
        super(R.layout.adapter_message_official, messageArticleList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, MessageOfficialBean messageOfficialBean) {
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.img_message_official), messageOfficialBean.getCover_url());
        helper.itemView.setTag(messageOfficialBean);
    }
}
