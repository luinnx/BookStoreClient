package com.bookstore.app.managers;

import java.net.URLEncoder;

import org.json.simple.JSONObject;

import android.util.Log;

import com.bookstore.app.entities.AgentDonationEntityRoot;
import com.bookstore.app.entities.AgentDonationResultEntity;
import com.bookstore.app.entities.AgentInfo;
import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.entities.AgentTaDaResultEntity;
import com.bookstore.app.entities.JobDetails;
import com.bookstore.app.entities.TaDaListRoot;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.JSONfunctions;

public class AgentManager implements IAgent {

	@Override
	public AgentJobListRoot getJobList(int agentId, int job_status,
			int pageIndex) {
		AgentJobListRoot agentJobListRoot = null;
		try {
			agentJobListRoot = (AgentJobListRoot) JSONfunctions
					.retrieveDataFromStream(String.format(
							CommonUrls.getInstance().agentJobList, agentId,
							job_status, pageIndex), AgentJobListRoot.class);
		} catch (Exception ex) {
			Log.e("BS", ex.getMessage());
		}
		return agentJobListRoot;
	}

	@Override
	public JobDetails getJobDetails(String jobID) {
		JobDetails jobDetails = null;
		try {
			jobDetails = (JobDetails) JSONfunctions.retrieveDataFromStream(
					String.format(CommonUrls.getInstance().agentJobDetails,
							jobID), JobDetails.class);
		} catch (Exception ex) {
			Log.e("BS", ex.getMessage());
		}
		return jobDetails;
	}

	@Override
	public boolean jobSubmit(JSONObject jsonObject) {
		Boolean result = false;
		try {
			result = (Boolean) JSONfunctions.retrieveDataFromJsonPostURL(CommonUrls.getInstance().jobSubimt, jsonObject, Boolean.class);
		} catch (Exception ex) {
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

	@Override
	public boolean addLocation(int agentid, double latitude, double longitude,
			String locationName) {
		boolean result = false;
		try {
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String
					.format(CommonUrls.getInstance().agentLocation, agentid,
							latitude, longitude, URLEncoder.encode(
									locationName,
									CommonConstraints.EncodingCode)),
					Boolean.class);
		} catch (Exception ex) {
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

	@Override
	public AgentInfo getAgentInformation(int agentID) {
		AgentInfo agentInfo = null;
		try {
			agentInfo = (AgentInfo) JSONfunctions.retrieveDataFromStream(String
					.format(CommonUrls.getInstance().getAgentInformation,
							agentID), AgentInfo.class);
		} catch (Exception ex) {
			Log.e("BS", ex.getMessage());
		}
		return agentInfo;
	}

	@Override
	public boolean addDonetion(int agentID, double amount, String comment) {
		boolean result = false;
		try {
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String
					.format(CommonUrls.getInstance().agentDonation, agentID,
							amount, URLEncoder.encode(comment,
									CommonConstraints.EncodingCode)),
					Boolean.class);
		} catch (Exception ex) {
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

	@Override
	public boolean submitTaDa(String agentID, String date, String startPlace,
			String startTime, String endPlace, String endTime,
			String description, String vehicelName, String distance,
			String amount, String otherAmount,String totalAmount, String status) {
		boolean result = false;

		result = (Boolean) JSONfunctions.retrieveDataFromStream(String.format(
				CommonUrls.getInstance().addTada, agentID, date,
				startPlace, startTime, endPlace, endTime, description,
				vehicelName, distance, amount,otherAmount, totalAmount, status),
				Boolean.class);

		return result;
	}

	@Override
	public TaDaListRoot getAllTaDaList(String agentId,int pageIndex) {
		TaDaListRoot listRoot=null;
		try{
			listRoot=(TaDaListRoot) JSONfunctions.retrieveDataFromStream(String
					.format(CommonUrls.getInstance().agent_tada_list,agentId,
							pageIndex), TaDaListRoot.class);
		}catch(Exception exception){
			Log.e("BS", exception.getMessage());
		}
		
		return listRoot;
	}

	@Override
	public boolean addTada(JSONObject jsonObject) {
		boolean result = false;
		try{
			result = (Boolean) JSONfunctions.retrieveDataFromJsonPostURL(CommonUrls.getInstance().addTada, jsonObject, Boolean.class);
		}catch(Exception ex){
			Log.e("BSA", ex.getMessage());
		}
		return result;
	}

	@Override
	public AgentTaDaResultEntity getIndividualTadaResultDetails(String tadaID) {
		AgentTaDaResultEntity donationEntity=null;
		donationEntity = (AgentTaDaResultEntity) JSONfunctions.retrieveDataFromStream(String
				.format(CommonUrls.getInstance().get_individual_tada_details, tadaID),
				AgentTaDaResultEntity.class);
		return donationEntity;
	}

	@Override
	public AgentDonationEntityRoot getAllDonation(int agentId, int pageIndex) {
		AgentDonationEntityRoot listRoot=null;
		try{
			listRoot=(AgentDonationEntityRoot) JSONfunctions.retrieveDataFromStream(String
					.format(CommonUrls.getInstance().agent_donation_list,agentId,
							pageIndex), AgentDonationEntityRoot.class);
		}catch(Exception exception){
			Log.e("BS", exception.getMessage());
		}		
		return listRoot;
	}

}
