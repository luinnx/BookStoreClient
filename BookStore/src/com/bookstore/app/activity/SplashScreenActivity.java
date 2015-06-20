package com.bookstore.app.activity;

import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		initViews();
	}

	private void initViews() {
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (CommonTasks.getPreferences(SplashScreenActivity.this,
						CommonConstraints.USER_ID).equals("")) {
					SplashScreenActivity.this.finish();
					Intent intent = new Intent(SplashScreenActivity.this,
							LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right);
				} else {

					if (CommonTasks.getPreferences(SplashScreenActivity.this,
							"USER_TYPE").equals("1")) {
						SplashScreenActivity.this.finish();
						Intent intent = new Intent(SplashScreenActivity.this,
								AdminHomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					} else if (CommonTasks.getPreferences(SplashScreenActivity.this,
							"USER_TYPE").equals("2")) {
						SplashScreenActivity.this.finish();
						Intent intent = new Intent(SplashScreenActivity.this,
								AgentHomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}

				}
			}
		}, 5000);

	}
}
