package com.andev.learnleakcanary;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

class GcWatcherTrigger {
    static WeakReference<GcWatcher> sGcWatcher
            = new WeakReference<GcWatcher>(new GcWatcher());
    static ArrayList<Runnable> sGcWatchers = new ArrayList<>();
    static Runnable[] sTmpWatchers = new Runnable[1];

    static final class GcWatcher {
        @Override
        protected void finalize() throws Throwable {
            synchronized (sGcWatchers) {
                sTmpWatchers = sGcWatchers.toArray(sTmpWatchers);
            }
            for (int i = 0; i < sTmpWatchers.length; i++) {
                if (sTmpWatchers[i] != null) {
                    sTmpWatchers[i].run();
                }
            }
            sGcWatcher = new WeakReference<GcWatcher>(new GcWatcher());
        }
    }

    public static void addGcWatcher(Runnable watcher) {
        synchronized (sGcWatchers) {
            sGcWatchers.add(watcher);
        }
    }
}
