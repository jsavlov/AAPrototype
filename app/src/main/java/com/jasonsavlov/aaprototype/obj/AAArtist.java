package com.jasonsavlov.aaprototype.obj;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.parallel.AlbumCallable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by jason on 8/3/15.
 */
public class AAArtist implements Parcelable, Comparable
{
    private String artistName = null;
    private ArrayList<String> artistFilePaths = new ArrayList<>();
    private HashMap<String, AAAlbum> albumHashMap = new HashMap<>();
    private int albumCount = 0;

    private final String TAG = getClass().getSimpleName();

    public static final Parcelable.Creator<AAArtist> CREATOR = new Parcelable.Creator<AAArtist>() {
        @Override
        public AAArtist createFromParcel(Parcel source)
        {
            return new AAArtist(source);
        }

        @Override
        public AAArtist[] newArray(int size)
        {
            return new AAArtist[0];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(artistName);
        dest.writeList(artistFilePaths);

        Bundle albumsBundle = new Bundle(AAAlbum.class.getClassLoader());
        dest.writeInt(albumHashMap.keySet().size());
        for (String key : albumHashMap.keySet())
        {
            dest.writeString(key);
            albumsBundle.putParcelable(key, albumHashMap.get(key));
        }
        dest.writeBundle(albumsBundle);

        dest.writeInt(albumCount);

    }

    public AAArtist(Parcel in) {
        artistName = in.readString();
        in.writeList(artistFilePaths);

        int keySize = in.readInt();
        ArrayList<String> keyArray = new ArrayList<>();
        for (int i = 0; i < keySize; i++) {
            keyArray.add(in.readString());
        }

        Bundle albumsBundle = in.readBundle();
        for (String key : keyArray) {
            albumHashMap.put(key, (AAAlbum) albumsBundle.getParcelable(key));
        }

        albumCount = in.readInt();

    }

    public AAArtist() {
        this.artistName = Resources.getSystem().getResourceName(R.string.no_artist_name);
        Log.w(this.getClass().getName(), "Note: Artist created using empty constructor. Artist has no name.");
    }

    public AAArtist(String name) {
        this.artistName = name;
    }

    public AAAlbum getAlbum(String name) {
        return albumHashMap.get(name);
    }

    public void addAlbum(String name) {
        albumHashMap.put(name, new AAAlbum(name, this));
        Log.v("addAlbum(String)", "Added album " + albumHashMap.get(name).getAlbumName() + " to artist " + this.artistName+ ". Compilation = " + albumHashMap.get(name).isCompilation());
        albumCount++;
    }

    public void addAlbum(AAAlbum album) {
        this.albumHashMap.put(album.getAlbumName(), album);
        Log.v("addAlbum(String)", "Added album " + albumHashMap.get(album.getAlbumName()).getAlbumName() + " to artist " + this.artistName + ". Compilation = " + album.isCompilation());
        albumCount++;
    }

    public ArrayList<AAAlbum> getAllAlbums() {

        ArrayList<AAAlbum> albumsToReturn = new ArrayList<>();
        List<AAAlbum> listOfAlbums = Collections.unmodifiableList(new ArrayList(albumHashMap.values()));


        int numberOfAlbums = albumCount;

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        // TODO: Implement a way to use threads to split up the work of adding albums to the list of albums to return.

        int stepFactor = numberOfAlbums / availableProcessors;

        if (numberOfAlbums < (availableProcessors * availableProcessors)) {
            for (AAAlbum a : albumHashMap.values()) {
                albumsToReturn.add(a);
            }
            return albumsToReturn;
        }

        ExecutorService pool = Executors.newFixedThreadPool(availableProcessors);
        Future<ArrayList<AAAlbum>>[] futures;

        int remainder;

        if ((remainder = numberOfAlbums % availableProcessors) == 0) {
            futures = new Future[availableProcessors];
        } else {
            futures = new Future[availableProcessors + 1];
        }

        int i, start;
        for (i = 0, start = 0; i < availableProcessors; i++) {
            futures[i] = pool.submit(new AlbumCallable(start, stepFactor, listOfAlbums));
            start += stepFactor;
        }

        if (remainder != 0) {
            futures[i] = pool.submit(new AlbumCallable(start, remainder, listOfAlbums));
        }

        try {
            for (int a = 0; a < futures.length; a++) {
                ArrayList<AAAlbum> l = futures[a].get();
                albumsToReturn.addAll(l);
            }
        } catch (Exception ex) {
            // IDEA: Show a dialog saying the process could not be completed and stay on current screen.
            Log.e(TAG, "getAllAlbums: One of the futures was interrupted.", ex);


        }

        pool.shutdown();

        return albumsToReturn;
    }

    @Override
    public String toString() {
        return this.artistName;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public int compareTo(Object another)
    {
        // Compare by artist name
        if (!(another instanceof AAArtist)) {
            // FIXME: Throw an exception
        }

        return this.artistName.compareTo(another.toString());
    }
}
