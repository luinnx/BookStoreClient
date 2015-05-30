package com.bookstore.app.activity;

import java.io.File;
import org.json.simple.JSONObject;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.JobDetails;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonTasks;

public class AgentJobAcceptRejectResultActivity extends AgentActionbarBase
		implements OnClickListener, IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	TextView tvNameBook, tvJobStatus, tvAuthor, tvPublisherName, tvISBN,
			tvQuantity, tvPublishDate, tvPrice, tvTeacherName, tvInstitution,
			tvTeacherMobileNumber, tvAgentName, tvAgentsAddress,
			tvAgentsCurrentLocation, tvAgentsMobileNumber, tvDialogCancel,
			tvDialogOK, tvTakePhoto, tvFromGallery;
	ImageView ivJobImage;
	EditText etTeacherPassword;
	Button btnOk;
	String jobID = "", mode = "", imageFilePath = "";
	AlertDialog alertDialog;
	boolean isJobSubmit = false;
	JobDetails jobDetails = null;
	File file;

	public String filename = "";
	JSONObject outerObject;
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_agent_jod_details);

		NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(3);

		initialization();
	}

	private void initialization() {
		tvNameBook = (TextView) findViewById(R.id.tvNameBook);
		tvJobStatus = (TextView) findViewById(R.id.tvJobStatus);

		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		tvPublisherName = (TextView) findViewById(R.id.tvPublisherName);
		tvISBN = (TextView) findViewById(R.id.tvISBN);
		tvQuantity = (TextView) findViewById(R.id.tvQuantity);
		tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);
		tvPrice = (TextView) findViewById(R.id.tvPrice);

		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvInstitution = (TextView) findViewById(R.id.tvInstitution);
		tvTeacherMobileNumber = (TextView) findViewById(R.id.tvTeacherMobileNumber);

		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAgentsAddress = (TextView) findViewById(R.id.tvAgentsAddress);
		tvAgentsCurrentLocation = (TextView) findViewById(R.id.tvAgentsCurrentLocation);
		tvAgentsMobileNumber = (TextView) findViewById(R.id.tvAgentsMobileNumber);
		ivJobImage = (ImageView) findViewById(R.id.ivJobImage);

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setText("OK");
		btnOk.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("JOB_ID")
				&& bundle.containsKey("MODE")) {
			jobID = bundle.getString("JOB_ID");
			mode = bundle.getString("MODE");
		}

		if (mode.equals("2") || mode.equals("3") || mode.equals("4")) {
			btnOk.setText("OK");
		} else {
			btnOk.setText("Submit");
		}

		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		LoadInformation();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {
			super.onBackPressed();
			/*if (btnOk.getText().toString().equals("Submit")) {
				super.onBackPressed();
			} else {
				onBackPressed();
			}*/
		}
	}

	private void LoadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Please Wait");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	public Object doInBackground() {
		IAgent agent = new AgentManager();

		return agent.getJobDetails(jobID);

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			jobDetails = (JobDetails) data;
			if (jobDetails != null) {
				setValue(jobDetails);
			}

		}
	}

	private void setValue(JobDetails jobDetails) {
		tvNameBook.setText(jobDetails.BookName);
		if (jobDetails.JobStatus == 3)
			tvJobStatus.setText("Job Status : Complete");
		else if (jobDetails.JobStatus == 1)
			tvJobStatus.setText("Job Status : Pending");
		else if (jobDetails.JobStatus == 2)
			tvJobStatus.setText("Job Status : Submitted");
		else if (jobDetails.JobStatus == 4)
			tvJobStatus.setText("Job Status : Rejected");

		tvAuthor.setText(jobDetails.BookAutherName);
		tvPublisherName.setText(jobDetails.BookPublisherName);
		tvISBN.setText(jobDetails.BookISBNNumber);
		tvQuantity.setText("" + jobDetails.No_Of_Book);
		tvPublishDate.setText(jobDetails.BookPublishDate);
		tvPrice.setText("" + jobDetails.BookPrice);

		tvTeacherName.setText(jobDetails.TeacherName);
		tvInstitution.setText(jobDetails.TeacherInstituteName);
		tvTeacherMobileNumber.setText(jobDetails.TeacherMobileNumber);

		tvAgentName.setText(jobDetails.AgentFullName);
		tvAgentsAddress.setText(jobDetails.AgentAddress);
		tvAgentsCurrentLocation.setText(jobDetails.AgentCurrentLocation);
		tvAgentsMobileNumber.setText(jobDetails.AgentMobileNumber);
	}


}
