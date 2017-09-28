package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 23..
 */

public class BookDetailAdapter extends RecyclerView.Adapter<BookDetailAdapter.BookDetailViewHolder>{
    private Context mContext_System;
    private ArrayList<BookHistory> mArrayList_BookHistories;
    private Typeface mTypeface_SeoulNamsanM;

    public BookDetailAdapter(Context context_System, ArrayList<BookHistory> arrayList_BookHistories) {
        mContext_System = context_System;
        mArrayList_BookHistories = arrayList_BookHistories;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context_System.getAssets(), "seoulnamsan_m.otf");
    }

    @Override
    public BookDetailAdapter.BookDetailViewHolder onCreateViewHolder(ViewGroup viewGroup_Parent, int int_ViewType) {
        View view = LayoutInflater.from(viewGroup_Parent.getContext()).inflate(R.layout.item_bookdetail, viewGroup_Parent, false);

        return new BookDetailAdapter.BookDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookDetailAdapter.BookDetailViewHolder viewHolder_BookDetail, int int_Position) {
        if(mArrayList_BookHistories.get(int_Position).getCategory() == 0){
            viewHolder_BookDetail.imageView_Category.setImageResource(R.mipmap.icon_library_40dp);
        }else{
            viewHolder_BookDetail.imageView_Category.setImageResource(R.mipmap.icon_bookstore_40dp);
        }
        viewHolder_BookDetail.textView_Name.setText(mArrayList_BookHistories.get(int_Position).getName());
        viewHolder_BookDetail.textView_Date.setText(mArrayList_BookHistories.get(int_Position).getDate());
    }


    @Override
    public int getItemCount() {
        return mArrayList_BookHistories.size();
    }

    public class BookDetailViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_Category;
        private TextView textView_Name;
        private TextView textView_Date;

        public BookDetailViewHolder(View view_One) {
            super(view_One);

            imageView_Category = (ImageView) view_One.findViewById(R.id.imageview_bookdetail_category);
            textView_Name = (TextView) view_One.findViewById(R.id.textview_bookdetail_name);
            textView_Date = (TextView) view_One.findViewById(R.id.textview_bookdetail_date);

            textView_Name.setTypeface(mTypeface_SeoulNamsanM);
            textView_Date.setTypeface(mTypeface_SeoulNamsanM);
        }


    }
}
