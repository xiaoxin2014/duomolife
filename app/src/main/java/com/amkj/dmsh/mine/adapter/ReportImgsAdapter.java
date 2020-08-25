package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;

/**
 * Created by xiaoxin on 2020/8/22
 * Version:v4.7.0
 */
public class ReportImgsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final Context context;

    public ReportImgsAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_report_img, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.itemView, item);
        showImageActivity(context, IMAGE_DEF, helper.getAdapterPosition(), getData());
    }
}
