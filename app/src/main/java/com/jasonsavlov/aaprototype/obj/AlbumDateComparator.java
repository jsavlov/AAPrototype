package com.jasonsavlov.aaprototype.obj;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jasonsavlov.aaprototype.ApplicationMain;
import com.jasonsavlov.aaprototype.R;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by jason on 1/15/16.
 */
public enum AlbumDateComparator implements Comparator<AAAlbum>
{
    INSTANCE;

    @Override
    public int compare(AAAlbum lhs, AAAlbum rhs)
    {
        ApplicationMain appMain = ApplicationMain.getInstance();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appMain.getApplicationContext());

        String sortOrderPreference = sharedPreferences.getString(appMain.getString(R.string.pref_album_sort_order_key), appMain.getString(R.string.default_sort_order));

        if (sortOrderPreference.equals(appMain.getString(R.string.sort_dateadded_asc)))
            return new Date(lhs.getLastModifiedTime()).compareTo(new Date(rhs.getLastModifiedTime()));

        return rhs.compareTo(rhs);
    }

}
