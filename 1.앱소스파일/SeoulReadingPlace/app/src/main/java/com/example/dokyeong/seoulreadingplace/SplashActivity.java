package com.example.dokyeong.seoulreadingplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 8. 22..
 */
public class SplashActivity extends AppCompatActivity {
    private Typeface mTypeface_SeoulNamsanM;

    private TextView textView_Copyright;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        textView_Copyright = (TextView)findViewById(R.id.textview_splash_copyright);
        textView_Copyright.setTypeface(mTypeface_SeoulNamsanM);

        SplashTask task_Splash = new SplashTask(this);
        task_Splash.execute();

        /*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                readLibraryDatabase();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
        */
    }

    public void readLibrary(){
        InputStream inputStream = null;
        try{
            AssetManager assetManager = getAssets();
            inputStream = assetManager.open("library_seoul.csv");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            bufferedReader.readLine();
            String strLine;
            while((strLine = bufferedReader.readLine()) != null){
                String[] strRows = strLine.split("\"");
                /*
                 * 3 : 구명
                 * 13 : 새주소명
                 * 15 : 시설명
                 * 17 : 운영기관
                 * 27 : 홈페이지주소
                 * 29 : 연락처
                 * 33 : 경도
                 * 35 : 위도
                 * 카테고리 : 도서관 , 서점
                 */
                for(int i = 0; i < strRows.length; i++){
                    Log.d("CSV", String.valueOf(i) + " : " + strRows[i]);
                }
                BookPlace bookPlace_One = new BookPlace(0, strRows[3], strRows[13], strRows[15], strRows[27], strRows[29], strRows[17], strRows[33], strRows[35]);

                BookPlaceSQLiteOpenHelper sqLiteOpenHelper_BookPlace = BookPlaceSQLiteOpenHelper.getInstance(this);
                sqLiteOpenHelper_BookPlace.addLocation(bookPlace_One);
            }
        }catch(IOException iOException){
            Log.d("Line", "Error1");
        }finally {
            try{
                inputStream.close();
            }catch(IOException iOException){
                Log.d("Line", "Error2");
            }
        }
    }

    public void readBookStore(){
        InputStream inputStream = null;
        try{
            AssetManager assetManager = getAssets();
            inputStream = assetManager.open("bookstore_seoul.csv");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            bufferedReader.readLine();
            String strLine;
            while((strLine = bufferedReader.readLine()) != null){
                String[] strRows = strLine.split(",");
                /*
                 * 0 : 구명
                 * 1 : 새주소명
                 * 2 : 시설명
                 * 8 : 운영기관
                 * 3 : 홈페이지주소
                 * 4 : 연락처
                 * 6 : 경도
                 * 5 : 위도
                 * 카테고리 : 도서관 , 서점
                 */
                for(int i = 0; i < strRows.length; i++){
                    Log.d("CSV", String.valueOf(i) + " : " + strRows[i]);
                }
                //Log.d("Name", "Name" + strRows[15] + ", Long = " + strRows[33] + ", Lat = " + strRows[35]);
                BookPlace bookPlace_One = new BookPlace(1, strRows[0], strRows[1], strRows[2], strRows[3], strRows[4], strRows[8], strRows[6], strRows[5]);
                Log.d("BookStore", strRows[0] + "," + strRows[1] + "," + strRows[2] + "," + strRows[3] + "," + strRows[4] + "," +
                        strRows[8] + "," + strRows[6] + "," + strRows[5]);
                BookPlaceSQLiteOpenHelper sqLiteOpenHelper_Location = BookPlaceSQLiteOpenHelper.getInstance(this);
                sqLiteOpenHelper_Location.addLocation(bookPlace_One);
            }
        }catch(IOException iOException){

        }finally{

        }
    }

    private class SplashTask extends AsyncTask<Book, Integer, ArrayList<BookPlace>> {
        private BookPlaceSQLiteOpenHelper mSqLiteOpenHelper_BookPlace;

        public SplashTask(Context context){
            mSqLiteOpenHelper_BookPlace = BookPlaceSQLiteOpenHelper.getInstance(context);
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BookPlace> doInBackground(Book... book_Books) {
            long long_Start = System.currentTimeMillis();

            SharedPreferences sharedPreferences_ApplicationState = getSharedPreferences("APPLICATION_STATE", MODE_PRIVATE);
            boolean boolean_First = sharedPreferences_ApplicationState.getBoolean("FIRST", true);
            ArrayList<BookPlace> arrayList_BookPlaces;

            if(boolean_First){
                readLibrary();
                readBookStore();
                SharedPreferences.Editor editor = sharedPreferences_ApplicationState.edit();
                editor.putBoolean("FIRST", false);
                editor.commit();
            }

            arrayList_BookPlaces = mSqLiteOpenHelper_BookPlace.getAllBookPlaces();

            long long_End = System.currentTimeMillis();
            long long_Duration = long_End - long_Start;
            Log.d("DURATION", String.valueOf(long_Duration));
            long long_Sleep = 2000 - long_Duration;

            if(long_Sleep > 0 && long_Sleep < 2000){
                try {
                    Thread.sleep(long_Sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return arrayList_BookPlaces;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(ArrayList<BookPlace> arrayList_BookPlaces){
            Log.d("onPostExecute", "onPostExecute");

            Intent intent_Main = new Intent(SplashActivity.this, MainActivity.class);
            intent_Main.putExtra("BOOKPLACES", arrayList_BookPlaces);
            startActivity(intent_Main);
            finish();
        }
    }
}
