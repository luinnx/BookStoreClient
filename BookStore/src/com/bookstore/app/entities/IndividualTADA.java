package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class IndividualTADA {
	@SerializedName("startplace")
	public String startplace;
	@SerializedName("tadaamount")
	public double tadaamount;
	@SerializedName("distance")
	public double distance;
	@SerializedName("endplace")
	public String endplace;
	@SerializedName("endtime")
	public String endtime;
	@SerializedName("description")
	public String description;
	@SerializedName("starttime")
	public String starttime;
	@SerializedName("vehicelname")
	public String vehicelname;
	@SerializedName("agentfullname")
	public String Agentfull_name;
	@SerializedName("totalamount")
	public double totalamount;
	@SerializedName("tadadate")
	public String tadadate;
	@SerializedName("pic_url")
	public String pic_url;
	@SerializedName("otheramount")
	public double otheramount;
	@SerializedName("gcmid")
	public String gcmid;
	@SerializedName("agentid")
	public int agentid;
}
