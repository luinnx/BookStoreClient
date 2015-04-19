package com.bookstore.app.managers;

import android.util.Log;

import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.JSONfunctions;

public class AgentManager implements IAgent{

	@Override
	public AgentJobListRoot getJobList(int agentId, int job_status,
			int pageIndex) {
		AgentJobListRoot agentJobListRoot = null;
		try{
			agentJobListRoot = (AgentJobListRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().agentJobList, agentId,job_status,pageIndex), AgentJobListRoot.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return agentJobListRoot;
	}

}
