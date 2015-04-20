package com.bookstore.app.activity;

import java.util.Date;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.AgentInfo;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class AgentMyProfileActivity extends AgentActionbarBase implements
		OnClickListener, IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	TextView tvAgentName, tvMobileNumber, tvEmail, tvMpoNumber, tvAgentAddress,
			tvAgentCurrentLocation, tvCreateDate;

	Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_my_profile);
		initViews();
	}

	private void initViews() {
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvMpoNumber = (TextView) findViewById(R.id.tvMpoNumber);
		tvAgentAddress = (TextView) findViewById(R.id.tvAgentAddress);
		tvAgentCurrentLocation =  (TextView) findViewById(R.id.tvAgentCurrentLocation);
		tvCreateDate = (TextView) findViewById(R.id.tvCreateDate);
		btnOk = (Button) findViewById(R.id.btnOk);
		
		btnOk.setOnClickListener(this);
		
		LoadInformation();
	}

	@Override
	public void onClick(View view) {

	}
	
	private void LoadInformation() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Please Wait");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	public Object doInBackground() {
		IAgent agent = new AgentManager();
		return agent.getAgentInformation(Integer.parseInt(CommonTasks.getPreferences(this, CommonConstraints.USER_ID)));
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			AgentInfo agentInfo = (AgentInfo) data;
			if(agentInfo != null){
				setData(agentInfo);
			}
		}
	}

	private void setData(AgentInfo agentInfo) {
		tvAgentName.setText(agentInfo.AgentName);
		tvMobileNumber.setText(agentInfo.AgentMobileNumber);
		tvEmail.setText(agentInfo.AgentEmail);
		tvMpoNumber.setText(agentInfo.AgentMPONumber);
		tvAgentAddress.setText(agentInfo.AgentAddress);
		tvAgentCurrentLocation.setText(agentInfo.AgentLocation);
		tvCreateDate.setText((String) DateUtils.getRelativeTimeSpanString(
				agentInfo.AgentCreateDate, new Date().getTime(), DateUtils.DAY_IN_MILLIS));
	}

}
