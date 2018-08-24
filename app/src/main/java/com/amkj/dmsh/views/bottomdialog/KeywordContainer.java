/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amkj.dmsh.views.bottomdialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;

import com.amkj.dmsh.R;

import java.util.ArrayList;

/**
 * 动态 添加 标题， 并且设置监听
 */
public class KeywordContainer extends LinearLineWrapLayout implements View.OnClickListener {
    private OnClickKeywordListener onClickKeywordListener;
    private KeywordViewFactory keywordViewFactory;
    private ArrayList<ProductParameterValueBean> keywordData;

    public KeywordContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeywordContainer(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        if (onClickKeywordListener != null) {
            onClickKeywordListener.onClickKeyword(v, (ProductParameterValueBean) v.getTag(R.id.sku_param));
        }
    }

    /**
     * 设置关键字
     *
     * @param keywordData 关键字数组
     */
    public void setKeywords(ArrayList<ProductParameterValueBean> keywordData) {
        if (keywordViewFactory == null) {
            throw new IllegalStateException("你必须设置keywordViewFactory");
        }
        removeAllViews();
        this.keywordData = keywordData;
        if (keywordData == null) {
            return;
        }
        CheckedTextView keywordTextView;
        String valueString;
        for (int w = 0; w < keywordData.size(); w++) {
            ProductParameterValueBean productParameterValueBean = keywordData.get(w);
            valueString = productParameterValueBean.getValueName();
            if (TextUtils.isEmpty(valueString)) continue;
            keywordTextView = keywordViewFactory.makeKeywordView(productParameterValueBean.getPropId(), valueString);
            if (keywordTextView == null) {
                throw new IllegalArgumentException("KeywordViewFactory.makeKeywordView()不能返回null");
            }
            keywordTextView.setText(valueString);
            keywordTextView.setTag(R.id.sku_param, productParameterValueBean);

            keywordTextView.setSelected(productParameterValueBean.isSelected());
            if (productParameterValueBean.isSelected()) {
                keywordTextView.setChecked(productParameterValueBean.getNotice()==1||productParameterValueBean.getNotice()==2);
            } else {
                keywordTextView.setChecked(false);
            }
            keywordTextView.setEnabled(productParameterValueBean.isNull());
            keywordTextView.setOnClickListener(this);
            addView(keywordTextView);
        }
        startLayoutAnimation();
    }

    public void setOnClickKeywordListener(OnClickKeywordListener onClickKeywordListener) {
        this.onClickKeywordListener = onClickKeywordListener;
    }

    public void setKeywordViewFactory(KeywordViewFactory keywordViewFactory) {
        this.keywordViewFactory = keywordViewFactory;
    }

    public interface OnClickKeywordListener {
        void onClickKeyword(View v, ProductParameterValueBean productParameterValueBean);
    }

    public interface KeywordViewFactory {
        CheckedTextView makeKeywordView(int proId, String text);
    }
}
