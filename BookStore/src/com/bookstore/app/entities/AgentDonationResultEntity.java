package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class AgentDonationResultEntity {
	@SerializedName("amount")
	public double Amount;
	@SerializedName("donatedamount")
	public double approved_amount;
	@SerializedName("comment")
	public String comment;
	@SerializedName("agentfullname")
	public String agent_name;
	@SerializedName("donationdate")
	public long date;
	@SerializedName("donateddate")
	public long donateddate;
	@SerializedName("agentpic")
	public String pic_url;
	@SerializedName("agentid")
	public int agentid;
	@SerializedName("adminid")
	public int adminid;
	@SerializedName("id")
	public int id;
	@SerializedName("gcmid")
	public String gcmid;

}
