package com.amkj.dmsh.release.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by dq on 2016/1/26.
 */
public class ImgGridRecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public static final String DEFAULT_ADD_IMG = "plus_icon_nor.png";
    private final Context context;

    public ImgGridRecyclerAdapter(Context context, ArrayList pathList) {
        super(R.layout.img_grid_item, pathList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView image = helper.getView(R.id.pv_image);
        int adapterPosition = helper.getAdapterPosition();
//        是否显示删除图标
        if (item.equals(DEFAULT_ADD_IMG)) {
            helper.setGone(R.id.delete, false);
            image.setImageResource(R.drawable.plus_icon_nor);
        } else {
            helper.setGone(R.id.delete, true).addOnClickListener(R.id.delete).setTag(R.id.delete, adapterPosition);
            GlideImageLoaderUtil.loadCenterCrop(context, image, "file://" + item);
        }
    }
}
