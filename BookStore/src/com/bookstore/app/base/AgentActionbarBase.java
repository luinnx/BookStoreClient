package com.bookstore.app.base;

import com.bookstore.app.activity.AdminNoInterNetActivity;
import com.bookstore.app.activity.AgentDonationListActivity;
import com.bookstore.app.activity.AgentMyProfileActivity;
import com.bookstore.app.activity.AgentTADAListActivity;
import com.bookstore.app.activity.AgentTA_DA_Activity;
import com.bookstore.app.activity.R;
import com.bookstore.app.base.InternetConnectionService.ConnectionServiceCallback;
import com.bookstore.app.customview.AddDonation;
import com.bookstore.app.customview.UserLogout;
import com.bookstore.app.entities.Donation;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public abstract class AgentActionbarBase extends FragmentActivity implements
		ConnectionServiceCallback {
	public ActionBar actionBar;
	private AlertDialog alertDialog;
	private EditText etDonationAmount, etDonationComment;
	private Button btn_donation_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Example);
		super.onCreate(savedInstanceState);
		createActionBar();
		//startNetCheckingService();
	}

	private void createActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);

	}
	
	private void startNetCheckingService() {
		Intent intent = new Intent(AgentActionbarBase.this, InternetConnectionService.class);
		// Interval in seconds
		intent.putExtra(InternetConnectionService.TAG_INTERVAL, 20);
		// URL to ping
		intent.putExtra(InternetConnectionService.TAG_URL_PING,
				"http://www.google.com");
		// Name of the class that is calling this service
		intent.putExtra(InternetConnectionService.TAG_ACTIVITY_NAME, this
				.getClass().getName());
		// Starts the service
		startService(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.agent_actionbar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_donation:
			agnetDonation();
			break;
		case R.id.action_tada_add:
			Intent int1 = new Intent(getApplicationContext(),
					AgentTA_DA_Activity.class);
			int1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(int1);
			break;
		case R.id.actionTadaList:
			Intent int4 = new Intent(getApplicationContext(),
					AgentTADAListActivity.class);
			int4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(int4);
			break;
		case R.id.action_donation_list:
			Intent doantion = new Intent(getApplicationContext(),
					AgentDonationListActivity.class);
			doantion.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(doantion);
			break;
		case R.id.action_profile:
			Intent int2 = new Intent(getApplicationContext(),
					AgentMyProfileActivity.class);
			int2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(int2);
			break;
		case R.id.action_logout:
			if (CommonTasks.isOnline(getApplicationContext()))
				new UserLogout(this).execute(Integer.parseInt(CommonTasks
						.getPreferences(this, CommonConstraints.USER_ID)));
			else
				CommonTasks.goSettingPage(getApplicationContext());
			break;

		}
		return true;
	}

	private void agnetDonation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		LayoutInflater inflater = getLayoutInflater();
		View donationView = inflater.inflate(R.layout.activity_agent_donation,
				null);
		builder.setView(donationView);
		builder.setTitle("Donation");

		etDonationAmount = (EditText) donationView
				.findViewById(R.id.etDonationAmount);
		etDonationComment = (EditText) donationView
				.findViewById(R.id.etDonationComment);
		btn_donation_submit = (Button) donationView
				.findViewById(R.id.btn_donation_submit);

		btn_donation_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etDonationAmount.getText().toString().equals("")) {
					CommonTasks.showToast(AgentActionbarBase.this,
							"Please enter amount!");
					return;
				}
				if (etDonationComment.getText().toString().equals("")) {
					CommonTasks.showToast(AgentActionbarBase.this,
							"Please enter comment!");
					return;
				}
				Donation donation = new Donation();
				donation.Amount = Double.parseDouble(etDonationAmount.getText()
						.toString());
				donation.Comment = etDonationComment.getText().toString();

				/*if (CommonTasks.isOnline(getApplicationContext())) {
					new AddDonation(AgentActionbarBase.this).execute(donation);
					alertDialog.dismiss();
				} else {
					CommonTasks.goSettingPage(getApplicationContext());
					return;
				}*/
			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}
	
	@Override
	public void hasInternetConnection() {
		Log.d("BNS", "HAS Internet");
	}

	@Override
	public void hasNoInternetConnection() {
		Log.d("BNS", "No Internet");
		//stopService(new Intent(this, InternetConnectionService.class));
		Intent intent = new Intent(AgentActionbarBase.this, AdminNoInterNetActivity.class);
		startActivity(intent);
	}
}
