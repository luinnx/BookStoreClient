package com.bookstore.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bookstore.app.base.AgentActionbarBase;

public class AgentMyProfileActivity extends AgentActionbarBase implements OnClickListener{
	
	LinearLayout llEditPersonalInfo,llPersonalInfo;
	ImageView ivEditAgentsProfile;
	Button btnOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_my_profile);
		initViews();
	}
	private void initViews() {
		llEditPersonalInfo=(LinearLayout) findViewById(R.id.llEditPersonalInfo);
		llPersonalInfo=(LinearLayout) findViewById(R.id.llPersonalInfo);
		ivEditAgentsProfile=(ImageView) findViewById(R.id.ivEditAgentsProfile);
		btnOk=(Button) findViewById(R.id.btnOk);
		ivEditAgentsProfile.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		
		
	}
	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.ivEditAgentsProfile){
			if(llPersonalInfo.getVisibility()==View.VISIBLE){
				llPersonalInfo.setVisibility(View.GONE);
				ivEditAgentsProfile.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_arrow_up32));
				llEditPersonalInfo.setVisibility(View.VISIBLE);
				btnOk.setText("Submit");
			}else{
				llPersonalInfo.setVisibility(View.VISIBLE);
				ivEditAgentsProfile.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_edit_personal_info));
				llEditPersonalInfo.setVisibility(View.GONE);
				btnOk.setText("OK");
			}
		}else if(view.getId()==R.id.btnOk){
			if(btnOk.getText().toString().equals("OK")){
				super.onBackPressed();
			}else{
				
			}
		}
		
	}

}
