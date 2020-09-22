package com.amkj.dmsh.release.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;

/**
 * Created by xiaoxin on 2020/8/25
 * Version:v4.7.0
 * ClassDescription : 发布报告适配器
 */
public class ReportImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final Context context;

    public ReportImgAdapter(Context context, List<String> pathList) {
        super(R.layout.img_grid_article_item, pathList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String imgPath) {
        ImageView image = helper.getView(R.id.pv_image);
        if (DEFAULT_ADD_IMG.equals(imgPath)) {
            image.setImageResource(R.drawable.plus_icon_nor);
        } else {
            GlideImageLoaderUtil.loadCenterCrop(context, image, imgPath);
        }
        //是否显示删除图标
        helper.setGone(R.id.delete, !DEFAULT_ADD_IMG.equals(imgPath))
                .addOnClickListener(R.id.delete);
    }
}
