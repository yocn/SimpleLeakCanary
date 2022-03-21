package com.andev.learnleakcanary.leakcanary;


import android.os.Handler;
import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class RefWatcher {
    private final GcTrigger gcTrigger;
    private final Set<String> retainedKeys;
    private final ReferenceQueue<Object> queue;

    public RefWatcher() {
        retainedKeys = new CopyOnWriteArraySet<>();
        queue = new ReferenceQueue<>();
        this.gcTrigger = GcTrigger.DEFAULT;
    }

    public void watch(Object watchedReference, final String referenceName) {
        final long watchStartNanoTime = System.nanoTime();
        String key = UUID.randomUUID().toString();
        retainedKeys.add(key);
        final KeyedWeakReference reference =
                new KeyedWeakReference(watchedReference, key, referenceName, queue);
//        printSet("add");

        Log.d(ActivityRefWatcher.TAG, "ref:" + reference.toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ensureGone(reference, referenceName, watchStartNanoTime);
            }
        }, 5000);
    }


    void ensureGone(final KeyedWeakReference reference, String referenceName, final long watchStartNanoTime) {
        long gcStartNanoTime = System.nanoTime();
        long watchDurationMs = NANOSECONDS.toMillis(gcStartNanoTime - watchStartNanoTime);

        removeWeaklyReachableReferences();

        if (gone(reference)) {
            return;
        }
        gcTrigger.runGc();
        removeWeaklyReachableReferences();
        if (!gone(reference)) {
            //do HeapDump, HeapAnalyzer
            Log.d(ActivityRefWatcher.TAG, Thread.currentThread().getName());
            Log.d(ActivityRefWatcher.TAG, referenceName + " leaked!");
        }
        return;
    }

    private boolean gone(KeyedWeakReference reference) {
        return !retainedKeys.contains(reference.key);
    }

    private void removeWeaklyReachableReferences() {
        // WeakReferences are enqueued as soon as the object to which they point to becomes weakly
        // reachable. This is before finalization or garbage collection has actually happened.
        KeyedWeakReference ref;
//        printSet("removeWeaklyReachableReferences:");

        while ((ref = (KeyedWeakReference) queue.poll()) != null) {
            retainedKeys.remove(ref.key);
//            printSet("remove:" + ref.key);
        }
    }

    private void printSet(String tag) {
        StringBuilder sb = new StringBuilder(tag);
        for (String s : retainedKeys) {
            sb.append(" ").append(s);
        }
        Log.d(ActivityRefWatcher.TAG, sb.toString());
    }

}
