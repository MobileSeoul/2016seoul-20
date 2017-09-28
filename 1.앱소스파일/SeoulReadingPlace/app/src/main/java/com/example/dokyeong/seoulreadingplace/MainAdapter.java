package com.example.dokyeong.seoulreadingplace;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DoKyeong on 2016. 8. 23..
 */
public class MainAdapter extends FragmentPagerAdapter{
    private List<Fragment> m_arrFragments;
    private List<String> m_arrTitles;

    public MainAdapter(FragmentManager fragmentManager){
        super(fragmentManager);

        m_arrFragments = new ArrayList<Fragment>();
        m_arrTitles = new ArrayList<String>();
    }

    public void addFragment(Fragment fragment, String strTitle){
        m_arrFragments.add(fragment);
        m_arrTitles.add(strTitle);
    }

    @Override
    public Fragment getItem(int iPosition){
        return m_arrFragments.get(iPosition);
    }

    @Override
    public int getCount(){
        return m_arrFragments.size();
    }
}
