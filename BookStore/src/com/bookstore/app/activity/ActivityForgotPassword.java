package com.bookstore.app.activity;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.interfaces.IUser;
import com.bookstore.app.managers.UserManager;
import com.bookstore.app.utils.CommonTasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityForgotPassword extends Activity implements OnClickListener, IAsynchronousTask{
	
	EditText etMpo;
	Button btnOk;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		initialization();
	}

	private void initialization() {
		etMpo = (EditText) findViewById(R.id.etMpo);
		btnOk = (Button) findViewById(R.id.btnOk);
		
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnOk){
			if(etMpo.getText().toString().equals("")){
				CommonTasks.showToast(this, "Please enter MPO Number");
				return;
			}
			LoadInformation();
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
		progressDialog = new ProgressDialog(this);
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
		IUser user = new UserManager();
		return user.forgotPassword(CommonTasks.getIMEINumber(this), etMpo.getText().toString().trim());
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			boolean result = (Boolean) data;
			if(result){
				confirmationMessage();
			}else{
				CommonTasks.showToast(this, "Please try again!");
			}
		}
	}
	
	public void confirmationMessage(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("Forgot Password");
		builder.setMessage("A password has been send to your email. Please check your email and signin"
				+ " If any problem are occured please contact with Administrator.");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onBackPressed();
			}
		});
		
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onBackPressed();
			}
		});
		builder.show();
	}
}
