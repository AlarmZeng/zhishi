package com.AlarmZeng.zhishi.activity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CollectionHelper extends SQLiteOpenHelper {

    private static final String CREATE_COLLECTION_TABLE = "create table collection (_id integer primary key autoincrement," +
            "title text," +
            "images text," +
            "content_id text," +
            "type integer)";

    private static CollectionHelper instance;

    public static CollectionHelper getInstances(Context context, int version) {

        if (instance == null) {

            synchronized (CollectionHelper.class) {
                if (instance == null) {
                    instance = new CollectionHelper(context, "collection.db", null, version);
                }
            }
        }

        return instance;
    }

    private CollectionHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COLLECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
