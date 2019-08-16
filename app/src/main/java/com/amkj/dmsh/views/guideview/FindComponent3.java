package com.amkj.dmsh.views.guideview;

import android.view.LayoutInflater;
import android.view.View;

import com.amkj.dmsh.R;

/**
 * Created by xiaoxin on 2019/8/9
 * Version:v4.1.0
 * ClassDescription :引导遮罩层
 */
public class FindComponent3 implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layer_find3, null);
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_TOP;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return -75;
    }

    @Override
    public int getYOffset() {
        return -20;
    }
}
