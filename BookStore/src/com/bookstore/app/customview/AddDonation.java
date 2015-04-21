package com.bookstore.app.customview;

import com.bookstore.app.entities.Donation;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class AddDonation extends AsyncTask<Donation, Void, Boolean> {
	Context context;
	ProgressDialog progressDialog;
	
	public AddDonation(Context _Context) {
		context = _Context;
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Please wait");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	protected Boolean doInBackground(Donation... params) {
		IAgent agent = new AgentManager();
		return agent.addDonetion(Integer.parseInt(CommonTasks.getPreferences(context, CommonConstraints.USER_ID)), params[0].Amount, params[0].Comment);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(result){
			progressDialog.dismiss();
			CommonTasks.showToast(context, "Donation Submit!");
		}else{
			progressDialog.dismiss();
			CommonTasks.showToast(context, "Unexpected error! Please try again!");
		}
	}

}
