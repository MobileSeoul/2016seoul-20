package com.example.dokyeong.seoulreadingplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DoKyeong on 2016. 9. 24..
 */
public class BookSearchResultAdapter extends RecyclerView.Adapter<BookSearchResultAdapter.BookSearchResultViewHolder> {
    private Context m_Context;
    private ArrayList<Book> mArrayList_Books;
    private AlertDialog mAlertDialog_Record;
    private Typeface mTypeface_SeoulNamsanM;

    public BookSearchResultAdapter(Context context, ArrayList<Book> arrayList_Books) {
        m_Context = context;
        mArrayList_Books = arrayList_Books;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context.getAssets(), "seoulnamsan_m.otf");
    }

    @Override
    public BookSearchResultViewHolder onCreateViewHolder(ViewGroup viewGroup_Parent, int int_ViewType) {
        View view = LayoutInflater.from(viewGroup_Parent.getContext()).inflate(R.layout.item_booksearchresult, viewGroup_Parent, false);

        return new BookSearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookSearchResultViewHolder bookSearchResultViewHolder, int int_Position) {
        bookSearchResultViewHolder.textView_Title.setText(mArrayList_Books.get(int_Position).getTitle());
        Glide.with(m_Context).load(mArrayList_Books.get(int_Position).getCover()).into(bookSearchResultViewHolder.imageView_Cover);
        bookSearchResultViewHolder.textView_Author.setText(mArrayList_Books.get(int_Position).getAuthor());
        bookSearchResultViewHolder.textView_Publisher.setText(mArrayList_Books.get(int_Position).getPublisher());
        bookSearchResultViewHolder.textView_Date.setText(mArrayList_Books.get(int_Position).getDate());
    }

    @Override
    public int getItemCount() {
        return mArrayList_Books.size();
    }

    public class BookSearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_Title;
        private ImageView imageView_Cover;
        private TextView textView_Author;
        private TextView textView_Publisher;
        private TextView textView_Date;
        private ImageView imageView_Record;
        private ImageView imageView_Link;

        public BookSearchResultViewHolder(View view_Item) {
            super(view_Item);

            textView_Title = (TextView)view_Item.findViewById(R.id.textview_booksearchresult_title);
            imageView_Cover = (ImageView)view_Item.findViewById(R.id.imageview_booksearchresult_cover);
            textView_Author = (TextView)view_Item.findViewById(R.id.textview_booksearchresult_author);
            textView_Publisher = (TextView)view_Item.findViewById(R.id.textview_booksearchresult_publisher);
            textView_Date = (TextView)view_Item.findViewById(R.id.textview_booksearchresult_date);
            imageView_Record = (ImageView)view_Item.findViewById(R.id.imageview_booksearchresult_record);
            imageView_Link = (ImageView)view_Item.findViewById(R.id.imageview_booksearchresult_link);

            textView_Title.setTypeface(mTypeface_SeoulNamsanM);
            textView_Author.setTypeface(mTypeface_SeoulNamsanM);
            textView_Publisher.setTypeface(mTypeface_SeoulNamsanM);
            textView_Date.setTypeface(mTypeface_SeoulNamsanM);

            imageView_Record.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //int int_Position = getLayoutPosition();
                    //String test = mArrayList_Books.get(int_Position).getDescription();
                    //Toast.makeText(m_Context, "Record", Toast.LENGTH_SHORT).show();
                    //서재에 등록하시겠습니까?
                    // 왼쪽 아니요 오른쪽이 예
                    LayoutInflater layoutInflater = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view_Record = layoutInflater.inflate(R.layout.dialog_record, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
                    builder.setView(view_Record);
                    mAlertDialog_Record = builder.create();
                    mAlertDialog_Record.setCanceledOnTouchOutside(false);
                    mAlertDialog_Record.show();

                    final TextView view_Content1 = (TextView)view_Record.findViewById(R.id.textview_record_content1);
                    final TextView view_Content2 = (TextView)view_Record.findViewById(R.id.textview_record_content2);
                    TextView view_No = (TextView)view_Record.findViewById(R.id.textview_record_no);
                    TextView view_Yes = (TextView)view_Record.findViewById(R.id.textview_record_yes);

                    view_Content1.setTypeface(mTypeface_SeoulNamsanM);
                    view_Content2.setTypeface(mTypeface_SeoulNamsanM);
                    view_No.setTypeface(mTypeface_SeoulNamsanM);
                    view_Yes.setTypeface(mTypeface_SeoulNamsanM);


                    view_No.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            mAlertDialog_Record.dismiss();
                        }
                    });


                    view_Yes.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            view_Content1.setVisibility(View.INVISIBLE);
                            view_Content2.setVisibility(View.VISIBLE);

                            BookSearchResultTask bookSearchResultTask_Task = new BookSearchResultTask(m_Context);
                            bookSearchResultTask_Task.execute(mArrayList_Books.get(getLayoutPosition()));

                        }
                    });

                }
            });

            imageView_Link.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int int_Position = getLayoutPosition();
                    String string_Url = mArrayList_Books.get(int_Position).getUrl();
                    Log.d("URL", string_Url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(string_Url));
                    m_Context.startActivity(intent);

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
            long long_Today = System.currentTimeMillis();
            Date date_Today = new Date(long_Today);
            SimpleDateFormat simpleDateFormat_Today = new SimpleDateFormat("yyyy.MM.dd");
            String string_Today = simpleDateFormat_Today.format(date_Today);

            Book book_One = book_Books[0];
            book_One.setStart(string_Today);
            //mBookDBHelper_Database.addBook(book_Books[0]);
            mBookDBHelper_Database.addBook(book_One);
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(Integer result){
            Intent intent = new Intent();
            intent.putExtra("RECORD", 1);
            ((BookSearchActivity)m_Context).setResult(BookSearchActivity.CODE_BOOKSEARCH, intent);
            ((BookSearchActivity)m_Context).finish();
            mAlertDialog_Record.dismiss();
        }
    }
}
