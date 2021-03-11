package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.CommonPagerAdapter;
import com.amkj.dmsh.base.ViewHolder;
import com.amkj.dmsh.bean.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.homepage.bean.VideoDetailEntity.VideoDetailBean.ProductInfoListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.dao.OrderDao.addShopCarGetSku;

/**
 * Created by xiaoxin on 2021/2/24
 * Version:v5.0.0
 * ClassDescription :视频关联商品适配器
 */
public class VideoRelatedAdapter extends CommonPagerAdapter<ProductInfoListBean> {

    private final Activity mContext;

    public VideoRelatedAdapter(Activity context, List<ProductInfoListBean> datas) {
        super(context, datas, R.layout.item_video_related);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder helper, int position, ProductInfoListBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(mContext, helper.getView(R.id.iv_cover), item.getCoverPath());
        helper.setText(R.id.tv_title, getStrings(item.getTitle()));
        helper.setText(R.id.tv_price, ConstantMethod.getRmbFormat(mContext, item.getPrice()) + "起");
        helper.setText(R.id.tv_price, ConstantMethod.getRmbFormat(mContext, item.getPrice()));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                baseAddCarProInfoBean.setProductId(item.getId());
                baseAddCarProInfoBean.setProName(item.getTitle());
                baseAddCarProInfoBean.setProPic(item.getCoverPath());
                baseAddCarProInfoBean.setShowSingle(false);
                addShopCarGetSku(mContext, baseAddCarProInfoBean);
            }
        });
    }
}
