package com.amkj.dmsh.release.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DraftListDBHelper extends SQLiteOpenHelper {

    public DraftListDBHelper(Context context) {
        super(context, DraftListDB.DB_NAME, null, DraftListDB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建数据库
         */
        String sql = DraftListDB.DraftList.SQL_CREATE_TABLE;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
