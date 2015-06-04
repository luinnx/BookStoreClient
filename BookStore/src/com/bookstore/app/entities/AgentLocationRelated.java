package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class AgentLocationRelated {
	@SerializedName("id")
	public int _id;
	@SerializedName("full_name")
	public String full_name;
	@SerializedName("email")
	public String email;
	@SerializedName("mobile_no")
	public String mobile_no;
	@SerializedName("location_name")
	public String location_name;
	@SerializedName("address")
	public String address;
	@SerializedName("pic_url")
	public String pic_url;
	@SerializedName("gcm_id")
	public String gcm_id;
	@SerializedName("mpo_no")
	public String mpo_no;
	@SerializedName("isActive")
	public int isActive;
	@SerializedName("createdate")
	public String create_date;
	@SerializedName("latitude")
	public double latitude;
	@SerializedName("longitude")
	public double longitude;
	@SerializedName("usertype")
	public int usertype;

}
