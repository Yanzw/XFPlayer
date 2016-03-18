package uk.me.feixie.xfplayer.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.me.feixie.xfplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;


    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initData() {

        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();

        mFragmentList.add(new LiveFragmentTV());
        mFragmentList.add(new LiveFragmentRadio());

        mTitleList.add("TV");
        mTitleList.add("Radio");
    }

    private void initViews(View view) {
        ViewPager vpLive = (ViewPager) view.findViewById(R.id.vpLive);
        TabLayout tlLive = (TabLayout) view.findViewById(R.id.tlLive);

        vpLive.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        vpLive.setCurrentItem(0);
        tlLive.setupWithViewPager(vpLive);
    }



    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragmentList!=null) {
                return mFragmentList.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (mFragmentList!=null) {
                return mFragmentList.size();
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

}
