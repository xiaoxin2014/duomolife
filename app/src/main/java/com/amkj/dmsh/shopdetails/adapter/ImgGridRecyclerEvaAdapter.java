package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;

/**
 * Created by dq on 2016/1/26.
 */
public class ImgGridRecyclerEvaAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final Context context;
    private int evaItemPosition;

    public ImgGridRecyclerEvaAdapter(Context context, ArrayList pathList, int adapterPosition) {
        super(R.layout.img_grid_item, pathList);
        evaItemPosition = adapterPosition;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView image = helper.getView(R.id.pv_image);
        int adapterPosition = helper.getAdapterPosition();
//        是否显示删除图标
        if (item.equals(DEFAULT_ADD_IMG)) {
            helper.setGone(R.id.delete, false);
            GlideImageLoaderUtil.loadCenterCrop(context, image, "file:///android_asset/" + item);
        } else {
            helper.setGone(R.id.delete, true).addOnClickListener(R.id.delete)
                    .setTag(R.id.delete, adapterPosition)
                    .setTag(R.id.delete, R.id.img_eva_list, evaItemPosition);
            GlideImageLoaderUtil.loadCenterCrop(context, image, "file://" + item);
        }
        helper.itemView.setTag(evaItemPosition);
    }
}
