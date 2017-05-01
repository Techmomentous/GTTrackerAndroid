
package com.lt.gpstracker.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lt.gpstracker.R;
import com.lt.gpstracker.adapter.NavDrawerListAdapter;
import com.lt.gpstracker.fragments.MessagingFragment;
import com.lt.gpstracker.fragments.MyLocationFragment;
import com.lt.gpstracker.fragments.MyTrackerFragment;
import com.lt.gpstracker.fragments.SettingsFragment;
import com.lt.gpstracker.pojo.NavDrawerItem;
import com.lt.gpstracker.utility.Preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TrackerMainActivity extends AppCompatActivity {
    private static final String TAG = "GTMainActivity";
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    @SuppressWarnings("deprecation")
    private ActionBarDrawerToggle drawerToggle;

    // nav drawer title
    private CharSequence drawerTitle;

    // used to store app title
    private CharSequence menuTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    static TrackerMainActivity activity;
    public NotificationManager notificationManager;
    public static final int NOTIFICATION_EX = 1;
    private File backupPath = null;
    FileOutputStream fos;

    public static TrackerMainActivity getInstance() {
        return activity;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {

            backupPath = Environment.getExternalStorageDirectory();
            backupPath = new File(backupPath.getPath() + "/Android/data/com.lt.gpstracker/files");

            if (!backupPath.exists()) {
                backupPath.mkdirs();
            }


            activity = this;
            menuTitle = drawerTitle = getTitle();
            Preference preference = new Preference();
            //Check if the app is boot first time on devices
            boolean firstTimeLoadindApp = preference.getFirstTimeLoad(TrackerMainActivity.this);

            if (firstTimeLoadindApp) {
                //if app is first time installed then save the Unique ID in shared preferences
                preference.storeFirstTimeLoad(false, TrackerMainActivity.this);
                preference.storeSessionID(UUID.randomUUID().toString(), TrackerMainActivity.this);
            }

            //Show notification on taskbar
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // load slide menu items
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

            // nav drawer icons from resources
            navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerList = (ListView) findViewById(R.id.list_slidermenu);

            setupNavDrawerItems(navMenuTitles, navMenuIcons, drawerList);

            // enabling action bar app icon and behaving it as toggle button
            if (getActionBar() == null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

            } else {
                getActionBar().setDisplayHomeAsUpEnabled(true);
                getActionBar().setHomeButtonEnabled(true);
            }

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ) {
                public void onDrawerClosed(View view) {
                    if(getActionBar()==null){
                        getSupportActionBar().setTitle(drawerTitle);
                    }else {
                        getActionBar().setTitle(drawerTitle);
                    }
                    //
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    if(getActionBar()==null){
                        getSupportActionBar().setTitle(drawerTitle);
                    }else {
                        getActionBar().setTitle(drawerTitle);
                    }
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };
            drawerLayout.setDrawerListener(drawerToggle);

            if (savedInstanceState == null) {
                if (firstTimeLoadindApp) {
                    displayView(1);
                } else {
                    // on first time display view for first nav item
                    displayView(0);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Error in onCreate :" + e);

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate :" + e);
        }
    }



    /**
     * Display A Notification on Taskbar
     *
     * @param title
     * @param message
     */
    @SuppressWarnings("deprecation")
    public void showNotification(String title, String message) {
        try {
            int icon = R.mipmap.ic_launcher;
            CharSequence tickerText = getString(R.string.app_name);
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon, tickerText, when);
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            Context context = getApplicationContext();
            Intent notificationIntent = new Intent(this, TrackerMainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            //notification.setLatestEventInfo(context, title, message, contentIntent);

            notificationManager.notify(NOTIFICATION_EX, notification);
        } catch (NullPointerException e) {
            Log.e(TAG, "Error in showNotification :" + e);

        } catch (Exception e) {
            Log.e(TAG, "Error in showNotification :" + e);
        }
    }

    /**
     * Remove the notification
     */
    public void removeNotification() {
        try {
            notificationManager.cancel(NOTIFICATION_EX);
        } catch (NullPointerException e) {
            Log.e(TAG, "Error in showNotification :" + e);

        } catch (Exception e) {
            Log.e(TAG, "Error in showNotification :" + e);
        }
    }

    /**
     * Initialize the navigation drawer items
     *
     * @param navMenuTitles
     * @param navMenuIcons
     */
    private void setupNavDrawerItems(String[] navMenuTitles, TypedArray navMenuIcons, ListView drawerList) {
        try {

            navDrawerItems = new ArrayList<NavDrawerItem>();

            // adding nav drawer items to array
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
            // Recycle the typed array
            navMenuIcons.recycle();

            drawerList.setOnItemClickListener(new SlideMenuClickListener());

            // setting the nav drawer list adapter
            adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
            drawerList.setAdapter(adapter);

        } catch (NullPointerException e) {
            Log.e(TAG, "Error while Setting up nav drawer :" + e);

        } catch (Exception e) {
            Log.e(TAG, "Error while Setting up nav drawer :" + e);
        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        try {
            // update the main content by replacing fragments
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new MyTrackerFragment(TrackerMainActivity.this);
                    break;
                case 1:
                    fragment = new SettingsFragment(TrackerMainActivity.this);
                    break;
                case 2:
                    fragment = new MessagingFragment();
                    break;
                case 3:
                    fragment = new MyLocationFragment();
                    break;
                default:
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

                // update selected item and title, then close the drawer
                drawerList.setItemChecked(position, true);
                drawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
                drawerLayout.closeDrawer(drawerList);
                drawerTitle=navMenuTitles[position];
            } else {
                // error in creating fragment
                Log.e(TAG, "Error in creating fragment");
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Error in displayView :" + e);

        } catch (Exception e) {
            Log.e(TAG, "Error in displayView :" + e);
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item

            displayView(position);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //		getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;

    }

    @Override
    public void finish() {

        removeNotification();
        super.finish();
    }

    @Override
    public void onBackPressed() {

        removeNotification();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        removeNotification();
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        removeNotification();
        super.onPause();
    }

    @Override
    public void setTitle(CharSequence title) {
        menuTitle = title;
        if(getActionBar()==null){
         getSupportActionBar().setTitle(menuTitle);
        }else {
            getActionBar().setTitle(menuTitle);
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (drawerToggle != null)
            drawerToggle.syncState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
