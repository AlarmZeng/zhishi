package com.AlarmZeng.zhishi.activity.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.AlarmZeng.zhishi.activity.db.CollectionHelper;

/**
 * Created by hunter_zeng on 2016/5/29.
 */
public class CollectionProvider extends ContentProvider {

    private CollectionHelper helper;

    private SQLiteDatabase db;

    private static final String TABLE_COLLECTION = "collection";

    private static final String AUTHORITY = "com.AlarmZeng.zhishi.provider";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private static final int COLLECTION_INSERT = 0;

    private static final int COLLECTION_DELETE = 1;

    private static final int COLLECTION_UPDATE = 2;

    private static final int COLLECTION_QUERY = 3;

    private static UriMatcher uriMatcher;

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, "collection/insert", COLLECTION_INSERT);
        uriMatcher.addURI(AUTHORITY, "collection/delete", COLLECTION_DELETE);
        uriMatcher.addURI(AUTHORITY, "collection/update", COLLECTION_UPDATE);
        uriMatcher.addURI(AUTHORITY, "collection/query", COLLECTION_QUERY);
    }

    @Override
    public boolean onCreate() {

        helper = CollectionHelper.getInstances(getContext(), 1);
        db = helper.getWritableDatabase();

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (uriMatcher.match(uri)) {

            case COLLECTION_QUERY :
                Cursor cursor = db.query(TABLE_COLLECTION, projection, selection, selectionArgs, null, null, sortOrder);

                return cursor;

            default:
                throw new IllegalArgumentException("未能识别的uri：" + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (uriMatcher.match(uri)) {

            case COLLECTION_INSERT :

                long rawId = db.insert(TABLE_COLLECTION, null, values);
                if (rawId == -1) {
                    //数据插入不成功
                    return null;
                }
                else {
                    //数据插入成功，将uri后面拼接一个行id返回
                    Uri uriReturn = ContentUris.withAppendedId(uri, rawId);
                    return uriReturn;
                }
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {

            case COLLECTION_DELETE :

                int number = db.delete(TABLE_COLLECTION, selection, selectionArgs);
                return number;

            default:
                throw new IllegalArgumentException("未能识别的uri：" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {
            case COLLECTION_UPDATE :

                int number = db.update(TABLE_COLLECTION, values, selection, selectionArgs);
                return number;

            default:
                throw new IllegalArgumentException("未能识别的uri:" + uri);
        }
    }
}
