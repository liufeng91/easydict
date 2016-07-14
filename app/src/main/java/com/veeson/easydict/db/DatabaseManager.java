package com.veeson.easydict.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veeson.easydict.model.TranslationRecord;
import com.veeson.easydict.model.YoudaoWord;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wilson on 2016/6/14.
 */
public class DatabaseManager {
    private static final String DATABASE_NAME = "EasyDict"; // 数据库名
    private static final int DATABASE_VERSION = 1; // 数据库版本号
    private static DatabaseManager databaseManager;
    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     *
     * @param context
     */
    private DatabaseManager(Context context) {
        db = new EasyDictOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
    }

    /**
     * 获取DatabaseManager实例
     *
     * @param context
     * @return
     */
    public synchronized static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
    }

    /**
     * 收藏某个YoudaoWord的实例
     *
     * @param youdaoWord
     */
    public void saveCollectionWord(@NonNull YoudaoWord youdaoWord) {
        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        values.put("errorCode", youdaoWord.errorCode);
        values.put("web", gson.toJson(youdaoWord.web));
        values.put("translation", gson.toJson(youdaoWord.translation));
        values.put("basic", gson.toJson(youdaoWord.basic));
        values.put("query", youdaoWord.query);
        db.insert("WordCollection", null, values);
    }

    /**
     * 查询用户收藏的所有单词并返回
     *
     * @return
     */
    public List<YoudaoWord> loadCollectionWords() {
        List<YoudaoWord> list = new ArrayList<>();
        // sqlite不能存储List集合，需将List集合数据转换成json字符串再存储进sqlite，想取出来的话解析json字符串就可以了。
        Gson gson = new Gson();
        Type typeList = new TypeToken<ArrayList<String>>() {
        }.getType();
        Type typeWeb = new TypeToken<List<YoudaoWord.WebEntity>>() {
        }.getType();
        Type typeBasic = new TypeToken<YoudaoWord.BasicEntity>() {
        }.getType();
        Cursor cursor = db.query("WordCollection", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                YoudaoWord youdaoWord = new YoudaoWord();
                youdaoWord.errorCode = cursor.getString(cursor.getColumnIndex("errorCode"));
                youdaoWord.web = gson.fromJson(cursor.getString(cursor.getColumnIndex("web")), typeWeb);
                youdaoWord.translation = gson.fromJson(cursor.getString(cursor.getColumnIndex("translation")), typeList);
                youdaoWord.basic = gson.fromJson(cursor.getString(cursor.getColumnIndex("basic")), typeBasic);
                youdaoWord.query = cursor.getString(cursor.getColumnIndex("query"));
                list.add(youdaoWord);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    /**
     * 查询某个单词是否被收藏
     *
     * @param word
     * @return
     */
    public boolean queryCollectionWord(@NonNull String word) {
        Cursor cursor = db.query("WordCollection", new String[]{"query"}, "query = ?", new String[]{word}, null, null, null);
        try {
            return cursor.moveToFirst();
        } catch (Exception e) {
            return false;
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 取消某个单词的收藏
     *
     * @param word
     * @return
     */
    public boolean cancelCollectionWord(@NonNull String word) {
        int count = db.delete("WordCollection", "query = ?", new String[]{word});
        return count != 0;
    }

    public void cancelAllCollectionWords() {
        db.delete("WordCollection", null, null);
    }

    /**
     * 存储翻译记录到数据库
     *
     * @param translationRecord
     */
    public void saveTranslationRecord(@NonNull TranslationRecord translationRecord) {
        ContentValues values = new ContentValues();
        values.put("original", translationRecord.original);
        values.put("result", translationRecord.result);
        values.put("date", translationRecord.date);
        values.put("fromTo", translationRecord.fromTo);
        db.insert("TranslationRecord", null, values);
    }

    /**
     * 删除一条翻译记录
     *
     * @param translationRecord
     */
    public void deleteTranslationRecord(@NonNull TranslationRecord translationRecord) {
        db.delete("TranslationRecord", "original = ?", new String[]{translationRecord.original});
    }

    /**
     * 删除所有翻译记录
     */
    public void deleteAllTranslationRecords() {
        db.delete("TranslationRecord", null, null);
    }

    /**
     * 加载所有翻译记录
     *
     * @return
     */
    public List<TranslationRecord> loadTranslationRecord() {
        List<TranslationRecord> list = new ArrayList<>();
        Cursor cursor = db.query("TranslationRecord", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                TranslationRecord translationRecord = new TranslationRecord();
                translationRecord.original = cursor.getString(cursor.getColumnIndex("original"));
                translationRecord.result = cursor.getString(cursor.getColumnIndex("result"));
                translationRecord.date = cursor.getString(cursor.getColumnIndex("date"));
                translationRecord.fromTo = cursor.getString(cursor.getColumnIndex("fromTo"));
                list.add(translationRecord);
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
}
