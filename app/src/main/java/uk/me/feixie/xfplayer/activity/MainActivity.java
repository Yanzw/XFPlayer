package uk.me.feixie.xfplayer.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import uk.me.feixie.xfplayer.R;
import uk.me.feixie.xfplayer.fragment.LiveFragment;
import uk.me.feixie.xfplayer.fragment.LocalFragment;
import uk.me.feixie.xfplayer.fragment.MeFragment;
import uk.me.feixie.xfplayer.fragment.ServerFragment;


public class MainActivity extends AppCompatActivity {

    private RadioButton rbLocal;
    private RadioButton rbServer;
    private RadioButton rbLive;
    private RadioButton rbMe;
    private RadioGroup rgMain;
    private FragmentManager mFragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        x.Ext.init(getApplication());
//        x.Ext.setDebug(BuildConfig.DEBUG);
//        x.view().inject(this);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        initListeners();
    }

    private void initViews() {

        rbLocal = (RadioButton) findViewById(R.id.rbLocal);
        rbServer = (RadioButton) findViewById(R.id.rbServer);
        rbLive = (RadioButton) findViewById(R.id.rbLive);
        rbMe = (RadioButton) findViewById(R.id.rbMe);
        rgMain = (RadioGroup) findViewById(R.id.rgMain);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Local");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        LocalFragment localFragment =  new LocalFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment,"localFragment").commit();

        rbLocal.setChecked(true);
    }

    private void initData() {

    }

    private void initListeners() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbLocal:
                        LocalFragment localFragment = (LocalFragment) mFragmentManager.findFragmentByTag("localFragment");
                        if (localFragment!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment).commit();
                        } else {
                            localFragment = new LocalFragment();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,localFragment,"localFragment").commit();
                        }
                        break;
                    case R.id.rbServer:
                        ServerFragment serverFragment = (ServerFragment) mFragmentManager.findFragmentByTag("serverFragment");
                        if (serverFragment!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,serverFragment).commit();
                        } else {
                            serverFragment = new ServerFragment();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,serverFragment,"serverFragment").commit();
                        }
                        break;
                    case R.id.rbLive:
                        LiveFragment liveFragment = (LiveFragment) mFragmentManager.findFragmentByTag("liveFragment");
                        if (liveFragment!=null) {
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,liveFragment).commit();
                        } else {
                            liveFragment = new LiveFragment();
                            mFragmentManager.beginTransaction().replace(R.id.rlMain,liveFragment,"liveFragment").commit();
                        }
                        break;
                    case R.id.rbMe:
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
}
