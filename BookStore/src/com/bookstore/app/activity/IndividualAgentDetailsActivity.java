package com.bookstore.app.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentLocationRelated;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.ImageLoader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class IndividualAgentDetailsActivity extends BookStoreActionBarBase
		implements IAsynchronousTask, OnClickListener, OnMarkerClickListener,
		InfoWindowAdapter {

	ImageView ivAgentImage;
	TextView tvAgentName, tvAddress, tvMpoNumber, tvMobileNumber,
			tvAgentCurrentLocation, tvCreateDate, tvUserType;
	Button btnOk, btnCall;
	String agentId = "";

	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	AgentLocationRelated agentEntity = null;

	GoogleMap frAgentLocationMap;
	double mLatitude = 0;
	double mLongitude = 0;
	String data = "";
	InputStream iStream = null;
	HttpURLConnection urlConnection = null;
	JSONObject jObject;
	Marker mainMarker;
	Bitmap myBitmap;
	ArrayList<Marker> markers;
	String addressText = "";
	TableRow trCreateDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_agent_details);
		initViews();
	}

	private void initViews() {

		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvMpoNumber = (TextView) findViewById(R.id.tvMpoNumber);
		tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
		tvAgentCurrentLocation = (TextView) findViewById(R.id.tvAgentCurrentLocation);
		tvCreateDate = (TextView) findViewById(R.id.tvCreateDate);
		ivAgentImage = (ImageView) findViewById(R.id.ivAgentImage);
		trCreateDate = (TableRow) findViewById(R.id.trCreateDate);
		tvUserType = (TextView) findViewById(R.id.tvUserType);

		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.actvityAgentLocationMap);
		frAgentLocationMap = fragment.getMap();

		frAgentLocationMap.setOnMarkerClickListener(this);
		frAgentLocationMap.setInfoWindowAdapter(this);

		btnOk = (Button) findViewById(R.id.btnOk);
		btnCall = (Button) findViewById(R.id.btnCall);

		btnCall.setOnClickListener(this);
		btnOk.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		agentId = bundle.getString("AGENT_ID");

		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInforMation();
	}

	private void loadInforMation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Loading , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		IAdminManager adminManager = new AdminManager();
		agentEntity = adminManager.getIndividualAgentDetails(agentId);

		if (agentEntity.pic_url.equals("")) {
			myBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_person_24);
		} else {

		}

		return agentEntity;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			agentEntity = new AgentLocationRelated();
			agentEntity = (AgentLocationRelated) data;

			tvAgentName.setText(agentEntity.full_name);
			tvAddress.setText(agentEntity.address);
			tvMpoNumber.setText(agentEntity.mpo_no);
			tvMobileNumber.setText(agentEntity.mobile_no);
			if (agentEntity.location_name != null) {
				tvAgentCurrentLocation.setText(agentEntity.location_name);
			} else {
				tvAgentCurrentLocation.setText(agentEntity.address);
			}

			if (agentEntity.usertype == 1) {
				tvUserType.setText("Admin");
			} else {
				tvUserType.setText("Agent");
			}

			if (agentEntity.create_date != null
					&& !agentEntity.create_date.isEmpty()
					&& !agentEntity.create_date.equals("null")) {
				trCreateDate.setVisibility(View.VISIBLE);
				tvCreateDate.setText(CommonTasks
						.getLongToDate(agentEntity.create_date));
			} else {
				trCreateDate.setVisibility(View.GONE);
			}

			loadMap(agentEntity.latitude, agentEntity.longitude);

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server error , please try again");
			onBackPressed();
		}

	}

	private void loadMap(double lat, double lng) {

		markers = new ArrayList<Marker>();
		try {
			frAgentLocationMap.clear();

			// create instance of latlng class.
			LatLng Location = new LatLng(lat, lng);
			
			addressText = CommonTasks.getLocationNameFromLatLong(getApplicationContext(), Location);

			View marker = ((LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.custom_marker_layout, null);
			TextView numTxt = (TextView) marker
					.findViewById(R.id.tvInfoText);
			ImageView ivAgentImage = (ImageView) marker
					.findViewById(R.id.ivMapAgentImage);
			numTxt.setText(agentEntity.full_name);

			myBitmap = (CommonTasks.createCircularShape(CommonTasks
					.getBitmapFromSdCard(getApplicationContext(),
							"/sdcard/BookStore/" + "" + agentId + ".png")));
			ivAgentImage.setImageBitmap(myBitmap);

			markers.add(frAgentLocationMap.addMarker(new MarkerOptions()
					.position(Location).icon(
							BitmapDescriptorFactory
									.fromBitmap(createDrawableFromView(
											this, marker)))));
			
			if (addressText.equals("")) {
				addressText = "Agent Not Activated Yet.";
			}
			tvAgentCurrentLocation.setText(addressText);
			frAgentLocationMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			LatLngBounds.Builder b = new LatLngBounds.Builder();
			if (markers.size() > 0) {
				for (Marker m : markers) {
					b.include(m.getPosition());
				}
				LatLngBounds bounds = b.build();
				
				frAgentLocationMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(),10));
				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// Convert a view to bitmap
	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnCall) {
			if (agentEntity != null) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + agentEntity.mobile_no));
				startActivity(intent);
			} else {
				CommonTasks.showToast(getApplicationContext(),
						"Agent Has no Mobile Number");
			}

		} else {
			super.onBackPressed();
		}

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// Getting view from the layout file info_window_layout
		View v = this.getLayoutInflater().inflate(
				R.layout.maps_info_window_show, null);

		// Getting the position from the marker
		TextView tvLastUpdateTime = (TextView) v
				.findViewById(R.id.tvLastUpdateTime);
		TextView tvUserType1 = (TextView) v.findViewById(R.id.tvUserType);

		TextView tvLocationName = (TextView) v
				.findViewById(R.id.tvLocationName);
		TextView tvAgentName = (TextView) v.findViewById(R.id.tvAgentName);
		ImageView ivAgentImage = (ImageView) v.findViewById(R.id.ivAgentImage);
		
		tvLastUpdateTime.setText("Update Time :" +
				 CommonTasks.getLongToDate(""+agentEntity.create_date));
		tvAgentName.setText("Name :" + agentEntity.full_name);
		tvLocationName.setText("Current Location :" + addressText);
		if (agentEntity.usertype == 1) {
			tvUserType1.setText("User Type : Admin");
		} else {
			tvUserType1.setText("User Type : Agent");
		}
		/*
		 * tvLastUpdateTime.setText("Last Update :" +
		 * CommonTasks.getLongToDate(agentEntity.create_date));
		 */
		//ivAgentImage.setImageBitmap(CommonTasks.createCircularShape(myBitmap));
		ivAgentImage.setImageBitmap(myBitmap);
		// Returning the view containing InfoWindow contents
		return v;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

}
