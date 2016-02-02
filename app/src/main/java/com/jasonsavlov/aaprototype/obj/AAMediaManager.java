package com.jasonsavlov.aaprototype.obj;

/**
 * Created by jason on 8/3/15.
 */

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Albums;
import android.net.Uri;
import android.util.Log;

import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.ui.format.TrackNumberFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.prefs.PreferenceChangeEvent;

public enum AAMediaManager {

    // The single instance enum
    INSTANCE;

    private static ApplicationMain appMain = ApplicationMain.getInstance();

    private static final String TAG = AAMediaManager.class.getSimpleName();

    private final ConcurrentSkipListMap<String, AAArtist> artistListMap = new ConcurrentSkipListMap<>();
    private final ConcurrentSkipListMap<String, AAAlbum> albumListMap = new ConcurrentSkipListMap<>();
    private static int artistCount = 0;

    private static String selection = Audio.Media.IS_MUSIC + " != 0";
    private static Uri musicURI = Audio.Media.EXTERNAL_CONTENT_URI;
    private static Uri albumURI = Albums.EXTERNAL_CONTENT_URI;
    private static String[] projections = { "*" };

    private static Cursor mediaCursor;
    private static Cursor albumCursor;

    private static Cursor refreshMediaCursor() {
        return ApplicationMain.getInstance().getContentResolver().query(musicURI, projections, selection, null, null);
    }

    private static Cursor refreshAlbumCursor() {
        return ApplicationMain.getInstance().getContentResolver().query(albumURI, projections, null, null, null);
    }

    // TODO: Implement a method of updating the AAMediaManager when the Android MediaStore is changed


    public synchronized void refreshAudioContent() {

        // TODO: Add the "changes" functionality

        // FIXME: Changes should be false by default once implementation is established
        boolean changes = true;

        String storageState = Environment.getExternalStorageState();
        System.out.println("storageState = " + storageState);

        artistListMap.clear();
        albumListMap.clear();


        mediaCursor = refreshMediaCursor();
        if (mediaCursor != null) {
            if (mediaCursor.moveToFirst()) {
                do {
                    // Get the artist name. See if it already exists
                    String artistName = mediaCursor.getString(mediaCursor.getColumnIndex(Audio.Media.ARTIST));
                    String albumName = mediaCursor.getString(mediaCursor.getColumnIndex(Audio.Media.ALBUM));
                    String songName = mediaCursor.getString(mediaCursor.getColumnIndex(Audio.Media.TITLE));
                    String songPathStr = mediaCursor.getString(mediaCursor.getColumnIndex(Audio.Media.DATA));
                    String albumIDStr = mediaCursor.getString(mediaCursor.getColumnIndex(Audio.Media.ALBUM_ID));
                    String songTrackNum = mediaCursor.getString(mediaCursor.getColumnIndex(Audio.Media.TRACK));
                    String songDurationStr = mediaCursor.getString(mediaCursor.getColumnIndex(Audio.Media.DURATION));
                    //String albumArtPath = mediaCursor.getString(mediaCursor.getColumnIndex(Albums.ALBUM_ART));

                    AAArtist currentArtist = artistListMap.get(artistName);
                    if (currentArtist == null) {
                        artistListMap.put(artistName, new AAArtist(artistName));
                        currentArtist = artistListMap.get(artistName);
                        AAMediaManager.this.artistCount++;
                        changes = true;
                    }

                    AAAlbum currentAlbum = currentArtist.getAlbum(albumName);
                    if (currentAlbum == null) {
                        AAAlbum aAlbum = AAMediaManager.this.albumListMap.get(albumName);
                        if (aAlbum == null) {
                            currentArtist.addAlbum(albumName);
                            currentAlbum = currentArtist.getAlbum(albumName);
                            currentAlbum.setAlbumID(albumIDStr);
                            //currentAlbum.setAlbumArtPath(albumArtPath);
                            AAMediaManager.this.albumListMap.put(albumName, currentAlbum);
                            Log.v(TAG, "new album: name = " + albumName + ", id = " + albumIDStr);
                            changes = true;
                        } else {
                            // If this block of code is reached, it means the album has multiple artists
                            // associated with it. Mark it as a compilation, dude.
                            // (This behavior is tentative; I may decide later to change how to handle
                            // these "compilations".
                            // This behavior has been tested; so far, it works as appropriate.
                            // (See the verbose log output as albums/artists/songs are added.)
                            if (!aAlbum.isCompilation()) {
                                aAlbum.makeCompilation();
                            }
                            currentArtist.addAlbum(aAlbum);
                            currentAlbum = aAlbum;
                        }
                    }

                    AASong nSong = new AASong(songName, currentArtist, currentAlbum, songPathStr);
                    nSong.setTrackNumString(songTrackNum);
                    nSong.setSongDuration(songDurationStr);

                    currentAlbum.addSong(nSong);

                } while (mediaCursor.moveToNext());
            }
        }

        albumCursor = refreshAlbumCursor();
        if (albumCursor != null) {
            if (albumCursor.moveToFirst()) {
                do {
                    String cAlbumName = albumCursor.getString(albumCursor.getColumnIndex(Albums.ALBUM));
                    String cArtPath = albumCursor.getString(albumCursor.getColumnIndex(Albums.ALBUM_ART));
                    AAAlbum aCurrentAlbum = albumListMap.get(cAlbumName);

                    try {
                        aCurrentAlbum.setAlbumArtPath(cArtPath);
                        Log.v(TAG, "Set album art path for album \"" + cAlbumName + "\", cArtPath = " + cArtPath);
                    } catch (NullPointerException e) {
                        Log.wtf(TAG, "aCurrentAlbum was null. Make sure albums are properly being created during the inital phase of mediamanager generation. cAlbumName = " + cAlbumName, e);
                    }

                } while (albumCursor.moveToNext());
            }

        }

        // Get the album's last modified time, but do it in a thread so we can continue our business
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (AAAlbum workingAlbum : albumListMap.values())
                {
                    synchronized (workingAlbum) {
                        File file = new File(workingAlbum.getAlbumSongs().get(0).getFilePath());
                        workingAlbum.setLastModifiedTime(file.lastModified());
                    }
                }
            }
        }).start();


        if (changes) {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    for (AAAlbum workingAlbum : albumListMap.values())
                    {
                        synchronized (workingAlbum) {
                            workingAlbum.parseDiscNumbers();
                        }
                    }
                }
            }).start();
        }

    }

    public synchronized ArrayList<String> getArtistNames() {
        ArrayList<String> al = new ArrayList<>();
        Set<String> artistNameSet = this.artistListMap.keySet();

        for (String s : artistNameSet) {
            al.add(s);
        }

        return al;
    }

    public synchronized ArrayList<AAArtist> getFullListOfArtists() {
        ArrayList<AAArtist> mainList = new ArrayList<>();
        for (AAArtist a : artistListMap.values()) {
            mainList.add(a);
        }
        return mainList;
    }

    public synchronized ArrayList<AAAlbum> getFullListOfAlbums() {
        ArrayList<AAAlbum> mainList = new ArrayList<>();
        for (AAAlbum a : albumListMap.values()) {
            mainList.add(a);
        }
        return mainList;
    }

    public synchronized AAAlbum getAlbumByName(String aName) {
        return albumListMap.get(aName);
    }

    public synchronized ArrayList<AAAlbum> getAlbumsByArtist(String aName)
    {

        AAArtist currentArtist = artistListMap.get(aName);
        return currentArtist.getAllAlbums();
    }

}
