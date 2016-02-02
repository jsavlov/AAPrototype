package com.jasonsavlov.aaprototype.obj;

import android.os.Parcelable;

/**
 * Created by jason on 12/27/15.
 */
class ParcelableConstants
{
    private ParcelableConstants()
    {
        throw new AssertionError("Cannot instantiate ParcelableConstants.");
    }

    /*
        These are the constants used across the program.

        (JK, these aren't actually used. They're just here because.. well.. lol idk)
     */
    
    // AASong keys
    static final String SONG_ALBUM_KEY = "song_album";
    static final String SONG_ARTIST_KEY = "song_artist_key";
    static final String SONG_NAME_KEY = "song_name";
    static final String FILE_PATH_KEY = "file_path";

    // AAAlbum keys
    static final String ALBUM_NAME_KEY = "album_name";
    static final String ALBUM_ARTIST_KEY = "album_artist";
    static final String ALBUM_ID_KEY = "album_id";
    static final String ALBUM_ART_PATH_KEY = "album_art_path";
    static final String ALBUM_SONGS_KEY = "album_songs";
    static final String ALBUM_ARTISTS_KEY = "album_artists";
    static final String SONG_COUNT_KEY = "song_count";
    static final String IS_COMPILATION_KEY = "is_compilation";

    // AAArtist keys
    static final String ARTIST_NAME_KEY = "artist_name";
    static final String ALBUM_FILE_PATHS_KEY = "album_file_paths_key";
    static final String ALBUM_HASH_MAP_KEY = "album_hash_map";
    static final String ALBUM_COUNT_KEY = "album_count";

}
