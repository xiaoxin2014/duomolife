package com.amkj.dmsh.constant;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/23
 * version 3.1.5
 * class description:xutils3.0同步解析
 */
public class XUtilsSyncJsonParse implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request){
// 请求头
    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result){
        if(!TextUtils.isEmpty(result)){
            return new Gson().fromJson(result,resultClass);
        }else{
            return null;
        }
    }
}
