package com.jasonsavlov.aaprototype;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.jasonsavlov.aaprototype.obj.AAMediaManager;

/**
 * Created by jason on 8/6/15.
 */
public class ApplicationMain extends Application {

    /**
     * instance variables
     */

    private AAMediaManager mainMediaManager = AAMediaManager.INSTANCE;


    // A singleton instance of the main application class
    private static ApplicationMain singleton;

    // A method to retrieve that singleton object
    public static ApplicationMain getInstance() {
        return singleton;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // instantiate singleton
        singleton = this;


        // Refresh the mainMediaManager to see if any updates occurred in the MediaStore.Audio library
        mainMediaManager.refreshAudioContent();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public AAMediaManager getMainMediaManager() {
        return mainMediaManager;
    }
}
