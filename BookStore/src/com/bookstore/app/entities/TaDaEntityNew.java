package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class TaDaEntityNew {
	@SerializedName("agentID")
	public int agentID;
	@SerializedName("date")
	public String date;
	@SerializedName("startPlace")
	public String startPlace;
	@SerializedName("startTime")
	public String startTime;
	@SerializedName("endPlace")
	public String endPlace;
	@SerializedName("endTime")
	public String endTime;
	@SerializedName("description")
	public String description;
	@SerializedName("vehicelName")
	public String vehicelName;
	@SerializedName("distance")
	public double distance;
	@SerializedName("amount")
	public double amount;
	@SerializedName("otherAmount")
	public double otherAmount;
	@SerializedName("totalAmount")
	public double totalAmount;
	@SerializedName("status")
	public int status;

}
