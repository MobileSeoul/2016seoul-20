package com.example.dokyeong.seoulreadingplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 30..
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Typeface mTypeface_SeoulNamsanM;
    private GoogleMap mGoogleMap_Map;
    private ArrayList<BookHistory> mArrayList_BookHistories;
    private ArrayList<BookPlace> mArrayList_BookPlaces;

    private ImageView imageView_Back;
    private TextView textView_Title;
    private MapFragment mapFragment_Map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        mArrayList_BookHistories = (ArrayList<BookHistory>)getIntent().getSerializableExtra("BOOKHISTORIES");
        mArrayList_BookPlaces = (ArrayList<BookPlace>)getIntent().getSerializableExtra("BOOKPLACES");

        imageView_Back = (ImageView)findViewById(R.id.imageview_map_back);
        textView_Title = (TextView)findViewById(R.id.textview_map_title);
        mapFragment_Map = (MapFragment)getFragmentManager().findFragmentById(R.id.fragment_map_map);

        textView_Title.setTypeface(mTypeface_SeoulNamsanM);
        imageView_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                finish();
            }
        });

        mapFragment_Map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap_Map) {
        mGoogleMap_Map = googleMap_Map;

        LatLng latLng_DefaultLocation = new LatLng(37.553946, 126.980948);
        mGoogleMap_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_DefaultLocation, 10));

        for(int int_OuterIndex = 0; int_OuterIndex < mArrayList_BookHistories.size(); int_OuterIndex++){
            for(int int_InnerIndex = 0; int_InnerIndex < mArrayList_BookPlaces.size(); int_InnerIndex++){
                if(mArrayList_BookHistories.get(int_OuterIndex).getName().equals(mArrayList_BookPlaces.get(int_InnerIndex).getName())){
                    Log.d("Map_Size", String.valueOf(mArrayList_BookHistories.size()));
                    Log.d("Place_Size", String.valueOf(mArrayList_BookPlaces.size()));
                    String string_Name = mArrayList_BookPlaces.get(int_InnerIndex).getName();
                    Double double_Latitude = Double.valueOf(mArrayList_BookPlaces.get(int_InnerIndex).getLatitude());
                    Double double_Longitude = Double.valueOf(mArrayList_BookPlaces.get(int_InnerIndex).getLongitude());

                    LatLng latLng_Location = new LatLng(double_Latitude, double_Longitude);
                    MarkerOptions markerOptions_BookPlace = new MarkerOptions();
                    markerOptions_BookPlace.title(string_Name);
                    markerOptions_BookPlace.position(latLng_Location);
                    if(mArrayList_BookPlaces.get(int_InnerIndex).getCategory() == 0){
                        markerOptions_BookPlace.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_library_48dp));
                    }else{
                        markerOptions_BookPlace.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_bookstore_48dp));
                    }
                    mGoogleMap_Map.addMarker(markerOptions_BookPlace);
                    break;
                }
            }
        }
    }

}
