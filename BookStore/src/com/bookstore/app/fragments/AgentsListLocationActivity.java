package com.bookstore.app.fragments;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.AgentLocationMapRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class AgentsListLocationActivity extends Fragment implements
		LocationListener, IAsynchronousTask {

	ListView lvAgentList;
	AgentListAdapter adapter;
	AgentListRoot agentListRoot = null;
	GoogleMap frAgentLocationMap;
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog progressDialog;
	double mLatitude = 0;
	double mLongitude = 0;
	String data = "";
	InputStream iStream = null;
	HttpURLConnection urlConnection = null;
	JSONObject jObject;
	ArrayList<Marker> markers;
	AgentLocationMapRoot agentLocationMapRoot = null;
	private static View view;
	ViewGroup parent;

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
		SupportMapFragment fragment = ((SupportMapFragment) getChildFragmentManager()
				.findFragmentById(R.id.fragAgentLocationMap));
		frAgentLocationMap = fragment.getMap();
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
		return adminManager.getAgentsLocation(0);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			@SuppressWarnings("unchecked")
			List<Object> complexList = (List<Object>) data;

			agentListRoot = new AgentListRoot();
			agentListRoot = (AgentListRoot) complexList.get(1);
			adapter = new AgentListAdapter(getActivity(),
					R.layout.agent_list_item, agentListRoot.agentList);
			lvAgentList.setAdapter(adapter);

			agentLocationMapRoot = new AgentLocationMapRoot();
			agentLocationMapRoot = (AgentLocationMapRoot) complexList.get(0);
			if (agentLocationMapRoot != null
					&& agentLocationMapRoot.agentLocationList.size() > 0) {
				LoadMap(agentLocationMapRoot);
			}

		} else {

			CommonTasks.showToast(getActivity(),
					"Can not loading data. Internal server error");

		}

	}

	private void LoadMap(AgentLocationMapRoot locationMapRoot) {
		/*
		 * This is map function where is shown all ATM Booth location in your
		 * redious. Initialy we use 5000 rediues in your location.
		 */
		// initialize variable of this method.
		String addressText = "";
		markers = new ArrayList<Marker>();
		double defaultLatitude, defaultLongitude;
		try {
			// clear the map before add the marker
			frAgentLocationMap.clear();
			LocationManager locationManager = (LocationManager) getActivity()
					.getSystemService(getActivity().LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			android.location.Location loc = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				for (int rowIndex = 0; rowIndex < locationMapRoot.agentLocationList
						.size(); rowIndex++) {
					// Getting a place from the places list
					// HashMap<String, String> hmPlace = list.get(rowIndex);
					// Getting latitude of the place
					double lat = locationMapRoot.agentLocationList
							.get(rowIndex).latitude;
					// Getting longitude of the place
					double lng = locationMapRoot.agentLocationList
							.get(rowIndex).longitude;
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
						addressText = addressText + address.getLocality()
								+ ", " + address.getCountryName();

						View marker = ((LayoutInflater) getActivity()
								.getSystemService(
										Context.LAYOUT_INFLATER_SERVICE))
								.inflate(R.layout.custom_marker_layout, null);
						TextView numTxt = (TextView) marker
								.findViewById(R.id.tvInfoText);
						numTxt.setText("Sajedul Karim");

						markers.add(frAgentLocationMap.addMarker(new MarkerOptions()
								.position(Location)
								.title(addressText)
								.snippet("Address")
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
					CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(
							bounds, 35, 35, 5);
					frAgentLocationMap.animateCamera(cu);
				}
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

}
