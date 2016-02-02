package com.jasonsavlov.aaprototype.activity;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAArtist;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;
import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.ui.AlbumFragment;
import com.jasonsavlov.aaprototype.ui.ArtistFragment;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener,
        ArtistFragment.OnFragmentInteractionListener,
        AlbumFragment.OnFragmentInteractionListener {

    public static final String MESSAGE = "Loading Albums...";
    private final String TAG = getClass().getSimpleName();

    private final BroadcastReceiver mountStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("mountStateReceiver", "onReceive: Media mounted. Refreshing AAMediaManager...");
            AAMediaManager.INSTANCE.refreshAudioContent();
            MainActivity.this.recreate();
            // IDEA: detect if a change was made, and force the app to return to the main screen for safety
        }
    };

    private final BroadcastReceiver unmountStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("unmountStateReceiver", "onReceive: unmounted media");
        }
    };

    private static IntentFilter mountStateFilter = new IntentFilter();
    private static IntentFilter unmountStateFilter = new IntentFilter();

    static {
        mountStateFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        unmountStateFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
    }


    @Override
    protected void onResume()
    {
        Log.d(TAG, "onResume: ");
        super.onResume();

        registerReceiver(mountStateReceiver, mountStateFilter);
        registerReceiver(unmountStateReceiver, unmountStateFilter);
    }

    @Override
    protected void onPause()
    {
        Log.d(TAG, "onPause: ");
        super.onPause();

        unregisterReceiver(mountStateReceiver);
        unregisterReceiver(unmountStateReceiver);
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    // The AAMediaManager object used across the whole application
    AAMediaManager mainMediaManager;

    ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(MESSAGE);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            // Making the progress dialog indeterminate is only temporary
            // until the incrementing system is implemented.
            progressDialog.setIndeterminate(true);
        }
        return progressDialog;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the default preferences for the program
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Set up the media manager
        mainMediaManager = ApplicationMain.getInstance().getMainMediaManager();
        //mainMediaManager = new AAMediaManager(getContentResolver());
        //mainMediaManager.refreshAudioContent();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setMediaManager(this.mainMediaManager);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // Show the build ID as a subtitle of the ActionBar
        actionBar.setSubtitle("Build ID: " + getString(R.string.build_identifier));

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getSimpleName(), "onActivityResult()");
        progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Log.d(TAG, "onOptionsItemSelected: settings menu item selected");
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_about:
                Log.d(TAG, "onOptionsItemSelected: about menu item selected");
                return true;
            default:
                Log.w(TAG, "onOptionsItemSelected: a menu item without a properly defined ID in the switch statement was selected. id = " + id);
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    // onFragmentInteraction method for interacting with artists
    @Override
    public void onFragmentInteraction(AAArtist artist) {
        // TODO: Show a list of the artist's albums
        Log.v("artist fragment act.", "Artist clicked: " + artist.getArtistName());

        new LoadAlbumsTask().execute(artist);

    }

    @Override
    public void onFragmentInteraction(AAAlbum album) {
        // TODO: Show the album page
        Log.v("album fragment act.", "Album clicked: " + album.getAlbumName());

        Intent detailIntent = new Intent(this, DetailAlbumActivity.class);
        detailIntent.putExtra(getString(R.string.album_identifier), album.getAlbumName());
        startActivity(detailIntent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private ArtistFragment artistFragment;
        private AlbumFragment albumFragment;
        private AAMediaManager mMediaManager;

        public void setMediaManager(AAMediaManager mm) {
            this.mMediaManager = mm;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:
                    if (artistFragment == null) {
                        if (this.mMediaManager == null) {
                            Log.wtf("SectionsPagerAdapter/getItem(int)",
                                    "You never set a media manager for the SectionsPagerAdapter object.",
                                    new Exception());
                        }
                        Log.v("SectionsPagerAdapter", "artistFragment was null. Creating new instance...");
                        artistFragment = ArtistFragment.newInstance(this.mMediaManager, null, null);
                    }
                    return artistFragment;

                case 1:
                    if (albumFragment == null) {
                        if (this.mMediaManager == null) {
                            Log.wtf("SectionsPagerAdapter/getItem(int)",
                                    "You never set a media manager for the SectionsPagerAdapter object.",
                                    new Exception());
                        }
                        Log.v("SectionsPagerAdapter", "albumFragment was null. Creating new instance...");
                        albumFragment = AlbumFragment.newInstance("all", null);
                    }
                    return albumFragment;

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            // We used to show 3 pages, but then the "Songs" tab just seemed irrelevant and extraneous.
            // So, just use two.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                //case 2:
                //    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    class LoadAlbumsTask extends AsyncTask<AAArtist, Integer, ArrayList<AAAlbum>>
    {
        // Constants

    /*
        Instance variables
      */

        Integer rawProgress;
        AAMediaManager mediaManager = ApplicationMain.getInstance().getMainMediaManager();
        AAArtist workingArtist;

        @Override
        protected ArrayList<AAAlbum> doInBackground(AAArtist... params)
        {

            Log.d(getClass().getSimpleName(), "doInBackground()");
            AAArtist mArtist = params[0];
            workingArtist = mArtist;

            //progressDialog.setMax(mArtist.getAlbumCount());

            ArrayList<AAAlbum> list = mediaManager.getAlbumsByArtist(mArtist.getArtistName());

            return list;
        }

        public LoadAlbumsTask()
        {
            super();
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(ArrayList<AAAlbum> aaAlbums)
        {
            super.onCancelled(aaAlbums);
        }

        @Override
        protected void onPostExecute(ArrayList<AAAlbum> aaAlbums)
        {
            Log.d(getClass().getSimpleName(), "onPostExecute");
            super.onPostExecute(aaAlbums);

            // TODO: Handle an interrupted building of album list

            // Hide the progress dialog
            int gen_token = ChooseAlbumActivity.setTempAlbums(aaAlbums);
            Intent artistAlbumIntent = new Intent(MainActivity.this, ChooseAlbumActivity.class);
            artistAlbumIntent.putExtra(ChooseAlbumActivity.TEMP_TOKEN_KEY, gen_token);
            //artistAlbumIntent.putParcelableArrayListExtra(ChooseAlbumActivity.ALBUM_LIST_KEY, aaAlbums);
            artistAlbumIntent.putExtra(ChooseAlbumActivity.ARTIST_NAME_KEY, workingArtist.getArtistName());

            progressDialog.dismiss();


            MainActivity.this.startActivity(artistAlbumIntent);

        }

        @Override
        protected void onPreExecute()
        {
            Log.d(getClass().getSimpleName(), "onPreExecute()");
            super.onPreExecute();

            rawProgress = 0;

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(MESSAGE);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            // Making the progress dialog indeterminate is only temporary
            // until the incrementing system is implemented.
            progressDialog.setIndeterminate(true);

            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);

            Integer mValue = values[0];

            synchronized (rawProgress) {
                rawProgress += mValue;
                progressDialog.incrementProgressBy(mValue);
            }

        }


    }

}
