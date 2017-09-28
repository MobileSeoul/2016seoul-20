package com.example.dokyeong.seoulreadingplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DoKyeong on 2016. 9. 16..
 */
public class BookSearchActivity extends AppCompatActivity {
    // 책 저장할때 중복 안되게 함

    public static final int CODE_BOOKSEARCH = 7460;

    private ImageView imageView_Back;
    private EditText editText_Keyword;
    private ImageView imageView_Search;
    private RecyclerView recyclerView_Result;

    private BookSearchResultAdapter mAdapter_Result;

    private ArrayList<Book> mArrayList_Books;

    private InputMethodManager mIntputMethodManager_Keyboard;
    private Typeface mTypeface_SeoulNamsanM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksearch);

        mIntputMethodManager_Keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        imageView_Back = (ImageView)findViewById(R.id.imageview_booksearch_back);
        editText_Keyword = (EditText)findViewById(R.id.edittext_booksearch_keyword);
        imageView_Search = (ImageView)findViewById(R.id.imageview_booksearch_search);
        recyclerView_Result = (RecyclerView)findViewById(R.id.recyclerview_booksearch_result);

        editText_Keyword.setTypeface(mTypeface_SeoulNamsanM);

        imageView_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.putExtra("RECORD", 0);
                setResult(BookSearchActivity.CODE_BOOKSEARCH, intent);
                finish();
            }
        });

        recyclerView_Result.setHasFixedSize(true);
        recyclerView_Result.setLayoutManager(new LinearLayoutManager(this));
        imageView_Search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                mIntputMethodManager_Keyboard.hideSoftInputFromWindow(editText_Keyword.getWindowToken(), 0);
                BookSearchTask bookSearchTask = new BookSearchTask(BookSearchActivity.this, 1);
                bookSearchTask.execute(editText_Keyword.getText().toString());
            }
        });

        editText_Keyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int int_KeyCode, KeyEvent event) {
                if(int_KeyCode == KeyEvent.KEYCODE_ENTER){
                    mIntputMethodManager_Keyboard.hideSoftInputFromWindow(editText_Keyword.getWindowToken(), 0);
                    BookSearchTask bookSearchTask = new BookSearchTask(BookSearchActivity.this, 1);
                    bookSearchTask.execute(editText_Keyword.getText().toString());
                    return true;
                }
                return false;
            }
        });

        int int_SearchMethod = getIntent().getIntExtra("SearchMethod", 1);

        if(int_SearchMethod == 0){
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.initiateScan();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("RECORD",0);
        setResult(BookSearchActivity.CODE_BOOKSEARCH, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                editText_Keyword.setText(result.getContents());
                BookSearchTask bookSearchTask = new BookSearchTask(BookSearchActivity.this, 0);
                bookSearchTask.execute(result.getContents());
                Log.d("asd", result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class BookSearchTask extends AsyncTask<String, Integer, Integer> {
        private Context m_Context;
        private int mInt_SearchType;

        public BookSearchTask(Context context, int int_SearchType){
            m_Context = context;
            mInt_SearchType = int_SearchType;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            mArrayList_Books = new ArrayList<Book>();
        }

        @Override
        protected Integer doInBackground(String... string_Queries) {
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                HttpUrl.Builder builder = HttpUrl.parse("http://book.interpark.com/api/search.api").newBuilder();
                builder.addQueryParameter("key", "B93F2411D60A307D6AAD7ECECC680C1176B155B12729EDEA14746043E8EC6965");
                builder.addQueryParameter("query", string_Queries[0]);
                builder.addQueryParameter("maxResults", "50");
                if(mInt_SearchType == 0){
                    builder.addQueryParameter("queryType", "isbn");
                }
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
                        switch (iEventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                strTagName = xmlPullParser.getName();

                                if (strTagName.equals("item")) {
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

                                Log.d("TEXT", strTagName);

                                if (strTagName.equals("title") && bInItemTag && boolean_TitleTag) {
                                    string_Title = xmlPullParser.getText();
                                } else if (strTagName.equals("coverLargeUrl") && bInItemTag && boolean_CoverTag) {
                                    string_Cover = xmlPullParser.getText();
                                } else if (strTagName.equals("author") && bInItemTag && boolean_AuthorTag) {
                                    string_Author = xmlPullParser.getText();
                                } else if (strTagName.equals("publisher") && bInItemTag && boolean_PublisherTag) {
                                    string_Publisher = xmlPullParser.getText();
                                } else if (strTagName.equals("pubDate") && bInItemTag && boolean_DateTag) {
                                    string_Date = xmlPullParser.getText();
                                } else if (strTagName.equals("mobileLink") && bInItemTag && boolean_UrlTag) {
                                    string_Url = xmlPullParser.getText();
                                } else if(strTagName.equals("isbn") && bInItemTag && boolean_IsbnTag){
                                    string_Isbn = xmlPullParser.getText();
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                strTagName = xmlPullParser.getName();

                                if(strTagName.equals("item")){
                                    bInItemTag = false;

                                    string_Date = string_Date.substring(0, 4) + "." + string_Date.substring(4, 6) + "." + string_Date.substring(6, 8);

                                    Book book = new Book(string_Title, string_Cover, string_Author, string_Publisher, string_Date, string_Url,
                                            string_Isbn, "", 0, 0);
                                    mArrayList_Books.add(book);

                                    string_Title = "";
                                    string_Cover = "";
                                    string_Author = "";
                                    string_Publisher = "";
                                    string_Date = "";
                                    string_Url = "";
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

                Log.d("doInBackground", "doInBackground");

            }catch(Exception exception){
                Log.d("Exception", "Exception");
            }
            Log.d("doInBackground", "doInBackground");
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(Integer result){
            Log.d("onPostExecute", "onPostExecute");
            mAdapter_Result = new BookSearchResultAdapter(m_Context, mArrayList_Books);
            recyclerView_Result.setAdapter(mAdapter_Result);
        }
    }
}
