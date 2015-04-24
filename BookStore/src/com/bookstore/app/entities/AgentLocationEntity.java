package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class AgentLocationEntity {
	@SerializedName("agentid")
	public int agentid;
	@SerializedName("locationname")
	public String locationname;
	@SerializedName("latitude")
	public double latitude;
	@SerializedName("longitude")
	public double longitude;
	@SerializedName("agentname")
	public String agentname;
	@SerializedName("id")
	public int id;
	@SerializedName("updatetime")
	public long updatetime;
	@SerializedName("agentpicurl")
	public String agentpicurl;

}
