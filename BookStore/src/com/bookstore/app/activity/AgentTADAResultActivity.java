package com.bookstore.app.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.AgentTaDaResultEntity;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class AgentTADAResultActivity extends AgentActionbarBase implements
		IAsynchronousTask, OnClickListener {

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;

	TextView tvStartPlace, tvEndPlace, tvTourPurpose, tvTourDate, tvTourCost,
			tvAdminName,tvDistance,tvTADAStatus;
	Button btnOk;
	String tadaID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_tada_result);
		NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(4);
		initViews();
	}

	private void initViews() {
		
		tvStartPlace=(TextView) findViewById(R.id.tvStartPlace);
		tvEndPlace=(TextView) findViewById(R.id.tvEndPlace);
		tvTourPurpose=(TextView) findViewById(R.id.tvTourPurpose);
		tvTourDate=(TextView) findViewById(R.id.tvTourDate);
		tvTourCost=(TextView) findViewById(R.id.tvTourCost);
		tvAdminName=(TextView) findViewById(R.id.tvAdminName);
		tvDistance=(TextView) findViewById(R.id.tvDistance);
		tvTADAStatus=(TextView) findViewById(R.id.tvTADAStatus);
		btnOk=(Button) findViewById(R.id.btnOk);
		
		btnOk.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		tadaID = bundle.getString("TADA_ID");
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		
		loadInformation();

	}

	private void loadInformation() {
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
		IAgent adminManager=new AgentManager();
		return adminManager.getIndividualTadaResultDetails(tadaID);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			AgentTaDaResultEntity agent=(AgentTaDaResultEntity) data;
			
			tvStartPlace.setText(agent.StartPlace);
			tvTourCost.setText(""+agent.totalamount);
			tvEndPlace.setText(agent.EndPlace);
			tvDistance.setText(""+agent.distance);
			tvAdminName.setText(agent.adminname);
			tvTourDate.setText(CommonTasks.getLongToDate(""+agent.createdate));
			
			if(agent.status==CommonConstraints.TADA_COMPLETED){
				tvTADAStatus.setText("Approved");
			}else if(agent.status==CommonConstraints.TADA_SUBMIT){
				tvTADAStatus.setText("Pending");
			}
			
			
		}else{
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

	@Override
	public void onClick(View arg0) {
		super.onBackPressed();
		
	}

}
