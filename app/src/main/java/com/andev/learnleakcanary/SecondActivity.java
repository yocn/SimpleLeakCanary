package com.andev.learnleakcanary;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SecondActivity extends BaseActivity {

	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leak_layout);

		button = findViewById(R.id.button);
		button.setText("SecondActivity click me");

		startAsyncTask();

		button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				startActivity(new Intent(SecondActivity.this, ThirdActivity.class));

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Drawable d = iv.getDrawable();
		if (d != null) {
			d.setCallback(null);
		}
		iv.setImageDrawable(null);
	}
}
