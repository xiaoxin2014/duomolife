package com.amkj.dmsh.shopdetails.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2020/3/11
 * Version:v4.4.2
 * ClassDescription :发票图片列表
 */
public class InvoiceListAdapter extends BaseQuickAdapter<Bitmap, BaseViewHolder> {

    public InvoiceListAdapter(@Nullable List<Bitmap> data) {
        super(R.layout.adapter_layout_invoice, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bitmap item) {
        if (item == null) return;
        ((ImageView) helper.itemView).setImageBitmap(item);
    }
}
