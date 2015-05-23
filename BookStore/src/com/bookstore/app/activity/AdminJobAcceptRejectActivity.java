package com.bookstore.app.activity;

import android.app.ProgressDialog;
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
			return adminManager.getJobInfoAcceptReject(jobID);
		} else {
			IAdminManager adminManager = new AdminManager();
			return adminManager.jobAcceptReject(jobID, CommonTasks
					.getPreferences(getApplicationContext(),
							CommonConstraints.USER_ID), jobDetails.agentgcmid,
					"" + jobDetails.bookid, "" + jobDetails.quantity, ""
							+ jobSubmitStatus, etAdminRemarks.getText()
							.toString().trim());
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (whichPurpose.equals("GET_JOB_INFO")) {
				jobDetails = (JobAcceptRejectDetails) data;

				//ivTeachersSigneture
				tvAuthor.setText(jobDetails.authername);
				tvQuantity.setText(jobDetails.no_of_book);
				tvPrice.setText(jobDetails.quantity);
				tvTeacherName.setText(jobDetails.teachername);
				tvInstitudes.setText(jobDetails.institute);
				tvAgentName.setText(jobDetails.agentname);
				tvAgentsMobileNumber.setText(jobDetails.agentmobilenumber);

			} else {

			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error.Please Try again later");
		}

	}

	@Override
	public void onClick(View view) {
		// btnOk, btnReject, btnAccept
		if (view.getId() == R.id.btnOk) {
			super.onBackPressed();

		} else if (view.getId() == R.id.btnReject) {
			whichPurpose = "JOB_SUBMIT";
			jobSubmitStatus = CommonConstraints.REJECTED_JOB;
			loadInformation();

		} else if (view.getId() == R.id.btnAccept) {
			whichPurpose = "JOB_SUBMIT";
			jobSubmitStatus = CommonConstraints.COMPLETED_JOB;
			loadInformation();
		}

	}

}
