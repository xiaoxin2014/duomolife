package com.amkj.dmsh.release.tutu;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;

import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.impl.components.edit.TuEditMultipleFragment;


/**
 * Created by xiaoxin on 2019/8/6
 * Version:v4.0.0
 * ClassDescription :自定义美化组件界面
 */
public class CustomMultipleFragment extends TuEditMultipleFragment {
    /**
     * 布局ID
     */
    public static int getLayoutId() {
        return R.layout.tusdk_impl_component_edit_multiple_fragment;
    }

    @Override
    protected void loadView(ViewGroup view) {
        super.loadView(view);
        LinearLayout linearLayout = view.findViewById(R.id.lsq_actions_wrapview);
        //tuSdk升级后，旧版本美颜功能无法使用，所以先手动去掉
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TuSdkTextButton tuSdkTextButton = (TuSdkTextButton) linearLayout.getChildAt(i);
            if (tuSdkTextButton.getText().equals("美颜")) {
                linearLayout.removeViewAt(i);
                break;
            }
        }
    }
}
