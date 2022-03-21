package com.andev.learnleakcanary;

import android.app.Application;
import android.util.Log;

import com.andev.learnleakcanary.leakcanary.ActivityRefWatcher;
import com.andev.learnleakcanary.leakcanary.DrawableRefWatcher;
import com.andev.learnleakcanary.leakcanary.RefWatcher;
import com.andev.learnleakcanary.leakcanary.ViewRefWatcher;

public class LearnLeakCanaryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        ActivityRefWatcher.install(this, new RefWatcher());
//        ViewRefWatcher.install(this, new RefWatcher());
        DrawableRefWatcher.install(this, new RefWatcher());
        GcWatcherTrigger.addGcWatcher(new Runnable() {
            @Override
            public void run() {
                Log.d(ActivityRefWatcher.TAG, "gcgcgc");
            }
        });
    }

}
