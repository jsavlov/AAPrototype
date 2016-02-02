package com.jasonsavlov.aaprototype.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;
import com.jasonsavlov.aaprototype.ui.DetailAlbumTopFragment;
import com.jasonsavlov.aaprototype.ui.SongDetailListFragment;

public class DetailAlbumActivity extends AppCompatActivity
        implements DetailAlbumTopFragment.OnFragmentInteractionListener,
        SongDetailListFragment.OnFragmentInteractionListener
{

    private final String TAG = getClass().getSimpleName();
    // Instance variables
    private AAAlbum mAlbum;
    private DetailAlbumTopFragment mTop;
    private SongDetailListFragment mList;
    private AAMediaManager mainManager;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainManager = ApplicationMain.getInstance().getMainMediaManager();

        setContentView(R.layout.activity_detail_album);

        mAlbum = mainManager.getAlbumByName(getIntent().getStringExtra(getString(R.string.album_identifier)));

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mAlbum.getAlbumName());
        mActionBar.setSubtitle(mAlbum.getAlbumArtistName());

        mTop = (DetailAlbumTopFragment) getSupportFragmentManager().findFragmentById(R.id.detail_top_fragment);
        mTop.setAlbum(mAlbum);
        mList = SongDetailListFragment.newInstance(mAlbum.getAlbumName(), null);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detail_list_fragment, mList);
        ft.commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        Log.d(TAG, "onFragmentInteraction: uri = " + uri.toString());
    }

    public void artworkImageButtonClicked(View view)
    {
        // TODO: Implement initial steps in manipulating the artwork image
        Log.d(TAG, "artworkImageButtonClicked: button was clicked!");
        Toast.makeText(this, "This feature is under construction...", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setTitle("Choose an Option...");
        aBuilder.setItems(R.array.artwork_button_options, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.d(TAG, "Item clicked: " + which);
            }
        });
        aBuilder.show();


    }
}
