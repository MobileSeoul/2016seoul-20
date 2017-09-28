package com.example.dokyeong.seoulreadingplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 9..
 */

public class BookPlaceSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_FILE_NAME = "BookPlace.db";
    public static final String DB_TABLE_NAME = "BookPlace";
    public static final int DB_VERSION = 1;

    private volatile static BookPlaceSQLiteOpenHelper mSQLiteOpenHelper_BookPlace = null;

    public static BookPlaceSQLiteOpenHelper getInstance(Context context){
        if(mSQLiteOpenHelper_BookPlace == null){
            synchronized (BookPlaceSQLiteOpenHelper.class){
                if(mSQLiteOpenHelper_BookPlace == null){
                    mSQLiteOpenHelper_BookPlace = new BookPlaceSQLiteOpenHelper(context.getApplicationContext());
                }
            }
        }
        return mSQLiteOpenHelper_BookPlace;
    }

    private BookPlaceSQLiteOpenHelper(Context context){ super(context, DB_FILE_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "category INTEGER, addressgu TEXT, address TEXT, name TEXT, website TEXT, phonenumber TEXT, " +
                "organization TEXT, longitude TEXT, latitude TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addLocation(BookPlace bookPlace_One){
        SQLiteDatabase sQLiteDatabase_BookPlace = getWritableDatabase();

        ContentValues contentValues_BookPlace = new ContentValues();
        contentValues_BookPlace.put("category", bookPlace_One.getCategory());
        contentValues_BookPlace.put("addressgu", bookPlace_One.getAddressGu());
        contentValues_BookPlace.put("address", bookPlace_One.getAddress());
        contentValues_BookPlace.put("name", bookPlace_One.getName());
        contentValues_BookPlace.put("website", bookPlace_One.getWebsite());
        contentValues_BookPlace.put("phonenumber", bookPlace_One.getPhoneNumber());
        contentValues_BookPlace.put("organization", bookPlace_One.getOrganization());
        contentValues_BookPlace.put("longitude", bookPlace_One.getLongitude());
        contentValues_BookPlace.put("latitude", bookPlace_One.getLatitude());

        sQLiteDatabase_BookPlace.insert(DB_TABLE_NAME, null, contentValues_BookPlace);
        sQLiteDatabase_BookPlace.close();
    }

    public ArrayList<BookPlace> getAllBookPlaces() {
        ArrayList<BookPlace> arrayList_BookPlaces = new ArrayList<BookPlace>();
        SQLiteDatabase sQLiteDatabase_BookPlace = getWritableDatabase();
        String string_Query = "select * from " + DB_TABLE_NAME;
        Cursor cursor_One = sQLiteDatabase_BookPlace.rawQuery(string_Query, null);

        if (cursor_One.moveToFirst()) {
            do {
                BookPlace bookPlace_One = new BookPlace(cursor_One.getInt(cursor_One.getColumnIndex("category")),
                        cursor_One.getString(cursor_One.getColumnIndex("addressgu")),
                        cursor_One.getString(cursor_One.getColumnIndex("address")),
                        cursor_One.getString(cursor_One.getColumnIndex("name")),
                        cursor_One.getString(cursor_One.getColumnIndex("website")),
                        cursor_One.getString(cursor_One.getColumnIndex("phonenumber")),
                        cursor_One.getString(cursor_One.getColumnIndex("organization")),
                        cursor_One.getString(cursor_One.getColumnIndex("longitude")),
                        cursor_One.getString(cursor_One.getColumnIndex("latitude")));

                arrayList_BookPlaces.add(bookPlace_One);
            } while (cursor_One.moveToNext());
        }

        cursor_One.close();
        sQLiteDatabase_BookPlace.close();

        return arrayList_BookPlaces;
    }
}
