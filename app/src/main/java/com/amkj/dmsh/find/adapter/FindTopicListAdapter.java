package com.amkj.dmsh.find.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.FindHotTopicEntity.FindHotTopicBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/11/22
 * class description:发现-话题列表
 */

public class FindTopicListAdapter extends BaseQuickAdapter<FindHotTopicBean, BaseViewHolder> {
    private final Context context;
    private final String hotString = "热门";

    public FindTopicListAdapter(Context context, List<FindHotTopicBean> data) {
        super(R.layout.adapter_find_topic, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FindHotTopicBean findHotTopicBean) {
        GlideImageLoaderUtil.loadRoundImg(context, (ImageView) helper.getView(R.id.iv_round_find_topic)
                , getStrings(findHotTopicBean.getImg_url()),AutoSizeUtils.mm2px(mAppContext,4));
        TextView tv_find_topic_title = helper.getView(R.id.tv_find_topic_title);
        String topicTitle = String.format(context.getResources().getString(R.string.topic_format)
                ,getStrings(findHotTopicBean.getTitle())) + (findHotTopicBean.getIstop() == 1 ? hotString : "");
        tv_find_topic_title.setText(topicTitle);
        Link link = new Link(hotString);
        link.setTextColor(Color.parseColor("#ffffff"));
        link.setUnderlined(false);
        link.setHighlightAlpha(0f);
        link.setBgColor(Color.parseColor("#ff5e6b"));
        link.setBgRadius(5);
        link.setTextSize(AutoSizeUtils.mm2px(mAppContext,18));
        LinkBuilder.on(tv_find_topic_title)
                .setText(topicTitle)
                .addLink(link)
                .build();

        helper.setText(R.id.tv_find_topic_join_number, findHotTopicBean.getParticipants_number() + "人参与")
                .itemView.setTag(findHotTopicBean);
    }
}
