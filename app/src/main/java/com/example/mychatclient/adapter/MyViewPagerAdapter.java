package com.example.mychatclient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wzj on 2017/9/21.
 */

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {


    private List<Fragment> mFragments ;
    private List<String> titles;

    public MyViewPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,
                              List<String> titles) {
        super(fm);
        this.mFragments = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null? mFragments.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
