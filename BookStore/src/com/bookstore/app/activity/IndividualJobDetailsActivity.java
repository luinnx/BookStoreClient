package com.bookstore.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.base.BookStoreActionBarBase;

public class IndividualJobDetailsActivity extends BookStoreActionBarBase
		implements OnClickListener {

	ImageView ivJobBookImage;
	TextView tvNameBook, tvJobStatus, tvAuthor, tvCurrentLocation, tvISBN,
			tvQuantity, tvPublishDate, tvPrice, tvMpoNumber, tvMobileNumber,
			tvTeacherMobileNumber, tvAgentName, tvAgentsAddress,
			tvAgentsCurrentLocation, tvAgentsMobileNumber;
	Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_job_details);
		initViews();

	}

	private void initViews() {
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {
			super.onBackPressed();
		}

	}

}
