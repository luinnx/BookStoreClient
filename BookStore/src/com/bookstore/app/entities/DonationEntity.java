package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class DonationEntity {
	@SerializedName("amount")
	public double Amount;
	@SerializedName("comment")
	public String Comment;
	@SerializedName("full_name")
	public String agent_name;
	@SerializedName("donationdate")
	public String date;
	@SerializedName("pic_url")
	public String pic_url;
}
