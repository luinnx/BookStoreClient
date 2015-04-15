package com.bookstore.app.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.base.BookStoreActionBarBase;

public class IndividualAgentDetailsActivity extends BookStoreActionBarBase {

	ImageView ivAgentImage;
	TextView tvAgentName, tvAddress, tvCurrentLocation, tvMpoNumber,
			tvMobileNumber, tvAgentCurrentLocation, tvCreateDate;
	Button btnOk, btnCall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_agent_details);
		initViews();
	}

	private void initViews() {

	}

}
