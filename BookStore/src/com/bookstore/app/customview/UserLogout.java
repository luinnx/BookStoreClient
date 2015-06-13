package com.bookstore.app.customview;

import com.bookstore.app.activity.LoginActivity;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.base.InternetConnectionService;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IUser;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.managers.UserManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonValues;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class UserLogout extends AsyncTask<Integer, Void, Object> {
	Context context;
	ProgressDialog progressDialog;

	public UserLogout(Context _context) {
		context = _context;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context,
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Logout. Please wait!");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	protected Object doInBackground(Integer... params) {
		if(CommonTasks.isOnline(context))
			return null;
		IUser user = new UserManager();
		return user.logout(params[0].hashCode());
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result != null && result.equals(true)) {
			progressDialog.dismiss();
			CommonTasks.savePreferencesForReasonCode(context,
					CommonConstraints.USER_ID, "" + "");
			CommonTasks.savePreferencesForReasonCode(context,
					CommonConstraints.USER_TYPE, "" + "");
			CommonTasks.savePreferencesForReasonCode(context,
					CommonConstraints.GCMID, "" + "");

			Intent intent2 = new Intent(context, LoginActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent2);
		} else {
			CommonTasks.showToast(context, "Please try again!");
		}
	}
}
