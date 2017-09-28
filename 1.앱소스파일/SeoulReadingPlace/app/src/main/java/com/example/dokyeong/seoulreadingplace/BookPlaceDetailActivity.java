package com.example.dokyeong.seoulreadingplace;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by DoKyeong on 2016. 10. 15..
 */

public class BookPlaceDetailActivity extends AppCompatActivity {
    private ImageView imageView_Back;
    private TextView textView_Name;
    private ImageView imageView_Picture;

    private ImageView imageView_Map;
    private TextView textView_Map;
    private ImageView imageView_Link;
    private TextView textView_Link;
    private ImageView imageView_Call;
    private TextView textView_Call;

    private TextView textView_Address;
    private TextView textView_Website;
    private TextView textView_PhoneNumber;
    private TextView textView_Organization;

    private BookPlace mBookPlace_One;
    private int mInt_Picture;
    private Typeface mTypeface_SeoulNamsanM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookplacedetail);

        mBookPlace_One = (BookPlace)getIntent().getSerializableExtra("BOOKPLACE");
        mInt_Picture = (int)getIntent().getIntExtra("PICTURE", R.drawable.bookplace001);
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        imageView_Back = (ImageView)findViewById(R.id.imageview_bookplacedetail_back);
        textView_Name = (TextView)findViewById(R.id.textview_bookplacedetail_name);
        imageView_Picture = (ImageView)findViewById(R.id.imageview_bookplacedetail_picture);
        imageView_Map = (ImageView)findViewById(R.id.imageview_bookplacedetail_map);
        textView_Map = (TextView)findViewById(R.id.textview_bookplacedetail_map);
        imageView_Link = (ImageView)findViewById(R.id.imageview_bookplacedetail_link);
        textView_Link = (TextView)findViewById(R.id.textview_bookplacedetail_link);
        imageView_Call = (ImageView)findViewById(R.id.imageview_bookplacedetail_call);
        textView_Call = (TextView)findViewById(R.id.textview_bookplacedetail_call);
        textView_Address = (TextView)findViewById(R.id.textview_bookplacedetail_address);
        textView_Website = (TextView)findViewById(R.id.textview_bookplacedetail_website);
        textView_PhoneNumber = (TextView)findViewById(R.id.textview_bookplacedetail_phonenumber);
        textView_Organization = (TextView)findViewById(R.id.textview_bookplacedetail_organization);

        textView_Name.setTypeface(mTypeface_SeoulNamsanM);
        textView_Name.setText(mBookPlace_One.getName());
        Glide.with(this).load(mInt_Picture).into(imageView_Picture);
        textView_Map.setTypeface(mTypeface_SeoulNamsanM);
        textView_Link.setTypeface(mTypeface_SeoulNamsanM);
        textView_Address.setTypeface(mTypeface_SeoulNamsanM);
        textView_Call.setTypeface(mTypeface_SeoulNamsanM);
        textView_Address.setText(mBookPlace_One.getAddress());
        textView_Website.setTypeface(mTypeface_SeoulNamsanM);
        textView_Website.setText(mBookPlace_One.getWebsite());
        textView_PhoneNumber.setTypeface(mTypeface_SeoulNamsanM);
        textView_PhoneNumber.setText(mBookPlace_One.getPhoneNumber());
        textView_Organization.setTypeface(mTypeface_SeoulNamsanM);
        textView_Organization.setText(mBookPlace_One.getOrganization());

        imageView_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        imageView_Map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String string_Latitude = mBookPlace_One.getLatitude();
                String string_Longitude = mBookPlace_One.getLongitude();
                Uri uri_Coordinate = Uri.parse("geo:" + string_Latitude + "," + string_Longitude);
                Intent intent_Map = new Intent(Intent.ACTION_VIEW,uri_Coordinate);
                startActivity(intent_Map);
            }
        });

        imageView_Link.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String string_Website = mBookPlace_One.getWebsite();
                if(!string_Website.equals("")){
                    Uri uri_Website = Uri.parse(string_Website);
                    Intent intent_Link = new Intent(Intent.ACTION_VIEW, uri_Website);
                    startActivity(intent_Link);
                }
            }
        });

        imageView_Call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String string_PhoneNumber = mBookPlace_One.getPhoneNumber();
                if(!string_PhoneNumber.equals("")){
                    Uri uri_PhoneNumber = Uri.parse("tel:" + string_PhoneNumber);
                    Intent intent_Call = new Intent(Intent.ACTION_DIAL, uri_PhoneNumber);
                    startActivity(intent_Call);
                }
            }
        });


    }
}
