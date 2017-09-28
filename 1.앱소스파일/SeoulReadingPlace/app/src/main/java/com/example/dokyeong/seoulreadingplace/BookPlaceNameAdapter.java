package com.example.dokyeong.seoulreadingplace;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DoKyeong on 2016. 10. 30..
 */

public class BookPlaceNameAdapter extends BaseAdapter {
    private Context mContext_System;
    private ArrayList<String> mArrayList_BookPlaceName;
    private LayoutInflater mLayoutInflater_;
    private Typeface mTypeface_SeoulNamsanM;

    public BookPlaceNameAdapter(Context context_System, ArrayList<String> arrayList_BookPlaceName_AddressGu){
        mContext_System = context_System;
        mArrayList_BookPlaceName = arrayList_BookPlaceName_AddressGu;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context_System.getAssets(), "seoulnamsan_m.otf");
    }

    @Override
    public int getCount() { return mArrayList_BookPlaceName.size(); }

    @Override
    public Object getItem(int int_Position) { return mArrayList_BookPlaceName.get(int_Position); }

    @Override
    public long getItemId(int int_Position) { return int_Position; }

    @Override
    public View getView(int int_Position, View view_ConvertView, ViewGroup viewGroup_Parent){
        View view_One = view_ConvertView;

        if(view_One == null){
            LayoutInflater layoutInflater = (LayoutInflater)mContext_System.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view_One = layoutInflater.inflate(R.layout.spinner_default, null);
        }

        String string_BookPlaceName = mArrayList_BookPlaceName.get(int_Position);

        if(null != string_BookPlaceName){

            TextView textView_Content = (TextView) view_One.findViewById(R.id.textview_default_content);
            textView_Content.setTypeface(mTypeface_SeoulNamsanM);
            textView_Content.setText(string_BookPlaceName);
        }

        return view_One;
    }

    @Override
    public View getDropDownView(int int_Position, View view_ConvertView, ViewGroup viewGroup_Parent) {
        View view_One = view_ConvertView;

        if(view_One == null){
            LayoutInflater layoutInflater = (LayoutInflater)mContext_System.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view_One = layoutInflater.inflate(R.layout.dropdown_default, null);
        }

        String string_BookPlaceName = mArrayList_BookPlaceName.get(int_Position);

        if(null != string_BookPlaceName){
            TextView textView_Content = (TextView) view_One.findViewById(R.id.textview_default_content);
            textView_Content.setTypeface(mTypeface_SeoulNamsanM);
            textView_Content.setText(string_BookPlaceName);
            View view_Line = (View)view_One.findViewById(R.id.view_default_line);
            view_Line.setVisibility(View.VISIBLE);
        }

        if(int_Position == (mArrayList_BookPlaceName.size()-1)){
            View view_Line = (View)view_One.findViewById(R.id.view_default_line);
            view_Line.setVisibility(View.INVISIBLE);
        }

        return view_One;
    }
}
