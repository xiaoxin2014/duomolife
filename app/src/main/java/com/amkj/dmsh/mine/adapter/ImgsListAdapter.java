package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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
public class ImgsListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;
    private int imgHeight;

    public ImgsListAdapter(Context context, @Nullable List<String> data) {
        this(context, data, 0);
    }

    /**
     * @param imgHeight 图片高度
     */
    public ImgsListAdapter(Context context, @Nullable List<String> data, int imgHeight) {
        super(R.layout.item_report_img, data);
        this.context = context;
        this.imgHeight = imgHeight;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item == null) return;
        ImageView itemView = (ImageView) helper.itemView;
        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.height = imgHeight > 0 ? imgHeight : ViewGroup.LayoutParams.WRAP_CONTENT;
        itemView.setLayoutParams(layoutParams);
        if (imgHeight > 0) {
            GlideImageLoaderUtil.loadCenterCrop(context, itemView, item);
        } else {
            GlideImageLoaderUtil.loadImage(context, itemView, item);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageActivity(context, IMAGE_DEF, helper.getAdapterPosition(), getData());
            }
        });
    }
}
