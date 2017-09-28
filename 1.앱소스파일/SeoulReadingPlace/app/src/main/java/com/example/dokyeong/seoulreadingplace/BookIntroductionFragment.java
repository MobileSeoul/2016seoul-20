package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DoKyeong on 2016. 8. 23..
 */
public class BookIntroductionFragment extends Fragment{
    // 처음 : 10개, 더보기 : 30개 - 그리드 타입, 한줄에 3개

    private Context m_Context;

    private ArrayList<Book> mArrayList_Bestsellers;
    private ArrayList<Book> mArrayList_RecommendationBooks;
    private ArrayList<Book> mArrayList_NewBooks;
    private Typeface mTypeface_SeoulNamsanM;

    // 16.09.23

    private BookIntroductionAdapter mBookIntroductionAdapter_Bestseller;
    private BookIntroductionAdapter mBookIntroductionAdapter_RecommendationBook;
    private BookIntroductionAdapter mBookIntroductionAdapter_NewBook;

    private TextView textView_Bestseller;
    private TextView textView_RecommendationBook;
    private TextView textView_NewBook;

    private RecyclerView recyclerView_Bestseller;
    private RecyclerView recyclerView_RecommendationBook;
    private RecyclerView recyclerView_NewBook;

    private TextView textView_MoreBestseller;
    private TextView textView_MoreRecommendationBook;
    private TextView textView_MoreNewBook;

    public BookIntroductionFragment(){

    }

    public void onAttach(Context context){
        super.onAttach(context);

        m_Context = context;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context.getAssets(), "seoulnamsan_m.otf");
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view = layoutInflater.inflate(R.layout.fragment_bookintroduction, container, false);

        textView_Bestseller = (TextView)view.findViewById(R.id.textview_bookintroduction_bestseller);
        textView_RecommendationBook = (TextView)view.findViewById(R.id.textview_bookintroduction_recommendationbook);
        textView_NewBook = (TextView)view.findViewById(R.id.textview_bookintroduction_newbook);

        textView_Bestseller.setTypeface(mTypeface_SeoulNamsanM);
        textView_RecommendationBook.setTypeface(mTypeface_SeoulNamsanM);
        textView_NewBook.setTypeface(mTypeface_SeoulNamsanM);

        recyclerView_Bestseller = (RecyclerView)view.findViewById(R.id.recyclerview_bookintroduction_bestseller);
        recyclerView_Bestseller.setHasFixedSize(true);
        recyclerView_Bestseller.setLayoutManager(new LinearLayoutManager(m_Context, LinearLayoutManager.HORIZONTAL, false));

        recyclerView_RecommendationBook = (RecyclerView)view.findViewById(R.id.recyclerview_bookintroduction_recommendationbook);
        recyclerView_RecommendationBook.setHasFixedSize(true);
        recyclerView_RecommendationBook.setLayoutManager(new LinearLayoutManager(m_Context, LinearLayoutManager.HORIZONTAL, false));

        recyclerView_NewBook = (RecyclerView)view.findViewById(R.id.recyclerview_bookintroduction_newbook);
        recyclerView_NewBook.setHasFixedSize(true);
        recyclerView_NewBook.setLayoutManager(new LinearLayoutManager(m_Context, LinearLayoutManager.HORIZONTAL, false));

        textView_MoreBestseller = (TextView)view.findViewById(R.id.textview_bookintroduction_morebestseller);
        textView_MoreBestseller.setTypeface(mTypeface_SeoulNamsanM);
        textView_MoreBestseller.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(m_Context, MoreBooksActivity.class);
                intent.putExtra("TYPE", 0);
                intent.putExtra("MoreBooks", mArrayList_Bestsellers);
                ((Activity)m_Context).startActivity(intent);
            }
        });
        textView_MoreRecommendationBook = (TextView)view.findViewById(R.id.textview_bookintroduction_morerecommendationbook);
        textView_MoreRecommendationBook.setTypeface(mTypeface_SeoulNamsanM);
        textView_MoreRecommendationBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(m_Context, MoreBooksActivity.class);
                intent.putExtra("TYPE", 1);
                intent.putExtra("MoreBooks", mArrayList_RecommendationBooks);
                ((Activity)m_Context).startActivity(intent);
            }
        });
        textView_MoreNewBook = (TextView)view.findViewById(R.id.textview_bookintroduction_morenewbook);
        textView_MoreNewBook.setTypeface(mTypeface_SeoulNamsanM);
        textView_MoreNewBook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(m_Context, MoreBooksActivity.class);
                intent.putExtra("TYPE", 2);
                intent.putExtra("MoreBooks", mArrayList_NewBooks);
                ((Activity)m_Context).startActivity(intent);
            }
        });

        BookIntroductionTask bookIntroductionTask = new BookIntroductionTask();
        bookIntroductionTask.execute(1);

        return view;
    }

    private class BookIntroductionTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute(){
            mArrayList_Bestsellers = new ArrayList<Book>();
            mArrayList_RecommendationBooks = new ArrayList<Book>();
            mArrayList_NewBooks = new ArrayList<Book>();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            getBestsellers();
            getRecommendationBooks();
            getNewBooks();
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(Integer result){
            mBookIntroductionAdapter_Bestseller = new BookIntroductionAdapter(m_Context, mArrayList_Bestsellers);
            mBookIntroductionAdapter_RecommendationBook = new BookIntroductionAdapter(m_Context, mArrayList_RecommendationBooks);
            mBookIntroductionAdapter_NewBook = new BookIntroductionAdapter(m_Context, mArrayList_NewBooks);

            mBookIntroductionAdapter_Bestseller = new BookIntroductionAdapter(m_Context, mArrayList_Bestsellers);
            mBookIntroductionAdapter_RecommendationBook = new BookIntroductionAdapter(m_Context, mArrayList_RecommendationBooks);
            mBookIntroductionAdapter_NewBook = new BookIntroductionAdapter(m_Context, mArrayList_NewBooks);

            recyclerView_Bestseller.setAdapter(mBookIntroductionAdapter_Bestseller);
            recyclerView_RecommendationBook.setAdapter(mBookIntroductionAdapter_RecommendationBook);
            recyclerView_NewBook.setAdapter(mBookIntroductionAdapter_NewBook);
        }
    }

    public void getBestsellers(){
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            HttpUrl.Builder builder = HttpUrl.parse("http://book.interpark.com/api/bestSeller.api").newBuilder();
            builder.addQueryParameter("key", "B93F2411D60A307D6AAD7ECECC680C1176B155B12729EDEA14746043E8EC6965");
            builder.addQueryParameter("categoryId", "100");

            String strUrl = builder.build().toString();

            final Request request = new Request.Builder()
                    .url(strUrl)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            try{
                String strTagName = "";
                Boolean bInItemTag = false;
                Boolean boolean_TitleTag = false;
                Boolean boolean_CoverTag = false;
                Boolean boolean_AuthorTag = false;
                Boolean boolean_PublisherTag = false;
                Boolean boolean_DateTag = false;
                Boolean boolean_UrlTag = false;
                Boolean boolean_IsbnTag = false;

                String string_Title = "";
                String string_Cover = "";
                String string_Author = "";
                String string_Publisher = "";
                String string_Date = "";
                String string_Url = "";
                String string_Isbn = "";

                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser= xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(response.body().byteStream(), "utf-8");
                //Log.d("stream", new InputStreamReader(response.body().byteStream()).toString());
                //xmlPullParser.setInput(new InputStreamReader(response.body().byteStream()));
                int iEventType= xmlPullParser.getEventType();

                while(iEventType != XmlPullParser.END_DOCUMENT){
                    switch (iEventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            strTagName = xmlPullParser.getName();

                            if(strTagName.equals("item")){
                                bInItemTag = true;
                                boolean_TitleTag = true;
                                boolean_CoverTag = true;
                                boolean_AuthorTag = true;
                                boolean_PublisherTag = true;
                                boolean_DateTag = true;
                                boolean_UrlTag = true;
                                boolean_IsbnTag = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            //strTagName = xmlPullParser.getName();

                            if(strTagName.equals("title") && bInItemTag && boolean_TitleTag){
                                string_Title = xmlPullParser.getText();
                            }else if(strTagName.equals("coverLargeUrl") && bInItemTag && boolean_CoverTag){
                                string_Cover = xmlPullParser.getText();
                            }else if(strTagName.equals("author") && bInItemTag && boolean_AuthorTag){
                                string_Author = xmlPullParser.getText();
                            }else if(strTagName.equals("publisher") && bInItemTag && boolean_PublisherTag){
                                string_Publisher = xmlPullParser.getText();
                            }else if(strTagName.equals("pubDate") && bInItemTag && boolean_DateTag){
                                string_Date = xmlPullParser.getText();
                            }else if(strTagName.equals("mobileLink") && bInItemTag && boolean_UrlTag){
                                string_Url = xmlPullParser.getText();
                            }else if(strTagName.equals("isbn") && bInItemTag && boolean_IsbnTag){
                                string_Isbn = xmlPullParser.getText();
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            strTagName = xmlPullParser.getName();

                            if(strTagName.equals("item")){
                                bInItemTag = false;

                                Book book = new Book(string_Title, string_Cover, string_Author, string_Publisher, string_Date, string_Url,
                                        string_Isbn, "", 0, 0);
                                mArrayList_Bestsellers.add(book);

                                string_Title = "";
                                string_Cover = "";
                                string_Author = "";
                                string_Publisher = "";
                                string_Date = "";
                                string_Url = "";
                                string_Isbn = "";
                            }else if(strTagName.equals("title")) {
                                boolean_TitleTag = false;
                            }else if(strTagName.equals("coverLargeUrl")){
                                boolean_CoverTag = false;
                            }else if(strTagName.equals("author")){
                                boolean_AuthorTag = false;
                            }else if(strTagName.equals("publisher")){
                                boolean_PublisherTag = false;
                            }else if(strTagName.equals("pubDate")){
                                boolean_DateTag = false;
                            }else if(strTagName.equals("mobileLink")){
                                boolean_UrlTag = false;
                            }else if(strTagName.equals("isbn")){
                                boolean_IsbnTag = false;
                            }
                            break;
                    }
                    iEventType = xmlPullParser.next();
                }

                response.body().close();
            }catch(Exception exception){

            }

        }catch(Exception exception){
            Log.d("Exception", "Exception");
        }
    }

    public void getRecommendationBooks(){
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            HttpUrl.Builder builder = HttpUrl.parse("http://book.interpark.com/api/recommend.api").newBuilder();
            builder.addQueryParameter("key", "B93F2411D60A307D6AAD7ECECC680C1176B155B12729EDEA14746043E8EC6965");
            builder.addQueryParameter("categoryId", "100");

            String strUrl = builder.build().toString();

            final Request request = new Request.Builder()
                    .url(strUrl)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            try{
                String strTagName = "";
                Boolean bInItemTag = false;
                Boolean boolean_TitleTag = false;
                Boolean boolean_CoverTag = false;
                Boolean boolean_AuthorTag = false;
                Boolean boolean_PublisherTag = false;
                Boolean boolean_DateTag = false;
                Boolean boolean_UrlTag = false;
                Boolean boolean_IsbnTag = false;

                String string_Title = "";
                String string_Cover = "";
                String string_Author = "";
                String string_Publisher = "";
                String string_Date = "";
                String string_Url = "";
                String string_Isbn = "";

                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser= xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(response.body().byteStream(), "utf-8");

                int iEventType= xmlPullParser.getEventType();

                while(iEventType != XmlPullParser.END_DOCUMENT){
                    switch (iEventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            strTagName = xmlPullParser.getName();

                            if(strTagName.equals("item")){
                                bInItemTag = true;
                                boolean_TitleTag = true;
                                boolean_CoverTag = true;
                                boolean_AuthorTag = true;
                                boolean_PublisherTag = true;
                                boolean_DateTag = true;
                                boolean_UrlTag = true;
                                boolean_IsbnTag = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            //strTagName = xmlPullParser.getName();

                            if(strTagName.equals("title") && bInItemTag && boolean_TitleTag){
                                string_Title = xmlPullParser.getText();
                            }else if(strTagName.equals("coverLargeUrl") && bInItemTag && boolean_CoverTag){
                                string_Cover = xmlPullParser.getText();
                            }else if(strTagName.equals("author") && bInItemTag && boolean_AuthorTag){
                                string_Author = xmlPullParser.getText();
                            }else if(strTagName.equals("publisher") && bInItemTag && boolean_PublisherTag){
                                string_Publisher = xmlPullParser.getText();
                            }else if(strTagName.equals("pubDate") && bInItemTag && boolean_DateTag){
                                string_Date = xmlPullParser.getText();
                            }else if(strTagName.equals("mobileLink") && bInItemTag && boolean_UrlTag){
                                string_Url = xmlPullParser.getText();
                            }else if(strTagName.equals("isbn") && bInItemTag && boolean_IsbnTag){
                                string_Isbn = xmlPullParser.getText();
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            strTagName = xmlPullParser.getName();

                            if(strTagName.equals("item")){
                                bInItemTag = false;

                                Book book = new Book(string_Title, string_Cover, string_Author, string_Publisher, string_Date, string_Url,
                                        string_Isbn, "", 0, 0);
                                mArrayList_RecommendationBooks.add(book);

                                string_Title = "";
                                string_Cover = "";
                                string_Author = "";
                                string_Publisher = "";
                                string_Date = "";
                                string_Url = "";
                                string_Isbn = "";
                            }else if(strTagName.equals("title")) {
                                boolean_TitleTag = false;
                            }else if(strTagName.equals("coverLargeUrl")){
                                boolean_CoverTag = false;
                            }else if(strTagName.equals("author")){
                                boolean_AuthorTag = false;
                            }else if(strTagName.equals("publisher")){
                                boolean_PublisherTag = false;
                            }else if(strTagName.equals("pubDate")){
                                boolean_DateTag = false;
                            }else if(strTagName.equals("mobileLink")){
                                boolean_UrlTag = false;
                            }else if(strTagName.equals("isbn")){
                                boolean_IsbnTag = false;
                            }
                            break;
                    }
                    iEventType = xmlPullParser.next();
                }

                response.body().close();
            }catch(Exception exception){

            }

        }catch(Exception exception){
            Log.d("Exception", "Exception");
        }
    }

    public void getNewBooks(){
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            HttpUrl.Builder builder = HttpUrl.parse("http://book.interpark.com/api/newBook.api").newBuilder();
            builder.addQueryParameter("key", "B93F2411D60A307D6AAD7ECECC680C1176B155B12729EDEA14746043E8EC6965");
            builder.addQueryParameter("categoryId", "100");

            String strUrl = builder.build().toString();

            final Request request = new Request.Builder()
                    .url(strUrl)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            try{
                String strTagName = "";
                Boolean bInItemTag = false;
                Boolean boolean_TitleTag = false;
                Boolean boolean_CoverTag = false;
                Boolean boolean_AuthorTag = false;
                Boolean boolean_PublisherTag = false;
                Boolean boolean_DateTag = false;
                Boolean boolean_UrlTag = false;
                Boolean boolean_IsbnTag = false;

                String string_Title = "";
                String string_Cover = "";
                String string_Author = "";
                String string_Publisher = "";
                String string_Date = "";
                String string_Url = "";
                String string_Isbn = "";

                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser= xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(response.body().byteStream(), "utf-8");

                int iEventType= xmlPullParser.getEventType();

                while(iEventType != XmlPullParser.END_DOCUMENT){
                    switch (iEventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            strTagName = xmlPullParser.getName();

                            if(strTagName.equals("item")){
                                bInItemTag = true;
                                boolean_TitleTag = true;
                                boolean_CoverTag = true;
                                boolean_AuthorTag = true;
                                boolean_PublisherTag = true;
                                boolean_DateTag = true;
                                boolean_UrlTag = true;
                                boolean_IsbnTag = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            //strTagName = xmlPullParser.getName();

                            if(strTagName.equals("title") && bInItemTag && boolean_TitleTag){
                                string_Title = xmlPullParser.getText();
                            }else if(strTagName.equals("coverLargeUrl") && bInItemTag && boolean_CoverTag){
                                string_Cover = xmlPullParser.getText();
                            }else if(strTagName.equals("author") && bInItemTag && boolean_AuthorTag){
                                string_Author = xmlPullParser.getText();
                            }else if(strTagName.equals("publisher") && bInItemTag && boolean_PublisherTag){
                                string_Publisher = xmlPullParser.getText();
                            }else if(strTagName.equals("pubDate") && bInItemTag && boolean_DateTag){
                                string_Date = xmlPullParser.getText();
                            }else if(strTagName.equals("mobileLink") && bInItemTag && boolean_UrlTag){
                                string_Url = xmlPullParser.getText();
                            }else if(strTagName.equals("isbn") && bInItemTag && boolean_IsbnTag){
                                string_Isbn = xmlPullParser.getText();
                            }

                            break;
                        case XmlPullParser.END_TAG:
                            strTagName = xmlPullParser.getName();

                            if(strTagName.equals("item")){
                                bInItemTag = false;

                                Book book = new Book(string_Title, string_Cover, string_Author, string_Publisher, string_Date, string_Url,
                                        string_Isbn, "", 0, 0);
                                mArrayList_NewBooks.add(book);

                                string_Title = "";
                                string_Cover = "";
                                string_Author = "";
                                string_Publisher = "";
                                string_Date = "";
                                string_Url = "";
                                string_Isbn = "";
                            }else if(strTagName.equals("title")) {
                                boolean_TitleTag = false;
                            }else if(strTagName.equals("coverLargeUrl")){
                                boolean_CoverTag = false;
                            }else if(strTagName.equals("author")){
                                boolean_AuthorTag = false;
                            }else if(strTagName.equals("publisher")){
                                boolean_PublisherTag = false;
                            }else if(strTagName.equals("pubDate")){
                                boolean_DateTag = false;
                            }else if(strTagName.equals("mobileLink")){
                                boolean_UrlTag = false;
                            }else if(strTagName.equals("isbn")){
                                boolean_IsbnTag = false;
                            }
                            break;
                    }
                    iEventType = xmlPullParser.next();
                }

                response.body().close();
            }catch(Exception exception){

            }

        }catch(Exception exception){
            Log.d("Exception", "Exception");
        }
    }
}
