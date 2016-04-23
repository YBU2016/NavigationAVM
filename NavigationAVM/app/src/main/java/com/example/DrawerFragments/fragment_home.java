package com.example.DrawerFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.example.Adapters.MyFragmentPagerAdapter;
import com.example.Fragments.Fragment1;
import com.example.Fragments.Fragment2;
import com.example.Fragments.Fragment3;
import com.example.omur.navigationavm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omur on 20.04.2016.
 */
public class fragment_home extends Fragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    ViewPager viewPager;
    TabHost tabHost;
    View v ;
    MyFragmentPagerAdapter myFragmentPagerAdapter ;
    HorizontalScrollView hScrollView ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         v = inflater.inflate(R.layout.activity_main_page,container,false) ;

        this.initViewPager();
        this.initTabHost();

        return v;
    }
    private void initTabHost() {

        tabHost = (TabHost) v.findViewById(R.id.tabhost);
        tabHost.setup();


        String[] tabNames = {"Tab1", "Tab2", "Tab3"};

        for (int i = 0; i < tabNames.length; i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]).setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getActivity()));
            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(this);
    }


    public class FakeContent implements TabHost.TabContentFactory {
        Context context;

        public FakeContent(Context mcontext) {
            context = mcontext;
        }

        @Override
        public View createTabContent(String tag) {

            View fakeview = new View(context);
            fakeview.setMinimumWidth(0);
            fakeview.setMinimumHeight(0);
            return fakeview;
        }
    }


    private void initViewPager() {

        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new Fragment1());
        fragmentList.add(new Fragment2());
        fragmentList.add(new Fragment3());

       myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);

        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectedItem) {

        tabHost.setCurrentTab(selectedItem);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);

        hScrollView = (HorizontalScrollView)  v.findViewById(R.id.h_scroll_view);
        View tabView= tabHost.getCurrentTabView();
        int scrollPos= tabView.getLeft()-(hScrollView.getWidth()-tabView.getWidth()) / 2 ;
        hScrollView.smoothScrollTo(scrollPos, 0);
    }
}
