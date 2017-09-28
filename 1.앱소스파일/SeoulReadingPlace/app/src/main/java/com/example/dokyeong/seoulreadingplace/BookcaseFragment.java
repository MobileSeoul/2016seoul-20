package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by DoKyeong on 2016. 8. 23..
 */
public class BookcaseFragment extends Fragment {
    public static final int CODE_BOOKCASE = 7450;

    private RecyclerView recyclerView_Bookcase;
    private ImageButton imageButton_Registration;
    private FloatingButton floatingButton_Registration;
    private View view_Overlay;
    private AlertDialog alertDialg_RegistrationMethod;

    private Context m_Context;
    private GridLayoutManager m_GridLayoutManager;
    private GridLayoutManager mLayoutManager_Bookcase;
    private BookcaseAdapter mBookcaseAdapter_Adapter;

    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;

    private ArrayList<Book> mArrayList_Books;
    private ArrayList<BookPlace> mArrayList_BookPlaces;

    private Typeface mTypeface_SeoulNamsanM;

    public BookcaseFragment(){

    }

    public void onAttach(Context context){
        super.onAttach(context);

        m_Context = context;

        mLayoutManager_Bookcase = new GridLayoutManager(context, 2);
        mTypeface_SeoulNamsanM = Typeface.createFromAsset(m_Context.getAssets(), "seoulnamsan_m.otf");
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        mArrayList_BookPlaces = (ArrayList<BookPlace>)(getArguments().getSerializable("BOOKPLACES"));

        View view = layoutInflater.inflate(R.layout.fragment_bookcase, container, false);
        recyclerView_Bookcase = (RecyclerView)view.findViewById(R.id.recyclerview_bookcase_bookcase);
        recyclerView_Bookcase.setHasFixedSize(true);
        recyclerView_Bookcase.setLayoutManager(mLayoutManager_Bookcase);
        //recyclerView_Bookcase.setAdapter(mBookcaseAdapter_Adapter);

        /*
        imageButton_Registration = (ImageButton)view.findViewById(R.id.imagebutton_bookcase_registration);
        imageButton_Registration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LayoutInflater layoutInflater
                        = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
                View view_RegistrationMethod = layoutInflater.inflate(R.layout.dialog_registrationmethod, null);
                builder.setView(view_RegistrationMethod);
                alertDialg_RegistrationMethod = builder.create();
                alertDialg_RegistrationMethod.setCanceledOnTouchOutside(false);
                alertDialg_RegistrationMethod.show();

                LinearLayout linearLayout_Barcode = (LinearLayout)view_RegistrationMethod.findViewById(R.id.linearlayout_registrationmethod_barcode);
                linearLayout_Barcode.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                        //intentIntegrator.setCaptureActivity();
                        //intentIntegrator.setOrientationLocked(true);
                        //intentIntegrator.initiateScan();
                        Intent intent = new Intent(m_Context, RegistrationActivity.class);
                        intent.putExtra("RegistrationMethod", 0);
                        startActivity(intent);
                        alertDialg_RegistrationMethod.dismiss();
                    }
                });

                LinearLayout linearLayout_Search = (LinearLayout)view_RegistrationMethod.findViewById(R.id.linearlayout_registrationmethod_search);
                linearLayout_Search.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(m_Context, RegistrationActivity.class);
                        intent.putExtra("RegistrationMethod", 1);
                        startActivity(intent);
                        alertDialg_RegistrationMethod.dismiss();
                    }
                });

                LinearLayout linearLayout_Hand = (LinearLayout)view_RegistrationMethod.findViewById(R.id.linearlayout_registrationmethod_hand);
                linearLayout_Hand.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(m_Context, RegistrationActivity.class);
                        intent.putExtra("RegistrationMethod", 2);
                        startActivity(intent);
                        alertDialg_RegistrationMethod.dismiss();
                    }
                });
            }
        });
        */

        floatingButton_Registration = (FloatingButton) view.findViewById(R.id.floatingbutton_bookcase_registration);
        View sheetView = view.findViewById(R.id.fab_sheet);
        view_Overlay = view.findViewById(R.id.dimoverlayframelayout_bookcase_overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(floatingButton_Registration, sheetView, view_Overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        TextView textVeiw_Barcode = (TextView)view.findViewById(R.id.textview_bookcase_barcode);
        TextView textVeiw_Search = (TextView)view.findViewById(R.id.textview_bookcase_search);
        textVeiw_Barcode.setTypeface(mTypeface_SeoulNamsanM);
        textVeiw_Search.setTypeface(mTypeface_SeoulNamsanM);

        view.findViewById(R.id.linearlayout_bookcase_barcode).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(m_Context, BookSearchActivity.class);
                intent.putExtra("SearchMethod", 0);
                ((Activity)m_Context).startActivityForResult(intent, BookcaseFragment.CODE_BOOKCASE);
                materialSheetFab.hideSheet();
            }
        });
        view.findViewById(R.id.linearlayout_bookcase_search).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(m_Context, BookSearchActivity.class);
                intent.putExtra("SearchMethod", 1);
                ((Activity)m_Context).startActivityForResult(intent, BookcaseFragment.CODE_BOOKCASE);
                materialSheetFab.hideSheet();
            }
        });

        BookcaseTask bookcaseTask_Task = new BookcaseTask(m_Context);
        bookcaseTask_Task.execute();

        return view;
    }

    @Override
    public void onActivityResult(int int_RequestCode, int int_ResultCode, Intent intent_Data) {
        super.onActivityResult(int_RequestCode, int_ResultCode, intent_Data);
        if(int_RequestCode == BookcaseFragment.CODE_BOOKCASE
                && int_ResultCode == BookSearchActivity.CODE_BOOKSEARCH){
            if(intent_Data.getIntExtra("RECORD", 0) == 1){
                BookcaseTask bookcaseTask_Task = new BookcaseTask(m_Context);
                bookcaseTask_Task.execute();
            }
        }else if(int_RequestCode == BookcaseFragment.CODE_BOOKCASE
                && int_ResultCode == BookHistoryActivity.CODE_BOOKHISTORY){
            if(intent_Data.getIntExtra("RECORD", 0) == 1){
                BookcaseTask bookcaseTask_Task = new BookcaseTask(m_Context);
                bookcaseTask_Task.execute();
            }
        }
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getActivity().getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(color);
        }
    }

    public MaterialSheetFab getMaterialSheetFab(){
        return materialSheetFab;
    }

    private class BookcaseTask extends AsyncTask<String, Integer, Integer> {
        private Context m_Context;
        private BookDBHelper mBookDBHelper_Database;

        public BookcaseTask(Context context){
            m_Context = context;

            mBookDBHelper_Database = BookDBHelper.getInstance(context);
            mArrayList_Books = new ArrayList<Book>();
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... string_Queries) {
            mArrayList_Books = mBookDBHelper_Database.getAllBooks();
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(Integer result){
            Log.d("onPostExecute", "onPostExecute");
            mBookcaseAdapter_Adapter = new BookcaseAdapter(m_Context, mArrayList_Books, mArrayList_BookPlaces);
            recyclerView_Bookcase.setAdapter(mBookcaseAdapter_Adapter);
        }
    }
}
