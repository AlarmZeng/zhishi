package com.AlarmZeng.zhishi.activity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunter_zeng on 2016/5/22.
 */
public class WebCacheHelper extends SQLiteOpenHelper {

    private static WebCacheHelper instance;

    private static final String CACHE_TABLE = "create table(_id integer primary key autoincrement," +
            "newsId integer," +
            "json text)";

    private WebCacheHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static WebCacheHelper getInstance(Context context, int version) {

        if (instance == null) {
            synchronized (WebCacheHelper.class) {
                if (instance == null) {
                    instance = new WebCacheHelper(context, "webCache.db", null, version);
                }
            }
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CACHE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
