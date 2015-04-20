package com.bookstore.app.activity;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.LoginEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.google.android.gms.plus.model.people.Person.AgeRange;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SplashScreenActivity extends Activity implements OnClickListener,
		AnimationListener, IAsynchronousTask {

	Animation imageAnimation;
	LinearLayout llLoginPanel;
	EditText etUserName, etPassword;
	ImageView ivLogo;
	Button b_Login, b_ForgotPassword;
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;
	String username, password;
	CheckBox box;
	String userType = "AGENT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		initViews();
	}

	private void initViews() {
		ivLogo = (ImageView) findViewById(R.id.ivLogo);
		llLoginPanel = (LinearLayout) findViewById(R.id.llLoginPanel);
		etUserName = (EditText) findViewById(R.id.etUserNames);
		etPassword = (EditText) findViewById(R.id.etPasswords);
		b_Login = (Button) findViewById(R.id.b_Login);
		box = (CheckBox) findViewById(R.id.cbIsLoginAsAdmin);
		b_ForgotPassword = (Button) findViewById(R.id.b_ForgotPassword);
		

		imageAnimation = AnimationUtils.loadAnimation(this, R.anim.splash);

		b_Login.setOnClickListener(this);
		b_ForgotPassword.setOnClickListener(this);

		if (CommonTasks.getPreferences(SplashScreenActivity.this,
				CommonConstraints.USER_USERNAME).equals("")) {
			imageAnimation.setAnimationListener(SplashScreenActivity.this);
			ivLogo.setAnimation(imageAnimation);
			imageAnimation.start();
		} else {

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					SplashScreenActivity.this.finish();

					if (CommonTasks.getPreferences(SplashScreenActivity.this,
							"USER_TYPE").equals("1")) {
						Intent intent = new Intent(SplashScreenActivity.this,
								AdminHomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					} else if (CommonTasks.getPreferences(
							SplashScreenActivity.this, "USER_TYPE").equals("2")) {
						Intent intent = new Intent(SplashScreenActivity.this,
								AgentHomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}

				}
			}, 0000);

		}

	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		llLoginPanel.animate().alpha(1.0f).setDuration(1000);

	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

	}

	private void validation() {
		if (etUserName.getText().toString().equals("")) {
			Toast.makeText(this, "Enter UserName!", Toast.LENGTH_SHORT).show();
			return;
		} else {
			username = etUserName.getText().toString().trim();

		}
		if (etPassword.getText().toString().equals("")) {
			Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
			return;
		} else {
			password = etPassword.getText().toString().trim();

		}
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		LoginRequest();

	}

	private void LoginRequest() {
		if (downloadAsyncTask != null)
			downloadAsyncTask.cancel(true);
		downloadAsyncTask = new DownloadableAsyncTask(this);
		downloadAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Login , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		IAdminManager adminManager = new AdminManager();
		if (box.isChecked()) {
			return adminManager.getAuthentication(username, password,
					CommonTasks.getPhoneId(getApplicationContext()),
					CommonConstraints.ADMIN_TYPE);
		} else {
			return adminManager.getAuthentication(username, password,
					CommonTasks.getPhoneId(getApplicationContext()),
					CommonConstraints.AGENT_TYPE);
		}

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			LoginEntity entity = (LoginEntity) data;
			if (entity.status) {
				CommonTasks.savePreferencesForReasonCode(this,
						CommonConstraints.USER_TYPE, "" + entity.type);
				CommonTasks.savePreferencesForReasonCode(this,
						CommonConstraints.USER_ID, "" + entity.id);

				if (entity.type == 1) {
					// Admin
					this.finish();
					Intent intent = new Intent(SplashScreenActivity.this,
							AdminHomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right);
				} else {
					// Agent
					this.finish();
					Intent intent = new Intent(SplashScreenActivity.this,
							AgentHomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right);
				}

			} else {
				CommonTasks.showToast(getApplicationContext(), entity.message);
			}
		}else{
			CommonTasks.showToast(getApplicationContext(), "Internal Server Error!!!");
		}

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.b_Login) {
			validation();
		}

	}

}
