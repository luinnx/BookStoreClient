package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AgentDonationEntityRoot {
	@SerializedName("DonationList")
	public ArrayList<AgentDonationEntity> donationList = null;
	
	public AgentDonationEntityRoot(){
		donationList = new ArrayList<>();
	}
}
