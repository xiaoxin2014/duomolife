package com.amkj.dmsh.time.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.find.bean.PostEntity.PostBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.skipTimePostDetail;
import static com.amkj.dmsh.dao.AddClickDao.addTimePostClick;

/**
 * Created by xiaoxin on 2019/7/9
 * Version:v4.8.0
 * ClassDescription :淘好货首页-帖子列表适配器
 */
public class TimePostAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {

    private Activity context;

    public TimePostAdapter(Activity context, @Nullable List<PostBean> data, boolean isHome) {
        super(isHome ? R.layout.item_time_post : R.layout.item_time_post_padding, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_cover), item.getCover());
        helper.setGone(R.id.tv_content, !TextUtils.isEmpty(item.getContent()))
                .setGone(R.id.iv_cover, !TextUtils.isEmpty(item.getCover()));

        TextView tvContent = helper.getView(R.id.tv_content);
        //跨境标识
        if (!TextUtils.isEmpty(item.getTagName())) {
            Link link = new Link("\t" + item.getTagName() + "\t");
            link.setTextColor(Color.parseColor("#ffffff"));
            link.setTextSize(AutoSizeUtils.mm2px(mAppContext, 22));
            link.setBgColor(Color.parseColor("#FF9EA6"));
            link.setBgRadius(AutoSizeUtils.mm2px(context, 5));
            link.setUnderlined(false);
            link.setHighlightAlpha(0f);
            CharSequence charSequence = LinkBuilder.from(context, link.getText() + " " + item.getContent())
                    .addLink(link)
                    .build();
            tvContent.setText(charSequence);
        } else {
            tvContent.setText(item.getContent());
        }

        tvContent.setMaxLines(!TextUtils.isEmpty(item.getCover()) ? 2 : 5);//纯文本最多显示5行，否则2行
        helper.itemView.setOnClickListener(v -> {
            skipTimePostDetail(context, String.valueOf(item.getId()));
            //统计点击量
            addTimePostClick(context, item.getId(), 1);
        });
        helper.itemView.setTag(item);
    }
}
