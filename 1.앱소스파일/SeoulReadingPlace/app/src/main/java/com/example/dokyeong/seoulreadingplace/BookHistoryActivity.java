package com.example.dokyeong.seoulreadingplace;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 9. 28..
 */

public class BookHistoryActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int CODE_BOOKHISTORY = 7470;

    //private Spinner spinner_
    private ImageView imageView_Back;
    private TextView textView_Title;
    private ImageView imageView_Registration;
    private TextView textView_Date;
    private EditText editText_Page;
    private Spinner spinner_AddressGu;
    private Spinner spinner_BookPlaceName;
    private TextView textView_Address;
    private MapFragment mapFragment_Map;
    private GoogleMap mGoogleMap_Map;
    private TextView textView_DateName;
    private TextView textView_PageName;
    private TextView textView_AddressName;
    private CheckBox checkBox_Finish;

    private Book mBook_One;
    private BookHistory mBookHistory_One;
    private ArrayList<BookPlace> mArrayList_BookPlaces;
    private ArrayList<String> mArrayList_BookPlaceNames;
    private ArrayAdapter<String> mArrayAdapter_BookPlaceNames;
    private Typeface mTypeface_SeoulNamsanM;
    private String mString_BookPlaceName;
    private int mInt_BookPlaceCategory;
    private int mInt_Finish = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookhistory);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        imageView_Back = (ImageView)findViewById(R.id.imageview_bookhistory_back);
        textView_Title = (TextView)findViewById(R.id.textview_bookhistory_title);
        imageView_Registration = (ImageView)findViewById(R.id.imageview_bookhistory_registration);
        textView_Date = (TextView)findViewById(R.id.textview_bookhistory_date);
        editText_Page = (EditText)findViewById(R.id.edittext_bookhistory_page);
        spinner_AddressGu = (Spinner)findViewById(R.id.spinner_bookhistory_addressgu);
        spinner_BookPlaceName = (Spinner)findViewById(R.id.spinner_bookhistory_bookpalcename);
        textView_Address = (TextView)findViewById(R.id.textview_bookhistory_address);
        mapFragment_Map = (MapFragment)getFragmentManager().findFragmentById(R.id.fragment_bookhistory_map);
        textView_DateName = (TextView)findViewById(R.id.textview_bookhistory_datename);
        textView_PageName = (TextView)findViewById(R.id.textview_bookhistory_pagename);
        textView_AddressName = (TextView)findViewById(R.id.textview_bookhistory_addressname);
        checkBox_Finish = (CheckBox)findViewById(R.id.checkbox_bookhistory_finish);

        textView_Title.setTypeface(mTypeface_SeoulNamsanM);
        textView_DateName.setTypeface(mTypeface_SeoulNamsanM);
        textView_Date.setTypeface(mTypeface_SeoulNamsanM);
        textView_PageName.setTypeface(mTypeface_SeoulNamsanM);
        textView_Address.setTypeface(mTypeface_SeoulNamsanM);
        textView_AddressName.setTypeface(mTypeface_SeoulNamsanM);
        checkBox_Finish.setTypeface(mTypeface_SeoulNamsanM);

        mBook_One = (Book)(getIntent().getSerializableExtra("BOOK"));
        mArrayList_BookPlaces = (ArrayList<BookPlace>)getIntent().getSerializableExtra("BOOKPLACES");

        imageView_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                Intent intent = new Intent();
                intent.putExtra("RECORD",0);
                setResult(BookHistoryActivity.CODE_BOOKHISTORY, intent);
                finish();
            }
        });

        textView_Title.setText(mBook_One.getTitle());

        imageView_Registration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                //Book
                mBook_One.setPage(Integer.valueOf(editText_Page.getText().toString()));
                mBook_One.setFinish(mInt_Finish);
                mBookHistory_One = new BookHistory(mBook_One.getTitle(), mBook_One.getIsbn(),
                        mInt_BookPlaceCategory, mString_BookPlaceName, textView_Date.getText().toString());

                BookHistoryTask task_BookHistory = new BookHistoryTask(BookHistoryActivity.this, mBook_One, mBookHistory_One);
                task_BookHistory.execute();
            }
        });

        textView_Date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                DatePickerDialog datePickerDialog_Date = new DatePickerDialog(BookHistoryActivity.this, listener_Date, 2016, 10, 1);
                datePickerDialog_Date.show();
            }
        });

        editText_Page.setText(String.valueOf(mBook_One.getPage()));

        checkBox_Finish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.checkbox_bookhistory_finish) {
                    if (isChecked) {
                        mInt_Finish = 1;
                    } else {
                        mInt_Finish = 0;
                    }
                }
            }
        });

        String[] array_AddressGu = {"강서구", "양천구", "구로구", "영등포구", "금천구", "관악구", "동작구", "용산구", "서초구", "강남구", "송파구", "강동구",
                "광진구", "성동구", "중구", "동대문구", "중랑구", "노원구", "도봉구", "강북구", "은평구", "서대문구", "종로구", "성북구"};
        AddressGuAdapter adapter_AddressGu = new AddressGuAdapter(this, array_AddressGu);

        spinner_AddressGu.setAdapter(adapter_AddressGu);

        spinner_AddressGu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mArrayList_BookPlaceNames = new ArrayList<String>();
                for(int int_Index = 0; int_Index < mArrayList_BookPlaces.size(); int_Index++){
                    if(parent.getItemAtPosition(position).toString().equals(mArrayList_BookPlaces.get(int_Index).getAddressGu())){
                        mArrayList_BookPlaceNames.add(mArrayList_BookPlaces.get(int_Index).getName());

                        BookPlaceNameAdapter adapter_BookPlaceName = new BookPlaceNameAdapter(BookHistoryActivity.this, mArrayList_BookPlaceNames);

                        spinner_BookPlaceName.setAdapter(adapter_BookPlaceName);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_BookPlaceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int int_Position, long id) {
                mString_BookPlaceName = "";
                String string_Address = "";
                int int_Category = 0;
                Double double_Latitude = 0.0;
                Double double_Longitude = 0.0;

                for(int int_Index = 0; int_Index < mArrayList_BookPlaces.size(); int_Index++){
                    if(parent.getItemAtPosition(int_Position).toString().equals(mArrayList_BookPlaces.get(int_Index).getName())){
                        mString_BookPlaceName = mArrayList_BookPlaces.get(int_Index).getName();
                        mInt_BookPlaceCategory = mArrayList_BookPlaces.get(int_Index).getCategory();
                        string_Address = mArrayList_BookPlaces.get(int_Index).getAddress();
                        int_Category = mArrayList_BookPlaces.get(int_Index).getCategory();
                        double_Latitude = Double.valueOf(mArrayList_BookPlaces.get(int_Index).getLatitude());
                        double_Longitude = Double.valueOf(mArrayList_BookPlaces.get(int_Index).getLongitude());
                        break;
                    }
                }

                textView_Address.setText(string_Address);
                mGoogleMap_Map.clear();
                LatLng latLng_Location = new LatLng(double_Latitude, double_Longitude);

                MarkerOptions markerOptions_BookPlace = new MarkerOptions();
                markerOptions_BookPlace.title(mString_BookPlaceName);
                markerOptions_BookPlace.position(latLng_Location);
                if(int_Category == 0){
                    markerOptions_BookPlace.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_library_48dp));
                }else{
                    markerOptions_BookPlace.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_bookstore_48dp));
                }
                mGoogleMap_Map.addMarker(markerOptions_BookPlace);
                mGoogleMap_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_Location, 15));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mapFragment_Map.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("RECORD",0);
        setResult(BookHistoryActivity.CODE_BOOKHISTORY, intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap_Map){
        mGoogleMap_Map = googleMap_Map;
    }

    private DatePickerDialog.OnDateSetListener listener_Date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            textView_Date.setText(String.valueOf(year) + "." + String.valueOf(monthOfYear+1) + "." +String.valueOf(dayOfMonth));
        }
    };

    private class BookHistoryTask extends AsyncTask<Book, Integer, Integer> {
        private BookHistorySQLiteOpenHelper mSqLiteOpenHelper_BookHistory;
        private BookDBHelper mSqLiteOpenHelper_Book;
        private Book mBook_One;
        private BookHistory mBookHistory_One;

        public BookHistoryTask(Context context, Book book_One, BookHistory bookHistory_One){
            mSqLiteOpenHelper_BookHistory = BookHistorySQLiteOpenHelper.getInstance(context);
            mSqLiteOpenHelper_Book = BookDBHelper.getInstance(context);

            mBook_One = book_One;
            mBookHistory_One = bookHistory_One;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Book... book_Books) {

            mSqLiteOpenHelper_Book.updateBook(mBook_One);
            mSqLiteOpenHelper_BookHistory.addBookHistory(mBookHistory_One);

            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(Integer integer_Result){
            Toast.makeText(BookHistoryActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra("RECORD",1);
            setResult(BookHistoryActivity.CODE_BOOKHISTORY, intent);
            finish();
        }
    }
}
