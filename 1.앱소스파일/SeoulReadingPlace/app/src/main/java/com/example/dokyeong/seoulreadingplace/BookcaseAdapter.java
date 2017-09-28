package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 9. 4..
 */
public class BookcaseAdapter extends RecyclerView.Adapter<BookcaseAdapter.BookcaseViewHolder>{
    private Context m_Context;
    private ArrayList<Book> mArrayList_Books;
    private ArrayList<BookPlace> mArrayList_BookPlaces;
    private AlertDialog mAlertDialog_History;
    private Typeface mTypeface_SeoulNamsanM;
    // 특수효과 - 아이들 사진 촬영,
    public BookcaseAdapter(Context context, ArrayList<Book> arrayList_Books, ArrayList<BookPlace> arrayList_BookPlaces){
        m_Context = context;
        mArrayList_Books = arrayList_Books;
        mArrayList_BookPlaces = arrayList_BookPlaces;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context.getAssets(), "seoulnamsan_m.otf");
    }

    @Override
    public BookcaseViewHolder onCreateViewHolder(ViewGroup viewGroup_Parent, int int_ViewType){
        View view = LayoutInflater.from(viewGroup_Parent.getContext()).inflate(R.layout.item_bookcase, null);

        return new BookcaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookcaseViewHolder bookcaseViewHolder, int int_Position){
        Glide.with(m_Context).load(mArrayList_Books.get(int_Position).getCover()).into(bookcaseViewHolder.imageView_Cover);
        bookcaseViewHolder.textView_Title.setText(mArrayList_Books.get(int_Position).getTitle());
        bookcaseViewHolder.textView_Author.setText(mArrayList_Books.get(int_Position).getAuthor());
        if(mArrayList_Books.get(int_Position).getFinish() == 1){
            bookcaseViewHolder.imageView_Finish.setImageResource(R.mipmap.icon_finish);
        }
    }

    @Override
    public int getItemCount(){
        //return 6;
        return mArrayList_Books.size();
    }

    public class BookcaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView_Cover;
        private TextView textView_Title;
        private TextView textView_Author;
        private ImageView imageView_Finish;

        public BookcaseViewHolder(View view_Item){
            super(view_Item);

            imageView_Cover = (ImageView)view_Item.findViewById(R.id.imageview_bookcase_cover);
            textView_Title = (TextView)view_Item.findViewById(R.id.textview_bookcase_title);
            textView_Author = (TextView)view_Item.findViewById(R.id.textview_bookcase_author);
            imageView_Finish = (ImageView)view_Item.findViewById(R.id.imageview_bookcase_finish);

            textView_Title.setTypeface(mTypeface_SeoulNamsanM);
            textView_Author.setTypeface(mTypeface_SeoulNamsanM);
            view_Item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view_Item) {
            LayoutInflater layoutInflater = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_Record = layoutInflater.inflate(R.layout.dialog_history, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
            builder.setView(view_Record);
            mAlertDialog_History = builder.create();
            mAlertDialog_History.setCanceledOnTouchOutside(false);
            mAlertDialog_History.show();

            final TextView view_Content1 = (TextView)view_Record.findViewById(R.id.textview_history_content1);
            final TextView view_Content2 = (TextView)view_Record.findViewById(R.id.textview_history_content2);
            TextView view_No = (TextView)view_Record.findViewById(R.id.textview_history_no);
            TextView view_Yes = (TextView)view_Record.findViewById(R.id.textview_history_yes);

            view_Content1.setTypeface(mTypeface_SeoulNamsanM);
            view_Content2.setTypeface(mTypeface_SeoulNamsanM);

            view_No.setTypeface(mTypeface_SeoulNamsanM);
            view_Yes.setTypeface(mTypeface_SeoulNamsanM);


            view_No.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(view_Content1.getVisibility() == View.VISIBLE){
                        view_Content1.setVisibility(View.GONE);
                        view_Content2.setVisibility(View.VISIBLE);
                    }else{
                        mAlertDialog_History.dismiss();
                    }


                }
            });


            view_Yes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(view_Content2.getVisibility() == View.GONE){
                        Intent intent_BookHistory = new Intent(m_Context, BookHistoryActivity.class);
                        intent_BookHistory.putExtra("BOOK", mArrayList_Books.get(getLayoutPosition()));
                        intent_BookHistory.putExtra("BOOKPLACES", mArrayList_BookPlaces);
                        ((Activity)m_Context).startActivityForResult(intent_BookHistory, BookcaseFragment.CODE_BOOKCASE);
                    }else{
                        Intent intent_BookDetail = new Intent(m_Context, BookDetailActivity.class);
                        intent_BookDetail.putExtra("BOOK", mArrayList_Books.get(getLayoutPosition()));
                        intent_BookDetail.putExtra("BOOKPLACES", mArrayList_BookPlaces);
                        m_Context.startActivity(intent_BookDetail);
                    }
                    mAlertDialog_History.dismiss();
                }
            });
        }
    }
}
