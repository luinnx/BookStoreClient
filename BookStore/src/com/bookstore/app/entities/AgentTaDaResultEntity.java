package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class AgentTaDaResultEntity {

	@SerializedName("id")
	public int id;
	@SerializedName("agentpic")
	public String agentpic;
	@SerializedName("distance")
	public double distance;
	@SerializedName("status")
	public int status;
	@SerializedName("adminname")
	public String adminname;
	@SerializedName("createdate")
	public long createdate;
	@SerializedName("adminid")
	public int adminid;
	@SerializedName("agentname")
	public String agentname;
	@SerializedName("totalamount")
	public double totalamount;
	
	@SerializedName("startplace")
	public String StartPlace;
	@SerializedName("endplace")
	public String EndPlace;
	

	/*
	 * "agentpic":"/Images/Agent/1.png", "distance":12.00, "status":2,
	 * "adminname":"Mahbub Hasan", "createdate":1432335995000, "adminid":1,
	 * "agentname":"Sajedul Karim", "totalamount":250.00
	 */
}
