package com.bookstore.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bookstore.app.activity.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
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
		boolean result=false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			try {
				Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
		        int returnVal = p1.waitFor();
		        if(returnVal == 0)
		        	result= true;
	        } catch (MalformedURLException e1) {
	            Log.e("BSA", e1.getMessage());
	        } catch (IOException e) {
	        	Log.e("BSA", e.getMessage());
	        } catch (InterruptedException e) {
	        	Log.e("BSA", e.getMessage());
			}
		}
		return result;
	}

	

	@SuppressWarnings("unchecked")
	public static JSONObject convertResultsetToJson(ResultSet resultSet,
			String tag) {
		JSONArray array = new JSONArray();
		JSONObject object = new JSONObject();
		try {
			JSONObject jSONObject;
			while (resultSet.next()) {
				jSONObject = new JSONObject();
				int rowCount = resultSet.getMetaData().getColumnCount();
				for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
					jSONObject
							.put(resultSet.getMetaData()
									.getColumnLabel(rowIndex + 1).toLowerCase(),
									resultSet.getObject(rowIndex + 1));
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

	public static int getPicSlNo() {
		int number = -9999;
		Random random = new Random();
		number = random.nextInt(9999);
		return number;
	}

	public static Bitmap decodeImage(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveImageToDirectory(Bitmap imageToSave, String fileName) {

		Log.d("BSS", "Saving Started :" + fileName);
		File direct = new File(Environment.getExternalStorageDirectory()
				+ "/BookStore");

		if (!direct.exists()) {
			File wallpaperDirectory = new File("/sdcard/BookStore/");
			wallpaperDirectory.mkdirs();
		}

		File file = new File(new File("/sdcard/BookStore/"), fileName + ".png");
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			Log.d("BSS", e.getMessage());
		}
	}

	public static Bitmap getBitMapFromUrl(String _url) {
		URL url;
        Log.d("BSS", "Fetching Image : "+_url);
		try {
			url = new URL(_url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.d("BSS", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("BSS", e.getMessage());
		}

		/*
		 * URL url = new URL( CommonUrls.getInstance().IMAGE_BASE_URL +
		 * iterable_element.agentpicurl);
		 */

		return null;
	}

	public static Bitmap getBitmapFromSdCard(Context  context,String imagePath) {

		Log.d("BSS", "Getting Image :" + imagePath);
		Bitmap bitmap = null;
		File f = new File(imagePath);
		
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		try {
			if(f.exists()){
				bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null,
						options);
			}else{
				bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_person);
			}
			
		} catch (FileNotFoundException e) {
			Log.d("BSS", e.getMessage());
		}

		return bitmap;
	}
	
	public static Bitmap getBitmapFromSdCard(String imagePath) {

		Log.d("BSS", "Getting Image :" + imagePath);
		Bitmap bitmap = null;
		File f = new File(imagePath);
		
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		try {
			if(f.exists()){
				bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null,
						options);
			}
		} catch (FileNotFoundException e) {
			Log.d("BSS", e.getMessage());
		}

		return bitmap;
	}
	
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	public static String getLocationNameFromLatLong(Context context,LatLng latLng){
		String locationName="";
		
		try{
			Geocoder geocoder=new Geocoder(context, Locale.getDefault());
			List<Address> addresses=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
			
			if(addresses!=null&&addresses.size()>0){
				Address address=addresses.get(0);
				for(int lineIndex=0;lineIndex<address.getMaxAddressLineIndex();lineIndex++){
					locationName+=address.getAddressLine(lineIndex)+",";
				}
				locationName = locationName + address.getLocality()
						+ ", " + address.getCountryName();
			}
		}catch(Exception ex){
			Log.e("BSS", ex.getMessage());
		}
		return locationName;
	}
	
	
}
