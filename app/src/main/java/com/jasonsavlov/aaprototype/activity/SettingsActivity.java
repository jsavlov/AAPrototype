package com.jasonsavlov.aaprototype.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jasonsavlov.aaprototype.R;
import com.jasonsavlov.aaprototype.ui.SettingsFragment;

/**
 * Created by jason on 1/15/16.
 */
public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        getSupportActionBar().setTitle(getString(R.string.settings_window_title));
    }
}
