package com.bookstore.app.base;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.customview.AddLocation;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.interfaces.IUser;
import com.bookstore.app.managers.UserManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class BookStoreService extends Service implements IAsynchronousTask,
		LocationListener {
	DownloadableAsyncTask downloadAsyncTask;
	GoogleCloudMessaging gcm;
	String regid = "";
	double latitide, longitude;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		regid = CommonTasks.getPreferences(this, CommonConstraints.GCMID);
		if (regid.isEmpty()) {

			if (CommonTasks.isOnline(getApplicationContext()))
				LoadInformation();
		}
		/*if (CommonTasks.getPreferences(this, CommonConstraints.USER_TYPE)
				.equals("2")) {
			getLocation();
		}*/
		getLocation();
		return START_STICKY;
	}

	private void getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 600000, 0, this);
	}

	private void LoadInformation() {
		if (downloadAsyncTask != null)
			downloadAsyncTask.cancel(true);
		downloadAsyncTask = new DownloadableAsyncTask(this);
		downloadAsyncTask.execute();
	}

	@Override
	public void showProgressBar() {

	}

	@Override
	public void hideProgressBar() {

	}

	@Override
	public Object doInBackground() {
		try {
			if (regid.isEmpty()) {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(this);
				}
				regid = gcm.register(CommonConstraints.APPID);
			}
			IUser user = new UserManager();
			return user.addGCMID(Integer.parseInt(CommonTasks.getPreferences(
					this, CommonConstraints.USER_ID)), regid);
		} catch (Exception ex) {
			Log.e("SB", ex.getMessage());
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			Boolean result = (Boolean) data;
			if (result) {
				CommonTasks.savePreferencesForReasonCode(this,
						CommonConstraints.GCMID, regid);
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		new AddLocation(this).execute(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

}
