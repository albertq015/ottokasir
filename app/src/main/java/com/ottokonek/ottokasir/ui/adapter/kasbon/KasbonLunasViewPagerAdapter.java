package com.ottokonek.ottokasir.ui.adapter.kasbon;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ottokonek.ottokasir.ui.fragment.kasbon.KasbonAktifFragment;
import com.ottokonek.ottokasir.ui.fragment.kasbon.KasbonCustomerFragment;
import com.ottokonek.ottokasir.ui.fragment.kasbon.KasbonLaporanFragment;
import com.ottokonek.ottokasir.ui.fragment.kasbon.KasbonLunasFragment;

public class KasbonLunasViewPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public KasbonLunasViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new KasbonLunasFragment();
            case 1:
                return new KasbonAktifFragment();
            case 2:
                return new KasbonCustomerFragment();
            case 3:
                return new KasbonLaporanFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}