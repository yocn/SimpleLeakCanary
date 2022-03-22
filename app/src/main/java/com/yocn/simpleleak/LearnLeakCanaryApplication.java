package com.yocn.simpleleak;

import android.app.Application;
import android.util.Log;

import com.yocn.simpleleak.leakcanary.ActivityRefWatcher;
import com.yocn.simpleleak.leakcanary.DrawableRefWatcher;
import com.yocn.simpleleak.leakcanary.RefWatcher;

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
