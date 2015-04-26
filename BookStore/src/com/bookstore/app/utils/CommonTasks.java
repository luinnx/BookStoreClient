package com.bookstore.app.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class CommonTasks {
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	public static void savePreferencesForReasonCode(Context context,
			String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getPreferences(Context context, String prefKey) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getString(prefKey, "");
	}

	public static String toStringDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getLongToDate(String time) {
		long foo = Long.parseLong(time);
		Date date = new Date(foo);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return (formatter.format(date));
	}

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static String getPhoneId(Context _context) {
		String phoneId = Secure.getString(_context.getContentResolver(),
				Secure.ANDROID_ID);
		return phoneId;
	}

	public static void showLogs(Context context, String message) {
		Log.d(CommonConstraints.TAG, message);
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static void goSettingPage(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("Connectivity");
		builder.setMessage("Opps! Internet connection are disable.Enable access this apps under Settings->Wi-Fi->Turn on.");
		builder.setPositiveButton("Settings", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				context.startActivity(new Intent(
						android.provider.Settings.ACTION_SETTINGS));
			}
		});

		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.show();
	}

	public static boolean checkPlayServices(Activity context) {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, context,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.e("BSA", "This device is not supported.");
				context.finish();
			}
			return false;
		}
		return true;
	}
}
