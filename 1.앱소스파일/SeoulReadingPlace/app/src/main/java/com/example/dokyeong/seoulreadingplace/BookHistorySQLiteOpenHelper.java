package com.example.dokyeong.seoulreadingplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 27..
 */

public class BookHistorySQLiteOpenHelper  extends SQLiteOpenHelper {
    public static final String DB_FILE_NAME = "BookHistory.db";
    public static final String DB_TABLE_NAME = "BookHistory";
    public static final int DB_VERSION = 1;

    private volatile static BookHistorySQLiteOpenHelper mSQLiteOpenHelper_BookHistory = null;

    public static BookHistorySQLiteOpenHelper getInstance(Context context){
        if(mSQLiteOpenHelper_BookHistory == null){
            synchronized (BookHistorySQLiteOpenHelper.class){
                if(mSQLiteOpenHelper_BookHistory == null){
                    mSQLiteOpenHelper_BookHistory = new BookHistorySQLiteOpenHelper(context.getApplicationContext());
                }
            }
        }
        return mSQLiteOpenHelper_BookHistory;
    }

    private BookHistorySQLiteOpenHelper(Context context){ super(context, DB_FILE_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, isbn TEXT, category INTEGER, name TEXT, date TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addBookHistory(BookHistory bookHistory_One){
        SQLiteDatabase sQLiteDatabase_BookHistory = getWritableDatabase();

        ContentValues contentValues_BookHistory = new ContentValues();
        contentValues_BookHistory.put("title", bookHistory_One.getTitle());
        contentValues_BookHistory.put("isbn", bookHistory_One.getIsbn());
        contentValues_BookHistory.put("category", bookHistory_One.getCategory());
        contentValues_BookHistory.put("name", bookHistory_One.getName());
        contentValues_BookHistory.put("date", bookHistory_One.getDate());

        sQLiteDatabase_BookHistory.insert(DB_TABLE_NAME, null, contentValues_BookHistory);
        sQLiteDatabase_BookHistory.close();
    }

    public ArrayList<BookHistory> getAllBookHistories() {
        ArrayList<BookHistory> arrayList_BookHistories = new ArrayList<BookHistory>();
        SQLiteDatabase sQLiteDatabase_BookHistory = getWritableDatabase();
        String string_Query = "select * from " + DB_TABLE_NAME;
        Cursor cursor_One = sQLiteDatabase_BookHistory.rawQuery(string_Query, null);

        if (cursor_One.moveToFirst()) {
            do {
                BookHistory bookHistory_One = new BookHistory(cursor_One.getString(cursor_One.getColumnIndex("title")),
                        cursor_One.getString(cursor_One.getColumnIndex("isbn")),
                        cursor_One.getInt(cursor_One.getColumnIndex("category")),
                        cursor_One.getString(cursor_One.getColumnIndex("name")),
                        cursor_One.getString(cursor_One.getColumnIndex("date")));

                arrayList_BookHistories.add(bookHistory_One);
            } while (cursor_One.moveToNext());
        }

        cursor_One.close();
        sQLiteDatabase_BookHistory.close();

        return arrayList_BookHistories;
    }
}
