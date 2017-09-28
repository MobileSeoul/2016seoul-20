package com.example.dokyeong.seoulreadingplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 3..
 */

public class BookDetailActivity extends AppCompatActivity {
    private ImageView imageView_Back;
    private TextView textView_Title;
    private ImageView imageView_Cover;
    private TextView textView_Author;
    private TextView textView_Publisher;
    private TextView textView_Start;
    private ImageView imageView_Map;
    private ImageView imageView_Link;
    private RecyclerView recyclerView_Detail;
    private TextView textView_Page;
    private ImageView imageView_Page;
    private TextView textView_Place;
    private TextView textView_Day;

    private Book mBook_One;
    private ArrayList<BookHistory> mArrayList_BookHistories;
    private BookDetailAdapter mAdapter_BookDetail;
    private Typeface mTypeface_SeoulNamsanM;
    private ArrayList<BookPlace> mArrayList_BookPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetail);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");
        mBook_One = (Book)(getIntent().getSerializableExtra("BOOK"));
        mArrayList_BookPlaces = (ArrayList<BookPlace>)getIntent().getSerializableExtra("BOOKPLACES");

        imageView_Back = (ImageView)findViewById(R.id.imageview_bookdetail_back);
        textView_Title = (TextView)findViewById(R.id.textview_bookdetail_title);
        imageView_Cover = (ImageView)findViewById(R.id.imageview_bookdetail_cover);
        textView_Author = (TextView)findViewById(R.id.textview_bookdetail_author);
        textView_Publisher = (TextView)findViewById(R.id.textview_bookdetail_publisher);
        textView_Start = (TextView)findViewById(R.id.textview_bookdetail_start);
        imageView_Map = (ImageView)findViewById(R.id.imageview_bookdetail_map);
        imageView_Link = (ImageView)findViewById(R.id.imageview_bookdetail_link);
        recyclerView_Detail = (RecyclerView)findViewById(R.id.recyclerview_bookdetail_bookhistory);
        textView_Page = (TextView)findViewById(R.id.textview_bookdetail_page);
        imageView_Page = (ImageView)findViewById(R.id.imageview_bookdetail_page);
        textView_Place = (TextView)findViewById(R.id.textview_bookdetail_place);
        textView_Day = (TextView)findViewById(R.id.textview_bookdetail_day);

        textView_Title.setTypeface(mTypeface_SeoulNamsanM);
        textView_Author.setTypeface(mTypeface_SeoulNamsanM);
        textView_Publisher.setTypeface(mTypeface_SeoulNamsanM);
        textView_Start.setTypeface(mTypeface_SeoulNamsanM);
        textView_Page.setTypeface(mTypeface_SeoulNamsanM);
        textView_Place.setTypeface(mTypeface_SeoulNamsanM);
        textView_Day.setTypeface(mTypeface_SeoulNamsanM);

        imageView_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                finish();
            }
        });

        textView_Title.setText(mBook_One.getTitle());
        Glide.with(this).load(mBook_One.getCover()).into(imageView_Cover);
        if(mBook_One.getFinish() == 1){
            imageView_Page.setImageResource(R.mipmap.icon_bookmark_red);
            //Glide.with(this).load(R.mipmap.icon_bookmark_red).into(imageView_Page);
            textView_Page.setText("완독");
        }else{
            textView_Page.setText(String.valueOf(mBook_One.getPage()));
        }


        if(mBook_One.getAuthor().equals("")){
            textView_Author.setText("저자 : 미상");
        }else{
            textView_Author.setText("저자 : " + mBook_One.getAuthor());
        }

        if(mBook_One.getPublisher().equals("")){
            textView_Publisher.setText("출판사 : 미상");
        }else{
            textView_Publisher.setText("출판사 : " + mBook_One.getPublisher());
        }

        textView_Start.setText(mBook_One.getStart());

        imageView_Map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                Intent intent_Map = new Intent(BookDetailActivity.this, MapActivity.class);
                intent_Map.putExtra("BOOKHISTORIES", mArrayList_BookHistories);
                intent_Map.putExtra("BOOKPLACES", mArrayList_BookPlaces);
                startActivity(intent_Map);
            }
        });

        imageView_Link.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                String string_Url = mBook_One.getUrl();
                Intent intent_Link = new Intent(Intent.ACTION_VIEW, Uri.parse(string_Url));
                startActivity(intent_Link);
            }
        });

        recyclerView_Detail.setHasFixedSize(true);
        recyclerView_Detail.setLayoutManager(new LinearLayoutManager(this));

        BookDetailTask task_BookDetail = new BookDetailTask(this);
        task_BookDetail.execute(mBook_One);
    }

    private class BookDetailTask extends AsyncTask<Book, Integer, ArrayList<BookHistory>> {
        private BookHistorySQLiteOpenHelper mSqLiteOpenHelper_BookHistory;

        public BookDetailTask(Context context){
            mSqLiteOpenHelper_BookHistory = BookHistorySQLiteOpenHelper.getInstance(context);
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected ArrayList<BookHistory> doInBackground(Book... book_Books) {

            String string_Title = book_Books[0].getTitle();
            String string_Isbn = book_Books[0].getIsbn();

            ArrayList<BookHistory> arrayList_AllBookHistories = mSqLiteOpenHelper_BookHistory.getAllBookHistories();
            Log.d("Size", String.valueOf(arrayList_AllBookHistories.size()));
            ArrayList<BookHistory> arrayList_BookHistories = new ArrayList<BookHistory>();

            for(int int_Index = 0; int_Index < arrayList_AllBookHistories.size(); int_Index++){
                if(string_Title.equals(arrayList_AllBookHistories.get(int_Index).getTitle())
                        && string_Isbn.equals(arrayList_AllBookHistories.get(int_Index).getIsbn())){
                    arrayList_BookHistories.add(arrayList_AllBookHistories.get(int_Index));
                }
            }

            Log.d("Size", String.valueOf(arrayList_BookHistories.size()));

            return arrayList_BookHistories;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(ArrayList<BookHistory> arrayList_BookHistories){
            mArrayList_BookHistories = arrayList_BookHistories;

            mAdapter_BookDetail = new BookDetailAdapter(BookDetailActivity.this, arrayList_BookHistories);
            recyclerView_Detail.setAdapter(mAdapter_BookDetail);

        }
    }
}
