package com.example.dokyeong.seoulreadingplace;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout_Menu;
    private TextView textView_Information;
    private ViewPager viewPager_Content;

    private MainAdapter m_MainAdapter;
    private Typeface mTypeface_SeoulNamsanM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTypeface_SeoulNamsanM = Typeface.createFromAsset(getAssets(), "seoulnamsan_m.otf");

        ArrayList<BookPlace> arrayList_BookPlaces = (ArrayList<BookPlace>)getIntent().getSerializableExtra("BOOKPLACES");

        Log.d("Size", String.valueOf(arrayList_BookPlaces.size()));

        Bundle bundle_BookPlaces = new Bundle();
        bundle_BookPlaces.putSerializable("BOOKPLACES", arrayList_BookPlaces);

        BookPlaceListFragment fragment_BookPlaceList = new BookPlaceListFragment();
        fragment_BookPlaceList.setArguments(bundle_BookPlaces);
        BookPlaceMapFragment fragment_BookPlaceMap = new BookPlaceMapFragment();
        fragment_BookPlaceMap.setArguments(bundle_BookPlaces);
        BookcaseFragment fragment_Bookcase = new BookcaseFragment();
        fragment_Bookcase.setArguments(bundle_BookPlaces);

        viewPager_Content = (ViewPager)findViewById(R.id.viewpager_main_content);
        viewPager_Content.setOffscreenPageLimit(3);
        m_MainAdapter = new MainAdapter(getSupportFragmentManager());
        m_MainAdapter.addFragment(fragment_BookPlaceList, "BOOKPLACELIST");
        m_MainAdapter.addFragment(fragment_BookPlaceMap, "BOOKPLACEMAP");
        m_MainAdapter.addFragment(fragment_Bookcase, "BOOKCASE");
        m_MainAdapter.addFragment(new BookIntroductionFragment(), "BOOKINTRODUCTION");
        viewPager_Content.setAdapter(m_MainAdapter);

        tabLayout_Menu = (TabLayout)findViewById(R.id.tablayout_main_menu);
        tabLayout_Menu.setupWithViewPager(viewPager_Content);
        tabLayout_Menu.getTabAt(0).setIcon(R.mipmap.library);
        tabLayout_Menu.getTabAt(1).setIcon(R.mipmap.locaticon);
        tabLayout_Menu.getTabAt(1).getIcon().setAlpha(100);
        tabLayout_Menu.getTabAt(2).setIcon(R.mipmap.shelve);
        tabLayout_Menu.getTabAt(2).getIcon().setAlpha(100);
        tabLayout_Menu.getTabAt(3).setIcon(R.mipmap.recommend1);
        tabLayout_Menu.getTabAt(3).getIcon().setAlpha(100);

        tabLayout_Menu.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                tab.getIcon().setAlpha(255);
                viewPager_Content.setCurrentItem(tab.getPosition(), false);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab){
                tab.getIcon().setAlpha(100);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab){

            }
        });

        textView_Information = (TextView)findViewById(R.id.textview_main_information);
        textView_Information.setTypeface(mTypeface_SeoulNamsanM);
        textView_Information.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view_One){
                Intent intent_Information = new Intent(MainActivity.this, InformationActivity.class);
                startActivity(intent_Information);
            }
        });
    }

    @Override
    protected void onActivityResult(int int_RequestCode, int int_ResultCode, Intent data) {
        Log.d("onActivityResult", "RequestCode = " + String.valueOf(int_RequestCode));

        if(int_RequestCode == BookcaseFragment.CODE_BOOKCASE
                && int_ResultCode == BookSearchActivity.CODE_BOOKSEARCH){
            Fragment fragment = m_MainAdapter.getItem(2);
            if(fragment != null){
                ((BookcaseFragment)fragment).onActivityResult(int_RequestCode, int_ResultCode, data);
            }
        }

        if(int_RequestCode == BookcaseFragment.CODE_BOOKCASE
                && int_ResultCode == BookHistoryActivity.CODE_BOOKHISTORY){
            Fragment fragment = m_MainAdapter.getItem(2);
            if(fragment != null){
                ((BookcaseFragment)fragment).onActivityResult(int_RequestCode, int_ResultCode, data);
            }
        }

        if(int_RequestCode == 49374){
            //Barcode Scan
            Fragment fragment = m_MainAdapter.getItem(2);
            if(fragment != null){
                ((BookcaseFragment)fragment).onActivityResult(int_RequestCode, int_ResultCode, data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = m_MainAdapter.getItem(2);
        if(fragment != null){
            MaterialSheetFab materialSheetFab = ((BookcaseFragment)fragment).getMaterialSheetFab();

            if(materialSheetFab.isSheetVisible()){
                materialSheetFab.hideSheet();
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }
}
