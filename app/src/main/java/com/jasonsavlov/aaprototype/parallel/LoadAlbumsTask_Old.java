package com.jasonsavlov.aaprototype.parallel;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.activity.MainActivity;
import com.jasonsavlov.aaprototype.obj.AAAlbum;
import com.jasonsavlov.aaprototype.obj.AAArtist;
import com.jasonsavlov.aaprototype.obj.AAMediaManager;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/**
 * Created by jason on 12/25/15.
 */
public class LoadAlbumsTask_Old extends AsyncTask<AAArtist, Integer, ArrayList<AAAlbum>>
{
    // Constants
    private static final String MESSAGE = "Loading Albums...";

    /*
        Instance variables
      */

    Integer rawProgress;
    ProgressDialog progressDialog;
    AAMediaManager mediaManager = ApplicationMain.getInstance().getMainMediaManager();

    @Override
    protected ArrayList<AAAlbum> doInBackground(AAArtist... params)
    {
        AAArtist mArtist = params[0];

        //progressDialog.setMax(mArtist.getAlbumCount());

        ArrayList<AAAlbum> list = mediaManager.getAlbumsByArtist(mArtist.getArtistName());

        return list;
    }

    public LoadAlbumsTask_Old()
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
        super.onPostExecute(aaAlbums);

        // Hide the progress dialog
        progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        rawProgress = 0;

        //progressDialog = new ProgressDialog();
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
