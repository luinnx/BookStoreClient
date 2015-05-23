package com.bookstore.app.activity;

import java.util.Date;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.DonationEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class DonationAcceptRejectActivity extends BookStoreActionBarBase
		implements IAsynchronousTask, OnClickListener {

	DonationEntity donationEntity;
	Button btnOK, btnReject, btnSubmit;
	EditText etApprovedAmount;
	TextView tvAgentName, tvDonationAmount, tvRequestDate, tvRequestPurpose;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	String whichPurpose = "FETCH_DONATION";
	String approvedAmount;
	int donationStatus;
	String donationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donation_details);
		initViews();
	}

	private void initViews() {
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvDonationAmount = (TextView) findViewById(R.id.tvDonationAmount);
		tvRequestDate = (TextView) findViewById(R.id.tvRequestDate);
		tvRequestPurpose = (TextView) findViewById(R.id.tvRequestPurpose);
		etApprovedAmount = (EditText) findViewById(R.id.etApprovedAmount);
		btnOK = (Button) findViewById(R.id.btnOK);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnReject = (Button) findViewById(R.id.btnReject);

		btnOK.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		btnReject.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		donationId = bundle.getString("DONATION_ID");
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
		if (whichPurpose.equals("FETCH_DONATION")) {
			IAdminManager manager = new AdminManager();
			return manager.getIndividualDonationDetails(donationId);
		} else {
			IAdminManager manager = new AdminManager();
			return manager.donationAck("" + donationEntity.id,
					donationEntity.gcmid, donationStatus, CommonTasks
							.getPreferences(getApplicationContext(),
									CommonConstraints.USER_ID), approvedAmount);
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {

			if (whichPurpose.equals("FETCH_DONATION")) {
				donationEntity = (DonationEntity) data;
				tvAgentName.setText(donationEntity.agent_name);
				tvDonationAmount.setText("" + donationEntity.Amount);
				tvRequestDate.setText((String) DateUtils
						.getRelativeTimeSpanString(donationEntity.date,
								new Date().getTime(), DateUtils.DAY_IN_MILLIS));
				tvRequestPurpose.setText(donationEntity.comment);
			} else {
				boolean b = (Boolean) data;
				if (b) {
					CommonTasks.showToast(getApplicationContext(),
							"Donation Approved Succesfully");
					onBackPressed();
				} else {
					CommonTasks.showToast(getApplicationContext(),
							"An Unexpected error occured.Please try again");
				}

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

		} else if (view.getId() == R.id.btnReject) {
			if (etApprovedAmount.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter Approved Amount");
				return;
			}
			whichPurpose = "SUBMIT_DONATION";
			donationStatus = CommonConstraints.DONATION_REGECTED;
			approvedAmount = etApprovedAmount.getText().toString().trim();
			loadInforMation();

		} else if (view.getId() == R.id.btnSubmit) {
			whichPurpose = "SUBMIT_DONATION";
			donationStatus = CommonConstraints.DONATION_COMPLETED;
			approvedAmount = "0";
			loadInforMation();
		}

	}

}