package com.bookstore.app.activity;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.LoginEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener, IAsynchronousTask {

	EditText etUserEmail, etUserPassword;
	Button b_Login, b_ForgotPassword;

	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;
	String username, password;
	CheckBox box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initViews();
	}

	private void initViews() {
		etUserEmail = (EditText) findViewById(R.id.etUserEmail);
		etUserPassword = (EditText) findViewById(R.id.etUserPassword);
		b_Login = (Button) findViewById(R.id.b_Login);
		b_ForgotPassword = (Button) findViewById(R.id.b_ForgotPassword);
		box = (CheckBox) findViewById(R.id.cbIsLoginAsAdmin);
		
		b_Login.setOnClickListener(this);
		b_ForgotPassword.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.b_Login) {
			validation();
		}else if(view.getId() == R.id.b_ForgotPassword){
			Intent intent = new Intent(this, ActivityForgotPassword.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}
	
	private void validation() {
		if (etUserEmail.getText().toString().equals("")) {
			Toast.makeText(this, "Enter UserName!", Toast.LENGTH_SHORT).show();
			return;
		} else {
			username = etUserEmail.getText().toString().trim();

		}
		if (etUserPassword.getText().toString().equals("")) {
			Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
			return;
		} else if(!CommonTasks.isEmailValid(etUserEmail.getText().toString().trim())){
			Toast.makeText(this, "Email Address is not Valid", Toast.LENGTH_SHORT).show();
			return;
		}else {
			password = etUserPassword.getText().toString().trim();

		}
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInformation();

	}
	
	private void loadInformation() {
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
		return adminManager.getAuthentication(username, password,
				CommonTasks.getIMEINumber(getApplicationContext()));
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
					Intent intent = new Intent(this,
							AdminHomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					overridePendingTransition(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right);
				} else {
					// Agent
					this.finish();
					Intent intent = new Intent(this,
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
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
