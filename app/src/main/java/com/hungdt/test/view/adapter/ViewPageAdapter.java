package com.hungdt.test.view.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleFrm = new ArrayList<>();
    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return titleFrm.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleFrm.get(position);
    }

    public void add(Fragment frm, String t){
        fragmentList.add(frm);
        titleFrm.add(t);
    }
}
