package com.AlarmZeng.zhishi.activity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hunter_zeng on 2016/5/22.
 */
public class NewsCacheHelper extends SQLiteOpenHelper {

    private static NewsCacheHelper instance;

    private static final String NEWS_CACHE_TABLE = "create table news_cache (_id integer primary key autoincrement," +
            "newsUrl text," +
            "json text)";

    private NewsCacheHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public NewsCacheHelper getInstance(Context context, int version) {

        if (instance == null) {
            synchronized (NewsCacheHelper.class) {
                if (instance == null) {
                    instance = new NewsCacheHelper(context, "newsCache.db", null, version);
                }
            }
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(NEWS_CACHE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
