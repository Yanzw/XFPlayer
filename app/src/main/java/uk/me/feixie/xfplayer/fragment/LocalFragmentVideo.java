package uk.me.feixie.xfplayer.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.activity.ShowActivity;
import uk.me.feixie.xfplayer.model.MenuItem;
import uk.me.feixie.xfplayer.model.Video;
import uk.me.feixie.xfplayer.utils.GloableConstants;
import uk.me.feixie.xfplayer.utils.TimeUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragmentVideo extends Fragment {

    private RecyclerView rvLocalVideo;
    private List<Video> localVideoList;
    private MyRVAdapter mRvAdapter;

    public LocalFragmentVideo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        rvLocalVideo = (RecyclerView) view.findViewById(R.id.rvLocalVideo);
        localVideoList = new ArrayList<>();

        String[] projection = new String[]{MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATA, MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.CATEGORY,MediaStore.Video.Media.DESCRIPTION};
        Cursor cursor = new CursorLoader(getContext(),MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,null, null, null).loadInBackground();
//                getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Video video = new Video();
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                video.setId(id);
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                video.setTitle(title);
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                video.setDuration(duration);
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                video.setData(data);
                String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                video.setResolution(resolution);
                String category = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.CATEGORY));
                video.setCategory(category);
                String description = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DESCRIPTION));
                video.setDescription(description);

//                System.out.println(video.toString());
                localVideoList.add(video);
            }
            cursor.close();
        }

        rvLocalVideo.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLocalVideo.setHasFixedSize(true);
        rvLocalVideo.setItemAnimator(new DefaultItemAnimator());
        mRvAdapter = new MyRVAdapter();
        rvLocalVideo.setAdapter(mRvAdapter);

    }


    /*------------------Recycler View for Local Video ------------------*/
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivVideoLocal;
        public TextView tvTitleVideoLocal;
        public TextView tvDurationVideoLocal;
        public TextView tvResolutionVideoLocal;
        public LinearLayout llLocalVideoCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivVideoLocal = (ImageView) itemView.findViewById(R.id.ivVideoLocal);
            tvTitleVideoLocal = (TextView) itemView.findViewById(R.id.tvTitleVideoLocal);
            tvDurationVideoLocal = (TextView) itemView.findViewById(R.id.tvDurationVideoLocal);
            tvResolutionVideoLocal = (TextView) itemView.findViewById(R.id.tvResolutionVideoLocal);

            llLocalVideoCard = (LinearLayout) itemView.findViewById(R.id.llLocalVideoCard);
            llLocalVideoCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    System.out.println(getAdapterPosition());
                    Intent intent = new Intent(getActivity(), ShowActivity.class);
                    intent.putExtra("video_path",localVideoList.get(getAdapterPosition()).getData());
                    startActivity(intent);
                }
            });
        }
    }

    public class MyRVAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.item_rv_local_video,null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,15,15,15);
            view.setLayoutParams(params);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            Video video = localVideoList.get(position);

            // create thumbnail for video
//            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(video.getData(),
//                    MediaStore.Images.Thumbnails.MINI_KIND);
//            holder.ivVideoLocal.setImageBitmap(thumbnail);
//            x.image().bind(holder.ivVideoLocal,video.getData());
            holder.tvTitleVideoLocal.setText(video.getTitle());

            //load video thumbnail to display imageview by Glide
            Glide.with(LocalFragmentVideo.this)
                    .load(video.getData())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(holder.ivVideoLocal);

            holder.tvDurationVideoLocal.setText(TimeUtil.getDurationBreakdown(Long.parseLong(video.getDuration())));
            holder.tvResolutionVideoLocal.setText(video.getResolution());

        }

        @Override
        public int getItemCount() {
            if (localVideoList!=null) {
                return localVideoList.size();
            }
            return 0;
        }
    }
}
