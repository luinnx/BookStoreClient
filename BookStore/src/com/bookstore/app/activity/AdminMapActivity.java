package com.bookstore.app.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Window;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentLocationMapRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdminMapActivity extends BookStoreActionBarBase implements IAsynchronousTask, LocationListener{
	
	GoogleMap atmFinderMap;
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog progressDialog;
	double mLatitude=0;
	double mLongitude=0;
	String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    JSONObject jObject;
    ArrayList<Marker> markers;
    AgentLocationMapRoot agentLocationMapRoot=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//hide title bar when opne this page in application
		setContentView(R.layout.agent_location_map_activity);
		Initalization();
	}
	
	private void Initalization() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frAgentLocationMap);
		atmFinderMap = fragment.getMap();
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideProgressBar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object doInBackground() {
		IAdminManager adminManager=new AdminManager();
		return adminManager.getAgentsLocation();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			agentLocationMapRoot=new AgentLocationMapRoot();
			agentLocationMapRoot=(AgentLocationMapRoot) data;
			if(agentLocationMapRoot != null && agentLocationMapRoot.agentLocationList.size()>0){
				LoadMap(agentLocationMapRoot);
			}
		}else{
			
		}
		
	}
	
	private void LoadMap(AgentLocationMapRoot locationMapRoot) {
		/*
		 * This is map function where is shown all ATM Booth location in your redious.
		 * Initialy we use 5000 rediues in your location.
		 */
		// initialize variable of this method.
		String addressText = "";
		markers = new ArrayList<Marker>();
		double defaultLatitude, defaultLongitude;
		try{
			//clear the map before add the marker
			atmFinderMap.clear();
			LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			android.location.Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(loc != null){
				for(int rowIndex = 0; rowIndex<locationMapRoot.agentLocationList.size();rowIndex++){
					// Getting a place from the places list
		            //HashMap<String, String> hmPlace = list.get(rowIndex);		
		            // Getting latitude of the place
		            double lat = locationMapRoot.agentLocationList.get(rowIndex).latitude;
		            // Getting longitude of the place
		            double lng = locationMapRoot.agentLocationList.get(rowIndex).longitude;
		            // create instance of latlng class.
					LatLng Location = new LatLng(lat,lng);
					// Create instance of geocoder that is used for geting address if marker pointer in pressed.
					Geocoder geocoder = new Geocoder(this, Locale.getDefault());
					// get list of address where lat and lang r putted
					List<Address> addresses = geocoder.getFromLocation(Location.latitude,Location.longitude,1);
					// check address are available and get it using for loop and store in address
					if (addresses != null && addresses.size() > 0) {
						Address address = addresses.get(0);
						for (int lineIndex = 0; lineIndex < address.getMaxAddressLineIndex(); lineIndex++) {
							addressText = addressText+ address.getAddressLine(lineIndex)+ ", ";
						}
						addressText = addressText + address.getLocality() + ", " + address.getCountryName();
						markers.add(atmFinderMap.addMarker(new MarkerOptions().position(Location).title(addressText)));
					}
					addressText = "";
				}
				
				atmFinderMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				//LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);
				LatLngBounds.Builder b = new LatLngBounds.Builder();
				if(markers.size() > 0){
					for (Marker m : markers) {
					    b.include(m.getPosition());
					}
					LatLngBounds bounds = b.build();
					CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 35,35,5);
					atmFinderMap.animateCamera(cu);
				}				
			}			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
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

}
