package com.bookstore.app.interfaces;

import org.json.simple.JSONObject;

import com.bookstore.app.entities.AgentDonationEntityRoot;
import com.bookstore.app.entities.AgentInfo;
import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.entities.AgentTaDaResultEntity;
import com.bookstore.app.entities.JobDetails;
import com.bookstore.app.entities.ResponseEntity;
import com.bookstore.app.entities.TaDaListRoot;

import org.json.simple.JSONObject;

public interface IAgent {
	public AgentJobListRoot getJobList(int agentId, int job_status,
			int pageIndex);

	public JobDetails getJobDetails(String jobID);

	public boolean jobSubmit(JSONObject jsonObject);

	public boolean addLocation(int agentid, double latitude, double longitude,
			String locationName);

	public AgentInfo getAgentInformation(int agentID);

	public boolean addDonetion(int agentID, double amount, String comment);

	
	public boolean submitTaDa(String agentID, String date, String startPlace,
			String startTime, String endPlace, String endTime,
			String description, String vehicelName, String distance,
			String amount, String otherAmount,String totalAmount, String status);
	public TaDaListRoot getAllTaDaList(String agentId,int pageIndex);
	public AgentDonationEntityRoot getAllDonation(int agentId, int pageIndex);
	
	public boolean addTada(JSONObject jsonObject);
	public AgentTaDaResultEntity getIndividualTadaResultDetails(String tadaID);
	
	public ResponseEntity addDailyActivity(org.json.JSONObject jsonObject);
}
