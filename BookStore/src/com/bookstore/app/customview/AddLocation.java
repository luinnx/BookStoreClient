package com.bookstore.app.customview;

import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

public class AddLocation extends AsyncTask<Location, Void, Object>{
	Context context;

	public AddLocation(Context _context) {
		context = _context;
	}

	@Override
	protected Object doInBackground(Location... params) {
		IAgent agent = new AgentManager();
		return agent.addLocation(Integer.parseInt(CommonTasks.getPreferences(context, CommonConstraints.USER_ID)),
				params[0].getLatitude(), 
				params[0].getLongitude());
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if(result != null){
			Boolean agentLocationResult = (Boolean) result;
			if(agentLocationResult)
				CommonTasks.showToast(context, "Location Update Successfully");
		}
	}

}
