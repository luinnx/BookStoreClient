package com.bookstore.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.bookstore.app.adapters.DonationListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.DonationEntity;
import com.bookstore.app.entities.DonationListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class DonationListActivity extends BookStoreActionBarBase implements
		OnItemClickListener, IAsynchronousTask {

	ListView lvAllDonationList;

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	DonationListAdapter adapter;
	DonationListRoot donationListRoot;
	Dialog donationDetails;
	String whichPurpose = "FETCH_DONATION";
	DonationEntity donationEntity;
	String approvedAmount;
	int donationStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donation);
		initViews();
	}

	private void initViews() {
		lvAllDonationList = (ListView) findViewById(R.id.lvAllDonationList);
		lvAllDonationList.setOnItemClickListener(this);
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
		return manager.getAllDonationList(0);
		/*if (whichPurpose.equals("FETCH_DONATION")) {
			IAdminManager manager = new AdminManager();
			return manager.getAllDonationList(0);
		}*/ /*else {
			IAdminManager manager = new AdminManager();
			return manager.donationAck("" + donationEntity.id,
					donationEntity.gcmid, donationStatus, CommonTasks
							.getPreferences(getApplicationContext(),
									CommonConstraints.USER_ID), approvedAmount);
		}*/

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {

			if (whichPurpose.equals("FETCH_DONATION")) {
				donationListRoot = new DonationListRoot();
				donationListRoot = (DonationListRoot) data;
				adapter = new DonationListAdapter(getApplicationContext(),
						R.layout.book_list_item, donationListRoot.donationList);
				lvAllDonationList.setAdapter(adapter);
			} /*else {
				boolean b = (Boolean) data;
				if (b) {
					CommonTasks.showToast(getApplicationContext(),
							"Donation Approved Succesfully");
					//lvAllDonationList.notify();
					onBackPressed();
				} else {
					CommonTasks.showToast(getApplicationContext(),
							"An Unexpected error occured.Please try again");
				}

			}*/

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		donationEntity = donationListRoot.donationList.get(position);
		
		Intent intent=new Intent(getApplicationContext(), DonationAcceptRejectActivity.class);
		intent.putExtra("DONATION_ID", ""+donationEntity.id);
		startActivity(intent);
		
		
		/*donationDetails = new Dialog(this);
		donationDetails.setContentView(R.layout.donation_details);
		donationDetails.setCancelable(true);
		donationDetails.setTitle("Donation Details Dialog");

		TextView tvAgentName = (TextView) donationDetails
				.findViewById(R.id.tvAgentName);
		TextView tvDonationAmount = (TextView) donationDetails
				.findViewById(R.id.tvDonationAmount);
		TextView tvRequestDate = (TextView) donationDetails
				.findViewById(R.id.tvRequestDate);
		TextView tvRequestPurpose = (TextView) donationDetails
				.findViewById(R.id.tvRequestPurpose);
		final EditText etApprovedAmount = (EditText) donationDetails
				.findViewById(R.id.etApprovedAmount);
		Button btnOK = (Button) donationDetails.findViewById(R.id.btnOK);
		Button btnSubmit = (Button) donationDetails
				.findViewById(R.id.btnSubmit);
		Button btnReject = (Button) donationDetails
				.findViewById(R.id.btnReject);

		tvAgentName.setText(donationEntity.agent_name);
		tvDonationAmount.setText("" + donationEntity.Amount);
		tvRequestDate.setText((String) DateUtils.getRelativeTimeSpanString(
				donationEntity.date, new Date().getTime(),
				DateUtils.DAY_IN_MILLIS));
		tvRequestPurpose.setText(donationEntity.comment);

		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				donationDetails.dismiss();

			}
		});

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (etApprovedAmount.getText().toString().trim().equals("")) {
					CommonTasks.showToast(getApplicationContext(),
							"Please Enter Approved Amount");
					return;
				}
				whichPurpose = "SUBMIT_DONATION";
				donationStatus = CommonConstraints.DONATION_COMPLETED;
				approvedAmount = etApprovedAmount.getText().toString().trim();
				loadInforMation();
				donationDetails.dismiss();

			}
		});

		btnReject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				whichPurpose = "SUBMIT_DONATION";
				donationStatus = CommonConstraints.DONATION_REGECTED;
				approvedAmount = "0";
				loadInforMation();
				donationDetails.dismiss();

			}
		});

		donationDetails.show();*/
	}

}
