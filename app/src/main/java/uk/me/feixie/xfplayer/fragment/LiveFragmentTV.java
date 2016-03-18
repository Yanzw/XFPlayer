package uk.me.feixie.xfplayer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.activity.ShowActivity;
import uk.me.feixie.xfplayer.model.LiveTVData;
import uk.me.feixie.xfplayer.utils.GloableConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragmentTV extends Fragment {

    private ArrayList<LiveTVData.LiveTV> mTvList;
    private LiveTVAdapter mAdapter;

    public LiveFragmentTV() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_fragment_tv, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initData() {

        RequestParams entity = new RequestParams(GloableConstants.LIVE_TV_JSON_PATH);
        entity.setCacheMaxAge(1000*6);

        x.http().get(entity, new Callback.CacheCallback<String>() {

            String result=null;
            boolean hasError = false;

            @Override
            public boolean onCache(String result) {
                this.result = result;
                return false;
            }

            @Override
            public void onSuccess(String result) {
                this.result = result;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (result!=null && !hasError) {
                    parseData(result);
                }
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        LiveTVData liveTVData = gson.fromJson(result, LiveTVData.class);
        mTvList = liveTVData.data;
        mAdapter.notifyDataSetChanged();
    }

    private void initViews(View view) {
        RecyclerView rvLiveTV = (RecyclerView) view.findViewById(R.id.rvLiveTV);
        rvLiveTV.setLayoutManager(new LinearLayoutManager(getContext() ));
        rvLiveTV.setHasFixedSize(true);
        mAdapter = new LiveTVAdapter();
        rvLiveTV.setAdapter(mAdapter);
    }



    /*-----------------Recycler View ViewHolder + Adapter-------------------------------*/
    public class LiveTVViewHolder extends RecyclerView.ViewHolder {

//        public ImageView ivLiveIcon;
        public TextView tvLiveTitle;
        public LinearLayout llLiveItem;

        public LiveTVViewHolder(View itemView) {
            super(itemView);

//            ivLiveIcon = (ImageView) itemView.findViewById(R.id.ivLiveIcon);
            tvLiveTitle = (TextView) itemView.findViewById(R.id.tvLiveTitle);
            llLiveItem = (LinearLayout) itemView.findViewById(R.id.llLiveItem);
            llLiveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveTVData.LiveTV liveTV = mTvList.get(getAdapterPosition());
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
//                    System.out.println(liveTV.tvPath);
                    intent.putExtra("video_path",liveTV.tvPath);
                    startActivity(intent);
                }
            });
        }
    }

    public class LiveTVAdapter extends RecyclerView.Adapter<LiveTVViewHolder> {

        @Override
        public LiveTVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_rv_live,null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20,15,20,15);
            view.setLayoutParams(params);
            return new LiveTVViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LiveTVViewHolder holder, int position) {
            holder.tvLiveTitle.setText(mTvList.get(position).title);
        }

        @Override
        public int getItemCount() {
            if (mTvList!=null) {
                return mTvList.size();
            }
            return 0;
        }
    }

}
