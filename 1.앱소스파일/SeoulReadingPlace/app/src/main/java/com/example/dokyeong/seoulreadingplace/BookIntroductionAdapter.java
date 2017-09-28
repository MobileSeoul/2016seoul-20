package com.example.dokyeong.seoulreadingplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 9. 19..
 */
public class BookIntroductionAdapter extends RecyclerView.Adapter<BookIntroductionAdapter.BookIntroductionViewHolder>{
    private Context m_Context;
    private ArrayList<Book> mArrayList_BookIntroductions;
    private Typeface mTypeface_SeoulNamsanM;

    public BookIntroductionAdapter(Context context, ArrayList<Book> arrayList_BookIntroductions){
        m_Context = context;
        mArrayList_BookIntroductions = arrayList_BookIntroductions;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context.getAssets(), "seoulnamsan_m.otf");
    }

    @Override
    public BookIntroductionViewHolder onCreateViewHolder(ViewGroup viewGroup_Parent, int int_ViewType){
        View view = LayoutInflater.from(viewGroup_Parent.getContext()).inflate(R.layout.item_bookintroduction, null);

        return new BookIntroductionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookIntroductionViewHolder bookIntroductionViewHolder, int int_Position){
        bookIntroductionViewHolder.textView_Title.setText(mArrayList_BookIntroductions.get(int_Position).getTitle());
        Glide.with(m_Context).load(mArrayList_BookIntroductions.get(int_Position).getCover()).into(bookIntroductionViewHolder.imageView_Cover);
        bookIntroductionViewHolder.textView_Author.setText(mArrayList_BookIntroductions.get(int_Position).getAuthor());
    }

    @Override
    public int getItemCount(){
        if(mArrayList_BookIntroductions.size() > 10){
            return 10;
        }else{
            return mArrayList_BookIntroductions.size();
        }
    }

    public class BookIntroductionViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_Cover;
        private TextView textView_Title;
        private TextView textView_Author;
        private ImageView imageView_Link;

        public BookIntroductionViewHolder(View view_Item){
            super(view_Item);

            imageView_Cover = (ImageView)view_Item.findViewById(R.id.imageview_bookintroduction_cover);
            textView_Title = (TextView)view_Item.findViewById(R.id.textview_bookintroduction_title);
            textView_Author = (TextView)view_Item.findViewById(R.id.textview_bookintroduction_author);
            imageView_Link = (ImageView)view_Item.findViewById(R.id.imageview_bookintroduction_link);

            textView_Title.setTypeface(mTypeface_SeoulNamsanM);
            textView_Author.setTypeface(mTypeface_SeoulNamsanM);

            imageView_Link.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int int_Position = getLayoutPosition();
                    String string_Url = mArrayList_BookIntroductions.get(int_Position).getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(string_Url));
                    m_Context.startActivity(intent);
                }
            });
        }
    }
}
