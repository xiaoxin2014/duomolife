package com.amkj.dmsh.release.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.release.bean.ImagePathBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_ADD_IMG;

/**
 * Created by dq on 2016/1/26.
 */
public class ImgGArticleRecyclerAdapter extends BaseQuickAdapter<ImagePathBean, BaseViewHolder> {
    private final Context context;

    public ImgGArticleRecyclerAdapter(Context context, List<ImagePathBean> pathList) {
        super(R.layout.img_grid_article_item, pathList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ImagePathBean imagePathBean) {
        ImageView image = helper.getView(R.id.pv_image);
        helper.addOnClickListener(R.id.delete).setTag(R.id.delete, helper.getAdapterPosition());
        if (DEFAULT_ADD_IMG.equals(imagePathBean.getPath())) {
            image.setImageResource(R.drawable.plus_icon_nor);
        } else {
            GlideImageLoaderUtil.loadCenterCrop(context, image, imagePathBean.getPath());
        }
        //        是否显示删除图标
        helper.setGone(R.id.delete, !DEFAULT_ADD_IMG.equals(imagePathBean.getPath()) && imagePathBean.isShowDelIcon());
    }
}
