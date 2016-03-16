package uk.me.feixie.xfplayer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.me.feixie.xfplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment {

    private RecyclerView rvServerContent;

    public ServerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_server, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rvServerContent = (RecyclerView) view.findViewById(R.id.rvServerContent);
        rvServerContent.setHasFixedSize(true);
        rvServerContent.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

}
