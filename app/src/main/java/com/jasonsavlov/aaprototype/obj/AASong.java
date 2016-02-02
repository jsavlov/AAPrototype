package com.jasonsavlov.aaprototype.obj;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;


/**
 * Created by jason on 8/3/15.
 */
public class AASong implements Comparable
{

    private AAAlbum songAlbum = null;
    private AAArtist songArtist = null;
    private String songName = null;
    private String filePath = null;

    private String songDuration;
    private String trackNumString;
    private int discNumber = 0;
    private boolean isStartingTrack = false;


    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(songAlbum, 0);
        dest.writeParcelable(songArtist, 0);
        dest.writeString(songName);
        dest.writeString(filePath);
    }

    public static final Parcelable.Creator<AASong> CREATOR = new Parcelable.Creator<AASong>() {
        @Override
        public AASong createFromParcel(Parcel source)
        {
            return new AASong(source);
        }

        @Override
        public AASong[] newArray(int size)
        {
            return new AASong[size];
        }
    };

    public AASong() {
        // Should we ever use the empty constructor? Probably not, very rarely.
        Log.w("AASong", "Note: AASong instance created using empty constructor. All fields null. Beware.");
    }

    public AASong (Parcel in) {
        this.songAlbum = in.readParcelable(AAAlbum.class.getClassLoader());
        this.songArtist = in.readParcelable(AAAlbum.class.getClassLoader());
        this.songName = in.readString();
        this.filePath = in.readString();
    }

    public AASong(String songName, AAAlbum songAlbum, String filePath) {
        this.songName = songName;
        this.songAlbum = songAlbum;
        this.songArtist = this.songAlbum.getAlbumArtist();
        this.filePath = filePath;
    }

    public AASong(String songName, AAArtist songArtist, AAAlbum songAlbum, String filePath) {
        this.songName = songName;
        this.songArtist = songArtist;
        this.songAlbum = songAlbum;
        this.filePath = filePath;
    }

    public AAAlbum getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(AAAlbum songAlbum) {
        this.songAlbum = songAlbum;
    }

    public AAArtist getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(AAArtist songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSongDuration()
    {
        return songDuration;
    }

    public void setSongDuration(String songDuration)
    {
        // TODO: Make the song duration into something human-readable
        this.songDuration = songDuration;
    }

    public String getTrackNumString()
    {
        return trackNumString;
    }

    public void setTrackNumString(String trackNumString)
    {
        // TODO: Make the track number into something human-readable

        this.trackNumString = trackNumString;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        Log.i("AASong", "Song \"" + this.songName + "\" filePath set manually. Value: " + this.filePath);
    }

    public int describeContents()
    {
        return 0;
    }

    public int getDiscNumber()
    {
        return discNumber;
    }

    public void setDiscNumber(int discNumber)
    {
        this.discNumber = discNumber;
    }

    public boolean isStartingTrack()
    {
        return isStartingTrack;
    }

    public void setIsStartingTrack(boolean isStartingTrack)
    {
        this.isStartingTrack = isStartingTrack;
    }


    @Override
    public String toString()
    {
        return this.songName;
    }

    @Override
    public int compareTo(Object another)
    {
        if (!(another instanceof AASong)) {
            // FIXME: Throw an exception
        }
        return this.songName.compareTo(another.toString());
    }
}
