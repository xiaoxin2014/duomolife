package com.amkj.dmsh.shopdetails.adapter;

import android.view.View;
import android.widget.EditText;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhy.autolayout.utils.AutoUtils;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/17
 * class description:请输入类描述
 */

public class BaseViewHolderHelperAppraise extends BaseViewHolder {
    EditText et_indent_appraise_input;

    public BaseViewHolderHelperAppraise(View view) {
        super(view);
        et_indent_appraise_input = (EditText) view.findViewById(R.id.et_indent_appraise_input);
        AutoUtils.autoSize(view);
    }
}
