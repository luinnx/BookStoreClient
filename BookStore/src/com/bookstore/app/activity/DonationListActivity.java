package com.bookstore.app.activity;

import java.util.Date;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

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
	ProgressDialog dialog;
	DonationListAdapter adapter;
	DonationListRoot donationListRoot;
	Dialog donationDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donation);
		initViews();
	}

	private void initViews() {
		lvAllDonationList = (ListView) findViewById(R.id.lvAllDonationList);
		lvAllDonationList.setOnItemClickListener(this);

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
		// TODO Auto-generated method stub

	}

	@Override
	public void hideProgressBar() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object doInBackground() {
		IAdminManager manager = new AdminManager();
		return manager.getAllDonationList(0);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			donationListRoot = new DonationListRoot();
			donationListRoot = (DonationListRoot) data;
			adapter = new DonationListAdapter(getApplicationContext(),
					R.layout.book_list_item, donationListRoot.donationList);
			lvAllDonationList.setAdapter(adapter);

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		DonationEntity donationEntity = donationListRoot.donationList
				.get(position);
		donationDetails = new Dialog(this);
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
		Button btnOK = (Button) donationDetails.findViewById(R.id.btnOK);
		
		tvAgentName.setText(donationEntity.agent_name);
		tvDonationAmount.setText(""+donationEntity.Amount);
		tvRequestDate.setText((String) DateUtils.getRelativeTimeSpanString(
				donationEntity.date, new Date().getTime(), DateUtils.DAY_IN_MILLIS));
		tvRequestPurpose.setText(donationEntity.Comment);
		
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				donationDetails.dismiss();
				
			}
		});
		donationDetails.show();
	}
}
