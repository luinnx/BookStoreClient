package com.bookstore.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bookstore.app.base.AgentActionbarBase;

public class AgentIndividualJobDetailsActivity extends AgentActionbarBase
		implements OnClickListener {
	LinearLayout llJobSubmitView;
	RelativeLayout rlJobSubmitView;
	ImageView ivSubmitJob;
	Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_agent_jod_details);
		initViews();
	}

	private void initViews() {
		ivSubmitJob = (ImageView) findViewById(R.id.ivSubmitJob);
		llJobSubmitView = (LinearLayout) findViewById(R.id.llJobSubmitView);
		btnOk=(Button) findViewById(R.id.btnOk);
		rlJobSubmitView=(RelativeLayout) findViewById(R.id.rlJobSubmitView);
		ivSubmitJob.setOnClickListener(this);
		btnOk.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivSubmitJob) {
			if (llJobSubmitView.getVisibility()==View.VISIBLE) {
				llJobSubmitView.setVisibility(View.GONE);
				ivSubmitJob.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_more_32));
				btnOk.setText("OK");
			} else {
				llJobSubmitView.setVisibility(View.VISIBLE);
				ivSubmitJob.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_arrow_up32));
				btnOk.setText("Submit");
			}

		}else if(view.getId()==R.id.btnOk){
			if(btnOk.getText().toString().equals("OK")){
				super.onBackPressed();
			}else{
				
			}
		}

	}

}
