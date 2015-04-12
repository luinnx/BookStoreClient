package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class JobEntity {
	@SerializedName("jobid")
	public int jobid;
	@SerializedName("bookname")
	public String bookname;
	@SerializedName("quantity")
	public int quantity;
	@SerializedName("teacherinstitute")
	public String teacherinstitute;
	@SerializedName("bookname")
	public String bookImage;
	@SerializedName("agentname")
	public String agentname;
	@SerializedName("agentaddress")
	public String agentaddress;
}