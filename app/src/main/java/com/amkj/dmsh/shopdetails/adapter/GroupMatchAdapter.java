package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.amkj.dmsh.shopdetails.bean.PropsBean;
import com.amkj.dmsh.shopdetails.bean.PropvaluesBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;

/**
 * Created by xiaoxin on 2019/5/30
 * Version:v4.1.0
 * ClassDescription :组合搭配列表适配器
 */
public class GroupMatchAdapter extends BaseQuickAdapter<CombineCommonBean, BaseViewHolder> {

    private final Activity context;

    public GroupMatchAdapter(Activity activity, @Nullable List<CombineCommonBean> data) {
        super(R.layout.item_group_match, data);
        context = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CombineCommonBean item) {
        if (item == null || item.getSkuSale() == null || item.getSkuSale().size() < 1) return;
        GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_cover_right), item.getPicUrl());
        helper.setGone(R.id.iv_com_pro_tag_out, item.getStock() < 1)
                .setText(R.id.tv_save_price, item.getTag())
                .setGone(R.id.tv_save_price, !item.isMainProduct())
                .setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_min_price, getRmbFormat(context, item.getMinPrice()))
                .setText(R.id.tv_max_price, getRmbFormat(context, "~" + "¥" + item.getMaxPrice(), false))
                //最低价！=最高价并且sku没有选中才会显示最高价
                .setGone(R.id.tv_max_price, (!TextUtils.isEmpty(item.getMaxPrice()) && !item.getMaxPrice().equals(item.getMinPrice())))
                .addOnClickListener(R.id.rl_cover).setTag(R.id.rl_cover, item)
                .setEnabled(R.id.tv_select_sku, item.getSkuSale().size() > 1)
                .addOnClickListener(R.id.tv_select_sku).setTag(R.id.tv_select_sku, item)
                .setEnabled(R.id.tv_shop_car_sel, !item.isMainProduct() && item.getStock() > 0);//主商品和无库存商品不可编辑

        TextView tvSku = helper.getView(R.id.tv_select_sku);
        //只有一个sku默认直接显示
        List<SkuSaleBean> skuSaleList = item.getSkuSale();
        if (item.getSkuSale().size() == 1) {
            setSkuValue(item, skuSaleList.get(0), tvSku);
        } else {
            //多个sku且没有选择任何sku时
            if (item.getSkuId() == 0) {
                tvSku.setText("请选择规格");
            } else {
                for (SkuSaleBean skuSaleBean : skuSaleList) {
                    if (item.getSkuId() == skuSaleBean.getId()) {
                        setSkuValue(item, skuSaleBean, tvSku);
                        break;
                    }
                }
            }
        }

        TextView tvSelect = helper.getView(R.id.tv_shop_car_sel);
        tvSelect.setSelected(item.isSelected());
        helper.itemView.setTag(item);
    }

    private void setSkuValue(CombineCommonBean item, SkuSaleBean skuSaleBean, TextView tvSku) {
        String skuValue = "";
        String[] spuArray = skuSaleBean.getPropValues().split(",");
        List<PropvaluesBean> propvalues = item.getPropvalues();
        List<PropsBean> props = item.getProps();
        for (int j = 0; j < spuArray.length; j++) {
            String propName = props.get(j).getPropName();
            for (PropvaluesBean propvalue : propvalues) {
                if (spuArray[j].equals(String.valueOf(propvalue.getPropValueId()))) {
                    String propValueName = propvalue.getPropValueName();
                    skuValue = skuValue + (TextUtils.isEmpty(skuValue) ? "" : ",") + propName + ":" + propValueName;
                    break;
                }
            }
        }
        tvSku.setText(skuValue);
    }
}
