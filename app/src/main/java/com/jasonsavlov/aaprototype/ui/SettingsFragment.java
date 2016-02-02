package com.jasonsavlov.aaprototype.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jasonsavlov.aaprototype.R;

/**
 * Created by jason on 1/15/16.
 */
public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Add the preferences from the preferences XML file
        addPreferencesFromResource(R.xml.preferences);
    }

}
