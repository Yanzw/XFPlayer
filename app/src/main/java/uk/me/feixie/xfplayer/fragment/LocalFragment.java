package uk.me.feixie.xfplayer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.me.feixie.xfplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragment extends Fragment {

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout dlLocal;

    public LocalFragment() {
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
        dlLocal = (DrawerLayout) view.findViewById(R.id.dlLocal);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),dlLocal, (Toolbar) getActivity().findViewById(R.id.toolbar),R.string.drawer_open,R.string.drawer_close);
        // Set the drawer toggle as the DrawerListener
        dlLocal.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

}
