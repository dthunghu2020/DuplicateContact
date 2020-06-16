package com.hungdt.test.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hungdt.test.view.ContactFragment;
import com.hungdt.test.view.DeleteFragment;
import com.hungdt.test.view.ManageFragment;
import com.hungdt.test.view.MergedFragment;
import com.hungdt.test.view.VipFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private int numberOfTabs;

    public ViewPageAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ContactFragment();
            case 1:
                return new ManageFragment();
            case 2:
                return new MergedFragment();
            case 3:
                return new DeleteFragment();
            case 4:
                return new VipFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
