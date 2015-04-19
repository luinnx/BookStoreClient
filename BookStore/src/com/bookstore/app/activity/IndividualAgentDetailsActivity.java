package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class IndividualAgentDetailsActivity extends BookStoreActionBarBase
		implements IAsynchronousTask {

	ImageView ivAgentImage;
	TextView tvAgentName, tvAddress, tvMpoNumber, tvMobileNumber,
			tvAgentCurrentLocation, tvCreateDate;
	Button btnOk, btnCall;
	String agentId = "";

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_agent_details);
		initViews();
	}

	private void initViews() {

		/*
		 * tvAgentName tvAddress tvCurrentLocation tvMpoNumber tvMobileNumber
		 * tvAgentCurrentLocation tvCreateDate
		 */

		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvMpoNumber = (TextView) findViewById(R.id.tvMpoNumber);
		tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
		tvAgentCurrentLocation = (TextView) findViewById(R.id.tvAgentCurrentLocation);
		tvCreateDate = (TextView) findViewById(R.id.tvCreateDate);

		Bundle bundle = getIntent().getExtras();
		agentId = bundle.getString("AGENT_ID");
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
		return adminManager.getIndividualAgentDetails(agentId);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			AgentEntity agentEntity = new AgentEntity();
			agentEntity = (AgentEntity) data;

			tvAgentName.setText(agentEntity.full_name);
			tvAddress.setText(agentEntity.address);
			tvMpoNumber.setText(agentEntity.mpo_no);
			tvMobileNumber.setText(agentEntity.mobile_no);
			tvAgentCurrentLocation.setText("Not Found");
			tvCreateDate.setText(CommonTasks
					.getLongToDate(agentEntity.create_date));

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server error , please try again");
		}

	}

}
