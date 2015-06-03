package com.bookstore.app.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.JobAcceptRejectDetails;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;

public class AdminJobAcceptRejectActivity extends BookStoreActionBarBase
		implements OnClickListener, IAsynchronousTask {

	ImageView ivTeachersSigneture;
	TextView tvNameBook, tvJobStatus, tvAuthor, tvCurrentLocation, tvISBN,
			tvQuantity, tvPublishDate, tvPrice, tvMpoNumber, tvMobileNumber,
			tvTeacherMobileNumber, tvAgentName, tvAgentsAddress,
			tvAgentsCurrentLocation, tvAgentsMobileNumber, tvInstitudes,
			tvTeacherName;
	Button btnOk, btnReject, btnAccept;
	EditText etAdminRemarks;

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	String jobID;
	String whichPurpose = "GET_JOB_INFO";
	int jobSubmitStatus;
	JobAcceptRejectDetails jobDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_accept_reject);
		NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(2);
		initViews();
	}

	public void initViews() {
		btnOk = (Button) findViewById(R.id.btnOk);
		btnReject = (Button) findViewById(R.id.btnReject);
		btnAccept = (Button) findViewById(R.id.btnAccept);
		btnOk.setOnClickListener(this);
		btnAccept.setOnClickListener(this);
		btnReject.setOnClickListener(this);

		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		tvISBN = (TextView) findViewById(R.id.tvISBN);
		tvQuantity = (TextView) findViewById(R.id.tvQuantity);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvInstitudes = (TextView) findViewById(R.id.tvInstitudes);
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAgentsAddress = (TextView) findViewById(R.id.tvAgentsAddress);
		tvAgentsMobileNumber = (TextView) findViewById(R.id.tvAgentsMobileNumber);
		etAdminRemarks = (EditText) findViewById(R.id.etAdminRemarks);
		ivTeachersSigneture=(ImageView) findViewById(R.id.ivTeachersSigneture);

		Bundle bundle = getIntent().getExtras();
		jobID = bundle.getString("JOB_ID");
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInformation();
	}

	public void loadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Loading , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		if (whichPurpose.equals("GET_JOB_INFO")) {
			IAdminManager adminManager = new AdminManager();
			jobDetails = adminManager.getJobInfoAcceptReject(jobID);
			try {
				if (!jobDetails.teachersignature.equals("")) {
					URL url = new URL(CommonUrls.getInstance().IMAGE_BASE_URL
							+ jobDetails.teachersignature);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(input);
					ivTeachersSigneture.setImageBitmap(myBitmap);
				} else {
					ivTeachersSigneture.setImageBitmap(BitmapFactory
							.decodeResource(getResources(),
									R.drawable.ic_person_24));
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return jobDetails;
		} else {
			IAdminManager adminManager = new AdminManager();
			return adminManager.jobAcceptReject(jobID, CommonTasks
					.getPreferences(getApplicationContext(),
							CommonConstraints.USER_ID), jobDetails.agentgcmid,
					"" + jobDetails.bookid, "" + jobDetails.no_of_book, ""
							+ jobSubmitStatus, etAdminRemarks.getText()
							.toString().trim());
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (whichPurpose.equals("GET_JOB_INFO")) {
				jobDetails = (JobAcceptRejectDetails) data;
				setValues(jobDetails);
			} else {
				Boolean b = (Boolean) data;
				if (b) {
					CommonTasks.showToast(getApplicationContext(),
							"Job Accept/Reject Succesfull");
					super.onBackPressed();
				} else {
					CommonTasks.showToast(getApplicationContext(),
							"Job Approved failed.Please Try Again");
					super.onBackPressed();
				}

			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error.Please Try again later");
		}

	}

	private void setValues(JobAcceptRejectDetails jobDetails2) {
		// ivTeachersSigneture
		tvAuthor.setText(jobDetails.authername);
		tvQuantity.setText("" + jobDetails.no_of_book);
		tvPrice.setText("" + jobDetails.bookprice);
		tvTeacherName.setText(jobDetails.teachername);
		tvInstitudes.setText(jobDetails.institute);
		tvAgentName.setText(jobDetails.agentname);
		tvAgentsMobileNumber.setText(jobDetails.agentmobilenumber);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {
			super.onBackPressed();

		} else if (view.getId() == R.id.btnReject) {

			if (etAdminRemarks.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter Why you reject this Job submission");
				return;
			}
			whichPurpose = "JOB_SUBMIT";
			jobSubmitStatus = CommonConstraints.REJECTED_JOB;
			loadInformation();

		} else if (view.getId() == R.id.btnAccept) {
			if (etAdminRemarks.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter a Remarks");
				return;
			}
			whichPurpose = "JOB_SUBMIT";
			jobSubmitStatus = CommonConstraints.COMPLETED_JOB;
			loadInformation();
		}

	}

}
