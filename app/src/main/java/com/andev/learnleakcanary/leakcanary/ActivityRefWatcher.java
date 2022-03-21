package com.andev.learnleakcanary.leakcanary;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

public final class ActivityRefWatcher {
	public static final String TAG = "RefWatcher";

	public static void install(Application application, RefWatcher refWatcher) {
		new ActivityRefWatcher(application, refWatcher).watchActivities();
	}

	private final Application.ActivityLifecycleCallbacks lifecycleCallbacks =
			new Application.ActivityLifecycleCallbacks() {
				@Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				}

				@Override public void onActivityStarted(Activity activity) {
				}

				@Override public void onActivityResumed(Activity activity) {
				}

				@Override public void onActivityPaused(Activity activity) {
				}

				@Override public void onActivityStopped(Activity activity) {
				}

				@Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				}

				@Override public void onActivityDestroyed(Activity activity) {
					Log.d(TAG, activity.getLocalClassName() + " onActivityDestroyed");
					ActivityRefWatcher.this.onActivityDestroyed(activity);
				}
			};

	private final Application application;
	private final RefWatcher refWatcher;

	/**
	 * Constructs an {@link ActivityRefWatcher} that will make sure the activities are not leaking
	 * after they have been destroyed.
	 */
	public ActivityRefWatcher(Application application, RefWatcher refWatcher) {
		this.application = application;
		this.refWatcher = refWatcher;
	}

	void onActivityDestroyed(Activity activity) {
		refWatcher.watch(activity, activity.getLocalClassName());
	}

	public void watchActivities() {
		// Make sure you don't get installed twice.
		stopWatchingActivities();
		application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
	}

	public void stopWatchingActivities() {
		application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
	}
}
