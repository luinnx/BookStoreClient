package com.bookstore.app.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CommonTasks {
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public final static long ONE_SECOND = 1000;
	public final static long SECONDS = 60;

	public final static long ONE_MINUTE = ONE_SECOND * 60;
	public final static long MINUTES = 60;

	public final static long ONE_HOUR = ONE_MINUTE * 60;
	public final static long HOURS = 24;

	public final static long ONE_DAY = ONE_HOUR * 24;

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

	public static String getRelativeTime(long duration) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date past;
		String result = "";
		try {
			past = format.parse(getLongToDate("" + duration));
			Date now = new Date();

			if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) > 0) {
				result += ""
						+ TimeUnit.MILLISECONDS.toMinutes(now.getTime()
								- past.getTime()) + " minutes";
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;

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

	public static String getIMEINumber(Context _context) {
		String imei = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) _context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = "" + telephonyManager.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imei.trim();
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
	
	@SuppressWarnings("unchecked")
	public static JSONObject convertResultsetToJson(ResultSet resultSet, String tag){
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        try {
            JSONObject jSONObject;
            while(resultSet.next()){
                jSONObject = new JSONObject();
                int rowCount = resultSet.getMetaData().getColumnCount();
                for(int rowIndex=0;rowIndex<rowCount;rowIndex++){
                    jSONObject.put(resultSet.getMetaData().getColumnLabel(rowIndex+1).toLowerCase(), resultSet.getObject(rowIndex+1));
                }
                array.add(jSONObject);
            }    
            object.put(tag, array);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return object;
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

	public static Bitmap createCircularShape(Bitmap bitmap) {
		Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setShader(shader);
		paint.setAntiAlias(true);
		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		return circleBitmap;
	}
}
