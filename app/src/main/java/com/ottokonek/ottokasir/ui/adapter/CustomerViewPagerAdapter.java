package com.ottokonek.ottokasir.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ottokonek.ottokasir.ui.fragment.customer.CustomerHistoryFragment;
import com.ottokonek.ottokasir.ui.fragment.customer.CustomerKasbonFragment;
import com.ottokonek.ottokasir.ui.fragment.customer.CustomerProfileFragment;

public class CustomerViewPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public CustomerViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CustomerProfileFragment();
            case 1:
                return new CustomerHistoryFragment();
            case 2:
                return new CustomerKasbonFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}