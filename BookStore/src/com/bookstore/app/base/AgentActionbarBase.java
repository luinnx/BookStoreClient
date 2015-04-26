package com.bookstore.app.base;

import com.bookstore.app.activity.AgentMyProfileActivity;
import com.bookstore.app.activity.LoginActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.customview.AddDonation;
import com.bookstore.app.entities.Donation;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.google.android.gms.drive.internal.al;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public abstract class AgentActionbarBase extends FragmentActivity {
	public ActionBar actionBar;
	private AlertDialog alertDialog;
	private EditText etDonationAmount, etDonationComment;
	private Button btn_donation_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Example);
		super.onCreate(savedInstanceState);
		createActionBar();
	}

	private void createActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);

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
		case R.id.action_profile:
			Intent int2 = new Intent(getApplicationContext(),
					AgentMyProfileActivity.class);
			int2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(int2);
			break;
		case R.id.action_logout:
			CommonTasks.savePreferencesForReasonCode(this,
					CommonConstraints.USER_ID, "" + "");
			Intent intent2=new Intent(getApplicationContext(),LoginActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(intent2);
			break;
			
		}
		return true;
	}

	private void agnetDonation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		LayoutInflater inflater = getLayoutInflater();
		View donationView = inflater.inflate(R.layout.activity_agent_donation, null);
		builder.setView(donationView);
		builder.setTitle("Donation");
		
		etDonationAmount = (EditText) donationView.findViewById(R.id.etDonationAmount);
		etDonationComment = (EditText) donationView.findViewById(R.id.etDonationComment);
		btn_donation_submit = (Button) donationView.findViewById(R.id.btn_donation_submit);
		
		btn_donation_submit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(etDonationAmount.getText().toString().equals("")){
					CommonTasks.showToast(AgentActionbarBase.this, "Please enter amount!");
					return;
				}
				if(etDonationComment.getText().toString().equals("")){
					CommonTasks.showToast(AgentActionbarBase.this, "Please enter comment!");
					return;
				}
				Donation donation = new Donation();
				donation.Amount = Double.parseDouble(etDonationAmount.getText().toString());
				donation.Comment = etDonationComment.getText().toString();				
				new AddDonation(AgentActionbarBase.this).execute(donation);
				alertDialog.dismiss();
			}
		});
		
		alertDialog = builder.create();
		alertDialog.show();
	}
}
