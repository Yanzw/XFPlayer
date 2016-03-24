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
public class LocalFragmentMusic extends Fragment {

    private ViewPager vpLocalMusic;
    private List<Fragment> mFragmentList;
//    private TabLayout tlLocalMusic;
    private List<String> titleList;


    public LocalFragmentMusic() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_fragment_music, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        titleList = new ArrayList<>();

        mFragmentList.add(new LocalFragmentMusicBySong());
//        mFragmentList.add(new LocalFragmentMusicBySong());
//        mFragmentList.add(new LocalFragmentMusicBySong());
//        mFragmentList.add(new LocalFragmentMusicBySong());

//        titleList.add("SONGS");
//        titleList.add("ARTISTS");
//        titleList.add("ALBUMS");
//        titleList.add("PLAYLIST");
    }

    private void initViews(View view) {
        vpLocalMusic = (ViewPager) view.findViewById(R.id.vpLocalMusic);
//        tlLocalMusic = (TabLayout) view.findViewById(R.id.tlLocalMusic);

        MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        vpLocalMusic.setAdapter(pagerAdapter);
        vpLocalMusic.setCurrentItem(0);

//        tlLocalMusic.setupWithViewPager(vpLocalMusic);
    }


    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            if (mFragmentList != null) {
                return mFragmentList.size();
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

}
