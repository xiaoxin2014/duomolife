package com.amkj.dmsh.homepage;

import android.text.TextUtils;

import com.amkj.dmsh.homepage.bean.MarqueeTextEntity.MarqueeTextBean;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxin on 2019/11/14
 * Version:v4.3.6
 * ClassDescription :滚动通知显示记录保存
 */
public class RollMsgIdDataSave {
    private static RollMsgIdDataSave mRollMsgIdDataSave = null;
    private static final String FILE_NAME = "rollMsgId";
    private static final String KEY = "HomeAlreadyShowId";

    private RollMsgIdDataSave() {
    }

    public static RollMsgIdDataSave getSingleton() {
        if (mRollMsgIdDataSave == null) {
            synchronized (RollMsgIdDataSave.class) {
                if (mRollMsgIdDataSave == null) {
                    mRollMsgIdDataSave = new RollMsgIdDataSave();
                }
            }
        }
        return mRollMsgIdDataSave;
    }

    //保存显示过的通知的id到本地
    public void saveMsgId(List<MarqueeTextBean> marqueeTextList) {

        try {
            String json = (String) SharedPreUtils.getParam(FILE_NAME, KEY, "");
            List<Integer> ids = TextUtils.isEmpty(json) ? new ArrayList<>() : GsonUtils.fromJson(json, new TypeToken<List<Integer>>() {
            }.getType());

            for (MarqueeTextBean bean : marqueeTextList) {
                int id = bean.getId();
                if (!ids.contains(id)) {
                    ids.add(id);
                }
            }

            SharedPreUtils.setParam(FILE_NAME, KEY, GsonUtils.toJson(ids));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    //根据id查询是否显示过此条通知
    public boolean containId(int msgId) {
        List<Integer> ids = new ArrayList<>();
        try {
            String json = (String) SharedPreUtils.getParam(FILE_NAME, KEY, "");
            ids = GsonUtils.fromJson(json, new TypeToken<List<Integer>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return ids.size() != 0 && ids.contains(msgId);
    }
}
