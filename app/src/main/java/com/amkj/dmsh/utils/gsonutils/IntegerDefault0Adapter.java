package com.amkj.dmsh.utils.gsonutils;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/5/17
 * version 3.1.3
 * class description:json integer 为空
 */
public class IntegerDefault0Adapter implements JsonSerializer<Integer>,JsonDeserializer<Integer>{
    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (TextUtils.isEmpty(json.getAsString())){
                return 0;
            }
        } catch (Exception ignore){
            return 0;
        }
        try {
            return json.getAsInt();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
