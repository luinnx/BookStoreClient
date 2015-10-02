package com.bookstore.app.activity;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.ActivityEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDetailsForAdmin extends Activity implements
		IAsynchronousTask {
	ActivityEntity activityEntity;
	TextView tvTeacherName, tvInstitudeName, tvFaculty, tvDistrict,
			tvMobileNumber, tvBookName, tvQuantity, tvActivityDate;
	Button btnEdit, btnSubmit;
	ProgressDialog dialog;
	DownloadableAsyncTask downloadAsyncTask;
	int activityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(17);
		initViews();
	}

	private void initViews() {
		Bundle bundle = getIntent().getExtras();
		activityId = bundle.getInt("ACTIVITY_ID");

		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvInstitudeName = (TextView) findViewById(R.id.tvInstitudeName);
		tvFaculty = (TextView) findViewById(R.id.tvFaculty);
		tvDistrict = (TextView) findViewById(R.id.tvDistrict);
		tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
		tvBookName = (TextView) findViewById(R.id.tvBookName);
		tvQuantity = (TextView) findViewById(R.id.tvQuantity);
		tvActivityDate = (TextView) findViewById(R.id.tvActivityDate);

		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		btnEdit.setText("OK");
		btnSubmit.setVisibility(View.GONE);
		btnEdit.setVisibility(View.GONE);

		btnEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();

			}
		});
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
		dialog.setMessage("Adding Agent , Plaese wait...");
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
		return adminManager.getActivityDetails(""+activityId);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			activityEntity = (ActivityEntity) data;
			tvTeacherName.setText(activityEntity.teacherName);
			tvInstitudeName.setText(activityEntity.institute_name);
			tvFaculty.setText(activityEntity.district);
			tvDistrict.setText(activityEntity.district);
			tvMobileNumber.setText(activityEntity.teacher_mobile_number);
			tvBookName.setText(activityEntity.book_name);
			tvQuantity.setText("" + activityEntity.book_quantity);
			tvActivityDate.setText(activityEntity.activity_date);
		} else {
			CommonTasks.showLogs(getApplicationContext(),
					"An Unexpected Error Occured");
		}
	}
}
