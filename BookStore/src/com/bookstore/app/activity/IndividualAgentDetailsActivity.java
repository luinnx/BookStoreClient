package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;
import com.bookstore.app.utils.ImageLoader;

public class IndividualAgentDetailsActivity extends BookStoreActionBarBase
		implements IAsynchronousTask, OnClickListener {

	ImageView ivAgentImage;
	TextView tvAgentName, tvAddress, tvMpoNumber, tvMobileNumber,
			tvAgentCurrentLocation, tvCreateDate;
	Button btnOk, btnCall;
	String agentId = "";

	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	AgentEntity agentEntity = null;

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
		ivAgentImage = (ImageView) findViewById(R.id.ivAgentImage);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnCall = (Button) findViewById(R.id.btnCall);

		btnCall.setOnClickListener(this);
		btnOk.setOnClickListener(this);

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
			agentEntity = new AgentEntity();
			agentEntity = (AgentEntity) data;

			tvAgentName.setText(agentEntity.full_name);
			tvAddress.setText(agentEntity.address);
			tvMpoNumber.setText(agentEntity.mpo_no);
			tvMobileNumber.setText(agentEntity.mobile_no);
			tvAgentCurrentLocation.setText("Not Found");
			tvCreateDate.setText(CommonTasks
					.getLongToDate(agentEntity.create_date));

			aq = new AQuery(this);
			imageLoader = new ImageLoader(this);
			imgOptions = CommonValues.getInstance().defaultImageOptions;
			imgOptions.targetWidth = 100;
			imgOptions.ratio = 0;
			imgOptions.round = 8;

			if (agentEntity.pic_url.equals("")) {
				aq.id(ivAgentImage).image(
						this.getResources()
								.getDrawable(R.drawable.ic_person_24));
			} else {
				aq.id(ivAgentImage)
						.image((CommonUrls.getInstance().IMAGE_BASE_URL + agentEntity.pic_url
								.toString()), imgOptions);
			}

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server error , please try again");
		}

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnCall) {
			if (agentEntity != null) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + agentEntity.mobile_no));
				startActivity(intent);
			} else {
				CommonTasks.showToast(getApplicationContext(),
						"Agent Has no Mobile Number");
			}

		} else {
			super.onBackPressed();
		}

	}

}
