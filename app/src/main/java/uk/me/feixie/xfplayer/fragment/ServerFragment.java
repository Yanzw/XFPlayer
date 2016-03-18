package uk.me.feixie.xfplayer.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.activity.OriginalShowActivity;
import uk.me.feixie.xfplayer.activity.ShowActivity;
import uk.me.feixie.xfplayer.model.ServerData;
import uk.me.feixie.xfplayer.utils.GloableConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment {

    private RecyclerView rvServerContent;
    private ProgressBar progressBar;
    private ArrayList<ServerData.ServerMovie> mServerMoviesList;
    private ImageOptions mImageOptions;
    private ServerAdapter mAdapter;

    public ServerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        //allow fragment to control menu
        setHasOptionsMenu(true);
        initViews(view);
        initData();
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_server);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_server:
                openVideoDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(View view) {

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        rvServerContent = (RecyclerView) view.findViewById(R.id.rvServerContent);
        rvServerContent.setHasFixedSize(true);
        rvServerContent.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //initialize image options for recyclervew image display
        mImageOptions = new ImageOptions.Builder()
//                        .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
//                        .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();

        mAdapter = new ServerAdapter();
        rvServerContent.setAdapter(mAdapter);
    }

    private void initData() {
        RequestParams entity = new RequestParams(GloableConstants.SERVER_JSON_PATH);
        entity.setCacheMaxAge(1000*6);
        x.http().get(entity, new Callback.CacheCallback<String>() {

            String result = null;
            Boolean hasError = false;

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
                Toast.makeText(getContext(),"Can not connect to server!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
//                System.out.println(result);
                if (result!=null && !hasError) {
                    progressBar.setVisibility(View.GONE);
                    //parse server json data by GSON to list
                    parseServerData(result);

                }
            }
        });
    }

    private void openVideoDialog() {
        final Dialog dialog = new Dialog(getContext());
        View view = View.inflate(getContext(), R.layout.view_server_dialog,null);
        final AppCompatAutoCompleteTextView acServerAddress = (AppCompatAutoCompleteTextView) view.findViewById(R.id.acServerAddress);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.select_dialog_item,GloableConstants.AUTO_COMPLETE_SERVER);
        acServerAddress.setThreshold(1);
        acServerAddress.setAdapter(adapter);
        final TextInputLayout ttlServer = (TextInputLayout) view.findViewById(R.id.ttlServer);
        acServerAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(acServerAddress.getText().toString())) {
                    ttlServer.setError("Network MRL cannot be empty!");
                } else {
                    ttlServer.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Button btnServerDialogPlay = (Button) view.findViewById(R.id.btnServerDialogPlay);
        Button btnServerDialogCancel = (Button) view.findViewById(R.id.btnServerDialogCancel);
        btnServerDialogPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(acServerAddress.getText().toString())) {
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    intent.putExtra("video_path",acServerAddress.getText().toString());
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    ttlServer.setError("Network MRL cannot be empty!");
                }
            }
        });
        btnServerDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void parseServerData(String result) {
        Gson gson = new Gson();
        ServerData serverData = gson.fromJson(result, ServerData.class);
        mServerMoviesList = serverData.data;
        mAdapter.notifyDataSetChanged();
    }

    /*----------------Recycler ViewHolder and Adapter-----------------------*/
    public class ServerViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivServerMovieIcon;
        public TextView tvServerMovieTitle;
        public LinearLayout llServerMovieContent;

        public ServerViewHolder(View itemView) {
            super(itemView);

            ivServerMovieIcon = (ImageView) itemView.findViewById(R.id.ivServerMovieIcon);
            tvServerMovieTitle = (TextView) itemView.findViewById(R.id.tvServerMovieTitle);

            llServerMovieContent = (LinearLayout) itemView.findViewById(R.id.llServerMovieContent);
            llServerMovieContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServerData.ServerMovie serverMovie = mServerMoviesList.get(getAdapterPosition());
                    String video_path = GloableConstants.SERVER_PATH+serverMovie.videoPath;
                    if (video_path.contains(".mp4")||video_path.contains(".3gp")) {
                        Intent intent = new Intent(getActivity(), OriginalShowActivity.class);
                        intent.putExtra("video_path",video_path);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), ShowActivity.class);
                        intent.putExtra("video_path",video_path);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    public class ServerAdapter extends RecyclerView.Adapter<ServerViewHolder> {

        @Override
        public ServerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_rv_server_video,null);
            return new ServerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ServerViewHolder holder, int position) {
            ServerData.ServerMovie serverMovie = mServerMoviesList.get(position);
            x.image().bind(holder.ivServerMovieIcon,GloableConstants.SERVER_PATH+serverMovie.imagePath,mImageOptions);
            holder.tvServerMovieTitle.setText(serverMovie.title);
        }

        @Override
        public int getItemCount() {
            if (mServerMoviesList!=null) {
                return mServerMoviesList.size();
            }
            return 0;
        }
    }

}
