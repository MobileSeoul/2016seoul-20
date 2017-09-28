package com.example.dokyeong.seoulreadingplace;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DoKyeong on 2016. 10. 24..
 */

public class InformationActivity extends AppCompatActivity {
    private Typeface mTypeface_SeoulNamsanM;

    private ImageView imageView_Back;

    private TextView textView_Title;
    private TextView textView_Version;
    private TextView textView_Copyright;

    private TextView textView_License0;
    private TextView textView_License1;
    private TextView textView_License2;
    private TextView textView_License3;
    private TextView textView_License4;
    private TextView textView_License5;
    private TextView textView_License6;
    private TextView textView_License7;
    private TextView textView_License8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        imageView_Back = (ImageView)findViewById(R.id.imageview_informaiton_back);
        textView_Title = (TextView)findViewById(R.id.textview_information_title);
        textView_Version = (TextView)findViewById(R.id.textview_information_version);
        textView_Copyright = (TextView)findViewById(R.id.textview_information_copyright);
        textView_License0 = (TextView)findViewById(R.id.license_fabtransitionLayout);
        textView_License1 = (TextView)findViewById(R.id.license_glide);
        textView_License2 = (TextView)findViewById(R.id.license_interpark);
        textView_License3 = (TextView)findViewById(R.id.license_library);
        textView_License4 = (TextView)findViewById(R.id.license_materialsheetfab);
        textView_License5 = (TextView)findViewById(R.id.license_okhttp);
        textView_License6 = (TextView)findViewById(R.id.license_seoul);
        textView_License7 = (TextView)findViewById(R.id.license_tedpermission);
        textView_License8 = (TextView)findViewById(R.id.license_zxingandroidembedded);


        textView_Title.setTypeface(mTypeface_SeoulNamsanM);
        textView_Version.setTypeface(mTypeface_SeoulNamsanM);
        textView_Copyright.setTypeface(mTypeface_SeoulNamsanM);
        textView_License0.setTypeface(mTypeface_SeoulNamsanM);
        textView_License1.setTypeface(mTypeface_SeoulNamsanM);
        textView_License2.setTypeface(mTypeface_SeoulNamsanM);
        textView_License3.setTypeface(mTypeface_SeoulNamsanM);
        textView_License4.setTypeface(mTypeface_SeoulNamsanM);
        textView_License5.setTypeface(mTypeface_SeoulNamsanM);
        textView_License6.setTypeface(mTypeface_SeoulNamsanM);
        textView_License7.setTypeface(mTypeface_SeoulNamsanM);
        textView_License8.setTypeface(mTypeface_SeoulNamsanM);

        imageView_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });




    }
}
