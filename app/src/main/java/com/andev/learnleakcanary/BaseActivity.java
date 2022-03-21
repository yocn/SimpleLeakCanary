package com.andev.learnleakcanary;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BaseActivity extends AppCompatActivity {

    public View rootView;
    public ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leak_layout);
        iv = findViewById(R.id.iv);
        rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        startAsyncTask();
    }

    @SuppressLint("StaticFieldLeak")
    void startAsyncTask() {
        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
        // the activity instance will leak.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Do some slow work in background
                SystemClock.sleep(10000);
                return null;
            }
        }.execute();
    }
}
