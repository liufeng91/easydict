package com.veeson.easydict.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper
 * <p/>
 * Created by Wilson on 2016/6/14.
 */
public class EasyDictOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_WORD_COLLECTION = "create table WordCollection ("
            + "id integer primary key autoincrement, "
            + "errorCode text, "
            + "web text, "
            + "translation text, "
            + "basic text, "
            + "query text)";

    public static final String CREATE_TRANSLATION_RECORD = "create table TranslationRecord ("
            + "id integer primary key autoincrement, "
            + "original text, "
            + "result text, "
            + "date text, "
            + "fromTo text)";

    public EasyDictOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORD_COLLECTION);
        db.execSQL(CREATE_TRANSLATION_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
