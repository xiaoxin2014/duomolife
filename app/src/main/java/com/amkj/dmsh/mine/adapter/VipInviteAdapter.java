package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.VipInviteEntity.VipInviteBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by xiaoxin on 2020/8/31
 * Version:v4.7.0
 * ClassDescription :分享有礼-会员邀请列表适配器
 */
public class VipInviteAdapter extends BaseQuickAdapter<VipInviteBean, BaseViewHolder> {
    public VipInviteAdapter(@Nullable List<VipInviteBean> data) {
        super(R.layout.item_vip_invite, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipInviteBean item) {
        if (item == null) return;
        String nickName = item.getNickName();
        helper.setText(R.id.tv_nick_name, nickName.length() > 6 ? nickName.substring(0, 6) + "..." : nickName)
                .setText(R.id.tv_time, item.getCreateTime());
    }
}
