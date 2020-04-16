package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.EditorCommentEntity.EditorCommentBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.TimeUtils.getDateFormat;

/**
 * Created by xiaoxin on 2019/3/16 0016
 * Version：V3.3.0
 * class description:小编精选留言适配器
 */
public class EditorCommentAdapter extends BaseQuickAdapter<EditorCommentBean, BaseViewHolder> {
    private Activity context;

    public EditorCommentAdapter(Activity context, int layoutResId, @Nullable List<EditorCommentBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EditorCommentBean item) {
        if (item != null) {
            GlideImageLoaderUtil.loadRoundImg(context, helper.getView(R.id.iv_head), item.getAvatar(), AutoSizeUtils.mm2px(context, 70), R.drawable.default_ava_img);
            helper.setText(R.id.tv_name, getStrings(item.getNickname()))
                    .setText(R.id.tv_time, getDateFormat(item.getCreateTime(), ""))
                    .setText(R.id.tv_content, getStrings(item.getContent()))
                    .setText(R.id.tv_like, item.getLikeString());
            ConstantMethod.setTextLink(context, helper.getView(R.id.tv_content));
            TextView tvLike = helper.getView(R.id.tv_like);
            tvLike.setSelected(item.getIsFavor());
            helper.addOnClickListener(R.id.tv_like).setTag(R.id.tv_like, item);
            helper.itemView.setTag(item);
        }
    }
}
