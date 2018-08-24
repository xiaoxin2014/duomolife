package com.amkj.dmsh.release.dialogutils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by Sai on 15/8/9.
 */
public class AlertViewAdapter extends BaseAdapter {
    private AlertSettingBean.AlertInitView alertInitView;
    private List<String> mData;
    private List<String> otherDataList;

    public AlertViewAdapter(List<String> data, List<String> otherDataList, AlertSettingBean.AlertInitView alertInitView) {
        this.mData = data;
        this.otherDataList = otherDataList;
        this.alertInitView = alertInitView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String data = mData.get(position);
        Holder holder = null;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item_alertbutton, null);
            holder = createHolder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.UpdateUI(parent.getContext(), data, position);
        return view;
    }

    public Holder createHolder(View view) {
        return new Holder(view);
    }

    public class Holder {
        private TextView tvAlert;

        public Holder(View view) {
            tvAlert = (TextView) view.findViewById(R.id.tvAlert);
        }

        public void UpdateUI(Context context, String data, int position) {
            tvAlert.setText(data);
            if (otherDataList != null && otherDataList.contains(data)) {
                tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidth1px() * (alertInitView.getOthSize() > 0
                        ? alertInitView.getOthSize() : 28));
                tvAlert.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getOthColorValue())?alertInitView.getOthColorValue():"#ff5e6b"));
            } else {
                tvAlert.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidth1px() * (alertInitView.getNorSize() > 0
                        ? alertInitView.getNorSize() : 28));
                tvAlert.setTextColor(Color.parseColor(!TextUtils.isEmpty(alertInitView.getNorColorValue())?alertInitView.getNorColorValue():"#0a88fa"));
            }
        }
    }
}