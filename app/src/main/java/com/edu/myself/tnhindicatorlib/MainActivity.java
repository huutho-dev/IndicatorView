package com.edu.myself.tnhindicatorlib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.edu.myself.indicatorviewpager.IndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private IndicatorView viewIndicator;
    private BookPagerAdaper adaper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adaper = new BookPagerAdaper(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adaper );
        viewIndicator = (IndicatorView) findViewById(R.id.viewIndicator);
        viewIndicator.setUpWithViewPager(viewPager);

    }

    private class BookPagerAdaper extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public BookPagerAdaper(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            fragments.add(new BookFragment());
            fragments.add(new BookFragment());
            fragments.add(new BookFragment());
            fragments.add(new BookFragment());
            fragments.add(new BookFragment());
            fragments.add(new BookFragment());
            fragments.add(new BookFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
