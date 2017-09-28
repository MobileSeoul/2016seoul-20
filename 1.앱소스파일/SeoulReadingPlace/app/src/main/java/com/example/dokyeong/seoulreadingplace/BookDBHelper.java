package com.example.dokyeong.seoulreadingplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 2..
 */

public class BookDBHelper extends SQLiteOpenHelper {
    public static final String DB_FILE_NAME = "Book.db";
    public static final String DB_TABLE_NAME = "Book";
    public static final int DB_VERSION = 1;

    private volatile static BookDBHelper mBookDBHelper_Database = null;

    public static BookDBHelper getInstance(Context context){
        if(mBookDBHelper_Database == null){
            synchronized (BookDBHelper.class){
                if(mBookDBHelper_Database == null){
                    mBookDBHelper_Database = new BookDBHelper(context.getApplicationContext());
                }
            }
        }
        return mBookDBHelper_Database;
    }

    private BookDBHelper(Context context){ super(context, DB_FILE_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, cover TEXT, author TEXT, publisher TEXT, date TEXT, url TEXT, isbn TEXT, start TEXT, page INTEGER, finish INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addBook(Book book_Book){
        SQLiteDatabase sQLiteDatabase_Database = getWritableDatabase();

        ContentValues contentValues_Value = new ContentValues();
        contentValues_Value.put("title", book_Book.getTitle());
        contentValues_Value.put("cover", book_Book.getCover());
        contentValues_Value.put("author", book_Book.getAuthor());
        contentValues_Value.put("publisher", book_Book.getPublisher());
        contentValues_Value.put("date", book_Book.getDate());
        contentValues_Value.put("url", book_Book.getUrl());
        contentValues_Value.put("isbn", book_Book.getIsbn());
        contentValues_Value.put("start", book_Book.getStart());
        contentValues_Value.put("page", book_Book.getPage());
        contentValues_Value.put("finish", book_Book.getFinish());

        sQLiteDatabase_Database.insert(DB_TABLE_NAME, null, contentValues_Value);
        sQLiteDatabase_Database.close();
    }

    public void updateBook(Book book_One){
        SQLiteDatabase sQLiteDatabase_Database = getWritableDatabase();
        ContentValues contentValues_Value = new ContentValues();
        contentValues_Value.put("page", book_One.getPage());
        contentValues_Value.put("finish", book_One.getFinish());

        sQLiteDatabase_Database.update(DB_TABLE_NAME, contentValues_Value, "isbn=?", new String[]{book_One.getIsbn()});
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> arrayList_Books = new ArrayList<Book>();
        SQLiteDatabase sQLiteDatabase_Database = getWritableDatabase();
        String string_Query = "select * from " + DB_TABLE_NAME;
        Cursor cursor_Cursor = sQLiteDatabase_Database.rawQuery(string_Query, null);

        if (cursor_Cursor.moveToFirst()) {
            do {
                Book book_Book = new Book(cursor_Cursor.getString(cursor_Cursor.getColumnIndex("title")),
                        cursor_Cursor.getString(cursor_Cursor.getColumnIndex("cover")),
                        cursor_Cursor.getString(cursor_Cursor.getColumnIndex("author")),
                        cursor_Cursor.getString(cursor_Cursor.getColumnIndex("publisher")),
                        cursor_Cursor.getString(cursor_Cursor.getColumnIndex("date")),
                        cursor_Cursor.getString(cursor_Cursor.getColumnIndex("url")),
                        cursor_Cursor.getString(cursor_Cursor.getColumnIndex("isbn")),
                        cursor_Cursor.getString(cursor_Cursor.getColumnIndex("start")),
                        cursor_Cursor.getInt(cursor_Cursor.getColumnIndex("page")),
                        cursor_Cursor.getInt(cursor_Cursor.getColumnIndex("finish")));

                arrayList_Books.add(book_Book);
            } while (cursor_Cursor.moveToNext());
        }

        cursor_Cursor.close();
        sQLiteDatabase_Database.close();

        return arrayList_Books;
    }
}
