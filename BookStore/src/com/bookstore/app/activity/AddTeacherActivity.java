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

public class AddTeacherActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	ImageView ivCaptureImage;
	EditText etTeacherFullName, etTeacherMobileNumber, etTeacherUserName,
			etInstitutionName, etTeacherPassword;
	Button btnOk;
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;
	
	String teacherMobileNumber="",teacherUserName="",teacherPassword="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_teacher);
		initViews();
	}

	private void initViews() {
		etTeacherFullName = (EditText) findViewById(R.id.etTeacherFullName);
		etTeacherMobileNumber = (EditText) findViewById(R.id.etTeacherMobileNumber);
		etTeacherUserName = (EditText) findViewById(R.id.etTeacherUserName);
		etInstitutionName = (EditText) findViewById(R.id.etInstitutionName);
		etTeacherPassword = (EditText) findViewById(R.id.etTeacherPassword);
		btnOk = (Button) findViewById(R.id.btnOk);
		ivCaptureImage=(ImageView) findViewById(R.id.ivCaptureImage);
		btnOk.setOnClickListener(this);
		ivCaptureImage.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {
			if (etTeacherFullName.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Techer full name");
				return;
			} else if (etTeacherMobileNumber.getText().toString().trim()
					.equals("")) {
				teacherMobileNumber="";
			} else if (etTeacherUserName.getText().toString().trim().equals("")) {
				teacherUserName="";
			} else if (etTeacherPassword.getText().toString().trim().equals("")) {
				teacherPassword="";
			} else if (etInstitutionName.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Institude name");
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
		dialog.setMessage("Adding Teacher , Plaese wait...");
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
		return adminManager.addTeacher(etTeacherFullName.getText().toString()
				.trim(), 
				etTeacherUserName.getText().toString().trim()==""?" ":etTeacherUserName.getText().toString().trim(),
				etTeacherPassword.getText().toString().trim()==""?" ":etTeacherPassword.getText().toString().trim(),
				etTeacherMobileNumber.getText().toString().trim()==""?" ":etTeacherMobileNumber.getText().toString().trim(),
				etInstitutionName.getText().toString().trim());
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			Boolean result = (Boolean) data;
			if (result) {
				CommonTasks.showToast(getApplicationContext(),
						"Teacher addition succesfull");
				super.onBackPressed();
			} else {
				CommonTasks.showToast(getApplicationContext(),
						"Teacher Addition failed. Please Try again");
			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

}
