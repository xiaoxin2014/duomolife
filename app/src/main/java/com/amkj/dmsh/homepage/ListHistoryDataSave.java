package com.amkj.dmsh.homepage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;
import java.util.List;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/3/11
 * class description:搜索 历史记录
 */

public class ListHistoryDataSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ListHistoryDataSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     *
     * @param tag
     * @param dataList
     */
    public <T> void setDataList(String tag, List<T> dataList) {
        if (null == dataList || dataList.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(dataList);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> dataList = new LinkedList<>();
        String strJson = preferences.getString(tag, "");
        if (TextUtils.isEmpty(strJson)) {
            return dataList;
        }
        Gson gson = new Gson();
        dataList = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return dataList;
    }

    public void delDataList(String tag) {
        //转换成json数据，再保存
        String strJson = "";
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();
    }
}
