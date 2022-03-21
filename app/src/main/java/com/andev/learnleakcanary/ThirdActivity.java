package com.andev.learnleakcanary;

import android.os.Bundle;

public class ThirdActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leak_layout);
		startAsyncTask();
	}
}
