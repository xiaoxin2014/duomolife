package com.amkj.dmsh.base;

import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xiaoxin on 2017/12/22 0022
 */

// 内部类存储item中的控件
public class ViewHolder extends RecyclerView.ViewHolder{

    private final SparseArray<View> mViews = new SparseArray<View>();
    private View mConvertView;

    public ViewHolder(View convertView) {
        super(convertView);
        mConvertView = convertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    //获取当前位置的条目
    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 为TextView设置字符串
     */
    public void setText(int viewId, String string) {
        TextView view =  getView(viewId);
        view.setText(string);
    }

    /**
     * 为TextView设置颜色
     */
    public void setTextColor(int viewId, int color) {
        TextView view =  getView(viewId);
        view.setTextColor(color);
    }

    /**
     * 为ImageView设置图片资源
     */
    public void setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
    }

    /**
     * 为ImageView设置bitmap
     */
    public void setImageBitmap(int viewId, Bitmap bm) {
        ImageView view =  getView(viewId);
        view.setImageBitmap(bm);
    }

    /**
     * 给view设置背景色
     */
    public void setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
    }

    /**
     * 设置view是否可见
     */
    public void setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否勾选复选框
     */
    public void setCheckbox(int viewId, boolean checked) {
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(checked);
    }

}
