package uk.me.feixie.xfplayer.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.activity.OriginalShowActivity;
import uk.me.feixie.xfplayer.activity.ShowActivity;
import uk.me.feixie.xfplayer.utils.FileUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragmentDirectories extends Fragment {

    private RecyclerView rvLocalDirectories;
    private List<File> mFileList;
    private MyRVAdapter mAdapter;
    private File mFile;


    public LocalFragmentDirectories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_fragment_directories, container, false);
        initViews(view);
        //allow fragment to control menu
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_back);
        item.setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final RotateAnimation rotate = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.back_rotate);
        rotate.setDuration(500);
        rotate.setInterpolator(new LinearInterpolator());

        final MenuItem backMenu = menu.findItem(R.id.action_back);
        View view = View.inflate(getContext(), R.layout.back_action_view, null);
        backMenu.setActionView(view);
        backMenu.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFile!=null){
                    File parentFile = mFile.getParentFile();
                    if(parentFile.isDirectory()) {
                        File[] files = parentFile.listFiles();
                        if (files!=null) {
                            backMenu.getActionView().startAnimation(rotate);
                            mFileList.clear();
                            for (File file:files) {
                                mFileList.add(file);
                            }
                            sortListAscending(mFileList);
                            mAdapter.notifyDataSetChanged();
                            mFile = parentFile;
                        } else {
                            Toast.makeText(getContext(), "This is the root folder!",Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(getContext(), "This is the root folder!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initViews(View view) {

        //initial views
        rvLocalDirectories = (RecyclerView) view.findViewById(R.id.rvLocalDirectories);
        rvLocalDirectories.setHasFixedSize(true);
        rvLocalDirectories.setLayoutManager(new LinearLayoutManager(getContext()));

        //initial file list
        mFileList = new ArrayList<>();

        //get files and put into list
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDirectory = new File(path);
        File[] mFiles = myDirectory.listFiles();

        for (File file : mFiles) {
//            System.out.println(file.toString());
            mFileList.add(file);
        }
        //sot the files in ascending order
        sortListAscending(mFileList);

        //initial recycler view adapter
        mAdapter = new MyRVAdapter();
        rvLocalDirectories.setAdapter(mAdapter);

    }



    //sort ascending order
    public void sortListAscending(List<File> fileList) {
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File current, File after) {
                String currentTitle = current.getName();
                String afterTitle = after.getName();
                int compare = currentTitle.compareToIgnoreCase(afterTitle);
                if (compare>0) return 1;
                else if (compare<0) return -1;
                else return 0;
            }

        });
    }


    /*-------------------------Recycler View for Directories-------------------------*/
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFolderTitle;
        public TextView tvFolderSubtitle;
        public ImageView ivFolder;
        public LinearLayout llLocalDirectories;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvFolderTitle = (TextView) itemView.findViewById(R.id.tvFolderTitle);
            tvFolderSubtitle = (TextView) itemView.findViewById(R.id.tvFolderSubtitle);
            ivFolder = (ImageView) itemView.findViewById(R.id.ivFolder);

            llLocalDirectories = (LinearLayout) itemView.findViewById(R.id.llLocalDirectories);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,50,10,50);
            llLocalDirectories.setLayoutParams(params);
            llLocalDirectories.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

//                    System.out.println(getAdapterPosition());
                    mFile = mFileList.get(getAdapterPosition());
                    if (mFile.isFile()) {
                        String mimeType = FileUtil.getMimeType(getContext(), Uri.fromFile(mFile));
                        if (mimeType.contains("avi")
                                || mimeType.contains("m4v")|| mimeType.contains("mov")|| mimeType.contains("mkv")
                                || mimeType.contains("rmvb")|| mimeType.contains("flv")|| mimeType.contains("wmv")) {

                            Intent intent = new Intent(getActivity(), ShowActivity.class);
                            intent.putExtra("video_path",mFile.getAbsolutePath());
                            startActivity(intent);
                        }

                        if (mimeType.contains("mp4") || mimeType.contains("3gp")) {
                            Intent intent = new Intent(getActivity(), OriginalShowActivity.class);
                            intent.putExtra("video_path",mFile.getAbsolutePath());
                            startActivity(intent);
                        }
//                        System.out.println(mimeType);
                    }
                    if (mFile.isDirectory()) {
                        File[] files = mFile.listFiles();
                        if (files.length>0) {
                            mFileList.clear();
                            for (File subFile : files) {
                                mFileList.add(subFile);
                            }
                            //sot the files in ascending order
                            sortListAscending(mFileList);
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                }
            });
        }
    }

    public class MyRVAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = View.inflate(getContext(), R.layout.item_rv_local_directories,null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            File file = mFileList.get(position);
            if (file.isDirectory()) {
                holder.ivFolder.setImageResource(R.drawable.ic_folder_black_24dp);
                File[] files = file.listFiles();
                if (files.length==0) {
                    holder.tvFolderSubtitle.setText("Directory is empty");
                } else {
                    holder.tvFolderSubtitle.setText(files.length+" subfolder");
                }

            } else if (file.isFile()) {
                holder.ivFolder.setImageResource(R.drawable.ic_description_black_24dp);
                holder.tvFolderSubtitle.setText("");
            }
            holder.tvFolderTitle.setText(file.getName());
        }

        @Override
        public int getItemCount() {
            if (mFileList!=null){
                return mFileList.size();
            }
            return 0;
        }
    }
}