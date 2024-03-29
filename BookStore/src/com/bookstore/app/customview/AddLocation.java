package com.bookstore.app.customview;

import java.util.List;
import java.util.Locale;

import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class AddLocation extends AsyncTask<Location, Void, Object>{
	Context context;

	public AddLocation(Context _context) {
		context = _context;
	}

	@Override
	protected Object doInBackground(Location... params) {
		
		if(!CommonTasks.isOnline(context))
			return null;	
		
		
		IAgent agent = new AgentManager();
		return agent.addLocation(Integer.parseInt(CommonTasks.getPreferences(context, CommonConstraints.USER_ID)),
				params[0].getLatitude(), 
				params[0].getLongitude(),
				getLocationName(params[0].getLatitude(), params[0].getLongitude()));
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if(result != null){
			Boolean agentLocationResult = (Boolean) result;
			if(agentLocationResult){
				Log.d("LS", "Location Update Successfully");}
			else{
				Log.d("LS", "Location Update Failed");
			}
		}
	}
	
	private String getLocationName(double lat, double lang){
		String lcoationAddress = "";
		try{
			LatLng Location = new LatLng(lat, lang);
			Geocoder geocoder = new Geocoder(context, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(
					Location.latitude, Location.longitude, 1);
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				for (int lineIndex = 0; lineIndex < address
						.getMaxAddressLineIndex(); lineIndex++) {
					lcoationAddress = lcoationAddress
							+ address.getAddressLine(lineIndex) + ", ";
				}
				lcoationAddress = lcoationAddress + address.getLocality()
						+ ", " + address.getCountryName();
			}
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}		
		return lcoationAddress;
	}

}
