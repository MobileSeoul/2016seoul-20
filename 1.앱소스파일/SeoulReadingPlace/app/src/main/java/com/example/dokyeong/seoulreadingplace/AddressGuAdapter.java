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
 * Created by DoKyeong on 2016. 10. 28..
 */

public class AddressGuAdapter extends BaseAdapter{
    private Context mContext_System;
    private String[]  mArray_AddressGu;
    private LayoutInflater mLayoutInflater_;
    private Typeface mTypeface_SeoulNamsanM;

    public AddressGuAdapter(Context context_System, String[] array_AddressGu){
        mContext_System = context_System;
        mArray_AddressGu = array_AddressGu;
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(context_System.getAssets(), "seoulnamsan_m.otf");
    }

    @Override
    public int getCount() { return mArray_AddressGu.length; }

    @Override
    public Object getItem(int int_Position) { return mArray_AddressGu[int_Position]; }

    @Override
    public long getItemId(int int_Position) { return int_Position; }

    @Override
    public View getView(int int_Position, View view_ConvertView, ViewGroup viewGroup_Parent){
        View view_One = view_ConvertView;

        if(view_One == null){
            LayoutInflater layoutInflater = (LayoutInflater)mContext_System.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view_One = layoutInflater.inflate(R.layout.spinner_default, null);
        }

        String string_AddressGu = mArray_AddressGu[int_Position];

        if(null != string_AddressGu){

            TextView textView_Content = (TextView) view_One.findViewById(R.id.textview_default_content);
            textView_Content.setTypeface(mTypeface_SeoulNamsanM);
            textView_Content.setText(string_AddressGu);
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

        String string_AddressGu = mArray_AddressGu[int_Position];

        if(null != string_AddressGu){
            TextView textView_Content = (TextView) view_One.findViewById(R.id.textview_default_content);
            textView_Content.setTypeface(mTypeface_SeoulNamsanM);
            textView_Content.setText(string_AddressGu);
            View view_Line = (View)view_One.findViewById(R.id.view_default_line);
            view_Line.setVisibility(View.VISIBLE);
        }

        if(int_Position == (mArray_AddressGu.length-1)){
            View view_Line = (View)view_One.findViewById(R.id.view_default_line);
            view_Line.setVisibility(View.INVISIBLE);
        }

        return view_One;
    }
}
