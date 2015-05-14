package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.IndividualTADA;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class ActivityTADADetails extends BookStoreActionBarBase implements OnClickListener, IAsynchronousTask{
	
	int tadaID;
	DownloadableAsyncTask asyncTask;
	ProgressDialog progressDialog;
	TextView tvAgentName, tvTadaStatus, tvTadaDate, tvStartPlace, tvStartTime, tvEndPlace, tvEndTime,
			tvComment, tvVehicelName, tvDistance, tvAmount, tvOtherAmount, tvTotalAmount;
	Button btnDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_tada);
		
		initialization();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LoadInformation();
	}

	private void initialization() {
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvTadaStatus = (TextView) findViewById(R.id.tvTadaStatus);
		tvTadaDate = (TextView) findViewById(R.id.tvTadaDate);
		tvStartPlace = (TextView) findViewById(R.id.tvStartPlace);
		tvStartTime = (TextView) findViewById(R.id.tvStartTime);
		tvEndPlace = (TextView) findViewById(R.id.tvEndPlace);
		tvEndTime = (TextView) findViewById(R.id.tvEndTime);
		tvComment = (TextView) findViewById(R.id.tvComment);
		tvVehicelName = (TextView) findViewById(R.id.tvVehicelName);
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvAmount = (TextView) findViewById(R.id.tvAmount);
		tvOtherAmount = (TextView) findViewById(R.id.tvOtherAmount);
		tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
		
		btnDone = (Button) findViewById(R.id.btnDone);
		
		btnDone.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		tadaID = bundle.getInt("TADA");
	}
	

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnDone){
			CommonTasks.showToast(this, "T.A.D.A Submit Done.");
			onBackPressed();
		}
	}
	
	private void LoadInformation(){
		if(asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	public Object doInBackground() {
		IAdminManager adminManager = new AdminManager();
		return adminManager.getTada(tadaID);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			IndividualTADA individualTADA = (IndividualTADA) data;
			setDataInfoView(individualTADA);
		}
	}

	private void setDataInfoView(IndividualTADA individualTADA) {
		tvAgentName.setText(individualTADA.Agentfull_name);
		tvTadaStatus.setText("Status : Pending");
		tvTadaDate.setText(individualTADA.tadadate);
		tvStartPlace.setText(individualTADA.startplace);
		tvStartTime.setText(individualTADA.starttime);
		tvEndPlace.setText(individualTADA.endplace);
		tvEndTime.setText(individualTADA.endtime);
		tvComment.setText(individualTADA.description);
		tvVehicelName.setText(individualTADA.vehicelname);
		tvDistance.setText(""+individualTADA.distance);
		tvAmount.setText(""+individualTADA.tadaamount);
		tvOtherAmount.setText(""+individualTADA.otheramount);
		tvTotalAmount.setText(""+individualTADA.totalamount);
	}
}