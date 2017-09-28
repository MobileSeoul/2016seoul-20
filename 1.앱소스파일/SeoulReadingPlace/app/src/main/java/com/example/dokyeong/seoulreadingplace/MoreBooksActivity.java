package com.example.dokyeong.seoulreadingplace;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 8..
 */

public class MoreBooksActivity extends AppCompatActivity {
    private ImageView imageView_Back;
    private TextView textView_Type;
    private RecyclerView recyclerView_MoreBooks;

    private Typeface mTypeface_SeoulNamsanM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morebooks);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        int int_Type = (int)getIntent().getIntExtra("TYPE", 0);
        ArrayList<Book> arrayList_MoreBooks = (ArrayList<Book>)getIntent().getSerializableExtra("MoreBooks");

        imageView_Back = (ImageView)findViewById(R.id.imageview_moreboks_back);
        textView_Type = (TextView)findViewById(R.id.textview_moreboks_type);

        imageView_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                finish();
            }
        });

        textView_Type.setTypeface(mTypeface_SeoulNamsanM);

        if(int_Type == 0){
            textView_Type.setText("베스트셀러");
        }else if(int_Type == 1){
            textView_Type.setText("추천도서");
        }else if(int_Type == 2){
            textView_Type.setText("신간도서");
        }else{

        }

        GridLayoutManager gridLayoutManager_MoreBooks = new GridLayoutManager(this, 3);

        recyclerView_MoreBooks = (RecyclerView)findViewById(R.id.recyclerview_morebooks_morebooks);
        recyclerView_MoreBooks.setHasFixedSize(true);
        recyclerView_MoreBooks.setLayoutManager(gridLayoutManager_MoreBooks);

        MoreBooksAdapter moreBooksAdapter = new MoreBooksAdapter(this, arrayList_MoreBooks);

        recyclerView_MoreBooks.setAdapter(moreBooksAdapter);
    }
}
