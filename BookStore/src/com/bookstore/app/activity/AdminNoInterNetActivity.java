package com.bookstore.app.activity;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.base.InternetConnectionService;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AdminNoInterNetActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	ImageView ivNoInternet;
	ProgressDialog dialog;
	DownloadableAsyncTask asyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_no_internet);
		initViews();
	}

	private void initViews() {

		ivNoInternet = (ImageView) findViewById(R.id.ivNoInternet);
		ivNoInternet.setOnClickListener(this);
		stopService(new Intent(this, InternetConnectionService.class));
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.ivNoInternet) {
			loadInformation();
		}

	}

	private void loadInformation() {
		if (asyncTask != null)
			asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Checking Internet Connection , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		try{
			Thread.sleep(2000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if (CommonTasks.isOnline(getApplicationContext()))
			return true;
		else
			return false;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		boolean result = (Boolean) data;

		if (result) {
			if (CommonTasks.getPreferences(getApplicationContext(),
					CommonConstraints.USER_TYPE).equals("1")) {
				Intent intent = new Intent(this, AdminHomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			} else {
				Intent intent = new Intent(this, AgentHomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			}
		} else {

			CommonTasks.showToast(getApplicationContext(),
					"Still No Internet Connection");

		}

	}

}
