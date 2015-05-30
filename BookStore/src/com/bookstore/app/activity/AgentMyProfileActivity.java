package com.bookstore.app.activity;

import java.util.Date;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.AgentInfo;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.interfaces.IUser;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.managers.UserManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;
import com.bookstore.app.utils.ImageLoader;
import com.mikhaellopez.circularimageview.CircularImageView;

public class AgentMyProfileActivity extends AgentActionbarBase implements
		OnClickListener, IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	TextView tvAgentName, tvMobileNumber, tvEmail, tvMpoNumber, tvAgentAddress,
			tvAgentCurrentLocation, tvCreateDate,tvDialogCancel,tvDialogOK;
	EditText etOldPassword, etNewPassword, etConfirmPassword;
	
	Button btnOk,btnForgotPassword;
	CircularImageView ivAgentImage;
	AlertDialog alertDialog;
	boolean isChangePassword;
	AQuery aq;
	ImageOptions imgOptions;
	ImageLoader imageLoader;

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
		btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);
		ivAgentImage = (CircularImageView) findViewById(R.id.ivAgentImage);
		
		btnOk.setOnClickListener(this);
		btnForgotPassword.setOnClickListener(this);
		
		aq = new AQuery(this);	
		imageLoader = new ImageLoader(this);	
		imgOptions = CommonValues.getInstance().defaultImageOptions; 		
		imgOptions.targetWidth=100;
		imgOptions.ratio=0;//AQuery.RATIO_PRESERVE;
		imgOptions.round = 8;
		
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		LoadInformation();
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.btnOk){
			onBackPressed();
		}else if(view.getId() == R.id.btnForgotPassword){
			ChangePassword();
		}
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
		if(isChangePassword){
			IUser user = new UserManager();
			return user.changePassword(
					Integer.parseInt(CommonTasks.getPreferences(this, CommonConstraints.USER_ID)), 
					etOldPassword.getText().toString().trim(), 
					etNewPassword.getText().toString().trim(), 
					Integer.parseInt(CommonTasks.getPreferences(this, CommonConstraints.USER_TYPE)));
		}else{
			IAgent agent = new AgentManager();
			return agent.getAgentInformation(Integer.parseInt(CommonTasks.getPreferences(this, CommonConstraints.USER_ID)));
		}		
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(isChangePassword){
				Boolean result = (Boolean) data;
				if(result){
					alertDialog.dismiss();
					CommonTasks.showToast(this, "Password Chagne Successfully");
					isChangePassword = false;
				}else{
					alertDialog.dismiss();
					isChangePassword =false;
					CommonTasks.showToast(this, "UnExpected Error! Please try again!");
				}
			}else{
				AgentInfo agentInfo = (AgentInfo) data;
				if(agentInfo != null){
					setData(agentInfo);
				}
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
		aq.id(ivAgentImage).image(CommonUrls.getInstance().IMAGE_BASE_URL+agentInfo.AgentPicUrl, imgOptions);
	}
	
	private void ChangePassword(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		LayoutInflater inflater = getLayoutInflater();
		View chagnePasswordView = inflater.inflate(R.layout.change_password, null);
		builder.setView(chagnePasswordView); 
		builder.setTitle("Change Password");
		
		etOldPassword = (EditText) chagnePasswordView.findViewById(R.id.etOldPassword);
		etNewPassword = (EditText) chagnePasswordView.findViewById(R.id.etNewPassword);
		etConfirmPassword = (EditText) chagnePasswordView.findViewById(R.id.etConfirmPassword);
		
		tvDialogCancel = (TextView) chagnePasswordView.findViewById(R.id.tvDialogCancel);
		tvDialogOK = (TextView) chagnePasswordView.findViewById(R.id.tvDialogOK);
		
		tvDialogCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		
		tvDialogOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(etOldPassword.getText().toString().trim().equals("")){
					CommonTasks.showToast(AgentMyProfileActivity.this, "Plese enter old password!");
					return;
				}
				if(etNewPassword.getText().toString().trim().equals("")){
					CommonTasks.showToast(AgentMyProfileActivity.this, "Plese enter new password!");
					return;
				}
				if(etConfirmPassword.getText().toString().trim().equals("")){
					CommonTasks.showToast(AgentMyProfileActivity.this, "Plese enter confirm password!");
					return;
				}
				if(etConfirmPassword.getText().toString().trim().equals(etNewPassword.getText().toString()) == false){
					CommonTasks.showToast(AgentMyProfileActivity.this, "Password not match!");
					return;
				}
				
				if (!CommonTasks.isOnline(getApplicationContext())) {
					CommonTasks.goSettingPage(getApplicationContext());
					return;
				}
				isChangePassword = true;
				LoadInformation();
			}
		});
		
		alertDialog = builder.create();
		alertDialog.show();
	}

}
