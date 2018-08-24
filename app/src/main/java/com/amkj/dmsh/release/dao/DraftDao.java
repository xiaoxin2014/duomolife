package com.amkj.dmsh.release.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.amkj.dmsh.release.bean.DraftInfo;

import java.util.ArrayList;
import java.util.List;


public class DraftDao {
    private DraftListDBHelper mHelper;

    public DraftDao(Context context) {
        mHelper = new DraftListDBHelper(context);
    }

    /**
     * 增加一条记录
     * 数据库应该保存的东西应该有
     * 1，富文本的HTML
     * 2，富文本的toString得到的字符串
     * 3，把当前的时间也存入
     * 4，存储URI和picid的容器
     * 5，
     * 6，把小组的名称存入
     * 7 用户的userId
     */
    public boolean add(String time, String html, String toString, String title, String tag, String atNameId, String type) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DraftListDB.DraftList.COLUMN_HTML, html);
        values.put(DraftListDB.DraftList.COLUMN_STRING, toString);
        values.put(DraftListDB.DraftList.COLUMN_TIME, time);
        values.put(DraftListDB.DraftList.COLUMN_TITLE, title);
        values.put(DraftListDB.DraftList.COLUMN_TAG, tag);
        values.put(DraftListDB.DraftList.COLUMN_ATNAMEID, atNameId);
        values.put(DraftListDB.DraftList.COLUMN_TYPE, type);
        Long insert = db.insert(DraftListDB.DraftList.TABLE_NAME, null, values);
        db.close();
        return insert != -1;

    }

    /**
     * 删除数据
     * 根据COLUMN_ID来
     */
    public boolean delete(int id) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        String whereClause = DraftListDB.DraftList.COLUMN_ID + "=?";
        String[] whereArgs = new String[]{id + ""};
        int delete = db.delete(DraftListDB.DraftList.TABLE_NAME, whereClause, whereArgs);
        db.close();
        return delete != 0;
    }


    /**
     * 查询所有数据
     */
    public List<DraftInfo> findAll() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select " + DraftListDB.DraftList.COLUMN_HTML + ","
                + DraftListDB.DraftList.COLUMN_ID + ","
                + DraftListDB.DraftList.COLUMN_TIME + ","
                + DraftListDB.DraftList.COLUMN_STRING + ","
                + DraftListDB.DraftList.COLUMN_TITLE + ","
                + DraftListDB.DraftList.COLUMN_TAG + ","
                + DraftListDB.DraftList.COLUMN_ATNAMEID + ","
                + DraftListDB.DraftList.COLUMN_TYPE + " from "
                + DraftListDB.DraftList.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        List<DraftInfo> mList = new ArrayList<DraftInfo>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DraftInfo info = new DraftInfo();
                info.html = cursor.getString(0);
                info.id = cursor.getInt(1);
                info.time = cursor.getString(2);
                info.text = cursor.getString(3);
                info.title = cursor.getString(4);
                info.tag = cursor.getString(5);
                info.atNameId = cursor.getString(6);
                info.type = cursor.getString(7);
                mList.add(info);
            }
            cursor.close();
        }
        db.close();

        return mList;

    }


    /**
     * 更新一条数据的time，title，html，toString，hashmap
     */
    public boolean update(int id, String time, String html, String text, String title, String tag, String atNameId, String type) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DraftListDB.DraftList.COLUMN_TIME, time);
        values.put(DraftListDB.DraftList.COLUMN_HTML, html);
        values.put(DraftListDB.DraftList.COLUMN_STRING, text);
        values.put(DraftListDB.DraftList.COLUMN_TITLE, title);
        values.put(DraftListDB.DraftList.COLUMN_TAG, tag);
        values.put(DraftListDB.DraftList.COLUMN_ATNAMEID, atNameId);
        values.put(DraftListDB.DraftList.COLUMN_TYPE, type);
        String whereClause = DraftListDB.DraftList.COLUMN_ID + "=?";
        String[] whereArgs = new String[]{id + ""};
        int update = db.update(DraftListDB.DraftList.TABLE_NAME, values, whereClause, whereArgs);
        return update != 0;
    }
}
