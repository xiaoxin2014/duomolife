package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.mine.bean.MineBgImgEntity.MineBgImgBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/26
 * class description:背景图片
 */

public class PersonalBgImgSelAdapter extends BaseQuickAdapter<MineBgImgBean, BaseViewHolderHelper> {
    private final Context context;

    public PersonalBgImgSelAdapter(Context context, List<MineBgImgBean> data) {
        super(R.layout.adapter_mine_bg_img, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, MineBgImgBean mineBgImgBean) {
        GlideImageLoaderUtil.loadFitCenter(context, (ImageView) helper.getView(R.id.iv_mine_bg_img), mineBgImgBean.getBgimg_url());
        helper.itemView.setTag(mineBgImgBean);
    }
}
