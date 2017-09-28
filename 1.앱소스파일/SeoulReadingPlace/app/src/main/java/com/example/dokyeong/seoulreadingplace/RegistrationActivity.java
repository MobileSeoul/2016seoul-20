package com.example.dokyeong.seoulreadingplace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DoKyeong on 2016. 9. 16..
 */
public class RegistrationActivity extends AppCompatActivity {
    private String strTest;

    private String mString_Title = "Test";
    private String mString_Cover = "";
    private String mString_Author = "Test";
    private String mString_Publisher = "Test";
    private String mString_Date = "Test";

    private ImageView imageView_Cover;
    private EditText editText_Title;
    private EditText editText_Author;
    private EditText editText_Publisher;
    private EditText editText_Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
/*
        imageView_Cover = (ImageView)findViewById(R.id.imageview_registration_cover);
        editText_Title = (EditText)findViewById(R.id.edittext_registration_title);
        editText_Author = (EditText)findViewById(R.id.edittext_registration_author);
        editText_Publisher = (EditText)findViewById(R.id.edittext_registration_publisher);
        editText_Date = (EditText)findViewById(R.id.edittext_registration_date);

        int int_RegistrationMethod = getIntent().getIntExtra("RegistrationMethod", 2);

        if(int_RegistrationMethod == 0){
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.initiateScan();

        }else if(int_RegistrationMethod == 1){
            Intent intent = new Intent(this, BookSearchActivity.class);
            startActivity(intent);
        }else if(int_RegistrationMethod == 2){

        }else{

        }
        */

    }
    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                strTest = intentResult.getContents();
                Toast.makeText(this, "Scanned: " + intentResult.getContents(), Toast.LENGTH_LONG).show();

                asd asd = new asd();
                asd.execute();

                //new Thread(new Runnable() {
                //    @Override
                //    public void run() {
                //        getBookInformationByInterpark();
                //    }
                //}).start();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getBookInformationByInterpark(){
        try{
            OkHttpClient okHttpClient = new OkHttpClient();

            HttpUrl.Builder builder = HttpUrl.parse("http://book.interpark.com/api/search.api").newBuilder();
            builder.addQueryParameter("key", "B93F2411D60A307D6AAD7ECECC680C1176B155B12729EDEA14746043E8EC6965");
            builder.addQueryParameter("query", strTest);
            builder.addQueryParameter("queryType", "isbn");
            String strUrl = builder.build().toString();
            Log.d("URL", strUrl);
            final Request request = new Request.Builder()
                    .url(strUrl)
                    .build();

            //Response response = okHttpClient.newCall(request).execute();
            //strReturnData = response.body().string();
            //Log.d("ReturnData", response.toString());
            //Log.d("ReturnData1", response.message());
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("enqueue", "onFailure()");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    //String msg = null;
                    //while((msg = br.readLine())!=null) {
                    //    Log.d("INTERPARK", msg);
                    //}
                    //response.body().close();

                    try{
                        String strTagName = "";
                        String strText = "";
                        Boolean bInItemTag = false;

                        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                        XmlPullParser xmlPullParser= xmlPullParserFactory.newPullParser();
                        xmlPullParser.setInput(response.body().byteStream(), "utf-8");
                        //xmlPullParser.setInput(new InputStreamReader(response.body().byteStream()));
                        int iEventType= xmlPullParser.getEventType();

                        while(iEventType != XmlPullParser.END_DOCUMENT){
                            switch (iEventType){
                                case XmlPullParser.START_DOCUMENT:
                                    Log.d("START_DOCUMENT", "START_DOCUMENT");
                                    break;
                                case XmlPullParser.START_TAG:
                                    strTagName = xmlPullParser.getName();

                                    Log.d("START_TAG", strTagName);

                                    if(strTagName.equals("item")){
                                        bInItemTag = true;
                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    if(strTagName.equals("itemId") && bInItemTag){
                                        strText = xmlPullParser.getText();
                                        Log.d("ItemId", xmlPullParser.getText());
                                    }else if(strTagName.equals("title") && bInItemTag) {
                                        Log.d("Title", xmlPullParser.getText());
                                    }else if(strTagName.equals("url") && bInItemTag) {
                                        Log.d("Url", xmlPullParser.getText());
                                    }else if(strTagName.equals("author") && bInItemTag) {
                                        Log.d("Author", xmlPullParser.getText());
                                    }else if(strTagName.equals("description") && bInItemTag) {
                                        Log.d("Description", xmlPullParser.getText());
                                    }else if(strTagName.equals("publisher") && bInItemTag) {
                                        Log.d("Publisher", xmlPullParser.getText());
                                    }else if(strTagName.equals("pubDate") && bInItemTag) {
                                        Log.d("PubDate", xmlPullParser.getText());
                                    }else if(strTagName.equals("coverLargeUrl") && bInItemTag){
                                        strText = xmlPullParser.getText();

                                        //if(m_bTest){
                                        //    m_strTest = strText;
                                        //
                                        //    m_bTest = false;
                                        //}

                                        Log.d("CoverLargeUrl", strText);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    strTagName = xmlPullParser.getName();

                                    Log.d("END_TAG", strTagName);

                                    if(strTagName.equals("item")){
                                        bInItemTag = false;
                                    }
                                    break;
                            }
                            iEventType = xmlPullParser.next();
                        }

                        response.body().close();
                    }catch(Exception exception){

                    }


                }
            });
        }catch(Exception exception){

        }
    }

    private class asd extends AsyncTask<String, Void, Book> {
        // 1. 입력
        // 2. Progress
        // 3. 리턴
        @Override
        public void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        public Book doInBackground(String... params){
            Book book = new Book("", "", "", "", "", "", 0);
            try{
                OkHttpClient okHttpClient = new OkHttpClient();

                HttpUrl.Builder builder = HttpUrl.parse("http://book.interpark.com/api/search.api").newBuilder();
                builder.addQueryParameter("key", "B93F2411D60A307D6AAD7ECECC680C1176B155B12729EDEA14746043E8EC6965");
                builder.addQueryParameter("query", strTest);
                builder.addQueryParameter("queryType", "isbn");
                String strUrl = builder.build().toString();
                Log.d("URL", strUrl);
                final Request request = new Request.Builder().url(strUrl).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("enqueue", "onFailure()");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                        //String msg = null;
                        //while((msg = br.readLine())!=null) {
                        //    Log.d("INTERPARK", msg);
                        //}
                        //response.body().close();

                        try{
                            String strTagName = "";
                            String strText = "";
                            Boolean bInItemTag = false;

                            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                            XmlPullParser xmlPullParser= xmlPullParserFactory.newPullParser();
                            xmlPullParser.setInput(response.body().byteStream(), "utf-8");
                            //xmlPullParser.setInput(new InputStreamReader(response.body().byteStream()));
                            int iEventType= xmlPullParser.getEventType();

                            while(iEventType != XmlPullParser.END_DOCUMENT){
                                switch (iEventType){
                                    case XmlPullParser.START_DOCUMENT:
                                        Log.d("START_DOCUMENT", "START_DOCUMENT");
                                        break;
                                    case XmlPullParser.START_TAG:
                                        strTagName = xmlPullParser.getName();

                                        Log.d("START_TAG", strTagName);

                                        if(strTagName.equals("item")){
                                            bInItemTag = true;
                                        }
                                        break;
                                    case XmlPullParser.TEXT:
                                        strTagName = xmlPullParser.getName();

                                        Log.d("TEXT", strTagName);

                                        if(strTagName.equals("itemId") && bInItemTag){
                                            strText = xmlPullParser.getText();
                                            Log.d("ItemId", xmlPullParser.getText());
                                        }else if(strTagName.equals("title") && bInItemTag) {
                                            mString_Title = xmlPullParser.getText();
                                            Log.d("Title", xmlPullParser.getText());
                                        }else if(strTagName.equals("url") && bInItemTag) {
                                            Log.d("Url", xmlPullParser.getText());
                                        }else if(strTagName.equals("author") && bInItemTag) {
                                            mString_Author = xmlPullParser.getText();
                                            Log.d("Author", xmlPullParser.getText());
                                        }else if(strTagName.equals("description") && bInItemTag) {
                                            Log.d("Description", xmlPullParser.getText());
                                        }else if(strTagName.equals("publisher") && bInItemTag) {
                                            mString_Publisher = xmlPullParser.getText();
                                            Log.d("Publisher", xmlPullParser.getText());
                                        }else if(strTagName.equals("pubDate") && bInItemTag) {
                                            mString_Date = xmlPullParser.getText();
                                            Log.d("PubDate", xmlPullParser.getText());
                                        }else if(strTagName.equals("coverLargeUrl") && bInItemTag){
                                            mString_Cover = xmlPullParser.getText();
                                            Log.d("CoverLargeUrl", strText);
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        strTagName = xmlPullParser.getName();

                                        Log.d("END_TAG", strTagName);

                                        if(strTagName.equals("item")){
                                            bInItemTag = false;
                                        }
                                        break;
                                }
                                iEventType = xmlPullParser.next();
                            }

                            response.body().close();
                        }catch(Exception exception){

                        }


                    }
                });
            }catch(Exception exception){

            }


            return book;
        }

        @Override
        public void onPostExecute(Book result){
            Log.d("Title", mString_Title);
            Log.d("Author", mString_Author);
            Log.d("Title", mString_Publisher);
            Log.d("Date", mString_Date);

            Glide.with(getApplication()).load("http://bimage.interpark.com/goods_image/4/2/5/1/232554251s.jpg").into(imageView_Cover);
            editText_Title.setText(mString_Title);
            editText_Author.setText(mString_Author);
            editText_Publisher.setText(mString_Publisher);
            editText_Date.setText(mString_Date);
        }
        
        public void onProgressUpdate(Integer... values){

        }
    }
    */
}
