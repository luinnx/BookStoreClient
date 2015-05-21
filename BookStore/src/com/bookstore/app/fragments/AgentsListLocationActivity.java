package com.bookstore.app.fragments;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.IndividualAgentDetailsActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.AgentLocationEntity;
import com.bookstore.app.entities.AgentLocationMapRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;
import com.bookstore.app.utils.ImageLoader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AgentsListLocationActivity extends Fragment implements
		LocationListener, IAsynchronousTask, OnItemClickListener,
		OnMarkerClickListener, InfoWindowAdapter {

	ListView lvAgentList;
	AgentListAdapter adapter;
	AgentListRoot agentListRoot = null;
	GoogleMap frAgentLocationMap;
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog progressDialog;

	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;
	double mLatitude = 0;
	double mLongitude = 0;
	String data = "";
	InputStream iStream = null;
	HttpURLConnection urlConnection = null;
	JSONObject jObject;
	ArrayList<Marker> markers;
	Hashtable<String, String> uriMarkers;
	Marker mainMarker;
	AgentLocationMapRoot agentLocationMapRoot = null;
	private static View view;
	ViewGroup parent;
	String whichService = "Agent List";
	ArrayList<Bitmap> mapPhotoList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		try {
			view = inflater.inflate(R.layout.fragment_all_agent_list_location,
					container, false);
			initalization(view);
		} catch (InflateException e) {
		}
		return view;

	}

	public static AgentsListLocationActivity newInstance(String text) {

		AgentsListLocationActivity f = new AgentsListLocationActivity();
		Bundle b = new Bundle();
		b.putString("msg", text);

		f.setArguments(b);

		return f;
	}

	private void initalization(View root) {
		lvAgentList = (ListView) root.findViewById(R.id.lvAgentList);

		lvAgentList.setOnItemClickListener(this);
		SupportMapFragment fragment = ((SupportMapFragment) getChildFragmentManager()
				.findFragmentById(R.id.fragAgentLocationMap));
		frAgentLocationMap = fragment.getMap();
		frAgentLocationMap.setOnMarkerClickListener(this);
		frAgentLocationMap.setInfoWindowAdapter(this);
		if (!CommonTasks.isOnline(getActivity())) {
			CommonTasks.goSettingPage(getActivity());
			return;
		}
		LoginRequest();
	}

	private void LoginRequest() {
		if (downloadAsyncTask != null)
			downloadAsyncTask.cancel(true);
		downloadAsyncTask = new DownloadableAsyncTask(this);
		downloadAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(getActivity(),
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait.");
		progressDialog.show();

	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		IAdminManager adminManager = new AdminManager();
		if (whichService.equals("Agent List")) {
			return adminManager.getAgentList(0);
		} else {
			AgentLocationMapRoot agentLocationMapRoot = null;
			try {
				agentLocationMapRoot = new AgentLocationMapRoot();
				agentLocationMapRoot = adminManager.getAgentsLocation();
				mapPhotoList = new ArrayList<Bitmap>();
				for (AgentLocationEntity iterable_element : agentLocationMapRoot.agentLocationList) {
					if (!iterable_element.agentpicurl.equals("")) {
						URL url = new URL(
								CommonUrls.getInstance().IMAGE_BASE_URL
										+ iterable_element.agentpicurl);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream input = connection.getInputStream();
						Bitmap myBitmap = BitmapFactory.decodeStream(input);
						mapPhotoList.add(myBitmap);
					} else {
						mapPhotoList.add(BitmapFactory.decodeResource(
								getActivity().getResources(),
								R.drawable.ic_person_24));
					}

				}
			} catch (Exception ex) {
				Log.e("BSA", ex.getMessage());
			}

			return agentLocationMapRoot;
		}

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {

			if (whichService.equals("Agent List")) {
				agentListRoot = (AgentListRoot) data;
				adapter = new AgentListAdapter(getActivity(),
						R.layout.agent_list_item, agentListRoot.agentList);
				whichService = "Load Map";

				lvAgentList.setAdapter(adapter);
				LoginRequest();
			} else {
				agentLocationMapRoot = (AgentLocationMapRoot) data;
				if (agentLocationMapRoot != null
						&& agentLocationMapRoot.agentLocationList.size() > 0) {
					LoadMap(agentLocationMapRoot);
				}
			}

		} else {

			CommonTasks.showToast(getActivity(),
					"Can not loading data. Internal server error");

		}

	}

	private void LoadMap(AgentLocationMapRoot locationMapRoot) {
		String addressText = "";
		markers = new ArrayList<Marker>();
		try {
			frAgentLocationMap.clear();
			for (int rowIndex = 0; rowIndex < locationMapRoot.agentLocationList
					.size(); rowIndex++) {
				double lat = locationMapRoot.agentLocationList.get(rowIndex).latitude;
				double lng = locationMapRoot.agentLocationList.get(rowIndex).longitude;
				// create instance of latlng class.
				LatLng Location = new LatLng(lat, lng);
				// Create instance of geocoder that is used for geting
				// address if marker pointer in pressed.
				Geocoder geocoder = new Geocoder(getActivity(),
						Locale.getDefault());
				// get list of address where lat and lang r putted
				List<Address> addresses = geocoder.getFromLocation(
						Location.latitude, Location.longitude, 1);
				// check address are available and get it using for loop and
				// store in address
				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					for (int lineIndex = 0; lineIndex < address
							.getMaxAddressLineIndex(); lineIndex++) {
						addressText = addressText
								+ address.getAddressLine(lineIndex) + ", ";
					}
					addressText = addressText + address.getLocality() + ", "
							+ address.getCountryName();

					View marker = ((LayoutInflater) getActivity()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							.inflate(R.layout.custom_marker_layout, null);
					TextView numTxt = (TextView) marker
							.findViewById(R.id.tvInfoText);
					CircularImageView ivAgentImage = (CircularImageView) marker
							.findViewById(R.id.ivMapAgentImage);
					numTxt.setText(locationMapRoot.agentLocationList
							.get(rowIndex).agentname);

					
					
					
					
					ivAgentImage.setImageBitmap(CommonTasks.createCircularShape(mapPhotoList.get(rowIndex)));

					markers.add(frAgentLocationMap
							.addMarker(new MarkerOptions().position(Location)
									.icon(BitmapDescriptorFactory
											.fromBitmap(createDrawableFromView(
													getActivity(), marker)))));

				}
				addressText = "";
			}

			frAgentLocationMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			LatLngBounds.Builder b = new LatLngBounds.Builder();
			if (markers.size() > 0) {
				for (Marker m : markers) {
					b.include(m.getPosition());
				}
				LatLngBounds bounds = b.build();
				CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
						35, 35, 5);
				frAgentLocationMap.animateCamera(cu);
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
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		AgentEntity agentEntity = agentListRoot.agentList.get(position);
		Intent intent = new Intent(getActivity(),
				IndividualAgentDetailsActivity.class);
		intent.putExtra("AGENT_ID", "" + agentEntity._id);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		
		// Getting view from the layout file info_window_layout
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.maps_info_window_show, null);

		// Getting the position from the marker
		LatLng latLng = marker.getPosition();
		AgentLocationEntity entity = null;
		int bitMapCount = 0;

		for (int i = 0; i < agentLocationMapRoot.agentLocationList.size(); i++) {
			if (latLng.latitude == agentLocationMapRoot.agentLocationList
					.get(i).latitude
					&& latLng.longitude == agentLocationMapRoot.agentLocationList
							.get(i).longitude) {
				entity = agentLocationMapRoot.agentLocationList.get(i);
				bitMapCount = i;
				break;
			}
		}

		TextView tvLastUpdateTime = (TextView) v
				.findViewById(R.id.tvLastUpdateTime);

		TextView tvLocationName = (TextView) v
				.findViewById(R.id.tvLocationName);
		TextView tvAgentName = (TextView) v.findViewById(R.id.tvAgentName);
		ImageView ivAgentImage = (ImageView) v.findViewById(R.id.ivAgentImage);

		tvAgentName.setText("Name :" + entity.agentname);
		tvLocationName.setText("Current Location :" + entity.locationname);
		tvLastUpdateTime.setText("Last Update :" + CommonTasks.getRelativeTime(entity.updatetime));
		ivAgentImage.setImageBitmap(CommonTasks.createCircularShape(mapPhotoList.get(bitMapCount)));

		return v;
	}

}
