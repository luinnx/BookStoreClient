package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class AddIMEIActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	ImageView ivCaptureImage;
	EditText etIMEI;
	Button btnOk;
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_imei);
		initViews();
	}

	private void initViews() {
		etIMEI = (EditText) findViewById(R.id.etIMEI);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {
			if (etIMEI.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(), "Enter IMEI");
				return;
			}
			if (!CommonTasks.isOnline(this)) {
				CommonTasks.goSettingPage(this);
				return;
			}
			loadInformation();
		}

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
		dialog.setMessage("Adding IMEI , Plaese wait...");
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
		return adminManager.addIMEI(etIMEI.getText().toString()
				.trim());
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			Boolean result = (Boolean) data;
			if (result) {
				CommonTasks.showToast(getApplicationContext(),
						"IMEI Addition Succesfull");
				super.onBackPressed();
			} else {
				CommonTasks.showToast(getApplicationContext(),
						"IMEI Addition Failed. Please Try again");
			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

}
