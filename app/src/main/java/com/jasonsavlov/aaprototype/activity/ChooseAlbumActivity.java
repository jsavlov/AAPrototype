package com.jasonsavlov.aaprototype.activity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;
import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.ui.AlbumFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ChooseAlbumActivity extends AppCompatActivity implements AlbumFragment.OnFragmentInteractionListener
{
    // Constants
    public static final String ARTIST_NAME_KEY = "album_artist";
    public static final String ALBUM_LIST_KEY = "list";
    public static final String TEMP_TOKEN_KEY = "temp_token";

    private static ArrayList<AAAlbum> temp_albums = null;
    private static int temp_token = -1;

    private final String TAG = getClass().getSimpleName();

    AAMediaManager mainManager;
    AlbumFragment albumFragment;
    ArrayList<AAAlbum> listOfAlbums;

    ActionBar aActionBar;

    public static int setTempAlbums(ArrayList<AAAlbum> list) {
        temp_albums = list;
        temp_token = Math.abs(new Random().nextInt());
        return temp_token;
    }

    public static ArrayList<AAAlbum> getTempAlbums(int token) throws IncorrectTokenException {
        if (temp_token == -1)
            throw new IncorrectTokenException("There is no token set. Cannot retrieve temporary list.");

        if (temp_token == -2)
            throw new IncorrectTokenException("The token sent in is the one set for the default value from retrieval from the Intent. Cannot retrieve temporary list.");

        if (token != temp_token)
            throw new IncorrectTokenException("The token entered is not equal to the token set. Cannot retrieve temporary list.");

        if (temp_albums == null)
            throw new IncorrectTokenException("There is no temporary list set. It is currently null.");

        return temp_albums;
    }

    public static boolean resetTempAlbums() {
        if (temp_token == -1 || temp_albums == null)
            return false;

        temp_token = -1;
        temp_albums = null;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Create a string that has the artist's name.
        // This was stored in the Intent over int the MainActivity.
        Log.d(TAG, "onCreate: onCreate called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        MainActivity parent = (MainActivity) getParent();

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.album_toolbar);
        //setSupportActionBar(myToolbar);

        String a_name = getIntent().getStringExtra(ARTIST_NAME_KEY);
        int theToken = getIntent().getIntExtra(TEMP_TOKEN_KEY, -2);

        listOfAlbums = getTempAlbums(theToken);
        resetTempAlbums();

        Collections.sort(listOfAlbums);

        //listOfAlbums = getIntent().getParcelableArrayListExtra(ALBUM_LIST_KEY);

        aActionBar = getSupportActionBar();
        aActionBar.setTitle(a_name);
        aActionBar.setSubtitle("Select an album...");
        aActionBar.show();



        mainManager = ApplicationMain.getInstance().getMainMediaManager();
        Log.v(this.getLocalClassName(), "a_name = " + a_name);

        // Replace the fragment in the static XML layout with our created object.



        albumFragment = (AlbumFragment) getSupportFragmentManager().findFragmentById(R.id.album_fragment_placeholder);
        //albumFragment.setArguments(AlbumFragment.createAlbumFragmentBundle(a_name, listOfAlbums));
        albumFragment.setMediaManager(ApplicationMain.getInstance().getMainMediaManager());
        albumFragment.setmClickedArtistName(a_name);
        albumFragment.setListOfAlbums(listOfAlbums);
        //albumFragment.initialize();

       /* if (a_name != null) {
            albumFragment = AlbumFragment.newInstance(a_name, listOfAlbums);

            try {
                albumFragment.setLoadingDialog(parent.getProgressDialog());
            } catch (NullPointerException ex) {
                Log.i(TAG, "onCreate: setting the loading dialog produced a NullPointerException.", ex);
            }


            // TODO: Replace the FragmentTransaction with replacing the view on onCreateView.


            // TODO: Create an initialize sort of method in AlbumFragment to get the motion started

            android.support.v4.app.FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.album_fragment_placeholder, albumFragment);
            ft.commit();
        }*/


    }

    @Nullable
    @Override
    public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs)
    {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        // This is where creating the view magic goes...

        switch (name) {
            // TODO: Add a condition for the placeholder fragment ID, and handle the view stuff
            default:
                //Log.d(TAG, "onCreateView: name = " + name);
        }

        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getSimpleName(), "onActivityResult()");
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);


    }


    // Delegate method(s).
    @Override
    public void onFragmentInteraction(AAAlbum album)
    {
        Intent detailIntent = new Intent(this, DetailAlbumActivity.class);
        detailIntent.putExtra(getString(R.string.album_identifier), album.getAlbumName());
        startActivity(detailIntent);
    }


}

class IncorrectTokenException extends RuntimeException {
    public IncorrectTokenException(String detailMessage)
    {
        super(detailMessage);
    }
}
