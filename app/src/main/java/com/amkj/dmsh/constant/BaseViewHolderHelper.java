package com.amkj.dmsh.constant;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/20
 * class description:请输入类描述
 */

public class BaseViewHolderHelper extends BaseViewHolder {

    public BaseViewHolderHelper(View view) {
        super(view);
        AutoUtils.autoSize(view);
    }
}
