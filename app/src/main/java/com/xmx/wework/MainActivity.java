package com.xmx.wework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.xmx.wework.Fragments.CloudFragment;
import com.xmx.wework.Fragments.HomeFragment;
import com.xmx.wework.Fragments.CalendarFragment;
import com.xmx.wework.Fragments.SQLFragment;
import com.xmx.wework.Fragments.SyncFragment;
import com.xmx.wework.Tools.ActivityBase.BaseNavigationActivity;
import com.xmx.wework.Tools.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity {
    private long mExitTime = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new CalendarFragment());
        fragments.add(new SyncFragment());
        fragments.add(new SQLFragment());
        fragments.add(new CloudFragment());

        List<String> titles = new ArrayList<>();
        titles.add("首页");
        titles.add("日历");
        titles.add("Sync");
        titles.add("SQL");
        titles.add("Cloud");

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), fragments, titles);

        ViewPager vp = (ViewPager) findViewById(R.id.pager_main);
        vp.setAdapter(adapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
