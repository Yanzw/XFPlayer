package uk.me.feixie.xfplayer.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.activity.OriginalShowActivity;
import uk.me.feixie.xfplayer.activity.ShowActivity;
import uk.me.feixie.xfplayer.model.WatchedItem;
import uk.me.feixie.xfplayer.utils.GloableConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    private List<WatchedItem> mWatchedList;
    private MyAdapter mAdapter;


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        System.out.println("onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initData();
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        System.out.println("onstart");
        initData();
    }

    private void initData() {
        mWatchedList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        long movieTime = sharedPreferences.getLong(GloableConstants.MOVIE_CURRENT_POSITION, 0);
        long movieDuration = sharedPreferences.getLong(GloableConstants.MOVIE_DURATION, 0);
        String movieTitle = sharedPreferences.getString(GloableConstants.MOVIE_TITLE, "");
        String movieDate = sharedPreferences.getString(GloableConstants.MOVIE_WATCHED_DATE, "");
        int moveType = sharedPreferences.getInt(GloableConstants.WATCHED_MOVIE,0);
        String movePath = sharedPreferences.getString("movie_file_path","");
        if (!TextUtils.isEmpty(movieTitle)) {
            WatchedItem movie = new WatchedItem(movieTime,movieDuration,movieTitle,movieDate,moveType,movePath);
            mWatchedList.add(movie);
        }

        long videoTime = sharedPreferences.getLong(GloableConstants.VIDEO_CURRENT_POSITION, 0);
        long videoDuration = sharedPreferences.getLong(GloableConstants.VIDEO_DURATION, 0);
        String videoTitle = sharedPreferences.getString(GloableConstants.VIDEO_TITLE, "");
        String videoDate = sharedPreferences.getString(GloableConstants.VIDEO_WATCHED_DATE, "");
        int videoType = sharedPreferences.getInt(GloableConstants.WATCHED_VIDEO, 0);
        String videoPath = sharedPreferences.getString("video_file_path","");
        if (!TextUtils.isEmpty(videoTitle)){
            WatchedItem video = new WatchedItem(videoTime,videoDuration,videoTitle,videoDate,videoType,videoPath);
            mWatchedList.add(video);
        }

        String liveTitle = sharedPreferences.getString(GloableConstants.LIVE_TITLE, "");
        String liveDate = sharedPreferences.getString(GloableConstants.LIVE_WATCHED_DATE, "");
        int liveType = sharedPreferences.getInt(GloableConstants.WATCHED_LIVE, 0);
        String livePath = sharedPreferences.getString("live_file_path","");
        if (!TextUtils.isEmpty(videoTitle)){
            WatchedItem live = new WatchedItem(0,0,liveTitle,liveDate,liveType,livePath);
            mWatchedList.add(live);
        }

        if (mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new MyAdapter();
        }
    }

    private void initViews(View view) {
        ListView lvMe = (ListView) view.findViewById(R.id.lvMe);
//        System.out.println(mWatchedList.toString());
        mAdapter = new MyAdapter();
        lvMe.setAdapter(mAdapter);
        lvMe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WatchedItem watchedItem = mWatchedList.get(position);
//                System.out.println(watchedItem.toString());
                Intent intent = new Intent();
                switch (watchedItem.getType()) {
                    case GloableConstants.TYPE_VIDEO:
                        intent.setClass(getActivity(), OriginalShowActivity.class);
                        intent.putExtra("video_path",watchedItem.getPath());
                        intent.putExtra("current_time",watchedItem.getCurrentTime());
                        startActivity(intent);
                        break;
                    case GloableConstants.TYPE_MOVIE:
                        intent.setClass(getActivity(), ShowActivity.class);
                        intent.putExtra("video_path",watchedItem.getPath());
                        intent.putExtra("current_time",watchedItem.getCurrentTime());
                        startActivity(intent);
                        break;
                    case GloableConstants.TYPE_AUDIO:
                        break;
                    case GloableConstants.TYPE_RADIO:
                        break;
                    case GloableConstants.TYPE_LIVE:
                        intent.setClass(getActivity(), ShowActivity.class);
                        intent.putExtra("video_path",watchedItem.getPath());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /*--------------------List Adapter---------------------------*/
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mWatchedList!=null) {
                return mWatchedList.size();
            }
            return 0;
        }

        @Override
        public WatchedItem getItem(int position) {
            return mWatchedList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_list_fragment_me,null);
            }

            WatchedItem watchedItem = mWatchedList.get(position);

            TextView tvWatchedTitle = (TextView) convertView.findViewById(R.id.tvWatchedTitle);
            tvWatchedTitle.setText(watchedItem.getTitle());
            TextView tvWatchedDate = (TextView) convertView.findViewById(R.id.tvWatchedDate);
            tvWatchedDate.setText(watchedItem.getWatchedDate());
            ProgressBar pbMe = (ProgressBar) convertView.findViewById(R.id.pbMe);
            if (watchedItem.getCurrentTime()>0){
                pbMe.setMax((int) watchedItem.getDuration());
                pbMe.setProgress((int) watchedItem.getCurrentTime());
                pbMe.setVisibility(View.VISIBLE);
            } else {
                pbMe.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

}
