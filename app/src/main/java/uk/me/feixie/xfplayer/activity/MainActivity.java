package uk.me.feixie.xfplayer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.me.feixie.xfplayer.BuildConfig;
import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.fragment.LiveFragment;
import uk.me.feixie.xfplayer.fragment.LocalFragmentDirectories;
import uk.me.feixie.xfplayer.fragment.LocalFragmentMusic;
import uk.me.feixie.xfplayer.fragment.LocalFragmentVideo;
import uk.me.feixie.xfplayer.fragment.MeFragment;
import uk.me.feixie.xfplayer.fragment.ServerFragment;
import uk.me.feixie.xfplayer.utils.GloableConstants;


public class MainActivity extends AppCompatActivity {

    private RadioButton rbLocal;
    private RadioGroup rgMain;
    private FragmentManager mFragmentManager;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout dlLocal;
    private ListView lvLeftMenu;
    private List<uk.me.feixie.xfplayer.model.MenuItem> menuList;
    private ActionBar mSupportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.Ext.init(getApplication());
        x.Ext.setDebug(BuildConfig.DEBUG);
//        x.view().inject(this);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
        initListeners();
    }

    private void initViews() {

        rbLocal = (RadioButton) findViewById(R.id.rbLocal);
        rgMain = (RadioGroup) findViewById(R.id.rgMain);

        dlLocal = (DrawerLayout)findViewById(R.id.dlLocal);
        lvLeftMenu = (ListView)findViewById(R.id.lvLeftMenu);

        MyListAdapter listAdapter = new MyListAdapter();
        lvLeftMenu.setAdapter(listAdapter);
        lvLeftMenu.setItemChecked(0, true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSupportActionBar = getSupportActionBar();
        if (mSupportActionBar!=null){
            mSupportActionBar.setTitle("Local");
        }

        LocalFragmentVideo localFragment =  new LocalFragmentVideo();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment, GloableConstants.FRAGMENT_LOCAL_VIDEO).commit();

        rbLocal.setChecked(true);
        if (rbLocal.isChecked() && mSupportActionBar!=null) {
            mSupportActionBar.setDisplayHomeAsUpEnabled(true);
            mSupportActionBar.setHomeButtonEnabled(true);
        }

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, dlLocal, toolbar, R.string.drawer_open, R.string.drawer_close);
        // Set the drawer toggle as the DrawerListener
        dlLocal.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    private void initData() {

        menuList = new ArrayList<>();
        uk.me.feixie.xfplayer.model.MenuItem video = new uk.me.feixie.xfplayer.model.MenuItem(R.drawable.ic_movie_black_24dp, "Video");
        menuList.add(video);
        uk.me.feixie.xfplayer.model.MenuItem audio = new uk.me.feixie.xfplayer.model.MenuItem(R.drawable.ic_library_music_black_24dp, "Audio");
        menuList.add(audio);
        uk.me.feixie.xfplayer.model.MenuItem directories = new uk.me.feixie.xfplayer.model.MenuItem(R.drawable.ic_folder_black_24dp, "Directories");
        menuList.add(directories);

    }

    private void initListeners() {

        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLocal:
                        dlLocal.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        mSupportActionBar.setTitle("Local");
                        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
                        mSupportActionBar.setHomeButtonEnabled(true);
                        mActionBarDrawerToggle.syncState();
//                        System.out.println(lvLeftMenu.getCheckedItemPosition());
                        if (lvLeftMenu.getCheckedItemPosition()==0) {
                            LocalFragmentVideo localFragment = (LocalFragmentVideo) mFragmentManager.findFragmentByTag(GloableConstants.FRAGMENT_LOCAL_VIDEO);
                            if (localFragment!=null) {
                                mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment).commit();
                            } else {
                                localFragment = new LocalFragmentVideo();
                                mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment,GloableConstants.FRAGMENT_LOCAL_VIDEO).commit();
                            }
                        } else if (lvLeftMenu.getCheckedItemPosition()==1) {
                            LocalFragmentMusic localFragment = (LocalFragmentMusic) mFragmentManager.findFragmentByTag(GloableConstants.FRAGMENT_LOCAL_AUDIO);
                            if (localFragment!=null) {
                                mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment).commit();
                            } else {
                                localFragment = new LocalFragmentMusic();
                                mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment,GloableConstants.FRAGMENT_LOCAL_AUDIO).commit();
                            }
                        } else if (lvLeftMenu.getCheckedItemPosition()==2) {
                            LocalFragmentDirectories localFragment = (LocalFragmentDirectories) mFragmentManager.findFragmentByTag(GloableConstants.FRAGMENT_LOCAL_DIRECTORIES);
                            if (localFragment!=null) {
                                mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment).commit();
                            } else {
                                localFragment = new LocalFragmentDirectories();
                                mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment,GloableConstants.FRAGMENT_LOCAL_DIRECTORIES).commit();
                            }
                        }

                        break;

                    case R.id.rbServer:
                        dlLocal.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        mSupportActionBar.setTitle("Server");
                        mSupportActionBar.setDisplayHomeAsUpEnabled(false);
                        mSupportActionBar.setHomeButtonEnabled(false);
                        ServerFragment serverFragment = (ServerFragment) mFragmentManager.findFragmentByTag("serverFragment");
                        if (serverFragment!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,serverFragment).commit();
                        } else {
                            serverFragment = new ServerFragment();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,serverFragment,"serverFragment").commit();
                        }
                        break;

                    case R.id.rbLive:
                        dlLocal.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        mSupportActionBar.setTitle("Live");
                        mSupportActionBar.setDisplayHomeAsUpEnabled(false);
                        mSupportActionBar.setHomeButtonEnabled(false);
                        LiveFragment liveFragment = (LiveFragment) mFragmentManager.findFragmentByTag("liveFragment");
                        if (liveFragment!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,liveFragment).commit();
                        } else {
                            liveFragment = new LiveFragment();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,liveFragment,"liveFragment").commit();
                        }
                        break;

                    case R.id.rbMe:
                        dlLocal.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        mSupportActionBar.setTitle("Me");
                        mSupportActionBar.setDisplayHomeAsUpEnabled(false);
                        mSupportActionBar.setHomeButtonEnabled(false);
                        MeFragment meFragment = (MeFragment) mFragmentManager.findFragmentByTag("meFragment");
                        if (meFragment!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,meFragment).commit();
                        } else {
                            meFragment = new MeFragment();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,meFragment,"meFragment").commit();
                        }
                        break;
                }
            }
        });

        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LocalFragmentVideo localFragmentVideo = (LocalFragmentVideo) mFragmentManager.findFragmentByTag(GloableConstants.FRAGMENT_LOCAL_VIDEO);
                LocalFragmentDirectories localFragmentDirectories = (LocalFragmentDirectories) mFragmentManager.findFragmentByTag(GloableConstants.FRAGMENT_LOCAL_DIRECTORIES);
                LocalFragmentMusic localFragmentMusic = (LocalFragmentMusic) mFragmentManager.findFragmentByTag(GloableConstants.FRAGMENT_LOCAL_AUDIO);
                switch (position) {
                    case 0:
                        if (localFragmentVideo!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragmentVideo).commit();
                        } else {
                            localFragmentVideo = new LocalFragmentVideo();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragmentVideo,GloableConstants.FRAGMENT_LOCAL_VIDEO).commit();
                        }
                        break;
                    case 1:
                        if (localFragmentMusic!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragmentMusic).commit();
                        } else {
                            localFragmentMusic = new LocalFragmentMusic();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragmentMusic,GloableConstants.FRAGMENT_LOCAL_AUDIO).commit();
                        }
                        break;
                    case 2:
                        if (localFragmentDirectories!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragmentDirectories).commit();
                        } else {
                            localFragmentDirectories = new LocalFragmentDirectories();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragmentDirectories,GloableConstants.FRAGMENT_LOCAL_DIRECTORIES).commit();
                        }
                        break;
                }
                dlLocal.closeDrawers();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*------------------Left menu list adapter------------------*/
    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuList.size();
        }

        @Override
        public uk.me.feixie.xfplayer.model.MenuItem getItem(int position) {
            return menuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            uk.me.feixie.xfplayer.model.MenuItem menuItem = menuList.get(position);

            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_list_menu, null);
            }

            ImageView ivMenuItem = (ImageView) convertView.findViewById(R.id.ivMenuItem);
            ivMenuItem.setImageResource(menuItem.getImageId());
            TextView tvMenuItem = (TextView) convertView.findViewById(R.id.tvMenuItem);
            tvMenuItem.setText(menuItem.getName());

            return convertView;
        }
    }
}
