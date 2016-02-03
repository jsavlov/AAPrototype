package com.jasonsavlov.aaprototype.obj;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.ui.format.TrackNumberFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by jason on 8/3/15.
 */
public class AAAlbum implements Parcelable, Comparable
{
    private static final String TAG = AAAlbum.class.getSimpleName();

    private static Bitmap defaultBitmap = null;

    private String albumName = null;
    private AAArtist albumArtist = null;
    private String albumID = null;
    private String albumArtPath = null;
    private ArrayList<AASong> albumSongs = new ArrayList<>();
    private ArrayList<AAArtist> albumArtists = null;
    private ArrayList<TrackListObject> albumTrackList = null;
    private int songCount = 0;
    private boolean isCompilation = false;
    private transient Bitmap albumArt;
    private long lastModifiedTime;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(albumName);
        dest.writeParcelable(albumArtist, 0);
        dest.writeString(albumID);
        dest.writeString(albumArtPath);
        //dest.writeTypedList(albumSongs);
        dest.writeTypedList(albumArtists);
        dest.writeInt(songCount);
        dest.writeInt(isCompilation ? 1 : 0);
    }

    public AAAlbum(Parcel in) {
        albumName = in.readString();
        albumArtist = in.readParcelable(AAArtist.class.getClassLoader());
        albumID = in.readString();
        albumArtPath = in.readString();
        in.readTypedList(albumSongs, AASong.CREATOR);
        in.readTypedList(albumArtists, AAArtist.CREATOR);
        songCount = in.readInt();
        isCompilation = in.readInt() == 1;

    }

    private static final Parcelable.Creator<AAAlbum> CREATOR = new Parcelable.Creator<AAAlbum>() {
        @Override
        public AAAlbum createFromParcel(Parcel source)
        {
            return new AAAlbum(source);
        }

        @Override
        public AAAlbum[] newArray(int size)
        {
            return new AAAlbum[0];
        }
    };

    public AAAlbum() {
        this.albumName = Resources.getSystem().getString(R.string.no_album_name);
        Log.w("AAAlbum", "Note: AAAlbum instance created using an empty constructor. albumArtist is null. Beware.");
    }

    public AAAlbum(AAArtist albumArtist) {
        this.albumName = Resources.getSystem().getString(R.string.no_album_name);
        this.albumArtist = albumArtist;
    }

    public AAAlbum(String albumName, AAArtist albumArtist) {
        this.albumName = albumName;
        this.albumArtist = albumArtist;
    }

    public AAAlbum(String albumName, AAArtist albumArtist, String albumID) {
        this.albumName = albumName;
        this.albumArtist = albumArtist;
        this.albumID = albumID;
    }



    public void makeCompilation() {
        this.albumArtist = null;
        this.isCompilation = true;
        Log.v("AAAlbum", "Note: album " + this.albumName + " was just converted to a compilation");
    }

    public AASong addSong(String name) {
        AASong nSong = new AASong(name, this, null);
        albumSongs.add(songCount, nSong);
        songCount++;
        Log.w("AAAlbum", "Note: Added a song to album " + this.albumName + " without a file path. Be sure to set it manually.");
        return nSong;
    }

    public AASong addSong(AASong song) {
        albumSongs.add(songCount, song);
        songCount++;
        return albumSongs.get(songCount-1);
    }

    public AASong addSong(String name, String filePath) {
        AASong nSong = new AASong(name, this, filePath);
        albumSongs.add(songCount, nSong);
        songCount++;
        return nSong;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
        this.albumArt = generateAlbumArtworkBitmap(albumArtPath);
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public AAArtist getAlbumArtist() {
        return albumArtist;
    }

    public String getAlbumArtistName() {
        if (isCompilation) {
            return "Various Artists";
        }
        return albumArtist.getArtistName();
    }

    public Bitmap getArtwork() {
        return this.albumArt;
    }

    public ArrayList<TrackListObject> getAlbumTrackList()
    {
        return albumTrackList;
    }

    public void setAlbumTrackList(ArrayList<TrackListObject> albumTrackList)
    {
        this.albumTrackList = albumTrackList;
    }

    public long getLastModifiedTime()
    {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime)
    {
        this.lastModifiedTime = lastModifiedTime;
        //Log.d(TAG, "setLastModifiedTime: last modified time set, " + this.lastModifiedTime);
    }

    public void parseDiscNumbers()
    {
        TrackNumberFormatter.processDiscNumbers(this.albumSongs);
    }

    private Bitmap generateAlbumArtworkBitmap(String albumArtPath)
    {
        Bitmap rawBitmap = null;

        if (albumArtPath == null) {
            // There is no album artwork.
            return generateDefaultArtworkBitmap();
        }

        rawBitmap = BitmapFactory.decodeFile(albumArtPath);


        if (rawBitmap == null) {
            // The bitmap file doesn't exist.
            return generateDefaultArtworkBitmap();
        }

        File tFile = new File(albumArtPath);
        //Log.d(TAG, "generateAlbumArtworkBitmap: tFile length = " + tFile.length());

        // We should have a bitmap at this point. Return it.
        return rawBitmap;
    }

    private Bitmap generateDefaultArtworkBitmap()
    {
        // TODO: Implement a default album artwork bitmap image


        return null;
    }

    @Override
    public String toString() {
        return this.albumName;
    }

    public void setAlbumArtist(AAArtist albumArtist) {
        this.albumArtist = albumArtist;
    }

    public ArrayList<AASong> getAlbumSongs() {
        return albumSongs;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public int getSongCount() {
        return songCount;
    }

    public boolean isCompilation() {
        return isCompilation;
    }

    public void setIsCompilation(boolean isCompilation) {
        this.isCompilation = isCompilation;
    }


    @Override
    public int compareTo(Object another)
    {
        if (!(another instanceof AAAlbum)) {
            // FIXME: Throw an exception
        }

        return this.albumName.compareTo(another.toString());
    }
}
