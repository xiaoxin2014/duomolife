package com.amkj.dmsh.address.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by atd48 on 2016/7/16.
 */
public class SelectedAddressAdapter extends BaseQuickAdapter<AddressInfoBean, BaseViewHolder> {

    private final boolean isShowDel;
    private final boolean isShowCheckDefault;

    /**
     * @param isShowDel          是否显示删除按钮
     * @param isShowCheckDefault 是否显示选中默认地址按钮
     */
    public SelectedAddressAdapter(List<AddressInfoBean> addressAllBeanList, boolean isShowDel, boolean isShowCheckDefault) {
        super(R.layout.adapter_address_selected_item, addressAllBeanList);
        this.isShowDel = isShowDel;
        this.isShowCheckDefault = isShowCheckDefault;
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressInfoBean addressAllBean) {
        helper.setText(R.id.tv_address_item_perName, getStrings(addressAllBean.getConsignee()))
                .setText(R.id.tv_address_item_perPhone, getStrings(addressAllBean.getMobile()))
                .setText(R.id.tv_address_item_address, getStrings(addressAllBean.getAddress_com() + addressAllBean.getAddress()))
                .setChecked(R.id.cb_address_selected_item, addressAllBean.getStatus() == 1)
                .setVisible(R.id.cb_address_selected_item, isShowCheckDefault)
                .setEnabled(R.id.cb_address_selected_item, isShowCheckDefault)
                .addOnClickListener(R.id.cb_address_selected_item).setTag(R.id.cb_address_selected_item, addressAllBean)
                .setGone(R.id.tv_address_item_del, isShowDel)
                .addOnClickListener(R.id.tv_address_item_edit).setTag(R.id.tv_address_item_edit, addressAllBean)
                .addOnClickListener(R.id.tv_address_item_del).setTag(R.id.tv_address_item_del, addressAllBean);
        helper.itemView.setTag(addressAllBean);
    }
}
