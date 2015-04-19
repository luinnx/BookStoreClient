package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;

public class IndividualJobDetailsActivity extends BookStoreActionBarBase
		implements OnClickListener, IAsynchronousTask {

	ImageView ivJobBookImage;
	TextView tvNameBook, tvJobStatus, tvAuthor, tvCurrentLocation, tvISBN,
			tvQuantity, tvPublishDate, tvPrice, tvMpoNumber, tvMobileNumber,
			tvTeacherMobileNumber, tvAgentName, tvAgentsAddress,
			tvAgentsCurrentLocation, tvAgentsMobileNumber, tvInstitude,
			tvTeacherName;
	Button btnOk;

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	String jobID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_job_details);
		initViews();

	}

	private void initViews() {
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);

		tvNameBook = (TextView) findViewById(R.id.tvNameBook);

		tvJobStatus = (TextView) findViewById(R.id.tvJobStatus);
		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);
		tvISBN = (TextView) findViewById(R.id.tvISBN);
		tvQuantity = (TextView) findViewById(R.id.tvQuantity);
		tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvInstitude = (TextView) findViewById(R.id.tvInstitude);
		tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
		tvTeacherMobileNumber = (TextView) findViewById(R.id.tvTeacherMobileNumber);
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAgentsAddress = (TextView) findViewById(R.id.tvAgentsAddress);
		tvAgentsCurrentLocation = (TextView) findViewById(R.id.tvAgentsCurrentLocation);
		tvAgentsMobileNumber = (TextView) findViewById(R.id.tvAgentsMobileNumber);

		Bundle bundle = getIntent().getExtras();
		jobID = bundle.getString("JOB_ID");

		loadInformation();

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {
			super.onBackPressed();
		}

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
		IAdminManager adminManager = new AdminManager();
		return adminManager.getJobDetails(jobID);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			JobEntity jobEntity = new JobEntity();
			jobEntity = (JobEntity) data;

			tvNameBook.setText(jobEntity.bookname);
			
			if(jobEntity.jobstatus){
				tvJobStatus.setText("Status : Completed" );
			}else{
				tvJobStatus.setText("Status : Pending" );
			}
			
			tvAuthor.setText(jobEntity.authername);
			tvAgentsCurrentLocation.setText(jobEntity.agentcurrentlocation);
			tvISBN.setText(jobEntity.isbn);
			tvQuantity.setText("" + jobEntity.quantity);
			tvPublishDate.setText(jobEntity.publishdate);
			tvPrice.setText("" + jobEntity.bookprice);
			
			tvTeacherName.setText(jobEntity.teachername);
			tvInstitude.setText(jobEntity.institute);
			tvTeacherMobileNumber.setText(jobEntity.teachermobilenumber);
			tvAgentName.setText(jobEntity.agentname);
			tvAgentsAddress.setText(jobEntity.agentaddress);
			tvAgentsMobileNumber.setText(jobEntity.agentmobilenumber);

		}

	}

}
