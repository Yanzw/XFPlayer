package uk.me.feixie.xfplayer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.activity.ShowActivity;
import uk.me.feixie.xfplayer.model.LiveRadioData;
import uk.me.feixie.xfplayer.model.LiveTVData;
import uk.me.feixie.xfplayer.utils.GloableConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragmentRadio extends Fragment {


    private ArrayList<LiveRadioData.LiveRadio> mRadioList;
    private LiveRadioAdapter mAdapter;

    public LiveFragmentRadio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_fragment_radio, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initData() {
        RequestParams entity = new RequestParams(GloableConstants.LIVE_RADIO_JSON_PATH);
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
        LiveRadioData liveRadioData = gson.fromJson(result, LiveRadioData.class);
        mRadioList = liveRadioData.data;
        mAdapter.notifyDataSetChanged();
    }

    private void initViews(View view) {
        RecyclerView rvLiveRadio = (RecyclerView) view.findViewById(R.id.rvLiveRadio);
        rvLiveRadio.setLayoutManager(new LinearLayoutManager(getContext() ));
        rvLiveRadio.setHasFixedSize(true);
        mAdapter = new LiveRadioAdapter();
        rvLiveRadio.setAdapter(mAdapter);
    }



    /*-----------------Recycler View ViewHolder + Adapter-------------------------------*/
    public class LiveRadioViewHolder extends RecyclerView.ViewHolder {

        //        public ImageView ivLiveIcon;
        public TextView tvLiveTitle;
        public LinearLayout llLiveItem;

        public LiveRadioViewHolder(View itemView) {
            super(itemView);

//            ivLiveIcon = (ImageView) itemView.findViewById(R.id.ivLiveIcon);
            tvLiveTitle = (TextView) itemView.findViewById(R.id.tvLiveTitle);
            llLiveItem = (LinearLayout) itemView.findViewById(R.id.llLiveItem);
            llLiveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveRadioData.LiveRadio liveRadio = mRadioList.get(getAdapterPosition());
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
//                    System.out.println(liveTV.tvPath);
                    intent.putExtra("video_path",liveRadio.radioPath);
                    startActivity(intent);
                }
            });
        }
    }

    public class LiveRadioAdapter extends RecyclerView.Adapter<LiveRadioViewHolder> {

        @Override
        public LiveRadioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_rv_live,null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20,15,20,15);
            view.setLayoutParams(params);
            return new LiveRadioViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LiveRadioViewHolder holder, int position) {
            holder.tvLiveTitle.setText(mRadioList.get(position).title);
        }

        @Override
        public int getItemCount() {
            if (mRadioList!=null) {
                return mRadioList.size();
            }
            return 0;
        }
    }

}
