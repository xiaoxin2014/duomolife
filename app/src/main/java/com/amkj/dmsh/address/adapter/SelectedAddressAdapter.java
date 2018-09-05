package com.amkj.dmsh.address.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity.AddressListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by atd48 on 2016/7/16.
 */
public class SelectedAddressAdapter extends BaseQuickAdapter<AddressListBean, BaseViewHolderHelper> {

    private final boolean isShowDel;

    public SelectedAddressAdapter(List<AddressListBean> addressAllBeanList, boolean isShowDel) {
        super(R.layout.adapter_address_selected_item, addressAllBeanList);
        this.isShowDel = isShowDel;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, AddressListBean addressAllBean) {
        helper.setText(R.id.tv_address_item_perName, getStrings(addressAllBean.getConsignee()))
                .setText(R.id.tv_address_item_perPhone, getStrings(addressAllBean.getMobile()))
                .setText(R.id.tv_address_item_address, getStrings(addressAllBean.getAddress_com() + addressAllBean.getAddress()))
                .setChecked(R.id.cb_address_selected_item, addressAllBean.getStatus() == 1)
                .addOnClickListener(R.id.cb_address_selected_item).setTag(R.id.cb_address_selected_item, addressAllBean)
                .setGone(R.id.tv_address_item_del,isShowDel)
                .addOnClickListener(R.id.tv_address_item_edit).setTag(R.id.tv_address_item_edit, addressAllBean)
                .addOnClickListener(R.id.tv_address_item_del).setTag(R.id.tv_address_item_del, addressAllBean);
        helper.itemView.setTag(addressAllBean);
    }
}
