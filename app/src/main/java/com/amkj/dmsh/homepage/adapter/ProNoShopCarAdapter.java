package com.amkj.dmsh.homepage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean.MarketLabelBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/27
 * class description:无需加入购物车
 */

public class ProNoShopCarAdapter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolder> {
    private final Context context;

    public ProNoShopCarAdapter(Context context, List<LikedProductBean> productSearList) {
        super(productSearList);
        addItemType(TYPE_0, R.layout.adapter_layout_qt_pro);
//        头部标题栏
        addItemType(TYPE_1, R.layout.adapter_ql_goods_pro_header);
//        搜索无结果
        addItemType(TYPE_2, R.layout.adapter_search_pro_empty);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        switch (helper.getItemViewType()) {
            case TYPE_1:
                helper.setText(R.id.tv_pro_title, "- 同类热销商品 -");
                break;
            case TYPE_2:
                helper.setText(R.id.tv_search_empty_title, String.format(context.getString(R.string.search_pro_empty), getStrings(likedProductBean.getTitle())))
                        .addOnClickListener(R.id.tv_search_empty_more);
                break;
            case TYPE_0:
                GlideImageLoaderUtil.loadThumbCenterCrop(context, (ImageView) helper.getView(R.id.iv_qt_pro_img)
                        , likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), true);
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_qt_pro_name, !TextUtils.isEmpty(likedProductBean.getName()) ?
                                getStrings(likedProductBean.getName()) : getStrings(likedProductBean.getTitle()))
                        .setText(R.id.tv_qt_pro_price, "￥" + likedProductBean.getPrice())
                        .setGone(R.id.tv_qt_pro_wait_buy, getStrings(likedProductBean.getSellStatus()).equals("待售"))
                        .setGone(R.id.tv_qt_pro_activity_tag, !TextUtils.isEmpty(likedProductBean.getTagContent()))
                        .setText(R.id.tv_qt_pro_activity_tag, getStrings(likedProductBean.getTagContent()))
                        .setText(R.id.tv_qt_pro_descrip, getStrings(likedProductBean.getSubtitle()))
                        .setGone(R.id.iv_pro_add_car, false)
                        .setGone(R.id.tv_communal_pro_red_tag, !TextUtils.isEmpty(likedProductBean.getActivityTag()))
                        .setText(R.id.tv_communal_pro_red_tag, getStrings(likedProductBean.getActivityTag()));
                FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
                if(likedProductBean.getMarketLabelList()!=null
                        &&likedProductBean.getMarketLabelList().size()>0){
                    fbl_market_label.setVisibility(View.VISIBLE);
                    fbl_market_label.removeAllViews();
                    for (MarketLabelBean marketLabelBean:likedProductBean.getMarketLabelList()) {
                        fbl_market_label.addView(createLabelText(marketLabelBean));
                    }
                }else{
                    fbl_market_label.setVisibility(View.GONE);
                }
                helper.itemView.setTag(likedProductBean);
                break;
        }
    }

    private View createLabelText(MarketLabelBean marketLabelBean) {
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        int tenLeftRight = AutoSizeUtils.mm2px(context, 10);
        int fiveTopBottom = AutoSizeUtils.mm2px(context, 5);
        textView.setPadding(tenLeftRight,fiveTopBottom,tenLeftRight,fiveTopBottom);
        textView.setTextColor(context.getResources().getColor(R.color.white));
        textView.setBackgroundColor(context.getResources().getColor(R.color.text_pink_red));
        textView.setText(getStrings(marketLabelBean.getTitle()));
        textView.setTextSize(AutoSizeUtils.mm2px(context,22));
        return textView;
    }
}
