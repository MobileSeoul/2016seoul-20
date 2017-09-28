package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 12..
 */

public class BookPlaceListAdapter extends RecyclerView.Adapter<BookPlaceListAdapter.BookListListViewHolder>{
    private Context mContext_System;
    private ArrayList<BookPlace> mArrayList_BookPlaces;
    private ArrayList<Integer> mArrayList_Pictures;
    private Typeface mTypeface_SeoulNamsanM;

    public BookPlaceListAdapter(Context context_System, ArrayList<BookPlace> arrayList_BookPlaces, ArrayList<Integer> arrayList_Pictures) {
        mContext_System = context_System;
        mArrayList_BookPlaces = arrayList_BookPlaces;
        mArrayList_Pictures = arrayList_Pictures;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context_System.getAssets(), "seoulnamsan_m.otf");
    }

    @Override
    public BookPlaceListAdapter.BookListListViewHolder onCreateViewHolder(ViewGroup viewGroup_Parent, int int_ViewType) {
        View view = LayoutInflater.from(viewGroup_Parent.getContext()).inflate(R.layout.item_bookplacelist, viewGroup_Parent, false);

        return new BookPlaceListAdapter.BookListListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookPlaceListAdapter.BookListListViewHolder viewHolder_BookList, int int_Position) {
        Glide.with(mContext_System).load(mArrayList_Pictures.get(int_Position)).into(viewHolder_BookList.imageView_Background);
        viewHolder_BookList.textView_Title.setText(mArrayList_BookPlaces.get(int_Position).getName());
        viewHolder_BookList.textView_Address.setText(mArrayList_BookPlaces.get(int_Position).getPhoneNumber());
    }


    @Override
    public int getItemCount() {
        return mArrayList_BookPlaces.size();
    }

    public class BookListListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_Background;
        private TextView textView_Title;
        private TextView textView_Address;

        public BookListListViewHolder(View view_One) {
            super(view_One);

            imageView_Background = (ImageView) view_One.findViewById(R.id.imageview_bookplacelist_background);
            textView_Title = (TextView) view_One.findViewById(R.id.textview_bookplacelist_title);
            textView_Address = (TextView) view_One.findViewById(R.id.textview_bookplacelist_address);

            textView_Title.setTypeface(mTypeface_SeoulNamsanM);
            textView_Address.setTypeface(mTypeface_SeoulNamsanM);
            view_One.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view_One){
                    Intent intent_BookPlaceDetail = new Intent(mContext_System, BookPlaceDetailActivity.class);
                    intent_BookPlaceDetail.putExtra("BOOKPLACE", mArrayList_BookPlaces.get(getLayoutPosition()));
                    intent_BookPlaceDetail.putExtra("PICTURE", mArrayList_Pictures.get(getLayoutPosition()));
                    ((Activity)mContext_System).startActivity(intent_BookPlaceDetail);
                }
            });
        }


    }

    private class BookSearchResultTask extends AsyncTask<Book, Integer, Integer> {
        private Context m_Context;
        private BookDBHelper mBookDBHelper_Database;

        public BookSearchResultTask(Context context){
            m_Context = context;

            mBookDBHelper_Database = BookDBHelper.getInstance(context);
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Book... book_Books) {
            mBookDBHelper_Database.addBook(book_Books[0]);
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(Integer result){
            Log.d("onPostExecute", "onPostExecute");
            Intent intent = new Intent();
            intent.putExtra("RECORD", 1);
            ((BookSearchActivity)m_Context).setResult(BookSearchActivity.CODE_BOOKSEARCH, intent);
            ((BookSearchActivity)m_Context).finish();
        }
    }
}
