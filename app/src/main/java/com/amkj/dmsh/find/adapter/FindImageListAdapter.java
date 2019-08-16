package com.amkj.dmsh.find.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_3;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/12/26
 * version 3.6
 * class description:图片列表
 */

public class FindImageListAdapter extends BaseMultiItemQuickAdapter<PictureBean, BaseViewHolder> {
    private final Context context;

    public FindImageListAdapter(Context context, List<PictureBean> pictureBeanList) {
        super(pictureBeanList);
        addItemType(TYPE_0, R.layout.adapter_image_communal);
        addItemType(TYPE_1, R.layout.adapter_image_width_communal);
        addItemType(TYPE_2, R.layout.adapter_image_more_communal);
//       评价图片 size 160*160
        addItemType(TYPE_3, R.layout.adapter_image_eva_communal);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PictureBean pictureBean) {
        switch (pictureBean.getItemType()) {
            case TYPE_1:
                final ImageView iv_image_details = helper.getView(R.id.iv_image_details);
                iv_image_details.setImageResource(R.drawable.load_loading_image);
                if (!TextUtils.isEmpty(pictureBean.getPath())) {
                    GlideImageLoaderUtil.loadImgDynamicDrawable(context, iv_image_details, pictureBean.getPath(),-1);
                }
                break;
            case TYPE_2:
                helper.itemView.setTag(pictureBean);
                break;
            case TYPE_3:
                final ImageView iv_eva_image = helper.getView(R.id.iv_eva_image);
                iv_eva_image.setImageResource(R.drawable.load_loading_image);
                helper.itemView.setTag(pictureBean);
                if (!TextUtils.isEmpty(pictureBean.getPath())) {
                    GlideImageLoaderUtil.loadThumbCenterCrop(context, iv_eva_image, pictureBean.getPath(),null,false);
                }
                break;
            default:
                GlideImageLoaderUtil.loadThumbCenterCrop(context, (ImageView) helper.getView(R.id.iv_image_path), getStrings(pictureBean.getPath()), null, false);
                helper.itemView.setTag(pictureBean);
                break;
        }
    }
}
