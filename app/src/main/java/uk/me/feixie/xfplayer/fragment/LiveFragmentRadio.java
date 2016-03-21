package uk.me.feixie.xfplayer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.activity.ShowActivity;
import uk.me.feixie.xfplayer.model.LiveRadioData;
import uk.me.feixie.xfplayer.service.RadioService;
import uk.me.feixie.xfplayer.utils.GloableConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragmentRadio extends Fragment {


    private ArrayList<LiveRadioData.LiveRadio> mRadioList;
    private LiveRadioAdapter mAdapter;
    private MenuItem mRadioMenu;
    private ProgressBar pbRadio;
    private View mViewPlay;
    private boolean isPlay = false;
    private String mCurrentVideoPath;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case GloableConstants.RADIO_READY:
                    pbRadio.setVisibility(View.GONE);
                    mIvPlay.setImageResource(android.R.drawable.ic_media_pause);
                    break;
                case GloableConstants.RADIO_ERROR:
                    pbRadio.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private ImageView mIvPlay;

    public LiveFragmentRadio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_fragment_radio, container, false);
        setHasOptionsMenu(true);
        initData();
        initViews(view);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_radio).setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mRadioMenu = menu.findItem(R.id.action_radio);
        mViewPlay = View.inflate(getActivity(), R.layout.radio_action_view_play,null);
        mIvPlay = (ImageView) mViewPlay.findViewById(R.id.actionViewPlay);
//        mViewPause = View.inflate(getActivity(), R.layout.radio_action_view_pause,null);
        mRadioMenu.setActionView(mViewPlay);
        mRadioMenu.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
//                    System.out.println("pause");
                    Intent intent = new Intent(getActivity(), RadioService.class);
                    intent.putExtra("command","pause");
                    getActivity().startService(intent);
                    mIvPlay.setImageResource(android.R.drawable.ic_media_play);
                } else {
//                    System.out.println("play");
//                    System.out.println(mCurrentVideoPath);
                    if (!TextUtils.isEmpty(mCurrentVideoPath)) {
                        Intent intent = new Intent(getActivity(), RadioService.class);
                        intent.putExtra("command","play");
                        intent.putExtra("radio_path",mCurrentVideoPath);
                        getActivity().startService(intent);
                        pbRadio.setVisibility(View.VISIBLE);
                        mIvPlay.setImageResource(android.R.drawable.ic_media_pause);
                    }
                }
                isPlay = !isPlay;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRadioMenu!=null) {
            mRadioMenu.setVisible(false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(getActivity(), RadioService.class);
        getActivity().stopService(intent);
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
        pbRadio = (ProgressBar) view.findViewById(R.id.pbRadio);

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
                    Intent intent = new Intent(getActivity(), RadioService.class);
//                    System.out.println(liveTV.tvPath);
                    intent.putExtra("radio_path",liveRadio.radioPath);
                    intent.putExtra("command","play");
                    intent.putExtra("messenger",new Messenger(mHandler));
                    getActivity().startService(intent);

//                    if (mRadioMenu!=null && mViewPause!=null){
//                        mRadioMenu.setActionView(mViewPause);
//                        isPlay = true;
//                        mCurrentVideoPath = liveRadio.radioPath;
//                    }
                    isPlay = true;
                    mCurrentVideoPath = liveRadio.radioPath;
                    pbRadio.setVisibility(View.VISIBLE);
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
