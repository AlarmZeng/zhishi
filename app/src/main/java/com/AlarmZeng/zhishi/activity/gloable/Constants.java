package com.AlarmZeng.zhishi.activity.gloable;

import android.net.Uri;

import com.AlarmZeng.zhishi.activity.provider.CollectionProvider;

/**
 * Created by hunter_zeng on 2016/5/16.
 */
public class Constants {

    //api
    public static final String SPLASH_URL = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

    public static final String MENU_LIST_URL = "http://news-at.zhihu.com/api/4/themes";

    public static final String TOP_STORIES_URL = "http://news-at.zhihu.com/api/4/news/latest";

    public static final String BEFORE_NEWS_URL = "http://news.at.zhihu.com/api/4/news/before/";

    public static final String MENU_NEWS_URL = "http://news-at.zhihu.com/api/4/theme/";

    public static final String CONTENT_URL = "http://news-at.zhihu.com/api/4/news/";

    public static final int MAIN_TOPIC = 521;

    //collection表
    public static final Uri URI_COLLECTION_INSERT = Uri.parse(CollectionProvider.BASE_URI + "/collection/insert");
    public static final Uri URI_COLLECTION_DELETE = Uri.parse(CollectionProvider.BASE_URI + "/collection/delete");
    public static final Uri URI_COLLECTION_UPDATE = Uri.parse(CollectionProvider.BASE_URI + "/collection/update");
    public static final Uri URI_COLLECTION_QUERY = Uri.parse(CollectionProvider.BASE_URI + "/collection/query");
}
