package com.bookstore.app.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.JobDetails;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;

public class AgentIndividualJobDetailsActivity extends AgentActionbarBase implements OnClickListener, IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog  progressDialog;
	TextView tvNameBook, tvJobStatus, tvAuthor, tvPublisherName, tvISBN,
		tvQuantity, tvPublishDate, tvPrice, tvTeacherName, tvInstitution,
		tvTeacherMobileNumber, tvAgentName, tvAgentsAddress,
		tvAgentsCurrentLocation, tvAgentsMobileNumber, tvDialogCancel, tvDialogOK;
	EditText etTeacherPassword;
	Button btnOk;
	String jobID = "", mode = "";
	AlertDialog alertDialog;
	boolean isJobSubmit = false;
	JobDetails jobDetails= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_agent_jod_details);
		initialization();
	}

	private void initialization() {
		tvNameBook = (TextView) findViewById(R.id.tvNameBook);
		tvJobStatus = (TextView) findViewById(R.id.tvJobStatus);
		
		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		tvPublisherName = (TextView) findViewById(R.id.tvPublisherName);
		tvISBN = (TextView) findViewById(R.id.tvISBN);
		tvQuantity = (TextView) findViewById(R.id.tvQuantity);
		tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		
		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvInstitution = (TextView) findViewById(R.id.tvInstitution);
		tvTeacherMobileNumber = (TextView) findViewById(R.id.tvTeacherMobileNumber);
		
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAgentsAddress = (TextView) findViewById(R.id.tvAgentsAddress);
		tvAgentsCurrentLocation = (TextView) findViewById(R.id.tvAgentsCurrentLocation);
		tvAgentsMobileNumber = (TextView) findViewById(R.id.tvAgentsMobileNumber);
		
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.containsKey("JOB_ID") && bundle.containsKey("MODE")){
			jobID = bundle.getString("JOB_ID");
			mode = bundle.getString("MODE");
		}
		
		if(mode.equals("0")){
			btnOk.setText("Submit");
		}
		
		LoadInformation();
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.btnOk){
			if(btnOk.getText().toString().equals("Submit")){
				jobSubmit();
			}else{
				onBackPressed();
			}
		}
	}
	
	private void LoadInformation(){
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
		if(isJobSubmit)
			return agent.jobSubmit(jobDetails.TeacherUserName, etTeacherPassword.getText().toString(),
					jobDetails.BookID, jobDetails.No_Of_Book, 
					Integer.parseInt(jobID), 1);
		else
			return agent.getJobDetails(jobID);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(isJobSubmit){
				Boolean result = (Boolean) data;
				if(result){
					alertDialog.dismiss();
					CommonTasks.showToast(this, "Job Submit done.");
					onBackPressed();
				}else{
					CommonTasks.showToast(this, "UnExpected error. Please try again!");
					alertDialog.dismiss();
				}
			}else{
				jobDetails = (JobDetails) data;
				if(jobDetails != null){
					setValue(jobDetails);
				}
			}
			
		}
	}

	private void setValue(JobDetails jobDetails) {
		tvNameBook.setText(jobDetails.BookName);
		if(jobDetails.JobStatus)
			tvJobStatus.setText("Job Status : Complete");
		else
			tvJobStatus.setText("Job Status : Pending");
		
		tvAuthor.setText(jobDetails.BookAutherName);
		tvPublisherName.setText(jobDetails.BookPublisherName);
		tvISBN.setText(jobDetails.BookISBNNumber);
		tvQuantity.setText(""+jobDetails.BookQuantity);
		tvPublishDate.setText(jobDetails.BookPublishDate);
		tvPrice.setText(""+jobDetails.BookPrice);
		
		tvTeacherName.setText(jobDetails.TeacherName);
		tvInstitution.setText(jobDetails.TeacherInstituteName);
		tvTeacherMobileNumber.setText(jobDetails.TeacherMobileNumber);
		
		tvAgentName.setText(jobDetails.AgentFullName);
		tvAgentsAddress.setText(jobDetails.AgentAddress);
		tvAgentsCurrentLocation.setText(jobDetails.AgentCurrentLocation);
		tvAgentsMobileNumber.setText(jobDetails.AgentMobileNumber);
	}
	
	private void jobSubmit(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		LayoutInflater inflater = getLayoutInflater();
		View josSubmitView = inflater.inflate(R.layout.job_submit_dialog, null);
		builder.setView(josSubmitView);
		builder.setTitle("Job Submit");
		builder.setCancelable(false);
		
		etTeacherPassword = (EditText) josSubmitView.findViewById(R.id.etTeacherPassword);
		tvDialogCancel = (TextView) josSubmitView.findViewById(R.id.tvDialogCancel);
		tvDialogOK = (TextView) josSubmitView.findViewById(R.id.tvDialogOK);
		
		tvDialogCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		
		tvDialogOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isJobSubmit = true;
				if(etTeacherPassword.getText().toString().equals("")){
					CommonTasks.showToast(AgentIndividualJobDetailsActivity.this, "Please enter password");
					isJobSubmit = false;
					return;
				}
				LoadInformation();
			}
		});
		
		alertDialog = builder.create();
		alertDialog.show();
	}

}
