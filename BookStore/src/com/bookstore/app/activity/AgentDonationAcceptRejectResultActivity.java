package com.bookstore.app.activity;

import java.util.Date;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentDonationResultEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonValues;

public class AgentDonationAcceptRejectResultActivity extends AgentActionbarBase
		implements IAsynchronousTask, OnClickListener {

	AgentDonationResultEntity donationEntity;
	Button btnOK;
	TextView tvAgentName, tvDonationAmount, tvRequestDate, tvApprovedAmount,
			tvApprovedAgentId, tvRequestPurpose;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	String whichPurpose = "FETCH_DONATION";
	String approvedAmount;
	int donationStatus;
	String donationId;
	TextView tvDonationStatus, tvDonationID;
	ImageView ivAgentImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agent_donation_details);
		NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
		initViews();
	}

	private void initViews() {
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvDonationAmount = (TextView) findViewById(R.id.tvDonationAmount);
		tvRequestDate = (TextView) findViewById(R.id.tvRequestDate);
		tvApprovedAmount = (TextView) findViewById(R.id.tvApprovedAmount);
		tvApprovedAgentId = (TextView) findViewById(R.id.tvApprovedAgentId);
		tvRequestPurpose = (TextView) findViewById(R.id.tvRequestPurpose);
		tvDonationStatus = (TextView) findViewById(R.id.tvDonationStatus);
		tvDonationID = (TextView) findViewById(R.id.tvDonationID);
		ivAgentImage=(ImageView) findViewById(R.id.ivAgentImage);
		btnOK = (Button) findViewById(R.id.btnOK);
		ivAgentImage.setVisibility(View.GONE);

		btnOK.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		donationId = bundle.getString("DONATION_ID");
		donationStatus = bundle.getInt("DONATION_STATUS");
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}

		loadInforMation();

	}

	private void loadInforMation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	public Object doInBackground() {
		IAdminManager manager = new AdminManager();
		return manager.agentGetIndividualDonationDetails(donationId);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {

			if (whichPurpose.equals("FETCH_DONATION")) {
				donationEntity = (AgentDonationResultEntity) data;
				
				if(donationEntity.adminname!=null){
					tvAgentName.setText(donationEntity.adminname);
				}else{
					tvAgentName.setText("Not Found");
				}
				
				
				tvDonationAmount.setText("" + donationEntity.amount);
				tvRequestPurpose.setText(donationEntity.comment);
				tvRequestDate.setText((String) DateUtils
						.getRelativeTimeSpanString(donationEntity.donationdate,
								new Date().getTime(), DateUtils.DAY_IN_MILLIS));

				tvApprovedAgentId.setText("" + donationEntity.agentid);

				if (donationStatus == 1) {
					tvDonationStatus.setText("Pending");
				} else if (donationStatus == 2) {
					tvDonationStatus.setText("Completed");
				} else {
					tvDonationStatus.setText("Rejected");
				}
				tvDonationID.setText(""+donationEntity.id);
				tvApprovedAmount.setText(""+donationEntity.acceptedamount);

			}

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.btnOK) {
			super.onBackPressed();

		}

	}

}
